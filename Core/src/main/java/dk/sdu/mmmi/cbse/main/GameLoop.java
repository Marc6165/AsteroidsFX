package dk.sdu.mmmi.cbse.main;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;
import javafx.animation.AnimationTimer;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;
import javafx.scene.Scene;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Polygon;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class GameLoop extends AnimationTimer {

    private final GameData gameData = new GameData();
    private final World world = new World();
    private final List<IEntityProcessingService> entityProcessors;
    private final List<IPostEntityProcessingService> postProcessors;
    private final List<IGamePluginService> gamePluginServices;
    private final Map<Entity, Polygon> polygons = new ConcurrentHashMap<>();
    private final Pane gameWindow = new Pane();

    private final URI scoreEndpointUrl;
    private final HttpClient requestClient;
    private Text scoreCounter;


    @Autowired
    public GameLoop(List<IEntityProcessingService> entityProcessors, List<IPostEntityProcessingService> postProcessors, List<IGamePluginService> gamePluginServices) {
        this.entityProcessors = entityProcessors;
        this.postProcessors = postProcessors;
        this.gamePluginServices = gamePluginServices;
        try {
            this.scoreEndpointUrl = new URI("http://localhost:8080/");
            requestClient = HttpClient.newHttpClient();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }    }

    public void startGame(Stage window) throws Exception {
        scoreCounter = new Text(10, 20, "Destroyed asteroids: 0");
        gameWindow.setPrefSize(gameData.getDisplayWidth(), gameData.getDisplayHeight());
        gameWindow.getChildren().add(scoreCounter);

        Scene scene = new Scene(gameWindow);
        scene.setOnKeyPressed(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, true);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, true);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, true);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, true);
            }
        });
        scene.setOnKeyReleased(event -> {
            if (event.getCode().equals(KeyCode.LEFT)) {
                gameData.getKeys().setKey(GameKeys.LEFT, false);
            }
            if (event.getCode().equals(KeyCode.RIGHT)) {
                gameData.getKeys().setKey(GameKeys.RIGHT, false);
            }
            if (event.getCode().equals(KeyCode.UP)) {
                gameData.getKeys().setKey(GameKeys.UP, false);
            }
            if (event.getCode().equals(KeyCode.SPACE)) {
                gameData.getKeys().setKey(GameKeys.SPACE, false);
            }

        });

        for (IGamePluginService plugin : gamePluginServices) {
            plugin.start(gameData, world);
        }
        for (Entity entity : world.getEntities()) {
            Polygon polygon = new Polygon(entity.getPolygonCoordinates());
            polygons.put(entity, polygon);
            gameWindow.getChildren().add(polygon);
        }

        render();

        window.setScene(scene);
        window.setTitle("ASTEROIDS");
        window.show();
    }

    @Override
    public void handle(long now) {
        update();
        draw();
        gameData.getKeys().update();
    }

    public void render() {
        new AnimationTimer() {
            @Override
            public void handle(long now) {
                update();
                draw();
                gameData.getKeys().update();
            }
        }.start();
    }
    /**
     * Removes deleted entities from the game window and the polygons map.
     */
    public void cleanUpEntities() {
        Iterator<Map.Entry<Entity, Polygon>> iterator = polygons.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Entity, Polygon> entry = iterator.next();
            Entity entity = entry.getKey();
            Polygon polygon = entry.getValue();
            if (!world.getEntities().contains(entity)) {
                gameWindow.getChildren().remove(polygon);
                iterator.remove();
                if (entity instanceof Asteroid ) {
                    addScoreBy(((Asteroid) entity).getSize());
                    updateScoreCounter();
                    System.out.println("Score: " + getScore());
                    System.out.println("Score added: " + ((Asteroid) entity).getSize());
                }
            }
        }
    }

    private void update() {
        for (IEntityProcessingService entityProcessorService : entityProcessors) {
            entityProcessorService.process(gameData, world);
        }

        for (IPostEntityProcessingService postEntityProcessorService : postProcessors) {
            postEntityProcessorService.process(gameData, world);
        }
        cleanUpEntities();
        updateScoreCounter();
    }

    private void draw() {
        for (Entity entity : world.getEntities()) {
            Polygon polygon = polygons.get(entity);
            if (polygon == null) {
                polygon = new Polygon(entity.getPolygonCoordinates());
                polygons.put(entity, polygon);
                gameWindow.getChildren().add(polygon);
            }
            polygon.setTranslateX(entity.getX());
            polygon.setTranslateY(entity.getY());
            polygon.setRotate(entity.getRotation());
        }
    }

    private long getScore() {
        return requestClient.sendAsync(java.net.http.HttpRequest.newBuilder()
                        .uri(scoreEndpointUrl.resolve("score"))
                        .GET()
                        .build(), java.net.http.HttpResponse.BodyHandlers.ofString())
                .thenApply(java.net.http.HttpResponse::body)
                .thenApply(Long::parseLong)
                .join();
    }

    private void incrementScore() {
        requestClient.sendAsync(java.net.http.HttpRequest.newBuilder()
                        .uri(scoreEndpointUrl.resolve("score/increment"))
                        .PUT(java.net.http.HttpRequest.BodyPublishers.noBody())
                        .build(), java.net.http.HttpResponse.BodyHandlers.discarding())
                .join();
    }

    private void addScoreBy(long value) {
        requestClient.sendAsync(java.net.http.HttpRequest.newBuilder()
                        .uri(scoreEndpointUrl.resolve("score/add/" + value))
                        .PUT(java.net.http.HttpRequest.BodyPublishers.noBody())
                        .build(), java.net.http.HttpResponse.BodyHandlers.discarding())
                .join();
    }

    private void updateScoreCounter() {
        scoreCounter.setText("Score: " + getScore());
    }

    public List<IGamePluginService> getGamePluginServices() {
        return gamePluginServices;
    }

    public List<IEntityProcessingService> getEntityProcessingServices() {
        return entityProcessors;
    }

    public List<IPostEntityProcessingService> getPostEntityProcessingServices() {
        return postProcessors;
    }
}
