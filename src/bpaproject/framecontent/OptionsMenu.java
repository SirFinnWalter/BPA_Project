package bpaproject.framecontent;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import bpaproject.GameWindow;
import bpaproject.Options;

/**
 * @file Options.java
 * @author Dakota Taylor
 * @createdOn Wednesday, 23 January, 2019
 */

/**
 * A {@code OptionMenu} displays and allows changes to a few settings. Settings
 * changed are remember throughout the session, but none are save to the config
 * file until Options.writeToFile() is called.
 */
public class OptionsMenu extends FrameContent {
    private static final long serialVersionUID = 2282810072396078159L;

    private static int volume = Math.round(Float.parseFloat(Options.getValue(Options.Setting.VOLUME)));
    private static int scale = Integer.parseInt(Options.getValue(Options.Setting.SCALE));

    public OptionsMenu(GameWindow gw) {
        super(gw);

        JPanel main = new JPanel();
        main.setLayout(new BoxLayout(main, BoxLayout.Y_AXIS));

        // VOLUME GROUP
        JLabel volumeTitle = new JLabel("Volume - " + volume + "%", JLabel.CENTER);
        volumeTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(volumeTitle);

        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, volume);
        volumeSlider.setMajorTickSpacing(20);
        volumeSlider.setMinorTickSpacing(10);
        volumeSlider.setPaintTicks(true);
        volumeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JSlider && !((JSlider) e.getSource()).getValueIsAdjusting()) {
                    JSlider slider = (JSlider) e.getSource();
                    volumeTitle.setText("Volume - " + slider.getValue() + "%");
                    if (slider.getValue() != gw.getAudioPlayer().getVolume())
                        gw.getAudioPlayer().setVolume(slider.getValue());

                    volume = gw.getAudioPlayer().getVolume();
                    Options.write(Options.Setting.VOLUME, Integer.toString(slider.getValue()));
                }
            }
        });
        main.add(volumeSlider);
        //

        // SCALE GROUP
        JLabel scaleTitle = new JLabel("Scale - " + scale, JLabel.CENTER);
        scaleTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        main.add(scaleTitle);
        JLabel scaleWarning = new JLabel("Requires restart", JLabel.CENTER);
        scaleWarning.setAlignmentX(Component.CENTER_ALIGNMENT);
        scaleWarning.setFont(new Font(scaleWarning.getFont().getFontName(), Font.PLAIN, 10));
        scaleWarning.setVisible(scale != GameWindow.ZOOM);
        main.add(scaleWarning);

        JSlider scaleSlider = new JSlider(JSlider.HORIZONTAL, 1, 8, scale);
        scaleSlider.setMajorTickSpacing(1);
        scaleSlider.setMinorTickSpacing(1);
        scaleSlider.setPaintTicks(true);
        scaleSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (e.getSource() instanceof JSlider && !((JSlider) e.getSource()).getValueIsAdjusting()) {
                    JSlider slider = (JSlider) e.getSource();

                    scaleTitle.setText("Scale - " + slider.getValue());
                    if (slider.getValue() != GameWindow.ZOOM)
                        scaleWarning.setVisible(true);
                    else
                        scaleWarning.setVisible(false);

                    scale = slider.getValue();
                    Options.write(Options.Setting.SCALE, Integer.toString(slider.getValue()));
                    gw.pack();
                }
            }
        });
        main.add(scaleSlider);
        //

        JButton back = new JButton("Back");
        back.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                MainMenu mm = new MainMenu(gw);
                gw.setFrameContent(mm);
            }
        });
        main.add(back);

        this.add(main);
        gw.pack();

    }
}