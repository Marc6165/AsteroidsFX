package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;

import java.util.Random;

public class AsteroidFactory {
    private static Random random = new Random();

    public static Entity createAsteroid(GameData gameData) {
        int size = random.nextInt(15) + 5;
        int hitPoints = size * 3;
        Entity asteroid = new Asteroid(size, hitPoints);

        // Calculate and set the coordinates for a hexagon
        int numSides = 6;  // A hexagon has 6 sides
        double[] xPoints = new double[numSides];
        double[] yPoints = new double[numSides];

        for (int i = 0; i < numSides; i++) {
            // Calculate the angle to the point (60 degrees apart for hexagon)
            double angle = Math.toRadians(60 * i);
            xPoints[i] = size * Math.cos(angle);
            yPoints[i] = size * Math.sin(angle);
        }

        // Assuming `setPolygonCoordinates` can take an array or multiple individual coordinates
        asteroid.setPolygonCoordinates(
                xPoints[0], yPoints[0],
                xPoints[1], yPoints[1],
                xPoints[2], yPoints[2],
                xPoints[3], yPoints[3],
                xPoints[4], yPoints[4],
                xPoints[5], yPoints[5]
        );

        asteroid.setX(random.nextInt(gameData.getDisplayWidth()));  // Set a random starting X position within display width
        asteroid.setY(random.nextInt(gameData.getDisplayHeight())); // Set a random starting Y position within display height
        asteroid.setRadius(size);
        asteroid.setRotation(random.nextInt(360));  // Set a random rotation angle

        return asteroid;
    }

}
