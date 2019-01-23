package bpaproject.framecontent;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bpaproject.characters.*;
import bpaproject.GameWindow;
import bpaproject.Tilemap;

/**
 * @file CharacterSelect.java
 * @author Dakota Taylor
 * @createdOn Tuesday, 15 January, 2019
 */

public class CharacterSelect extends WindowContent {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
    private static final long serialVersionUID = -2395465395541349602L;

    private static final ImageIcon[] CS_IMAGES = new ImageIcon[] {
            new ImageIcon(GameWindow.loadImage(new File("assets\\select\\character0.png"), GameWindow.ZOOM)),
            new ImageIcon(GameWindow.loadImage(new File("assets\\select\\character1.png"), GameWindow.ZOOM)),
            new ImageIcon(GameWindow.loadImage(new File("assets\\select\\character2.png"), GameWindow.ZOOM)),
            new ImageIcon(GameWindow.loadImage(new File("assets\\select\\character3.png"), GameWindow.ZOOM)), };

    JLabel[] csPortraits = new JLabel[4];
    Tilemap map;

    public CharacterSelect(GameWindow gw, Tilemap map) {
        super(gw);
        this.map = map;
        JPanel main = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        gbc.fill = GridBagConstraints.BOTH;
        gbc.gridy = 0;
        JButton[] next = new JButton[4];
        for (int i = 0; i < next.length; i++) {
            next[i] = new JButton("\u2191");
            next[i].putClientProperty("index", i);
            next[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nextPortrait((int) ((JButton) e.getSource()).getClientProperty("index"));
                }
            });

            gbc.gridx = i;
            main.add(next[i], gbc);
        }

        gbc.gridy = 1;
        for (int i = 0; i < csPortraits.length; i++) {
            csPortraits[i] = new JLabel(CS_IMAGES[0]);
            csPortraits[i].putClientProperty("portrait", 0);
            gbc.gridx = i;
            main.add(csPortraits[i], gbc);
        }

        gbc.gridy = 2;
        JButton[] prev = new JButton[4];
        for (int i = 0; i < prev.length; i++) {
            prev[i] = new JButton("\u2193");
            prev[i].putClientProperty("index", i);
            prev[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    prevPortrait((int) ((JButton) e.getSource()).getClientProperty("index"));
                }
            });

            gbc.gridx = i;
            main.add(prev[i], gbc);
        }

        gbc.gridy = 3;
        JButton play = new JButton("Play!");
        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startGame();
            }
        });
        main.add(play, gbc);
        this.add(main);
        gw.pack();
    }

    private void nextPortrait(int index) {
        if (index < 0 || index > 4)
            return;

        int current = (int) csPortraits[index].getClientProperty("portrait");
        int value = current + 1 > CS_IMAGES.length - 1 ? 0 : current + 1;

        csPortraits[index].putClientProperty("portrait", value);
        csPortraits[index].setIcon(CS_IMAGES[value]);
        LOGGER.log(Level.FINER, "Next image to " + value + " from button index #" + index);
    }

    private void prevPortrait(int index) {
        if (index < 0 || index > 4)
            return;

        int current = (int) csPortraits[index].getClientProperty("portrait");
        int value = current - 1 < 0 ? CS_IMAGES.length - 1 : current - 1;

        csPortraits[index].putClientProperty("portrait", value);
        csPortraits[index].setIcon(CS_IMAGES[value]);
        LOGGER.log(Level.FINER, "Previous image to " + value + " from button index #" + index);
    }

    private void startGame() {
        LOGGER.log(Level.FINE, "Creating game.");
        Game game = new Game(getGameWindow());
        game.setMap(map);

        for (int i = 0; i < csPortraits.length; i++) {
            int characterValue = (int) csPortraits[i].getClientProperty("portrait");
            Point p = map.getPlayerPosition(i);
            if (p == null)
                continue;

            Player character;
            switch (characterValue) {
            case 1:
                character = new CharacterA(16 * p.x, 16 * p.y, getGameWindow().getListener(i));
                break;
            case 2:
                character = new CharacterB(16 * p.x, 16 * p.y, getGameWindow().getListener(i));
                break;
            case 3:
                character = new CharacterC(16 * p.x, 16 * p.y, getGameWindow().getListener(i));
                break;
            default:
                character = null;
                break;
            }
            if (character != null)
                game.addPlayer(character);

        }
        getGameWindow().setSize(map.getWidth() * 16, map.getHeight() * 16);
        getGameWindow().getAudioPlayer().endLoop();
        LOGGER.log(Level.INFO, "Starting game.");
        getGameWindow().setWindowContent(game);

    }
}