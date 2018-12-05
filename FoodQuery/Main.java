package application;
	
import javafx.application.Application;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;

/**
 * This class contains the main method and setup the primary stage of mealplanner
 * 
 * @author Aaron Hernandez
 * @author Xiao Fei
 * @author Henry Koenig
 */
public class Main extends Application {
	
	/**
	 * The start method that sets up the primary stage and scene for GUI
	 * @param primaryStage
	 */
	@Override
	public void start(Stage primaryStage) {
		
		try {
			
			BorderPane root = new BorderPane();
			root = GUI.setupGUI(root);
			Scene scene = new Scene(root,800,600);
			scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
			primaryStage.setTitle("Meal Planner");
			primaryStage.setScene(scene);
			primaryStage.setResizable(false);
			primaryStage.show();
			
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * The main method
	 * @param args
	 */
	public static void main(String[] args) {
		launch(args);
	}
}
