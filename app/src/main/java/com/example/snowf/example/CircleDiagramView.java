package com.example.snowf.example;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Draw a circle diagram.
 * @author SaintSnowflake
 * @version 1.0
 */
public class CircleDiagramView extends View {
    public ArrayList<CircleDiagramFragment> fragments = new ArrayList<>();
    private float rectangleSize;
    private RectF rectf;
    private Paint p;

    /** Angle between touch point and coordinate line X.
     * Has the value 360, if not selected no fragment. */
    private double touchAngle;

    public CircleDiagramView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.touchAngle = 360;
        p = new Paint();
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        // Set rectangle size using display width. Around the rectangle space the size of 50 px.
        this.rectangleSize = metrics.widthPixels - 100;
        rectf = new RectF(50, 50, rectangleSize + 50, rectangleSize + 50);
    }

    /**
     * Draw all fragments current diagram.
     * Selected fragment has a black stroke and located at a distance from the center of the diagram.
     */
    @Override
    protected void onDraw(Canvas canvas) {
        // Find the sum of the values of all fragments
        int sum = 0;
        for (int i = 0; i < fragments.size(); i++) {
            sum += fragments.get(i).getValue();
        }
        p.setStrokeWidth(10);
        // Set starting angle
        float angle = -90;
        // For all fragments do
        for (int i = 0; i < fragments.size(); i++) {
            // Find the angle between the lines forming the current fragment
            float fragmentSize = 360f / ((float) sum / fragments.get(i).getValue());
            p.setStyle(Paint.Style.FILL);
            // Set the fragment color
            p.setColor(fragments.get(i).getColor());
            Log.d("myLog", "New fragment: " + angle + " " + (angle + fragmentSize) + ", fragmentSize " + fragmentSize);
            // If touch point is between the lines forming the current fragment then
            // (It means that the current fragment was selected)
            if (angle < this.touchAngle && this.touchAngle < angle + fragmentSize) {
                // A single fragment does not need to move
                if (fragments.size() > 1) {
                    // Find the fragment offset values from the center of the diagram
                    float sin = (float) Math.sin(Math.toRadians(angle + fragmentSize / 2)) * 50;
                    float cos = (float) Math.cos(Math.toRadians(angle + fragmentSize / 2)) * 50;
                    Log.d("myLog", "Angle: " + (angle + fragmentSize / 2) + "Sin: " + sin + ", cos: " + cos);
                    // Set new rectangle coordinates using fragment offset values
                    rectf = new RectF(50 + cos, 50 + sin, rectangleSize + 50 + cos, rectangleSize + 50 + sin);
                }
                // Draw new fragment
                canvas.drawArc(rectf, angle, fragmentSize, true, p);
                // Draw black stroke for fragment
                p.setStyle(Paint.Style.STROKE);
                p.setColor(Color.WHITE);
                canvas.drawArc(rectf, angle, fragmentSize, true, p);
                // Show message about fragment value and share of sector
                String toastMessage = "The value is " + fragments.get(i).getValue() + ".\n" +
                "The share of sector is " + (float) 100 / sum * fragments.get(i).getValue() + "%.";
                Toast toast = Toast.makeText(super.getContext(),
                        toastMessage,
                        Toast.LENGTH_SHORT);
                toast.show();
                // "Deleted" information about touch point
                this.touchAngle = 360;
                // Set old rectangle coordinates
                rectf = new RectF(50, 50, rectangleSize + 50, rectangleSize + 50);
            }
            else {
                // Draw new fragment
                canvas.drawArc(rectf, angle, fragmentSize, true, p);
            }
            // Add fragment size to starting angle
            angle += fragmentSize;
        }
    }

    public void setTouchAngle(double touchAngle) {
        this.touchAngle = touchAngle;
    }
}
