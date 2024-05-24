import java.awt.*;
import java.awt.event.KeyEvent;

public class Platformer extends GameEngine{

    public static void main(String[] args) {
        createGame(new Platformer(),60);
    }

    private boolean left, right, jump;
    Image spritesheet;

    public void init() {

        setWindowSize(800, 600);
        spritesheet = loadImage("resource/AnimationSheet_Character.png");

        // Setup booleans
        left  = false;
        right = false;
        jump = false;

        //initialise player
        initPlayer();
    }

    //player variables
    double playerPositionX;
    double playerPositionY;
    double playerSpeed;
    Image[] playerSpriteIdle = new Image[2];
    Image[] playerSpriteWalking = new Image[8];
    double playerFrameTimer;

    public void initPlayer() {
        //load images for player into arrays
        for (int i = 0; i < 8; i++) {
            playerSpriteWalking[i] = subImage(spritesheet, i*32,96, 32, 32);
        }
        for (int i = 0; i < 2; i++) {
            playerSpriteIdle[i] = subImage(spritesheet, i * 32, 0, 32, 32);
        }

        //starting point for player
        playerPositionX = 250;
        playerPositionY = 250;

        //controls how fast player moves
        playerSpeed = 200;

        //frame timer for which frame is displayed
        playerFrameTimer = 0;
    }

    @Override
    public void update(double dt) {
        updatePlayer(dt);
    }

    public void updatePlayer(double dt) {
        //updating frame timer for player
        playerFrameTimer +=dt;

        //player movement
        if (left) {
            playerPositionX -= playerSpeed*dt;
        }
        if (right) {
            playerPositionX += playerSpeed*dt;
        }

        if (jump) {
            //unsure how to implement yet
        }

    }

    @Override
    public void paintComponent() {
        //draw background, to be replaced with asset later
        changeBackgroundColor(white);
        clearBackground(width(),height());

        //draw temporary ground
        changeColor(green);
        drawSolidRectangle(0, 314, 800, 600);

        //draws the player
        drawPlayer();
    }

    //draws the player
    public void drawPlayer() {
        int i;

        //may be better to replace if statements with switch if we are going to be adding many more player states

        //walking animation when moving left
        if (left) {
            i = getAnimationFrame(playerFrameTimer, 1, 8);
            //drawing image with negative width flips it horozontally
            drawImage(playerSpriteWalking[i], playerPositionX+64,  playerPositionY, -64, 64);
        }
        //walking animation when moving right
        else if (right) {
            i = getAnimationFrame(playerFrameTimer, 1, 8);
            drawImage(playerSpriteWalking[i], playerPositionX,  playerPositionY, 64, 64);
        }
        //idle
        else {
            //currently idle will always face right, can add int/bool to track direction of last direction faced and draw image to match
            i = getAnimationFrame(playerFrameTimer, 1, 2);
            drawImage(playerSpriteIdle[i], playerPositionX, playerPositionY, 64, 64);
        }
    }
    //this method is used to calculate the frame to display independent of the frame rate
    public int getAnimationFrame(double timer, double duration, int numFrames) {
        // Get frame
        int i = floor(((timer % duration) / duration) * numFrames);
        // Check range
        if(i >= numFrames) {
            i = numFrames-1;
        }
        // Return
        return i;
    }


    // KeyPressed for Game
    public void keyPressed(KeyEvent e) {

        // The user pressed left arrow
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            left = true;
        }
        // The user pressed right arrow
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            right = true;
        }
        // The user pressed space
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump = true;
        }
    }

    public void keyReleased(KeyEvent e) {
        // The user released left arrow
        if (e.getKeyCode() == KeyEvent.VK_LEFT) {
            // Record it
            left = false;
        }
        // The user released right arrow
        if (e.getKeyCode() == KeyEvent.VK_RIGHT) {
            // Record it
            right = false;
        }
        // The user released space
        if(e.getKeyCode() == KeyEvent.VK_SPACE) {
            jump = false;
        }
    }
}

