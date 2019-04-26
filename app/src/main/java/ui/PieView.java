package ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by User on 7/31/2018.
 */

public class PieView extends View {

    int centerX;
    int centerY;
    int radius = 60;
    int degree;

    Paint pieColor, centerColor;
    RectF oval;


    public PieView(Context context) {
        super(context);
        init();
    }

    public PieView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PieView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        int centerY = canvas.getHeight()/2;
        int centerX = canvas.getWidth()/2;
//        System.out.println(centerX + " " + centerY);

        oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        canvas.drawArc(oval, -90, degree, false, pieColor);
//        canvas.drawCircle(centerX, centerY, radius*2/4, centerColor);
    }

    private void init(){

        pieColor = new Paint();
        pieColor.setColor(MyColor.PRIMARY);
        //Shader gradient = new SweepGradient(0,getMeasuredHeight()/2, Color.RED, Color.WHITE);
        pieColor.setAntiAlias(true);
        pieColor.setStyle(Paint.Style.STROKE);
        pieColor.setStrokeWidth(radius*3/2);

        centerColor = new Paint();
        centerColor.setColor(MyColor.SECONDARY);
        //Shader gradient = new SweepGradient(0,getMeasuredHeight()/2, Color.RED, Color.WHITE);
        centerColor.setAntiAlias(true);
        centerColor.setStyle(Paint.Style.FILL);

        oval = new RectF(centerX - radius, centerY - radius, centerX + radius, centerY + radius);

    }

    public int getDegree() {
        return degree;
    }

    public void setDegree(int degree) {
        this.degree = degree;
        postInvalidate();
    }
}
