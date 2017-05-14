package com.kylecorry.stargazer.imageProcessing.stars;

import org.opencv.core.*;

import java.util.*;

/**
 * Created by Kylec on 5/9/2017.
 */
public class StarFinder {


    public PriorityQueue<StarPair> matchStars(List<Point> stars1, List<Point> stars2) { // O(n^2logn)
        PriorityQueue<StarPair> starPairs = new PriorityQueue<>(Comparator.comparingDouble(StarPair::getDistance));
        for (Point star : stars2) {
            starPairs.add(findClosest(star, stars1));
        }
        return starPairs;
    }

    private StarPair findClosest(Point star1, List<Point> stars) { // O(n)
        StarPair minPair = null;

        for (Point star : stars) {
            double dist = distance(star, star1);
            if (minPair == null) {
                minPair = new StarPair(star1, star, dist);
            } else if (dist < minPair.distance) {
                minPair = new StarPair(star1, star, dist);
            }
        }

        return minPair;
    }

    private double distance(Point p1, Point p2) {
        return Math.sqrt(Math.pow(p1.x - p2.x, 2) + Math.pow(p1.y - p2.y, 2));
    }


    public class StarPair {
        Point star1, star2;
        double distance;

        public StarPair(Point star1, Point star2, double distance) {
            this.star1 = star1;
            this.star2 = star2;
            this.distance = distance;
        }

        public Point getStar1() {
            return star1;
        }

        public Point getStar2() {
            return star2;
        }

        public double getDistance() {
            return distance;
        }
    }

}
