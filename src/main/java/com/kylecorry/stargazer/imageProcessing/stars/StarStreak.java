package com.kylecorry.stargazer.imageProcessing.stars;


import org.opencv.core.Point;

/**
 * A representation of a star streak in an image.
 */
public class StarStreak {
    private Point start, end;

    /**
     * A representation of a star streak in an image.
     *
     * @param start The starting point of the streak.
     * @param end   The ending point of the streak.
     */
    public StarStreak(Point start, Point end) {
        this.start = start;
        this.end = end;
    }

    /**
     * Set the starting point of the streak.
     *
     * @param start The starting point.
     */
    public void setStart(Point start) {
        this.start = start;
    }

    /**
     * Set the ending point of the streak.
     *
     * @param end The ending point.
     */
    public void setEnd(Point end) {
        this.end = end;
    }

    /**
     * Get the starting point of the streak.
     *
     * @return The starting point of the streak.
     */
    public Point getStart() {
        return start;
    }

    /**
     * Get the ending point of the streak.
     *
     * @return The ending point of the streak.
     */
    public Point getEnd() {
        return end;
    }
}
