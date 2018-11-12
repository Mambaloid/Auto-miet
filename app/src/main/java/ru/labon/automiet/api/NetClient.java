package ru.labon.automiet.api;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.labon.automiet.models.Card;
import ru.labon.automiet.models.Error;
import ru.labon.automiet.models.ProgressData;
import ru.labon.automiet.models.Question;
import ru.labon.automiet.models.Rating;
import ru.labon.automiet.models.ResultAuth;
import ru.labon.automiet.models.Task;
import ru.labon.automiet.models.ThemeAb;

public class NetClient {

    private Api api;

    public NetClient(String baseUrl, final String accessToken) {
        OkHttpClient client = new OkHttpClient.Builder().addInterceptor(new Interceptor() {
            @Override
            public okhttp3.Response intercept(Chain chain) throws IOException {
                Request.Builder builder = chain.request().newBuilder();
                if (accessToken != null) {
                    builder.addHeader("Authorization", "Bearer " + accessToken);
                }
                return chain.proceed(builder.build());
            }
        }).readTimeout(60, TimeUnit.SECONDS).build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .client(client)
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        api = retrofit.create(Api.class);
    }

    public void auth(String email, String password, Callback<ResultAuth> callback) {
        Log.d("URL: ", api.auth(email, password).request().url().toString());
        api.auth(email, password).enqueue(callback);
    }
    public void getSumCheck(Callback<String> callback) {
        api.getCheckSum().enqueue(callback);
    }
    public void syncThemes(Callback<List<ThemeAb>> callback) {
        api.syncThemes().enqueue(callback);
    }

    public void syncCards(Callback<List<Card>> callback) {

        api.syncCards().enqueue(callback);
    }

    public void syncQuestions(Callback<List<Question>> callback) {
        api.syncQuestions().enqueue(callback);
    }
    public void syncTask(Callback<List<Task>> callback) {
        api.syncTask().enqueue(callback);
    }

    public void checkVersion(Callback<String> callback) {
        api.checkVersion().enqueue(callback);
    }

    public void getVideoUrl(int num, Callback<String> callback) {
        api.getVideoUrl(num).enqueue(callback);
    }

    public void sendFeedBack(String text, Callback<String> callback) {
        Log.d("URL: ", api.sendFeedback(text).request().url().toString());
        api.sendFeedback(text).enqueue(callback);
    }

    public void sendAnswerGroup(String text, Callback<String> callback) {
        api.sendAnswerGroup(text).enqueue(callback);
    }

    public void getProgress(Callback<List<ProgressData>> callback) {
        Log.d("URL: ", api.getProgress().request().url().toString());
        api.getProgress().enqueue(callback);
    }


    public void getRatingPosition(Callback<String> callback) {
        api.getRatingPosition().enqueue(callback);
    }

    public void getRating(Callback<List<Rating>> callback) {
        api.getRating().enqueue(callback);
    }

    public void sendUserAnswers(String json, Callback<String> callback) {
        api.sendUserAnswers(json).enqueue(callback);
    }

    public void getMyErrors(Callback<List<Error>> callback){
        api.getMyErrors().enqueue(callback);
    }

}
