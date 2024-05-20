package dk.sdu.mmmi.cbse.collisionsystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IDamageable;
import dk.sdu.mmmi.cbse.common.services.IPostEntityProcessingService;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CollisionDetector implements IPostEntityProcessingService {

    public CollisionDetector() {
    }


    @Override
    public void process(GameData gameData, World world) {
        List<Entity> entities = new ArrayList<>(world.getEntities());
        for (int i = 0; i < entities.size(); i++) {
            Entity entity1 = entities.get(i);
            for (int j = i + 1; j < entities.size(); j++) {
                Entity entity2 = entities.get(j);
                if (intersects(entity1, entity2)) {
                    if (entity1 instanceof IDamageable && entity2 instanceof IDamageable) {
                        dealDamage((IDamageable) entity1, (IDamageable) entity2);
                    }
                }
            }
        }
    }

    private void dealDamage(IDamageable entity1, IDamageable entity2) {
        entity1.takeDamage(1);
        entity2.takeDamage(1);
    }



    public Boolean intersects(Entity entity1, Entity entity2) {
        float dx = (float) entity1.getX() - (float) entity2.getX();
        float dy = (float) entity1.getY() - (float) entity2.getY();
        float distance = (float) Math.sqrt(dx * dx + dy * dy);
        return distance < entity1.getRadius() + entity2.getRadius();
    }
}