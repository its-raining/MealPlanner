package application;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.io.File;
import java.io.PrintWriter;
import java.io.FileNotFoundException;
import java.io.IOException;
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
		
		//Create all of our BPtrees
		indexes.put("calories", new BPTree<Double, FoodItem>(3));
		indexes.put("fat", new BPTree<Double, FoodItem>(3));
		indexes.put("carbohydrate", new BPTree<Double, FoodItem>(3));
		indexes.put("fiber", new BPTree<Double, FoodItem>(3));
		indexes.put("protein", new BPTree<Double, FoodItem>(3));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skeleton.FoodDataADT#loadFoodItems(java.lang.String)
	 */
	@Override
	public void loadFoodItems(String filePath) {		
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
		
		if (rules == null || rules.isEmpty()) {
			return this.getAllFoodItems();
		}
		
	    //Holds the results of each rule being applied individually
	    List <List<FoodItem>> rulesResults = new ArrayList<List<FoodItem>>();
	    
	    //range searches for each rule, and adds the result to rulesResults
	    for (int i = 0; i < rules.size(); i++) {
	        String[] currRule = rules.get(i).split(" ");
	        rulesResults.add(indexes.get(currRule[0]).rangeSearch(Double.parseDouble(currRule[2]), currRule[1]));
	    }
	    
	    System.out.println(indexes.get("calories").toString());
	    
	    //The list that is the intersection of the results
	    List<FoodItem> filteredFoods = rulesResults.get(0);
	    
	    
	    for (int i = 1; i < rulesResults.size(); i++) {
	        filteredFoods.retainAll(rulesResults.get(i));
	    }
	    
		return filteredFoods;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
	 */
	@Override
	public void addFoodItem(FoodItem foodItem) {
		boolean foodAdded = false;
	    for (int i = 0; i < foodItemList.size(); i++) {
	    	
	    	// comparing the food item's name to existing food items' names
	        if (foodItemList.get(i).getName().compareTo(foodItem.getName()) > 0) {
	            foodItemList.add(i, foodItem);
	            foodAdded = true;
	            break;
	        } 
	        
	    }
	    if(!foodAdded) {
	    	foodItemList.add(foodItem);
	    }
	    //If greater than all food in list, add to end
	    //foodItemList.add(foodItem);
	    
	    //Lastly, add to all BPtrees
	    indexes.get("calories").insert(foodItem.getNutrientValue("calories"), foodItem);
	    indexes.get("fat").insert(foodItem.getNutrientValue("fat"), foodItem);
	    indexes.get("carbohydrate").insert(foodItem.getNutrientValue("carbohydrate"), foodItem);
	    indexes.get("fiber").insert(foodItem.getNutrientValue("fiber"), foodItem);
	    indexes.get("protein").insert(foodItem.getNutrientValue("protein"), foodItem);
	    
	    //System.out.println(foodItem.getNutrientValue("calories"));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see skeleton.FoodDataADT#getAllFoodItems()
	 */
	@Override
	public List<FoodItem> getAllFoodItems() {
		return new ArrayList<FoodItem>(this.foodItemList);
	}

	@Override
	public void saveFoodItems(String filename) {
		
		try {
			
		    File outFile = new File(filename);
		    
		    //Prevent errors later by creating a new file if outFile is not found
		    if (!outFile.exists()) {
		        outFile.createNewFile();
		    }
		    
		    PrintWriter writer = new PrintWriter(outFile);
		    
		    //Writes a new line in proper format for every food item
		    for (int i = 0; i < foodItemList.size(); i++) {	
		        writer.print(foodItemList.get(i).getID() + ",");
		        writer.print(foodItemList.get(i).getName() + ",");
		        
		        HashMap<String, Double> nutrientMap = foodItemList.get(i).getNutrients();
		        
		        writer.print("calories," + nutrientMap.get("calories") + ",");
		        writer.print("fat," + nutrientMap.get("fat") + ",");
		        writer.print("carbohydrate," + nutrientMap.get("carbohydrate") + ",");
		        writer.print("fiber," + nutrientMap.get("fiber") + ",");
		        writer.println("protein," + nutrientMap.get("protein"));
		      
		    }
		    writer.flush();
		    writer.close();
		    
		}
		catch (IOException e) {
		    System.out.println(e.getMessage());
		}
		
		
		
	}
}