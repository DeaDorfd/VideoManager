package me.deadorfd.videos.controller;

import java.io.File;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXToggleButton;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.fxml.FXML;
import javafx.scene.input.MouseButton;
import javafx.stage.DirectoryChooser;
import javafx.stage.Stage;
import me.deadorfd.videos.App;
import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.FileManager;
import me.deadorfd.videos.utils.Settings;
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
	private void initialize() {
		textfieldPath.setText(new Setting(Settings.DEFAULT_PATH).getStatus());
		toggleDebugMode.setSelected(Boolean.valueOf(new Setting(Settings.DEBUG_MODE).getStatus()));
		if (toggleDebugMode.isSelected()) toggleDebugMode.setText("AN");
		textfieldHistoryClearDays.setText(new Setting(Settings.HISTORY_CLEAR_DAYS).getStatus());
		toggleHistoryClear.setSelected(Boolean.valueOf(new Setting(Settings.HISTORY_CLEAR).getStatus()));
		if (toggleHistoryClear.isSelected()) toggleHistoryClear.setText("AN");
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
			if (toggleDebugMode.isSelected()) {
				toggleDebugMode.setText("AN");
			} else
				toggleDebugMode.setText("AUS");
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
		toggleHistoryClear.setOnAction(event -> {
			new Setting(Settings.HISTORY_CLEAR).setStatus(toggleHistoryClear.isSelected() + "");
			if (toggleHistoryClear.isSelected()) {
				toggleHistoryClear.setText("AN");
			} else
				toggleHistoryClear.setText("AUS");
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
			new Setting(Settings.DEBUG_MODE).setStatus(toggleDebugMode.isSelected() + "");
			new Setting(Settings.HISTORY_CLEAR).setStatus(toggleHistoryClear.isSelected() + "");
			new Setting(Settings.HISTORY_CLEAR_DAYS).setStatus(textfieldHistoryClearDays.getText());
			new App().changePage("Main");
		});
	}
}