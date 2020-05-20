package com.example.cgpaucc;


import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import data.SemAdapter;

/**
 * A simple {@link Fragment} subclass.
 */

public class L100 extends Fragment implements SemAdapter.onSemListener {


    ArrayList<Sem> sem = new ArrayList<>();

    public L100() {
        // Required empty public constructor
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.sem_list, container, false);

        sem.add(new Sem("First Semester",R.drawable.l100s1));
        sem.add(new Sem("Second Semester",R.drawable.l100s2));

        SemAdapter StAd = new SemAdapter(sem, this);

        RecyclerView root = (RecyclerView) rootView.findViewById(R.id.rootview);

        root.setAdapter(StAd);

        root.setLayoutManager(new LinearLayoutManager(getContext()));
/**
        //Sends an intent passing in the semester value for a given semester
        root.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

*/
        return rootView;
    }

    @Override
    public void onSemClick(int position) {

        Intent Intent = new Intent(getActivity(),SemesterPreview.class);

        if(position == 0){
            Intent.putExtra("SEM_KEY", 1);

        }
        else {
            Intent.putExtra("SEM_KEY", 2);

        }

        startActivity(Intent);
    }
}

