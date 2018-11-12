package ru.labon.automiet.helpers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import cn.pedant.SweetAlert.SweetAlertDialog;
import me.grantland.widget.AutofitTextView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.labon.automiet.App;
import ru.labon.automiet.adapters.RVAdapter;
import ru.labon.automiet.controllers.LoadActivity;
import ru.labon.automiet.controllers.LoginActivity;
import ru.labon.automiet.controllers.PlayerActivity;
import ru.labon.automiet.models.Card;
import ru.labon.automiet.models.ThemeAb;

/**
 * Created by HP on 14.11.2017.
 */

public class FullHelper {

    public interface Consumer<T> {
        void execute(T arg);
    }

    public static String imgUrl(String imgName) {
       // Calendar calendar = Calendar.getInstance();
      //  String versionNumber = "?" + calendar.get(Calendar.DAY_OF_MONTH) + "_"
       //         + calendar.get(Calendar.HOUR_OF_DAY);

        String url = "http://automiet-test.h1n.ru/traning/img/conspekts/";
        url += imgName;// + versionNumber;
        url = url.replaceAll("(\\r|\\n)", "");
        Log.d("URL", url);
        return url;
    }

    public static String correctTitle(String title) {
        String[] result = title.split("#");
        return result[result.length > 1 ? 1 : 0];
    }

    public static boolean checkColumnsExist(MainDbHelper dbHelper) {
        return Card.columnsExist(dbHelper) && ThemeAb.columnsExist(dbHelper);
    }

