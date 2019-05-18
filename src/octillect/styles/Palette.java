package octillect.styles;

import java.io.File;
import java.io.FileNotFoundException;

import java.util.HashMap;
import java.util.Scanner;

import javafx.scene.paint.Color;
import octillect.exceptions.OCTException;
import octillect.exceptions.PaletteFileNotFoundException;

public final class Palette {

    private static final String CSS_LOCATION  = "./src/octillect/styles/";
    private static final String CSS_FILE_NAME = "Palette.css";

    /**
     * Constant field; use Palette.PRIMARY
     */
    public static final Color PRIMARY;
    public static final Color PRIMARY_LIGHT;
    public static final Color PRIMARY_DARK;

    public static final Color SUCCESS;
    public static final Color INFO;
    public static final Color WARNING;
    public static final Color DANGER;

    public static final Color DARK_100;
    public static final Color DARK_200;
    public static final Color DARK_300;
    public static final Color DARK_400;
    public static final Color DARK_500;
    public static final Color DARK_600;
    public static final Color DARK_700;
    public static final Color DARK_800;
    public static final Color DARK_900;

    static {

        // Populate the palette hash map from the Palette CSS file.

        HashMap<String, Color> palette = new HashMap<>();

        try {
            Scanner scanner = null;
            try {
                scanner = new Scanner(new File(CSS_LOCATION + CSS_FILE_NAME));
            } catch (FileNotFoundException e) {
                throw new PaletteFileNotFoundException();
            }
            while (scanner.hasNextLine()) {
                String buffer = scanner.nextLine();
                if (buffer.contains(";")) {
                    buffer = buffer.replace(" ", "");
                    String name = buffer.substring(0, buffer.indexOf(':'));
                    String hexCode = buffer.substring(buffer.indexOf('#'), buffer.indexOf('#') + 7);
                    palette.put(name, Color.valueOf(hexCode));
                }
            }
            scanner.close();
        } catch (OCTException e) {
            e.printError();
        }


        // Initialize the Colors

        PRIMARY       = palette.get("-o-primary");
        PRIMARY_LIGHT = palette.get("-o-primary-light");
        PRIMARY_DARK  = palette.get("-o-primary-dark");

        SUCCESS       = palette.get("-o-success");
        INFO          = palette.get("-o-info");
        WARNING       = palette.get("-o-warning");
        DANGER        = palette.get("-o-danger");

        DARK_100      = palette.get("-o-dark-100");
        DARK_200      = palette.get("-o-dark-200");
        DARK_300      = palette.get("-o-dark-300");
        DARK_400      = palette.get("-o-dark-400");
        DARK_500      = palette.get("-o-dark-500");
        DARK_600      = palette.get("-o-dark-600");
        DARK_700      = palette.get("-o-dark-700");
        DARK_800      = palette.get("-o-dark-800");
        DARK_900      = palette.get("-o-dark-900");

    }

    /**
     * Getter method for FXML;
     * put the following at the beginning of the FXML file:
     * <fx:define>
     *     <fx:id="Palette" />
     * </fx:define>
     * then use ${Palette.PRIMARY} for binding.
     */
    public Color getPRIMARY()       { return PRIMARY; }
    public Color getPRIMARY_LIGHT() { return PRIMARY_LIGHT; }
    public Color getPRIMARY_DARK()  { return PRIMARY_DARK; }

    public Color getSUCCESS() { return SUCCESS; }
    public Color getINFO()    { return INFO; }
    public Color getWARNING() { return WARNING; }
    public Color getDANGER()  { return DANGER; }

    public Color getDARK_100() { return DARK_100; }
    public Color getDARK_200() { return DARK_200; }
    public Color getDARK_300() { return DARK_300; }
    public Color getDARK_400() { return DARK_400; }
    public Color getDARK_500() { return DARK_500; }
    public Color getDARK_600() { return DARK_600; }
    public Color getDARK_700() { return DARK_700; }
    public Color getDARK_800() { return DARK_800; }
    public Color getDARK_900() { return DARK_900; }

}