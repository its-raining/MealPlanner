/**
 * Filename:   FoodItem.java
 * Project:    Meal Planner
 * Authors:    Henry Koenig 001, Xiao Fei 001, Aaron Hernandez 001
 * 
 * Additional credits: Java 8 API
 *
 * Bugs or other notes: None known.
 * 
 */
package application;

import java.util.HashMap;

/**
 * This class represents a food item and all its properties.
 * 
 * @author Aaron Hernandez
 * @author Xiao Fei
 * @author Henry Koenig
 */
public class FoodItem {
	// The name of the food item.
	private String name;

	// The id of the food item.
	private String id;

	// Map of nutrients and corresponding value.
	private HashMap<String, Double> nutrients;

	/**
	 * Constructor for FoodItem
	 * 
	 * @param name
	 *            name of the food item
	 * @param idunique
	 *            id of the food item
	 */
	public FoodItem(String id, String name) {
		this.id = id;
		this.name = name;
		this.nutrients = new HashMap<String, Double>();
	}

	/**
	 * Gets the name of this food item
	 * 
	 * @return name of this food item
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Gets the unique id of this food item
	 * 
	 * @return the id field of this food item
	 */
	public String getID() {
		return this.id;
	}

	/**
	 * Gets the nutrients of ths food item
	 * 
	 * @return nutrients hash map of this food item
	 */
	public HashMap<String, Double> getNutrients() {
		return this.nutrients;
	}

	/**
	 * Adds a nutrient and its value to this food. If nutrient already exists,
	 * update the value associated with it.
	 * 
	 * @param name
	 *            the name of of the nutrient to add
	 * @param value
	 *            the value of the nutrient associated with name
	 */
	public void addNutrient(String name, double value) {
		this.nutrients.put(name.toLowerCase(), value);
	}

	/**
	 * Returns the value associated with the given nutrient for this food item. If
	 * not present, then returns 0.
	 * 
	 * @param name
	 *            the name of the nutrient to find the value of
	 * @return the value associated with the given nutrient name, 0 if name is not
	 *         present
	 */
	public double getNutrientValue(String name) {

		// check if given nutrient's name exists
		if (this.nutrients.containsKey(name)) {
			return this.nutrients.get(name);
		}

		return 0;
	}

}