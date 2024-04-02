package com.example.Mosaic.mosaic.development;

import javax.imageio.ImageIO;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Map;
import javax.swing.*;
import java.awt.*;

public class Test {


    public static void main(String[] args) {
        /*
        try { URL imageURL = new
        URL("https://www.colorabout.com/images/color/rgb/0-255-255.jpg?v=1"); // Ваш
        URL изображения BufferedImage image = ImageIO.read(imageURL);

        double[] avgColor = Mosaic.averageColor(image);
        System.out.println("Average Color (R, G, B): (" + avgColor[0] + ", " +
        avgColor[1] + ", " + avgColor[2] + ")"); } catch (Exception e) {
        e.printStackTrace(); }*/


        // Проверка функции resize
        /*try {
            // Укажите путь к вашему изображению
            BufferedImage inputImage = ImageIO.read(new File("src/main/java/com/example/Mosaic/images/img.jpg"));
            int newWidth = 200; // Новая ширина изображения
            BufferedImage resizedImage = Mosaic.resize(inputImage, newWidth);
            //. Укажите путь для сохранения измененного изображения
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
    }*/
        createAndShowGUI();
    }

    private static void createAndShowGUI() {
        JFrame frame = new JFrame("Создание мозайки");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 400);

        //Создание панели
        JPanel panel = new JPanel();
        panel.setLayout(new GridLayout(2, 1));
        panel.setSize(400, 400);

        // Создание кнопки выбора файла
        String inputFileButtonName = "Выбрать файл";
        JButton inputFileButton = new JButton(inputFileButtonName);
        JLabel inputFileLabel = new JLabel("Файл не выбран");
        inputFileButton.addActionListener(new OperationButtonClickListener(inputFileButtonName, inputFileLabel));
        panel.add(inputFileButton);
        panel.add(inputFileLabel);

        panel.setLayout(new GridLayout(6, 2));

        //Создаем выподающий список с шаблонами
        JLabel templateLabel = new JLabel("Выберите шаблон:");
        Choice templateChoice = new Choice(); // Добавляем элементы в выпадающий список
        String templatePath = ""; //Путь к выбранному шаблону
        templateChoice.add("Цветы");
        templateChoice.add("SomeThing");
        templateChoice.select(0);// Устанавливаем начальное выбранное значение
        // Добавляем слушателя событий для выбора элементов в списке
        templateChoice.addItemListener(new OperationChoiceListener(templateChoice.getSelectedItem()));
        panel.add(templateLabel);
        panel.add(templateChoice);


        //Создаем поле для ввода размера tiles
        JLabel tileSizeLabel = new JLabel("Введите размер элемента мозайк:");
        JTextField tileSizeField = new JTextField();
        panel.add(tileSizeLabel);
        panel.add(tileSizeField);


        //Создание кнопки Создать мозайку
        String createMosaicButtonName = "Создать мозайку";
        JButton createMosaic = new JButton(createMosaicButtonName);
        createMosaic.addActionListener(new OperationButtonClickListener(createMosaicButtonName, tileSizeField));
        panel.add(createMosaic);

        frame.add(panel);
        frame.setVisible(true);
    }

}
