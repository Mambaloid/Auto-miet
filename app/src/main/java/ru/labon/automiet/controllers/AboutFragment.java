package ru.labon.automiet.controllers;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import ru.labon.automiet.R;

/**
 * Created by HP on 02.10.2017.
 */

public class AboutFragment extends android.support.v4.app.Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.about_fragment, container, false);
        WebView webView = (WebView) view.findViewById(R.id.webview);
        webView.loadUrl("file:///android_asset/about.html");
        return view;
    }


}
