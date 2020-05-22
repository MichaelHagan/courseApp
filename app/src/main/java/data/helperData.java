package data;

public class helperData {

    private String text;
    private int imageId = NO_IMAGE_PROVIDED;
    private static final int NO_IMAGE_PROVIDED = -1;

    public helperData(String conText, int conInt) {
        text = conText;
        imageId = conInt;
    }

    public helperData(String conText) {
        text = conText;
    }

    public String getHelperText() {
        return text;
    }


    public int getImg() {

        return imageId;
    }


    public boolean hasImage() {

        return imageId != NO_IMAGE_PROVIDED;

    }

}