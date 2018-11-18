package application;

import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;


public class GUI {

	public static BorderPane setupGUI(BorderPane root) {
		
		root.setTop(setTopMenu());
		root.setBottom(setBottomButtons());
		root.setLeft(setLeftFoodPane());
		
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
	private static ScrollPane setLeftFoodPane() {
		ScrollPane foodListPane = new ScrollPane();
		
		TableView<FoodItem> foodView = new TableView<FoodItem>();
		foodView.setEditable(false);
		foodView.setPrefSize(340, 500);
		foodView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		TableColumn<FoodItem, String> nameCol = new TableColumn<>("Food Name");
		
		foodView.getColumns().addAll(nameCol);
		
		foodListPane.setContent(foodView);
		foodListPane.getStyleClass().add("scrollpane");
		
		return foodListPane;
	}
	
	private static ScrollPane setFilterPane() {
		return null;
	}
	
	private static ScrollPane setRightMealPane() {
		return null;
	}
	
	private static HBox setBottomButtons() {
		HBox hbox = new HBox();
		hbox.setId("hbox-bottom");
		return hbox;
	}
}
