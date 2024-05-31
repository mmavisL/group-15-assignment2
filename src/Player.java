import java.awt.*;

public class Player {
    private final Image[] idleSprites;
    private final Image[] walkingSprites;
    private int currentSpriteIndex = 0;
    private double spriteTimer = 0;
    private double x = 100, y = GameConfig.GROUND_LEVEL - GameConfig.SPRITE_SIZE;
    private boolean left = false, right = false, jump = false;
    private boolean isJumping = false;
    private double jumpSpeed = 0;
    private static final double GRAVITY = 500;

    public Player(Image[] idleSprites, Image[] walkingSprites) {
        this.idleSprites = idleSprites;
        this.walkingSprites = walkingSprites;
    }

    public void update(double dt) {
        spriteTimer += dt;
        if (right) {
            x += 200 * dt;
            if (spriteTimer > 0.1) {
                currentSpriteIndex = (currentSpriteIndex + 1) % walkingSprites.length;
                spriteTimer = 0;
            }
        } else if (left) {
            x -= 200 * dt;
            if (spriteTimer > 0.1) {
                currentSpriteIndex = (currentSpriteIndex + 1) % walkingSprites.length;
                spriteTimer = 0;
            }
        } else {
            if (spriteTimer > 0.5) {
                currentSpriteIndex = (currentSpriteIndex + 1) % idleSprites.length;
                spriteTimer = 0;
            }
        }

        if (jump && !isJumping) {
            isJumping = true;
            jumpSpeed = -300;
        }

        if (isJumping) {
            y += jumpSpeed * dt;
            jumpSpeed += GRAVITY * dt;
            if (y >= GameConfig.GROUND_LEVEL - GameConfig.SPRITE_SIZE) {
                y = GameConfig.GROUND_LEVEL - GameConfig.SPRITE_SIZE;
                isJumping = false;
                jump = false;
            }
        }
    }

    public void draw(Graphics2D g, double offsetX) {
        Image currentSprite;
        if (right || left) {
            currentSprite = walkingSprites[Math.min(currentSpriteIndex, walkingSprites.length - 1)];
        } else {
            currentSprite = idleSprites[Math.min(currentSpriteIndex, idleSprites.length - 1)];
        }
        g.drawImage(currentSprite, (int) (x - offsetX), (int) y, GameConfig.SPRITE_SIZE, GameConfig.SPRITE_SIZE, null);
    }

    public Rectangle getBounds(double offsetX) {
        return new Rectangle((int) (x - offsetX), (int) y, GameConfig.SPRITE_SIZE, GameConfig.SPRITE_SIZE);
    }

    public double getX() {
        return x;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public Image[] getIdleSprites() {
        return idleSprites;
    }

    public Image[] getWalkingSprites() {
        return walkingSprites;
    }
}