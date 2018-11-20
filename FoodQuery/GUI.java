package application;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;


public class GUI {

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
		ScrollPane foodListPane = new ScrollPane();
		
		TableView<FoodItem> foodView = new TableView<FoodItem>();
		
		foodView.setEditable(false);
		foodView.setPrefSize(340, 500);
		foodView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		TableColumn<FoodItem, String> nameCol = new TableColumn<>("Food Name");
		
		foodView.getColumns().addAll(nameCol);	
		
		foodListPane.setContent(foodView);
		foodListPane.getStyleClass().add("scrollpane");
		
		leftVbox.setAlignment(Pos.CENTER);
		leftVbox.getChildren().addAll(myMeal, foodListPane);
		return leftVbox;
	}

	@SuppressWarnings("unchecked")
	private static VBox setFilterPane() {
		VBox filterVbox = new VBox();
		final Label filters = new Label("Filters");
		filters.setFont(new Font("Arial", 25));
		ScrollPane filterPane = new ScrollPane();
		
		TableView<String> filterView = new TableView<String>();
		
		filterView.setEditable(false);
		filterView.setPrefSize(225, 500);
		filterView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		TableColumn<String, Double> nutrientCol = new TableColumn<>("Nutrient");
		TableColumn<String, Double> rangeCol = new TableColumn<>("Range");

		filterView.getColumns().addAll(nutrientCol, rangeCol);
		
		filterPane.setContent(filterView);
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
		ScrollPane rightMealPane = new ScrollPane();
		
		TableView<FoodItem> myMealView = new TableView<FoodItem>();
		
		myMealView.setEditable(false);
		myMealView.setPrefSize(225, 500);
		myMealView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		TableColumn<FoodItem, String> nameCol = new TableColumn<>("My Food Name");
		
		myMealView.getColumns().addAll(nameCol);		
		rightMealPane.setContent(myMealView);
		rightMealPane.getStyleClass().add("mymealpane");
		
		myMealVbox.setAlignment(Pos.CENTER);
		myMealVbox.getChildren().addAll(myMeal, rightMealPane);
		
		return myMealVbox;
	}
	
	private static Pane setBottomButtons() {
		Pane bottomPane = new Pane();		
		bottomPane.setId("bottompane");
		
		Button addtoMeal = new Button("Add To Meal");
		addtoMeal.setLayoutX(5);
		addtoMeal.setLayoutY(10);
		
		Button applyQuery = new Button("Apply Query");
		applyQuery.setLayoutX(350);
		applyQuery.setLayoutY(10);
		
		Button viewMealSummary = new Button("View Meal Summary");
		viewMealSummary.setLayoutX(575);
		viewMealSummary.setLayoutY(10);
		
		Button resetFilter = new Button("Reset Filter");
		resetFilter.setLayoutX(350);
		resetFilter.setLayoutY(47);
		
		Button removeFood = new Button("Remove Food");
		removeFood.setLayoutX(575);
		removeFood.setLayoutY(47);
		
		bottomPane.getChildren().addAll(addtoMeal, applyQuery, viewMealSummary, resetFilter, removeFood);
		return bottomPane;
	}
}
