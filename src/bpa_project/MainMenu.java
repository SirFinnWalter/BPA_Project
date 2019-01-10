package bpa_project;

import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

/**
 * @file MainMenu.java
 * @author Dakota Taylor
 * @createdOn Monday, 07 January, 2019
 */

public class MainMenu extends WindowContent {

    public MainMenu(GameWindow gw) {
        super(gw);
        this.setPreferredSize(new Dimension(400 * GameWindow.ZOOM, 300 * GameWindow.ZOOM));

        JButton button = new JButton("Play!");
        button.setPreferredSize(new Dimension(200 * GameWindow.ZOOM, 100 * GameWindow.ZOOM));
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                BufferedImage image = GameWindow.loadImage(new File("assets\\tilesets\\RuinsTileset.png"));
                SpriteSheet sheet = new SpriteSheet(image, 16, 16);
                Tileset tiles = new Tileset(new File("assets\\maps\\DefaultTileset.bt"), sheet);
                Tilemap map = new Tilemap(new File("assets\\maps\\DefaultMap.bm"), tiles);
                gw.setSize(map.getWidth() * 16, map.getHeight() * 16);

                Game game = new Game(gw, gw.getRenderHandler());
                game.setMap(map);
                gw.setWindowContent(game);
            }
        });
        this.setVisible(true);
        this.add(button);
    }

    @Override
    public void init() {
    }

    @Override
    public void update() {

    }

    @Override
    public void render() {

    }

}