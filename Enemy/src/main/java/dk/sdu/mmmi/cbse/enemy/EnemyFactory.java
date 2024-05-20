package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

import java.util.Random;

public class EnemyFactory {
    private static Random random = new Random();

    public static Entity createEnemyShip (GameData gameData) {
        Entity enemyShip = new Enemy(1);

        enemyShip.setPolygonCoordinates(-5,-5,10,0,-5,5);
        enemyShip.setX(random.nextInt(gameData.getDisplayWidth()));  // Set a random starting X position within display width
        enemyShip.setY(random.nextInt(gameData.getDisplayHeight()));
        enemyShip.setRadius(2);
        return enemyShip;
    }
}
