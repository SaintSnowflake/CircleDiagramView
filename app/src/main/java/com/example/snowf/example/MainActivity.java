package com.example.snowf.example;

import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener {
    private int displayWidth;
    private int displayHeight;
    private CircleDiagramView circleDiagram;
    private TextView valueTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DisplayMetrics metrics = this.getResources().getDisplayMetrics();
        this.displayWidth = metrics.widthPixels;
        this.displayHeight = metrics.heightPixels;
        Log.d("MyLogs", "Display: width " + displayWidth + ", height " + displayHeight);

        valueTextView = (TextView) findViewById(R.id.valueTextView);
        circleDiagram = (CircleDiagramView) findViewById(R.id.circleDiagram);
        circleDiagram.setOnTouchListener(this);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.addNewFragmentButton:
                try {
                    // Add new fragment to circle diagram and invalidate it
                    int newValue = Integer.parseInt(valueTextView.getText().toString());
                    CircleDiagramFragment newFragment = new CircleDiagramFragment(newValue);
                    circleDiagram.fragments.add(newFragment);
                    circleDiagram.invalidate();
                } catch (NumberFormatException e) {
                    // If the valueTextView contains the letters, show a warning
                    valueTextView.setText("");
                    Toast toast = Toast.makeText(this,
                            "Введено неверное значение",
                            Toast.LENGTH_SHORT);
                    toast.show();
                    Log.d("MyLogs", "NumberFormatException");
                }
                break;
        }
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        // Get the touch coordinates
        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                Log.d("MyLogs", "Touch in " + x + ", " + y);
                // Reduce the coordinate values, simulating that diagram center is at the point (0, 0)
                x -= displayWidth / 2;
                y -= displayWidth / 2;
                float radius = (displayWidth - 100) / 2;
                // If the touch coordinates are included in a diagram
                if (x * x + y * y <= radius * radius) {
                    Log.d("MyLogs", "Inside circle");
                    Log.d("MyLogs", "Coordinates x: " + x + ", y: " + y);
                    // Find angle between touch point and coordinate line X
                    float a = y / x;
                    double angle = Math.asin(a / (Math.sqrt(a * a + 1))) * 180 / Math.PI;
                    if (x < 0)
                        angle += 180;
                    // Set the angle and invalidate this view
                    circleDiagram.setTouchAngle(angle);
                    circleDiagram.invalidate();
                    Log.d("MyLogs", "Angle is " + angle);
                }
                else {
                    circleDiagram.invalidate();
                    Log.d("MyLogs", "Outside circle");
                }
                break;
        }
        return true;
    }
}
