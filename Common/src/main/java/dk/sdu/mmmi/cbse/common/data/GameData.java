package dk.sdu.mmmi.cbse.common.data;

import java.awt.Dimension;
import java.awt.Toolkit;

public class GameData {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();

    private int displayWidth  = (int) (screenSize.width * 0.8); ;
    private int displayHeight = (int) (screenSize.height * 0.8);
    private final GameKeys keys = new GameKeys();

    private long startTime;  // To track when the game starts

    public GameData() {
        this.startTime = System.currentTimeMillis(); // Initialize start time when GameData is created
    }

    public GameKeys getKeys() {
        return keys;
    }

    public void setDisplayWidth(int width) {
        this.displayWidth = width;
    }

    public int getDisplayWidth() {
        return displayWidth;
    }

    public void setDisplayHeight(int height) {
        this.displayHeight = height;
    }

    public int getDisplayHeight() {
        return displayHeight;
    }

    // Method to get the elapsed game time in seconds
    public int getGameTime() {
        long currentTime = System.currentTimeMillis();
        return (int) ((currentTime - this.startTime) / 1000);
    }

    // Method to reset the game time (if needed)
    public void resetGameTime() {
        this.startTime = System.currentTimeMillis();
    }

}
