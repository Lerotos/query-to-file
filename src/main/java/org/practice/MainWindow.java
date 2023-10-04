package org.practice;

import javax.swing.*;
import java.sql.SQLException;


public class MainWindow {
    private final JFrame window;
    private String connection;
    private String userName;
    private String userPassword;
    private String path;
    private String query;

    public MainWindow(){
        window = new JFrame();
        window.setTitle("Database to Excel");
        window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        window.setSize(800, 550);
        window.setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        //generate input Fields + confirm Buttons
        JTextField con = new JTextField("Enter Database Connection (Example: jdbc:mysql://localhost:3306/db)",40);
        JButton confirmCon = new JButton("Confirm Database");
        JTextField user = new JTextField("Enter Database User",40);
        JButton confirmUser = new JButton("Confirm User");
        JTextField userPass = new JPasswordField("Enter User Password",40);
        JButton confirmPass = new JButton("Confirm Password");
        JTextField outputPath = new JTextField("Enter Output Path",40);
        JButton confirmPath = new JButton("Confirm Path");
        JButton confirmQuery = new JButton("Confirm Query");
        JTextArea queryArea = new JTextArea("Enter Query", 20, 60);

        //disable most of them
        user.setEnabled(false);
        confirmUser.setEnabled(false);
        userPass.setEnabled(false);
        confirmPass.setEnabled(false);
        outputPath.setEnabled(false);
        confirmPath.setEnabled(false);
        queryArea.setEnabled(false);
        confirmQuery.setEnabled(false);

        //Button logic, saves input + enables further input Fields
        confirmCon.addActionListener(e -> {
            connection = con.getText();
            user.setEnabled(true);
            confirmUser.setEnabled(true);
        });

        confirmUser.addActionListener(e -> {
            userName = user.getText();
            userPass.setEnabled(true);
            confirmPass.setEnabled(true);
        });

        //additionally tests the connection
        confirmPass.addActionListener(e -> {
            userPassword = userPass.getText();
            try {
                Query.testCon(connection, userName, userPassword);
            } catch (SQLException ex) {
                exceptionWindow(ex);
            }
            outputPath.setEnabled(true);
            confirmPath.setEnabled(true);
        });

        confirmPath.addActionListener(e -> {
            path = outputPath.getText();
            queryArea.setEnabled(true);
            confirmQuery.setEnabled(true);
        });

        confirmQuery.addActionListener(e -> {
            query = queryArea.getText();
            Query.query(path, query, connection, userName, userPassword);
        });

        //adds parts to the panel
        panel.add(con);
        panel.add(confirmCon);
        panel.add(user);
        panel.add(confirmUser);
        panel.add(userPass);
        panel.add(confirmPass);
        panel.add(outputPath);
        panel.add(confirmPath);
        panel.add(queryArea);
        panel.add(confirmQuery);

        window.add(panel);

    }

    public void show(){
        window.setVisible(true);
    }

    //generates new Window when exception is thrown
    public static void exceptionWindow(Exception e){
        JFrame eWindow = new JFrame();
        eWindow.setTitle("Error");
        eWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        eWindow.setSize(400, 200);
        eWindow.setLocationRelativeTo(null);

        JPanel panel = new JPanel();

        JTextArea errorArea = new JTextArea(e.toString(), 6, 30);
        errorArea.setLineWrap(true);
        errorArea.setWrapStyleWord(true);
        errorArea.setOpaque(false);
        errorArea.setEditable(false);

        JButton okButton = new JButton("Confirm");

        okButton.addActionListener(x -> eWindow.dispose());

        panel.add(errorArea);
        panel.add(okButton);
        eWindow.add(panel);
        eWindow.setVisible(true);
    }
}
