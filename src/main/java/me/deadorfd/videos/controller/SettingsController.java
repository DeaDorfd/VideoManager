package me.deadorfd.videos.controller;

import java.io.File;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.input.MouseButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import me.deadorfd.videos.App;
import me.deadorfd.videos.Video;
import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.FileManager;
import me.deadorfd.videos.utils.Settings;
import me.deadorfd.videos.utils.language.Languages;
import me.deadorfd.videos.utils.sql.Setting;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 29.02.2024
 * @Time 02:39:59
 */
public class SettingsController {
	@FXML
	private JFXButton buttonMain;

	@FXML
	private JFXTextField textfieldPath, textfieldHistoryClearDays;

	@FXML
	private FontAwesomeIconView iconFolder;

	@FXML
	private JFXToggleButton toggleDebugMode, toggleHistoryClear;

	@FXML
	private JFXComboBox<String> boxLanguage;

	@FXML
	private Label labelTitel, labelDefaultPath, labelDebugMode, labelHistoryDelete;

	private void setLanguage() {
		buttonMain.setText(Video.getLanguageAPI().getText("generell.button_back"));
		labelTitel.setText(Video.getLanguageAPI().getText("settings.label_titel"));
		labelDefaultPath.setText(Video.getLanguageAPI().getText("settings.label_defaultpath"));
		labelDebugMode.setText(Video.getLanguageAPI().getText("settings.label_debugmode"));
		labelHistoryDelete.setText(Video.getLanguageAPI().getText("settings.label_historydelete"));
		String on = Video.getLanguageAPI().getText("settings.toggle_on");
		String off = Video.getLanguageAPI().getText("settings.toggle_off");
		if (toggleDebugMode.isSelected()) toggleDebugMode.setText(on);
		if (!toggleDebugMode.isSelected()) toggleDebugMode.setText(off);
		if (toggleHistoryClear.isSelected()) toggleHistoryClear.setText(on);
		if (!toggleHistoryClear.isSelected()) toggleHistoryClear.setText(off);
	}

	@FXML
	private void initialize() {
		for (Languages lang : Languages.values()) boxLanguage.getItems().add(lang.getName());
		boxLanguage.setValue(new Setting(Settings.LANGUAGE).getStatus());
		textfieldPath.setText(new Setting(Settings.DEFAULT_PATH).getStatus());
		toggleDebugMode.setSelected(Boolean.valueOf(new Setting(Settings.DEBUG_MODE).getStatus()));
		textfieldHistoryClearDays.setText(new Setting(Settings.HISTORY_CLEAR_DAYS).getStatus());
		toggleHistoryClear.setSelected(Boolean.valueOf(new Setting(Settings.HISTORY_CLEAR).getStatus()));
		setLanguage();
		boxLanguage.setOnAction(event -> {
			String language = boxLanguage.getSelectionModel().getSelectedItem();
			new Setting(Settings.LANGUAGE).setStatus(language);
			setLanguage();
		});
		iconFolder.setOnMouseClicked(event -> {
			if (event.getButton() != MouseButton.PRIMARY) return;
			DirectoryChooser directoryChooser = new DirectoryChooser();
			directoryChooser.setTitle("Ordner auswählen");
			File selected = directoryChooser.showDialog(new Stage());
			if (selected == null) return;
			textfieldPath.setText(selected.getPath().replace('\\', '/'));
			new Setting(Settings.DEFAULT_PATH).setStatus(selected.getPath().replace('\\', '/'));
		});
		toggleDebugMode.setOnAction(event -> {
			new Setting(Settings.DEBUG_MODE).setStatus(toggleDebugMode.isSelected() + "");
			String on = Video.getLanguageAPI().getText("settings.toggle_on");
			String off = Video.getLanguageAPI().getText("settings.toggle_off");
			if (toggleDebugMode.isSelected()) {
				toggleDebugMode.setText(on);
			} else
				toggleDebugMode.setText(off);
		});
		toggleHistoryClear.setOnAction(event -> {
			new Setting(Settings.HISTORY_CLEAR).setStatus(toggleHistoryClear.isSelected() + "");
			String on = Video.getLanguageAPI().getText("settings.toggle_on");
			String off = Video.getLanguageAPI().getText("settings.toggle_off");
			if (toggleHistoryClear.isSelected()) {
				toggleHistoryClear.setText(on);
			} else
				toggleHistoryClear.setText(off);
		});
		textfieldPath.setOnAction(event -> {
			File file = new File(textfieldPath.getText());
			if (!file.exists()) {
				textfieldPath.setText(new Setting(Settings.DEFAULT_PATH).getStatus());
				return;
			}
			textfieldPath.setText(file.getPath().replace('\\', '/'));
			new Setting(Settings.DEFAULT_PATH).setStatus(file.getPath().replace('\\', '/'));
		});
		textfieldHistoryClearDays.setOnAction(
				event -> new Setting(Settings.HISTORY_CLEAR_DAYS).setStatus(toggleHistoryClear.getText()));
		buttonMain.setOnAction(event -> {
			if (!textfieldPath.getText().isBlank()) {
				File file = new File(textfieldPath.getText());
				if (!file.exists()) {
					textfieldPath.setText(new Setting(Settings.DEFAULT_PATH).getStatus());
					return;
				}
				Data.path = new Setting(Settings.DEFAULT_PATH).getStatus();
				Data.oripath = new Setting(Settings.DEFAULT_PATH).getStatus();
				Data.currentFolders.clear();
				FileManager.init();
				new Setting(Settings.DEFAULT_PATH).setStatus(file.getPath().replace('\\', '/'));
			}
			String language = boxLanguage.getSelectionModel().getSelectedItem();
			new Setting(Settings.LANGUAGE).setStatus(language);
			new Setting(Settings.DEBUG_MODE).setStatus(toggleDebugMode.isSelected() + "");
			new Setting(Settings.HISTORY_CLEAR).setStatus(toggleHistoryClear.isSelected() + "");
			new Setting(Settings.HISTORY_CLEAR_DAYS).setStatus(textfieldHistoryClearDays.getText());
			new App().changePage("Main");
		});
	}
}