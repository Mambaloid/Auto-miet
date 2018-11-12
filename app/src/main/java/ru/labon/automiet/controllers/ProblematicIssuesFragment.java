package ru.labon.automiet.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.labon.automiet.App;
import ru.labon.automiet.R;
import ru.labon.automiet.models.Error;

public class ProblematicIssuesFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.problematic_issues_fragment, container, false);
        App.getNetClient().getMyErrors(new Callback<List<Error>>() {
            @Override
            public void onResponse(Call<List<Error>> call, Response<List<Error>> response) {
                if (response.isSuccessful()) {
                    String condition = "";
                    List<Error> errors = response.body();
                    int index = 1;
                    for (Error error : errors) {
                        if (index == errors.size())
                            condition += "(theme_num = " + error.themeNum + " AND in_theme_num = " + error.inThemeNum + ")";
                        else condition += "(theme_num = " + error.themeNum + " AND in_theme_num = " + error.inThemeNum + ") OR ";
                        index++;
                    }
                    Log.d("CANDY_SHOP",condition);
                    ((MainActivity) getActivity()).setFragment();
                    if (!condition.equals("")) {
                        Intent intent = new Intent(getActivity(), TestActivity.class);
                        intent.putExtra("condition", condition);
                        getActivity().startActivity(intent);
                    } else Toast.makeText(getContext(),"У вас нет таких вопросов. Поздравляем!1",Toast.LENGTH_LONG).show();
                } else Toast.makeText(getContext(),"Упс!",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Call<List<Error>> call, Throwable t) {
                Toast.makeText(getContext(),"Упс!",Toast.LENGTH_LONG).show();
            }
        });
        return view;
    }
}
