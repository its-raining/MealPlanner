/**
 * Filename:   FoodItemAddForm.java
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

/**
 * This class represents a food item add form that will pop-up for users to
 * enter new food's info
 * 
 * @author Aaron Hernandez
 * @author Xiao Fei
 * @author Henry Koenig
 */
public class FoodItemAddForm {

	// user ID will automatically assigned in back-end
	private static int userID;

	// ok, cancel and clear buttons
	private Button okayButton;
	private Button cancelButton;
	private Button clearButton;

	// index 0="name", 1="ID", 2="Calories", 3="Fat", 4="Carb", 5="Fiber",
	// 6="Protein"
	private ArrayList<Label> labelNames;
	private ArrayList<TextField> inputValues;

	/**
	 * The public method called to set up the stage, scene and everything else in
	 * the form to add a food item
	 * 
	 * @param foodList
	 *            The food list to add the user-specified foodItem to
	 * @param foodData
	 *            The backend for managing all the operations associated with
	 *            FoodItems
	 * @param foodComparator
	 *            The comparator used to sort the foodList
	 */
	public void start(ObservableList<FoodItem> foodList, FoodData foodData, Comparator<FoodItem> foodComparator) {
		Stage stage = new Stage();
		stage.setTitle("Add Food Item");

		// set up the grid pane
		GridPane gridPane = new GridPane();
		gridPane.setAlignment(Pos.CENTER);
		gridPane.setHgap(10);
		gridPane.setVgap(10);
		gridPane.setPadding(new Insets(25, 25, 25, 25));

		// add labels to nutrient arraylist
		addNutrientLabels();

		// add s and textfields to grid pane
		for (int i = 0; i < labelNames.size(); i++) {
			gridPane.add(labelNames.get(i), 0, i);
			gridPane.add(inputValues.get(i), 1, i);
		}

		// an hbox that put ok and cancel button together
		HBox oknCancel = new HBox();
		okayButton = new Button("OK");
		cancelButton = new Button("Cancel");

		// the functionalities of ok and cancel button
		handleCancel(stage);
		handleOK(stage, foodList, foodData, foodComparator);

		// add two buttons to hbox
		oknCancel.setSpacing(30);
		oknCancel.getChildren().addAll(okayButton, cancelButton);
		gridPane.add(oknCancel, 1, 7);

		clearButton = new Button("Clear");
		handleClear();

		// add clear button to gridpane
		gridPane.add(clearButton, 2, 7);

		// set up a new scene for add-form
		Scene scene = new Scene(gridPane, 400, 300);
		stage.setScene(scene);
		stage.show();
	}

	/**
	 * The public method that adds nutrient's labels
	 */
	public void addNutrientLabels() {
		labelNames = new ArrayList<Label>();
		inputValues = new ArrayList<TextField>();

		// adding labels to labelNames array list
		labelNames.add(new Label("Food Name:"));
		labelNames.add(new Label("Calories:"));
		labelNames.add(new Label("Fat:"));
		labelNames.add(new Label("Carbs:"));
		labelNames.add(new Label("Fiber:"));
		labelNames.add(new Label("Protein:"));

		// add each text field to inputValues array list
		for (int i = 0; i < 6; i++) {
			inputValues.add(new TextField());
		}
	}

	/**
	 * The functionality of cancel button
	 * 
	 * @param stage
	 *            The stage (i.e. window) to close upon clicking the cancel button
	 */
	public void handleCancel(Stage stage) {
		cancelButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				stage.close();
			}
		});
	}

	/**
	 * The functionality of OK button
	 * 
	 * @param stage
	 *            The stage (i.e. window) to close, if everything is filled out,
	 *            upon clicking the ok button
	 * @param foodList
	 *            The food list to add the user-specified foodItem to
	 * @param foodData
	 *            The backend for managing all the operations associated with
	 *            FoodItems
	 * @param foodComparator
	 *            The comparator used to sort the foodList
	 */
	public void handleOK(Stage stage, ObservableList<FoodItem> foodList, FoodData foodData,
			Comparator<FoodItem> foodComparator) {
		okayButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				try {
					// initialize food name and food id
					String foodName = inputValues.get(0).getText();
					String foodID = userID + "";

					// if the user doesn't type in a food name or id
					if (foodName.trim().equals("")) {
						throw new Exception();
					}

					double calories = Double.parseDouble(inputValues.get(1).getText());
					double fat = Double.parseDouble(inputValues.get(2).getText());
					double carb = Double.parseDouble(inputValues.get(3).getText());
					double fiber = Double.parseDouble(inputValues.get(4).getText());
					double protein = Double.parseDouble(inputValues.get(5).getText());

					// creates a new food item of nutrients given
					FoodItem foodItem = new FoodItem(foodID, foodName);
					foodItem.addNutrient("calories", calories);
					foodItem.addNutrient("fat", fat);
					foodItem.addNutrient("carbohydrate", carb);
					foodItem.addNutrient("fiber", fiber);
					foodItem.addNutrient("protein", protein);

					// adds the new food item to each list
					foodList.add(foodItem);
					foodData.addFoodItem(foodItem);

					// sort food list after insertion
					Collections.sort(foodList, foodComparator);

					userID++;

					stage.close();

				} catch (NumberFormatException f) {

					// catch exception and pop-up an alert if user did not enter double for
					// nutrient's value
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Invalid Input");
					alert.setHeaderText(null);
					alert.setContentText("Please enter a double for nutrient's value.");

					alert.showAndWait();
				} catch (Exception n) {

					// catch exception and pop-up an alert if user did not enter food name
					Alert alert = new Alert(AlertType.INFORMATION);
					alert.setTitle("Invalid Input");
					alert.setHeaderText(null);
					alert.setContentText("Food Name isn't present.");

					alert.showAndWait();
				}

			}
		});
	}

	/**
	 * Functionality of clear button, which sets text fields to empty string
	 */
	public void handleClear() {
		clearButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				// looping through text field array list and set each of them to empty string
				for (TextField tf : inputValues) {
					tf.setText("");
				}
			}
		});
	}
}
