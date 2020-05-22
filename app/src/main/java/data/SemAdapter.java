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


// Creates the basic adapter extending from RecyclerView.Adapter
public class SemAdapter extends
        RecyclerView.Adapter<SemAdapter.ViewHolder> {

    // Stores a member variable for the contacts
    private List<Sem> mSem;

    private onSemListener mOnSemListener;

    // Passes in the contact array into the constructor
    public SemAdapter(List<Sem> sems, onSemListener onSemListener) {
        this.mOnSemListener = onSemListener;
        mSem = sems;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflates the custom layout
        View semView = inflater.inflate(R.layout.sem_list_item, parent, false);

        // Returns a new holder instance
        ViewHolder viewHolder = new ViewHolder(semView, mOnSemListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(SemAdapter.ViewHolder holder, int position) {

        // Gets the data model based on position
        Sem sem = mSem.get(position);

        // Sets item views based on views and data model
        TextView textView = holder.nameTextView;
        ImageView imageView = holder.default_Image;
        textView.setText(sem.getSemesterText());

        if (sem.hasImage()) {
            imageView.setImageResource(sem.getImg());
            imageView.setVisibility(View.VISIBLE);
        } else {

            imageView.setVisibility(View.GONE);

        }


    }


    @Override
    public int getItemCount() {
        return mSem.size();
    }

    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        public TextView nameTextView;
        public ImageView default_Image;

        onSemListener onSemListener;

        // constructor that accepts the entire item row
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

    //OnItem click Listener
    public interface onSemListener {

        void onSemClick(int position);


    }

}