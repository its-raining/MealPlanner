package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import javafx.collections.FXCollections;

/**
 * This class represents the backend for managing all the operations associated
 * with FoodItems
 * 
 * @author Aaron Hernandez
 * @author Xiao Fei
 * @author Henry Koenig
 */
public class FoodData implements FoodDataADT<FoodItem> {

	// List of all the food items.
	private List<FoodItem> foodItemList;

	// Map of nutrients and their corresponding index
	private HashMap<String, BPTree<Double, FoodItem>> indexes;

	/**
	 * Public constructor
	 */
	public FoodData() {
		this.foodItemList = FXCollections.observableArrayList();
		this.indexes = new HashMap<String, BPTree<Double, FoodItem>>();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skeleton.FoodDataADT#loadFoodItems(java.lang.String)
	 */
	@Override
	public void loadFoodItems(String filePath) {
		foodItemList = FXCollections.observableArrayList();
		indexes = new HashMap<String, BPTree<Double, FoodItem>>();
		
		try {			
			File inFile = new File(filePath);
			Scanner readFile = new Scanner(inFile);
			while (readFile.hasNextLine()) {
				
				String[] currLine = readFile.nextLine().split(",");
				if (currLine.length == 0 || currLine[0].equals("")) {
					// Exits loop at end of data
					break;
				}
				
				// Sets ID and name
				FoodItem tempFood = (new FoodItem(currLine[0], currLine[1]));
				
				// Adds Nutrients by name/value pairs
				for (int i = 2; i < currLine.length; i += 2) {
					tempFood.addNutrient(currLine[i], Double.parseDouble(currLine[i + 1]));
					
					if (i + 1 == currLine.length - 2) {						
						// breaks loops on improper file format
						break;
					}
				}
				this.addFoodItem(tempFood);
			}
			readFile.close();
		} catch (FileNotFoundException e) {
			System.out.println("File not Found");
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skeleton.FoodDataADT#filterByName(java.lang.String)
	 */
	@Override
	public List<FoodItem> filterByName(String substring) {
		List<FoodItem> filteredList = new ArrayList<FoodItem>();
		for (int i = 0; i < foodItemList.size(); i++) {
			
			// add food that contains substring to filtered list
			if (foodItemList.get(i).getName().contains(substring)) {
				filteredList.add(foodItemList.get(i));
			}
		}
		return filteredList;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skeleton.FoodDataADT#filterByNutrients(java.util.List)
	 */
	@Override
	public List<FoodItem> filterByNutrients(List<String> rules) {
		
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
	 */
	@Override
	public void addFoodItem(FoodItem foodItem) {
		
	    for (int i = 0; i < foodItemList.size(); i++) {
	    	
	    	// comparing the food item's name to existing food items' names
	        if (foodItemList.get(i).getName().compareTo(foodItem.getName()) > 0) {
	            foodItemList.add(i, foodItem);
	            return;
	        }
	        
	    }
	    //If greater than all food in list, add to end
	    foodItemList.add(foodItem);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skeleton.FoodDataADT#getAllFoodItems()
	 */
	@Override
	public List<FoodItem> getAllFoodItems() {
		return this.foodItemList;
	}

	@Override
	public void saveFoodItems(String filename) {
		
		try {
			
		    File outFile = new File(filename);
		    
		    if (!outFile.exists()) {
		        outFile.createNewFile();
		    }
		    
		    PrintWriter writer = new PrintWriter(outFile);
		    for (int i = 0; i < foodItemList.size(); i++) {
		    	
		        writer.print(foodItemList.get(i).getID() + ",");
		        writer.print(foodItemList.get(i).getName());
		        
		        String nutrients = "";
		        HashMap<String, Double> nutrientMap = foodItemList.get(i).getNutrients();

		        // FIXME: NEED TO CONVERT THIS TO LIST TO MATCH CSV FORMAT
		        for (Map.Entry<String, Double> entry : nutrientMap.entrySet()) {
		            nutrients += "," + entry.getKey() + "," + entry.getValue();
		        }
		        
		        writer.println(nutrients);
		    }
		    writer.flush();
		    writer.close();
		    
		    
		}
		catch(Exception e) {
		    System.out.println("Hey Idiot");
		}
		
	}
}