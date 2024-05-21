package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.Entity;
import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class EnemyPlugin implements IGamePluginService {
    private Entity enemy;

    public EnemyPlugin() {
    }
    @Override
    public void start(GameData gameData, World world) {
        int initialEnemies = 2;
        for (int i = 0; i < initialEnemies; i++) {
            Entity enemy = EnemyFactory.createEnemyShip(gameData);
            world.addEntity(enemy);
        }
    }

    @Override
    public void stop(GameData gameData, World world) {
        world.removeEntity(enemy);
    }
}
