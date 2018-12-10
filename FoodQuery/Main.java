/**
 * Filename:   Main.java
 * Project:    Meal Planner
 * Authors:    Aaron Hernandez 001, Henry Koenig 001, Xiao Fei 001
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * 
 * Due Date:   Dec 12th
 * Version:    1.0
 * 
 * Credits:    N/A
 * 
 * Bugs:       N/A
 */
package application;

import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/**
 * This class contains the main method and sets up the primary stage of Meal
 * Planner
 * 
 * @author Aaron Hernandez
 * @author Xiao Fei
 * @author Henry Koenig
 */
public class Main extends Application {

	/**
	 * The start method that sets up the primary stage and scene for GUI
	 * 
	 * @param primaryStage
	 *            The main stage (i.e. window) for the GUI to use
	 */
	@Override
	public void start(Stage primaryStage) {

		try {

			BorderPane root = new BorderPane();
			root = GUI.setupGUI(root);
			Scene scene = new Scene(root, 800, 600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Meal Planner");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * The main method to launch the application
	 * 
	 * @param args
	 *            The arguments passed into the Meal Planner GUI (unused)
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
