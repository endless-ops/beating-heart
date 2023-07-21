package com.dc18669.heart.code;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class DrawHeart extends Canvas implements Runnable {
    private static final int COUNT = 2000;
    private static final double PI = Math.PI;
    private static final int DEFAULT_SIZE = 10;
    private final Color color = new Color(231, 124, 142);
    private int centerX;
    private int centerY;
    private int rFrame = 0;

    public DrawHeart() {
    }

    public void paint(Graphics g) {
        this.centerX = this.getWidth() / 2;
        this.centerY = this.getHeight() / 2;
        g.setColor(this.color);
        Heart heart = new Heart();
        this.draw(g, heart, this.rFrame);
        (new Thread(this)).start();
    }

    public void run() {
        try {
            Thread.sleep(160L);
            ++this.rFrame;
        } catch (InterruptedException var2) {
            throw new RuntimeException(var2);
        }

        this.repaint();
    }

    private void draw(Graphics g, Heart heart, int renderFrame) {
        heart.render(g, renderFrame);
    }

    private HeartPoint getHeartPoints(double t, double shrinkRatio) {
        if (shrinkRatio == 0.0) {
            shrinkRatio = 10.0;
        }

        double distX = 16.0 * Math.pow(Math.sin(t), 3.0);
        double distY = -(13.0 * Math.cos(t) - 5.0 * Math.cos(2.0 * t) - 2.0 * Math.cos(3.0 * t) - Math.cos(4.0 * t));
        distX *= shrinkRatio;
        distY *= shrinkRatio;
        distX += (double)this.centerX;
        distY += (double)this.centerY;
        return new HeartPoint((int)distX, (int)distY, 0);
    }

    private HeartPoint getScatterPoints(int x, int y, double beta) {
        x -= (int)(-beta * Math.log(Math.random()) * (double)(x - this.centerX));
        y -= (int)(-beta * Math.log(Math.random()) * (double)(y - this.centerY));
        return new HeartPoint(x, y, 0);
    }

    private HeartPoint getShirkPoints(int x, int y, double ratio) {
        double force = -1.0 / Math.pow(Math.pow((double)(x - this.centerX), 2.0) + Math.pow((double)(y - this.centerY), 2.0), 0.6);
        double dx = ratio * force * (double)(x - this.centerX);
        double dy = ratio * force * (double)(y - this.centerY);
        x = (int)((double)x - dx);
        y = (int)((double)y - dy);
        return new HeartPoint(x, y, 0);
    }

    private double getCurve(double p) {
        return 2.0 * 3.0 * Math.sin(4.0 * p) / 6.283185307179586;
    }

    private class Heart {
        private final List<HeartPoint> ohps = new ArrayList();
        private final List<HeartPoint> shps = new ArrayList();
        private final List<HeartPoint> chps = new ArrayList();
        private final List<List<HeartPoint>> alps = new ArrayList();
        private final int frame = 20;

        public Heart() {
            this.build(2000);

            for(int i = 0; i < 20; ++i) {
                this.getCalc(20);
            }

        }

        private void build(int number) {
            Random random = new Random();

            int i;
            for(i = 0; i < number; ++i) {
                double t = random.nextDouble() * 2.0 * Math.PI;
                this.ohps.add(DrawHeart.this.getHeartPoints(t, 0.0));
            }

            Iterator var6 = this.ohps.iterator();

            HeartPoint hp;
            while(var6.hasNext()) {
                hp = (HeartPoint)var6.next();

                for(int ix = 0; ix < 3; ++ix) {
                    this.shps.add(DrawHeart.this.getScatterPoints(hp.getX(), hp.getY(), 0.05));
                }
            }

            for(i = 0; i < 4000; ++i) {
                hp = (HeartPoint)this.ohps.get(random.nextInt(this.ohps.size()));
                this.chps.add(DrawHeart.this.getScatterPoints(hp.getX(), hp.getY(), 0.17));
            }

        }

        private HeartPoint getCalcPosition(int x, int y, double ratio) {
            Random random = new Random();
            double force = -1.0 / Math.pow(Math.pow((double)(x - DrawHeart.this.centerX), 2.0) + Math.pow((double)(y - DrawHeart.this.centerY), 2.0), 0.526);
            double dx = ratio * force * (double)(x - DrawHeart.this.centerX) + (double)random.nextInt(2) - 1.0;
            double dy = ratio * force * (double)(y - DrawHeart.this.centerY) + (double)random.nextInt(2) - 1.0;
            x = (int)((double)x - dx);
            y = (int)((double)y - dy);
            return new HeartPoint(x, y, 0);
        }

        private void getCalc(int xFrame) {
            List<HeartPoint> alpsB = new ArrayList();
            double ratio = 10.0 * DrawHeart.this.getCurve((double)xFrame * 1.0 / 10.0 * Math.PI);
            double haloRadius = 4.0 + 6.0 * (1.0 + DrawHeart.this.getCurve((double)xFrame * 1.0 * Math.PI));
            double haloNumber = 3000.0 + 4000.0 * Math.abs(Math.pow(DrawHeart.this.getCurve((double)xFrame * 1.0 * Math.PI), 2.0));
            List<HeartPoint> ahp = new ArrayList();
            Random random = new Random();

            HeartPoint hp;
            int size;
            for(int i = 0; (double)i < haloNumber; ++i) {
                hp = DrawHeart.this.getHeartPoints(random.nextDouble() * 2.0 * Math.PI, 11.6);
                hp = DrawHeart.this.getShirkPoints(hp.getX(), hp.getY(), haloRadius);
                if (!ahp.contains(hp)) {
                    ahp.add(hp);
                    int x = hp.getX() + random.nextInt(28) - 14;
                    size = hp.getY() + random.nextInt(28) - 14;
                    int[] a = new int[]{1, 2, 2};
                    int sizex = a[random.nextInt(3)];
                    alpsB.add(new HeartPoint(x, size, sizex));
                }
            }

            Iterator var17 = this.ohps.iterator();

            HeartPoint cp;
            while(var17.hasNext()) {
                hp = (HeartPoint)var17.next();
                cp = this.getCalcPosition(hp.getX(), hp.getY(), ratio);
                size = random.nextInt(3) + 1;
                alpsB.add(new HeartPoint(cp.getX(), cp.getY(), size));
            }

            var17 = this.shps.iterator();

            while(var17.hasNext()) {
                hp = (HeartPoint)var17.next();
                cp = this.getCalcPosition(hp.getX(), hp.getY(), ratio);
                size = random.nextInt(2) + 1;
                alpsB.add(new HeartPoint(cp.getX(), cp.getY(), size));
            }

            var17 = this.chps.iterator();

            while(var17.hasNext()) {
                hp = (HeartPoint)var17.next();
                cp = this.getCalcPosition(hp.getX(), hp.getY(), ratio);
                size = random.nextInt(2) + 1;
                alpsB.add(new HeartPoint(cp.getX(), cp.getY(), size));
            }

            this.alps.add(alpsB);
        }

        private void render(Graphics g, int renderFrame) {
            int index = renderFrame % 20;
            List<HeartPoint> hps = (List)this.alps.get(index);
            Iterator var5 = hps.iterator();

            while(var5.hasNext()) {
                HeartPoint hp = (HeartPoint)var5.next();
                g.fillOval(hp.getX(), hp.getY(), hp.getSize(), hp.getSize());
            }

        }
    }
}
