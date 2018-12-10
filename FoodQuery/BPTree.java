/**
 * Filename:   BPTree.java
 * Project:    Meal Planner
 * Authors:    Aaron Hernandez 001, Henry Koenig 001, Xiao Fei 001
 *
 * Semester:   Fall 2018
 * Course:     CS400
 * 
 * Due Date:   Dec 12th
 * Version:    1.0
 * 
 * Credits:    N/A
 * 
 * Bugs:       N/A
 */

package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Implementation of a B+ tree to allow efficient access to many different
 * indexes of a large data set. BPTree objects are created for each type of
 * index needed by the program. BPTrees provide an efficient range search as
 * compared to other types of data structures due to the ability to perform
 * log_m N lookups and linear in-order traversals of the data items.
 * 
 * @author Aaron Hernandez
 * @author Xiao Fei
 * @author Henry Koenig
 *
 * @param <K>
 *            key - expect a string that is the type of id for each item
 * @param <V>
 *            value - expect a user-defined type that stores all data for a food
 *            item
 */
public class BPTree<K extends Comparable<K>, V> implements BPTreeADT<K, V> {

	// Root of the tree
	private Node root;

	// Branching factor is the number of children nodes
	// for internal nodes of the tree
	private int branchingFactor;

	/**
	 * Public constructor
	 * 
	 * @param branchingFactor
	 *            The branching factor of the BPTree
	 */
	public BPTree(int branchingFactor) {
		if (branchingFactor <= 2) {
			throw new IllegalArgumentException("Illegal branching factor: " + branchingFactor);
		}

		this.branchingFactor = branchingFactor;
		this.root = new LeafNode();
	}

	/**
	 * Inserts the key and value in the appropriate nodes in the tree
	 * 
	 * Note: key-value pairs with duplicate keys can be inserted into the tree.
	 * 
	 * @param key
	 *            The K key to insert into the BPTree
	 * @param value
	 *            The V value to insert into the BPTree
	 */
	@Override
	public void insert(K key, V value) {
		this.root.insert(key, value);
		if (this.root.isOverflow()) {
			InternalNode newRoot = new InternalNode();
			Node newChild = this.root.split();
			K rootKey = this.root.getFirstLeafKey();
			newRoot.children.add(newChild);
			newRoot.children.add(this.root);
			newRoot.keys.add(rootKey);
			this.root = newRoot;
		}
	}

	/**
	 * Gets the values that satisfy the given range search arguments.
	 * 
	 * Value of comparator can be one of these: "<=", "==", ">="
	 * 
	 * Example: If given key = 2.5 and comparator = ">=": return all the values with
	 * the corresponding keys >= 2.5
	 * 
	 * If key is null or not found, return empty list. If comparator is null, empty,
	 * or not according to required form, return empty list.
	 * 
	 * @param key
	 *            to be searched
	 * @param comparator
	 *            is a string
	 * @return list of values that are the result of the range search; if nothing
	 *         found, return empty list
	 */
	@Override
	public List<V> rangeSearch(K key, String comparator) {
		if (!comparator.contentEquals(">=") && !comparator.contentEquals("==") && !comparator.contentEquals("<="))
			return new ArrayList<V>();

		return this.root.rangeSearch(key, comparator);
	}

	/**
	 * Returns a string representation for the tree This method is provided to
	 * students in the implementation.
	 * 
	 * @return a string representation
	 */
	@Override
	public String toString() {

		Queue<List<Node>> queue = new LinkedList<List<Node>>();
		queue.add(Arrays.asList(root));

		StringBuilder sb = new StringBuilder();
		while (!queue.isEmpty()) {

			Queue<List<Node>> nextQueue = new LinkedList<List<Node>>();
			while (!queue.isEmpty()) {

				List<Node> nodes = queue.remove();
				sb.append('{');

				Iterator<Node> it = nodes.iterator();
				while (it.hasNext()) {

					Node node = it.next();
					sb.append(node.toString());

					if (it.hasNext())
						sb.append(", ");

					if (node instanceof BPTree.InternalNode)
						nextQueue.add(((InternalNode) node).children);
				}

				sb.append('}');
				if (!queue.isEmpty())
					sb.append(", ");
				else {
					sb.append('\n');
				}
			}
			queue = nextQueue;
		}
		return sb.toString();
	}

