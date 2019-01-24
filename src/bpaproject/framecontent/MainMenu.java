package bpaproject.framecontent;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.WindowEvent;

import javax.swing.JButton;

import bpaproject.GameWindow;

/**
 * @file MainMenu.java
 * @author Dakota Taylor
 * @createdOn Monday, 07 January, 2019
 */

/**
 * A {@code MainMenu} allows the player to view credits or change settings
 * before playing or exiting the game.
 */
public class MainMenu extends FrameContent {
    private static final long serialVersionUID = -7939301824932848451L;
    private static final File BGM_FILE = new File("assets\\audio\\MenuSelc00.wav");

    public MainMenu(GameWindow gw) {
        super(gw);
        gw.getAudioPlayer().setAudio(BGM_FILE);
        gw.getAudioPlayer().loopAudio();

        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 0, GridBagConstraints.CENTER,
                GridBagConstraints.NONE,
                new Insets(2 * GameWindow.ZOOM, 5 * GameWindow.ZOOM, 2 * GameWindow.ZOOM, 5 * GameWindow.ZOOM), 0, 0);

        JButton play = new JButton("Play!");

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapSelect ms = new MapSelect(gw);
                gw.setFrameContent(ms);
            }
        });
        play.setPreferredSize(new Dimension(150 * GameWindow.ZOOM, 60 * GameWindow.ZOOM));
        this.add(play, gbc);

        gbc.gridy++;
        JButton options = new JButton("Options");
        options.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                OptionsMenu op = new OptionsMenu(gw);
                gw.setFrameContent(op);
            }
        });
        options.setPreferredSize(new Dimension(150 * GameWindow.ZOOM, 25 * GameWindow.ZOOM));
        this.add(options, gbc);

        gbc.gridy++;
        JButton credits = new JButton("Credits");
        credits.setPreferredSize(new Dimension(150 * GameWindow.ZOOM, 25 * GameWindow.ZOOM));
        this.add(credits, gbc);

        gbc.gridy++;
        JButton exit = new JButton("Exit");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                gw.dispatchEvent(new WindowEvent(gw, WindowEvent.WINDOW_CLOSING));
            }
        });
        exit.setPreferredSize(new Dimension(150 * GameWindow.ZOOM, 25 * GameWindow.ZOOM));
        this.add(exit, gbc);

        gw.pack();
    }

}