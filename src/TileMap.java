import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class TileMap {
    private final Image rock;
    ArrayList<Tile> platformTiles;


    public TileMap(String filename, Image rock){
        this.rock = rock;

        try {
            BufferedReader br = new BufferedReader(new FileReader(filename));
            int numCols = Integer.parseInt(br.readLine());
            int numRows = Integer.parseInt(br.readLine());
            int[][] map = new int[numRows][numCols];

            String deliminator = " ";
            platformTiles = new ArrayList<>();

            for (int row = 0; row < numRows; row++) {
                String line = br.readLine();
                String[] tokens = line.split(deliminator);
                for (int col = 0; col < numCols; col++) {
                    map[row][col] = Integer.parseInt(tokens[col]);
                }
            }

            for (int row = 0; row < numRows; row++) {
                for (int col = 0; col < numCols; col++) {
                    int rc = map[row][col];
                    if (rc == 0) {
                        Tile tile = new Tile(col*GameConfig.TILE_SIZE, row*GameConfig.TILE_SIZE);
                        platformTiles.add(tile);

                    }
                }
            }

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void drawTileMap(Graphics g, double offsetX) {

        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

        for (Tile solidTile : platformTiles) {
            double x = solidTile.getX() - offsetX;
            double y = solidTile.getY();
            g.drawImage(rock, (int)x, (int)y, GameConfig.TILE_SIZE, GameConfig.TILE_SIZE, null);
            //g2d.fillRect((int) x, (int) y, tileSize, tileSize);
        }
    }
}
