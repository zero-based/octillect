package octillect.styles;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.HashMap;
import java.util.Scanner;

import javafx.scene.paint.Color;

public final class Palette {

    private static final String CSS_LOCATION  = "./src/octillect/styles/";
    private static final String CSS_FILE_NAME = "Palette.css";

    /**
     * Constant field; use Palette.PRIMARY
     */
    public static final Color PRIMARY;
    public static final Color PRIMARY_DARK;
    public static final Color DARK_900;
    public static final Color DARK_700;
    public static final Color DARK_500;
    public static final Color DARK_300;

    static {

        // Populate the palette hash map from the Palette CSS file.

        HashMap<String, Color> palette = new HashMap<>();
        try {
            Scanner scanner = new Scanner(new File(CSS_LOCATION + CSS_FILE_NAME));
            while (scanner.hasNextLine()) {
                String buffer = scanner.nextLine();
                if (buffer.contains(";")) {
                    buffer         = buffer.replace(" ", "");
                    String name    = buffer.substring(0, buffer.indexOf(':'));
                    String hexCode = buffer.substring(buffer.indexOf('#'), buffer.indexOf('#') + 7);
                    palette.put(name, Color.valueOf(hexCode));
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        // Initialize the Colors

        PRIMARY      = palette.get("-o-primary");
        PRIMARY_DARK = palette.get("-o-primary-dark");
        DARK_900     = palette.get("-o-dark-900");
        DARK_700     = palette.get("-o-dark-700");
        DARK_500     = palette.get("-o-dark-500");
        DARK_300     = palette.get("-o-dark-300");

    }

    /**
     * Getter method for FXML;
     * put the following at the beginning of the FXML file:
     * <fx:define>
     *     <fx:id="Palette" />
     * </fx:define>
     * then use ${Palette.PRIMARY} for binding.
     */
    public Color getPRIMARY() { return PRIMARY; }
    public Color getPRIMARY_DARK() { return PRIMARY_DARK; }
    public Color getDARK_900() { return DARK_900; }
    public Color getDARK_700() { return DARK_700; }
    public Color getDARK_500() { return DARK_500; }
    public Color getDARK_300() { return DARK_300; }

}