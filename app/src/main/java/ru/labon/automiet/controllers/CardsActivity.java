package ru.labon.automiet.controllers;

import android.animation.ObjectAnimator;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.drawee.view.SimpleDraweeView;
import com.stfalcon.frescoimageviewer.ImageViewer;


import java.util.ArrayList;
import java.util.List;

import ru.labon.automiet.R;
import ru.labon.automiet.Views.SearchDialog.OnSearchItemSelected;
import ru.labon.automiet.Views.SearchDialog.SearchListItem;
import ru.labon.automiet.Views.SearchDialog.SearchableDialog;
import ru.labon.automiet.helpers.FullHelper;
import ru.labon.automiet.helpers.MainDbHelper;
import ru.labon.automiet.helpers.OnSwipeTouchListener;
import ru.labon.automiet.models.Card;
import ru.labon.automiet.models.ThemeAb;

/**
 * Created by HP on 17.10.2017.
 */

public class CardsActivity extends CommonActivity {
    ImageView draweeView;
    private Button buttonBack, buttonNext;
    private TextView textViewDescription, textViewHeader;
    private List<Card> cardList;
    private List<SearchListItem> searchCardItems = new ArrayList<>();
    private SearchableDialog searchableDialog;
    private int position;
    private int idTheme;
    private ProgressBar progressBar;
    private RelativeLayout progressCardLayout;
    private ProgressBar progressBarCard;
    private TextView currentCardNumberTextView;
    private MainDbHelper dbHelper;
    private ThemeAb themeAb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.card_activity);

        idTheme = getIntent().getIntExtra("id_theme", -1);
        dbHelper = MainDbHelper.getInstance(this);
        cardList = Card.getByTheme(dbHelper, idTheme);

        searchCardItems = new ArrayList<>();
        for (int i = 0; i < cardList.size(); i++) {
            searchCardItems.add(new SearchListItem(i, FullHelper.correctTitle(cardList.get(i).getHeading())));
        }

        if (cardList.isEmpty()) {
            Toast.makeText(this, "Нет материалов по этой теме!", Toast.LENGTH_LONG).show();
            finish();
            return;
        }


        themeAb = ThemeAb.getById(dbHelper, idTheme);

        setTitle(ThemeAb.getById(dbHelper, idTheme).getName());
        initialize();
        if (savedInstanceState != null)
            position = savedInstanceState.getInt("position");
        else position = themeAb.getThemeState();
        Log.d("POSITION", position + "");
        setByPosition();
    }

    private void initialize() {
        // infoText = findViewById(R.id.textView);
        progressBar = findViewById(R.id.progressBar);
        draweeView = findViewById(R.id.sdvImage);
        buttonBack = findViewById(R.id.button_back_card);
        buttonBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position > 0) {
                    position--;
                    setByPosition();
                }
            }
        });

        buttonNext = findViewById(R.id.button_next_card);
        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (position < cardList.size() - 1) {
                    position++;
                    setByPosition();
                }
            }
        });
        findViewById(R.id.scrollView).setOnTouchListener(new OnSwipeTouchListener(this) {
            public void onSwipeRight() {
                if (position > 0) {
                    position--;
                    setByPosition();
                }
            }

            public void onSwipeLeft() {
                if (position < cardList.size() - 1) {
                    position++;
                    setByPosition();
                }
            }
        });
        textViewDescription = findViewById(R.id.description_text_card);
        textViewHeader = findViewById(R.id.header_text_card);

        progressCardLayout = findViewById(R.id.progressCardContainer);
        progressCardLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchableDialog.show(position);
            }
        });

        progressBarCard = findViewById(R.id.progressCard);
        progressBarCard.setMax(cardList.size());
        currentCardNumberTextView = findViewById(R.id.current_card_number_text_view);
        searchableDialog = new SearchableDialog(this, searchCardItems, "Выберите:");
        searchableDialog.setOnItemSelected(new OnSearchItemSelected() {
            @Override
            public void onClick(int position, SearchListItem searchListItem) {
                CardsActivity.this.position = position;
                setByPosition();
            }
        });


    }


    private void setByPosition() {
        progressBar.setVisibility(View.VISIBLE);
        // infoText.setVisibility(View.GONE);
        draweeView.setMinimumHeight(0);
        draweeView.setImageBitmap(null);
        Card currentCard = cardList.get(position);
        buttonBack.setEnabled(position != 0);
        buttonNext.setEnabled(position != cardList.size() - 1);
        currentCardNumberTextView.setText((position + 1) + " из " + cardList.size());
        progressBarCard.setProgress(position + 1);

        //для теста
        final String[] list = {FullHelper.imgUrl(currentCard.getImgName())};
        //initialize();
        final Uri imageUri = Uri.parse(list[0]);
        Glide.with(this)
                .asBitmap()
                .load(imageUri)
                .listener(new RequestListener<Bitmap>() {
                    @Override
                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Bitmap> target, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(Bitmap resource, Object model, Target<Bitmap> target, DataSource dataSource, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        // infoText.setVisibility(View.VISIBLE);
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
                new ImageViewer.Builder(CardsActivity.this, list)
                        .setStartPosition(0)
                        .show();
            }
        });

        textViewHeader.setText(FullHelper.correctTitle(currentCard.getHeading()));
        textViewDescription.setText(currentCard.getDescription());
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("position", position);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt("position", position);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (themeAb != null) {
            themeAb.setThemeState(position).save(dbHelper);
        }
    }
}
