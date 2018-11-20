package application;

import java.io.File;

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
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class GUI {
		
	private static TableView<FoodItem> foodView;
	private static ObservableList<FoodItem> foodList = FXCollections.observableArrayList();
	private static int userId=0;
	private static FoodData foodData = new FoodData();

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
		nameCol.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getName()));
		foodView.getColumns().addAll(nameCol);	
		foodView.setItems(foodList);
		
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
		
		TableColumn<String, String> nutrientCol = new TableColumn<String, String>("Nutrient");
		TableColumn<String, Double> rangeCol = new TableColumn<String, Double>("Range");

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
				
		final TextField addFoodName = new TextField();
		addFoodName.setPromptText("Food Name");
		addFoodName.setLayoutX(5);
		addFoodName.setLayoutY(10);
		addFoodName.setMaxWidth(100);
		
		final TextField addCalorie = new TextField();
		addCalorie.setPromptText("Calorie");
		addCalorie.setLayoutX(113);
		addCalorie.setLayoutY(10);
		addCalorie.setMaxWidth(70);
		
		final TextField addFat = new TextField();
		addFat.setPromptText("Fat");
		addFat.setLayoutX(190);
		addFat.setLayoutY(10);
		addFat.setMaxWidth(70);
		
		final TextField addCarbonHydrate = new TextField();
		addCarbonHydrate.setPromptText("Carbs");
		addCarbonHydrate.setLayoutX(5);
		addCarbonHydrate.setLayoutY(47);
		addCarbonHydrate.setMaxWidth(70);
		
		final TextField addFiber = new TextField();
		addFiber.setPromptText("Fiber");
		addFiber.setLayoutX(78);
		addFiber.setLayoutY(47);
		addFiber.setMaxWidth(70);
		
		final TextField addProtein = new TextField();
		addProtein.setPromptText("Protein");
		addProtein.setLayoutX(265);
		addProtein.setLayoutY(10);
		addProtein.setMaxWidth(75);
		
		Button addtoFoodList = new Button("Add to List");
		addtoFoodList.setLayoutX(151);
		addtoFoodList.setLayoutY(47);
		
		Button addtoMeal = new Button("Add To Meal");
		addtoMeal.setLayoutX(240);
		addtoMeal.setLayoutY(47);
		
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
		
		addtoFoodList.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			 public void handle(ActionEvent e) {
				FoodItem newFood = new FoodItem("" + userId, addFoodName.getText());
				userId++;
				foodList.add(newFood);
			}
		});		
		
		bottomPane.getChildren().addAll(addFoodName, addCalorie, addFat, addCarbonHydrate, addFiber, addProtein, addtoMeal, addtoFoodList, applyQuery, viewMealSummary, resetFilter, removeFood);
		return bottomPane;
	}
}
