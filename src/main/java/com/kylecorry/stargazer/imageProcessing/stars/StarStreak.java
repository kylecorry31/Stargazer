package com.kylecorry.stargazer.imageProcessing.stars;


import org.opencv.core.Point;

/**
 * Created by Kylec on 5/11/2017.
 */
public class StarStreak {
    private Point start, end;

    public StarStreak(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    public void setStart(Point start) {
        this.start = start;
    }

    public void setEnd(Point end) {
        this.end = end;
    }

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }
}
