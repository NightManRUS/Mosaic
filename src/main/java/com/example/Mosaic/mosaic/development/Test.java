package com.example.Mosaic.mosaic.development;

import javax.swing.*;
import java.awt.*;

public class Test {


    public static void main(String[] args) {

//        String templatePath = Template.FLOWERS.getTemplate();
//        TilesDBManager.tilesDB(templatePath);
//        TilesDBManager.writeTilesBD(TilesDBManager.TILESDB, "Flowers.txt");

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
        templateChoice.add("");
        templateChoice.add("Цветы");
        templateChoice.add("SomeThing");
        templateChoice.select(0);// Устанавливаем начальное выбранное значение
        // Добавляем слушателя событий для выбора элементов в списке
        templateChoice.addItemListener(new OperationChoiceListener(templateChoice));
        panel.add(templateLabel);
        panel.add(templateChoice);


        //Создаем поле для ввода размера tiles
        JLabel tileSizeLabel = new JLabel("Введите размер элемента мозайки:");
        JTextField tileSizeField = new JTextField();
        panel.add(tileSizeLabel);
        panel.add(tileSizeField);


        //Создаем поле для ввода минимального расстояния между одинаковыми картинками
        JLabel minDistanceLabel = new JLabel("Минимальное расстояние между одинаковыми картинками:");
        JTextField minDistanceField = new JTextField();
        panel.add(minDistanceLabel);
        panel.add(minDistanceField);


        //Создаем выподающий список с шаблонами
        JLabel mosaicSizeLabel = new JLabel("Выберите шаблон:");
        Choice mosaicSizeChoice = new Choice(); // Добавляем элементы в выпадающий список
        mosaicSizeChoice.add("");
        mosaicSizeChoice.add("Оригинал");
        mosaicSizeChoice.add("2 мегапикс. = 1920x1080");
        mosaicSizeChoice.add("6 мегапикс. = 3326x1871");
        mosaicSizeChoice.add("13 мегапикс. = 4096×3072");
        mosaicSizeChoice.add("24 мегапикс. = 7680x3148");
        mosaicSizeChoice.add("62 мегапикс. = 12288x5036");
        mosaicSizeChoice.select(0);// Устанавливаем начальное выбранное значение
        // Добавляем слушателя событий для выбора элементов в списке
        mosaicSizeChoice.addItemListener(new OperationChoiceListener(mosaicSizeChoice));
        panel.add(mosaicSizeLabel);
        panel.add(mosaicSizeChoice);

        //Создание кнопки Создать мозайку
        String createMosaicButtonName = "Создать мозайку";
        JButton createMosaic = new JButton(createMosaicButtonName);
        createMosaic.addActionListener(new OperationButtonClickListener(createMosaicButtonName,
                tileSizeField, minDistanceField));
        panel.add(createMosaic);

        frame.add(panel);
        frame.setVisible(true);
    }

}
