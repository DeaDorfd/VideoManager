package me.deadorfd.videos.controller;

import static me.deadorfd.videos.utils.Data.*;
import static me.deadorfd.videos.utils.FileManager.*;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import me.deadorfd.videos.App;
import me.deadorfd.videos.Video;
import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.Folder;
import me.deadorfd.videos.utils.Session;
import me.deadorfd.videos.utils.video.NormalVideo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 02.02.2024
 * @Time 19:41:35
 */
public class VideosController {

	@FXML
	private AnchorPane root;

	@FXML
	private JFXButton buttonMain, buttonOpenFolder, buttonBack;

	@FXML
	private ScrollPane videosPane, folderPane;

	@FXML
	private VBox vboxVideos, vboxFolder;

	private String videoInfoPath = "";
	public static String prePath = "";
	public static Session session;

	private AnchorPane videoInfoPane;

	@FXML
	private void initialize() {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/pages/VideoInfoPage.fxml"));
		try {
			videoInfoPane = loader.load();
		} catch (IOException e) {
			e.printStackTrace();
		}
		VideoInfoController controller = (VideoInfoController) loader.getController();
		videoInfoPane.setLayoutX(478);
		videoInfoPane.setLayoutY(48);
		root.getChildren().add(videoInfoPane);
		videoInfoPane.setVisible(false);
		videosPane.setFitToHeight(true);
		videosPane.setFitToWidth(true);
		folderPane.setFitToHeight(true);
		folderPane.setFitToWidth(true);
		buttonMain.setText(Video.getLanguageAPI().getText("generell.button_back"));
		buttonBack.setText(Video.getLanguageAPI().getText("videos.button_back"));
		buttonOpenFolder.setText(Video.getLanguageAPI().getText("videos.button_openfolder"));
		buttonMain.setOnAction(event -> {
			if (!prePath.isBlank()) prePath = "";
			if (session == null) session = new Session(path);
			session.setPath(path);
			session.setVideoVValue(videosPane.getVvalue());
			session.setFolderVValue(folderPane.getVvalue());
			if (!videoInfoPath.isBlank()) session.setVideoInfoPath(videoInfoPath);
			new App().changePage("Main");
		});
		buttonOpenFolder.setOnAction(event -> {
			try {
				Desktop.getDesktop().open(new File(path));
			} catch (IOException e1) {}
		});
		buttonBack.setOnAction(event -> {
			if (!currentFolders.isEmpty()) {
				currentFolders.remove(currentFolders.size() - 1);
				updatePath();
				reinit(controller);
			}
		});
		vboxVideos.setSpacing(10);
		vboxFolder.setSpacing(10);
		if (prePath.isBlank()) {
			if (session != null) {
				currentFolders.clear();
				currentFolders.addAll(session.getFolders());
				updatePath();
				reinit(controller);
				if (!session.getVideoInfoPath().isBlank()) {
					NormalVideo video = new NormalVideo(new File(session.getVideoInfoPath()));
					videoInfoPane.setVisible(false);
					controller.openVideoInfo(video);
					controller.setOnVideoDelete(video, () -> {
						reinit(controller);
					});
				}
				folderPane.setVvalue(session.getFolderVValue());
				videosPane.setVvalue(session.getVideoVValue());
			} else {
				for (File folder : folders)
					vboxFolder.getChildren().add(getFolderButton(new Folder(folder), controller));
				for (NormalVideo video : videos)
					vboxVideos.getChildren().add(getVideoButton(video, controller));
			}
		} else {
			prePath = prePath.replaceAll(oripath, "");
			currentFolders.clear();
			for (String folder : prePath.split("/"))
				if (!Data.isVideoFile(folder) & !folder.isBlank()) currentFolders.add(folder);
			updatePath();
			reinit(controller);
			NormalVideo video = new NormalVideo(new File(oripath + prePath));
			videoInfoPane.setVisible(false);
			controller.openVideoInfo(video);
			controller.setOnVideoDelete(video, () -> {
				reinit(controller);
			});
		}
	}

	private JFXButton getVideoButton(NormalVideo video, VideoInfoController controller) {
		JFXButton button = new JFXButton(video.getName());
		button.setFont(new Font("Candara", 15));
		button.setPrefSize(200, 25);
		button.setStyle("-fx-background-color: #3D4956; -fx-text-fill: #ffff;");
		button.setOnAction(event -> {
			videoInfoPane.setVisible(false);
			controller.openVideoInfo(video);
			controller.setOnVideoDelete(video, () -> {
				reinit(controller);
			});
		});
		button.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.MIDDLE) video.getVideoInfo().openImage();
		});
		return button;
	}

	private JFXButton getFolderButton(Folder folder, VideoInfoController controller) {
		JFXButton button = new JFXButton(folder.getName());
		button.setStyle("-fx-background-color: #3D4956; -fx-text-fill: #ffff;");
		button.setFont(new Font("Candara", 15));
		button.setPrefSize(200, 25);
		button.setOnAction(event -> {
			currentFolders.add(folder.getName());
			updatePath();
			reinit(controller);
		});
		return button;
	}

	private void reinit(VideoInfoController controller) {
		videos.clear();
		folders.clear();
		vboxVideos.getChildren().clear();
		vboxFolder.getChildren().clear();
		for (File file : new File(path).listFiles()) {
			if (Data.isVideoFile(file.getName())) {
				videos.add(new NormalVideo(file));
				continue;
			} else if (file.getName().contains(".")) continue;
			folders.add(file);
			continue;
		}
		for (NormalVideo video : videos) vboxVideos.getChildren().add(getVideoButton(video, controller));
		for (File folder : folders)
			vboxFolder.getChildren().add(getFolderButton(new Folder(folder), controller));
	}

}