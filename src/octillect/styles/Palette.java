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
    public static final Color PRIMARY_LIGHT;
    public static final Color PRIMARY_DARK;

    public static final Color SUCCESS;
    public static final Color INFO;
    public static final Color WARNING;
    public static final Color DANGER;

    public static final Color DARK_300;
    public static final Color DARK_500;
    public static final Color DARK_700;
    public static final Color DARK_900;

    public static final Color BLUE_GRAY_50;
    public static final Color BLUE_GRAY_100;
    public static final Color BLUE_GRAY_200;
    public static final Color BLUE_GRAY_300;
    public static final Color BLUE_GRAY_400;
    public static final Color BLUE_GRAY_500;
    public static final Color BLUE_GRAY_600;
    public static final Color BLUE_GRAY_700;
    public static final Color BLUE_GRAY_800;
    public static final Color BLUE_GRAY_900;

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

        PRIMARY       = palette.get("-o-primary");
        PRIMARY_LIGHT = palette.get("-o-primary-light");
        PRIMARY_DARK  = palette.get("-o-primary-dark");

        SUCCESS       = palette.get("-o-success");
        INFO          = palette.get("-o-info");
        WARNING       = palette.get("-o-warning");
        DANGER        = palette.get("-o-danger");

        DARK_300      = palette.get("-o-dark-300");
        DARK_500      = palette.get("-o-dark-500");
        DARK_700      = palette.get("-o-dark-700");
        DARK_900      = palette.get("-o-dark-900");

        BLUE_GRAY_50  = palette.get("-o-blue-gray-50");
        BLUE_GRAY_100 = palette.get("-o-blue-gray-100");
        BLUE_GRAY_200 = palette.get("-o-blue-gray-200");
        BLUE_GRAY_300 = palette.get("-o-blue-gray-300");
        BLUE_GRAY_400 = palette.get("-o-blue-gray-400");
        BLUE_GRAY_500 = palette.get("-o-blue-gray-500");
        BLUE_GRAY_600 = palette.get("-o-blue-gray-600");
        BLUE_GRAY_700 = palette.get("-o-blue-gray-700");
        BLUE_GRAY_800 = palette.get("-o-blue-gray-800");
        BLUE_GRAY_900 = palette.get("-o-blue-gray-900");

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

    public Color getDARK_300() { return DARK_300; }
    public Color getDARK_500() { return DARK_500; }
    public Color getDARK_700() { return DARK_700; }
    public Color getDARK_900() { return DARK_900; }

    public Color getBLUE_GRAY_50()  { return BLUE_GRAY_50; }
    public Color getBLUE_GRAY_100() { return BLUE_GRAY_100; }
    public Color getBLUE_GRAY_200() { return BLUE_GRAY_200; }
    public Color getBLUE_GRAY_300() { return BLUE_GRAY_300; }
    public Color getBLUE_GRAY_400() { return BLUE_GRAY_400; }
    public Color getBLUE_GRAY_500() { return BLUE_GRAY_500; }
    public Color getBLUE_GRAY_600() { return BLUE_GRAY_600; }
    public Color getBLUE_GRAY_700() { return BLUE_GRAY_700; }
    public Color getBLUE_GRAY_800() { return BLUE_GRAY_800; }
    public Color getBLUE_GRAY_900() { return BLUE_GRAY_900; }

}