package ru.labon.automiet.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import java.util.ArrayList;
import java.util.List;

import me.grantland.widget.AutofitTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.labon.automiet.App;
import ru.labon.automiet.R;
import ru.labon.automiet.helpers.FullHelper;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.models.DbModel;
import ru.labon.automiet.models.ThemeAb;

/**
 * Created by Admin on 03.10.2017.
 */

public class LoadActivity extends AppCompatActivity {

    private CircularProgressBar progressBar;
    private AutofitTextView textView;
    private MainDbHelper dbHelper;
    private Callback callback;

    private ArrayList<String> progressLabels = new ArrayList<>();
    private int currentRequest = 0;
    private int requestCount = 0;
    private float progressForRequest = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.load_data);

        dbHelper = MainDbHelper.getInstance(this);
        dbHelper.recreateAllTables();

        progressBar = (CircularProgressBar) findViewById(R.id.load_data_progress_bar);
        textView = (AutofitTextView) findViewById(R.id.load_data_text_view);

        progressBar.setProgress(0);


        callback = new Callback<List<DbModel>>() {
            @Override
            public void onResponse(Call<List<DbModel>> call, Response<List<DbModel>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    final List<DbModel> listModel = response.body();
                    FullHelper.checkApiPermissions(LoadActivity.this, response.code());

                    currentRequest++;

                    //progressBar.setProgressWithAnimation(currentRequest*progressForRequest, 100);

                    //  progressBar.setProgress(currentRequest*progressForRequest);


                    textView.setText(progressLabels.get(currentRequest));

                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            List<DbModel> list = listModel;
                            for (DbModel model : list) {
                                model.save(dbHelper);
                            }
                        }
                    }).start();


                    if (currentRequest == requestCount) {
                        progressBar.setProgressWithAnimation(100, 100);
                        App.setLastTimeUpdate((int) (System.currentTimeMillis() / 1000));
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish();
                    }
                } else {
                    Toast.makeText(LoadActivity.this,"Что-то пошло не так. Попробуйте снова.",Toast.LENGTH_SHORT).show();
                    FullHelper.logOut(LoadActivity.this);
                }
            }

            @Override
            public void onFailure(Call<List<DbModel>> call, Throwable t) {
                Toast.makeText(LoadActivity.this,"Нет подключения к интернету!",Toast.LENGTH_SHORT).show();
                FullHelper.logOut(LoadActivity.this);
            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();

        //progressBar.setProgressWithAnimation(0, 1000);
        requestCount = 4;
        progressForRequest = 100 / requestCount;
        App.getNetClient().syncThemes(callback); progressLabels.add("Скачивание тем");
        App.getNetClient().syncCards(callback); progressLabels.add("Скачивание материалов");
        App.getNetClient().syncQuestions(callback); progressLabels.add("Скачивание вопросов");
        App.getNetClient().syncTask(callback); progressLabels.add("Скачивание билетов");
        progressLabels.add("Успешно!");
        textView.setText(progressLabels.isEmpty() ? "Скачивание материалов" : progressLabels.get(0));
        progressBar.setProgressWithAnimation(100, 15000);

      /*  final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                textView.setText("Скачивание рейтинга");
                progressBar.setProgressWithAnimation(100, 1000);

            }
        }, 1000);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                MainActivity.isDownload = true;
                finish();

            }
        }, 2000);*/

    }

}
