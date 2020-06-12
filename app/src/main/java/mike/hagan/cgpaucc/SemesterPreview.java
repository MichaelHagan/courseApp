/*
 * Copyright (C) 2016 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package mike.hagan.cgpaucc;

import android.content.ContentUris;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import data.gpaDbHelper;
import data.gpaContract.gpaEntry;
import data.gpaCursorAdapter;

/**
 * Displays list of courses that were entered and stored in the app.
 */

public class SemesterPreview extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * Semester variable to indicate which semester to work on.
     */
    int semester;

    private static final int COURSE_LOADER = 0;

    gpaCursorAdapter mCursorAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_semester_preview);

        semester = getIntent().getExtras().getInt("SEM_KEY");

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SemesterPreview.this, EditorActivity.class);
                intent.putExtra("SEM_KEY", semester);
                startActivity(intent);
            }
        });

        //Find the ListView which will be populated with the course data
        ListView courseListView = (ListView) findViewById(R.id.list);

        //Find and set empty view on the ListView so that it only shows when the list has 0 items
        View emptyView = findViewById(R.id.empty_view);
        courseListView.setEmptyView(emptyView);

        //Setup an Adapter to create a list item for each row of course data in the Cursor
        //There is no course data yet (until the loader finishes) so pass in null for the Cursor
        mCursorAdapter = new gpaCursorAdapter(this, null);
        courseListView.setAdapter(mCursorAdapter);

        //Setup item click Listener
        courseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //Create new intent to go to {@Link EditorActivity}
                Intent intent = new Intent(SemesterPreview.this, EditorActivity.class);

                //Form the content URI that represents the specific course that was clicked on,
                //by appending the "id" (passed as input to this method) onto the
                //{@Link gpaEntry#CONTENT_URI}.
                //For example, the URI would be "content://mike.hagan.cgpaucc/cgpaucc/2"
                //if the course with ID 2 was clicked on.

                Uri currentCourseUri = ContentUris.withAppendedId(gpaEntry.CONTENT_URI, id);

                //Set the URI on the data field of the intent
                intent.setData(currentCourseUri);

                //Launch the {@Link EditorActivity} to display the data for the current Course.
                startActivity(intent);
            }
        });
        //Kick off the loader
        getSupportLoaderManager().initLoader(COURSE_LOADER, null, this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        switch (semester) {

            case 1:
                setTitle("100 Semester 1");
                break;
            case 2:
                setTitle("100 Semester 2");
                break;
            case 3:
                setTitle("200 Semester 1");
                break;
            case 4:
                setTitle("200 Semester 2");
                break;
            case 5:
                setTitle("300 Semester 1");
                break;
            case 6:
                setTitle("300 Semester 2");
                break;
            case 7:
                setTitle("400 Semester 1");
                break;
            default:
                setTitle("400 Semester 2");
                break;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {

            case R.id.action_delete_all_entries:
                showDeleteConfirmationDialog();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        //Define a projection that specifies the columns from the table we care about.
        String[] projection = {
                gpaEntry._ID,
                gpaEntry.COLUMN_COURSE_GRADE,
                gpaEntry.COLUMN_COURSE_TITLE,
                gpaEntry.COLUMN_COURSE_CODE,
                gpaEntry.COLUMN_CREDIT_HOURS,
                gpaEntry.COLUMN_GRADEPOINT

        };

        //Define Selection arguments to query semester specific records
        String selection = gpaEntry.COLUMN_SEMESTER + "=?";
        String[] selectionArgs = {Integer.toString(semester)};

        //This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   //Parent activity context
                gpaEntry.CONTENT_URI,           //Provider content URI to query
                projection,                     //Columns to include in the resulting Cursor
                selection,                  //No selection clause
                selectionArgs,              //No selection arguments
                null);                //Default sort order

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        //Update {@Link gpaCursorAdapter with this new cursor containing updated course data}
        mCursorAdapter.swapCursor(data);
        Retriever();
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mCursorAdapter.swapCursor(null);
    }

    /**
     * Helper method to delete all courses in the database.
     */
    private void deleteAllCourses() {

        //Define Selection arguments to delete semester specific records
        String selection = gpaEntry.COLUMN_SEMESTER + "=?";
        String[] selectionArgs = {Integer.toString(semester)};

        int rowsDeleted = getContentResolver().delete(gpaEntry.CONTENT_URI, selection, selectionArgs);
        Log.v("SemesterPreview", rowsDeleted + " rows deleted from database");

    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.deleteAll_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the Course.
                deleteAllCourses();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the Course.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    /**
     * Function to retrieve gpa and credit data to calculate gpa and cgpa
     */

    private void Retriever() {

        gpaDbHelper gpaRetriever = new gpaDbHelper(this);

        SQLiteDatabase db = gpaRetriever.getReadableDatabase();

        //Define Selection arguments to query semester specific records
        String selection = null;
        String[] selectionArgs;

        String[] projection = {
                gpaEntry.COLUMN_GRADEPOINT,
                gpaEntry.COLUMN_CREDIT_HOURS,
                gpaEntry.COLUMN_SEMESTER
        };

        //Defines the selection arguments depending on semester
        switch (semester) {
            case 1:
                selection = gpaEntry.COLUMN_SEMESTER + "=?";
                selectionArgs = new String[]{"1"};
                break;

            case 2:
                selection = gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?";
                selectionArgs = new String[]{"1", "2"};
                break;

            case 3:
                selection = gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?";
                selectionArgs = new String[]{"1", "2", "3"};
                break;

            case 4:
                selection = gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?";
                selectionArgs = new String[]{"1", "2", "3", "4"};
                break;
            case 5:
                selection = gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?";
                selectionArgs = new String[]{"1", "2", "3", "4", "5"};
                break;

            case 6:
                selection = gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?";
                selectionArgs = new String[]{"1", "2", "3", "4", "5", "6"};
                break;

            case 7:
                selection = gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?";
                selectionArgs = new String[]{"1", "2", "3", "4", "5", "6", "7"};
                break;

            default:
                selection = gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?" + " OR " + gpaEntry.COLUMN_SEMESTER + "=?";
                selectionArgs = new String[]{"1", "2", "3", "4", "5", "6", "7", "8"};
                break;

        }


        Cursor retrieve = db.query(
                gpaEntry.TABLE_NAME,
                projection,
                selection,
                selectionArgs,
                null,
                null,
                null
        );

        try {

            int creditColumnIndex = retrieve.getColumnIndex(gpaEntry.COLUMN_CREDIT_HOURS);
            int gradePointColumnIndex = retrieve.getColumnIndex(gpaEntry.COLUMN_GRADEPOINT);
            int semesterColumnIndex = retrieve.getColumnIndex(gpaEntry.COLUMN_SEMESTER);

            double totalGradepoint = 0;
            double semesterGradepoint = 0;
            int semesterCredit = 0;
            int totalCredit = 0;

            while (retrieve.moveToNext()) {

                /**
                 * Calculate gradePoint and credit for a given semester and
                 * total semesters
                 */
                if (retrieve.getInt(semesterColumnIndex) == semester) {
                    semesterCredit += retrieve.getInt(creditColumnIndex);
                    semesterGradepoint += retrieve.getDouble(gradePointColumnIndex);
                }

                totalCredit += retrieve.getInt(creditColumnIndex);
                totalGradepoint += retrieve.getDouble(gradePointColumnIndex);

            }

            //Calculates gpa and cgpa if Semester records are present
            if (semesterCredit != 0 || semesterGradepoint != 0) {
                double gpa = ((semesterGradepoint / (semesterCredit * 4)) * 4);
                double cgpa = ((totalGradepoint / (totalCredit * 4)) * 4);
                TextView gpaTextView = (TextView) findViewById(R.id.gpa);
                TextView cgpaTextView = (TextView) findViewById(R.id.cgpa);

                gpaTextView.setText("GPA: " + String.format("%.4f", gpa));
                cgpaTextView.setText("CGPA: " + String.format("%.4f", cgpa));

            }

            //Clears the gpa and cgpa TextViews when semester records are empty
            if (semesterCredit == 0) {
                TextView gpaTextView = (TextView) findViewById(R.id.gpa);
                TextView cgpaTextView = (TextView) findViewById(R.id.cgpa);

                gpaTextView.setText(" ");
                cgpaTextView.setText(" ");


            }


        } finally {
            retrieve.close();
        }


    }


}
