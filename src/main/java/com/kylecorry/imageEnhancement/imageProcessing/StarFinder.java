package com.kylecorry.imageEnhancement.imageProcessing;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;

/**
 * Created by Kylec on 5/9/2017.
 */
public class StarFinder {

    private BufferedImage image;

    public StarFinder(BufferedImage image) {
        this.image = image;
    }

    public List<Point> locateStars() {
        List<Point> points = new LinkedList<>();
        BufferedImage gray = grayscale();
        BufferedImage stars = filterStars(gray);

        int border = 800;
        for (int x = border; x < stars.getWidth() - border; x++) {
            for (int y = border; y < stars.getHeight() - border; y++) {
                Color color = new Color(stars.getRGB(x, y));
                if(color.getRed() == 255){
                    points.add(new Point(x, y));
                }
            }
        }
        return points;
    }

    private Color getAverage(Color color) {
        int total = color.getRed() + color.getBlue() + color.getGreen();
        int average = total / 3;
        return new Color(average, average, average);
    }

    private BufferedImage filterStars(BufferedImage gray) {
        BufferedImage mask = new BufferedImage(gray.getWidth(), gray.getHeight(), gray.getType());
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                if (color.getRed() > 80) {
                    mask.setRGB(x, y, Color.WHITE.getRGB());
                } else {
                    mask.setRGB(x, y, Color.BLACK.getRGB());
                }
            }
        }

        // Remove large objects
        boolean[][] painted = new boolean[mask.getHeight()][mask.getWidth()];
        for (int i = 0; i < mask.getHeight(); i++) {
            for (int j = 0; j < mask.getWidth(); j++) {
                if (new Color(mask.getRGB(j, i)).getRed() == 255 && !painted[i][j]) {
                    Queue<Point> queue = new LinkedList<>();
                    List<Point> points = new LinkedList<>();
                    queue.add(new Point(j, i));

                    int pixelCount = 0;
                    while (!queue.isEmpty()) {
                        Point p = queue.remove();

                        if ((p.x >= 0) && (p.x < mask.getWidth() && (p.y >= 0) && (p.y < mask.getHeight()))) {
                            if (!painted[p.y][p.x] && new Color(mask.getRGB(p.x, p.y)).getRed() == 255) {
                                painted[p.y][p.x] = true;
                                pixelCount++;
                                points.add(p);
                                queue.add(new Point(p.x + 1, p.y));
                                queue.add(new Point(p.x - 1, p.y));
                                queue.add(new Point(p.x, p.y + 1));
                                queue.add(new Point(p.x, p.y - 1));
                            }
                        }
                    }
                    if(pixelCount > 50 || pixelCount < 20){
                        for(Point p: points){
                            mask.setRGB(p.x, p.y, Color.BLACK.getRGB());
                        }
                    } else {
//                        for(Point p: points){
//                            mask.setRGB(p.x, p.y, Color.BLACK.getRGB());
//                        }
//                        mask.setRGB(points.get(0).x, points.get(0).y, Color.WHITE.getRGB());
                    }
                }

            }
        }

        try {
            ImageIO.write(mask, "JPEG", new File("Output/" + System.currentTimeMillis() + ".jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        return mask;
    }

    private BufferedImage grayscale() {
        BufferedImage gray = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        for (int x = 0; x < image.getWidth(); x++) {
            for (int y = 0; y < image.getHeight(); y++) {
                Color color = new Color(image.getRGB(x, y));
                Color average = getAverage(color);
                gray.setRGB(x, y, average.getRGB());
            }
        }
        return gray;
    }
}
