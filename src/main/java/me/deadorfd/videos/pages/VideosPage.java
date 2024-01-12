package me.deadorfd.videos.pages;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;

import javax.swing.border.EmptyBorder;

import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.pages
 * @Date 16.03.2023
 * @Time 13:27:30
 */
public class VideosPage extends JPanel {
	private JTextField textField;

	/**
	 * Create the panel.
	 */
	public VideosPage() {
		setBackground(new Color(128, 128, 128));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBounds(100, 100, 1280, 720);
		setLayout(null);

//		String MEDIA_URL = "file:/c:/Users/Alejandro/Downloads/oow2010-2.flv";
//		Media media = new Media(MEDIA_URL);
//		MediaPlayer player = new MediaPlayer(media);
//		MediaView view = new MediaView(player);
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g); // fixes the immediate problem.
		Graphics2D g2 = (Graphics2D) g;
		Line2D lin = new Line2D.Float(100, 100, 250, 260);
		g2.draw(lin);
	}
}