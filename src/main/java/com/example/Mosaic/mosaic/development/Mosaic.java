package com.example.Mosaic.mosaic.development;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Mosaic {


    // ищем средний цвет картинки
    public static double[] averageColor(BufferedImage img){
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
    public static BufferedImage resize(BufferedImage inputImg, int newWidth) {
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

    // объявляем tilesDB в памяти (храним название картинки и значение RGB)
    public static Map<String, double[]> tilesDB(String path) {
        System.out.println("Start populating tiles db ...");
        Map<String, double[]> db = new HashMap<>();
        File[] files = new File(path).listFiles();
        if (files != null) {
            for (File f : files) {
                String name = path + "/" + f.getName();
                try {
                    BufferedImage img = ImageIO.read(new File(name));
                    db.put(name, averageColor(img));
                } catch (IOException e) {
                    System.out.println("Error: " + e.getMessage() + " " + name);
                }
            }
        } else {
            System.out.println("Error: cannot open directory 'tiles'");
        }
        System.out.println("Finished populating tiles db.");

        int count = 0;
        for (Map.Entry<String, double[]> f : db.entrySet()) {
            count++;
            System.out.println(f.getValue()[0] + "," + f.getValue()[1] + "," +f.getValue()[2]  +"  "+ f.getKey() + "  " + count);

        }

        return db;
    }

    // ищет максимально близко совпадающее изображение
    public static String nearest(double[] target, Map<String, double[]> db) {
        String fileName = null;
        double smallest = 1000000.0;

        for (Map.Entry<String, double[]> entry : db.entrySet()) {
            double[] value = entry.getValue();
            double dist = distance(target, value);

            if (dist < smallest) {
                fileName = entry.getKey();
                smallest = dist;
            }

        }
        if (fileName != null) {
            db.remove(fileName);
        }
        return fileName;

    }

    // возвращает Евклидово расстояние между двумя точками
    public static double distance(double[] p1, double[] p2) {
        return Math.sqrt(sq(p2[0] - p1[0]) + sq(p2[1] - p1[1]) + sq(p2[2] - p1[2]));
    }

    // возвращает квадрат
    public static double sq(double n) {
        return n * n;
    }



}
