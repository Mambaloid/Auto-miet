package ru.labon.automiet.controllers;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ru.labon.automiet.R;
import ru.labon.automiet.adapters.VMAdapter;
import ru.labon.automiet.helpers.FullHelper;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.models.ThemeAb;

public class VideomaterialFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.videomaterial_fragment, container, false);
        MainDbHelper dbHelper = MainDbHelper.getInstance(getContext());


        List<File> videos = FullHelper.getAllVideos(getActivity());
        if (videos.size() > 0) {
            Collections.sort(videos, new Comparator<File>() {
                @Override
                public int compare(File file1, File file2) {
                    Integer fileName1 = Integer.parseInt(file1.getName().replace(".mp4", ""));
                    Integer fileName2 = Integer.parseInt(file2.getName().replace(".mp4", ""));
                    return fileName1.compareTo(fileName2);
                }
            });
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
            LinearLayoutManager llm = new LinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(llm);
            List<ThemeAb> list = ThemeAb.getAll(dbHelper);
            VMAdapter adapter = new VMAdapter(list, videos, getActivity());
            recyclerView.setItemViewCacheSize(list.size());
            recyclerView.setAdapter(adapter);
        } else {
            view.findViewById(R.id.recycler_view).setVisibility(View.GONE);
            view.findViewById(R.id.empty_view).setVisibility(View.VISIBLE);
        }
        return view;
    }
}

