package octillect.models;

import com.sun.prism.Image;

import java.awt.*;

public class Label {

    private String name;
    private Task[] card;
    private Image icon;
    private Color color;

    public Label() {}

    public Label(String name, Task[] card, Image icon, Color color) {
        this.name = name;
        this.card = card;
        this.icon = icon;
        this.color = color;
    }

    public Task[] getCard() { return card; }
    public void setCard(Task[] card) { this.card = card; }

    public Color getColor() { return color; }
    public void setColor(Color color) { this.color = color; }

    public Image getIcon() { return icon; }
    public void setIcon(Image icon) { this.icon = icon; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

}
