package com.example.Mosaic.mosaic.development;

import java.awt.image.BufferedImage;

public class Mosaic {


    // ищем средний цвет картинки
    public double[] averageColor(BufferedImage img){
        int width = img.getWidth();
        int height = img.getHeight();
        double r = 0.0, g = 0.0, b = 0.0;

        for (int y = 0; y < height; y++){
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);

                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = (rgb) & 0xFF;

                r += (double) red;
                g += (double) green;
                b += (double) blue;
            }
        }
        double totalPixels = width * height;
        return new double[]{r / totalPixels, g / totalPixels, b / totalPixels};
    }
}
