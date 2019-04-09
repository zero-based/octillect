package octillect.styles;

import javafx.scene.paint.Color;
import java.io.File;
import java.io.FileNotFoundException;
import java.net.URL;
import java.util.Scanner;

public final class Palette {

    /** Constant field; use Palette.PRIMARY */
    public static final Color PRIMARY      = getColorFromCSS("-o-primary");
    public static final Color PRIMARY_DARK = getColorFromCSS("-o-primary-dark");
    public static final Color DARK_900     = getColorFromCSS("-o-dark-900");
    public static final Color DARK_700     = getColorFromCSS("-o-dark-700");
    public static final Color DARK_500     = getColorFromCSS("-o-dark-500");
    public static final Color DARK_300     = getColorFromCSS("-o-dark-300");

    /** Getter method for FXML; use ${Palette.PRIMARY} */
    public Color getPRIMARY() { return PRIMARY; }
    public Color getPRIMARY_DARK() { return PRIMARY_DARK; }
    public Color getDARK_900() { return DARK_900; }
    public Color getDARK_700() { return DARK_700; }
    public Color getDARK_500() { return DARK_500; }
    public Color getDARK_300() { return DARK_300; }

    private static Color getColorFromCSS(String colorName) {
        String colorHex = "";
        try {
            URL fileURL = Palette.class.getResource("Palette.css");
            Scanner scanner = new Scanner(new File(fileURL.getPath()));
            while (scanner.hasNextLine()) {
                String buffer = scanner.nextLine();
                if (buffer.contains(" " + colorName + " ")){
                    colorHex = buffer.substring(buffer.indexOf('#'), buffer.indexOf('#') + 7);
                    break;
                }
            }
            scanner.close();
        } catch (FileNotFoundException exception) {
            exception.printStackTrace();
        }

        if (colorHex.equals("")){
            System.out.println("Couldn't find the color name specified!");
        }

        return Color.valueOf(colorHex);
    }

}
