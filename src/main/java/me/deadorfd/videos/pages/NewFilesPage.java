package me.deadorfd.videos.pages;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import me.deadorfd.videos.Main;
import me.deadorfd.videos.manager.WrapLayout;
import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.Utils;
import me.deadorfd.videos.utils.video.NormalVideo;

import javax.swing.JLabel;
import java.awt.Font;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JCheckBox;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.pages
 * @Date 20.12.2022
 * @Time 23:18:08
 */
public class NewFilesPage extends JPanel {

	/**
	 * Create the panel.
	 */

	public static ArrayList<JButton> buttons = new ArrayList<>();
	private static JPanel panel;

	public NewFilesPage() {
		setBackground(new Color(128, 128, 128));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBounds(100, 100, 1280, 720);
		setLayout(null);

		JLabel lblTitel = new JLabel("Neue Videos");
		lblTitel.setHorizontalAlignment(SwingConstants.CENTER);
		lblTitel.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 15));
		lblTitel.setBounds(177, 11, 190, 14);
		add(lblTitel);

		JButton btnMenu = new JButton("Menü");
		btnMenu.addActionListener(e -> Main.switchScenes("main"));
		btnMenu.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 15));
		btnMenu.setBounds(89, 425, 190, 23);
		add(btnMenu);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setBounds(10, 70, 424, 311);
		add(scrollPane);

		panel = new JPanel();
		panel.setLayout(new WrapLayout());
		panel.setSize(new Dimension(300, 1));
		scrollPane.setViewportView(panel);

		JCheckBox chckbxBack30Days = new JCheckBox("Nach 30 Tagen");

		JCheckBox chckbxBack10Days = new JCheckBox("Nach 10 Tagen");
		chckbxBack10Days.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 11));
		chckbxBack10Days.setBounds(57, 40, 119, 23);
		chckbxBack10Days.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				chckbxBack30Days.setSelected(false);
				ArrayList<JButton> removeList = new ArrayList<>();
				for (JButton button : buttons) {
					panel.remove(button);
					removeList.add(button);
				}
				buttons.removeAll(removeList);
				initVideos(10);
				for (File file : Data.createdInLastThirtyDays)
					panel.add(videoButton(new NormalVideo(file)));
				panel.updateUI();
			}
		});
		add(chckbxBack10Days);

		chckbxBack30Days.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 11));
		chckbxBack30Days.setBounds(213, 40, 119, 23);
		chckbxBack30Days.addItemListener(e -> {
			if (e.getStateChange() == ItemEvent.SELECTED) {
				chckbxBack10Days.setSelected(false);
				ArrayList<JButton> removeList = new ArrayList<>();
				for (JButton button : buttons) {
					panel.remove(button);
					removeList.add(button);
				}
				buttons.removeAll(removeList);
				initVideos(30);
				for (File file : Data.createdInLastThirtyDays)
					panel.add(videoButton(new NormalVideo(file)));
				panel.updateUI();
			}
		});
		add(chckbxBack30Days);

		scrollPane_VideoInfo = new JScrollPane();
		scrollPane_VideoInfo.setBounds(481, 11, 743, 578);
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
		textArea_VideoInfo.setEditable(false);
		textArea_VideoInfo.setLineWrap(true);
		textArea_VideoInfo.setBounds(242, 357, 247, 133);
		panel_VideoInfo.add(textArea_VideoInfo);

		lblVideoInfo = new JLabel("Video Infos:");
		lblVideoInfo.setFont(new Font("Tahoma", Font.PLAIN, 14));
		lblVideoInfo.setForeground(Color.WHITE);
		lblVideoInfo.setHorizontalAlignment(SwingConstants.CENTER);
		lblVideoInfo.setBounds(242, 299, 247, 47);
		panel_VideoInfo.add(lblVideoInfo);
	}

	private JButton videoButton(NormalVideo video) {
		String name = video.getName();
		JButton button = new JButton(name);
		button.setToolTipText(
				"<html>" + name + "<br>" + video.getVideoInfo().getDuration() + "</html>");
		button.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 15));
		button.setPreferredSize(new Dimension(400, 50));
		button.setOpaque(true);
		button.addActionListener(e -> openVideoInfo(video));
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON3) video.getVideoInfo().openImage();
			}
		});
		panel.add(button);
		buttons.add(button);
		return button;
	}

	private JScrollPane scrollPane_VideoInfo;
	private JPanel panel_VideoInfo;
	private JPanel panel_VideoImage;
	private JLabel lblVideoTitel;
	private JButton btnPlay;
	private JButton btnFav;
	private JTextArea textArea_VideoInfo;
	private JLabel lblVideoInfo;
	private String lastImg;

	private void openVideoInfo(NormalVideo video) {
		lastImg = video.getVideoInfo().getFrame();
		scrollPane_VideoInfo.setVisible(true);
		lblVideoTitel.setText(video.getName());
		panel_VideoImage.removeAll();
		Utils.openImageFrameVideo(lastImg, panel_VideoImage);
		for (ActionListener listener : btnPlay.getActionListeners())
			btnPlay.removeActionListener(listener);
		btnPlay.addActionListener(e -> video.play());
		textArea_VideoInfo.setText(video.getVideoInfo().getDuration() + "\nErstellt: "
				+ video.createdDate()
				+ "\nGröße: "
				+ video.getFileSizeAsString());
		File file = new File(lastImg);
		file.delete();
	}

	private void initVideos(int time) {
		if (!Data.createdInLastThirtyDays.isEmpty()) Data.createdInLastThirtyDays.clear();
		getAllNewFiles(Data.oripath, time);
	}

	public void getAllNewFiles(String path, int time) {
		File folder = new File(path);
		for (File file : folder.listFiles()) {
			if (file.getName().contains(".mp4")) {
				if (Utils.isCreatedInTheLastDays(file, time))
					Data.createdInLastThirtyDays.add(file);
			} else if (file.isDirectory()) {
				getAllNewFiles(file.getAbsolutePath(), time);
			}
		}
	}

//	public static void publicInit() {
//		initVideos(30);
//		for (File file : Data.createdInLastThirtyDays) videoButton(file, panel);
//	}
}