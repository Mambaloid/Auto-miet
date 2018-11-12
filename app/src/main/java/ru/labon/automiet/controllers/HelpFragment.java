package ru.labon.automiet.controllers;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


import cn.pedant.SweetAlert.SweetAlertDialog;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.labon.automiet.App;
import ru.labon.automiet.R;
import ru.labon.automiet.helpers.FullHelper;

/**
 * Created by HP on 02.10.2017.
 */

public class HelpFragment extends android.support.v4.app.Fragment {

    SweetAlertDialog pDialog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.help_fragment, container, false);

        final EditText editTextDescr = view.findViewById(R.id.editTextCommentHelp);
        final EditText editTextMail =  view.findViewById(R.id.editTextEmailHelp);


        view.findViewById(R.id.buttonSubmitHelp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String text = editTextDescr.getText() + "";
                if (text.isEmpty()) {

                    FullHelper.showDialog(HelpFragment.this.getActivity(),
                            "Ошибка",
                            "Заполните поле!",
                            SweetAlertDialog.WARNING_TYPE,null);

                    return;
                }
                String mail = editTextMail.getText() + "";

                text += mail.isEmpty() ? "" : "<br>Прислать ответ на почту: " + mail;

                view.setEnabled(false);

                pDialog = new SweetAlertDialog(HelpFragment.this.getActivity(), SweetAlertDialog.PROGRESS_TYPE);
                pDialog.setTitleText("Отправка");
                pDialog.setContentText( "Пожалуйста подождите..");
                pDialog.setCancelable(false);
                pDialog.show();


                App.getNetClient().sendFeedBack(text, new Callback<String>() {
                    @Override
                    public void onResponse(Call<String> call, Response<String> response) {
                        Log.d("Send mail", response.body() + "    ");
                        closeHelpForm();
                    }

                    @Override
                    public void onFailure(Call<String> call, Throwable throwable) {
                        Log.d("Send mail", throwable.getMessage());
                        closeHelpForm();
                    }
                });
            }
        });
        return view;
    }

    public void closeHelpForm() {
        pDialog.dismiss();
        FullHelper.showDialog(this.getActivity(),
                "Письмо отправлено!",
                "В скором времени мы рассмотрим Ваше обращение.",
                SweetAlertDialog.SUCCESS_TYPE,
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        ((MainActivity) HelpFragment.this.getActivity()).setFragment();
                        sweetAlertDialog.dismissWithAnimation();
                    }
                }
        );
    }
}

