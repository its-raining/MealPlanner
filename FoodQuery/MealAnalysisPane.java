/**
 * Filename:   MealAnalysisPane.java
 * Project:    p5
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
import java.util.HashMap;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class sets up the pop-up meal analysis pane with both numerical 
 * and bar-chart analysis
 * 
 * @author Aaron Hernandez
 * @author Xiao Fei
 * @author Henry Koenig
 */
public class MealAnalysisPane {
	
	// Map of nutrients and their corresponding values
	private HashMap<String, Double> mealAnalysis;
	
	// Array list of labels
	private ArrayList<Label> labelNames;
	
	/**
	 * The method called with meal data to set up meal analysis pane
	 * 
	 * @param mealData
	 */
	public void start(MealData mealData) {
		
		// setup a new stage for meal analysis pane
        Stage stage = new Stage();
        stage.setTitle("Meal Analysis");
        
        BorderPane borderPane = new BorderPane();
        
        // use left-pane for numerical sum of each nutrients, center-pane for bar chart
        borderPane.setLeft(addNutrientLabels(mealData));
        borderPane.setCenter(analysis());               
        
        Scene scene = new Scene(borderPane,400,300);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
	}
	
	/**
	 * This method returns a v-box that contains all numerical values of nutrients
	 * 
	 * @param mealData
	 * @return v-box
	 */
	private VBox addNutrientLabels(MealData mealData) {
		VBox vbox = new VBox();
		
		labelNames = new ArrayList<Label>();
		
		// call getAnalysis method to get the map of nutrients and their value
		mealAnalysis = mealData.getAnalysis();
		
		// v-box's alignment, spacing and padding setting
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(30);
		vbox.setPadding(new Insets(10,10,10,10));
		
		// adding nutrient's label and value to labelNames array list
		labelNames.add(new Label("Calories:" + mealAnalysis.get("calories")));
		labelNames.add(new Label("Fat:" + mealAnalysis.get("fat")));
		labelNames.add(new Label("Carbs:" + mealAnalysis.get("carbohydrate")));
		labelNames.add(new Label("Fiber:" + mealAnalysis.get("fiber")));
		labelNames.add(new Label("Protein:" + mealAnalysis.get("protein")));
		
		// add labels to v-box
		vbox.getChildren().addAll(labelNames);
		return vbox;
	}
	
	/**
	 * This method returns a bar-chart with all nutrients except calories
	 * 
	 * @return
	 */
	private BarChart<String, Number> analysis() {
		
		// x and y axis for bar chart
	    final CategoryAxis xAxisNutrients = new CategoryAxis();
        final NumberAxis yAxisGrams = new NumberAxis();
        
        // initialize a bar chart
		final BarChart<String, Number> bc = new BarChart<String, Number>(xAxisNutrients, yAxisGrams);
		bc.setTitle("Meal Analysis");
		xAxisNutrients.setLabel("Nutrients");
		yAxisGrams.setLabel("Grams");
		
		// initialize one series and add nutrients to it
		XYChart.Series<String, Number> data = new XYChart.Series<>();
		data.getData().add(new XYChart.Data<String, Number>("Fat", mealAnalysis.get("fat")));
		data.getData().add(new XYChart.Data<String, Number>("Carbs", mealAnalysis.get("carbohydrate")));
		data.getData().add(new XYChart.Data<String, Number>("Fiber", mealAnalysis.get("fiber")));
		data.getData().add(new XYChart.Data<String, Number>("Protein", mealAnalysis.get("protein")));
		
		bc.getData().add(data);
		
		// hide series icon
		bc.setLegendVisible(false);
		
		return bc;		
	}

}
