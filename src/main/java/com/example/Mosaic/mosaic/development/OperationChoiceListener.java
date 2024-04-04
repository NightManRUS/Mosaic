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
                if(TilesDBManager.TILESDB == null){
                    templatePath = Template.FLOWERS.getTemplate();
                    TilesDBManager.tilesDB(templatePath);
                    TilesDBManager.writeTilesBD(TilesDBManager.TILESDB, "Flowers.txt");
                }

                break;
            case "SomeThing":
                templatePath = Template.SOMETHING.getTemplate();
            default:
                templatePath = "";
        }

    }
}
