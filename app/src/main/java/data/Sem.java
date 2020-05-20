package data;

public class Sem {

    private String mSemesterText;
    private int ImageResourceId = NO_IMAGE_PROVIDED;
    private static  final int NO_IMAGE_PROVIDED = -1;

    public Sem(String SemesterText,int icon) {

        mSemesterText = SemesterText;
        ImageResourceId = icon;

          }


    public String getSemesterText() {

        return mSemesterText;

    }

    public int getImg(){

        return ImageResourceId;
    }


    public boolean hasImage(){

        return ImageResourceId != NO_IMAGE_PROVIDED;

    }

}
