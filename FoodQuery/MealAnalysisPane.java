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

public class MealAnalysisPane {
	private HashMap<String, Double> mealAnalysis;
	private ArrayList<Label> labelNames;
	
	public void start(MealData mealData) {
		
		//setup a new stage for meal analysis
        Stage stage = new Stage();
        stage.setTitle("Meal Analysis");
        
        BorderPane borderPane = new BorderPane();
        
        //use left-pane for numerical sum of each nutrients, center-pane for bar chart
        borderPane.setLeft(addNutrientLabels(mealData));
        borderPane.setCenter(analysis());               
        
        Scene scene = new Scene(borderPane,400,300);
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
	}

	private VBox addNutrientLabels(MealData mealData) {
		VBox vbox = new VBox();
		
		labelNames = new ArrayList<Label>();
		mealAnalysis = mealData.getAnalysis();
		
		vbox.setAlignment(Pos.CENTER);
		vbox.setSpacing(30);
		vbox.setPadding(new Insets(10,10,10,10));
		
		labelNames.add(new Label("Calories:" + mealAnalysis.get("calories")));
		labelNames.add(new Label("Fat:" + mealAnalysis.get("fat")));
		labelNames.add(new Label("Carbs:" + mealAnalysis.get("carbohydrate")));
		labelNames.add(new Label("Fiber:" + mealAnalysis.get("fiber")));
		labelNames.add(new Label("Protein:" + mealAnalysis.get("protein")));
		
		vbox.getChildren().addAll(labelNames);
		return vbox;
	}
	
	private BarChart<String, Number> analysis() {
		//x and y axis for bar chart
	    final CategoryAxis xAxisNutrients = new CategoryAxis();
        final NumberAxis yAxisGrams = new NumberAxis();
        
        //initialize a bar chart
		final BarChart<String, Number> bc = new BarChart<String, Number>(xAxisNutrients, yAxisGrams);
		bc.setTitle("Meal Analysis");
		xAxisNutrients.setLabel("Nutrients");
		yAxisGrams.setLabel("Grams");
		
		//initialize one series and add nutrients to it
		XYChart.Series<String, Number> data = new XYChart.Series<>();
		data.getData().add(new XYChart.Data<String, Number>("Fat", mealAnalysis.get("fat")));
		data.getData().add(new XYChart.Data<String, Number>("Carbs", mealAnalysis.get("carbohydrate")));
		data.getData().add(new XYChart.Data<String, Number>("Fiber", mealAnalysis.get("fiber")));
		data.getData().add(new XYChart.Data<String, Number>("Protein", mealAnalysis.get("protein")));
		
		bc.getData().add(data);
		
		//hide series icon
		bc.setLegendVisible(false);
//		bc.setMaxWidth(150);
//		bc.setMaxHeight(100);
		
		return bc;		
	}

}
