package data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.cgpaucc.R;

import java.util.List;


// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views
public class SemAdapter extends
        RecyclerView.Adapter<SemAdapter.ViewHolder> {

    // Store a member variable for the contacts
    private List<Sem> mSem;

    private onSemListener mOnSemListener;

    // Pass in the contact array into the constructor
    public SemAdapter(List<Sem> sems, onSemListener onSemListener) {
        this.mOnSemListener = onSemListener;
        mSem = sems;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View semView = inflater.inflate(R.layout.sem_list_item, parent, false);

        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(semView, mOnSemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SemAdapter.ViewHolder holder, int position) {

        // Get the data model based on position
        Sem sem = mSem.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.nameTextView;
        ImageView imageView = holder.default_Image;
        textView.setText(sem.getSemesterText());

        if(sem.hasImage()) {
            imageView.setImageResource(sem.getImg());
            imageView.setVisibility(View.VISIBLE);
        }
        else{

            imageView.setVisibility(View.GONE);

        }


    }


    @Override
    public int getItemCount() {
        return mSem.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView nameTextView;
        public ImageView default_Image;

        onSemListener onSemListener;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView, onSemListener OnSemListener) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            nameTextView = (TextView) itemView.findViewById(R.id.Sem_text_view);
            default_Image = (ImageView) itemView.findViewById(R.id.image);
            this.onSemListener = OnSemListener;
            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {

        onSemListener.onSemClick(getAdapterPosition());

        }
    }

    public interface onSemListener{

         void onSemClick(int position);


    }

}