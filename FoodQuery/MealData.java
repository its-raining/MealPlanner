package application;

import java.util.HashMap;
import java.util.List;

import javafx.collections.FXCollections;

public class MealData {
	private List<FoodItem> mealList;
	
	public MealData() {
		this.mealList = FXCollections.observableArrayList();
	}
	
	public void addFoodItem(FoodItem mealItem) {
	    mealList.add(mealItem);
	}
	
	public void removeFoodItem(FoodItem mealItem) {
	    mealList.remove(mealItem);
	}
	
	public List<FoodItem> getMealList(){
		return this.mealList;
	}
	
	public HashMap<String, Double> getAnalysis(){
		HashMap<String, Double> mealAnalysis = new HashMap<String, Double>();
		//add calories
		double calories = 0;
		double fat = 0;
		double carbs = 0;
		double protein = 0;
		double fiber = 0;

		for (int i = 0; i < mealList.size(); i++) {
			calories += mealList.get(i).getNutrients().get("calories");
			fat += mealList.get(i).getNutrients().get("fat");
			carbs += mealList.get(i).getNutrients().get("carbonhydrate");
			protein += mealList.get(i).getNutrients().get("protein");
			fiber += mealList.get(i).getNutrients().get("fiber");
		}
		mealAnalysis.put("calories", calories);
		mealAnalysis.put("fat", fat);
		mealAnalysis.put("carbonhydrate", carbs);
		mealAnalysis.put("protein", protein);
		mealAnalysis.put("fiber", fiber);
		
		return mealAnalysis;
	}
}
