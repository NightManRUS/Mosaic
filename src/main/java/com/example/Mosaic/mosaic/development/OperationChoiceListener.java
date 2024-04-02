package com.example.Mosaic.mosaic.development;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;


public class OperationChoiceListener implements ItemListener {

    public String selectedTemplate;
    public static String templatePath; //Путь к шаблону

    public OperationChoiceListener(String selectedTemplate) {
        this.selectedTemplate = selectedTemplate;
    }
    @Override
    public void itemStateChanged(ItemEvent e) {
        // Обработка выбора элемента
        String selectedValue = selectedTemplate;
        switch (selectedValue) {
            case "Цветы":
                templatePath = Template.FLOWERS.getTemplate();
                Mosaic.tilesDB(templatePath);
                break;
            case "SomeThing":
                return;
        }

    }
}
