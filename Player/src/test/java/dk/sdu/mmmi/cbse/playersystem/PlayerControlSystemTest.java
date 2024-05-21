package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.GameKeys;
import dk.sdu.mmmi.cbse.common.data.World;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class PlayerControlSystemTest {

    @InjectMocks
    private PlayerControlSystem playerControlSystem;

    @Mock
    private GameData gameData;

    @Mock
    private World world;

    @Mock
    private GameKeys gameKeys;

    private Entity player;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        player = mock(Entity.class);

        // Ensure the gameData returns the gameKeys
        when(gameData.getKeys()).thenReturn(gameKeys);

        // Ensure the world returns the player entity
        when(world.getEntities(Player.class)).thenReturn(Collections.singletonList(player));
    }

    @Test
    void testPlayerMoveLeft() {
        when(gameKeys.isDown(GameKeys.LEFT)).thenReturn(true);
        when(player.getRotation()).thenReturn(0.0);
        doNothing().when(player).setRotation(anyDouble());

        playerControlSystem.process(gameData, world);

        ArgumentCaptor<Double> captorRotation = ArgumentCaptor.forClass(Double.class);
        verify(player).setRotation(captorRotation.capture());

        assertEquals(-5.0, captorRotation.getValue());
    }

    @Test
    void testPlayerMoveRight() {
        when(gameKeys.isDown(GameKeys.RIGHT)).thenReturn(true);
        when(player.getRotation()).thenReturn(0.0);
        doNothing().when(player).setRotation(anyDouble());

        playerControlSystem.process(gameData, world);

        ArgumentCaptor<Double> captorRotation = ArgumentCaptor.forClass(Double.class);
        verify(player).setRotation(captorRotation.capture());

        assertEquals(5.0, captorRotation.getValue());
    }

    @Test
    void testPlayerMoveUp() {
        when(gameKeys.isDown(GameKeys.UP)).thenReturn(true);
        when(player.getX()).thenReturn(50.0);
        when(player.getY()).thenReturn(50.0);
        when(player.getRotation()).thenReturn(0.0);
        doNothing().when(player).setX(anyDouble());
        doNothing().when(player).setY(anyDouble());

        playerControlSystem.process(gameData, world);

        ArgumentCaptor<Double> captorX = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Double> captorY = ArgumentCaptor.forClass(Double.class);
        verify(player, atLeastOnce()).setX(captorX.capture());
        verify(player, atLeastOnce()).setY(captorY.capture());

        List<Double> allX = captorX.getAllValues();
        List<Double> allY = captorY.getAllValues();

        assertTrue(allX.contains(51.0)); // Check if one of the captured values is the expected one
        assertTrue(allY.contains(50.0)); // Check if one of the captured values is the expected one
    }

    @Test
    void testPlayerMoveUpWithRotation() {
        when(gameKeys.isDown(GameKeys.UP)).thenReturn(true);
        when(player.getX()).thenReturn(50.0);
        when(player.getY()).thenReturn(50.0);
        when(player.getRotation()).thenReturn(90.0);
        doNothing().when(player).setX(anyDouble());
        doNothing().when(player).setY(anyDouble());

        playerControlSystem.process(gameData, world);

        ArgumentCaptor<Double> captorX = ArgumentCaptor.forClass(Double.class);
        ArgumentCaptor<Double> captorY = ArgumentCaptor.forClass(Double.class);
        verify(player, atLeastOnce()).setX(captorX.capture());
        verify(player, atLeastOnce()).setY(captorY.capture());

        List<Double> allX = captorX.getAllValues();
        List<Double> allY = captorY.getAllValues();

        assertTrue(allX.contains(50.0)); // Check if one of the captured values is the expected one
        assertTrue(allY.contains(51.0)); // Check if one of the captured values is the expected one
    }


    @Test
    void testPlayerStayWithinBounds() {
        when(gameData.getDisplayWidth()).thenReturn(100);
        when(gameData.getDisplayHeight()).thenReturn(100);

        // Test left boundary
        when(player.getX()).thenReturn(-10.0);
        playerControlSystem.process(gameData, world);
        verify(player, atLeastOnce()).setX(1.0);

        // Test right boundary
        when(player.getX()).thenReturn(110.0);
        playerControlSystem.process(gameData, world);
        verify(player, atLeastOnce()).setX(99.0);

        // Test bottom boundary
        when(player.getY()).thenReturn(-10.0);
        playerControlSystem.process(gameData, world);
        verify(player, atLeastOnce()).setY(1.0);

        // Test top boundary
        when(player.getY()).thenReturn(110.0);
        playerControlSystem.process(gameData, world);
        verify(player, atLeastOnce()).setY(99.0);
    }
}
