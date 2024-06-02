import java.awt.*;

public class Player {
    private final Image[] idleSprites;
    private final Image[] walkingSprites;
    private final Image[] jumpingSprites;
    private double spriteTimer = 0;
    private int currentSpriteIndex = 0;
    private double x = 100, y = GameConfig.GROUND_LEVEL - GameConfig.SPRITE_SIZE;
    private double yVelocity;
    private boolean onTile, onGround;
    private boolean isJumping;
    private boolean left = false, right = false, jump = false;
    private boolean facingLeft;
    private static final double GRAVITY = 500;


    public Player(Image[] idleSprites, Image[] walkingSprites, Image[] playerJumpingSprites) {
        this.idleSprites = idleSprites;
        this.walkingSprites = walkingSprites;
        this.jumpingSprites = playerJumpingSprites;
        facingLeft = false;
        yVelocity = 0;
        onTile = false;
        onGround = true;
        isJumping = false;
    }

    public void update(double dt) {
        spriteTimer += dt;

        double yPrevious = y;
        if (left) {
            x -= 200 * dt;
        }
        if (right) {
            x += 200 * dt;
        }

        if (!onTile && !onGround) {
            jump = false;
            isJumping = true;
        } else {
            isJumping = false;
        }

        if (jump && yVelocity == 0) {
            yVelocity = -500;
            onTile = false;
        }

        yVelocity += 1000 * dt;
        y += yVelocity * dt;

        if (onTile) {
            y = yPrevious;
            yVelocity = 0;
        }

        if (y >= GameConfig.GROUND_LEVEL - GameConfig.SPRITE_SIZE) {
            y = GameConfig.GROUND_LEVEL - GameConfig.SPRITE_SIZE;
            yVelocity = 0;
            onGround = true;
        }
        else {
            onGround = false;
        }
    }

    public void draw(Graphics2D g, double offsetX) {
        int i = 0;
        double drawX = x - offsetX;
        if (left && !isJumping) {
            i = getAnimationFrame(spriteTimer, 1, 8);
            g.drawImage(walkingSprites[i], (int) drawX + GameConfig.SPRITE_SIZE, (int) y, -GameConfig.SPRITE_SIZE, GameConfig.SPRITE_SIZE, null);
        } else if (right && !isJumping) {
            i = getAnimationFrame(spriteTimer, 1, 8);
            g.drawImage(walkingSprites[i], (int) drawX, (int) y, GameConfig.SPRITE_SIZE, GameConfig.SPRITE_SIZE, null);
        } else if (facingLeft && isJumping) {
            i = getAnimationFrame(spriteTimer, 1, 8);
            g.drawImage(jumpingSprites[i], (int) drawX+GameConfig.SPRITE_SIZE, (int) y, -GameConfig.SPRITE_SIZE, GameConfig.SPRITE_SIZE, null);
        } else if (!facingLeft && isJumping) {
            i = getAnimationFrame(spriteTimer, 1, 8);
            g.drawImage(jumpingSprites[i], (int) drawX, (int) y, GameConfig.SPRITE_SIZE, GameConfig.SPRITE_SIZE, null);
        } else if (facingLeft && !isJumping) {
            i = getAnimationFrame(spriteTimer, 1, 2);
            g.drawImage(idleSprites[i], (int) drawX+GameConfig.SPRITE_SIZE, (int) y, -GameConfig.SPRITE_SIZE, GameConfig.SPRITE_SIZE, null);
        } else {
            i = getAnimationFrame(spriteTimer, 1, 2);
            g.drawImage(idleSprites[i], (int) drawX, (int) y, GameConfig.SPRITE_SIZE, GameConfig.SPRITE_SIZE, null);
        }
    }

    public Rectangle getBounds(double offsetX) {
        return new Rectangle((int) (x - offsetX), (int) y, GameConfig.SPRITE_SIZE, GameConfig.SPRITE_SIZE);
    }

    public double getX() {
        return x;
    }

    public void setLeft(boolean left) {
        this.left = left;
        if(left) {
            setFacingLeft(true);
        }
    }

    public void setRight(boolean right) {
        this.right = right;
        if (right) {
            setFacingLeft(false);
        }
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }
    public void setX(float x) { this.x = x; }

    public void setY(float y) { this.y = y; }

    public void setYVelocity(double yVelocity) {this.yVelocity = yVelocity;}

    public double getY() { return y; }

    public boolean getLeft(){ return left; }

    public boolean getRight(){ return right; }

    public double getYVelocity() { return yVelocity; }

    public void setOnTile(boolean onTile) { this.onTile = onTile; }

    public boolean getOnTile() { return onTile; }

    private int getAnimationFrame(double timer, double duration, int numFrames) {
        int i = (int) ((timer % duration) / duration * numFrames);
        return Math.min(i, numFrames - 1);
    }

    public Image[] getIdleSprites() {
        return idleSprites;
    }

    public Image[] getWalkingSprites() {
        return walkingSprites;
    }

    public boolean isJumping() {
        return isJumping;
    }

    public Image[] getJumpingSprites() { return jumpingSprites;}

    public void setFacingLeft(boolean facingLeft){
        this.facingLeft = facingLeft;
    }
}