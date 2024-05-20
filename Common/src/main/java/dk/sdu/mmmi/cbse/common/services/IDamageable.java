package dk.sdu.mmmi.cbse.common.services;

public interface IDamageable {

    void takeDamage(int damage);

    int getHitPoints();

    void setHitPoints(int hitPoints);

}