	/**
	 * This abstract class represents any type of node in the tree This class is a
	 * super class of the LeafNode and InternalNode types.
	 * 
	 * @author sapan
	 * @author Aaron Hernandez
	 * @author Xiao Fei
	 * @author Henry Koenig
	 */
	private abstract class Node {

		// List of keys
		List<K> keys;

		/**
		 * Package constructor
		 */
		Node() {
			this.keys = new ArrayList<K>();
		}

		/**
		 * Inserts key and value in the appropriate leaf node and balances the tree if
		 * required by splitting
		 * 
		 * @param key
		 *            The K key to insert
		 * @param value
		 *            The V value to insert
		 */
		abstract void insert(K key, V value);

		/**
		 * Gets the first leaf key of the tree
		 * 
		 * @return the first leaf key
		 */
		abstract K getFirstLeafKey();

		/**
		 * Gets the new sibling created after splitting the node
		 * 
		 * @return the new sibling Node after splitting
		 */
		abstract Node split();

		/*
		 * (non-Javadoc)
		 * 
		 * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
		 */
		abstract List<V> rangeSearch(K key, String comparator);

		/**
		 * Checks to see if the current node has overflowed, which is useful when
		 * splitting.
		 * 
		 * @return boolean true if the current node has overflowed, false otherwise
		 */
		abstract boolean isOverflow();

		/**
		 * Gets a String representation of the keys
		 * 
		 * @return a String representing the list of keys in the current Node.
		 */
		public String toString() {
			return keys.toString();
		}

	} // End of abstract class Node

	/**
	 * This class represents an internal node of the tree. This class is a concrete
	 * sub class of the abstract Node class and provides implementation of the
	 * operations required for internal (non-leaf) nodes.
	 * 
	 * @author sapan
	 * @author Aaron Hernandez
	 * @author Xiao Fei
	 * @author Henry Koenig
	 */
	private class InternalNode extends Node {

		// List of children nodes
		List<Node> children;

		/**
		 * Package constructor
		 */
		InternalNode() {
			super();
			this.children = new ArrayList<Node>();
		}

		/**
		 * @see BPTree.Node#getFirstLeafKey()
		 */
		K getFirstLeafKey() {

			return this.children.get(0).getFirstLeafKey();
		}

		/**
		 * @see BPTree.Node#isOverflow()
		 */
		boolean isOverflow() {

			if (this.keys.size() >= branchingFactor) {
				return true;
			}

			return false;
		}

		/**
		 * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
		 */
		void insert(K key, V value) {

			// checks proper index to insert key-value pair for children
			boolean hasInserted = false;
			int i = 0;
			for (i = 0; i < this.keys.size(); i++) {

				// if key to insert <= the current key index in the current Node
				if (key.compareTo(this.keys.get(i)) <= 0) {

					// insert into the child's left subtree
					this.children.get(i).insert(key, value);
					hasInserted = true;
					break;

				}

			}

			// if not inserted, insert the key-value pair for child at the end
			if (!hasInserted) {
				this.children.get(i).insert(key, value);
			}

			// if overflow occurs
			if (this.children.get(i).isOverflow()) {

				// split children and get child's first leaf key
				Node newChild = this.children.get(i).split();
				K childKey = this.children.get(i).getFirstLeafKey();

				// check proper index to insert into internal node's key list
				boolean keyAdded = false;
				for (int j = 0; j < this.keys.size(); j++) {

					// if the first leaf key < the current key index of current Node
					if (childKey.compareTo(this.keys.get(j)) < 0) {

						// add the first leaf key of the child at this index
						this.keys.add(j, childKey);
						keyAdded = true;
						break;

					}

				}

				// if not inserted, insert at the end of internal node's key list
				if (!keyAdded) {
					this.keys.add(childKey);
				}

				// Add child
				this.children.add(i, newChild);
			}

		}

