package application;

import java.util.ArrayList;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FoodItemAddForm {	
	
	private static int id = 0;
	
	private Button okayButton;
	private Button cancelButton;
	private Button clearButton;

	private ArrayList<Label> labelNames;       // index 0 = "Calories", 1 = "Carbs", 
	private ArrayList<TextField> inputValues;  // index 0 = Calorie Val, 1 = carb val 
	
	public void start() {
        Stage stage = new Stage();
        stage.setTitle("Add Food Item");
        
        GridPane gridPane = new GridPane();
        gridPane.setAlignment(Pos.CENTER);
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(25, 25, 25, 25));
 
        addNutrientLabels();
        
        for(int i = 0; i < labelNames.size(); i++) {
        	 gridPane.add(labelNames.get(i), 0, i);
        	 gridPane.add(inputValues.get(i), 1, i);
        }
        
        //gridPane.setGridLinesVisible(true);
        HBox oknCancel = new HBox();
        okayButton = new Button("OK");
        cancelButton = new Button("Cancel");
        
        oknCancel.setSpacing(30);
        oknCancel.getChildren().addAll(okayButton, cancelButton);
        gridPane.add(oknCancel, 1, 7);
        
        clearButton = new Button("Clear");
        gridPane.add(clearButton, 2, 7);
        
        Scene scene = new Scene(gridPane,400,300);
        stage.setScene(scene);
        stage.show();
	}
	
	public void addNutrientLabels(){
		labelNames = new ArrayList<Label>();
		inputValues = new ArrayList<TextField>();
		
		labelNames.add(new Label("Food Name:"));
		labelNames.add(new Label("Calories:"));
		labelNames.add(new Label("Fat:"));
		labelNames.add(new Label("Carbs:"));
		labelNames.add(new Label("Fiber:"));
		labelNames.add(new Label("Protein:"));
		
		for (int i = 0; i < 6; i++) {
			inputValues.add(new TextField());
		}
		
		
	}

}
