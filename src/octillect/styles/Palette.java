package octillect.styles;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javafx.scene.paint.Color;

public final class Palette {

    private static final String CSS_LOCATION  = "./src/octillect/styles/";
    private static final String CSS_FILE_NAME = "Palette.css";

    /**
     * Constant field; use Palette.PRIMARY
     */
    public static final Color PRIMARY      = getColorFromCSS("-o-primary");
    public static final Color PRIMARY_DARK = getColorFromCSS("-o-primary-dark");
    public static final Color DARK_900     = getColorFromCSS("-o-dark-900");
    public static final Color DARK_700     = getColorFromCSS("-o-dark-700");
    public static final Color DARK_500     = getColorFromCSS("-o-dark-500");
    public static final Color DARK_300     = getColorFromCSS("-o-dark-300");


    /**
     * Getter method for FXML;
     * put the following at the beginning of the FXML file:
     * <fx:define>
     *     <octillect.styles.Palette fx:id="Palette" />
     * </fx:define>
     * then use ${Palette.PRIMARY} for binding.
     */
    public Color getPRIMARY() { return PRIMARY; }
    public Color getPRIMARY_DARK() { return PRIMARY_DARK; }
    public Color getDARK_900() { return DARK_900; }
    public Color getDARK_700() { return DARK_700; }
    public Color getDARK_500() { return DARK_500; }
    public Color getDARK_300() { return DARK_300; }


    /**
     * Looks up for the color in the Palette CSS file then gets its
     * HEX code and return a Color object having the same HEX code.
     */
    private static Color getColorFromCSS(String colorName) {
        String colorHexCode = "";
        try {
            Scanner scanner = new Scanner(new File(CSS_LOCATION + CSS_FILE_NAME));
            while (scanner.hasNextLine()) {
                String buffer = scanner.nextLine();
                if (buffer.contains(" " + colorName + " ")){
                    colorHexCode = buffer.substring(buffer.indexOf('#'), buffer.indexOf('#') + 7);
                    break;
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        if (colorHexCode.equals("")){
            System.out.println(colorName + " is not found in " + CSS_FILE_NAME);
        }

        return Color.valueOf(colorHexCode);
    }

}
