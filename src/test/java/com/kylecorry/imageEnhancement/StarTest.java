package com.kylecorry.imageEnhancement;

import com.github.gasrios.raw.io.TiffInputStream;
import com.github.gasrios.raw.lang.TiffProcessorException;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Kylec on 5/10/2017.
 */
public class StarTest {

    @Test
    public void run() {
        BufferedImage hdr = Main.getImage("1.jpg");
        BufferedImage stars = Main.getImage("2.jpg");
        StarMerge starMerge = new StarMerge();
        BufferedImage image = starMerge.mergeStars(stars, hdr);
        try {
            ImageIO.write(image, "JPEG", new File("starsAlignment.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void test() {
        BufferedImage image = Main.getImage("1.jpg");
        int size = 100;
        BufferedImage small = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);
        Graphics2D graphics2D = small.createGraphics();
        int i = 0;
        for (int x = 0; x < image.getWidth() - size; x += size) {
            for (int y = 0; y < image.getHeight() - size; y += size) {
                for (int j = 0; j < size; j++) {
                    for (int k = 0; k < size; k++) {
                        small.setRGB(j, k, image.getRGB(x + j, y + k));
                    }
                }
                try {
                    ImageIO.write(small, "JPEG", new File("Output/" + i + ".jpg"));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                i++;
//                if(i > 200)
//                    break;
            }
//            if(i > 200)
//                break;

        }
        graphics2D.dispose();
    }

}
