package application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

public class FoodItemAddForm {	
	
	private static int userID;
	
	private Button okayButton;
	private Button cancelButton;
	private Button clearButton;

	 // index 0="name", 1="ID", 2="Calories", 3="Fat", 4="Carb", 5="Fiber", 6="Protein"
	private ArrayList<Label> labelNames;       
	private ArrayList<TextField> inputValues; 
	
	public void start(ObservableList<FoodItem> foodList, FoodData foodData, Comparator<FoodItem> foodComparator) {
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
        
        HBox oknCancel = new HBox();
        okayButton = new Button("OK");
        cancelButton = new Button("Cancel");
        
        handleCancel(cancelButton, stage);
        handleOK(okayButton, stage, foodList, foodData, foodComparator);
        
        oknCancel.setSpacing(30);
        oknCancel.getChildren().addAll(okayButton, cancelButton);
        gridPane.add(oknCancel, 1, 7);
        
        clearButton = new Button("Clear");
        handleClear(clearButton);
        
        gridPane.add(clearButton, 2, 7);
        
        Scene scene = new Scene(gridPane,400,300);
        stage.setScene(scene);
        stage.show();
	}
	
	public void addNutrientLabels(){
		labelNames = new ArrayList<Label>();
		inputValues = new ArrayList<TextField>();
		
		labelNames.add(new Label("Food Name:"));
		//labelNames.add(new Label("Food ID:"));
		labelNames.add(new Label("Calories:"));
		labelNames.add(new Label("Fat:"));
		labelNames.add(new Label("Carbs:"));
		labelNames.add(new Label("Fiber:"));
		labelNames.add(new Label("Protein:"));
		
		for (int i = 0; i < 6; i++) {
			inputValues.add(new TextField());
		}
	}
	
	public void handleCancel(Button cancel, Stage stage) {
		cancel.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				stage.close();
			}
		});
	}
	
	public void handleOK(Button OK, Stage stage,ObservableList<FoodItem> foodList, FoodData foodData, Comparator<FoodItem> foodComparator) {
		OK.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				
				try {
					
				    String foodName = inputValues.get(0).getText();
				    String foodID = userID + "";
				    
				    // if the user doesn't type in a food name or id
				    if (foodName.trim().equals("")) {
				    	
				    	throw new Exception();
				    }
				    
				    double calories =  Double.parseDouble(inputValues.get(1).getText());
				    double fat = Double.parseDouble(inputValues.get(2).getText());
				    double carb =  Double.parseDouble(inputValues.get(3).getText());
				    double fiber = Double.parseDouble(inputValues.get(4).getText());
				    double protein = Double.parseDouble(inputValues.get(5).getText());
				    				 				    
				    // creates a new food item of nutrients given
				    FoodItem foodItem = new FoodItem(foodID, foodName);
				    foodItem.addNutrient("calories", calories);
				    foodItem.addNutrient("fat", fat);
				    foodItem.addNutrient("carbonhydrate", carb);
				    foodItem.addNutrient("fiber", fiber);
				    foodItem.addNutrient("protein", protein);
				    
				    // adds the new food item to each list
				    foodList.add(foodItem);
				    foodData.addFoodItem(foodItem);
				    Collections.sort(foodList, foodComparator);
/*				    for (FoodItem f : foodList) {
				    	System.out.print(f.getID() + " " + f.getName() + ": ");
				    	System.out.print(f.getNutrientValue("calories") + " ");
				    	System.out.print(f.getNutrientValue("fat") + " ");
				    	System.out.print(f.getNutrientValue("carb") + " ");
				    	System.out.print(f.getNutrientValue("fiber") + " ");
				    	System.out.println(f.getNutrientValue("protein") + " ");
				    }
				    
				    System.out.println("===========================");
				    
				    for (FoodItem f : foodData.getAllFoodItems()) {
				    	System.out.print(f.getID() + f.getName() + ": ");
				    	System.out.print(f.getNutrientValue("calories") + " ");
				    	System.out.print(f.getNutrientValue("fat") + " ");
				    	System.out.print(f.getNutrientValue("carb") + " ");
				    	System.out.print(f.getNutrientValue("fiber") + " ");
				    	System.out.println(f.getNutrientValue("protein") + " ");
				    }
				    
				    System.out.println("===========================");
				    System.out.println();*/
				    
				    userID++;
				    
				    stage.close();
				    
				    
				} catch (NumberFormatException f) {
					//System.out.println("User entered a string instead of a double");
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Invalid Input");
					alert.setHeaderText(null);
					alert.setContentText("Please enter a double for nutrient's value.");

					alert.showAndWait();
				} catch (Exception n) {
					//System.out.println("Food Name isn't present");
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Invalid Input");
					alert.setHeaderText(null);
					alert.setContentText("Food Name isn't present.");

					alert.showAndWait();
				}
				
			}
		});
	}
	
	public void handleClear(Button clearButton) {
		clearButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				
				for (TextField tf: inputValues) {
					tf.setText("");
				}	
			}
		});
	}
}
