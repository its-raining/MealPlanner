package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class MealAnalysisPane {
	private Button okayButton;
	private HashMap<String, Double> mealAnalysis;
	private ArrayList<Label> labelNames;
	
	public void start() {
        Stage stage = new Stage();
        stage.setTitle("Meal Analysis");
        
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
 
        addNutrientLabels();
        
        for(int i = 0; i < labelNames.size(); i++) {
        	 gridPane.add(labelNames.get(i), 0, i);
        }
        
        okayButton = new Button("OK");
        
        handleOK(okayButton, stage);
                
        gridPane.add(okayButton, 1, 7);
        
        Scene scene = new Scene(gridPane,400,300);
        stage.setScene(scene);
        stage.show();
	}

	private void handleOK(Button okayButton, Stage stage) {
		okayButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				stage.close();
			}
		});	
	}

	private void addNutrientLabels() {
		labelNames = new ArrayList<Label>();
		
		labelNames.add(new Label("Calories:" + mealAnalysis.get("calories")));
		labelNames.add(new Label("Fat:" + mealAnalysis.get("fat")));
		labelNames.add(new Label("Carbs:" + mealAnalysis.get("carbonhydrate")));
		labelNames.add(new Label("Fiber:" + mealAnalysis.get("fiber")));
		labelNames.add(new Label("Protein:" + mealAnalysis.get("protein")));
		
	}


}
