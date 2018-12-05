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
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This class represents a food item add form that will pop-up for users to enter new food's info
 * 
 * @author Aaron Hernandez
 * @author Xiao Fei
 * @author Henry Koenig
 */
public class FilterRulesForm {
	
	// ok, cancel and clear buttons
	private Button okayButton;
	private Button cancelButton;
	
	private ComboBox<String> rule;
	private ComboBox<String> comparator;
	private TextField value;
	
	/**
	 * The public method called to set up the stage, scene and everything else in the form to add a food item
	 * @param foodList
	 * @param foodData
	 * @param foodComparator
	 */
	public void start(ObservableList<String> filterList) {
        Stage stage = new Stage();
        stage.setTitle("Add New Rule");
        
        // border pane with center pane and bottom pane
        BorderPane borderPane = new BorderPane();
        
        VBox vBox = new VBox();
        
        // v-box's alignment, spacing and padding setting
     	vBox.setAlignment(Pos.CENTER);
     	vBox.setSpacing(30);
     	vBox.setPadding(new Insets(10,10,10,10));
        borderPane.setCenter(vBox);
        
        HBox hBox = new HBox();
        
        // H-box's alignment, spacing and padding setting
     	hBox.setAlignment(Pos.CENTER);
     	hBox.setSpacing(30);
     	hBox.setPadding(new Insets(10,10,10,10));
        borderPane.setBottom(hBox);
        
        // center pane contains 2 combo boxes and 1 text field
        this.rule = new ComboBox<String>();
        rule.getItems().addAll("name", "calories", "fat", "carbohydrate", "fiber", "protein");
        rule.setPromptText("Rule Type");
        
        this.comparator = new ComboBox<String>();
        comparator.getItems().addAll("<=", ">=", "==");
        comparator.setPromptText("Comparator");
        
        this.value = new TextField();
        value.setMaxWidth(150);
        vBox.getChildren().addAll(rule, comparator, value);
        
        // bottom pane for add new rule pane
        okayButton = new Button("OK");
        cancelButton = new Button("Cancel");
        hBox.getChildren().addAll(okayButton, cancelButton);
        
        handleOK(stage, filterList);
        handleCancel(stage);
        handleRuleName();
        
        // set up a new scene for add-form
        Scene scene = new Scene(borderPane,400,300);
        stage.setScene(scene);
        stage.show();
	}
	
	public void handleRuleName() {
		rule.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent e) {
				
				String selectedRule = rule.getSelectionModel().getSelectedItem();
				if (selectedRule != null && selectedRule.trim().equals("name")) {
					comparator.setDisable(true);
					
				} else {
					comparator.setDisable(false);
				}
			}
		});
	}
	
	/**
	 * The functionality of cancel button
	 * @param cancel
	 * @param stage
	 */
	public void handleCancel(Stage stage) {
		cancelButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				stage.close();
			}
		});
	}
	
	/**
	 * The functionality of ok button
	 * @param OK button
	 * @param stage
	 * @param foodList
	 * @param foodData
	 * @param foodComparator
	 */
	public void handleOK(Stage stage, ObservableList<String> filterList) {
		okayButton.setOnAction(new EventHandler<ActionEvent>(){
			@Override
			public void handle(ActionEvent e) {
				
				try {
					
					String selectedRule = rule.getSelectionModel().getSelectedItem();
					String selectedValue = comparator.getSelectionModel().getSelectedItem();
					if (selectedRule != null) {
					    
						if (selectedRule.trim().equals("name")) {
							
							if (value.getText().trim().equals("")) {
								 throw new Exception("Name field is required");
							}
							
							// for name rule, only adds the name entered 
							filterList.add("name: " + value.getText());
							stage.close();
							return;
						   
						} else {
							
							if (selectedValue != null) {
								
								// check for NumberFormatException only
								Double.parseDouble(value.getText());
								
							} else {
								
								// popup window "please select a comparator"
								throw new Exception("Please select a comparator");
							}
							
						}
						
					} else {
						
						// popup window "please select a rule"
						throw new Exception("Please select a rule");
					}
					
					filterList.add(selectedRule + " " + selectedValue + " " + value.getText().trim());
				    stage.close();
				    
				} catch (NumberFormatException f) {
					
					// catch exception and pop-up an alert if user did not enter double for nutrient's value
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
					alert.setContentText(n.getMessage());

					alert.showAndWait();
				}
				
			}
		});
	}
}
