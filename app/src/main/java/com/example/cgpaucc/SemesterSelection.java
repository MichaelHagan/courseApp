package com.example.cgpaucc;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class SemesterSelection extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview_main);
        /**
         * TextView objects for each semester
         */
        TextView s101 = (TextView) findViewById(R.id.s101);
        TextView s102 = (TextView) findViewById(R.id.s102);
        TextView s201 = (TextView) findViewById(R.id.s201);
        TextView s202 = (TextView) findViewById(R.id.s202);
        TextView s301 = (TextView) findViewById(R.id.s301);
        TextView s302 = (TextView) findViewById(R.id.s302);
        TextView s401 = (TextView) findViewById(R.id.s401);
        TextView s402 = (TextView) findViewById(R.id.s402);

        /**
         * Click Listeners for each semester passing in corresponding semester integer value
         */

        s101.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent Intent = new Intent(SemesterSelection.this,SemesterPreview.class);
        Intent.putExtra("SEM_KEY", 1);
        startActivity(Intent);
    }
});

        s102.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(SemesterSelection.this,SemesterPreview.class);
                Intent.putExtra("SEM_KEY", 2);
                startActivity(Intent);
            }
        });

        s201.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(SemesterSelection.this,SemesterPreview.class);
                Intent.putExtra("SEM_KEY", 3);
                startActivity(Intent);
            }
        });

        s202.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(SemesterSelection.this,SemesterPreview.class);
                Intent.putExtra("SEM_KEY", 4);
                startActivity(Intent);
            }
        });

        s301.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(SemesterSelection.this,SemesterPreview.class);
                Intent.putExtra("SEM_KEY", 5);
                startActivity(Intent);
            }
        });

        s302.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(SemesterSelection.this,SemesterPreview.class);
                Intent.putExtra("SEM_KEY", 6);
                startActivity(Intent);
            }
        });

        s401.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(SemesterSelection.this,SemesterPreview.class);
                Intent.putExtra("SEM_KEY", 7);
                startActivity(Intent);
            }
        });

        s402.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Intent = new Intent(SemesterSelection.this,SemesterPreview.class);
                Intent.putExtra("SEM_KEY", 8);
                startActivity(Intent);
            }
        });





    }
}
