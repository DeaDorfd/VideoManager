package me.deadorfd.videos.controller;

import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import me.deadorfd.videos.utils.files.BaseFile;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 06.05.2024
 * @Time 03:48:45
 */
public class FileInfoController {

	@FXML
	private ImageView preview;

	@FXML
	private Label infos;

	public void setInformations(BaseFile baseFile) {
		preview.setImage(baseFile.getImage());
		infos.setText(baseFile.getName() + "\n" + baseFile.getFileInfo().getCreatedDate());
		preview.hoverProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue) preview.setOpacity(0.7);
			if (!newValue) preview.setOpacity(1);
		});
		preview.setOnMouseClicked(event -> {
			baseFile.open();
		});
	}

	public void setCustomImageClick(EventHandler<? super MouseEvent> value) {
		preview.setOnMouseClicked(value);
	}
}