import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import javax.swing.*;

public class Platformer extends GameEngine {
    private GameWorld gameWorld;
    private Player player;
    private Image[] coinSprites;
    private Image mushroomSprite;
    private Image background;
    private AudioClip bgm;
    private AudioClip jumpSound;
    private AudioClip collectCoinSound;
    private AudioClip hurtSound;
    private boolean gameWon = false;
    private JButton restartButton;

    public static void main(String[] args) {
        createGame(new Platformer(), 60);
    }

    public Platformer() {
        super(GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT);
    }

    @Override
    public void init() {
        Image playerSpriteSheet = loadImage("./resource/AnimationSheet_Character.png");
        Image coinSheet = loadImage("./resource/Coin_Silver-Sheet.png");
        mushroomSprite = loadImage("./resource/Mushroom.png");
        background = loadImage("./resource/Background.png");

        Image[] playerIdleSprites = new Image[2];
        Image[] playerWalkingSprites = new Image[8];
        for (int i = 0; i < 8; i++) {
            playerWalkingSprites[i] = subImage(playerSpriteSheet, i * 32, 96, 32, 32);
        }
        for (int i = 0; i < 2; i++) {
            playerIdleSprites[i] = subImage(playerSpriteSheet, i * 32, 0, 32, 32);
        }

        player = new Player(playerIdleSprites, playerWalkingSprites);

        coinSprites = new Image[10];
        int coinSheetWidth = coinSheet.getWidth(null);
        int coinSheetHeight = coinSheet.getHeight(null);
        int coinIndex = 0;

        for (int y = 0; y < coinSheetHeight; y += 32) {
            for (int x = 0; x < coinSheetWidth; x += 32) {
                if (coinIndex < 10) {
                    coinSprites[coinIndex] = subImage(coinSheet, x, y, 32, 32);
                    if (coinSprites[coinIndex] == null) {
                        System.out.println("Error: coin image " + coinIndex + " is null.");
                    } else {
                        System.out.println("Coin frame " + coinIndex + " loaded successfully.");
                    }
                    coinIndex++;
                }
            }
        }

        gameWorld = new GameWorld(this, player, coinSprites, mushroomSprite, background);

        // load sound effects
        bgm = loadAudio("./resource/sounds/bgm.wav");
        startAudioLoop(bgm);
        jumpSound = loadAudio("./resource/sounds/Jump.wav");
        collectCoinSound = loadAudio("./resource/sounds/collectcoin.wav");
        hurtSound = loadAudio("./resource/sounds/hurt.wav");
        SwingUtilities.invokeLater(() -> {
            restartButton = new JButton("Restart");
            restartButton.setFont(new Font("Arial", Font.BOLD, 32));
            restartButton.setBounds(300, 350, 200, 50);
            restartButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    restartGame();
                }
            });
            mFrame.getContentPane().setLayout(null);
            mFrame.getContentPane().add(restartButton);
            restartButton.setVisible(false);
        });
    }

    @Override
    public void update(double dt) {
        if (!gameWon) {
            gameWorld.update(dt);
        }
    }

    @Override
    public void paintComponent() {
        if (gameWon) {
            // draw the background
            for (int i = 0; i < GameConfig.WINDOW_WIDTH; i += background.getWidth(null)) {
                for (int j = 0; j < GameConfig.WINDOW_HEIGHT; j += background.getHeight(null)) {
                    mGraphics.drawImage(background, i, j, null);
                }
            }

            // draw winning screen
            mGraphics.setColor(Color.WHITE);
            mGraphics.setFont(new Font("Arial", Font.BOLD, 64));
            mGraphics.drawString("You Won!!!", 250, 300);
            if (restartButton != null) {
                restartButton.setVisible(true);
            }
        } else {
            gameWorld.draw(mGraphics);
            if (restartButton != null) {
                restartButton.setVisible(false);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (!gameWon) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> player.setLeft(true);
                case KeyEvent.VK_RIGHT -> player.setRight(true);
                case KeyEvent.VK_SPACE -> {
                    player.setJump(true);
                    playAudio(jumpSound);
                }
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameWon) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> player.setLeft(false);
                case KeyEvent.VK_RIGHT -> player.setRight(false);
                case KeyEvent.VK_SPACE -> player.setJump(false);
            }
        }
    }

    public void playCollectCoinSound() {
        playAudio(collectCoinSound);
    }

    public void playHurtSound() {
        playAudio(hurtSound);
    }

    public void showWinningScreen() {
        gameWon = true;
        if (restartButton != null) {
            restartButton.setVisible(true);
        }
    }

    private void restartGame() {
        // Restart the game logic
        gameWon = false;
        if (restartButton != null) {
            restartButton.setVisible(false);
        }
        player = new Player(player.getIdleSprites(), player.getWalkingSprites());
        gameWorld = new GameWorld(this, player, coinSprites, mushroomSprite, background);
    }
}