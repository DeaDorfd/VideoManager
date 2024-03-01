package me.deadorfd.videos;

import java.io.IOException;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * @Author DeaDorfd
 * @Project videos
 * @Package me.deadorfd.videos
 * @Date 29.02.2024
 * @Time 21:47:07
 */
public class App extends Application {

	private static Stage stage;

	public static void main(String[] args) {
		launch(args);
		System.exit(0);
	}

	@Override
	public void start(Stage stage) throws Exception {
		this.stage = stage;
		Parent root = FXMLLoader.load(getClass().getResource("/pages/MainPage.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.show();
	}

	public void changePage(String fxml) {
		try {
			Parent root = FXMLLoader.load(getClass().getResource("/pages/" + fxml + "Page.fxml"));
			stage.getScene().setRoot(root);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static Stage getStage() {
		return stage;
	}

}
