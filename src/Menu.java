import java.awt.*;

public class Menu {
    private final Image background;
    private int menuOption;
    private boolean instructions = false;
    Font font;

    public Menu(Image background, Font font){
        this.background = background;
        this.font = font;
    }

    public void setInstructions(boolean instructions) {this.instructions = instructions;}

    public void update(int menuOption) {
        this.menuOption = menuOption;
    }

    public void draw(Graphics2D g) {
        for (int i = 0; i < GameConfig.WORLD_WIDTH / GameConfig.WINDOW_WIDTH + 1; i++) {
            g.drawImage(background, i * GameConfig.WINDOW_WIDTH, 0, GameConfig.WINDOW_WIDTH, GameConfig.WINDOW_HEIGHT, null);
        }

        float menuTextX = 225;
        float menuTextY = 225;

        if (instructions) {
            setFontSize(g, 30);
            g.setColor(Color.white);
            g.drawString("How to play:", 30, 50);
            setFontSize(g, 20);
            g.drawString("Move the player character using the", 30, 100);
            g.drawString("← → arrow keys. Press Space to jump.", 30, 130);
            g.drawString("Your goal is to jump and collect coins", 30, 180);
            g.drawString("to increase your score.", 30, 210);
            g.drawString("You must avoid the mushrooms, these", 30, 250);
            g.drawString("will decrease your score.", 30, 280);
            g.drawString("Keep collecting coins until you reach", 30, 330);
            g.drawString("the end of the level!", 30, 360);

            g.drawString("Press Esc to return to menu", 30, 550);
        }
        else {
            setFontSize(g,40);
            if (menuOption == 0) {
                g.setColor(Color.white);
            } else {
                g.setColor(Color.gray);
            }
            g.drawString("Play", menuTextX, menuTextY);

            if (menuOption == 1) {
                g.setColor(Color.white);
            } else {
                g.setColor(Color.gray);
            }
            g.drawString("Instructions", menuTextX, menuTextY + 50);

            if (menuOption == 2) {
                g.setColor(Color.white);
            } else {
                g.setColor(Color.gray);
            }
            g.drawString("Exit", menuTextX, menuTextY + 100);

            g.setColor(Color.white);
            setFontSize(g,20);
            g.drawString("Press Enter to make a selection", 90, 550);
        }
    }

    public void setFontSize(Graphics g, float fontSize){
        font = font.deriveFont(fontSize);
        g.setFont(font);
    }
}
