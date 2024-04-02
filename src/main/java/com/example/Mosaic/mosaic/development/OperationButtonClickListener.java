package com.example.Mosaic.mosaic.development;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

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
                JFileChooser fileChooser = new JFileChooser();
                int returnValue = fileChooser.showOpenDialog(null);
                if (returnValue == JFileChooser.APPROVE_OPTION) {
                    JOptionPane.showMessageDialog(null, "Файл выбран: "
                            + fileChooser.getSelectedFile().getName());
                    inputFileName = fileChooser.getSelectedFile().getName();
                } else {
                    JOptionPane.showMessageDialog(null, "Файл не выбран");
                };
            case "Продолжить":
                return;
            default:
                throw new UnsupportedOperationException("Не поддерживаемая операция " + buttonName);
        }
    }

}