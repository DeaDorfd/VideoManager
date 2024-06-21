package me.deadorfd.videos.controller;

import com.jfoenix.controls.JFXSlider;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.beans.InvalidationListener;
import javafx.beans.binding.Bindings;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import javafx.scene.media.MediaPlayer.Status;
import javafx.scene.media.MediaView;
import javafx.stage.Stage;
import javafx.util.Duration;
import me.deadorfd.videos.utils.Settings;
import me.deadorfd.videos.utils.files.VideoFile;
import me.deadorfd.videos.utils.sql.Setting;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 02.04.2024
 * @Time 14:41:49
 */
public class MediaPlayerController {

	@FXML
	private StackPane stackpaneMedia;

	private MediaView mediaView;

	@FXML
	private Label labelTitel, labelEndTime, labelCurrentTime, labelVol;

	@FXML
	private FontAwesomeIconView iconPlay, iconSkipNext, iconSkipBack, iconVol, iconSize, iconMute;

	@FXML
	private AnchorPane paneVol, root;

	@FXML
	private JFXSlider sliderVol, barVideoTime;

	private Double sizeWidth, playWidth, skipNextWidth, skipBackWidth;

	private Double sizeHeight, playHeight, skipNextHeight, skipBackHeight, iconVolHeight;

	public static VideoFile video;

	private String convertToTime(Double millisDouble) {
		long durationInMillis = millisDouble.longValue();
		long millis = durationInMillis % 1000;
		long second = (durationInMillis / 1000) % 60;
		long minute = (durationInMillis / (1000 * 60)) % 60;
		long hour = (durationInMillis / (1000 * 60 * 60)) % 24;

		return String.format("%02d:%02d:%02d", hour, minute, second);
	}

	private MediaPlayer player;

	@FXML
	private void initialize() {
		paneVol.setVisible(false);
		barVideoTime.setValue(0);
		EventHandler handler = e -> {
			if (paneVol.isVisible()) paneVol.setVisible(false);
		};
		iconVol.setOnMouseClicked(event -> paneVol.setVisible(!paneVol.isVisible()));
		if (video == null) return;
		labelTitel.setText(video.getName());
		player = new MediaPlayer(new Media(video.getFile().toURI().toString()));
		mediaView = new MediaView(player);
		stackpaneMedia.getChildren().add(mediaView);
		player.setVolume(Double.parseDouble(new Setting(Settings.MEDIAPLAYER_VOLUME).getStatus()));
		player.setOnReady(() -> {
			barVideoTime.maxProperty()
					.bind(Bindings.createDoubleBinding(() -> player.getTotalDuration().toSeconds(), player.totalDurationProperty()));
			labelEndTime.setText(convertToTime(player.getTotalDuration().toMillis()));
		});
		InvalidationListener listener = e -> player.seek(Duration.seconds(barVideoTime.getValue()));
		player.currentTimeProperty().addListener((observable, oldValue, newValue) -> {
			barVideoTime.valueProperty().removeListener(listener);
			barVideoTime.setValue(newValue.toSeconds());
			labelCurrentTime.setText(convertToTime(newValue.toMillis()));
			barVideoTime.valueProperty().addListener(listener);
		});
		sliderVol.setValue(Double.parseDouble(new Setting(Settings.MEDIAPLAYER_VOLUME).getStatus()) * 100.0);
		labelVol.setText(Double.valueOf(sliderVol.getValue()).intValue() + "");
		sliderVol.valueProperty().addListener(event -> {
			new Setting(Settings.MEDIAPLAYER_VOLUME).setStatus(sliderVol.getValue() / 100.0 + "");
			player.setVolume(sliderVol.getValue() / 100.0);
			labelVol.setText(Double.valueOf(sliderVol.getValue()).intValue() + "");
		});
		iconMute.setOnMouseClicked(event -> {
			if (player.isMute()) {
				player.setMute(false);
				iconMute.setGlyphName("VOLUME_UP");
				iconVol.setGlyphName("VOLUME_UP");
				player.setVolume(sliderVol.getValue() / 100.0);
			} else {
				iconMute.setGlyphName("VOLUME_OFF");
				iconVol.setGlyphName("VOLUME_OFF");
				player.setMute(true);
			}
		});
		iconPlay.setOnMouseClicked(event -> {
			if (player.getStatus() == Status.PAUSED || player.getStatus() == Status.READY) {
				iconPlay.setGlyphName("PAUSE");
				player.play();
			} else if (player.getStatus() == Status.PLAYING) {
				iconPlay.setGlyphName("PLAY");
				player.pause();
			} else if (player.getStatus() == Status.STOPPED) {
				iconPlay.setGlyphName("PAUSE");
				player.seek(Duration.seconds(0));
				player.play();
			}
		});
		iconSkipNext.setOnMouseClicked(event -> player.seek(Duration.seconds(player.getCurrentTime().toSeconds() + 10)));
		iconSkipBack.setOnMouseClicked(event -> player.seek(Duration.seconds(player.getCurrentTime().toSeconds() - 10)));
		iconSize.setOnMouseClicked(event -> {
			Stage stage = (Stage) root.getScene().getWindow();
			stage.setFullScreen(!stage.isFullScreen());
		});
		root.hoverProperty().addListener((observable, oldValue, newValue) -> {
			if (player.getStatus() == Status.PAUSED || player.getStatus() == Status.READY) return;
			if (newValue) {
				// show
				iconPlay.setVisible(true);
				iconVol.setVisible(true);
				iconSkipBack.setVisible(true);
				iconSkipNext.setVisible(true);
				iconSize.setVisible(true);
				labelTitel.setVisible(true);
				labelCurrentTime.setVisible(true);
				labelEndTime.setVisible(true);
				barVideoTime.setVisible(true);
			} else {
				// hide
				paneVol.setVisible(false);
				iconPlay.setVisible(false);
				iconVol.setVisible(false);
				iconSkipBack.setVisible(false);
				iconSkipNext.setVisible(false);
				iconSize.setVisible(false);
				labelTitel.setVisible(false);
				labelCurrentTime.setVisible(false);
				labelEndTime.setVisible(false);
				barVideoTime.setVisible(false);
			}
		});
		root.widthProperty().addListener(event -> resize(root.getWidth(), root.getHeight()));
		root.heightProperty().addListener(event -> resize(root.getWidth(), root.getHeight()));
	}

	private void resize(Double width, Double height) {
		stackpaneMedia.setPrefWidth(width);
		mediaView.setFitWidth(width);
		barVideoTime.setPrefWidth(width);
		labelTitel.setPrefWidth(width);
		labelEndTime.setLayoutX(width - 75);
		iconSize.setLayoutX(width - 60);
		iconSkipNext.setLayoutX((width / 2) + 45);
		iconPlay.setLayoutX(width / 2);
		iconSkipBack.setLayoutX((width / 2) - 45);

		stackpaneMedia.setPrefHeight(height);
		mediaView.setFitHeight(height);
		iconSize.setLayoutY(height - 40);
		iconSkipNext.setLayoutY(height - 40);
		iconPlay.setLayoutY(height - 40);
		iconSkipBack.setLayoutY(height - 40);
		barVideoTime.setLayoutY(height - 105);
		labelCurrentTime.setLayoutY(height - 90);
		labelEndTime.setLayoutY(height - 90);
		paneVol.setLayoutY(height - 145);
		iconVol.setLayoutY(height - 40);
	}

	public void stop() {
		if (player.getStatus() == Status.PLAYING) player.stop();
	}
}