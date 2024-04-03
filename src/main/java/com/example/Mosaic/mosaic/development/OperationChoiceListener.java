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
            case "Цветы":
                templatePath = Template.FLOWERS.getTemplate();
                Mosaic.tilesDB(templatePath);
                break;
            case "SomeThing":
                templatePath = Template.SOMETHING.getTemplate();
            default:
                templatePath = "";
        }

    }
}