		/**
		 * @see BPTree.Node#split()
		 */
		Node split() {

			// create a new sibling internal node
			InternalNode newInternalNode = new InternalNode();
			int size = this.children.size();

			// move the first half of the children into newInternalNode
			for (int i = 0; i < size / 2; i++) {
				newInternalNode.children.add(this.children.remove(0));
			}

			// moves the first half of keys into newInternalNode
			for (int j = 0; j < branchingFactor / 2; j++) {
				newInternalNode.keys.add(this.keys.remove(0));
			}

			// checks for even or odd branching factor and handles accordingly
			if (branchingFactor % 2 == 1) {
				this.keys.remove(0);
			} else {
				newInternalNode.children.add(this.children.remove(0));
				this.keys.remove(0);
			}

			return newInternalNode;
		}

		/**
		 * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
		 */
		List<V> rangeSearch(K key, String comparator) {

			List<V> comparedValues = new ArrayList<V>();

			// indices for the middle, left bound, and right bound for binary search
			int mid = (this.keys.size() - 1) / 2;
			int left = 0;
			int right = this.keys.size() - 1;

			// checks the comparator
			switch (comparator) {
			case "<=":

				// applies binary search for half the keys size
				for (int i = 0; i <= (this.keys.size() / 2); i++) {

					// if key to search is >= to current key at mid
					if (key.compareTo(this.keys.get(mid)) >= 0) {

						// search right
						left = mid + 1;
						mid = (((mid + 1) + right) / 2);

					} else {

						// search left
						right = mid - 1;
						mid = (left + (mid - 1)) / 2;

					}

				}

				// once binary search ends, if key to search is >= key at last index found
				if (key.compareTo(this.keys.get(mid)) >= 0) {

					// go into right subtree
					comparedValues.addAll(this.children.get(mid + 1).rangeSearch(key, comparator));
				} else {

					// go into left subtree
					comparedValues.addAll(this.children.get(mid).rangeSearch(key, comparator));
				}

				break;

			case ">=":

				// applies binary search for half the keys size
				for (int i = 0; i <= (this.keys.size() / 2); i++) {

					// if key to search is > current key at mid
					if (key.compareTo(this.keys.get(mid)) > 0) {

						// search right
						left = mid + 1;
						mid = (((mid + 1) + right) / 2);

					} else {

						// search left
						right = mid - 1;
						mid = (left + (mid - 1)) / 2;

					}

				}

				// once binary search ends, if key to search <= key at last index found
				if (key.compareTo(this.keys.get(mid)) <= 0) {

					// go into left subtree
					comparedValues.addAll(this.children.get(mid).rangeSearch(key, comparator));

				} else {

					// go into right subtree
					comparedValues.addAll(this.children.get(mid + 1).rangeSearch(key, comparator));
				}

				break;

			case "==":

				// applies binary search for half the keys size
				for (int i = 0; i <= (this.keys.size() / 2); i++) {

					// if key to search is > current key at mid
					if (key.compareTo(this.keys.get(mid)) > 0) {

						// search right
						left = mid + 1;
						mid = (((mid + 1) + right) / 2);

					} else {

						// search left
						right = mid - 1;
						mid = (left + (mid - 1)) / 2;

					}

				}

				// once binary search ends, if key to search <= key at last index found
				if (key.compareTo(this.keys.get(mid)) <= 0) {

					// go into left subtree
					comparedValues.addAll(this.children.get(mid).rangeSearch(key, comparator));
				} else {

					// go into right subtree
					comparedValues.addAll(this.children.get(mid + 1).rangeSearch(key, comparator));
				}

				break;
			}

			return comparedValues;

		}

	} // End of class InternalNode

	/**
	 * This class represents a leaf node of the tree. This class is a concrete sub
	 * class of the abstract Node class and provides implementation of the
	 * operations that required for leaf nodes.
	 * 
	 * @author sapan
	 * @author Aaron Hernandez
	 * @author Xiao Fei
	 * @author Henry Koenig
	 */
	private class LeafNode extends Node {

