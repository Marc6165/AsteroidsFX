package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IDamageable;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Random;

public class AsteroidProcessor implements IEntityProcessingService {

    private IAsteroidSplitter asteroidSplitter = new AsteroidSplitterImpl();

    @Override
    public void process(GameData gameData, World world) {

        for (Entity asteroid : world.getEntities(Asteroid.class)) {
            double changeX = Math.cos(Math.toRadians(asteroid.getRotation()));
            double changeY = Math.sin(Math.toRadians(asteroid.getRotation()));

            asteroid.setX(asteroid.getX() + changeX * 1);
            asteroid.setY(asteroid.getY() + changeY * 1);

            if (asteroid.getX() < 0) {
                asteroid.setX(gameData.getDisplayWidth());
            }
            if (asteroid.getX() > gameData.getDisplayWidth()) {
                asteroid.setX(0);
            }
            if (asteroid.getY() < 0) {
                asteroid.setY(gameData.getDisplayHeight());
            }
            if (asteroid.getY() > gameData.getDisplayHeight()) {
                asteroid.setY(0);
            }
            if (asteroid instanceof IDamageable && ((IDamageable) asteroid).getHitPoints() <= 0) {
                asteroidSplitter.createSplitAsteroid(asteroid, world);
                world.removeEntity(asteroid);
            }

        }
        asteroidFrequency(gameData, world);
    }

    private void asteroidFrequency(GameData gameData, World world) {
        int currentCount = world.getEntities(Asteroid.class).size();
        int gameTime = gameData.getGameTime(); // Assuming gameTime is managed and updated elsewhere in GameData
        int additionalAsteroids = (gameTime / 20) * 3; // Increase asteroid count every 20 seconds
        int targetAsteroidCount = 10 + additionalAsteroids; // Start with 10 and add more over time
        int neededAsteroids = targetAsteroidCount - currentCount;

        for (int i = 0; i < neededAsteroids; i++) {
            Entity asteroid = AsteroidFactory.createAsteroid(gameData);
            world.addEntity(asteroid);
        }
    }

    public void setAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = asteroidSplitter;
    }

    public void removeAsteroidSplitter(IAsteroidSplitter asteroidSplitter) {
        this.asteroidSplitter = null;
    }
}
