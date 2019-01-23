package bpaproject.framecontent;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.ImageIcon;
import javax.swing.JButton;

import bpaproject.*;

/**
 * @file MapSelect.java
 * @author Dakota Taylor
 * @createdOn Saturday, 19 January, 2019
 */

public class MapSelect extends FrameContent {
    private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
    private static final long serialVersionUID = 4049890020257627138L;

    private static final ImageIcon[] MS_IMAGES = new ImageIcon[] {
            new ImageIcon(GameWindow.loadImage(new File("assets\\select\\map0.png"), GameWindow.ZOOM)),
            new ImageIcon(GameWindow.loadImage(new File("assets\\select\\map1.png"), GameWindow.ZOOM)),
            new ImageIcon(GameWindow.loadImage(new File("assets\\select\\map2.png"), GameWindow.ZOOM)),
            new ImageIcon(GameWindow.loadImage(new File("assets\\select\\map3.png"), GameWindow.ZOOM)) };

    public MapSelect(GameWindow gw) {
        super(gw);

        JButton[] mapButtons = new JButton[MS_IMAGES.length];
        for (int i = 0; i < mapButtons.length; i++) {
            mapButtons[i] = new JButton(MS_IMAGES[i]);
            mapButtons[i].putClientProperty("index", i);
            mapButtons[i].addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    selectMap((int) ((JButton) e.getSource()).getClientProperty("index"));
                }
            });
            this.add(mapButtons[i]);
            gw.pack();
        }

    }

    private void selectMap(int index) {
        LOGGER.log(Level.INFO, "Selected map #" + index);
        Tilemap map;
        switch (index) {
        case 0: {
            BufferedImage image = GameWindow.loadImage(new File("assets\\tilesets\\ForestTileset.png"));
            SpriteSheet sheet = new SpriteSheet(image, 16, 16);
            Tileset tiles = new Tileset(new File("assets\\maps\\DefaultTileset.bt"), sheet);
            map = new Tilemap(new File("assets\\maps\\DefaultMap.bm"), tiles);
        }
            break;
        case 1: {
            BufferedImage image = GameWindow.loadImage(new File("assets\\tilesets\\RuinsTileset.png"));
            SpriteSheet sheet = new SpriteSheet(image, 16, 16);
            Tileset tiles = new Tileset(new File("assets\\maps\\DefaultTileset.bt"), sheet);
            map = new Tilemap(new File("assets\\maps\\RuinsMap.bm"), tiles);
        }
            break;
        case 2: {
            BufferedImage image = GameWindow.loadImage(new File("assets\\tilesets\\FireTileset.png"));
            SpriteSheet sheet = new SpriteSheet(image, 16, 16);
            Tileset tiles = new Tileset(new File("assets\\maps\\DefaultTileset.bt"), sheet);
            map = new Tilemap(new File("assets\\maps\\FireMap.bm"), tiles);
        }
            break;
        case 3: {
            BufferedImage image = GameWindow.loadImage(new File("assets\\tilesets\\SnowTileset.png"));
            SpriteSheet sheet = new SpriteSheet(image, 16, 16);
            Tileset tiles = new Tileset(new File("assets\\maps\\DefaultTileset.bt"), sheet);
            map = new Tilemap(new File("assets\\maps\\SnowMap.bm"), tiles);
        }
            break;
        default:
            LOGGER.log(Level.WARNING, "Selected an unavalible map!");
            map = null;
            break;
        }

        if (map != null) {
            if (map.isValid()) {
                CharacterSelect cs = new CharacterSelect(getGameWindow(), map);
                getGameWindow().setWindowContent(cs);
                getGameWindow().getAudioPlayer().setAudio(new File("assets\\audio\\MapSelc3D.wav"));
            } else {
                LOGGER.log(Level.WARNING, "Tilemap is not a valid map!");
            }
        }
    }
}