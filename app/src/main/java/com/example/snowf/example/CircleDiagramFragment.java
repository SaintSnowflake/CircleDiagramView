package com.example.snowf.example;

import android.graphics.Color;

/**
 * Set the random color and store fragment value.
 * @author SaintSnowflake
 * @version 1.0
 */
public final class CircleDiagramFragment {
    private int value;
    private int color;

    public CircleDiagramFragment(int value) {
        this.value = value;
        color = Color.argb(255, (int) (Math.random() * 255), (int) (Math.random() * 255), (int) (Math.random() * 255));
    }

    public int getValue() {
        return value;
    }

    public int getColor() {
        return color;
    }
}
