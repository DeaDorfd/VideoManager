package me.deadorfd.videos.controller;

import java.io.File;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextArea;

import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.paint.Paint;
import me.deadorfd.videos.utils.video.BaseVideo;
import me.deadorfd.videos.utils.video.HistoryVideo;
import me.deadorfd.videos.utils.video.NormalVideo;
import me.deadorfd.videos.utils.video.info.NormalVideoInfo;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 09.04.2024
 * @Time 01:49:14
 */
public class VideoInfoController {

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

	@FXML
	private JFXButton videoInfoHistoryDelete, videoInfoOpenVideosTab;

	private Boolean isHistoryVideo = false;
	private Boolean isNormalVideo = false;

	@FXML
	private void initialize() {
		videoInfoImage.setImage(null);
		videoInfoPane.setVisible(false);
		videoInfoPane.setNodeOrientation(NodeOrientation.INHERIT);
	}

	public void setOnHistoryDelete(HistoryVideo video, Runnable run) {
		videoInfoHistoryDelete.setOnAction(event -> {
			video.getHistory().removeAll();
			Platform.runLater(run);
		});
	}

	public void setOnVideoDelete(BaseVideo video, Runnable run) {
		videoInfoDelete.setOnMouseClicked(event -> {
			if (!video.delete()) return;
			videoInfoPane.setVisible(false);
			Platform.runLater(run);
		});
	}

	public void setOnVideoFavorite(BaseVideo video, Runnable run) {
		videoInfoFav.setOnMouseClicked(event -> {
			if (video.isFavorite()) {
				video.removeFromFavorites();
				videoInfoFav.setFill(Paint.valueOf("#000000"));
				Platform.runLater(run);
			} else {
				video.addToFavorites();
				videoInfoFav.setFill(Paint.valueOf("#ff0000"));
				Platform.runLater(run);
			}
		});
	}

	public void openVideoInfo(BaseVideo video) {
		isHistoryVideo = video instanceof HistoryVideo;
		isNormalVideo = video instanceof NormalVideo;
		videoInfoImage.setImage(null);
		if (video.isFavorite())
			videoInfoFav.setFill(Paint.valueOf("#ff0000"));
		else
			videoInfoFav.setFill(Paint.valueOf("#000000"));
		videoInfoPane.setVisible(true);
		videoInfoTitel.setText(video.getName());
		videoInfoPlay.setOnAction(e -> video.play());
		if (!isHistoryVideo) {
			videoInfoHistoryDelete.setVisible(false);
		}
		if (isNormalVideo) {
			videoInfoOpenVideosTab.setVisible(false);
			videoInfoPlay.setLayoutX(247);
			videoInfoPlay.setLayoutY(516);
		}
		videoInfoOpenVideosTab.setOnAction(event -> video.openInVideosTab());
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
		Platform.runLater(() -> {
			NormalVideoInfo info = (NormalVideoInfo) video.getVideoInfo();
			String lastImg = info.getFrame();
			Image image = new Image("file:/" + lastImg);
			videoInfoImage.setImage(image);
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
					+ "\n"
					+ (isHistoryVideo
							? "Zuletzt gesehen: " + ((HistoryVideo) video).getHistory().getTimeInString()
							: ""));
			File file = new File(lastImg);
			file.delete();
		});
	}

}
