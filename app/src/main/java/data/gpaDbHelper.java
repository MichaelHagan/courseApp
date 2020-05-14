package data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import data.gpaContract.gpaEntry;

public class gpaDbHelper extends SQLiteOpenHelper {

    /**
     * Name of database
     **/

    private static final String DATABASE_NAME = "cgpa.db";

    /**
     * Database version. Must be increased upon schema change
     */

    private static final int DATABASE_VERSION = 1;

    public gpaDbHelper(Context context){

        super(context, DATABASE_NAME,null,DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //CREATE TABLE cgpa (id INTEGER PRIMARY KEY, name TEXT, weight INTEGER);
        //Create a String that contains the SQL statement to create the cgpa table
        String SQL_CREATE_TABLE = "CREATE TABLE " + gpaEntry.TABLE_NAME
                + "(" + gpaEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +gpaEntry.COLUMN_COURSE_CODE + " TEXT NOT NULL, "
                +gpaEntry.COLUMN_COURSE_TITLE+ " TEXT, "
                +gpaEntry.COLUMN_COURSE_GRADE+ " DOUBLE NOT NULL, "
                +gpaEntry.COLUMN_CREDIT_HOURS+ " INTEGER NOT NULL DEFAULT 0, "
                +gpaEntry.COLUMN_SEMESTER+ " INTEGER NOT NULL, "
                +gpaEntry.COLUMN_GRADEPOINT+ " DOUBLE NOT NULL )";

        db.execSQL(SQL_CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }

}
