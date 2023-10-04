package org.practice;

import javax.swing.*;
import java.awt.*;

public class Main extends Component {


    public static void main(String[] args){

        SwingUtilities.invokeLater(() -> {
            MainWindow mainWindow = new MainWindow();
            mainWindow.show();
        });




    }







}