package dk.sdu.mmmi.cbse.playersystem;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.services.IDamageable;

/**
 *
 * @author Emil
 */
public class Player extends Entity implements IDamageable {
    private int hitPoints;

    public Player(int hitPoints) {
        this.hitPoints = hitPoints;
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
