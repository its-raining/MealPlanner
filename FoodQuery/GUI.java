/**
 * Filename:   GUI.java
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

import java.util.List;
import java.io.File;
import java.util.ArrayList;
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
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

/**
 * This class setup the layout of overall border-pane. GUI controls all the
 * functionality of the overall Meal Planner Application. Menu includes load and
 * save, apply query and reset filter. Bottom pane includes all the control
 * buttons for adding food to food list and my meal list, and add/remove rules
 * to filter list. Left, center and right pane include food list, filter list
 * and my meal list.
 * 
 * @author Aaron Hernandez
 * @author Xiao Fei
 * @author Henry Koenig
 */
public class GUI {

	// the private field of TableView for food list
	private static TableView<FoodItem> foodView;

	// the private field of TableView for my meal list
	private static TableView<FoodItem> mealView;

	// the private field of TableView for filters' list
	private static TableView<String> filterView;

	// the private field of ObservableList for food list
	private static ObservableList<FoodItem> foodList = FXCollections.observableArrayList();

	// the private field of ObservableList for my meal list
	private static ObservableList<FoodItem> mealList = FXCollections.observableArrayList();

	// the private field of ObservableList for filters' list
	private static ObservableList<String> filterList = FXCollections.observableArrayList();

	// the private field of FoodData for food list
	private static FoodData foodData = new FoodData();

	// the private field of MealData for my meal list
	private static MealData mealData = new MealData();

	// the private field of Comparator comparing names of FoodItem
	private static final Comparator<FoodItem> FOOD_COMPARATOR = new Comparator<FoodItem>() {
		@Override
		public int compare(FoodItem food1, FoodItem food2) {
			return food1.getName().compareTo(food2.getName());
		}
	};

	/**
	 * The method to setup the layout of border-pane
	 * 
	 * @param root
	 *            The BorderPane that contains all functionality for the Meal
	 *            Planner
	 * @return the overall border-pane of the Meal Planner app
	 */
	public static BorderPane setupGUI(BorderPane root) {

		// top pane as menu
		root.setTop(setTopMenu());

		// bottom pane is where all the buttons located
		root.setBottom(setBottomButtons());

		// left pane is for the food list
		root.setLeft(setLeftFoodPane());

		// center pane is for the filter list
		root.setCenter(setFilterPane());

		// right pane is for my meal list
		root.setRight(setRightMealPane());

		return root;
	}

