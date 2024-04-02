package com.example.Mosaic.mosaic.development;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class OperationButtonClickListener implements ActionListener {

    public String inputFileName;
    public String buttonName; // Название нажатой кнопки

    public OperationButtonClickListener(String buttonName) {
        this.buttonName = buttonName;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        switch (buttonName) {
            //Если нажата кнопка Выбрать файл открывается проводник
            case "Выбрать файл":
                FileDialog fileChooser = new FileDialog((java.awt.Frame) null, "Выберите файл", FileDialog.LOAD); // Создаем экземпляр FileDialog
                fileChooser.setVisible(true); // Открываем диалоговое окно выбора файла

                String selectedFilePath = fileChooser.getFile(); // Получаем выбранный файл
                if (selectedFilePath != null) { // Проверяем, был ли выбран файл
                    String selectedFileDirectory = fileChooser.getDirectory(); // Получаем директорию выбранного файла
                    String selectedFileFullPath = selectedFileDirectory + selectedFilePath; // Полный путь к выбранному файлу
                    inputFileName = selectedFileFullPath;
                    System.out.println("Выбранный файл: " + selectedFileFullPath); // Выводим путь к выбранному файлу
                } else {
                    System.out.println("Файл не выбран"); // Если файл не выбран, выводим сообщение об этом
                }
            case "Продолжить":
                return;
            default:
                throw new UnsupportedOperationException("Не поддерживаемая операция " + buttonName);
        }
    }

}