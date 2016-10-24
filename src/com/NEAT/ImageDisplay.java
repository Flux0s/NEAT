package com.NEAT;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ImageDisplay extends JFrame {

	public ImageDisplay(Rectangle screen) {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setResizable(true);
		getContentPane().setLayout(new FlowLayout());
		Robot scan = null;
		try {
			scan = new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
		BufferedImage capture = scan.createScreenCapture(screen);
		getContentPane().add(new JLabel(new ImageIcon(capture)));
		pack();
		setVisible(true);
	}

	public ImageDisplay(ArrayList<Rectangle> screenSections) {
		setDefaultCloseOperation(WindowConstants.HIDE_ON_CLOSE);
		setResizable(true);
		getContentPane().setLayout(new FlowLayout());
		for (int i = 0; i < screenSections.size(); i++) {
			Robot scan = null;
			try {
				scan = new Robot();
			} catch (AWTException e) {
				e.printStackTrace();
			}
			BufferedImage capture = scan.createScreenCapture(screenSections.get(i));
			getContentPane().add(new JLabel(new ImageIcon(capture)));
		}
		pack();
		setVisible(true);

	}
}
