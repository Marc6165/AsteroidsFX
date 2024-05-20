package dk.sdu.mmmi.cbse.bulletsystem;

import dk.sdu.mmmi.cbse.common.bullet.Bullet;
import dk.sdu.mmmi.cbse.common.bullet.BulletSPI;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IDamageable;
import dk.sdu.mmmi.cbse.common.services.IEntityProcessingService;

import java.util.ArrayList;
import java.util.List;

public class BulletControlSystem implements IEntityProcessingService, BulletSPI {


    @Override
    public void process(GameData gameData, World world) {
        List<Entity> bulletsToRemove = new ArrayList<>();

        for (Entity bullet : world.getEntities(Bullet.class)) {

            double changeX = Math.cos(Math.toRadians(bullet.getRotation()));
            double changeY = Math.sin(Math.toRadians(bullet.getRotation()));

            bullet.setX(bullet.getX() + changeX * 3);
            bullet.setY(bullet.getY() + changeY * 3);

            // Check if the bullet is out of screen
            if (isBulletOutOfScreen(bullet, gameData)) {
                bulletsToRemove.add(bullet);
            }
            if (bullet instanceof IDamageable && ((IDamageable) bullet).getHitPoints() <= 0) {
                world.removeEntity(bullet);
            }
        }

        // Remove all bullets that are out of screen
        for (Entity bullet : bulletsToRemove) {
            world.removeEntity(bullet);
        }
    }


    @Override
    public Entity createBullet(Entity shooter, GameData gameData) {
        Entity bullet = new Bullet(10);
        bullet.setPolygonCoordinates(1, -1, 1, 1, -1, 1, -1, -1);

        double shooterX = shooter.getX();
        double shooterY = shooter.getY();
        bullet.setX(shooterX);
        bullet.setY(shooterY);

        double shooterRotation = shooter.getRotation();
        bullet.setRotation(shooterRotation);

        return bullet;
    }

    private boolean isBulletOutOfScreen(Entity bullet, GameData gameData) {
        return bullet.getX() < 0 || bullet.getX() > gameData.getDisplayWidth() ||
                bullet.getY() < 0 || bullet.getY() > gameData.getDisplayHeight();
    }
}
