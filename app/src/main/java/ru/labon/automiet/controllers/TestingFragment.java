package ru.labon.automiet.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

import ru.labon.automiet.R;
import ru.labon.automiet.adapters.ListAdapter;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.models.ThemeAb;

/**
 * Created by HP on 03.10.2017.
 */

public class TestingFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.list_view, container, false);

        MainDbHelper dbHelper = MainDbHelper.getInstance(getContext());

        ListView listView = view.findViewById(R.id.list_view);
        final List<ThemeAb> list = ThemeAb.getAll(dbHelper);
        listView.setAdapter(new ListAdapter(list, getActivity(), ListAdapter.TEST));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getActivity(), TestActivity.class);
                intent.putExtra("id_theme", list.get(i).getNum());
                startActivity(intent);
            }
        });
        return view;
    }
}
