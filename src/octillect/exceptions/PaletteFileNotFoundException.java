package octillect.exceptions;

public class PaletteFileNotFoundException extends OCTException {

    final static String CODE        = "202";
    final static String DESCRIPTON  = "Couldn't find Palette.css";

    public PaletteFileNotFoundException() {
        super(CODE, DESCRIPTON);
    }

}
