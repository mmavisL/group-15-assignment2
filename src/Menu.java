import java.awt.*;
import java.io.File;
import java.io.IOException;

import static java.awt.Font.TRUETYPE_FONT;

public class Menu {
    private final Image background;
    private int menuOption;
    Font prstart;
    File file;

    public Menu(Image background){
        this.background = background;
        file = new File("./resource/prstart.ttf");
    }

    public void update(int menuOption) {
        this.menuOption = menuOption;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < GameConfig.WORLD_WIDTH / GameConfig.WINDOW_WIDTH + 1; i++) {
            g.drawImage(background, i * GameConfig.WINDOW_WIDTH, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT, null);
        }

        //loads in custom font prstart.ttf
        try {
            //
            prstart = Font.createFont(TRUETYPE_FONT, file);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(prstart);
            float fontSize = 40;
            prstart = prstart.deriveFont(fontSize);
            g.setFont(prstart);
            //g.setFont(new Font("Arial", Font.BOLD, 40));
        } catch (FontFormatException | IOException e) {
            throw new RuntimeException(e);
        }

        //g.setFont(new Font("Arial", Font.BOLD, 40));

        g.setColor(Color.white);
        //g.drawLine(400,0,400,600);

        float menuTextX = 225;
        float menuTextY = 225;

        if(menuOption == 0) {g.setColor(Color.white);}
        else {g.setColor(Color.gray);}
        g.drawString("Play", menuTextX, menuTextY);

        if(menuOption == 1) {g.setColor(Color.white);}
        else {g.setColor(Color.gray);}
        g.drawString("Instructions", menuTextX, menuTextY + 50);

        if(menuOption == 2) {g.setColor(Color.white);}
        else {g.setColor(Color.gray);}
        g.drawString("Exit", menuTextX, menuTextY + 100);

        //add section for instructions on how to play once game completed
    }
}
