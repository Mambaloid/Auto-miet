package ru.labon.automiet.controllers;

import android.os.Bundle;

import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.labon.automiet.R;
import ru.labon.automiet.adapters.AccountListAdapter;
import ru.labon.automiet.models.ExpandableAccountGroup;
import ru.labon.automiet.models.ExpandableAccountItem;

/**
 * Created by HP on 02.10.2017.
 */

public class AccountFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.recycler_view, container, false);
        List<ExpandableAccountGroup> groups = getGroups();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        AccountListAdapter adapter = new AccountListAdapter(groups, getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        return view;
    }

    public List<ExpandableAccountGroup> getGroups() {
        List<ExpandableAccountGroup> result = new ArrayList<>();
        List<ExpandableAccountItem> items = new ArrayList<>();
        items.add(new ExpandableAccountItem("Фамилия","Смирнов"));
        items.add(new ExpandableAccountItem("Имя","Никита"));
        items.add(new ExpandableAccountItem("Отчество","Владимирович"));

        result.add(new ExpandableAccountGroup("Личные данные",items));
        result.add(new ExpandableAccountGroup("Медицнское заключение",items));
        result.add(new ExpandableAccountGroup("Водительское удостоверение",items));
        result.add(new ExpandableAccountGroup("Сведения об автошколе",items));
        return result;
    }
}
