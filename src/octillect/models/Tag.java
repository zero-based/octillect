package octillect.models;

import javafx.scene.paint.Color;

public class Tag {

    private String id;
    private String name;
    private Color color;


    public Tag() {
    }

    public Tag(String id, String name, Color color) {
        this.id = id;
        this.name = name;
        this.color = color;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public String getColorHex() {
        double r = color.getRed() * 255;
        double g = color.getGreen() * 255;
        double b = color.getBlue() * 255;
        return String.format("#%02X%02X%02X", (int) r, (int) g, (int) b);
    }

}
