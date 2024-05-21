package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IDamageable;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.Collection;
import java.util.Random;
import java.util.ServiceLoader;

import static java.util.stream.Collectors.toList;


public class EnemyControlSystem implements IEntityProcessingService {

    private int lastSpawnTime = 0;
    private static final long SPAWN_INTERVAL = 20;
    private static final int MAX_ENEMIES = 5;
    private static final int MOVEMENT_SPEED = 2;
    private static final int WINDOW_PADDING = 30;
    private static final int ROTATION_CHANGE = 10;

    @Override
    public void process(GameData gameData, World world) {
        manageEnemySpawning(gameData, world);
        for (Entity enemy : world.getEntities(Enemy.class)) {
            moveEnemy(enemy);
            keepEnemyWithinBounds(enemy, gameData);
            shootBullets(enemy, world, gameData);
            if (enemy instanceof IDamageable && ((IDamageable) enemy).getHitPoints() <= 0) {
                world.removeEntity(enemy);
            }
        }
    }

    private void manageEnemySpawning(GameData gameData, World world) {
        int currentTime = gameData.getGameTime();
        System.out.println("Time since last spawn: " + (currentTime - lastSpawnTime) + " s");
        System.out.println("Current enemy count: " + world.getEntities(Enemy.class).size());
        if (currentTime - lastSpawnTime >= SPAWN_INTERVAL && world.getEntities(Enemy.class).size() < MAX_ENEMIES) {
            Entity enemy = EnemyFactory.createEnemyShip(gameData);
            lastSpawnTime = currentTime;
            world.addEntity(enemy);
        }
    }

    private void moveEnemy(Entity enemy) {
        Random random = new Random();
        int randomInt = random.nextInt(20) + 1;
        if (randomInt < 5) {
            enemy.setRotation(enemy.getRotation() - ROTATION_CHANGE);
        } else if (randomInt < 10) {
            enemy.setRotation(enemy.getRotation() + ROTATION_CHANGE);
        } else if (randomInt < 15) {
            enemy.setRotation(enemy.getRotation());
        }

        double changeX = Math.cos(Math.toRadians(enemy.getRotation()));
        double changeY = Math.sin(Math.toRadians(enemy.getRotation()));

        double movementSpeed = (randomInt > 14) ? MOVEMENT_SPEED : 1;
        enemy.setX(enemy.getX() + changeX * movementSpeed);
        enemy.setY(enemy.getY() + changeY * movementSpeed);
    }

    private void keepEnemyWithinBounds(Entity enemy, GameData gameData) {
        if (enemy.getX() < WINDOW_PADDING || enemy.getX() > gameData.getDisplayWidth() - WINDOW_PADDING) {
            enemy.setRotation(enemy.getRotation() - ROTATION_CHANGE);
            enemy.setX(Math.max(WINDOW_PADDING, Math.min(enemy.getX(), gameData.getDisplayWidth() - WINDOW_PADDING)));
        }

        if (enemy.getY() < WINDOW_PADDING || enemy.getY() > gameData.getDisplayHeight() - WINDOW_PADDING) {
            enemy.setRotation(enemy.getRotation() - ROTATION_CHANGE);
            enemy.setY(Math.max(WINDOW_PADDING, Math.min(enemy.getY(), gameData.getDisplayHeight() - WINDOW_PADDING)));
        }
    }

    private void shootBullets(Entity enemy, World world, GameData gameData) {
        Random random = new Random();
        int randomInt = random.nextInt(100) + 1;
        if (randomInt < 5) {
            Collection<? extends BulletSPI> bulletSPIs = getBulletSPIs();
            if (!bulletSPIs.isEmpty()) {
                BulletSPI bulletSPI = bulletSPIs.stream().findFirst().get();
                world.addEntity(bulletSPI.createBullet(enemy, gameData));
            }
        }
    }
        private Collection<? extends BulletSPI> getBulletSPIs() {
        return ServiceLoader.load(BulletSPI.class).stream().map(ServiceLoader.Provider::get).collect(toList());
    }
}
