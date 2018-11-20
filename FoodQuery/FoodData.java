package application;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import javafx.collections.FXCollections;

/**
 * This class represents the backend for managing all 
 * the operations associated with FoodItems
 * 
 * @author sapan (sapan@cs.wisc.edu)
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
        this.foodItemList = new ArrayList<FoodItem>();
        this.indexes = new HashMap<String, BPTree<Double, FoodItem>>();
    }
    
    
    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#loadFoodItems(java.lang.String)
     */
    @Override
    public void loadFoodItems(String filePath) {
        foodItemList = FXCollections.observableArrayList();
        try {
    	File inFile = new File(filePath);
    	Scanner readFile = new Scanner(inFile);
    	int lineNum = 0;//Index to modify in foodItemList
    	while (readFile.hasNextLine()) {
    	    String[] currLine = readFile.nextLine().split(",");
    	    if(currLine.length == 0 || currLine[0].equals("")) {
    	        break;//Exits loop at end of data
    	    }
    	    foodItemList.add(new FoodItem(currLine[0], currLine[1]));//Sets ID and name
    	    for (int i = 2; i < currLine.length; i+= 2) {//Adds Nutrients by name/value pairs
    	        foodItemList.get(lineNum).addNutrient(currLine[i], Double.parseDouble(currLine[i+1]));
    	        if (i + 1 == currLine.length - 2) {
    	            break;//breaks loops on improper files
    	        }
    	    }
    	   lineNum++; 
    	} 	
    	readFile.close();
        }
        catch(FileNotFoundException e) {
            System.out.println("File not Found");
        }    
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByName(java.lang.String)
     */
    @Override
    public List<FoodItem> filterByName(String substring) {
        // TODO : Complete
        return null;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#filterByNutrients(java.util.List)
     */
    @Override
    public List<FoodItem> filterByNutrients(List<String> rules) {
        // TODO : Complete
        return null;
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#addFoodItem(skeleton.FoodItem)
     */
    @Override
    public void addFoodItem(FoodItem foodItem) {
        // TODO : Complete
    }

    /*
     * (non-Javadoc)
     * @see skeleton.FoodDataADT#getAllFoodItems()
     */
    @Override
    public List<FoodItem> getAllFoodItems() {
        return this.foodItemList;
    }


	@Override
	public void saveFoodItems(String filename) {
		// TODO Auto-generated method stub
		
	}
}