		// List of values
		List<V> values;

		// Reference to the next leaf node
		LeafNode next;

		// Reference to the previous leaf node
		LeafNode previous;

		/**
		 * Package constructor
		 */
		LeafNode() {
			super();
			this.values = new ArrayList<V>();
			this.next = null;
			this.previous = null;
		}

		/**
		 * @see BPTree.Node#getFirstLeafKey()
		 */
		K getFirstLeafKey() {
			return this.keys.get(0);
		}

		/**
		 * @see BPTree.Node#isOverflow()
		 */
		boolean isOverflow() {
			if (this.keys.size() >= branchingFactor) {
				return true;
			}

			return false;
		}

		/**
		 * Only concerned with adding leaf node
		 * 
		 * @see BPTree.Node#insert(Comparable, Object)
		 */
		void insert(K key, V value) {

			// checks for proper index to insert key-value pair in this leaf node
			boolean hasInserted = false;
			for (int i = 0; i < this.keys.size(); i++) {

				// if key to insert is < current key at index i
				if (key.compareTo(this.keys.get(i)) < 0) {

					// add the key-value pair at index i of current node keys and values lists
					this.keys.add(i, key);
					this.values.add(i, value);
					hasInserted = true;
					break;

				}
			}

			// if key-value wasn't inserted, add the key-value pair the end of the lists
			if (!hasInserted) {
				this.keys.add(key);
				this.values.add(value);
			}

		}

		/**
		 * @see BPTree.Node#split()
		 */
		Node split() {

			// gets a new leaf node
			LeafNode newLeaf = new LeafNode();

			// transfers over the first half of the keys-value pairs to newLeaf
			for (int i = 0; i < branchingFactor / 2; i++) {
				newLeaf.insert(this.keys.remove(0), this.values.remove(0));
			}

			// relinks newLeaf and the current leaf node
			newLeaf.next = this;
			newLeaf.previous = this.previous;
			if (this.previous != null) {
				this.previous.next = newLeaf;
			}
			this.previous = newLeaf;

			return newLeaf;
		}

		/**
		 * @see BPTree.Node#rangeSearch(Comparable, String)
		 */
		List<V> rangeSearch(K key, String comparator) {

			List<V> comparedValues = new ArrayList<V>();

			// check the comparator
			switch (comparator) {
			case "<=":

				// if this leaf node has a previously linked leaf node, rangeSearch on previous
				if (this.previous != null) {
					comparedValues.addAll(this.previous.rangeSearch(key, comparator));
				}

				// check the keys in the current node to determine insertion
				for (int i = 0; i < this.keys.size(); i++) {
					if (this.keys.get(i).compareTo(key) <= 0) {
						comparedValues.add(this.values.get(i));
					}
				}

				break;

			case ">=":

				// check the keys in the current node to determine insertion
				for (int j = 0; j < this.keys.size(); j++) {

					if (this.keys.get(j).compareTo(key) >= 0) {
						comparedValues.add(this.values.get(j));
					}
				}

				// if this leaf node has a next linked leaf node, rangeSearch on next
				if (this.next != null) {
					comparedValues.addAll(this.next.rangeSearch(key, comparator));
				}

				break;

			case "==":

				// iterate over keys in current leaf node
				for (int j = 0; j < this.keys.size(); j++) {

					// if a key match exists, add to values list
					if (this.keys.get(j).compareTo(key) == 0) {

						comparedValues.add(this.values.get(j));

					} else if (this.keys.get(j).compareTo(key) > 0) {

						// if found a current node key > key we're searching
						return comparedValues;

					}
				}

				// if this leaf node has a next linked leaf node, rangeSearch on next
				if (this.next != null) {
					comparedValues.addAll(this.next.rangeSearch(key, comparator));
				}

				break;
			}

			return comparedValues;
		}

	} // End of class LeafNode
} // End of class BPTree
