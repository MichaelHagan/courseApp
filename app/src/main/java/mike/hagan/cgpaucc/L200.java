package mike.hagan.cgpaucc;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.ArrayList;

import data.Sem;
import data.SemAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class L200 extends Fragment implements SemAdapter.onSemListener{

    ArrayList<Sem> sem = new ArrayList<>();

    public L200() {
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

        sem.add(new Sem("First Semester",R.drawable.l200s1));
        sem.add(new Sem("Second Semester",R.drawable.l200s2));

        SemAdapter StAd = new SemAdapter(sem,this);

        RecyclerView root = (RecyclerView) rootView.findViewById(R.id.rootview);

        root.setAdapter(StAd);

        root.setLayoutManager(new LinearLayoutManager(getContext()));



        return rootView;

    }

    @Override
    public void onSemClick(int position) {

        if(position == 0){
            Intent Intent = new Intent(getActivity(),SemesterPreview.class);
            Intent.putExtra("SEM_KEY", 3);
            startActivity(Intent);
        }
        else {

            Intent Intent = new Intent(getActivity(),SemesterPreview.class);
            Intent.putExtra("SEM_KEY", 4);
            startActivity(Intent);
        }

    }
}
