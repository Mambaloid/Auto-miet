package ru.labon.automiet.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

import ru.labon.automiet.R;

/**
 * Created by HP on 03.10.2017.
 */

public class TestListAdapter extends BaseAdapter {


    private String[] data;
    private Context ctx;
    private LayoutInflater lInflater;
    private int count;

    public TestListAdapter(String[] data, Context ctx, int count) {

        this.data = data;
        this.ctx = ctx;
        this.count = count;
        this.lInflater = (LayoutInflater) ctx
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return count;
    }

    @Override
    public Object getItem(int i) {
        return data[i];
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = lInflater.inflate(R.layout.item_test, parent, false);
        }
        if (!data[i].isEmpty())
            ((TextView) view.findViewById(R.id.answer_text)).setText(data[i]);
        return view;
    }

}
