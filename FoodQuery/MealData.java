/**
 * Filename:   MealData.java
 * Project:    Meal Planner
 * Authors:    Henry Koenig, Xiao Fei, Aaron Hernandez
 * 
 * Additional credits: Java 8 API, Javafx API
 *
 * Bugs or other notes: The "View Meal Summary" button in GUI rarely creates an almost
 * entirely black window with no data displayed. Testing hasn't been able to recreate this problem 
 * consistently, and we are lead to believe that it may be a computer specific error, or an issue with the Javafx 
 * back end and not our code.
 * 
 */
package application;

import java.util.HashMap;
import java.util.List;
import javafx.collections.FXCollections;

/**
 * This class represents the back-end for managing all operations associated with
 * the foodItems in a meal
 * 
 * @author Aaron Hernandez
 * @author Xiao Fei
 * @author Henry Koenig
 */
public class MealData {
	
	// List of all the foodItems in this meal.
	private List<FoodItem> mealList;
	
	/**
	 * The public constructor for MealData
	 */
	public MealData() {
		this.mealList = FXCollections.observableArrayList();
	}
	
	/**
	 * The method adds given mealItem to mealList
	 * @param mealItem
	 */
	public void addFoodItem(FoodItem mealItem) {
	    mealList.add(mealItem);
	}
	
	/**
	 * The method removes given mealItem from mealList
	 * @param mealItem
	 */
	public void removeFoodItem(FoodItem mealItem) {
	    mealList.remove(mealItem);
	}
	
	/**
	 * Simply returns a reference to mealList
	 * @return mealList field
	 */
	public List<FoodItem> getMealList(){
		return this.mealList;
	}
	
	/**
	 * The method analyzes nutrients of the FoodItems within this MealData. It then returns the 
	 * total values for each nutrient
	 * 
	 * @return a map with nutrients and their value
	 */
	public HashMap<String, Double> getAnalysis(){
	    //create a HashMap of nutrients and initialize all nutrient values to 0
		HashMap<String, Double> mealAnalysis = new HashMap<String, Double>();
		
		double calories = 0;
		double fat = 0;
		double carbs = 0;
		double protein = 0;
		double fiber = 0;
		
		//add the values for each nutrient of each FoodItem
		for (int i = 0; i < mealList.size(); i++) {
			calories += mealList.get(i).getNutrients().get("calories");
			fat += mealList.get(i).getNutrients().get("fat");
			carbs += mealList.get(i).getNutrients().get("carbohydrate");
			protein += mealList.get(i).getNutrients().get("protein");
			fiber += mealList.get(i).getNutrients().get("fiber");
		}
		
		//put nutrient and its value to a hashmap
		mealAnalysis.put("calories", calories);
		mealAnalysis.put("fat", fat);
		mealAnalysis.put("carbohydrate", carbs);
		mealAnalysis.put("protein", protein);
		mealAnalysis.put("fiber", fiber);
		
		return mealAnalysis;
	}
}
