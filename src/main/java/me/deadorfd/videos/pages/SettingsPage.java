package me.deadorfd.videos.pages;

import java.awt.Color;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import me.deadorfd.videos.Main;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import javax.swing.JCheckBox;
import javax.swing.JTextField;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.pages
 * @Date 20.12.2022
 * @Time 15:01:08
 */
public class SettingsPage extends JPanel {
	private JTextField pathText;

	/**
	 * Create the panel.
	 */
	public SettingsPage() {
		// 128, 128, 128
		setBackground(new Color(128, 128, 128));
		setBorder(new EmptyBorder(5, 5, 5, 5));
		setBounds(100, 100, 619, 482);
		setLayout(null);

		JButton btnBackMenuButton = new JButton("Menü");
		btnBackMenuButton.addActionListener(e -> Main.switchScenes("main"));
		btnBackMenuButton.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 15));
		btnBackMenuButton.setBounds(215, 399, 158, 23);
		add(btnBackMenuButton);

		JLabel titel = new JLabel("Einstellungen");
		titel.setHorizontalAlignment(SwingConstants.CENTER);
		titel.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 15));
		titel.setBounds(215, 27, 158, 14);
		add(titel);

		JCheckBox debugMode = new JCheckBox("Debug Modus");
		debugMode.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 15));
		debugMode.setBounds(215, 73, 158, 23);
		add(debugMode);

		pathText = new JTextField();
		pathText.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 11));
		pathText.setBounds(287, 148, 138, 20);
		add(pathText);
		pathText.setColumns(10);

		JLabel pathTitel = new JLabel("Hauptordner:");
		pathTitel.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 15));
		pathTitel.setBounds(172, 151, 105, 14);
		add(pathTitel);

		JCheckBox rightclickVideos = new JCheckBox("Rechtsklick Erweiterung (Videos)");
		rightclickVideos.setHorizontalAlignment(SwingConstants.CENTER);
		rightclickVideos.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 15));
		rightclickVideos.setBounds(160, 225, 287, 23);
		add(rightclickVideos);

		JCheckBox rightclickFolders = new JCheckBox("Rechtsklick Erweiterung (Ordner)");
		rightclickFolders.setHorizontalAlignment(SwingConstants.CENTER);
		rightclickFolders.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 15));
		rightclickFolders.setBounds(160, 280, 287, 23);
		add(rightclickFolders);

		JCheckBox darkMode = new JCheckBox("Dark Mode");
		darkMode.setFont(new Font("Source Serif Pro Black", Font.PLAIN, 15));
		darkMode.setBounds(215, 335, 158, 23);
		add(darkMode);
	}
}