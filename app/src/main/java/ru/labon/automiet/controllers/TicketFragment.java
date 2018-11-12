package ru.labon.automiet.controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import ru.labon.automiet.R;
import ru.labon.automiet.adapters.TaskAdapter;
import ru.labon.automiet.helpers.FullHelper;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.models.Task;

/**
 * Created by HP on 21.11.2017.
 */

public class TicketFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ticket_fragment, container, false);
        MainDbHelper dbHelper = MainDbHelper.getInstance(getContext());

        List<Task> tasks = Task.getNames(dbHelper);

        if (tasks.isEmpty()) {
            view.findViewById(R.id.recycler_view).setVisibility(View.GONE);
            view.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
            FullHelper.emptyDataDialog(getActivity());
            return view;
        }

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        //LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 3));
        TaskAdapter adapter = new TaskAdapter(tasks, getActivity());
        recyclerView.setAdapter(adapter);

        return view;
    }
}
