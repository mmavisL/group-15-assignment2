import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;

import static java.awt.Font.TRUETYPE_FONT;

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
    private boolean gameLost = false;
    private Menu menu;
    private enum GameState {Menu, Play}
    private int menuOption;
    private boolean instructions;
    GameState gamestate = GameState.Menu;
    Font font;
    File file;
    private TileMap tilemap;



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
        Image rockSheet = loadImage("./resource/Cave - Platforms.png");

        Image rock = subImage(rockSheet, 15,15, 199,199);

        Image[] playerWalkingSprites = new Image[8];
        for (int i = 0; i < 8; i++) {
            playerWalkingSprites[i] = subImage(playerSpriteSheet, i * 32, 96, 32, 32);
        }
        Image[] playerIdleSprites = new Image[2];
        for (int i = 0; i < 2; i++) {
            playerIdleSprites[i] = subImage(playerSpriteSheet, i * 32, 0, 32, 32);
        }
        Image[] playerJumpingSprites = new Image[8];
        for (int i = 0; i < 8; i++) {
            playerJumpingSprites[i] = subImage(playerSpriteSheet, i * 32, 160, 32, 32);
        }

        player = new Player(playerIdleSprites, playerWalkingSprites, playerJumpingSprites);
        tilemap = new TileMap("./src/tilemap.txt", rock);


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

        loadGameFont();
        menu = new Menu(background, font);

        gameWorld = new GameWorld(this, player, coinSprites, mushroomSprite, background, font, tilemap);

        // load sound effects
        bgm = loadAudio("./resource/sounds/bgm.wav");
        startAudioLoop(bgm);
        jumpSound = loadAudio("./resource/sounds/Jump.wav");
        collectCoinSound = loadAudio("./resource/sounds/collectcoin.wav");
        hurtSound = loadAudio("./resource/sounds/hurt.wav");
    }

    @Override
    public void update(double dt) {

        if (gamestate == GameState.Menu) {
            menu.update(menuOption);
            menu.setInstructions(instructions);
        }
        else if (gamestate == GameState.Play) {
            gameWorld.update(dt);
        }
    }

    @Override
    public void paintComponent() {

        if (gamestate == GameState.Menu) {
            menu.draw(mGraphics);
        }
        else {
            if (gameWon || gameLost) {
                // draw the background
                for (int i = 0; i < GameConfig.WINDOW_WIDTH; i += background.getWidth(null)) {
                    for (int j = 0; j < GameConfig.WINDOW_HEIGHT; j += background.getHeight(null)) {
                        mGraphics.drawImage(background, i, j, null);
                    }
                }
                if (gameWon) {
                    // draw winning screen
                    mGraphics.setColor(Color.WHITE);
                    float fontSize = 40;
                    font = font.deriveFont(fontSize);
                    mGraphics.setFont(font);
                    mGraphics.drawString("You Won!!!", 200, 300);
                    fontSize = 20;
                    font = font.deriveFont(fontSize);
                    mGraphics.setFont(font);
                    mGraphics.drawString("Final score: " + gameWorld.getScore(), 250, 330);
                    mGraphics.drawString("Press Enter to restart, or Esc to exit", 20, 550);
                }
                else if (gameLost) {
                    //draw losing screen
                    mGraphics.setColor(Color.WHITE);
                    float fontSize = 40;
                    font = font.deriveFont(fontSize);
                    mGraphics.setFont(font);
                    mGraphics.drawString("You Lost.", 225, 300);
                    fontSize = 20;
                    font = font.deriveFont(fontSize);
                    mGraphics.setFont(font);
                    mGraphics.drawString("Final score: " + gameWorld.getScore(), 250, 330);
                    mGraphics.drawString("Press Enter to play again,", 150, 520);
                    mGraphics.drawString("or Esc to exit", 250, 550);
                }
            } else {
                gameWorld.draw(mGraphics);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        if (gamestate == GameState.Menu){
            keyPressedMenu(e);
        }
        else if (gamestate == GameState.Play) {
            keyPressedPlay(e);
        }
    }

    public void keyPressedPlay(KeyEvent e) {
        if (!gameWon && !gameLost) {
            switch (e.getKeyCode()) {
                case KeyEvent.VK_LEFT -> player.setLeft(true);
                case KeyEvent.VK_RIGHT -> player.setRight(true);
                case KeyEvent.VK_SPACE -> {
                    player.setJump(true);
                    if (!player.isJumping()) {playAudio(jumpSound);}
                }
            }
        }
        else {
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {restartGame();}
            if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {System.exit(0);}
        }
    }

    public void keyPressedMenu(KeyEvent e) {
        if (!instructions) {
            if (e.getKeyCode() == KeyEvent.VK_UP) {
                if (menuOption > 0) {
                    menuOption--;
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_DOWN) {
                if (menuOption < 2) {
                    menuOption++;
                }
            }
            if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                if (menuOption == 0) {
                    gamestate = GameState.Play;
                } else if (menuOption == 1) {
                    instructions = true;
                } else if (menuOption == 2) {
                    System.exit(0);
                }
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_ESCAPE && instructions) {
            instructions = false;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (!gameWon && !gameLost && gamestate == GameState.Play) {
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
    }

    public void showLoosingScreen() { gameLost = true;}

    private void restartGame() {
        // Restart the game logic
        gameWon = false;
        gameLost = false;
        player = new Player(player.getIdleSprites(), player.getWalkingSprites(), player.getJumpingSprites());
        gameWorld = new GameWorld(this, player, coinSprites, mushroomSprite, background, font, tilemap);
    }

    public void loadGameFont() {
        //loads in custom font prstart.ttf
        try {
            file = new File("./resource/prstart.ttf");
            font = Font.createFont(TRUETYPE_FONT, file);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(font);
            float fontSize = 40;
            font = font.deriveFont(fontSize);
            mGraphics.setFont(font);
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}