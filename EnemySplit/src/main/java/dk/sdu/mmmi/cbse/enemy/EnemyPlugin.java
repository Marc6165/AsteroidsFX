package dk.sdu.mmmi.cbse.enemy;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;
import dk.sdu.mmmi.cbse.common.services.IGamePluginService;

public class EnemyPlugin implements IGamePluginService {
    @Override
    public void start(GameData gameData, World world) {
        System.out.println("EnemyPlugin in EnemySplit-component started");
    }

    @Override
    public void stop(GameData gameData, World world) {
        System.out.println("EnemyPlugin stopped");
    }
}
