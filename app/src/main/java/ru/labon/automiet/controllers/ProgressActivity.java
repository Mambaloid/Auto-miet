package ru.labon.automiet.controllers;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.utils.ViewPortHandler;
import cn.pedant.SweetAlert.SweetAlertDialog;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.labon.automiet.App;
import ru.labon.automiet.R;
import ru.labon.automiet.helpers.FullHelper;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.models.ProgressData;
import ru.labon.automiet.models.ThemeAb;

/**
 * Created by Admin on 05.12.2017.
 */

public class ProgressActivity extends CommonActivity {

    RelativeLayout progressLay;
    PieChart chart;
    Spinner themesSpinner;
    SweetAlertDialog pDialog;
    List<ProgressData> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.progress_lay);

        setTitle("Прогресс");

        progressLay = (RelativeLayout) findViewById(R.id.progressLay);
        chart = (PieChart) findViewById(R.id.chart);
        themesSpinner = (Spinner) findViewById(R.id.themes_spinner);


        pDialog = new SweetAlertDialog(ProgressActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.setTitleText("Отправка");
        pDialog.setContentText("Пожалуйста подождите..");
        pDialog.setCancelable(false);
        pDialog.show();


        Description descr = new Description();
        descr.setText("");
        chart.setDescription(descr);
        chart.setRotationEnabled(true);
        chart.setHoleRadius(25f);
        chart.setTransparentCircleAlpha(0);
        chart.setEntryLabelColor(Color.parseColor("#464646"));
        //chart.setCenterText("Super Cool Chart");
        chart.setEntryLabelTextSize(15);


        App.getNetClient().getProgress(new Callback<List<ProgressData>>() {
            @Override
            public void onResponse(Call<List<ProgressData>> call, Response<List<ProgressData>> response) {
                data = response.body();

                if (data == null || data.isEmpty()) fail();

                ArrayAdapter<String> adapter = new ArrayAdapter<>(ProgressActivity.this,
                        android.R.layout.simple_spinner_item,
                        ThemeAb.getNames(MainDbHelper.getInstance(ProgressActivity.this)));

                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

                themesSpinner.setAdapter(adapter);
                themesSpinner.setPrompt("Тема:");
                themesSpinner.setSelection(0);
                themesSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                        setChart();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
                setChart();
                progressLay.setVisibility(View.GONE);
                pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<List<ProgressData>> call, Throwable throwable) {
                fail();
            }
        });

    }

    private void fail() {
        FullHelper.showDialog(ProgressActivity.this,
                "Ошибка",
                "Не удалось получить данные",
                SweetAlertDialog.ERROR_TYPE,
                new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        ProgressActivity.this.finish();
                    }
                });
    }

    private void setChart() {
        ProgressData themeData = data.get(themesSpinner.getSelectedItemPosition());

        ArrayList<PieEntry> yEntrys = new ArrayList<>();
        ArrayList<String> xEntrys = new ArrayList<>();


        yEntrys.add(new PieEntry(themeData.right, "Правильные ответы"));
        yEntrys.add(new PieEntry(themeData.total - themeData.right, "Нет ответов"));

        xEntrys.add("Правильные ответы");
        xEntrys.add("Неправильные ответы");


        //create the data set
        PieDataSet pieDataSet = new PieDataSet(yEntrys, "");
        pieDataSet.setSliceSpace(2);
        pieDataSet.setValueTextSize(12);

        //add colors to dataset
        ArrayList<Integer> colors = new ArrayList<>();
        colors.add(Color.parseColor("#4d9287"));
        colors.add(Color.parseColor("#b3e4da"));

        pieDataSet.setColors(colors);

        //add legend to chart
        Legend legend = chart.getLegend();
        legend.setTextSize(15);
        legend.setForm(Legend.LegendForm.CIRCLE);
        legend.setPosition(Legend.LegendPosition.LEFT_OF_CHART);

        //create pie data object
        PieData pieData = new PieData(pieDataSet);
        pieData.setValueFormatter(new ValueFormatter());
        pieData.setValueTextSize(15);
        chart.setData(pieData);
        chart.invalidate();
    }

    private class ValueFormatter implements IValueFormatter {

        @Override
        public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
            return (int) value + "";
        }
    }
}
