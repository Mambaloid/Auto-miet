package ru.labon.automiet.api;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import ru.labon.automiet.models.Card;
import ru.labon.automiet.models.Error;
import ru.labon.automiet.models.ProgressData;
import ru.labon.automiet.models.Question;
import ru.labon.automiet.models.Rating;
import ru.labon.automiet.models.ResultAuth;
import ru.labon.automiet.models.Task;
import ru.labon.automiet.models.ThemeAb;

public interface Api {

    @GET("login.php?checksum=asd")
    Call<ResultAuth> auth(@Query("login") String login, @Query("password") String password);

    @GET("sync.php?type=theme")
    Call<List<ThemeAb>> syncThemes();

    @GET("sync.php?type=card")
    Call<List<Card>> syncCards();

    @GET("sync.php?type=question")
    Call<List<Question>> syncQuestions();

    @GET("sync.php?type=task")
    Call<List<Task>> syncTask();

    @GET("version.php")
    Call<String> checkVersion();

    @GET("video_for_download.php")
    Call<String> getVideoUrl(@Query("num") int num);

    @POST("feed_back.php")
    @FormUrlEncoded
    Call<String> sendFeedback(@Field("text") String text);

    @POST("answer_group.php")
    @FormUrlEncoded
    Call<String> sendAnswerGroup(@Field("data") String text);

    @GET("progress.php")
    Call<List<ProgressData>> getProgress();

    @GET("rating.php")
    Call<List<Rating>> getRating();

    @GET("getRatingPosition.php")
    Call<String> getRatingPosition();

    @GET("get_check_sum.php")
    Call<String> getCheckSum();

    @POST("user_answers.php")
    @FormUrlEncoded
    Call<String> sendUserAnswers(@Field("data") String json);

    @GET("errors.php")
    Call<List<Error>> getMyErrors();

}
