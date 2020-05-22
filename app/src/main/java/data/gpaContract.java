package data;

import android.net.Uri;
import android.provider.BaseColumns;


public class gpaContract {

    private gpaContract() {
    }


    public static final class gpaEntry implements BaseColumns {
        /**
         * Content authority constant
         */

        public static final String CONTENT_AUTHORITY = "com.example.cgpaucc";
        public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
        public static final String PATH_CGPAUCC = "cgpaucc";

        /**
         * The content URI to access the data in the provider
         */

        public static final Uri CONTENT_URI = Uri.withAppendedPath
                (BASE_CONTENT_URI, PATH_CGPAUCC);

        public final static String TABLE_NAME = "cgpaucc";
        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_COURSE_CODE = "code";
        public final static String COLUMN_COURSE_TITLE = "title";
        public final static String COLUMN_COURSE_GRADE = "grade";
        public final static String COLUMN_CREDIT_HOURS = "hours";
        public final static String COLUMN_GRADEPOINT = "gradepoint";
        public final static String COLUMN_SEMESTER = "semester";
        public static final int GRADE_E = 0;
        public static final int GRADE_D2 = 1;
        public static final int GRADE_D1 = 2;
        public static final int GRADE_C2 = 3;
        public static final int GRADE_C1 = 4;
        public static final int GRADE_B2 = 5;
        public static final int GRADE_B1 = 6;
        public static final int GRADE_A = 7;
        public static final int INACTIVE = 8;

    }

    public static double isValidGrade(int grade) {
        switch (grade) {
            case gpaEntry.GRADE_A:
                return (gpaEntry.GRADE_A / 2);
            case gpaEntry.GRADE_B1:
                return (gpaEntry.GRADE_B1 / 2);
            case gpaEntry.GRADE_B2:
                return (gpaEntry.GRADE_B2 / 2);
            case gpaEntry.GRADE_C1:
                return (gpaEntry.GRADE_C1 / 2);
            case gpaEntry.GRADE_C2:
                return (gpaEntry.GRADE_C2 / 2);
            case gpaEntry.GRADE_D1:
                return (gpaEntry.GRADE_D1 / 2);
            case gpaEntry.GRADE_D2:
                return (gpaEntry.GRADE_D2 / 2);
            case gpaEntry.GRADE_E:
                return (gpaEntry.GRADE_E / 2);
            case gpaEntry.INACTIVE:
                return (gpaEntry.INACTIVE / 2);
        }


        return 9;
    }


}
