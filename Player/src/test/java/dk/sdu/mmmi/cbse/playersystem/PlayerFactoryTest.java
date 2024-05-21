package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PlayerFactoryTest {

    private GameData gameData;

    @BeforeEach
    void setUp() {
        gameData = mock(GameData.class);
        when(gameData.getDisplayHeight()).thenReturn(800);
        when(gameData.getDisplayWidth()).thenReturn(600);
    }

    @Test
    public void testCreatePlayerShip() {
        Entity playerShip = PlayerFactory.createPlayerShip(gameData);

        // Check if player is created with correct coordinates
        assertEquals(400, playerShip.getX(), 0.1, "X coordinate should be half of display width");
        assertEquals(300, playerShip.getY(), 0.1, "Y coordinate should be half of display height");

        // Check if radius is set correctly
        assertEquals(2, playerShip.getRadius(), 0.1, "Radius should be set to 2");

        // Check if polygon coordinates are set correctly
        double[] expectedCoords = new double[]{-5, -5, 10, 0, -5, 5};
        double[] actualCoords = playerShip.getPolygonCoordinates();
        assertEquals(expectedCoords.length, actualCoords.length, "Polygon coordinates length should match");
        for (int i = 0; i < expectedCoords.length; i++) {
            assertEquals(expectedCoords[i], actualCoords[i], 0.1, "Polygon coordinate mismatch at index " + i);
        }
    }
}
