public class GameConfig {
    public static final int WINDOW_WIDTH = 800;
    public static final int WINDOW_HEIGHT = 600;
    public static final int SPRITE_SIZE = 64;
    public static final int GROUND_LEVEL = 550;
    public static final int JUMP_HEIGHT = 150;
    public static final int WORLD_WIDTH = 20000;
    public static final int SEGMENT_WIDTH = 200;
    public static final int MAX_COINS_PER_SEGMENT = 3;
    public static final int COINS_TOTAL = (WORLD_WIDTH / SEGMENT_WIDTH) * MAX_COINS_PER_SEGMENT;
    public static final int MAX_MUSHROOMS_PER_SEGMENT = 1;
    public static final int MUSHROOMS_TOTAL = (WORLD_WIDTH / SEGMENT_WIDTH) * MAX_MUSHROOMS_PER_SEGMENT;
}