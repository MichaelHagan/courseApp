package mike.hagan.cgpaucc;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import com.google.android.material.tabs.TabLayout;
import data.CategoryAdapter;
import data.gpaContract.gpaEntry;
import data.gpaDbHelper;


public class SemesterSelection extends AppCompatActivity {

    boolean mShouldAnimateMenuItem;
    boolean animate;
    gpaDbHelper helper = new gpaDbHelper(this);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Set the content of the activity to use the activity_main.xml layout file
        setContentView(R.layout.activity_preview_main);

        // Find the view pager that will allow the user to swipe between fragments
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);

        viewPager.setOffscreenPageLimit(3);

        // Create an adapter that knows which fragment should be shown on each page
        CategoryAdapter adapter = new CategoryAdapter(this, getSupportFragmentManager());

        // Set the adapter onto the view pager
        viewPager.setAdapter(adapter);

        // Find the tab layout that shows the tabs
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);

        // Connect the tab layout with the view pager. This will
        //   1. Update the tab layout when the view pager is swiped
        //   2. Update the view pager when a tab is selected
        //   3. Set the tab layout's tab names with the view pager's adapter's titles
        //      by calling onPageTitle()
        tabLayout.setupWithViewPager(viewPager);

        checkDB();

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_preview_main, menu);

        if (mShouldAnimateMenuItem && animate) {
            ImageView image = new ImageView(this);
            image.setPadding(16, 16, 16, 16);
            image.setImageResource(R.drawable.baseline_help_white_24);
            menu.getItem(0).setActionView(image); //item in the 0 position
            Animation anim = AnimationUtils.loadAnimation(this, R.anim.animation);
            anim.setAnimationListener(new Animation.AnimationListener() {

                @Override
                public void onAnimationStart(Animation animation) {

                }

                @Override
                public void onAnimationRepeat(Animation animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animation animation) {
                    invalidateOptionsMenu();
                }
            });

            menu.getItem(0).getActionView().startAnimation(anim);

            mShouldAnimateMenuItem = false;

        }


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent help = new Intent(SemesterSelection.this, HelpActivity.class);

        if (animate) {
            /**inserts dummy data into database in order to trigger the checkDB if statement.
             *shuts off animation since the help activity has been accessed
            **/
            SQLiteDatabase db = helper.getWritableDatabase();
            db.execSQL("INSERT INTO " + gpaEntry.TABLE_NAME + "( "
                    + gpaEntry.COLUMN_COURSE_CODE + ", "
                    + gpaEntry.COLUMN_COURSE_TITLE + ", "
                    + gpaEntry.COLUMN_COURSE_GRADE + ", "
                    + gpaEntry.COLUMN_CREDIT_HOURS + ", "
                    + gpaEntry.COLUMN_SEMESTER + ", "
                    + gpaEntry.COLUMN_GRADEPOINT + ")"
                    + " VALUES" + "('0','0',0,0,0,0 )");
        }

        startActivity(help);
        return true;
    }

    public void checkDB() {


        SQLiteDatabase db = helper.getReadableDatabase();

        Cursor mCursor = db.rawQuery("SELECT * FROM " + gpaEntry.TABLE_NAME, null);


        if (mCursor.moveToFirst()) {
            // Do not animate
            animate = false;
            mShouldAnimateMenuItem = false;
        } else {
            // Animate
            animate = true;
            mShouldAnimateMenuItem = true;
        }


    }


}