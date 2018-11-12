package ru.labon.automiet.controllers;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import ru.labon.automiet.R;

/**
 * Активити, от которого должны наследоваться все остальные, в которых должен быть показан тулбар
 */
public abstract class CommonActivity extends AppCompatActivity {
    private View view;
    private FrameLayout contentFrame;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("ACTIVITY","ON_CREATE");
        super.setContentView(R.layout.common_activity);
        contentFrame = findViewById(R.id.content_frame_common_activity);

        ImageButton exitButton = findViewById(R.id.button_exit_toolbar_common_activity);
        exitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                preExitMethod();
                finish();
            }
        });
    }

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        view = getLayoutInflater().inflate(layoutResID, null);
        contentFrame.addView(view);
    }

    public void setTitle(String text) {
        ((TextView) findViewById(R.id.text_view_title_common_activity)).setText(text);
    }

    public View getView() {
        return view;
    }

    protected void preExitMethod() {}

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("ACTIVITY","ON_START");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("ACTIVITY","ON_RESUME");
    }
}