    public static boolean isColumnExists(MainDbHelper dbHelper,
                                         String tableName,
                                         String columnToFind) {
        Cursor cursor = null;

        try {
            cursor = dbHelper.getWritableDatabase().rawQuery(
                    "PRAGMA table_info(" + tableName + ")",
                    null
            );

            int nameColumnIndex = cursor.getColumnIndexOrThrow("name");

            while (cursor.moveToNext()) {
                String name = cursor.getString(nameColumnIndex);

                if (name.equals(columnToFind)) {
                    return true;
                }
            }

            return false;
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public static String testingImgURL(int themeNumber, int questionNumber) {
        String url = "http://automiet-test.h1n.ru/traning/img/ab/" + themeNumber + "-" + questionNumber + ".jpg";
        Log.d("URL", url);
        return url;
    }

    public static void showDialog(Context context,
                                  String title,
                                  String text,
                                  int type,
                                  SweetAlertDialog.OnSweetClickListener sweetClickListener) {

        SweetAlertDialog pDialog = new SweetAlertDialog(context, type);
        pDialog.setTitleText(title);
        pDialog.setContentText(text);
        pDialog.setCancelable(false);
        pDialog.setConfirmClickListener(sweetClickListener);
        if (!((Activity) context).isFinishing()) {
            pDialog.show();
        }
    }

    public static void checkVersion(final Context context) {
        if (!App.tokenIsExist()) return;
        App.getNetClient().checkVersion(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                FullHelper.checkApiPermissions(context, response.code());
                int version = 0;
                try {
                    version = Integer.parseInt(response.body());

                    if (version > App.getVersion()) {
                        FullHelper.newVersionDialog(context);
                    }
                } catch (NumberFormatException e) {
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
    }

    public static void getRatingPosition(final AutofitTextView textView) {
        if (!App.tokenIsExist()) return;
        App.getNetClient().getRatingPosition(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                Log.d("Rating Position", response.body());
                if (response.isSuccessful()) {
                    try {
                        String data = java.net.URLDecoder.decode(response.body(), "UTF-8");
                        String ratingPosition = "Вы\n" + data.replace("_", " ");
                        Log.d("Rating Position", ratingPosition);
                        textView.setText(ratingPosition);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                        textView.setText("Нет данных о рейтинге");
                    }

                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                Log.d("Rating Position", "fail");
            }
        });
    }


    public static void checkSumChanges(final Context context) {
        if (!App.tokenIsExist()) return;
        if (System.currentTimeMillis() / 1000 - App.getLastTimeUpdate() < 3600) return;
        App.getNetClient().getSumCheck(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                FullHelper.checkApiPermissions(context, response.code());
                if (!App.getSum().equals(response.body())) {
                    changeTableDialog(context, response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
            }
        });
        return;
    }

    private static void newVersionDialog(final Context context) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        pDialog.setTitleText("Ура!");
        pDialog.setContentText("Вышла новая версия приложения. Скачать?");
        pDialog.setCancelText("Нет");
        pDialog.setConfirmText("Да");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://automiet-test.h1n.ru/traning/mobileApp.php"));
                context.startActivity(browserIntent);
            }
        });
        pDialog.setCancelable(true);
        if (!((Activity) context).isFinishing()) {
            pDialog.show();
        }
    }

    private static void changeTableDialog(final Context context, final String newSum) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        pDialog.setTitleText("Внимание!");
        pDialog.setContentText("Доступны новые материалы. Обновить сейчас?");
        pDialog.setCancelText("Нет");
        pDialog.setConfirmText("Да");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                App.setSum(newSum);
                Intent loadIntent = new Intent(context, LoadActivity.class);
                context.startActivity(loadIntent);
                ((AppCompatActivity) context).finish();
            }
        });
        pDialog.setCancelable(true);
        if (!((Activity) context).isFinishing()) {
            pDialog.show();
        }
    }

    public static void emptyDataDialog(final Context context) {
        SweetAlertDialog pDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        pDialog.setTitleText("Внимание!");
        pDialog.setContentText("Отстутсвуют материалы по этому разделу.\nЗагрузить сейчас?");
        pDialog.setCancelText("Нет");
        pDialog.setConfirmText("Да");
        pDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Intent loadIntent = new Intent(context, LoadActivity.class);
                context.startActivity(loadIntent);
                ((AppCompatActivity) context).finish();
            }
        });
        pDialog.setCancelable(true);
        if (!((Activity) context).isFinishing()) {
            pDialog.show();
        }
    }


    public static void videoDialog(final Context context,
                                   final int videoNum,
                                   final RVAdapter.ThemeViewHolder viewHolder,
                                   final FileDownloadListener fileDownloadListener,
                                   final FullHelper.Consumer<Integer> downloadIdInterface) {

        ConnectivityManager connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi != null && mWifi.isConnected()) {
            videoDownload(context, videoNum, viewHolder, fileDownloadListener, downloadIdInterface);
        } else {

            SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
            sweetAlertDialog.setTitleText("Внимание");
            sweetAlertDialog.setContentText("Видеоматериал не скачан!\nЗагрузить или смотреть онлайн?\n(Видеофайл имеет большой размер, рекомендуем подключение по WI-Fi)");
            sweetAlertDialog.setCancelText("Загрузить");
            sweetAlertDialog.setConfirmText("Онлайн");
            sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    Intent intent = new Intent(context, PlayerActivity.class);
                    intent.putExtra("video_num", videoNum);
                    context.startActivity(intent);
                }
            });
            sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                @Override
                public void onClick(SweetAlertDialog sweetAlertDialog) {
                    sweetAlertDialog.dismiss();
                    videoDownload(context, videoNum, viewHolder, fileDownloadListener, downloadIdInterface);
                }
            });
            sweetAlertDialog.setCancelable(true);
            if (!((Activity) context).isFinishing()) {
                sweetAlertDialog.show();
            }
        }

    }

    public static void videoDownload(final Context context,
                                     final int videoNum,
                                     final RVAdapter.ThemeViewHolder viewHolder,
                                     final FileDownloadListener fileDownloadListener,
                                     final FullHelper.Consumer<Integer> downloadIdInterface) {
        App.getNetClient().getVideoUrl(videoNum, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {

                    Log.d("downloader", "----------------------");
                    Log.d("downloader", context.getFilesDir().getAbsolutePath() + "/videos/" + videoNum + ".mp4");
                    try {
                        String url = java.net.URLDecoder.decode(response.body(), "UTF-8");
                        BaseDownloadTask downloadTask = FileDownloader.getImpl().create(url)
                                .setPath(context.getFilesDir().getAbsolutePath() + "/videos/" + videoNum + ".mp4")
                                .setListener(fileDownloadListener);
                        downloadTask.setTag(viewHolder);
                        downloadIdInterface.execute(downloadTask.getId());
                        downloadTask.start();
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }

                } else {
                    showDialog(context, "Ошибка!", "Не удаётся загрузить видео!", SweetAlertDialog.ERROR_TYPE, null);
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable throwable) {
                showDialog(context, "Ошибка!", "Не удаётся загрузить видео!", SweetAlertDialog.ERROR_TYPE, null);
            }
        });
    }

    public static void logOut(Context context) {
        App.removeToken();
        App.createNetClient();
        App.setLastTimeUpdate(0);
        deleteAllVideos(context);
        MainDbHelper.getInstance(context).recreateAllTables();
        Intent loginIntent = new Intent(context, LoginActivity.class);
        context.startActivity(loginIntent);
        ((Activity) context).finish();
    }

    public static void checkApiPermissions(Context context, int code) {
        if (code == 403) {
            logOut(context);
        }
    }

    public static boolean checkVideoExist(Context context, int videoNum) {
        File file = new File(context.getFilesDir().getAbsolutePath() + "/videos/" + videoNum + ".mp4");
        return file.exists();
    }

    public static void deleteAllVideos(Context context) {
        File dir = new File(context.getFilesDir().getAbsolutePath() + "/videos");
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (String aChildren : children) {
                File file = new File(dir, aChildren);
                boolean result = (file.delete());
                Log.d("VIDEO: " + file.getName(), "IS_DELETE: " + result);
            }
        }
    }

    public static void deleteVideo(final Context context, final String videoName, final Consumer<Boolean> consumer) {
        SweetAlertDialog sweetAlertDialog = new SweetAlertDialog(context, SweetAlertDialog.NORMAL_TYPE);
        sweetAlertDialog.setTitleText("Внимание");
        sweetAlertDialog.setContentText("Удалить видеоматериал?");
        sweetAlertDialog.setCancelText("Нет");
        sweetAlertDialog.setConfirmText("Да");
        sweetAlertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
                File file = new File(context.getFilesDir().getAbsolutePath() + "/videos/" + videoName);
                consumer.execute(file.delete());
            }
        });
        sweetAlertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                sweetAlertDialog.dismiss();
            }
        });
        sweetAlertDialog.setCancelable(true);
        if (!((Activity) context).isFinishing()) {
            sweetAlertDialog.show();
        }

    }

    public static List<File> getAllVideos(Context context) {
        File dir = new File(context.getFilesDir().getAbsolutePath() + "/videos");
        if (!dir.exists()){
            dir.mkdir();
        }
        List<File> inFiles = new ArrayList<>();
        Queue<File> files = new LinkedList<>();
        files.addAll(Arrays.asList(dir.listFiles()));
        while (!files.isEmpty()) {
            File file = files.remove();
            if (file.getName().endsWith(".mp4")) {
                inFiles.add(file);
            }
        }
        return inFiles;
    }

}
