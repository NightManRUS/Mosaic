package com.example.Mosaic.mosaic.development;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;

public class Test {
    public static void main(String[] args) {
        /*
         * try { URL imageURL = new
         * URL("https://www.colorabout.com/images/color/rgb/0-255-255.jpg?v=1"); // Ваш
         * URL изображения BufferedImage image = ImageIO.read(imageURL);
         *
         * double[] avgColor = Mosaic.averageColor(image);
         * System.out.println("Average Color (R, G, B): (" + avgColor[0] + ", " +
         * avgColor[1] + ", " + avgColor[2] + ")"); } catch (Exception e) {
         * e.printStackTrace(); }
         */

        // Проверка функции resize
        try {
            // Укажите путь к вашему изображению
            BufferedImage inputImage = ImageIO.read(new File("src/main/java/com/example/Mosaic/images/img.jpg"));
            int newWidth = 200; // Новая ширина изображения
            BufferedImage resizedImage = Mosaic.resize(inputImage, newWidth);
            // Укажите путь для сохранения измененного изображения
            ImageIO.write(resizedImage, "PNG", new File("src/main/java/com/example/Mosaic/images/resizedImg.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Проверка функции tilesBD
        String path = "C:\\Users\\Kirill\\Desktop\\РГРТУ\\3 курс\\2 семестр\\НИР\\Датасеты\\цветы";
        Map<String, double[]> db = Mosaic.tilesDB(path);

        // Проверка функции nearest
        double[] target = {109.512,89.79443313609467,98.586054437869};
        String fileName = null;
        fileName = Mosaic.nearest(target, db);
        System.out.println(fileName);
    }
}
