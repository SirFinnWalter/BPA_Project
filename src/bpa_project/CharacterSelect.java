package bpa_project;

import java.awt.BorderLayout;
import java.awt.Canvas;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Graphics;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.imageio.ImageIO;

/**
 * @file CharacterSelect.java
 * @author Dakota Taylor
 * @createdOn Tuesday, 15 January, 2019
 */

public class CharacterSelect extends WindowContent {

    public CharacterSelect(GameWindow gw) {
        super(gw);

        // Graphics gfx = this.getGraphics();
        // gfx.drawImage(charA, 200, 200, 400, 400, null);
        // gw.getRenderHandler().render(gfx);
        // gfx.dispose();
        // gw.getRenderHandler().renderImage(charA, 200, 200, GameWindow.ZOOM,
        // GameWindow.ZOOM);
        gw.setPreferredSize(new Dimension(400 * GameWindow.ZOOM, 200 * GameWindow.ZOOM));
        this.setPreferredSize(new Dimension(400 * GameWindow.ZOOM, 200 * GameWindow.ZOOM));
        gw.setSize(400, 200);
        JButton upButton1 = new JButton("^");
        JButton upButton3 = new JButton("^");
        JButton upButton4 = new JButton("^");
        JButton upButton2 = new JButton("^");
        upButton1.setPreferredSize(
                new Dimension((this.getPreferredSize().width / 4) - 5 * GameWindow.ZOOM, 10 * GameWindow.ZOOM));
        upButton2.setPreferredSize(
                new Dimension((this.getPreferredSize().width / 4) - 5 * GameWindow.ZOOM, 10 * GameWindow.ZOOM));
        upButton3.setPreferredSize(
                new Dimension((this.getPreferredSize().width / 4) - 5 * GameWindow.ZOOM, 10 * GameWindow.ZOOM));
        upButton4.setPreferredSize(
                new Dimension((this.getPreferredSize().width / 4) - 5 * GameWindow.ZOOM, 10 * GameWindow.ZOOM));

        JButton downButton1 = new JButton("v");
        JButton downButton2 = new JButton("v");
        JButton downButton3 = new JButton("v");
        JButton downButton4 = new JButton("v");
        downButton1.setPreferredSize(
                new Dimension((this.getPreferredSize().width / 4) - 5 * GameWindow.ZOOM, 10 * GameWindow.ZOOM));
        downButton2.setPreferredSize(
                new Dimension((this.getPreferredSize().width / 4) - 5 * GameWindow.ZOOM, 10 * GameWindow.ZOOM));
        downButton3.setPreferredSize(
                new Dimension((this.getPreferredSize().width / 4) - 5 * GameWindow.ZOOM, 10 * GameWindow.ZOOM));
        downButton4.setPreferredSize(
                new Dimension((this.getPreferredSize().width / 4) - 5 * GameWindow.ZOOM, 10 * GameWindow.ZOOM));

        this.setLayout(new BorderLayout());

        JPanel topPanel = new JPanel();
        topPanel.add(upButton1);
        topPanel.add(upButton2);
        topPanel.add(upButton3);
        topPanel.add(upButton4);
        this.add(topPanel, BorderLayout.NORTH);

        JPanel centerPanel = new JPanel();

        BufferedImage charImage = GameWindow.loadImage(new File("assets\\sprites\\csCharacter1.png"));
        JLabel character1 = new JLabel(new ImageIcon(charImage));
        JLabel character2 = new JLabel(new ImageIcon(charImage));
        JLabel character3 = new JLabel(new ImageIcon(charImage));
        JLabel character4 = new JLabel(new ImageIcon(charImage));
        centerPanel.add(character1);
        centerPanel.add(character2);
        centerPanel.add(character3);
        centerPanel.add(character4);

        this.add(centerPanel);
        JPanel bottomPanel = new JPanel();

        bottomPanel.add(downButton1, BorderLayout.SOUTH);
        bottomPanel.add(downButton2, BorderLayout.SOUTH);
        bottomPanel.add(downButton3, BorderLayout.SOUTH);
        bottomPanel.add(downButton4, BorderLayout.SOUTH);
        this.add(bottomPanel, BorderLayout.SOUTH);
        this.setVisible(true);
        // canvas.setPreferredSize(new Dimension(400 * GameWindow.ZOOM, 300 *
        // GameWindow.ZOOM));
    }

    @Override
    public void init() {
        super.init();

        // canvas.createBufferStrategy(3);
        // canvas.requestFocus();
    }

    @Override
    public void render() {

        // try {
        // BufferStrategy bStrategy = canvas.getBufferStrategy();
        // Graphics gfx = bStrategy.getDrawGraphics();
        // // super.paint(gfx);
        // renderer.renderImage(charA, 100, 100, GameWindow.ZOOM, GameWindow.ZOOM);
        // renderer.render(gfx);
        // gfx.dispose();
        // bStrategy.show();
        // renderer.clear(0xFF0000FF);
        // } catch (IllegalStateException e) {
        // JOptionPane.showMessageDialog(null, "Game preformed an illegal
        // operation.\nClosing...", "Uh oh!",
        // JOptionPane.WARNING_MESSAGE);
        // System.exit(0);
        // }
    }
}