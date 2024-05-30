import java.awt.*;

public class Mushroom {
    private final double x, y;
    private final Image sprite;
    public static final int SIZE = 32;

    public Mushroom(double x, double y, Image sprite) {
        this.x = x;
        this.y = y;
        this.sprite = sprite;
    }

    public void draw(Graphics2D g, double offsetX) {
        g.drawImage(sprite, (int) (x - offsetX), (int) y, SIZE, SIZE, null);
    }

    public Rectangle getBounds(double offsetX) {
        return new Rectangle((int) (x - offsetX), (int) y, SIZE, SIZE);
    }
}