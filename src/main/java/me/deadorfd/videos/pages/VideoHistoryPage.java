package me.deadorfd.videos.pages;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;

import me.deadorfd.videos.Main;
import me.deadorfd.videos.manager.WrapLayout;
import static me.deadorfd.videos.utils.Data.*;
import me.deadorfd.videos.utils.Utils;
import me.deadorfd.videos.utils.video.HistoryVideo;
import me.deadorfd.videos.utils.video.info.NormalVideoInfo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.pages
 * @Date 21.02.2023
 * @Time 13:43:53
 */
public class VideoHistoryPage extends JPanel {

	private static JPanel panel;

	/**
	 * Create the panel.
	 */
	public VideoHistoryPage() {
		setBackground(new Color(128, 128, 128));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBounds(100, 100, 1280, 720);
		setLayout(null);

		JLabel titel = new JLabel("History");
		titel.setFont(new Font(font, Font.PLAIN, 15));
		titel.setHorizontalAlignment(SwingConstants.CENTER);
		titel.setBounds(219, 11, 120, 14);
		add(titel);

		JButton btnMainButton = new JButton("Menü");
		btnMainButton.addActionListener(e -> Main.switchScenes("main"));
		btnMainButton.setFont(new Font(font, Font.PLAIN, 15));
		btnMainButton.setBounds(20, 642, 120, 23);
		add(btnMainButton);

		JScrollPane scrollPane_Videos = new JScrollPane();
		scrollPane_Videos.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane_Videos.setBounds(20, 53, 238, 539);
		add(scrollPane_Videos);

		panel_Videos = new JPanel();
		panel_Videos.setLayout(new WrapLayout());
		panel_Videos.setSize(new Dimension(300, 1));
		scrollPane_Videos.setViewportView(panel_Videos);
		for (HistoryVideo video : history) panel_Videos.add(getVideoButton(video));

		scrollPane_VideoInfo = new JScrollPane();
		scrollPane_VideoInfo.setBounds(401, 53, 743, 578);
		scrollPane_VideoInfo.setVisible(false);
		add(scrollPane_VideoInfo);

		panel_VideoInfo = new JPanel();
		panel_VideoInfo.setBackground(Color.DARK_GRAY);
		panel_VideoInfo.setLayout(null);
		panel_VideoInfo.setSize(new Dimension(300, 1));
		scrollPane_VideoInfo.setViewportView(panel_VideoInfo);

		lblVideoTitel = new JLabel("Video:");
		lblVideoTitel.setForeground(Color.WHITE);
		lblVideoTitel.setHorizontalAlignment(SwingConstants.CENTER);
		lblVideoTitel.setFont(new Font("Tahoma", Font.BOLD, 15));
		lblVideoTitel.setBounds(10, 27, 721, 33);
		panel_VideoInfo.add(lblVideoTitel);

		panel_VideoImage = new JPanel();
		panel_VideoImage.setBounds(152, 68, 436, 231);
		panel_VideoInfo.add(panel_VideoImage);

		btnPlay = new JButton("Video Abspielen");
		btnPlay.setFont(new Font("Tahoma", Font.PLAIN, 14));
		btnPlay.setBounds(242, 516, 247, 33);
		panel_VideoInfo.add(btnPlay);

		btnFav = new JButton("Favorisieren");
		btnFav.setBounds(625, 542, 106, 23);
		panel_VideoInfo.add(btnFav);

		textArea_VideoInfo = new JTextArea();
		textArea_VideoInfo.setFont(new Font("Monospaced", Font.BOLD, 16));
		textArea_VideoInfo.setEditable(false);
		textArea_VideoInfo.setLineWrap(true);
		textArea_VideoInfo.setBounds(152, 349, 436, 156);
		panel_VideoInfo.add(textArea_VideoInfo);

		lblVideoInfo = new JLabel("Video Infos:");
		lblVideoInfo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblVideoInfo.setForeground(Color.WHITE);
		lblVideoInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblVideoInfo.setBounds(242, 299, 247, 39);
		panel_VideoInfo.add(lblVideoInfo);
	}

	public static void updatePanel() {
		panel_Videos.removeAll();
		panel_Videos.updateUI();
		for (HistoryVideo video : history) panel_Videos.add(getVideoButton(video));
	}

	private static JPanel panel_Videos;

	private static JScrollPane scrollPane_VideoInfo;
	private static JPanel panel_VideoInfo;
	private static JPanel panel_VideoImage;
	private static JLabel lblVideoTitel;
	private static JButton btnPlay;
	private static JButton btnFav;
	private static JTextArea textArea_VideoInfo;
	private static JLabel lblVideoInfo;
	private static String lastImg;

	private static void openVideoInfo(HistoryVideo video) {
		if (video.isFavorite())
			btnFav.setText("Favorisiert");
		else
			btnFav.setText("Favorisieren");
		scrollPane_VideoInfo.setVisible(true);
		lblVideoTitel.setText(video.getName());
		panel_VideoImage.removeAll();
		for (ActionListener listener : btnPlay.getActionListeners())
			btnPlay.removeActionListener(listener);
		btnPlay.addActionListener(e -> video.play());
		for (ActionListener listener : btnFav.getActionListeners())
			btnFav.removeActionListener(listener);
		btnFav.addActionListener(e -> {
			if (video.isFavorite()) {
				video.removeFromFavorites();
				btnFav.setText("Favorisieren");
			} else {
				video.addToFavorites();
				btnFav.setText("Favorisiert");
			}
		});

		textArea_VideoInfo.setText(" \n \n\nLoading...\n \n ");

		// Infos
		new Thread(() -> {
			NormalVideoInfo info = (NormalVideoInfo) video.getVideoInfo();
			lastImg = info.getFrame();
			Utils.openImageFrameVideo(lastImg, panel_VideoImage);
			textArea_VideoInfo.setText(info.getDuration() + "\nErstellt: "
					+ video.createdDate()
					+ "\nGröße: "
					+ video.getFileSizeAsString()
					+ "\nAuflösung: "
					+ info.getWidth()
					+ " x "
					+ info.getHeight()
					+ "\nFormat: "
					+ video.getFormat()
					+ "\nFPS: "
					+ info.getFrameRate()
					+ "\nZuletzt gesehen: "
					+ video.getHistory().getTimeInString());
			File file = new File(lastImg);
			file.delete();
		}).start();
	}

	private static JButton getVideoButton(HistoryVideo video) {
		JButton button = new JButton(video.getName());
		button.setFont(new Font(font, Font.PLAIN, 11));
		button.setPreferredSize(new Dimension(200, 25));
		button.setOpaque(true);
		button.addActionListener(event -> openVideoInfo(video));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON3) video.getVideoInfo().openImage();
			}
		});
		return button;
	}
}