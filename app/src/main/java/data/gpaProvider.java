package data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import data.gpaContract.gpaEntry;

/**
 * {@link ContentProvider} for cgpas app.
 */

public class gpaProvider extends ContentProvider {

    /** URI matcher code for the content URI for the data table */
    private static final int DATA = 100;

    /** URI matcher code for the content URI for a single data in the table */
    private static final int DATA_ID = 101;

    /**
     * UriMatcher object to match a content URI to a corresponding code.
     * The input passed into the constructor represents the code to return for the root URI.
     * It's common to use NO_MATCH as the input for this case.
     */
    private static final UriMatcher sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

    // Static initializer. This is run the first time anything is called from this class.
    static {
        // The calls to addURI() go here, for all of the content URI patterns that the provider
        // should recognize. All paths added to the UriMatcher have a corresponding code to return
        // when a match is found.
        sUriMatcher.addURI(gpaEntry.CONTENT_AUTHORITY,gpaEntry.PATH_CGPAUCC,DATA);
        sUriMatcher.addURI(gpaEntry.CONTENT_AUTHORITY, gpaEntry.PATH_CGPAUCC + "/#", DATA_ID);
    }

    /** Tag for the log messages */
    public static final String LOG_TAG = gpaProvider.class.getSimpleName();

    /**Database Helper object*/
    private gpaDbHelper mDbHelper;

    /**
     * Initialize the provider and the database helper object.
     */
    @Override
    public boolean onCreate() {
        mDbHelper = new gpaDbHelper(getContext());

        return true;
    }

