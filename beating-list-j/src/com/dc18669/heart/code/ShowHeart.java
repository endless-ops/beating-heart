package com.dc18669.heart.code;

import java.awt.Color;
import javax.swing.JFrame;

public class ShowHeart extends JFrame {
    private static final int WIDTH = 640;
    private static final int HEIGHT = 640;

    public ShowHeart() {
        this.setWin();
        this.showHeart();
    }

    private void setWin() {
        this.setTitle("跳动的爱");
        this.setSize(640, 640);
        this.getContentPane().setBackground(Color.BLACK);
        this.setVisible(true);
        this.setDefaultCloseOperation(3);
    }

    private void showHeart() {
        this.getContentPane().add(new DrawHeart());
    }
}