package me.deadorfd.videos.controller;

import com.jfoenix.controls.JFXButton;

import javafx.fxml.FXML;
import me.deadorfd.videos.App;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos.controller
 * @Date 02.02.2024
 * @Time 19:34:02
 */
public class MainController {
	@FXML
	private JFXButton buttonVideos, buttonSettings, buttonHistory;

	@FXML
	private JFXButton buttonFavorites, buttonSearch, buttonNewVideos;

	@FXML
	private void initialize() {
		buttonVideos.setOnAction(event -> new App().changePage("Videos"));
		buttonSettings.setOnAction(event -> new App().changePage("Settings"));
		buttonHistory.setOnAction(event -> new App().changePage("History"));
		buttonFavorites.setOnAction(event -> new App().changePage("Favorites"));
		buttonSearch.setOnAction(event -> new App().changePage("Search"));
		buttonNewVideos.setOnAction(event -> new App().changePage("NewVideos"));
	}
}