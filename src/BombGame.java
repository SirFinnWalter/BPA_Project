
/*
 @file BombGame.java
 @author Dakota Taylor
 @created on Friday, 14 September, 2018
*/

// TODO: 46:15

import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.awt.Graphics;
import java.awt.Canvas;

import java.lang.Runnable;
import java.lang.Thread;

import javax.swing.JFrame;

import javax.imageio.ImageIO;

import java.io.File;
import java.io.IOException;

public class BombGame extends JFrame implements Runnable {

    private Canvas canvas = new Canvas();
    private RenderHandler renderer;
    private BufferedImage testImage;

    /**
     * Creates a default window
     */
    public BombGame() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setBounds(0, 0, 1000, 800);
        setLocationRelativeTo(null);

        add(canvas);
        setVisible(true);

        canvas.createBufferStrategy(3);

        renderer = new RenderHandler(getWidth(), getHeight());

        testImage = loadImage(new File("").getAbsolutePath().concat("\\assets\\sprites\\pikachu.png"));
    }

    public void update() {

    }

    /**
     * 
     * 
     * @param filepath Path to the image
     * @return The formatted image data
     */
    private BufferedImage loadImage(String filepath) {
        try {
            BufferedImage loadedImage = ImageIO.read(new File(filepath));
            BufferedImage formattedImage = new BufferedImage(loadedImage.getWidth(), loadedImage.getHeight(),
                    BufferedImage.TYPE_INT_RGB);
            formattedImage.getGraphics().drawImage(loadedImage, 0, 0, null);

            return formattedImage;
        } catch (IOException e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 
     */
    public void render() {
        BufferStrategy bStrategy = canvas.getBufferStrategy();

        Graphics gfx = bStrategy.getDrawGraphics();

        super.paint(gfx);

        renderer.renderImage(testImage, 0, 0, 1, 1);
        renderer.render(gfx);

        gfx.dispose();
        bStrategy.show();
    }

    /**
     * 
     */
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
