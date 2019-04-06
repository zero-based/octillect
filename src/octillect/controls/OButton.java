package octillect.controls;

import javafx.beans.property.ObjectProperty;
import javafx.scene.control.Button;

public class OButton extends Button {

    public final String OBUTTON_CSS = "../styles/OButton.css";
    public enum COLOR {PRIMARY, DARK900, DARK700, DARK500, DARK300};
    public enum FILL {TRUE, FALSE};
    public ObjectProperty<COLOR> color;
    public ObjectProperty<FILL> fill;

    public OButton() {
        super();
        getStylesheets().add(getClass().getResource(OBUTTON_CSS).toExternalForm());
        getStyleClass().add("o-button");
    }

    public COLOR getColor() {
        return color.get();
    }

    public FILL getFill() {
        return fill.get();
    }

    public ObjectProperty<COLOR> colorProperty() {
        return color;
    }

    public ObjectProperty<FILL> fillProperty() {
        return fill;
    }

    public void setColor(COLOR color) {
        if (color == COLOR.PRIMARY)
            getStyleClass().add("o-button-primary");
        else if (color == COLOR.DARK900)
            getStyleClass().add("o-button-dark-900");
        else if (color == COLOR.DARK700)
            getStyleClass().add("o-button-dark-700");
        else if (color == COLOR.DARK500)
            getStyleClass().add("o-button-dark-500");
        else if (color == COLOR.DARK300)
            getStyleClass().add("o-button-dark-300");
    }

    public void setFill(FILL fill) {
        if (fill == FILL.TRUE)
            getStyleClass().add("o-button-fill");
        else if (fill == FILL.FALSE)
            getStyleClass().add("o-button-stroke");
    }
}
