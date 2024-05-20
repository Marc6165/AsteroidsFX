package dk.sdu.mmmi.cbse.common.asteroids;
import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.services.IDamageable;

public class Asteroid extends Entity implements IDamageable {
    private int size;

    private int hitPoints;

    public Asteroid(int size, int hitPoints) {
        this.size = size;
        this.hitPoints = hitPoints;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    @Override
    public void takeDamage(int damage) {
        this.hitPoints -= damage;
    }

    @Override
    public int getHitPoints() {
        return this.hitPoints;
    }

    @Override
    public void setHitPoints(int hitPoints) {
        this.hitPoints = hitPoints;
    }
}
