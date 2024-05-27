public class GameConfig {


    public static final int SPRITE_SIZE = 64;
    public static final int GROUND_LEVEL = 514;
    public static final int JUMP_HEIGHT = 150;  // The highest point player can reach with a jump
    public static final int WORLD_WIDTH = 2000;  // Width of the game world
    public static final int SEGMENT_WIDTH = 200; // Width of each segment
    public static final int MAX_COINS_PER_SEGMENT = 3;
    public static final int COINS_TOTAL = (WORLD_WIDTH / SEGMENT_WIDTH) * MAX_COINS_PER_SEGMENT;// Max number of coins per segment
}
