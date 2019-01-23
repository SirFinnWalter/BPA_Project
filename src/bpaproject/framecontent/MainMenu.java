package bpaproject.framecontent;

import java.awt.event.ActionListener;
import java.io.File;
import java.awt.Dimension;
import java.awt.event.ActionEvent;

import javax.swing.JButton;

import bpaproject.GameWindow;

/**
 * @file MainMenu.java
 * @author Dakota Taylor
 * @createdOn Monday, 07 January, 2019
 */

public class MainMenu extends FrameContent {

    public MainMenu(GameWindow gw) {
        super(gw);

        JButton play = new JButton("Play!");

        play.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MapSelect ms = new MapSelect(gw);
                gw.setWindowContent(ms);
                gw.getAudioPlayer().setAudio(new File("assets\\audio\\MapSelc2D.wav"));
            }
        });
        play.setPreferredSize(new Dimension(150 * GameWindow.ZOOM, 60 * GameWindow.ZOOM));

        this.add(play);
        // gw.setContentPane(this);
        gw.getAudioPlayer().setAudio(new File("assets\\audio\\MapSelc1D.wav"));
        gw.getAudioPlayer().loopAudio();
        gw.pack();
        gw.setVisible(true);
        // this.add(button);
    }

}