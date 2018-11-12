package ru.labon.automiet.controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.labon.automiet.R;
import ru.labon.automiet.adapters.RVAdapter;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.models.ThemeAb;

/**
 * Created by HP on 03.10.2017.
 */

public class MaterialFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recycle_view, container, false);
        MainDbHelper dbHelper = MainDbHelper.getInstance(getContext());

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(llm);
        List<ThemeAb> list = ThemeAb.getAll(dbHelper);
        RVAdapter adapter = new RVAdapter(list, getActivity());
        recyclerView.setItemViewCacheSize(list.size());
        recyclerView.setAdapter(adapter);
        return view;
    }


}

