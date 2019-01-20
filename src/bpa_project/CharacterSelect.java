package bpa_project;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import bpa_project.characters.*;

/**
 * @file CharacterSelect.java
 * @author Dakota Taylor
 * @createdOn Tuesday, 15 January, 2019
 */

public class CharacterSelect extends WindowContent {
        JLabel[] csPortraits = new JLabel[4];
        ArrayList<ImageIcon> csImages = new ArrayList<ImageIcon>();

        public CharacterSelect(GameWindow gw) {
                super(gw);
                gw.setPreferredSize(new Dimension(400 * GameWindow.ZOOM, 240 * GameWindow.ZOOM));
                this.setPreferredSize(new Dimension(400 * GameWindow.ZOOM, 240 * GameWindow.ZOOM));
                gw.setSize(400, 240);

                JPanel centerPanel = new JPanel();

                Image image0 = GameWindow.loadImage(new File("assets\\sprites\\csCharacter0.png"));
                image0 = image0.getScaledInstance(image0.getWidth(this) * GameWindow.ZOOM,
                                image0.getHeight(this) * GameWindow.ZOOM, Image.SCALE_SMOOTH);
                ImageIcon icon0 = new ImageIcon(image0);

                Image image1 = GameWindow.loadImage(new File("assets\\sprites\\csCharacter1.png"));
                image1 = image1.getScaledInstance(image1.getWidth(this) * GameWindow.ZOOM,
                                image1.getHeight(this) * GameWindow.ZOOM, Image.SCALE_SMOOTH);
                ImageIcon icon1 = new ImageIcon(image1);
                Image image2 = GameWindow.loadImage(new File("assets\\sprites\\csCharacter2.png"));
                image2 = image2.getScaledInstance(image2.getWidth(this) * GameWindow.ZOOM,
                                image2.getHeight(this) * GameWindow.ZOOM, Image.SCALE_SMOOTH);
                ImageIcon icon2 = new ImageIcon(image2);
                csImages.add(icon0);
                csImages.add(icon1);
                csImages.add(icon2);
                for (int i = 0; i < csPortraits.length; i++) {
                        csPortraits[i] = new JLabel(csImages.get(0));
                        csPortraits[i].putClientProperty("portrait", 0);
                        centerPanel.add(csPortraits[i], BorderLayout.CENTER);
                }

                JPanel topPanel = new JPanel();
                JButton[] nextButtons = new JButton[4];
                for (int i = 0; i < nextButtons.length; i++) {
                        nextButtons[i] = new JButton("\u2191");
                        nextButtons[i].putClientProperty("index", i);
                        nextButtons[i].setPreferredSize(
                                        new Dimension((this.getPreferredSize().width / 4) - 5 * GameWindow.ZOOM,
                                                        10 * GameWindow.ZOOM));

                        nextButtons[i].addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                        nextPortrait((int) ((JButton) e.getSource()).getClientProperty("index"));
                                }
                        });
                        topPanel.add(nextButtons[i], BorderLayout.NORTH);
                }

                JPanel bottomPanel = new JPanel();
                JButton[] prevButtons = new JButton[4];
                for (int i = 0; i < prevButtons.length; i++) {
                        prevButtons[i] = new JButton("\u2193");
                        prevButtons[i].putClientProperty("index", i);
                        prevButtons[i].setPreferredSize(
                                        new Dimension((this.getPreferredSize().width / 4) - 5 * GameWindow.ZOOM,
                                                        10 * GameWindow.ZOOM));
                        prevButtons[i].addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                        prevPortrait((int) ((JButton) e.getSource()).getClientProperty("index"));
                                }
                        });
                        bottomPanel.add(prevButtons[i], BorderLayout.SOUTH);
                }

                JPanel playPanel = new JPanel();
                JButton playButton = new JButton("Play!");
                playButton.setPreferredSize(new Dimension(150 * GameWindow.ZOOM, 25 * GameWindow.ZOOM));
                playButton.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {

                                BufferedImage image = GameWindow
                                                .loadImage(new File("assets\\tilesets\\FireTileset.png"));
                                SpriteSheet sheet = new SpriteSheet(image, 16, 16);
                                Tileset tiles = new Tileset(new File("assets\\maps\\DefaultTileset.bt"), sheet);
                                Tilemap map = new Tilemap(new File("assets\\maps\\FireMap.bm"), tiles);
                                gw.setSize(map.getWidth() * 16, map.getHeight() * 16);

                                Game game = new Game(gw, gw.getRenderHandler());
                                game.setMap(map);

                                try {
                                        for (int i = 0; i < csPortraits.length; i++) {
                                                int characterValue = (int) csPortraits[i].getClientProperty("portrait");
                                                Point p = map.getPlayerPosition(i);
                                                switch (characterValue) {
                                                case 1:
                                                        game.addPlayer(new CharacterA(16 * p.x, 16 * p.y,
                                                                        gw.getListener(i)));
                                                        break;
                                                case 2:
                                                        game.addPlayer(new CharacterB(16 * p.x, 16 * p.y,
                                                                        gw.getListener(i)));

                                                        break;
                                                case 3:
                                                        game.addPlayer(new CharacterC(16 * p.x, 16 * p.y,
                                                                        gw.getListener(i)));
                                                default:
                                                        break;
                                                }
                                        }

                                } catch (CloneNotSupportedException ex) {
                                        // TODO: handle exception
                                }

                                gw.setWindowContent(game);
                        }
                });
                playPanel.add(playButton);

                this.add(topPanel);
                this.add(centerPanel);
                this.add(bottomPanel);
                this.add(playPanel);
                this.setVisible(true);
        }

        private void nextPortrait(int index) {
                if (index < 0 || index > 4)
                        return;

                int current = (int) csPortraits[index].getClientProperty("portrait");
                int value = current + 1 > csImages.size() - 1 ? 0 : current + 1;

                csPortraits[index].putClientProperty("portrait", value);
                csPortraits[index].setIcon(csImages.get(value));
        }

        private void prevPortrait(int index) {
                if (index < 0 || index > 4)
                        return;

                int current = (int) csPortraits[index].getClientProperty("portrait");
                int value = current - 1 < 0 ? csImages.size() - 1 : current - 1;

                csPortraits[index].putClientProperty("portrait", value);
                csPortraits[index].setIcon(csImages.get(value));
        }
}