package com.example.Mosaic.mosaic.development;

import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class OperationChoiceListener implements ItemListener {

    public Choice templateChoice;
    public String selectedTemplate;
    public static String templatePath; //Путь к шаблону

    public OperationChoiceListener(Choice templateChoice) {
        this.templateChoice = templateChoice;
    }

    @Override
    public void itemStateChanged(ItemEvent e) {
        // Обработка выбора элемента
        selectedTemplate = templateChoice.getSelectedItem();
        switch (selectedTemplate) {
            //кейсы для выбора шаблона
            case "Цветы":
                //считывание tilesBD из файла
                TilesDBManager.TILESDB = TilesDBManager.readTilesDB("Flowers.txt");

                //Если файл не существует то создать и записать в файл tilesDB
                if (TilesDBManager.TILESDB == null) {
                    templatePath = Template.FLOWERS.getTemplate();
                    TilesDBManager.tilesDB(templatePath);
                    TilesDBManager.writeTilesBD(TilesDBManager.TILESDB, "Flowers.txt");
                }

                break;
            case "SomeThing":
                templatePath = Template.SOMETHING.getTemplate();


            case "Оригинал":
                Mosaic.mosaicSize = 0;
                break;

            case "2 мегапикс. = 1920x1080":
                Mosaic.mosaicSize = 1920 * 1080;
                break;

            case "6 мегапикс. = 3326x1871":
                Mosaic.mosaicSize = 3326 * 1871;
                break;

            case "13 мегапикс. = 4096×3072":
                Mosaic.mosaicSize = 4096 * 3072;
                break;

            case "24 мегапикс. = 7680x3148":
                Mosaic.mosaicSize = 7680 * 3148;
                break;

            case "62 мегапикс. = 12288x5036":
                Mosaic.mosaicSize = 12288 * 5036;
                break;


            default:
                templatePath = "";
        }

    }
}
