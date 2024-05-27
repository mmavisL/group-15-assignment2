import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class GameWorld {
    private final Player player;
    private final ArrayList<Coin> coins;
    private final Image[] coinSprites;
    private int score;
    private double worldOffsetX;
    private final Map<Integer, Integer> segmentCoinCount;

    public GameWorld(Player player, Image[] coinSprites) {
        this.player = player;
        this.coinSprites = coinSprites;
        this.coins = new ArrayList<>();
        this.score = 0;
        this.worldOffsetX = 0;
        this.segmentCoinCount = new HashMap<>();

        generateInitialCoins();
    }

    public void update(double dt) {
        player.update(dt);
        for (Coin coin : coins) {
            coin.update(dt);
        }
        checkCoinCollisions();
        updateWorldOffset();
    }

    public void draw(Graphics2D g) {
        // Draw background
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, Platformer.WINDOW_WIDTH, Platformer.WINDOW_HEIGHT);

        // Draw ground
        g.setColor(Color.GREEN);
        g.fillRect(0, GameConfig.GROUND_LEVEL, Platformer.WINDOW_WIDTH, 100);

        // Draw player
        player.draw(g, worldOffsetX);

        // Draw coins
        for (Coin coin : coins) {
            coin.draw(g, worldOffsetX);
        }

        // Draw score
        g.setColor(Color.BLACK);
        g.setFont(new Font("Arial", Font.BOLD, 20));
        g.drawString("Score: " + score, 650, 50);
        g.drawImage(coinSprites[0], 620, 30, 32, 32, null);
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

    private void checkCoinCollisions() {
        Rectangle playerBounds = player.getBounds(worldOffsetX);
        for (int i = 0; i < coins.size(); i++) {
            Coin coin = coins.get(i);
            if (playerBounds.intersects(coin.getBounds(worldOffsetX))) {
                coins.remove(i);
                score++;
                i--;
            }
        }
    }

    private void updateWorldOffset() {
        double playerScreenX = player.getX() - worldOffsetX;
        if (playerScreenX > Platformer.WINDOW_WIDTH / 2) {
            worldOffsetX = player.getX() - Platformer.WINDOW_WIDTH / 2;
        }
    }

    public int getScore() {
        return score;
    }
}