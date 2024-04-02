package com.example.Mosaic.mosaic.development;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class OperationButtonClickListener implements ActionListener {

    private static String inputFileName = "1";
    public String buttonName; // Название нажатой кнопки
    public JLabel label;
    public JTextField tileSizeField;

    public OperationButtonClickListener(String buttonName) {
        this.buttonName = buttonName;
    }

    //Перегрузка класса для кнопки Выбрать файл
    public OperationButtonClickListener(String buttonName, JLabel label) {
        this.buttonName = buttonName;
        this.label = label;
    }

    //Перегрузка класса для кнопки Создать мозаику
    public OperationButtonClickListener(String buttonName, JTextField tileSizeField) {
        this.buttonName = buttonName;
        this.tileSizeField = tileSizeField;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (buttonName) {
            //Если нажата кнопка Выбрать файл открывается проводник
            case "Выбрать файл":
                // Создаем экземпляр FileDialog
                FileDialog fileChooser = new FileDialog((java.awt.Frame) null, "Выберите файл", FileDialog.LOAD);
                fileChooser.setVisible(true); // Открываем диалоговое окно выбора файла

                String selectedFilePath = fileChooser.getFile(); // Получаем выбранный файл
                if (selectedFilePath != null) { // Проверяем, был ли выбран файл
                    String selectedFileDirectory = fileChooser.getDirectory(); // Получаем директорию выбранного файла
                    String selectedFileFullPath = selectedFileDirectory + selectedFilePath; // Полный путь к выбранному файлу
                    inputFileName = selectedFileFullPath;
                    label.setText("Выбранный файл: " + inputFileName);
                    System.out.println("Выбранный файл: " + inputFileName); // Выводим путь к выбранному файлу
                } else {
                    System.out.println("Файл не выбран"); // Если файл не выбран, выводим сообщение об этом
                    label.setText("Файл не выбран");

                };
                break;

            //Если нажата кнопка Создать мозайку
            case "Создать мозайку":
                Mosaic.cloneTilesDB();
                try {
                    System.out.println(inputFileName);
                    BufferedImage inputImage = ImageIO.read(new File(inputFileName));
                    BufferedImage resultImg = Mosaic.mosaic(inputImage, Integer.parseInt(tileSizeField.getText()));
                    try {
                        ImageIO.write(resultImg, "PNG", new File(
                                "src/main/java/com/example/Mosaic/images/resizedImg.jpg"));
                        System.out.println("Разрешение изображения: " + resultImg.getHeight() + "х" + resultImg.getWidth());
                        System.out.println("Изображение успешно сохранено в файл: " +
                                "src/main/java/com/example/Mosaic/images/resizedImg.jpg");

                    } catch (IOException ex3) {
                        // Если произошла ошибка при сохранении изображения, выводим сообщение об ошибке
                        System.out.println("Ошибка при сохранении изображения: " + ex3.getMessage());
                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                } catch (InterruptedException ex2) {
                    throw new RuntimeException(ex2);
                }
                break;

            default:
                throw new UnsupportedOperationException("Не поддерживаемая операция " + buttonName);
        }
    }

}