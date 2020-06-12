package data;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;
import mike.hagan.cgpaucc.R;


/**
 * {@link gpaCursorAdapter} is an adapter for a list or grid view
 * that uses a {@link Cursor} of gpa data as its data source. This adapter knows
 * how to create list items for each row of data in the {@link Cursor}.
 */
public class gpaCursorAdapter extends CursorAdapter {

    /**
     * Constructs a new {@link gpaCursorAdapter}.
     *
     * @param context The context
     * @param c       The cursor from which to get the data.
     */
    public gpaCursorAdapter(Context context, Cursor c) {
        super(context, c, 0 /* flags */);
    }


    /**
     * Makes a new blank list item view. No data is set (or bound) to the views yet.
     *
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already
     *                moved to the correct position.
     * @param parent  The parent to which the new view is attached to
     * @return the newly created list item view.
     */
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        return LayoutInflater.from(context).inflate(R.layout.list_item, parent, false);

    }

    /**
     * This method binds the gpa data (in the current row pointed to by cursor) to the given
     * list item layout. For example, the name for the current gpa can be set on the name TextView
     * in the list item layout.
     *
     * @param view    Existing view, returned earlier by newView() method
     * @param context app context
     * @param cursor  The cursor from which to get the data. The cursor is already moved to the
     *                correct row.
     */
    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        //find individual views that we want to modify in the list item layout
        TextView course_codeTextView = (TextView) view.findViewById(R.id.code);
        TextView course_titleTextView = (TextView) view.findViewById(R.id.title);
        TextView gradeTextView = (TextView) view.findViewById(R.id.grade);
        TextView creditTextView = (TextView) view.findViewById(R.id.credit);
        TextView gradePointTextView = (TextView) view.findViewById(R.id.gradePoint);

        //find the columns of gpa attributes that we're interested in
        int course_codeColumnIndex = cursor.getColumnIndex(gpaContract.gpaEntry.COLUMN_COURSE_CODE);
        int course_titleColumnIndex = cursor.getColumnIndex(gpaContract.gpaEntry.COLUMN_COURSE_TITLE);
        int gradeColumnIndex = cursor.getColumnIndex(gpaContract.gpaEntry.COLUMN_COURSE_GRADE);
        int creditColumnIndex = cursor.getColumnIndex(gpaContract.gpaEntry.COLUMN_CREDIT_HOURS);
        int gradePointColumnIndex = cursor.getColumnIndex(gpaContract.gpaEntry.COLUMN_GRADEPOINT);

        //Read the gpa attributes from the Cursor for the current gpa
        String course_code = cursor.getString(course_codeColumnIndex);
        String course_title = cursor.getString(course_titleColumnIndex);
        String grade = cursor.getString(gradeColumnIndex);
        String credit = cursor.getString(creditColumnIndex);
        String gradePoint = cursor.getString(gradePointColumnIndex);

        int grade_num = Integer.parseInt(grade);

        //Switch statement to convert grade integer value to grade text
        String g;
        switch (grade_num) {
            case 7:
                g = "A";
                break;

            case 6:
                g = "B+";
                break;
            case 5:
                g = "B";
                break;
            case 4:
                g = "C+";
                break;
            case 3:
                g = "C";
                break;
            case 2:
                g = "D+";
                break;
            case 1:
                g = "D";
                break;
            case 0:
                g = "E";
                break;
            default:
                g = "PENDING";
                break;
        }

        //Update the TextViews with the attributes for the current record
        course_codeTextView.setText(course_code);
        course_titleTextView.setText(course_title);
        creditTextView.setText(credit);
        gradePointTextView.setText(gradePoint);


        if (credit.equals("0")) {
            gradeTextView.setTextSize(9);
            gradeTextView.setText("AUDIT");
        } else if (g.equals("PENDING")) {
            gradeTextView.setTextSize(9);
            gradeTextView.setText("PENDING");
        } else {
            gradeTextView.setTextSize(18);
            gradeTextView.setText(g);
        }


    }

}