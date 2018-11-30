package application;

import java.io.File;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class GUI {
		
	private static TableView<FoodItem> foodView;
	private static TableView<FoodItem> myMealView;
	private static ObservableList<FoodItem> foodList = FXCollections.observableArrayList();
	private static FoodData foodData = new FoodData();
	private static final Comparator<FoodItem> FOOD_COMPARATOR = new Comparator<FoodItem>() {
		@Override
		public int compare(FoodItem food1, FoodItem food2) {
			return food1.getName().compareTo(food2.getName());
		}
	};
	
	private static MealData mealData = new MealData();
	private static ObservableList<FoodItem> mealList = FXCollections.observableArrayList();


	public static BorderPane setupGUI(BorderPane root) {
				
		root.setTop(setTopMenu());
		root.setBottom(setBottomButtons());
		root.setLeft(setLeftFoodPane());			
		root.setCenter(setFilterPane());		
		root.setRight(setRightMealPane());
				
		return root;
	}
	
	private static MenuBar setTopMenu() {
		
		// creates the File and FoodList drop-downs
		final Menu fileMenu = new Menu("File");
		final Menu foodMenu = new Menu("FoodList");
		
		// spawns menu bar at top of screen
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, foodMenu);
		
		// items underneath the file menu
		MenuItem load = new MenuItem("Load");
		MenuItem save = new MenuItem("Save");
		
		load.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				FileChooser fileChooser = new FileChooser();
				File file = fileChooser.showOpenDialog(new Stage());
				if (file != null) {
					foodData.loadFoodItems(file.getAbsolutePath());
					foodList.clear(); 
					
					for (FoodItem food : foodData.getAllFoodItems()) {
						foodList.add(food);
					}
				}
			}
		});
		
		save.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				FileChooser fileChooser = new FileChooser();
				File file = fileChooser.showSaveDialog(new Stage());
				if (file != null) {
					foodData.saveFoodItems(file.getAbsolutePath());				
				}
			}
		});
		
		fileMenu.getItems().add(load);
		fileMenu.getItems().add(save);
		
		// items underneath the FoodList menu
		MenuItem addFood = new MenuItem("Add New Food");
		MenuItem addRule = new MenuItem("Add New Rule");
		MenuItem removeRule = new MenuItem("Remove Rule");
		
		foodMenu.getItems().add(addFood);
		foodMenu.getItems().add(addRule);
		foodMenu.getItems().add(removeRule);
		
		return menuBar;
	}
	
	@SuppressWarnings("unchecked")
	private static VBox setLeftFoodPane() {
		VBox leftVbox = new VBox();
		final Label myMeal = new Label("Food List");
		myMeal.setFont(new Font("Arial", 25));
		Pane foodListPane = new Pane();
		
		foodView = new TableView<FoodItem>();

		foodView.setEditable(false);
		foodView.setPrefSize(340, 440);
		foodView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);
		
		TableColumn<FoodItem, String> nameCol = new TableColumn<>("Food Name");
		nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
		nameCol.setPrefWidth(750);
		foodView.getColumns().addAll(nameCol);	
		foodView.setItems(foodList);
		
		foodListPane.getChildren().addAll(myMeal, foodView);
		foodListPane.setPrefSize(350,450);
		
		leftVbox.setAlignment(Pos.CENTER);
		leftVbox.getChildren().addAll(myMeal, foodListPane);
		return leftVbox;
	}

	@SuppressWarnings("unchecked")
	private static VBox setFilterPane() {
		VBox filterVbox = new VBox();
		final Label filters = new Label("Filters");
		filters.setFont(new Font("Arial", 25));
		Pane filterPane = new Pane();
		
		TableView<String> filterView = new TableView<String>();
		
		filterView.setEditable(false);
		filterView.setPrefSize(220, 440);
		filterView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		TableColumn<String, String> nutrientCol = new TableColumn<String, String>("Nutrient");
		TableColumn<String, Double> rangeCol = new TableColumn<String, Double>("Range");

		filterView.getColumns().addAll(nutrientCol, rangeCol);
		
		filterPane.getChildren().addAll(filters, filterView);
		filterPane.setPrefSize(220,450);
		filterPane.getStyleClass().add("filterpane");
		
		filterVbox.setAlignment(Pos.CENTER);
		filterVbox.getChildren().addAll(filters, filterPane);
		return filterVbox;
	}
	
	@SuppressWarnings("unchecked")
	private static VBox setRightMealPane() {
		VBox myMealVbox = new VBox();
		final Label myMeal = new Label("My Meal");
		myMeal.setFont(new Font("Arial", 25));
		Pane rightMealPane = new Pane();
		
		myMealView = new TableView<FoodItem>();
		
		myMealView.setEditable(false);
		myMealView.setPrefSize(220, 440);
		myMealView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		TableColumn<FoodItem, String> nameCol = new TableColumn<>("My Food Name");
		
		//
		nameCol.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getName()));
		nameCol.setPrefWidth(750);
		myMealView.getColumns().addAll(nameCol);	
		myMealView.setItems(mealList);
		
		rightMealPane.getChildren().addAll(myMeal, myMealView);
		rightMealPane.setPrefSize(220,450);
		//
		
		//myMealView.getColumns().addAll(nameCol);
		//rightMealPane.getChildren().addAll(myMealView, myMeal);
		//rightMealPane.setPrefSize(220,450);
		
		rightMealPane.getStyleClass().add("mymealpane");
		
		myMealVbox.setAlignment(Pos.CENTER);
		myMealVbox.getChildren().addAll(myMeal, rightMealPane);
		
		return myMealVbox;
	}
	
	private static Pane setBottomButtons() {
		Pane bottomPane = new Pane();		
		bottomPane.setId("bottompane");		
		
		Button addtoFoodList = new Button("Add to List");
		addtoFoodList.setLayoutX(10);
		addtoFoodList.setLayoutY(10);		
		
		Button addtoMeal = new Button("Add To Meal");
		addtoMeal.setLayoutX(10);
		addtoMeal.setLayoutY(47);
		addtoMeal.setMaxWidth(105);
		
		addtoMeal.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			 public void handle(ActionEvent e) {				
				mealData.addFoodItem(foodView.getSelectionModel().getSelectedItem());
				for (FoodItem myfood : mealData.getMealList()) {
					mealList.add(myfood);
				}
			}
		});	
		
		Button applyQuery = new Button("Apply Query");
		applyQuery.setLayoutX(350);
		applyQuery.setLayoutY(10);
		
		Button viewMealSummary = new Button("View Meal Summary");
		viewMealSummary.setLayoutX(575);
		viewMealSummary.setLayoutY(10);
		
		addtoMeal.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			 public void handle(ActionEvent e) {
				
			}
		});	
		
		Button resetFilter = new Button("Reset Filter");
		resetFilter.setLayoutX(350);
		resetFilter.setLayoutY(47);
		
		Button removeFood = new Button("Remove Food");
		removeFood.setLayoutX(575);
		removeFood.setLayoutY(47);
		
		addtoMeal.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			 public void handle(ActionEvent e) {
				
			}
		});	
		
		addtoFoodList.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			 public void handle(ActionEvent e) {
				FoodItemAddForm foodItemAddForm = new FoodItemAddForm();
				foodItemAddForm.start(foodList, foodData, FOOD_COMPARATOR);
				
				Collections.sort(foodList, FOOD_COMPARATOR);
			}
		});		
		
		bottomPane.getChildren().addAll(addtoMeal, addtoFoodList, applyQuery, viewMealSummary, resetFilter, removeFood);
		return bottomPane;
	}
}
