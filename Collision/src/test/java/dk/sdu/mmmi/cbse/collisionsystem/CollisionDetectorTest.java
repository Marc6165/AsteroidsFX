package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IDamageable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CollisionDetectorTest {

    private CollisionDetector collisionDetector;
    private GameData gameData;
    private World world;
    private Entity entity1, entity2;
    private IDamageable damageable1, damageable2;

    @BeforeEach
    void setUp() {
        collisionDetector = new CollisionDetector();
        gameData = new GameData();
        world = mock(World.class);
        entity1 = mock(Entity.class, Mockito.withSettings().extraInterfaces(IDamageable.class));
        entity2 = mock(Entity.class, Mockito.withSettings().extraInterfaces(IDamageable.class));
        damageable1 = (IDamageable) entity1;
        damageable2 = (IDamageable) entity2;

        when(entity1.getX()).thenReturn(100.0);
        when(entity1.getY()).thenReturn(100.0);
        when(entity1.getRadius()).thenReturn(10.0f);

        when(entity2.getX()).thenReturn(110.0); // Close enough to collide
        when(entity2.getY()).thenReturn(110.0);
        when(entity2.getRadius()).thenReturn(10.0f);
    }

    @Test
    void testIntersectsTrue() {
        assertTrue(collisionDetector.intersects(entity1, entity2), "Entities should intersect");
    }

    @Test
    void testIntersectsFalse() {
        when(entity2.getX()).thenReturn(200.0); // Far enough not to collide
        when(entity2.getY()).thenReturn(200.0);
        assertFalse(collisionDetector.intersects(entity1, entity2), "Entities should not intersect");
    }

    @Test
    void testProcessWithCollision() {
        when(world.getEntities()).thenReturn(Arrays.asList(entity1, entity2));
        collisionDetector.process(gameData, world);
        verify(damageable1).takeDamage(1);
        verify(damageable2).takeDamage(1);
    }

    @Test
    void testProcessNoCollision() {
        when(entity2.getX()).thenReturn(200.0);
        when(entity2.getY()).thenReturn(200.0);
        when(world.getEntities()).thenReturn(Arrays.asList(entity1, entity2));
        collisionDetector.process(gameData, world);
        verify(damageable1, never()).takeDamage(anyInt());
        verify(damageable2, never()).takeDamage(anyInt());
    }
}


