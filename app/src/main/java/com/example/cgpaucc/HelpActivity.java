package com.example.cgpaucc;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;

import data.helperAdapter;
import data.helperData;

public class HelpActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        ArrayList<helperData> helpOptions = new ArrayList<>();

        setContentView(R.layout.activity_help);

        helpOptions.add(new helperData(getString(R.string.About)));

        helpOptions.add(new helperData(getString(R.string.help1), R.drawable.help1));

        helpOptions.add(new helperData(getString(R.string.help2), R.drawable.help2));

        helpOptions.add(new helperData(getString(R.string.help3), R.drawable.help3));

        helpOptions.add(new helperData(getString(R.string.help4), R.drawable.help4));

        helpOptions.add(new helperData(getString(R.string.help5), R.drawable.help5));

        helpOptions.add(new helperData(getString(R.string.help6), R.drawable.help6));

        helpOptions.add(new helperData(getString(R.string.help7), R.drawable.help7));

        helpOptions.add(new helperData(getString(R.string.help8)));

        helperAdapter adapter = new helperAdapter(this, helpOptions);

        ListView listView = (ListView) findViewById(R.id.help_list);

        listView.setAdapter(adapter);

    }


}
