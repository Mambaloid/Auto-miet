package ru.labon.automiet;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.stetho.Stetho;
import com.liulishuo.filedownloader.FileDownloader;

import ru.labon.automiet.api.NetClient;

public class App extends Application {

    private static final String APP_PREFERENCES = "AUTOMIET";
    private static final String APP_PREFERENCES_ACCESS_TOKEN = "ACCESS_TOKEN";
    private static final String APP_PREFERENCES_LAST_TIME_UPDATE = "BASE_LAST_TIME_UPDATE";
    private static final String APP_PREFERENCES_VERSION = "APP_VERSION";
    private static final String APP_PREFERENCES_CHECK_SUM = "CHECK_SUM";
    private static final String BASE_URL = "http://automiet-test.h1n.ru/traning/api/";

    private static SharedPreferences preferences;
    private static NetClient netClient;

    @Override
    public void onCreate() {
        super.onCreate();
        preferences = getSharedPreferences(APP_PREFERENCES, Context.MODE_PRIVATE);

        if(BuildConfig.DEBUG) {
            Stetho.InitializerBuilder initializerBuilder = Stetho.newInitializerBuilder(this);
            initializerBuilder.enableWebKitInspector(Stetho.defaultInspectorModulesProvider(this));
            initializerBuilder.enableDumpapp(Stetho.defaultDumperPluginsProvider(this));

            Stetho.Initializer initializer = initializerBuilder.build();
            Stetho.initialize(initializer);
        }
        Fresco.initialize(this);
        createNetClient();

        FileDownloader.setupOnApplicationOnCreate(this);
    }

    public static String getAccessToken() {
        return preferences.getString(APP_PREFERENCES_ACCESS_TOKEN, "");
    }

    public static int getLastTimeUpdate() {
        return preferences.getInt(APP_PREFERENCES_LAST_TIME_UPDATE, 0);
    }

    public static String getSum() {
        return preferences.getString(APP_PREFERENCES_CHECK_SUM,"");
    }

    public static void setSum(String sum) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_PREFERENCES_CHECK_SUM, sum);
        editor.apply();
    }

    public static void setLastTimeUpdate(int time) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(APP_PREFERENCES_LAST_TIME_UPDATE, time);
        editor.apply();
    }

    public static int getVersion() {
        try {
            return Integer.parseInt(preferences.getString(APP_PREFERENCES_VERSION, "0"));
        } catch (NumberFormatException e) {
            return 0;
        }
    }

    public static void setVersion(int version) {

        try {
            SharedPreferences.Editor editor = preferences.edit();
            editor.putString(APP_PREFERENCES_VERSION, version + "");
            editor.apply();
        } catch (Exception e) {

        }

    }

    public static boolean tokenIsExist() {
        return !getAccessToken().isEmpty();
    }

    public static void removeToken() {
        setAccessToken("");
    }

    public static void setAccessToken(String accessToken) {
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(APP_PREFERENCES_ACCESS_TOKEN, accessToken);
        editor.apply();
    }

    public static NetClient getNetClient() {
        return netClient;
    }

    public static void createNetClient() {
        String accessToken = getAccessToken();
        if (!accessToken.equals("")) {
            netClient = new NetClient(BASE_URL, accessToken);
        } else {
            netClient = new NetClient(BASE_URL, null);
        }
    }


}
