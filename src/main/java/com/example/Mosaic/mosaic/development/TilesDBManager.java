package com.example.Mosaic.mosaic.development;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class TilesDBManager {

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
                    db.put(name, Mosaic.averageColor(img));
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

    //Запись TilesBD в файл
    public static void writeTilesBD(Map<String, double[]> tilesBD, String fileName) {

        String outputPath = "src/main/java/com/example/Mosaic/tilesDB/" + fileName;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath))) {
            if (tilesBD != null) {
                for (Map.Entry<String, double[]> entry : tilesBD.entrySet()) {
                    String key = entry.getKey();
                    double[] values = entry.getValue();

                    // Формируем строку для записи в файл
                    StringBuilder sb = new StringBuilder();
                    sb.append(key).append(": ");
                    for (double value : values) {
                        sb.append(value).append(" ");
                    }
                    sb.append(System.lineSeparator());

                    // Записываем строку в файл
                    writer.write(sb.toString());
                }
            }
            System.out.println("База данных сохранена в файл: " + outputPath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    // Метод для чтения данных из файла
    public static Map<String, double[]> readTilesDB(String fileName) {

        String inputPath = "src/main/java/com/example/Mosaic/tilesDB/" + fileName;

        //Проверка наличия файла
        File file = new File(inputPath);
        if (!file.exists()){
            return null;
        }

        Map<String, double[]> data = new HashMap<>();
        try (BufferedReader reader = new BufferedReader(new FileReader(inputPath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                // Разбиваем строку по символу ":"
                String[] parts = line.split(": ");
                if (parts.length == 2) {
                    String key = parts[0].trim();
                    String[] valuesString = parts[1].trim().split(" ");

                    // Преобразуем значения из строкового представления в double
                    double[] values = new double[valuesString.length];
                    for (int i = 0; i < valuesString.length; i++) {
                        values[i] = Double.parseDouble(valuesString[i]);
                    }

                    // Добавляем данные в Map
                    data.put(key, values);
                }
            }
            System.out.println("Данные успешно считаны из файла: " + inputPath);
        } catch (IOException e) {
            System.out.println("Ошибка при чтении данных из файла: " + e.getMessage());
            e.printStackTrace();
        }
        return data;
    }

}
