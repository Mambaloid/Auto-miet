package ru.labon.automiet.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.labon.automiet.App;
import ru.labon.automiet.R;
import ru.labon.automiet.adapters.RatingAdapter;
import ru.labon.automiet.helpers.FullHelper;
import ru.labon.automiet.models.Rating;

/**
 * Created by HP on 02.10.2017.
 */

public class RatingFragment extends android.support.v4.app.Fragment {

    Button btnProgress;
    SweetAlertDialog pDialog;
    List<Rating> ratings;
    TextView selfPosition;
    ListView ratingList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.raiting_fragment, container, false);

        selfPosition = (TextView) view.findViewById(R.id.your_position_text_view);
        ratingList = (ListView) view.findViewById(R.id.ratingList);

        btnProgress = (Button) view.findViewById(R.id.buttonProgress);
        btnProgress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RatingFragment.this.getContext(), ProgressActivity.class);
                startActivity(intent);
            }
        });

        pDialog = new SweetAlertDialog(this.getContext(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Получение данных");
        pDialog.setContentText("Пожалуйста подождите..");
        pDialog.setCancelable(false);
        pDialog.show();


        App.getNetClient().getRating(new Callback<List<Rating>>() {
            @Override
            public void onResponse(Call<List<Rating>> call, Response<List<Rating>> response) {
                ratings = response.body();
                if (ratings == null || ratings.isEmpty()) fail();
                setRating();
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<Rating>> call, Throwable throwable) {
                fail();
            }
        });


        return view;
    }

    private void setRating() {
        selfPosition.setText("Ваше место в рейтинге: " + ratings.get(ratings.size() - 1).position + "\nКоличество очков: "+ratings.get(ratings.size() - 1).points);
        ratings.remove(ratings.size() - 1);

        RatingAdapter ratingAdapter = new RatingAdapter(ratings, this.getContext());
        ratingList.setAdapter(ratingAdapter);

    }

    private void fail() {
        FullHelper.showDialog(this.getContext(),
                "Ошибка",
                "Не удалось получить данные",
                SweetAlertDialog.ERROR_TYPE,
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        ((MainActivity) RatingFragment.this.getActivity()).setFragment();
                    }
                });
    }


}
