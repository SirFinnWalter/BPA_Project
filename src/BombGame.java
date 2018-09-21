
/*
 @file BombGame.java
 @author Dakota Taylor
 @created on Friday, 14 September, 2018
*/

import java.awt.Canvas;
// import java.awt.Color;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

import java.lang.Runnable;
import java.lang.Thread;

import javax.swing.JFrame;

import javax.imageio.ImageIO;

import java.io.IOException;

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

        BufferedImage pikachu = loadImage("pikachu.png");
    }

    public void update() {

    }

    // TODO 38:00
    private BufferedImage loadImage(String path) {
        try {
            BufferedImage loadedImage = ImageIO.read(BombGame.class.getResource(path));
            BufferedImage formattedImage = new BufferImage(loadImage.getWidth(), loadImage.getHeight(), BufferedImage.TYPE_INT_RGB);
            return loadedImage;
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
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

    public static void main(String[] args) {
        BombGame game = new BombGame();
        Thread gameThread = new Thread(game);
        gameThread.start();
    }
}
