package pl.spring.demo;

import java.util.ResourceBundle;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Startup extends Application {

	public static void main(String[] args) {
		Application.launch(args);
	}

	@Override
	public void start(Stage primaryStage) throws Exception {

		primaryStage.setTitle("Book Store");

		Parent root = FXMLLoader.load(
				getClass().getResource("/pl/spring/demo/view/book-search.fxml"),
				ResourceBundle.getBundle("pl/spring/demo/bundle/base"));

		Scene scene = new Scene(root);

		scene.getStylesheets().add(getClass()
				.getResource("/pl/spring/demo/css/standard.css").toExternalForm());

		primaryStage.setScene(scene);

		primaryStage.show();

	}

}
