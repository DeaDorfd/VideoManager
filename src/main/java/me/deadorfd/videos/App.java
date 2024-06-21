package me.deadorfd.videos;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;

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
		File dump = new File(System.getProperty("java.io.tmpdir") + "/videos/");
		System.out.println(dump.exists());
		if (dump.exists()) {
			File[] files = dump.listFiles();
			for (File f : files) f.delete();
			System.out.println(dump.delete());
		}
		System.exit(0);
	}

	@Override
	public void start(Stage stage) throws Exception {
		App.stage = stage;
		Parent root = FXMLLoader.load(getClass().getResource("/pages/MainPage.fxml"));
		Scene scene = new Scene(root);
		stage.setScene(scene);
		stage.setResizable(false);
		stage.setTitle("Videos Programm");
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
