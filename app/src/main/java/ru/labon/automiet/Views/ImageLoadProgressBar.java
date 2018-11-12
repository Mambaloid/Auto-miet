package ru.labon.automiet.Views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;

import com.facebook.drawee.drawable.ProgressBarDrawable;

import ru.labon.automiet.R;

/**
 * Created by HP on 28.11.2017.
 */

public class ImageLoadProgressBar extends ProgressBarDrawable {


    private float level;
    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int color;
    private final RectF oval = new RectF();
    private int radius = 60;
    private Context context;
    public ImageLoadProgressBar(Context context){
        paint.setStrokeWidth(15);
        paint.setStyle(Paint.Style.STROKE);
        this.context = context;
        this.color = context.getResources().getColor(R.color.main);
    }

    @Override
    protected boolean onLevelChange(int level) {
        this.level = level;
        invalidateSelf();
        return true;
    }

    @Override
    public void draw(Canvas canvas) {
        oval.set(canvas.getWidth() / 2 - radius, canvas.getHeight() / 2 - radius,
                canvas.getWidth() / 2 + radius, canvas.getHeight() / 2 + radius);

        drawCircle(canvas, level, color);
    }


    private void drawCircle(Canvas canvas, float level, int color) {
        paint.setColor(color);
        float angle;
        angle = 360 / 1f;
        angle = level * angle;
        canvas.drawArc(oval, 0, Math.round(angle), false, paint);
    }

}
