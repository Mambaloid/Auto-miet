package ru.labon.automiet.controllers;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mikhaellopez.circularprogressbar.CircularProgressBar;

import me.grantland.widget.AutofitTextView;
import ru.labon.automiet.App;
import ru.labon.automiet.adapters.NavigationDrawerAdapter;
import ru.labon.automiet.R;
import ru.labon.automiet.helpers.FullHelper;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.models.AnswerGroup;
import ru.labon.automiet.models.Card;
import ru.labon.automiet.models.Question;
import ru.labon.automiet.models.Task;
import ru.labon.automiet.models.ThemeAb;
import ru.labon.automiet.models.UserAnswer;

public class MainActivity extends AppCompatActivity {


    private DrawerLayout mDrawerLayout;
    private TextView title;
    private Fragment fragment;
    private CircularProgressBar circularProgressBar;
    private AutofitTextView ratingPositionTextView;
    MainDbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sliding_activity_layout);

        dbHelper = MainDbHelper.getInstance(this);

        App.setVersion(12);
        Log.d("Oooooeeeeh", "Mr. Crabs");


        checkTablesExist();

        if (!App.tokenIsExist()) {
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        } else if (App.getLastTimeUpdate() == 0 || !FullHelper.checkColumnsExist(dbHelper)) {
            Intent loadIntent = new Intent(this, LoadActivity.class);
            startActivity(loadIntent);
            finish();
        }
        setNavigationDrawer();
        setToolbar();
        setFragment();

        FullHelper.checkSumChanges(this);
        FullHelper.checkVersion(this);
    }

    private void checkTablesExist() {
        AnswerGroup.checkTableExist(dbHelper);
        Card.checkTableExist(dbHelper);
        Question.checkTableExist(dbHelper);
        Task.checkTableExist(dbHelper);
        ThemeAb.checkTableExist(dbHelper);
        UserAnswer.checkTableExist(dbHelper);
    }


    private void setNavigationDrawer() {
        mDrawerLayout = findViewById(R.id.drawer_layout);
        circularProgressBar = findViewById(R.id.circular_progress_bar_nav_drawer);
        ListView mDrawerList = findViewById(R.id.left_drawer);
        mDrawerList.setAdapter(new NavigationDrawerAdapter(this));

        ratingPositionTextView = mDrawerLayout.findViewById(R.id.text_view_nav_drawer);
        FullHelper.getRatingPosition(ratingPositionTextView);


        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

                switch (i) {
                    case 0:
                        fragment = new TrainingFragment();
                        title.setText(R.string.training);
                        //return;
                        break;
                    case 1:
                        fragment = new TicketFragment();
                        title.setText(R.string.ticket);
                        //return;
                    break;
                    case 2:
                       // fragment = new ExamFragment();
                        //title.setText(R.string.exam);
                        mDrawerLayout.closeDrawer(Gravity.LEFT);
                        Intent intent = new Intent(MainActivity.this, TestActivity.class);
                        intent.putExtra("is_exam", true);
                        MainActivity.this.startActivity(intent);
                        return;
                    //break;
                    case 3:
                        fragment = new ProblematicIssuesFragment();
                        title.setText(R.string.my_errors);
                        break;
                    case 4:
                        fragment = new AccountFragment();
                        title.setText(R.string.account);
                        return;
                    //break;
                    case 5:
                        fragment = new RatingFragment();
                        title.setText(R.string.rating);
                        //return;
                        break;
                    case 6:
                        fragment = new VideomaterialFragment();
                        title.setText(R.string.videomaterial);
                        //return;
                        break;
                    case 7:
                        fragment = new AboutFragment();
                        title.setText(R.string.about);
                        //return;
                        break;
                    case 8:
                        fragment = new HelpFragment();
                        title.setText(R.string.help);
                        //return;
                        break;
                    case 9:
                        FullHelper.logOut(MainActivity.this);
                        break;
                }
                if (fragment != null) {
                    mDrawerLayout.closeDrawer(Gravity.LEFT);
                    Log.d("OPENED", fragment.getClass().toString());
                    fragmentTransaction.replace(R.id.content_frame, fragment);
                    fragmentTransaction.commit();
                }
            }
        });
        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                String text = ratingPositionTextView.getText().toString().replace("Вы\n", "");
                Log.d("parse", text);
                String[] split = text.split(" из ");
                float current = Float.parseFloat(split[0]);
                float all = Float.parseFloat(split[1]);
                float percent = (all - current) / all * 100;
                Log.d("percent", percent + "");
                circularProgressBar.setProgressWithAnimation(percent, 1000);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                circularProgressBar.setProgress(0);
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });

    }

    private void setToolbar() {
        ImageView buttonToolbar = findViewById(R.id.buttonToolbar);
        buttonToolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mDrawerLayout.openDrawer(Gravity.LEFT);
            }
        });
        title = findViewById(R.id.titleToolbar);
    }

    public void setFragment() {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragment = new TrainingFragment();
        title.setText(R.string.training);
        fragmentTransaction.replace(R.id.content_frame, fragment);
        fragmentTransaction.commit();
    }


}
