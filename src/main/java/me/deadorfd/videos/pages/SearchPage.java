package me.deadorfd.videos.pages;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import me.deadorfd.videos.Main;
import me.deadorfd.videos.manager.WrapLayout;
import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.video.NormalVideo;

import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.ItemEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.pages
 * @Date 20.12.2022
 * @Time 15:01:23
 */
public class SearchPage extends JPanel {
	private JTextField textSearch;
	public JCheckBox bxPersons = new JCheckBox("Person");
	public JCheckBox bxContains = new JCheckBox("Beinhaltet");
	public JCheckBox bxStartsWith = new JCheckBox("Fängt an mit");
	public static ArrayList<File> result = new ArrayList<>();
	public ArrayList<JButton> buttons = new ArrayList<>();

	/**
	 * Create the panel.
	 */
	public SearchPage() {
		setBackground(new Color(128, 128, 128));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBounds(100, 100, 619, 482);
		setLayout(null);

		JLabel titel = new JLabel("Suche");
		titel.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 15));
		titel.setHorizontalAlignment(SwingConstants.CENTER);
		titel.setBounds(186, 25, 212, 14);
		add(titel);

		JButton btnMenu = new JButton("Menü");
		btnMenu.addActionListener(e -> Main.switchScenes("main"));
		btnMenu.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 15));
		btnMenu.setBounds(186, 413, 212, 23);
		add(btnMenu);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(16);
		scrollPane.setBounds(22, 125, 548, 277);
		add(scrollPane);

		JPanel panel = new JPanel();
		panel.setLayout(new WrapLayout());
		panel.setSize(new Dimension(300, 1));
		scrollPane.setViewportView(panel);

		for (File file : result) videoButton(new NormalVideo(file), panel);

		bxPersons.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 11));
		bxPersons.setBounds(79, 77, 97, 23);
		bxPersons.addItemListener(e -> {
			if (e.getStateChange() != ItemEvent.SELECTED) return;
			bxContains.setSelected(false);
			bxStartsWith.setSelected(false);
		});
		add(bxPersons);

		bxContains.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 11));
		bxContains.setBounds(186, 77, 97, 23);
		bxContains.addItemListener(e -> {
			if (e.getStateChange() != ItemEvent.SELECTED) return;
			bxPersons.setSelected(false);
			bxStartsWith.setSelected(false);
		});
		add(bxContains);

		bxStartsWith.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 11));
		bxStartsWith.setBounds(293, 77, 97, 23);
		bxStartsWith.addItemListener(e -> {
			if (e.getStateChange() != ItemEvent.SELECTED) return;
			bxContains.setSelected(false);
			bxPersons.setSelected(false);
		});
		add(bxStartsWith);

		textSearch = new JTextField();
		textSearch.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 11));
		textSearch.setBounds(270, 50, 128, 20);
		textSearch.addActionListener(e -> {
			ArrayList<JButton> removeList = new ArrayList<>();
			for (JButton button : buttons) {
				panel.remove(button);
				removeList.add(button);
			}
			buttons.removeAll(removeList);
			search(textSearch);
			for (File file : result) videoButton(new NormalVideo(file), panel);
			panel.updateUI();
		});
		add(textSearch);
		textSearch.setColumns(10);

		JLabel lblSearch = new JLabel("Suche:");
		lblSearch.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 11));
		lblSearch.setHorizontalAlignment(SwingConstants.CENTER);
		lblSearch.setBounds(186, 53, 74, 17);
		add(lblSearch);
	}

	public void search(JTextField text) {
		String content = text.getText();
		if (getType().equals("contains")) {
			if (!result.isEmpty()) result.clear();
			getContainsVideos(Data.oripath, content);
		} else if (getType().equals("startsWith")) {
			if (!result.isEmpty()) result.clear();
			getStartsWithVideos(Data.oripath, content);
		}
	}

	public static void getContainsVideos(String path, String content) {
		File folder = new File(path);
		content = content.toLowerCase();
		for (File file : folder.listFiles()) {
			String name = file.getName().toLowerCase();
			if (file.isDirectory()) {
				getContainsVideos(file.getAbsolutePath(), content);
			} else if (name.contains(content)) {
				result.add(file);
			}
		}
	}

	public static void getStartsWithVideos(String path, String content) {
		File folder = new File(path);
		content = content.toLowerCase();
		for (File file : folder.listFiles()) {
			String name = file.getName().toLowerCase();
			if (file.isDirectory()) {
				getContainsVideos(file.getAbsolutePath(), content);
			} else if (name.startsWith(content)) {
				result.add(file);
			}
		}
	}

	public String getType() {
		if (bxContains.isSelected()) return "contains";
		if (bxStartsWith.isSelected()) return "startsWith";
		if (bxPersons.isSelected()) return "persons";
		return "contains";
	}

	public void videoButton(NormalVideo video, JPanel panel) {
		String name = video.getName();
		JButton button = new JButton(name);
		button.setToolTipText(
				"<html>" + name + "<br>" + video.getVideoInfo().getDuration() + "</html>");
		button.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 15));
		button.setPreferredSize(new Dimension(500, 50));
		button.setOpaque(true);
		button.addActionListener(e -> video.play());
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent event) {
				if (event.getButton() == MouseEvent.BUTTON3) video.getVideoInfo().openImage();
			}
		});
		panel.add(button);
		buttons.add(button);
	}
}