package octillect.styles;

import java.io.File;
import java.io.FileInputStream;

import javafx.scene.text.Font;

/**
 * [1] To use in <b>CSS</b>, use the following attributes:
 * <pre>
 * -fx-font-family: 'Family Name';
 * -fx-font-weight: bold;
 * -fx-font-style: italic;
 * </pre>
 *
 * [2] To use in name <b>code</b>, use the following names:
 * <pre>
 * "Family Name Regular"
 * "Family Name Italic"
 * "Family Name Bold"
 * "Family Name Bold Italic"
 * </pre>
 */
public class Fonts {

    private static final String PRODUCT_SANS_LOCATION    = "./src/octillect/resources/Product-Sans/";
    private static final String PRODUCT_SANS_REGULAR     = PRODUCT_SANS_LOCATION + "Product-Sans-Regular.ttf";
    private static final String PRODUCT_SANS_ITALIC      = PRODUCT_SANS_LOCATION + "Product-Sans-Italic.ttf";
    private static final String PRODUCT_SANS_BOLD        = PRODUCT_SANS_LOCATION + "Product-Sans-Bold.ttf";
    private static final String PRODUCT_SANS_BOLD_ITALIC = PRODUCT_SANS_LOCATION + "Product-Sans-Bold-Italic.ttf";

    /**
     * Loads member fonts into memory.
     */
    public static void load() {
        try {
            Font.loadFont(new FileInputStream(new File(PRODUCT_SANS_REGULAR)), 16);
            Font.loadFont(new FileInputStream(new File(PRODUCT_SANS_ITALIC)), 16);
            Font.loadFont(new FileInputStream(new File(PRODUCT_SANS_BOLD)), 16);
            Font.loadFont(new FileInputStream(new File(PRODUCT_SANS_BOLD_ITALIC)), 16);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
