package ru.labon.automiet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.List;

import ru.labon.automiet.R;
import ru.labon.automiet.models.Rating;

/**
 * Created by Admin on 12.12.2017.
 */

public class RatingAdapter extends BaseAdapter {

    private List<Rating> ratings;
    private Context context;
    private LayoutInflater lInflater;

    public RatingAdapter(
            List<Rating> ratings,
            Context context) {

        this.ratings = ratings;
        this.context = context;

        this.lInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }


    @Override
    public int getCount() {
        return ratings.size();
    }

    @Override
    public Object getItem(int i) {
        return ratings.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.rating_list_item, parent, false);
        }
       if(ratings != null && ratings.get(i) != null){
           Rating rating = ratings.get(i);
           ((TextView)view.findViewById(R.id.rating_name)).setText(rating.fio);
           ((TextView)view.findViewById(R.id.rating_position_text_view)).setText(rating.position+"");
           ((TextView)view.findViewById(R.id.rating_points)).setText(rating.points+"");
       }
        return view;
    }
}
