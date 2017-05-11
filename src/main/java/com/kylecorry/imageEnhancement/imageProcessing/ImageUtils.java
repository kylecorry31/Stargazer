package com.kylecorry.imageEnhancement.imageProcessing;

import java.awt.*;
import java.awt.image.*;

/**
 * Created by Kylec on 5/9/2017.
 */
public class ImageUtils {

    public static BufferedImage copyImage(BufferedImage source, int type) {
        BufferedImage b = new BufferedImage(source.getWidth(), source.getHeight(), type);
        Graphics2D g = b.createGraphics();
        g.drawImage(source, 0, 0, null);
        g.dispose();
        return b;
    }

    public static BufferedImage toRGB(BufferedImage source){
        return copyImage(source, BufferedImage.TYPE_INT_RGB);
    }
}
