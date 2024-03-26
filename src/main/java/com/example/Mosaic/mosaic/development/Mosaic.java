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

                r += red;
                g += green;
                b += blue;
            }
        }
        double totalPixels = width * height;
        return new double[]{r / totalPixels, g / totalPixels, b / totalPixels};
    }
    // изменяем размер изображения на новое значение newWidth
    public BufferedImage resize(BufferedImage inputImg, int newWidth) {
        int width = inputImg.getWidth();
        int height = inputImg.getHeight();
        double ratio = (double) width / newWidth;
        double stepX = 0.0, stepY = 0.0;

        BufferedImage outputImg = new BufferedImage((int) (newWidth), (int) (height / ratio), BufferedImage.TYPE_INT_ARGB);
        for (int y = 0; y < height; y++) {

            for (int x = 0; x < width; x++) {
                int rgb = inputImg.getRGB(x, y);
                int alpha = (rgb >> 24) & 0xFF;
                int red = (rgb >> 16) & 0xFF;
                int green = (rgb >> 8) & 0xFF;
                int blue = (rgb) & 0xFF;


                stepX = (x / ratio);
                if (stepX > (int) (newWidth))
                    stepX--;

                stepY = (y / ratio);
                if (stepY > (int) (height / ratio))
                    stepY--;

                outputImg.setRGB((int) stepX , (int) stepY,
                        (alpha << 24) | (red << 16) | (green << 8) | blue);

            }

        }

        return outputImg;
    }

}
