package octillect.styles;

import javafx.scene.paint.Color;

public final class Palette {

    /** Constant field; use Palette.PRIMARY */
    public static final Color PRIMARY      = Color.valueOf("#1473e6");
    public static final Color PRIMARY_DARK = Color.valueOf("#0f64d2");
    public static final Color DARK_900     = Color.valueOf("#1b1b1b");
    public static final Color DARK_700     = Color.valueOf("#323232");
    public static final Color DARK_500     = Color.valueOf("#979797");
    public static final Color DARK_300     = Color.valueOf("#cdcdcd");

    /** Getter method for FXML; use ${Palette.Primary} */
    public Color getPrimary() { return PRIMARY; }
    public Color getPrimaryDark() { return PRIMARY_DARK; }
    public Color getDark900() { return DARK_900; }
    public Color getDark700() { return DARK_700; }
    public Color getDark500() { return DARK_500; }
    public Color getDark300() { return DARK_300; }

}
