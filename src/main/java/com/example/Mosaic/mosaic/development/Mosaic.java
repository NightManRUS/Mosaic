package com.example.Mosaic.mosaic.development;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.awt.Graphics2D;
import java.util.concurrent.TimeUnit;

public class Mosaic {

    // Определение базы данных плиток
    public static Map<String, double[]> TILESDB = new HashMap<>();

    // Клонирование базы данных плиток при каждой генерации фотомозаики
    public static Map<String, double[]> cloneTilesDB() {
        Map<String, double[]> db = new HashMap<>();
        for (Map.Entry<String, double[]> entry : TILESDB.entrySet()) {
            db.put(entry.getKey(), entry.getValue().clone());
        }
        return db;
    }

    // ищем средний цвет картинки
    public static double[] averageColor(BufferedImage img) {
        int width = img.getWidth();
        int height = img.getHeight();
        double r = 0.0, g = 0.0, b = 0.0;

        for (int y = 0; y < height; y++) {
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

                outputImg.setRGB((int) stepX, (int) stepY,
                        (alpha << 24) | (red << 16) | (green << 8) | blue);

            }

        }

        return outputImg;
    }

    // объявляем tilesDB в памяти (храним название картинки и значение RGB)
    public static void tilesDB(String path) {
        System.out.println(path);
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
//        for (Map.Entry<String, double[]> f : db.entrySet()) {
//            count++;
//            System.out.println(f.getValue()[0] + "," + f.getValue()[1] + "," + f.getValue()[2] + "  " + f.getKey() + "  " + count);
//
//        }

        TILESDB = db;
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

    // Функция обрезки изображения с использованием блокирующей очереди
    public static BlockingQueue<BufferedImage> cutWithChannel(BufferedImage original, Map<String,
            double[]> db, int tileSize, int x1, int y1, int x2, int y2) {
        BlockingQueue<BufferedImage> queue = new LinkedBlockingQueue<>();
        // Создаем новый поток для обработки изображения.
        new Thread(() -> {
            // Создаем новое изображение, которое будет содержать результат обработки.
            BufferedImage newImage = new BufferedImage(x2 - x1, y2 - y1, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = newImage.createGraphics();

            // Проходим по всем координатам изображения с шагом равным размеру плитки.
            for (int y = 0; y < y2; y += tileSize) {
                for (int x = 0; x < x2; x += tileSize) {
                    // Получаем цвет пикселя из оригинального изображения.
                    int rgba = original.getRGB(x, y);

                    // Извлекаем красный (R), зеленый (G) и синий (B) каналы
                    int red = (rgba >> 16) & 0xFF; // Красный канал
                    int green = (rgba >> 8) & 0xFF; // Зеленый канал
                    int blue = rgba & 0xFF; // Синий канал

                    double[] color = {red, green, blue};
                    // Находим ближайшую плитку в базе данных.
                    String nearestTile = nearest(color, db);
                    try {
                        // Читаем изображение выбранной плитки из файла.

                        BufferedImage img = ImageIO.read(new File(nearestTile));
                        // Изменяем размер плитки на заданный tileSize.
                        BufferedImage resizedTile = resize(img, tileSize);
                        // Рисуем измененную плитку на новом изображении.
                        g2d.drawImage(resizedTile, x - x1, y - y1, null);

                    } catch (IOException e) {
                        System.out.println("Error: " + e.getMessage());
                    }
                }
            }

            // Завершаем работу графического контекста.
            g2d.dispose();

            // Добавляем обработанное изображение в блокирующую очередь.
            queue.offer(newImage);
        }).start();
        // Возвращаем блокирующую очередь с обработанным изображением.
        return queue;
    }


    // Функция для комбинирования изображений из четырех каналов в одно изображение
    public static BlockingQueue<BufferedImage> combine(Rectangle rectangle, BlockingQueue<BufferedImage> c1,
                                                       BlockingQueue<BufferedImage> c2, BlockingQueue<BufferedImage> c3,
                                                       BlockingQueue<BufferedImage> c4) {
        BlockingQueue<BufferedImage> resultBlockingQueue = new LinkedBlockingQueue<>(); // Создаем блокирующую очередь для результата
        new Thread(() -> { // Создаем новый поток для обработки изображений
            try {
                // Создаем новое изображение
                BufferedImage img = new BufferedImage(rectangle.width, rectangle.height, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2d = img.createGraphics(); // Получаем графический контекст для рисования на изображении
                for (int i = 0; i < 4; i++) { // Цикл по четырем каналам
                    BufferedImage CurrentImg = null; // Переменная для текущего изображения из канала
                    BlockingQueue<BufferedImage> channel = null; // Переменная для текущего канала
                    int x = 0, y = 0; // Координаты для рисования изображения на общем изображении
                    switch (i) { // Определение текущего канала и его координат на общем изображении
                        case 0:
                            CurrentImg = c1.take(); // Получаем изображение из первого канала
                            channel = c1; // Устанавливаем текущий канал
                            x = rectangle.x; // Устанавливаем координату X
                            y = rectangle.y; // Устанавливаем координату Y
                            break;
                        case 1:
                            CurrentImg = c2.take(); // Получаем изображение из второго канала
                            channel = c2; // Устанавливаем текущий канал
                            x = rectangle.x + rectangle.width / 2; // Устанавливаем координату X
                            y = rectangle.y; // Устанавливаем координату Y
                            break;
                        case 2:
                            CurrentImg = c3.take(); // Получаем изображение из третьего канала
                            channel = c3; // Устанавливаем текущий канал
                            x = rectangle.x; // Устанавливаем координату X
                            y = rectangle.y + rectangle.height / 2; // Устанавливаем координату Y
                            break;
                        case 3:
                            CurrentImg = c4.take(); // Получаем изображение из четвертого канала
                            channel = c4; // Устанавливаем текущий канал
                            x = rectangle.x + rectangle.width / 2; // Устанавливаем координату X
                            y = rectangle.y + rectangle.height / 2; // Устанавливаем координату Y
                            break;
                    }
                    if (CurrentImg != null) { // Если изображение получено из канала
                        g2d.drawImage(CurrentImg, x, y, null); // Наносим изображение на общее изображение
                        channel.put(CurrentImg); // Помещаем изображение обратно в канал
                    }
                }
                g2d.dispose(); // Освобождаем графический контекст

                // Помещаем изображение в результативную очередь
                resultBlockingQueue.offer(img);
            } catch (InterruptedException e) { // Обработка исключений
                e.printStackTrace(); // Вывод стека вызовов исключения
            }
        }).start(); // Запускаем поток для обработки изображений
        return resultBlockingQueue; // Возвращаем результативную очередь
    }

    public static BufferedImage mosaic(BufferedImage img, int tileSize) throws InterruptedException, IOException {
        long t0 = System.currentTimeMillis();

        // Получаем размеры оригинального изображения
        int width = img.getWidth();
        int height = img.getHeight();

        // Клонируем базу данных плиток
        Map<String, double[]> db = cloneTilesDB();

        Rectangle rect = new Rectangle(0, 0, width, height);

        // Разделяем оригинальное изображение на четыре части
        BlockingQueue<BufferedImage> c1 = cutWithChannel(img, db, tileSize, 0, 0, width / 2, height / 2);
        BlockingQueue<BufferedImage> c2 = cutWithChannel(img, db, tileSize, width / 2, 0, width, height / 2);
        BlockingQueue<BufferedImage> c3 = cutWithChannel(img, db, tileSize, 0, height / 2, width / 2, height);
        BlockingQueue<BufferedImage> c4 = cutWithChannel(img, db, tileSize, width / 2, height / 2, width, height);

        // Соединяем полученные изображения
        BufferedImage result = combine(rect, c1, c2, c3, c4).take();

        long t1 = System.currentTimeMillis();

        // Выводим результаты обработки
        System.out.println("Продолжительность: " + (t1 - t0) + " ms");

        // Возвращаем результаты обработки
        return result;
    }
}
