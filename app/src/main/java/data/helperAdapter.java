package data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.cgpaucc.R;
import java.util.ArrayList;

public class helperAdapter extends ArrayAdapter<helperData> {

    public helperAdapter(Context context, ArrayList<helperData> sections) {
        super(context, 0, sections);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.help_list_item, parent, false);
        }

        helperData currentData = getItem(position);

        TextView helperTextView = (TextView) listItemView.findViewById(R.id.helper_text);

        helperTextView.setText(currentData.getHelperText());

        ImageView image = (ImageView) listItemView.findViewById(R.id.helper_image);

        if (currentData.hasImage()) {
            image.setImageResource(currentData.getImg());
            image.setVisibility(View.VISIBLE);
        } else {

            image.setVisibility(View.GONE);

        }

        return listItemView;


    }
}
