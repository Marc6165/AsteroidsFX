package dk.sdu.mmmi.cbse.asteroid;

import dk.sdu.mmmi.cbse.common.asteroids.Asteroid;
import dk.sdu.mmmi.cbse.common.asteroids.IAsteroidSplitter;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.World;

import java.util.Random;

public class AsteroidSplitterImpl implements IAsteroidSplitter {

    private Random random = new Random();

    private final int MIN_SPLIT_SIZE = 4;
    private final int MIN_ASTEROID_SIZE_TO_SPLIT = 8;

    @Override
    public void createSplitAsteroid(Entity asteroid, World world) {
        if (!(asteroid instanceof Asteroid)) return;
        Asteroid original = (Asteroid) asteroid;
        int originalSize = original.getSize();
        if (originalSize <= MIN_ASTEROID_SIZE_TO_SPLIT) return;  // Ensure original is large enough to split

        int numberOfFragments = Math.max(2, originalSize / 10);  // Adjust this to control fragment count
        int remainingSize = originalSize;
        int sizePerFragment = Math.max(MIN_ASTEROID_SIZE_TO_SPLIT, originalSize / numberOfFragments);

        for (int i = 0; i < numberOfFragments; i++) {
            int fragmentSize = Math.max(MIN_SPLIT_SIZE, remainingSize / (numberOfFragments - i));
            remainingSize -= fragmentSize;

            if (i == numberOfFragments - 1) {
                // Ensure last fragment takes up any remaining size not perfectly divisible
                fragmentSize = remainingSize + fragmentSize;
            }

            // Create and add the fragment if it meets the minimum size requirement
            if (fragmentSize >= MIN_SPLIT_SIZE) {
                Entity fragment = createAsteroidFragment(original, fragmentSize);
                world.addEntity(fragment);
            }
        }
    }

    private Entity createAsteroidFragment(Asteroid original, int size) {
        Entity asteroid = new Asteroid(size, size * 3);

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

        double angle = random.nextDouble() * 360;
        double distance = random.nextDouble() * 5; // Randomize the starting distance
        double changeX = Math.cos(Math.toRadians(angle)) * distance;
        double changeY = Math.sin(Math.toRadians(angle)) * distance;

        asteroid.setRadius(size);

        asteroid.setX(original.getX() + changeX);
        asteroid.setY(original.getY() + changeY);
        asteroid.setRotation(random.nextDouble() * 360);

        return asteroid;
    }
}


