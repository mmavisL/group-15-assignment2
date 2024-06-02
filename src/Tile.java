import java.awt.*;

public class Tile {
    private int x;
    private int y;

    public Tile(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX(){
        return x;
    }

    public int getY(){
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public Rectangle getBounds(double offsetX) {
        return new Rectangle((int) (x - offsetX),y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE);
    }


}
