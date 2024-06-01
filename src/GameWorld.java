import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameWorld {
    private final Platformer platformer;
    private final Player player;
    private final ArrayList<Coin> coins;
    private final ArrayList<Mushroom> mushrooms;
    private final Image[] coinSprites;
    private final Image mushroomSprite;
    private int score;
    private double worldOffsetX;
    private final Map<Integer, Integer> segmentCoinCount;
    private final Map<Integer, Integer> segmentMushroomCount;
    private final Image background;
    Font font;

    public GameWorld(Platformer platformer, Player player, Image[] coinSprites, Image mushroomSprite, Image background, Font font) {
        this.platformer = platformer;
        this.player = player;
        this.coinSprites = coinSprites;
        this.mushroomSprite = mushroomSprite;
        this.background = background;
        this.coins = new ArrayList<>();
        this.mushrooms = new ArrayList<>();
        this.score = 0;
        this.worldOffsetX = 0;
        this.segmentCoinCount = new HashMap<>();
        this.segmentMushroomCount = new HashMap<>();
        this.font = font;
        generateInitialCoins();
        generateInitialMushrooms();
    }

    public void update(double dt) {
        if (score >= GameConfig.WINNING_SCORE) {
            platformer.showWinningScreen();
            return;
        }

        player.update(dt);
        for (Coin coin : coins) {
            coin.update(dt);
        }
        checkCollisions();
        updateWorldOffset();
    }

    public void draw(Graphics2D g) {
        // Draw background
        for (int i = 0; i < GameConfig.WORLD_WIDTH / GameConfig.WINDOW_WIDTH + 1; i++) {
            g.drawImage(background, i * GameConfig.WINDOW_WIDTH - (int) worldOffsetX, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT, null);
        }

        // Draw player
        player.draw(g, worldOffsetX);

        // Draw coins
        for (Coin coin : coins) {
            coin.draw(g, worldOffsetX);
        }

        // Draw mushrooms
        for (Mushroom mushroom : mushrooms) {
            mushroom.draw(g, worldOffsetX);
        }

        // Draw score
        float fontSize = 26;
        font = font.deriveFont(fontSize);
        g.setFont(font);
        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 550, 50);
        g.drawImage(coinSprites[0], 515, 20, 32, 32, null);
    }

    private void generateInitialCoins() {
        Random rand = new Random();
        for (int i = 0; i < GameConfig.COINS_TOTAL; i++) {
            double x, y;
            boolean overlaps;
            do {
                int segment = rand.nextInt(GameConfig.WORLD_WIDTH / GameConfig.SEGMENT_WIDTH);
                x = segment * GameConfig.SEGMENT_WIDTH + rand.nextInt(GameConfig.SEGMENT_WIDTH - Coin.SIZE);
                y = GameConfig.GROUND_LEVEL - Coin.SIZE - rand.nextInt(GameConfig.JUMP_HEIGHT);
                overlaps = false;

                // Check if the segment has reached the max coin limit
                if (segmentCoinCount.getOrDefault(segment, 0) >= GameConfig.MAX_COINS_PER_SEGMENT) {
                    overlaps = true;
                    continue;
                }

                // Check for overlaps with existing coins
                for (Coin coin : coins) {
                    if (coin.getBounds(0).intersects(new Rectangle((int) x, (int) y, Coin.SIZE, Coin.SIZE))) {
                        overlaps = true;
                        break;
                    }
                }

                if (!overlaps) {
                    segmentCoinCount.put(segment, segmentCoinCount.getOrDefault(segment, 0) + 1);
                }
            } while (overlaps);
            coins.add(new Coin(x, y, coinSprites));
        }
    }

    private void generateInitialMushrooms() {
        Random rand = new Random();
        for (int i = 0; i < GameConfig.MUSHROOMS_TOTAL; i++) {
            double x, y;
            boolean overlaps;
            do {
                int segment = rand.nextInt(GameConfig.WORLD_WIDTH / GameConfig.SEGMENT_WIDTH);
                x = segment * GameConfig.SEGMENT_WIDTH + rand.nextInt(GameConfig.SEGMENT_WIDTH - Mushroom.SIZE);
                y = GameConfig.GROUND_LEVEL - Mushroom.SIZE - rand.nextInt(GameConfig.JUMP_HEIGHT);
                overlaps = false;

                // Check if the segment has reached the max mushroom limit
                if (segmentMushroomCount.getOrDefault(segment, 0) >= GameConfig.MAX_MUSHROOMS_PER_SEGMENT) {
                    overlaps = true;
                    continue;
                }

                // Check for overlaps with existing mushrooms
                for (Mushroom mushroom : mushrooms) {
                    if (mushroom.getBounds(0).intersects(new Rectangle((int) x, (int) y, Mushroom.SIZE, Mushroom.SIZE))) {
                        overlaps = true;
                        break;
                    }
                }

                // Check for overlaps with existing coins
                for (Coin coin : coins) {
                    if (coin.getBounds(0).intersects(new Rectangle((int) x, (int) y, Mushroom.SIZE, Mushroom.SIZE))) {
                        overlaps = true;
                        break;
                    }
                }

                if (!overlaps) {
                    segmentMushroomCount.put(segment, segmentMushroomCount.getOrDefault(segment, 0) + 1);
                }
            } while (overlaps);
            mushrooms.add(new Mushroom(x, y, mushroomSprite));
        }
    }

    private void checkCollisions() {
        Rectangle playerBounds = player.getBounds(worldOffsetX);
        // Check coin collisions
        for (int i = 0; i < coins.size(); i++) {
            Coin coin = coins.get(i);
            if (playerBounds.intersects(coin.getBounds(worldOffsetX))) {
                coins.remove(i);
                score++;
                platformer.playCollectCoinSound();  // Play coin collection sound effect
                i--;
            }
        }
        // Check mushroom collisions
        for (int i = 0; i < mushrooms.size(); i++) {
            Mushroom mushroom = mushrooms.get(i);
            if (playerBounds.intersects(mushroom.getBounds(worldOffsetX))) {
                mushrooms.remove(i);
                score--;
                platformer.playHurtSound();  // Play hurt sound effect
                i--;
            }
        }
    }

    private void updateWorldOffset() {
        double playerScreenX = player.getX() - worldOffsetX;
        if (playerScreenX > GameConfig.WINDOW_WIDTH / 2) {
            worldOffsetX = player.getX() - GameConfig.WINDOW_WIDTH / 2;
        }
    }

    public int getScore() {
        return score;
    }

    public Player getPlayer() {
        return player;
    }
}