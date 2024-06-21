package me.deadorfd.videos.utils.files;

import javafx.concurrent.Task;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCombination;
import javafx.stage.Stage;
import me.deadorfd.videos.controller.MediaPlayerController;
import me.deadorfd.videos.utils.files.info.BaseFileInfo;
import me.deadorfd.videos.utils.files.info.VideoInfo;
import me.deadorfd.videos.utils.sql.History;

import java.awt.*;
import java.io.File;
import java.io.IOException;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.utils.files
 * @Date 06.05.2024
 * @Time 03:00:33
 */
public class VideoFile extends BaseFile {

	public VideoFile(File file) {
		super(file);
	}

	@Override
	public BaseFileInfo getFileInfo() {
		return new VideoInfo(this);
	}

	@Override
	public void open() {
		if (!getFileInfo().getFormat().equals("mp4")) {
			try {
				Desktop.getDesktop().open(getFile());
				History.create(getPath());
			} catch (IOException ignored) {}
			return;
		}
		MediaPlayerController.video = this;
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/MediaPlayerPage.fxml"));
		Task<Parent> loadTask = new Task<>() {
			@Override
			protected Parent call() throws IOException {
				return loader.load();
			}
		};
		loadTask.setOnSucceeded(e -> {
			Parent root = loadTask.getValue();
			Stage stage = new Stage();
			stage.setTitle(getName());
			stage.setScene(new Scene(root));
			stage.setResizable(true);
			stage.setFullScreenExitKeyCombination(KeyCombination.NO_MATCH);
			MediaPlayerController controller = loader.getController();
			stage.show();
			stage.setOnCloseRequest(event -> controller.stop());
			History.create(getPath());
		});
		new Thread(loadTask).start();
	}

	@Override
	public Image getImage() {
		return new Image("file:/" + ((VideoInfo) getFileInfo()).getVideoLoader().getFrame(), true);
	}

}
