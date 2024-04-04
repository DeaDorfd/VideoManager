package me.deadorfd.videos.controller;

import static me.deadorfd.videos.utils.Data.*;
import static me.deadorfd.videos.utils.FileManager.*;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import me.deadorfd.videos.App;
import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.Folder;
import me.deadorfd.videos.utils.Session;
import me.deadorfd.videos.utils.video.NormalVideo;
import me.deadorfd.videos.utils.video.info.NormalVideoInfo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 02.02.2024
 * @Time 19:41:35
 */
public class VideosController {

	@FXML
	private JFXButton buttonMain, buttonOpenFolder, buttonBack;

	@FXML
	private ScrollPane videosPane, folderPane;

	@FXML
	private VBox vboxVideos, vboxFolder;

	@FXML
	public void btnOnMainClick(ActionEvent event) {
		if (!prePath.isBlank()) prePath = "";
		if (session == null) session = new Session(path);
		session.setPath(path);
		session.setVideoVValue(videosPane.getVvalue());
		session.setFolderVValue(folderPane.getVvalue());
		if (!videoInfoPath.isBlank()) session.setVideoInfoPath(videoInfoPath);
		new App().changePage("Main");
	}

	@FXML
	public void btnOnOpenFolderClick(ActionEvent event) {
		try {
			Desktop.getDesktop().open(new File(path));
		} catch (IOException e1) {}
	}

	@FXML
	public void btnOnBackClick(ActionEvent event) {
		if (!currentFolders.isEmpty()) {
			currentFolders.remove(currentFolders.size() - 1);
			updatePath();
			reinit();
		}
	}

	public static String prePath = "";
	public static Session session;

	@FXML
	private void initialize() {
		videoInfoPane.setVisible(false);
		videosPane.setFitToHeight(true);
		videosPane.setFitToWidth(true);
		folderPane.setFitToHeight(true);
		folderPane.setFitToWidth(true);
		vboxVideos.setSpacing(10);
		vboxFolder.setSpacing(10);
		if (prePath.isBlank()) {
			if (session != null) {
				currentFolders.clear();
				currentFolders.addAll(session.getFolders());
				updatePath();
				reinit();
				if (!session.getVideoInfoPath().isBlank())
					openVideoInfo(new NormalVideo(new File(session.getVideoInfoPath())));
				folderPane.setVvalue(session.getFolderVValue());
				videosPane.setVvalue(session.getVideoVValue());
			} else {
				for (File folder : folders) vboxFolder.getChildren().add(getFolderButton(new Folder(folder)));
				for (NormalVideo video : videos) vboxVideos.getChildren().add(getVideoButton(video));
			}
		} else {
			prePath = prePath.replaceAll(oripath, "");
			currentFolders.clear();
			for (String folder : prePath.split("/"))
				if (!Data.isVideoFile(folder) & !folder.isBlank()) currentFolders.add(folder);
			updatePath();
			reinit();
			openVideoInfo(new NormalVideo(new File(oripath + prePath)));
		}
	}

	@FXML
	private AnchorPane videoInfoPane;

	@FXML
	private Label videoInfoTitel;

	@FXML
	private ImageView videoInfoImage;

	@FXML
	private JFXTextArea videoInfos;

	@FXML
	private JFXButton videoInfoPlay;

	@FXML
	private FontAwesomeIconView videoInfoFav, videoInfoDelete;

	private String videoInfoPath = "";

	private void openVideoInfo(NormalVideo video) {
		videoInfoPath = video.getPath();
		if (video.isFavorite())
			videoInfoFav.setFill(Paint.valueOf("#ff0000"));
		else
			videoInfoFav.setFill(Paint.valueOf("#000000"));
		videoInfoPane.setVisible(true);
		videoInfoTitel.setText(video.getName());
		videoInfoImage.setImage(null);
		videoInfoPlay.setOnAction(e -> video.play());
		videoInfoDelete.setOnMouseClicked(event -> {
			if (!video.delete()) return;
			videoInfoPane.setVisible(false);
			reinit();
			for (File folder : folders) vboxFolder.getChildren().add(getFolderButton(new Folder(folder)));
			for (NormalVideo videos : videos) vboxVideos.getChildren().add(getVideoButton(videos));
		});
		videoInfoFav.setOnMouseClicked(event -> {
			if (video.isFavorite()) {
				video.removeFromFavorites();
				videoInfoFav.setFill(Paint.valueOf("#000000"));
			} else {
				video.addToFavorites();
				videoInfoFav.setFill(Paint.valueOf("#ff0000"));
			}
		});

		videoInfos.setText(" \n \n\nLoading...\n \n ");

		// Infos
		new Thread(() -> {
			NormalVideoInfo info = (NormalVideoInfo) video.getVideoInfo();
			String lastImg = info.getFrame();
			videoInfoImage.setImage(new Image("file:/" + lastImg));
			videoInfos.setText(info.getDuration() + "\nErstellt: "
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
					+ info.getFrameRate());
			File file = new File(lastImg);
			file.delete();
		}).start();
	}

	private JFXButton getVideoButton(NormalVideo video) {
		JFXButton button = new JFXButton(video.getName());
		button.setFont(new Font("Candara", 15));
		button.setPrefSize(200, 25);
		button.setStyle("-fx-background-color: #3D4956; -fx-text-fill: #ffff;");
		button.setOnAction(event -> openVideoInfo(video));
		button.setOnMouseClicked(event -> {
			if (event.getButton() == MouseButton.MIDDLE) video.getVideoInfo().openImage();
		});
		return button;
	}

	private JFXButton getFolderButton(Folder folder) {
		JFXButton button = new JFXButton(folder.getName());
		button.setStyle("-fx-background-color: #3D4956; -fx-text-fill: #ffff;");
		button.setFont(new Font("Candara", 15));
		button.setPrefSize(200, 25);
		button.setOnAction(event -> {
			currentFolders.add(folder.getName());
			updatePath();
			reinit();
		});
		return button;
	}

	private void reinit() {
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
		for (NormalVideo video : videos) vboxVideos.getChildren().add(getVideoButton(video));
		for (File folder : folders) vboxFolder.getChildren().add(getFolderButton(new Folder(folder)));
	}

}