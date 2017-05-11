package com.kylecorry.imageEnhancement;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;

/**
 * Created by Kylec on 5/11/2017.
 */
public class StarMerge {

    public BufferedImage mergeStars(BufferedImage stars, BufferedImage hdr) {
        BufferedImage output = ImageUtils.copyImage(stars, stars.getType());
        for (int x = 0; x < output.getWidth(); x++) {
            for (int y = 0; y < output.getHeight(); y++) {
                Color starColor = new Color(stars.getRGB(x, y));
                Color hdrColor = new Color(hdr.getRGB(x, y));
                float[] colors = Color.RGBtoHSB(hdrColor.getRed(), hdrColor.getGreen(), hdrColor.getBlue(), null);
                Color c = new Color(colors[0], colors[1], colors[2]);

                float[] colors2 = Color.RGBtoHSB(starColor.getRed(), starColor.getGreen(), starColor.getBlue(), null);
                Color c2 = new Color(colors2[0], colors2[1], colors2[2]);


                boolean bright = inRange(c.getBlue(), 100, 180) || inRange(c2.getBlue(), 120, 255);
                boolean hue = inRange(c.getRed(), 100, 200);
                boolean sat = inRange(c.getRed(), 145, 160);

                if (!(bright && hue && sat)) {
//                    output.setRGB(x, y, Color.BLACK.getRGB());
                    output.setRGB(x, y, hdrColor.getRGB());
                } else {
//                    output.setRGB(x, y, Color.WHITE.getRGB());
                }
            }
        }

        return output;
    }


    private boolean inRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

    private double brightness(Color color) {
        return (color.getRed() + color.getGreen() + color.getBlue()) / 3.0;
    }

    private double distance(Color first, Color second) {
        int r = first.getRed() - second.getRed();
        int g = first.getGreen() - second.getGreen();
        int b = first.getBlue() - second.getBlue();
        return Math.sqrt(r * r + g * g + b * b);
    }


}
