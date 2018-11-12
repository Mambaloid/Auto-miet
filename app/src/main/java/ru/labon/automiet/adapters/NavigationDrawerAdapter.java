package ru.labon.automiet.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import ru.labon.automiet.R;

public class NavigationDrawerAdapter extends BaseAdapter {

    private String[] dataList;
    private List<Drawable> imageList = new ArrayList<>();
    private Context context;
    private LayoutInflater lInflater;
    private Integer[] blockedViews = {4};

    public NavigationDrawerAdapter(Context context) {
        this.context = context;
        Resources resources = context.getResources();
        this.dataList = resources.getStringArray(R.array.string_array_menu);
        this.imageList.add(resources.getDrawable(R.drawable.ic_item_training));
        this.imageList.add(resources.getDrawable(R.drawable.ic_item_tickets));
        this.imageList.add(resources.getDrawable(R.drawable.ic_item_exam));
        this.imageList.add(resources.getDrawable(R.drawable.ic_item_errors));
        this.imageList.add(resources.getDrawable(R.drawable.ic_item_account));
        this.imageList.add(resources.getDrawable(R.drawable.ic_item_rating));
        this.imageList.add(resources.getDrawable(R.drawable.ic_video));
        this.imageList.add(resources.getDrawable(R.drawable.ic_item_about));
        this.imageList.add(resources.getDrawable(R.drawable.ic_item_help));
        this.imageList.add(resources.getDrawable(R.drawable.ic_item_exit));
        lInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return dataList.length;
    }

    @Override
    public Object getItem(int i) {
        return dataList[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if (convertView == null) {
            convertView = lInflater.inflate(R.layout.drawer_list_item, null);
            holder = new ViewHolder();
            holder.textView = (TextView)convertView.findViewById(R.id.textItem);
            holder.imageView = (ImageView) convertView.findViewById(R.id.imageItem);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder)convertView.getTag();
        }
        if (Arrays.asList(blockedViews).contains(i))
            holder.imageView.setColorFilter(Color.GRAY);
        holder.textView.setText(dataList[i]);
        holder.imageView.setImageDrawable(imageList.get(i));
        return convertView;
    }

    @Override
    public CharSequence[] getAutofillOptions() {
        return new CharSequence[0];
    }

    class ViewHolder {
        TextView textView;
        ImageView imageView;
        View view;
    }

}
