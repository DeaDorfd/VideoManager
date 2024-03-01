package me.deadorfd.videos.controller;

import static me.deadorfd.videos.utils.Data.*;

import java.io.File;
import java.util.UUID;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import me.deadorfd.videos.App;
import me.deadorfd.videos.utils.Data;
import me.deadorfd.videos.utils.sql.History;
import me.deadorfd.videos.utils.video.HistoryVideo;
import me.deadorfd.videos.utils.video.info.NormalVideoInfo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 29.02.2024
 * @Time 00:59:15
 */
public class HistoryController {
	@FXML
	private JFXButton buttonMain;

	@FXML
	private JFXButton buttonOpenFolder;

	@FXML
	private JFXButton buttonBack;

	@FXML
	private ScrollPane videosPane;

	@FXML
	private VBox vboxVideos;

	@FXML
	public void btnOnMainClick(ActionEvent event) {
		new App().changePage("Main");
	}

	@FXML
	private void initialize() {
		Data.history.clear();
		for (UUID uuid : History.getAllHistory()) Data.history.add(new HistoryVideo(uuid));
		videoInfoPane.setVisible(false);
		videosPane.setFitToHeight(true);
		videosPane.setFitToWidth(true);
		vboxVideos.setSpacing(10);
		for (HistoryVideo video : history) vboxVideos.getChildren().add(getVideoButton(video));
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
	private JFXButton videoInfoFav;

	@FXML
	private JFXButton videoInfoHistoryDelete;

	private void openVideoInfo(HistoryVideo video) {
		if (video.isFavorite())
			videoInfoFav.setText("Favorisiert");
		else
			videoInfoFav.setText("Favorisieren");
		videoInfoPane.setVisible(true);
		videoInfoTitel.setText(video.getName());
		videoInfoImage.setImage(null);
		videoInfoPlay.setOnAction(e -> video.play());
		videoInfoHistoryDelete.setOnAction(event -> {
			video.getHistory().removeAll();
			Data.history.clear();
			for (UUID uuid : History.getAllHistory()) Data.history.add(new HistoryVideo(uuid));
			vboxVideos.getChildren().clear();
			for (HistoryVideo videos : history) vboxVideos.getChildren().add(getVideoButton(videos));
		});
		videoInfoFav.setOnAction(event -> {
			if (video.isFavorite()) {
				video.removeFromFavorites();
				videoInfoFav.setText("Favorisieren");
			} else {
				video.addToFavorites();
				videoInfoFav.setText("Favorisiert");
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
					+ info.getFrameRate()
					+ "\nZuletzt gesehen: "
					+ video.getHistory().getTimeInString());
			File file = new File(lastImg);
			file.delete();
		}).start();
	}

	private JFXButton getVideoButton(HistoryVideo video) {
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
}