    /**
     * Perform the query for the given URI. Use the given projection, selection, selection arguments, and sort order.
     */
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
                        String sortOrder) {
        //Get readable database
        SQLiteDatabase database = mDbHelper.getReadableDatabase();

        //This cursor will hold the result of the query
        Cursor cursor;

        //Figure out if the URI matcher can match the URI to a specific code
        int match = sUriMatcher.match(uri);
        switch (match) {
            case DATA:
                //For the cgpa code, query the cgpa table directly with the given
                //projection, selection, selection arguments and sort order. The cursor
                //could contain multiple rows of the cgpa table.

                cursor = database.query(gpaContract.gpaEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null,
                        null, sortOrder);
                break;
            case DATA_ID:

                selection = gpaContract.gpaEntry._ID + "=?";
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};
                cursor = database.query(gpaContract.gpaEntry.TABLE_NAME, projection,
                        selection, selectionArgs, null, null,
                        null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Cannot query unknown URI " + uri);
        }

        //Set notification URI on the Cursor
        //So we know what content URI the Cursor was created for.
        //If the data at this URI changes, then we know we need to update the Cursor.
        cursor.setNotificationUri(getContext().getContentResolver(),uri);

        //return the cursor
        return cursor;

    }

    /**
     * Insert new data into the provider with the given ContentValues.
     */
    @Override
    public Uri insert(Uri uri, ContentValues contentValues) {
        final int match = sUriMatcher.match(uri);
        switch (match) {
            case DATA:
                return insertData(uri, contentValues);
            default:
                throw new IllegalArgumentException("Insertion is not supported for " + uri);
        }
    }

    /**
     * Insert a cgpa into the database with the given content values. Return the new content URI
     * for that specific row in the database.
     */
    private Uri insertData(Uri uri, ContentValues values) {
        // Get writeable database
        SQLiteDatabase database = mDbHelper.getWritableDatabase();

        // Insert the new cgpa with the given values
        long id = database.insert(gpaContract.gpaEntry.TABLE_NAME, null, values);

        // If the ID is -1, then the insertion failed. Log an error and return null.
        if (id == -1) {
            Log.e(LOG_TAG, "Failed to insert row for " + uri);
            return null;}

        //Notify all listeners that the data has changed for the cgpa content URI
        getContext().getContentResolver().notifyChange(uri,null);

        // Once we know the ID of the new row in the table,
        // return the new URI with the ID appended to the end of it
        return ContentUris.withAppendedId(uri, id);
    }

    /**
     * Updates the data at the given selection and selection arguments, with the new ContentValues.
     */
    @Override
    public int update(Uri uri, ContentValues contentValues, String selection, String[] selectionArgs) {
        final int match = sUriMatcher.match(uri);

        switch (match) {
            case DATA:
                return updatecgpa(contentValues, uri, selection, selectionArgs);
            case DATA_ID:
                //selection variable holds the where clause
                selection = gpaEntry._ID + " =? ";

                //the selectionArgs holds an array of values for the selection clause
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                return updatecgpa(contentValues, uri, selection, selectionArgs);
            default:
                throw new IllegalArgumentException("Insertion is not supported " + uri);

        }
    }

    /**
     * a helper method to help update  cgpa data into the database with
     *
     * @param selection which contents the where clause
     * @param cValues   the data to be stored
     * @return selectionArgs which contents the values for the where clause
     */
    private int updatecgpa(ContentValues cValues,Uri uri, String selection, String[] selectionArgs) {
        //get access to the database
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //validating data
        //get each value contained in the ContentValues object


        //check that the courseCode value is not empty
        if (cValues.containsKey(gpaEntry.COLUMN_COURSE_CODE)) {
            String code = cValues.getAsString(gpaEntry.COLUMN_COURSE_CODE);
            //validating the name attribute
            if (code.isEmpty()) {
                throw new IllegalStateException("Course code cannot be empty");
            }
        }

        //check that the courseTitle value is not empty
        if (cValues.containsKey(gpaEntry.COLUMN_COURSE_GRADE)) {
            Integer grade = cValues.getAsInteger(gpaEntry.COLUMN_COURSE_GRADE);

            //validation for the gender attribute
            if (grade != gpaContract.isValidGender(grade) && grade == null) {
                throw new IllegalStateException("Grade is not valid");
            }
        }

        //check that the name value is not empty
        if (cValues.containsKey(gpaEntry.COLUMN_CREDIT_HOURS)) {
            Integer credit = cValues.getAsInteger(gpaEntry.COLUMN_CREDIT_HOURS);

            //validation for the weight
            if (credit < 0 && credit != null) {
                throw new IllegalStateException("Credit Hours is invalid");
            }
        }

        //if contentValues has no values then return 0 so that the database is not update
        //since there are no new values
        if (cValues.size() == 0) {
            return 0;
        }

        //notifies the listener that data has changed for this particular cgpaucc uri
        getContext().getContentResolver().notifyChange(uri, null);

        //calling the update method of the sqlitedatabase to update existing data
        return db.update(gpaEntry.TABLE_NAME, cValues, selection, selectionArgs);
    }


    /**
     * Delete the data at the given selection and selection arguments.
     */
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int match = sUriMatcher.match(uri);
        switch (match) {
            case DATA:
                return deleteCourse(uri, selection, selectionArgs);
            case DATA_ID:
                //selection variable holds the where clause
                selection = gpaEntry._ID + " =? ";

                //the selectionArgs holds an array of values for the selection clause
                selectionArgs = new String[]{String.valueOf(ContentUris.parseId(uri))};

                return deleteCourse(uri,selection, selectionArgs);

            default:
                throw new IllegalArgumentException("Deletion is not supported for" + uri);
        }
    }

    /**
     * a helper method to help delete Course data from the database with
     *
     * @param selection which contents the where clause
     * @return selectionArgs which contents the values for the where clause
     */
    private int deleteCourse(Uri uri, String selection, String[] selectionArgs) {
        //get the database object
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        //notifies the listener that data has changed for this particular Course uri
        getContext().getContentResolver().notifyChange(uri, null);

        return db.delete(gpaEntry.TABLE_NAME, selection, selectionArgs);
    }

    /**
     * Returns the MIME type of data for the content URI.
     */
    @Override
    public String getType(Uri uri) {
        return null;
    }

}