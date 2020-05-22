package com.example.cgpaucc;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.loader.app.LoaderManager;
import androidx.core.app.NavUtils;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import data.gpaContract.gpaEntry;

/**
 * Allows user to create a new course or edit an existing one.
 */

public class EditorActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    /**
     * boolean to show whether form has been altered
     */
    private boolean mHasChanged = false;

    /**
     * Content URI for the existing course (null if it's a new course)
     */
    private Uri mCurrentUri;

    private static final int EXISTING_LOADER = 0;

    /**
     * EditText field to enter the course code
     */
    private EditText mCourseCodeEditText;

    /**
     * EditText field to enter the course title
     */
    private EditText mCourseTitleEditText;

    /**
     * EditText field to enter the course credit hours
     */
    private EditText mCreditHoursEditText;

    /**
     * EditText field to enter the course grade
     */
    private Spinner mGradeSpinner;

    /**
     * Semester variable to indicate which semester to work on.
     */
    int semester;

    /**
     * Grade of the course.
     */
    private double mGrade = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        //Examine the intent that was used to launch this activity
        //in order to figure out if we're creating a new course or editing an existing one.
        Intent intent = getIntent();
        Uri currentUri = intent.getData();
        mCurrentUri = currentUri;

        //if the intent DOES NOT contain a course content URI, then we know that we are
        //creating a new course.
        if (currentUri == null) {
            //This is a new course, so change the app bar to say "Add a course"
            setTitle(getString(R.string.editor_activity_title_new_course));
            //Get Semester value from SemesterPreview Activity
            semester = getIntent().getExtras().getInt("SEM_KEY");

            // Invalidate the options menu, so the "Delete" menu option can be hidden.
            // (It doesn't make sense to delete a course that hasn't been created yet.)
            invalidateOptionsMenu();
        } else {
            //Otherwise this is an existing Course, so change app bar to say "Edit Course"
            setTitle(getString(R.string.editor_activity_title_edit_course));
            getSupportLoaderManager().initLoader(EXISTING_LOADER, null, this);

        }


        // Find all relevant views that we will need to read user input from
        mCourseCodeEditText = (EditText) findViewById(R.id.edit_course_code);
        mCourseTitleEditText = (EditText) findViewById(R.id.edit_course_title);
        mCreditHoursEditText = (EditText) findViewById(R.id.edit_course_credit);
        mGradeSpinner = (Spinner) findViewById(R.id.spinner_grade);

        mCourseCodeEditText.setOnTouchListener(mTouchListener);
        mCourseTitleEditText.setOnTouchListener(mTouchListener);
        mCreditHoursEditText.setOnTouchListener(mTouchListener);
        mGradeSpinner.setOnTouchListener(mTouchListener);

        setupSpinner();


    }

    /**
     * Setup the dropdown spinner that allows the user to select the grade of the course.
     */
    private void setupSpinner() {
        // Create adapter for spinner. The list options are from the String array it will use
        // the spinner will use the default layout
        ArrayAdapter gradeSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_grade_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        gradeSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mGradeSpinner.setAdapter(gradeSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mGradeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                if (!TextUtils.isEmpty(selection)) {

                    if (selection.equals(getString(R.string.grade_A))) {
                        mGrade = gpaEntry.GRADE_A; // A
                    } else if (selection.equals(getString(R.string.grade_B1))) {
                        mGrade = gpaEntry.GRADE_B1; // B+
                    } else if (selection.equals(getString(R.string.grade_B2))) {
                        mGrade = gpaEntry.GRADE_B2; // B
                    } else if (selection.equals(getString(R.string.grade_C1))) {
                        mGrade = gpaEntry.GRADE_C1; // C+
                    } else if (selection.equals(getString(R.string.grade_C2))) {
                        mGrade = gpaEntry.GRADE_C2; // C
                    } else if (selection.equals(getString(R.string.grade_D1))) {
                        mGrade = gpaEntry.GRADE_D1; // D+
                    } else if (selection.equals(getString(R.string.grade_D2))) {
                        mGrade = gpaEntry.GRADE_D2; // D
                    } else if (selection.equals(getString(R.string.grade_E))) {
                        mGrade = gpaEntry.GRADE_E; // E
                    } else {
                        mGrade = gpaEntry.INACTIVE; // Inactive
                    }

                }
            }

            // Because AdapterView is an abstract class, onNothingSelected must be defined
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mGrade = 0; // E
            }
        });
    }

    /**
     * Get user input  from editor and save new course into database.
     */
    private void insertCourse() {
        String codeString = mCourseCodeEditText.getText().toString().trim();
        String titleString = mCourseTitleEditText.getText().toString().trim();
        String creditString = mCreditHoursEditText.getText().toString().trim();

        //close the activity of nothing is inputted
        if (mCurrentUri == null &&
                TextUtils.isEmpty(codeString) && TextUtils.isEmpty(titleString) &&
                TextUtils.isEmpty(creditString)) {
            return;
        }

        // If the course credit is not provided by the user, don't try to parse the string into an
        // integer value. Use 0 by default.
        int credit = 0;
        if (!TextUtils.isEmpty(creditString)) {
            credit = Integer.parseInt(creditString);
        }

        ContentValues values = new ContentValues();
        values.put(gpaEntry.COLUMN_COURSE_CODE, codeString);
        values.put(gpaEntry.COLUMN_COURSE_TITLE, titleString);
        values.put(gpaEntry.COLUMN_CREDIT_HOURS, credit);
        values.put(gpaEntry.COLUMN_COURSE_GRADE, (mGrade));
        values.put(gpaEntry.COLUMN_SEMESTER, semester);
        if (mGrade == 0 || mGrade == 8) {
            values.put(gpaEntry.COLUMN_GRADEPOINT, 0);
        } else {
            values.put(gpaEntry.COLUMN_GRADEPOINT, (((mGrade + 1) / 2) * credit));
        }


        if (mCurrentUri == null) {

            // Insert a new course into the provider, returning the content URI for the new course.
            Uri newUri = getContentResolver().insert(gpaEntry.CONTENT_URI, values);

            // Show a toast message depending on whether or not the insertion was successful
            if (newUri == null) {
                // If the new content URI is null, then there was an error with insertion.
                Toast.makeText(this, getString(R.string.editor_insert_course_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the insertion was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_insert_course_successful),
                        Toast.LENGTH_SHORT).show();
            }

        } else {

            // Otherwise this is an EXISTING course, so update the course with content URI: mCurrentUri
            // and pass in the new ContentValues. Pass in null for the selection and selection args
            // because mCurrentUri will already identify the correct row in the database that
            // we want to modify.
            int rowsAffected = getContentResolver().update(mCurrentUri, values, null, null);

            // Show a toast message depending on whether or not the update was successful.
            if (rowsAffected == 0) {
                // If no rows were affected, then there was an error with the update.
                Toast.makeText(this, getString(R.string.editor_update_course_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the update was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_update_course_successful),
                        Toast.LENGTH_SHORT).show();
            }


        }

    }

    // OnTouchListener that listens for any user touches on a View, implying that they are modifying
    // the view, and we change the mHasChanged boolean to true.
    private View.OnTouchListener mTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mHasChanged = true;
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            // Respond to a click on the "Save" menu option
            case R.id.action_save:
                //save course to database
                insertCourse();
                //Exit activity
                finish();
                return true;
            case R.id.action_delete:
                // Pop up confirmation dialog for deletion
                showDeleteConfirmationDialog();

                return true;
            case android.R.id.home:
                // If the course hasn't changed, continue with navigating up to parent activity
                // which is the {@link CatalogActivity}.
                if (!mHasChanged) {
                    NavUtils.navigateUpFromSameTask(EditorActivity.this);
                    return true;
                }

                // Otherwise if there are unsaved changes, setup a dialog to warn the user.
                // Create a click listener to handle the user confirming that
                // changes should be discarded.
                DialogInterface.OnClickListener discardButtonClickListener =
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                // User clicked "Discard" button, navigate to parent activity.
                                NavUtils.navigateUpFromSameTask(EditorActivity.this);
                            }
                        };

                // Show a dialog that notifies the user they have unsaved changes
                showUnsavedChangesDialog(discardButtonClickListener);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {

        // Since the editor shows all course attributes, define a projection that contains
        // all columns from the course table
        String[] projection = {
                gpaEntry._ID,
                gpaEntry.COLUMN_COURSE_CODE,
                gpaEntry.COLUMN_COURSE_TITLE,
                gpaEntry.COLUMN_COURSE_GRADE,
                gpaEntry.COLUMN_CREDIT_HOURS,
                gpaEntry.COLUMN_SEMESTER
        };

        // This loader will execute the ContentProvider's query method on a background thread
        return new CursorLoader(this,   // Parent activity context
                mCurrentUri,         // Query the content URI for the current course
                projection,             // Columns to include in the resulting Cursor
                null,                   // No selection clause
                null,                   // No selection arguments
                null);                  // Default sort order
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        // Proceed with moving to the first row of the cursor and reading data from it
        // (This should be the only row in the cursor)
        if (data.moveToFirst()) {
            // Find the columns of course attributes that we're interested in
            int codeColumnIndex = data.getColumnIndex(gpaEntry.COLUMN_COURSE_CODE);
            int titleColumnIndex = data.getColumnIndex(gpaEntry.COLUMN_COURSE_TITLE);
            int gradeColumnIndex = data.getColumnIndex(gpaEntry.COLUMN_COURSE_GRADE);
            int creditColumnIndex = data.getColumnIndex(gpaEntry.COLUMN_CREDIT_HOURS);
            int semesterColumnIndex = data.getColumnIndex(gpaEntry.COLUMN_SEMESTER);

            // Extract out the value from the Cursor for the given column index
            String code = data.getString(codeColumnIndex);
            String title = data.getString(titleColumnIndex);
            int grade = data.getInt(gradeColumnIndex);
            int credit = data.getInt(creditColumnIndex);
            int semesterL = data.getInt(semesterColumnIndex);

            // Update the views on the screen with the values from the database
            mCourseCodeEditText.setText(code);
            mCourseTitleEditText.setText(title);
            mCreditHoursEditText.setText(Integer.toString(credit));
            semester = semesterL;

            // Grade is a dropdown spinner, so map the constant value from the database
            // into one of the dropdown options
            // Then call setSelection() so that option is displayed on screen as the current selection.
            switch (grade) {
                case gpaEntry.GRADE_A:
                    mGradeSpinner.setSelection(0);
                    break;
                case gpaEntry.GRADE_B1:
                    mGradeSpinner.setSelection(1);
                    break;
                case gpaEntry.GRADE_B2:
                    mGradeSpinner.setSelection(2);
                    break;
                case gpaEntry.GRADE_C1:
                    mGradeSpinner.setSelection(3);
                    break;
                case gpaEntry.GRADE_C2:
                    mGradeSpinner.setSelection(4);
                    break;
                case gpaEntry.GRADE_D1:
                    mGradeSpinner.setSelection(5);
                    break;
                case gpaEntry.GRADE_D2:
                    mGradeSpinner.setSelection(6);
                    break;
                case gpaEntry.GRADE_E:
                    mGradeSpinner.setSelection(7);
                default:
                    mGradeSpinner.setSelection(8);
                    break;
            }


        }


    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

        mCourseCodeEditText.getText().clear();
        mCourseTitleEditText.getText().clear();
        mCreditHoursEditText.getText().clear();
        mGradeSpinner.setAdapter(null);

    }

    private void showUnsavedChangesDialog(
            DialogInterface.OnClickListener discardButtonClickListener) {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.unsaved_changes_dialog_msg);
        builder.setPositiveButton(R.string.discard, discardButtonClickListener);
        builder.setNegativeButton(R.string.keep_editing, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Keep editing" button, so dismiss the dialog
                // and continue editing the course.
                if (dialog != null) {
                    dialog.dismiss();
                }
            }
        });

        // Create and show the AlertDialog
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public void onBackPressed() {
        // If the course hasn't changed, continue with handling back button press
        if (!mHasChanged) {
            super.onBackPressed();
            return;
        }

        // Otherwise if there are unsaved changes, setup a dialog to warn the user.
        // Create a click listener to handle the user confirming that changes should be discarded.
        DialogInterface.OnClickListener discardButtonClickListener =
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // User clicked "Discard" button, close the current activity.
                        finish();
                    }
                };

        // Show dialog that there are unsaved changes
        showUnsavedChangesDialog(discardButtonClickListener);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
        // If this is a new course, hide the "Delete" menu item.
        if (mCurrentUri == null) {
            MenuItem menuItem = menu.findItem(R.id.action_delete);
            menuItem.setVisible(false);
        }
        return true;
    }

    private void showDeleteConfirmationDialog() {
        // Create an AlertDialog.Builder and set the message, and click listeners
        // for the positive and negative buttons on the dialog.
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(R.string.delete_dialog_msg);
        builder.setPositiveButton(R.string.delete, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Delete" button, so delete the course.
                deleteCourse();
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User clicked the "Cancel" button, so dismiss the dialog
                // and continue editing the course.
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
     * Perform the deletion of the course in the database.
     */
    private void deleteCourse() {
        // Only perform the delete if this is an existing course.
        if (mCurrentUri != null) {
            // Call the ContentResolver to delete the course at the given content URI.
            // Pass in null for the selection and selection args because the mCurrentUri
            // content URI already identifies the course that we want.
            int rowsDeleted = getContentResolver().delete(mCurrentUri, null, null);
            // Show a toast message depending on whether or not the delete was successful.
            if (rowsDeleted == 0) {
                // If no rows were deleted, then there was an error with the delete.
                Toast.makeText(this, getString(R.string.editor_delete_course_failed),
                        Toast.LENGTH_SHORT).show();
            } else {
                // Otherwise, the delete was successful and we can display a toast.
                Toast.makeText(this, getString(R.string.editor_delete_course_successful),
                        Toast.LENGTH_SHORT).show();
            }
        }

        //Exit activity
        finish();

    }

}