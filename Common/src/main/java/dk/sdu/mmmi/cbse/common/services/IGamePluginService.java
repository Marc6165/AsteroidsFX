package dk.sdu.mmmi.cbse.common.services;

import dk.sdu.mmmi.cbse.common.data.GameData;
import dk.sdu.mmmi.cbse.common.data.World;

/**
 * This interface defines the lifecycle control of the game's plugins.
 * <p>
 *     This interface should be implemented by the game's plugins to manage their lifecycle within the game engine.
 *     Implementations should provide logic for initializing (start) and cleaning up resources from a plugin.
 * </p>
 */

public interface IGamePluginService {

    /**
     * Initializes the plugin
     * <p>
     * This method is called when the game engine is starting up. It should be used to initialize resources
     * that the plugin needs to function.
     * Pre-conditions
     * - The gameData object should be initialized with the game's meta-data.
     * - The world object should be initialized with the game's entities.
     * Post-conditions
     * - The plugin should be initialized and ready to be used by the game engine.
     *
     * @param gameData object containing meta-data about the game
     * @param world object that manages all entities within the game. Can add or remove entities.
     */
    void start(GameData gameData, World world);

    /**
     * Cleans up resources used by the plugin
     * <p>
     * This method is called when the game engine is shutting down. It should be used to clean up resources
     * that the plugin has used during its lifecycle.
     * Pre-conditions
     * - The gameData object should be initialized with the game's meta-data.
     * - The world object should be initialized with the game's entities.
     * Post-conditions
     * - The plugin should be cleaned up and ready to be removed from the game engine.
     * @param gameData object containing meta-data about the game
     * @param world object that manages all entities within the game. Can add or remove entities.

     */
    void stop(GameData gameData, World world);
}
