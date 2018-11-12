package ru.labon.automiet.controllers;

import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.gson.Gson;
import cn.pedant.SweetAlert.SweetAlertDialog;
import com.stfalcon.frescoimageviewer.ImageViewer;

import java.io.FileNotFoundException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.labon.automiet.App;
import ru.labon.automiet.R;
import ru.labon.automiet.adapters.NavigationTestAdapter;
import ru.labon.automiet.adapters.TestListAdapter;
import ru.labon.automiet.helpers.FullHelper;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.helpers.OnSwipeTouchListener;
import ru.labon.automiet.models.AnswerGroup;
import ru.labon.automiet.models.Error;
import ru.labon.automiet.models.Question;
import ru.labon.automiet.models.Task;
import ru.labon.automiet.models.ThemeAb;
import ru.labon.automiet.models.UserAnswer;

public class TestActivity extends CommonActivity {

    private ListView list;
    private View header;
    private int idTheme;
    private int taskNumber;
    boolean isExam;
    private String condition;
    private List<Task> tasks;
    private List<Question> questionList;
    private int position = -1;
    private ImageView draweeView;
    private TextView taskQuest;
    private Question currentQuest;
    private int countRightAnswer;
    private int countAllAnswer;
    private boolean isSuccessAnswer;
    private RecyclerView navigationRecyclerView;
    private NavigationTestAdapter navigationTestAdapter;
    private UserAnswer currentAnswer;
    private MainDbHelper dbHelper;
    private TextView infoText;
    private ProgressBar progressBar;
    private ArrayList<NavigationTestAdapter.Quest> savedStates;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity);

        list = findViewById(R.id.list_view_test_activity);
        navigationRecyclerView = findViewById(R.id.navigation_recycler_view);

        header = getLayoutInflater().inflate(R.layout.header_test_activity, null);

        draweeView = header.findViewById(R.id.sdvImage);
        taskQuest = header.findViewById(R.id.description_header_test_activity);
        infoText = header.findViewById(R.id.textView);
        progressBar = header.findViewById(R.id.progressBar);

        idTheme = getIntent().getIntExtra("id_theme", -1);
        taskNumber = getIntent().getIntExtra("tasks_number", -1);
        isExam = getIntent().getBooleanExtra("is_exam", false);
        condition = getIntent().getStringExtra("condition");
        dbHelper = MainDbHelper.getInstance(this);
        if (idTheme > -1) {
            questionList = Question.getByTheme(dbHelper, idTheme);
            setTitle(ThemeAb.getById(dbHelper, idTheme).getName() + "");
        } else if (taskNumber > 0) {
            questionList = Question.getByTask(dbHelper, taskNumber);
            tasks = Task.getAll(dbHelper, "task_number=" + taskNumber);
            setTitle("Билет №" + taskNumber);
        } else if (isExam) {
            questionList = Question.getForExam(dbHelper);
            setTitle("Экзамен");
        } else if (condition != null) {
            questionList = Question.getAll(dbHelper,condition);
            setTitle("Проблемные вопросы");
        }

        if (questionList.isEmpty()) {
            Toast.makeText(this, "Нет материалов по этой теме!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }
        if (savedInstanceState != null) {
            position = savedInstanceState.getInt("position");
            countRightAnswer = savedInstanceState.getInt("count_right_answer");
            countAllAnswer = savedInstanceState.getInt("count_all_answer");
            savedStates = savedInstanceState.getParcelableArrayList("arraylist");
        } else {
            position = 0;
            countRightAnswer = 0;
            countAllAnswer = 0;
            savedStates = null;
        }
        setNavigationRecyclerView();
        setByPosition();
    }

    private void setList(String[] items) {
        list.setOnTouchListener(new OnSwipeTouchListener(TestActivity.this) {
            public void onSwipeRight() {
                if (position > 0) {
                    position--;
                    if (navigationTestAdapter.getStateByPosition(position)
                            == NavigationTestAdapter.Quest.state_neutral)
                        setByPosition();
                    else position++;
                }
            }
            public void onSwipeLeft() {
                if(position < questionList.size() - 1) {
                    position++;
                    if (navigationTestAdapter.getStateByPosition(position)
                            == NavigationTestAdapter.Quest.state_neutral)
                        setByPosition();
                    else position--;
                }
            }
        });
        list.setAdapter(new TestListAdapter(items, this, currentQuest.getVarsCount()));
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, final View view, int i, long l) {
                if (i == 0) return;
                list.setEnabled(false);
                if (currentQuest.getRightAns() == i) {
                    if (isSuccessAnswer) {
                        countRightAnswer++;
                        navigationTestAdapter.changeStateByPosition(position, NavigationTestAdapter.Quest.state_positive);
                    }
                    addAnswer(isSuccessAnswer);
                    FullHelper.showDialog(
                            TestActivity.this,
                            "Верно!",
                            currentQuest.getComment(),
                            SweetAlertDialog.SUCCESS_TYPE,
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    countAllAnswer++;
                                    sweetAlertDialog.dismiss();
                                    list.setEnabled(true);
                                    checkPosition();
                                }
                            });
                } else {
                    view.setBackgroundResource(R.color.red_btn_bg_color);
                    isSuccessAnswer = false;
                    navigationTestAdapter.changeStateByPosition(position, NavigationTestAdapter.Quest.state_negative);
                    FullHelper.showDialog(
                            TestActivity.this,
                            "Неверно!",
                            currentQuest.getComment(),
                            SweetAlertDialog.WARNING_TYPE,
                            new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sweetAlertDialog) {
                                    sweetAlertDialog.dismiss();
                                    list.setEnabled(true);
                                }
                            });
                }
            }
        });
        list.removeHeaderView(header);
        list.addHeaderView(header);
    }

    private void setByPosition() {
        infoText.setVisibility(View.GONE);
        progressBar.setVisibility(View.VISIBLE);
        draweeView.setMinimumHeight(0);
        draweeView.setImageBitmap(null);
        currentQuest = questionList.get(position);
        if (taskNumber > 0 || isExam || condition != null) {
            idTheme = currentQuest.getThemeNum();
        }
        currentAnswer = UserAnswer.getByCondition(dbHelper, "theme_num = " + currentQuest.getThemeNum() + " AND in_theme_num = " + currentQuest.getInThemeNum());
        isSuccessAnswer = true;
        final String[] list = {FullHelper.testingImgURL(idTheme, currentQuest.getInThemeNum())};
        final Uri imageUri = Uri.parse(list[0]);
        Glide.with(this)
                .asBitmap()
                .load(imageUri)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        if ((e == null)) {
                            return false;
                        }
                        Throwable cause = e.getRootCauses().get(0);
                        if (cause instanceof UnknownHostException) {
                            Toast.makeText(TestActivity.this, "Ошибка загрузки", Toast.LENGTH_LONG).show();
                        }
                        if (cause instanceof FileNotFoundException) {
                            Toast.makeText(TestActivity.this, "Фотографии нет", Toast.LENGTH_SHORT).show();
                        }
//                        switch (e.getClass().getCanonicalName()) {
//                            case "java.net.UnknownHostException":
//                                Toast.makeText(TestActivity.this, "Ошибка загрузки", Toast.LENGTH_LONG).show();
//                                break;
//                            case "java.io.FileNotFoundException":
//                                Toast.makeText(TestActivity.this, "Фотографии нет", Toast.LENGTH_SHORT).show();
//                                break;
//                            default:
//                                Toast.makeText(TestActivity.this, e.getRootCauses().get(0).getClass().getCanonicalName(), Toast.LENGTH_LONG).show();
//                                break;
//                        }
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        infoText.setVisibility(View.VISIBLE);
                        double w = draweeView.getWidth();
                        double resW = resource.getWidth();
                        double resH = resource.getHeight();
                        double h = w * (resH / resW);
                        Log.d("ASPECT", "h:" + h + " w:" + w + " resH:" + resH + " resW:" + resW);
                        draweeView.setMinimumHeight((int) h);
                        return false;
                    }
                })
                .into(draweeView);
        draweeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ImageViewer.Builder(TestActivity.this, list)
                        .setStartPosition(0)
                        .show();
            }
        });
        taskQuest.setText(currentQuest.getTask());
        navigationTestAdapter.changeStateByPosition(position, NavigationTestAdapter.Quest.state_current);

        String[] answers = new String[5];
        switch (currentQuest.getVarsCount()) {
            case 5:
                answers[4] = currentQuest.getAnsV5();
            case 4:
                answers[3] = currentQuest.getAnsV4();
            case 3:
                answers[2] = currentQuest.getAnsV3();
            case 2:
                answers[1] = currentQuest.getAnsV2();
                answers[0] = currentQuest.getAnsV1();
                break;
        }
        setList(answers);
    }

    private void checkPosition() {
        Log.d("COUNT", countAllAnswer + "/" + questionList.size());
        if (countAllAnswer == questionList.size()) {
            sendAnswers();

            final AnswerGroup answerGroup = new AnswerGroup();
            answerGroup.themeNum = idTheme;
            answerGroup.rightAnswerCount = countRightAnswer;
            answerGroup.date = (int) (System.currentTimeMillis() / 1000);
            answerGroup.save(dbHelper);

            String json = new Gson().toJson(AnswerGroup.getAll(dbHelper));
            Log.d("json to web AnswerGroup", json);

            App.getNetClient().sendAnswerGroup(json, new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    if (response.isSuccessful()) {
                        Log.d("SENDING", "Success sendAnswerGroup " + response.body());
                        AnswerGroup.deleteAll(dbHelper);
                    } else {
                        Log.d("SENDING", "Not success sendAnswerGroup " + response.code());
                    }
                }

                @Override
                public void onFailure(Call<String> call, Throwable throwable) {
                    Log.d("Error sendAnswerGroup ", throwable.getMessage());

                }
            });

            FullHelper.showDialog(
                    this,
                    isExam ? "Экзамен окончен" : "Тест окончен",
                    "Ваш результат: " + countRightAnswer + " из " + questionList.size() + " верно",
                    SweetAlertDialog.NORMAL_TYPE,
                    new SweetAlertDialog.OnSweetClickListener() {
                        @Override
                        public void onClick(SweetAlertDialog sweetAlertDialog) {
                            sweetAlertDialog.dismiss();
                            finish();
                        }
                    }
            );
        } else {
            if (position == (questionList.size() - 1)) {
                position = -1;
                checkPosition();
            } else {
                position++;
                switch (navigationTestAdapter.getStateByPosition(position)) {
                    case NavigationTestAdapter.Quest.state_current:
                    case NavigationTestAdapter.Quest.state_neutral:
                        setByPosition();
                        return;
                }
                checkPosition();
            }
        }
    }

        private void setNavigationRecyclerView() {
        navigationTestAdapter = new NavigationTestAdapter(questionList.size(), new NavigationTestAdapter.ClickInterface() {
            @Override
            public void click(int pos) {

                switch (navigationTestAdapter.getStateByPosition(position)) {
                    case NavigationTestAdapter.Quest.state_positive:
                        addAnswer(true);
                        countAllAnswer++;
                        break;
                    case NavigationTestAdapter.Quest.state_negative:
                        addAnswer(false);
                        countAllAnswer++;
                        break;
                }
                Log.d("COUNT", countAllAnswer + "/" + questionList.size());
                position = pos;

                switch (navigationTestAdapter.getStateByPosition(position)) {
                    case NavigationTestAdapter.Quest.state_current:
                    case NavigationTestAdapter.Quest.state_neutral:
                        setByPosition();
                        break;
                }
            }
        });
        if (savedStates != null)
            navigationTestAdapter.setQuests(savedStates);
        navigationRecyclerView.setAdapter(navigationTestAdapter);
    }

    private void addAnswer(boolean isSuccessAnswer) {
        if (currentAnswer != null) {
            if (isSuccessAnswer)
                currentAnswer.success_count += 1;
            else currentAnswer.error_count += 1;
            currentAnswer.save(dbHelper);
        } else {
            UserAnswer newAnswer = new UserAnswer();
            newAnswer.theme_num = currentQuest.getThemeNum();
            newAnswer.in_theme_num = currentQuest.getInThemeNum();
            if (isSuccessAnswer) {
                newAnswer.success_count = 1;
                newAnswer.error_count = 0;
            } else {
                newAnswer.success_count = 0;
                newAnswer.error_count = 1;
            }
            newAnswer.save(dbHelper);
        }
    }

    private void sendAnswers() {
        String jsonUserAnswers = new Gson().toJson(UserAnswer.getAll(dbHelper));
        Log.d("json to web UserAnswers", jsonUserAnswers);

        App.getNetClient().sendUserAnswers(jsonUserAnswers, new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                if (response.isSuccessful()) {
                    Log.d("SENDING", "Success sendUserAnswers " + response.body());
                    UserAnswer.deleteAll(dbHelper);
                } else {
                    Log.d("SENDING", "Not success sendUserAnswers " + response.code());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("SENDING", "Not success sendUserAnswers " + t.getMessage());
            }
        });
    }


    @Override
    protected void preExitMethod() {
        super.preExitMethod();
        sendAnswers();
    }

    @Override
    public void onBackPressed() {
        sendAnswers();
        super.onBackPressed();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("position",position);
        outState.putInt("count_right_answer",countRightAnswer);
        outState.putInt("count_all_answer",countAllAnswer);
        outState.putParcelableArrayList("arraylist",navigationTestAdapter.quests);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position",position);
        outState.putInt("count_right_answer",countRightAnswer);
        outState.putInt("count_all_answer",countAllAnswer);
        outState.putParcelableArrayList("arraylist",navigationTestAdapter.quests);
    }
}
