
/*
 * =======================================
 * File:             BombGame.java
 * Date Created:     09/14/2018 12:33:28 pm
 * Author:           Dakota Taylor
 * -                 -
 * Last Modified: Thu Sep 20 2018
 * Modified By: Dakota Taylor
 * =======================================
 */
import java.awt.Canvas;
import java.awt.Color;
import java.awt.image.BufferStrategy;
import java.awt.Graphics;
import java.lang.Runnable;
import java.lang.Thread;
import javax.swing.JFrame;

public class BombGame extends JFrame implements Runnable {

    private Canvas canvas = new Canvas();
    private RenderHandler renderer;

    public BombGame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 1000, 800);
        setLocationRelativeTo(null);
        add(canvas);
        setVisible(true);
        canvas.createBufferStrategy(3);

        renderer = new RenderHandler(getWidth(), getHeight());

    }

    public void update() {

    }

    public void render() {
        BufferStrategy bStrategy = canvas.getBufferStrategy();
        Graphics gfx = bStrategy.getDrawGraphics();
        super.paint(gfx);

        renderer.render(gfx);

        gfx.dispose();
        bStrategy.show();
    }

    public void run() {
        BufferStrategy bStrategy = canvas.getBufferStrategy();
        int i = 0;
        int x = 0;

        long lastTime = System.nanoTime();
        double nanoSecondConversion = 1000000000.0 / 60;
        double changeInSeconds = 0;

        while (true) {
            long now = System.nanoTime();

            changeInSeconds += (now = lastTime) / nanoSecondConversion;
            while (changeInSeconds >= 1) {
                update();
                changeInSeconds = 0;
            }

            render();
            lastTime = now;
        }
    }
}