	/**
	 * Menu bar includes load & save and controls over filters
	 * 
	 * @return a MenuBar instance with load/save and filter controls to add at the
	 *         top of the main BorderPane
	 */
	private static MenuBar setTopMenu() {

		// creates the File and FoodList drop-downs
		final Menu fileMenu = new Menu("File");
		final Menu foodMenu = new Menu("Filters");

		// spawns menu bar at top of screen
		MenuBar menuBar = new MenuBar();
		menuBar.getMenus().addAll(fileMenu, foodMenu);

		// items underneath the file menu
		MenuItem load = new MenuItem("Load");
		MenuItem save = new MenuItem("Save");

		// when user clicks on load button, user can choose a file to load through
		// file-chooser
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

		// when user clicks on load button, user can choose a file name to save through
		// file-chooser
		save.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent t) {
				FileChooser fileChooser = new FileChooser();
				File file = fileChooser.showSaveDialog(new Stage());
				if (file != null) {
					foodData.saveFoodItems(file.getAbsolutePath());
				}
			}
		});

		// add load and save to fileMenu
		fileMenu.getItems().add(load);
		fileMenu.getItems().add(save);

		// items underneath the filter menu
		MenuItem applyQuery = new MenuItem("Apply Query");
		MenuItem resetFilter = new MenuItem("Reset Filter");

		// apply query button's functionality
		applyQuery.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				// nutrient rules list filtered without name rules
				List<String> nutrientRules = new ArrayList<String>();

				// list of filtered name rules
				List<FoodItem> nameFilterResults = null;

				// loop through filter list
				for (String rule : filterList) {
					if (rule.contains("name")) {
						if (nameFilterResults == null) {
							nameFilterResults = foodData.filterByName(rule.split(" ")[1]);
						} else {
							nameFilterResults.retainAll(foodData.filterByName(rule.split(" ")[1]));
						}
					} else {

						// add rules other than name to nutrient rules
						nutrientRules.add(rule);
					}
				}

				// filter foodItem by nutrient rules
				List<FoodItem> filteredNutrients = foodData.filterByNutrients(nutrientRules);

				// if there isn't any rules in name filter, then get all the foodItem
				if (nameFilterResults == null) {
					nameFilterResults = foodData.getAllFoodItems();
				}

				// intersect result of filtering name and filtering nutrients
				filteredNutrients.retainAll(nameFilterResults);

				// update foodList
				foodList.clear();
				for (FoodItem food : filteredNutrients) {
					foodList.add(food);
				}
			}
		});

		// reset filter button's functionality
		resetFilter.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				// clear filterList and foodList but not foodData
				filterList.clear();
				foodList.clear();

				// add foodItems in foodData to foodList
				for (FoodItem food : foodData.getAllFoodItems()) {
					foodList.add(food);
				}
			}
		});

		// add operations related to filters to foodMenu
		foodMenu.getItems().add(applyQuery);
		foodMenu.getItems().add(resetFilter);

		return menuBar;
	}

	/**
	 * Sets up the food list pane on the left side of the main BorderPane
	 * 
	 * @return a VBox instance for the left pane with food list
	 */
	@SuppressWarnings("unchecked")
	private static VBox setLeftFoodPane() {
		VBox leftVbox = new VBox();
		final Label myMeal = new Label("Food List");
		myMeal.setFont(new Font("Arial", 25));
		Pane foodListPane = new Pane();

		// set up the TableView for food list
		foodView = new TableView<FoodItem>();
		foodView.setEditable(false);
		foodView.setPrefSize(340, 440);
		foodView.setColumnResizePolicy(TableView.UNCONSTRAINED_RESIZE_POLICY);

		// one column named food name with all food's names
		TableColumn<FoodItem, String> nameCol = new TableColumn<FoodItem, String>("Food Name");

		// lambda expression for adding food to the table
		nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));

		// making sure enough space for food's name
		nameCol.setPrefWidth(750);
		foodView.getColumns().addAll(nameCol);
		foodView.setItems(foodList);

		// add title and TableView of food list to the pane
		foodListPane.getChildren().addAll(myMeal, foodView);
		foodListPane.setPrefSize(350, 450);

		// set up and add title & pane to v-box
		leftVbox.setAlignment(Pos.CENTER);
		leftVbox.getChildren().addAll(myMeal, foodListPane);
		return leftVbox;
	}

	/**
	 * Sets up the filter pane in the center of the main BorderPane
	 * 
	 * @return a VBox instance for the center pane with filters
	 */
	@SuppressWarnings("unchecked")
	private static VBox setFilterPane() {
		VBox filterVbox = new VBox();
		final Label filters = new Label("Filters");
		filters.setFont(new Font("Arial", 25));
		Pane filterPane = new Pane();

		// set up the TableView for filters
		filterView = new TableView<String>();
		filterView.setEditable(false);
		filterView.setPrefSize(220, 440);
		filterView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		TableColumn<String, String> ruleCol = new TableColumn<String, String>("Rule");

		// lambda expression for adding food to the table
		ruleCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue()));

		// making sure enough space for food's name
		ruleCol.setPrefWidth(750);

		filterView.getColumns().addAll(ruleCol);
		filterView.setItems(filterList);

		// add title and TableView of filters to the pane
		filterPane.getChildren().addAll(filters, filterView);
		filterPane.setPrefSize(220, 450);
		filterPane.getStyleClass().add("filterpane");

		// set up and add title & pane to v-box
		filterVbox.setAlignment(Pos.CENTER);
		filterVbox.getChildren().addAll(filters, filterPane);
		return filterVbox;
	}

	/**
	 * Sets up the meal list on the right side of the main BorderPane
	 * 
	 * @return a VBox instance for the right pane with my meal list
	 */
	@SuppressWarnings("unchecked")
	private static VBox setRightMealPane() {
		VBox myMealVbox = new VBox();
		final Label myMeal = new Label("My Meal");
		myMeal.setFont(new Font("Arial", 25));
		Pane rightMealPane = new Pane();

		// set up the TableView for my meal list
		mealView = new TableView<FoodItem>();
		mealView.setEditable(false);
		mealView.setPrefSize(220, 440);
		mealView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);

		// one column named my food name with all current food's names
		TableColumn<FoodItem, String> nameCol = new TableColumn<FoodItem, String>("My Food Name");

		// lambda expression for adding food to the table
		nameCol.setCellValueFactory(f -> new SimpleStringProperty(f.getValue().getName()));

		// making sure enough space for food's name
		nameCol.setPrefWidth(750);
		mealView.getColumns().addAll(nameCol);
		mealView.setItems(mealList);

		// add title and TableView of filters to the pane
		rightMealPane.getChildren().addAll(myMeal, mealView);
		rightMealPane.setPrefSize(220, 450);
		rightMealPane.getStyleClass().add("mymealpane");

		// set up and add title & pane to v-box
		myMealVbox.setAlignment(Pos.CENTER);
		myMealVbox.getChildren().addAll(myMeal, rightMealPane);
		return myMealVbox;
	}

	/**
	 * Sets up the buttons at the bottom of the main BorderPane
	 * 
	 * @return a Pane instance for the bottom of the GUI with all the buttons
	 */
	private static Pane setBottomButtons() {

		// the bottom pane
		Pane bottomPane = new Pane();
		bottomPane.setId("bottompane");

		// the add to list button
		Button addtoFoodList = new Button("Add to List");
		addtoFoodList.setLayoutX(10);
		addtoFoodList.setLayoutY(10);

		// add to list button's functionality
		addtoFoodList.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				// pop-up form that prompts user to add foodItem to my meal
				FoodItemAddForm foodItemAddForm = new FoodItemAddForm();
				foodItemAddForm.start(foodList, foodData, FOOD_COMPARATOR);
			}
		});

		// the add to meal button
		Button addtoMeal = new Button("Add To Meal");
		addtoMeal.setLayoutX(10);
		addtoMeal.setLayoutY(47);
		addtoMeal.setMaxWidth(105);

		// add to meal button's functionality
		addtoMeal.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				// add selected food from foodList to my meal list
				mealData.addFoodItem(foodView.getSelectionModel().getSelectedItem());
				mealList.add(foodView.getSelectionModel().getSelectedItem());
			}
		});

		// the add new rule button
		Button addNewRule = new Button("Add New Rule");
		addNewRule.setLayoutX(350);
		addNewRule.setLayoutY(10);

		// add new rule button's functionality
		addNewRule.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				// pop-up form that prompts user to add a new rule
				FilterRulesForm filterRuleForm = new FilterRulesForm();
				filterRuleForm.start(filterList);
			}
		});

		// the remove rule button
		Button removeRule = new Button("Remove Rule");
		removeRule.setLayoutX(350);
		removeRule.setLayoutY(47);

		// the functionality of remove rule button
		removeRule.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {

				// selected rule will be removed from filterList
				filterList.remove(filterView.getSelectionModel().getSelectedItem());
			}
		});

		// the view meal summary button
		Button viewMealSummary = new Button("View Meal Summary");
		viewMealSummary.setLayoutX(575);
		viewMealSummary.setLayoutY(10);

		// when user clicks on view meal summary button, meal analysis will pop-up
		viewMealSummary.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				MealAnalysisPane mealAnalysisPane = new MealAnalysisPane();
				mealAnalysisPane.start(mealData);
			}
		});

		// the remove food button
		Button removeFood = new Button("Remove Food");
		removeFood.setLayoutX(575);
		removeFood.setLayoutY(47);

		// when user clicks on remove food button, selected food will be removed from my
		// meal list
		removeFood.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				mealData.removeFoodItem(mealView.getSelectionModel().getSelectedItem());
				mealList.remove(mealView.getSelectionModel().getSelectedItem());
			}
		});

		// add all components to bottom pane
		bottomPane.getChildren().addAll(addtoMeal, addtoFoodList, addNewRule, viewMealSummary, removeRule, removeFood);
		return bottomPane;
	}
}
