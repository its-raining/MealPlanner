package application;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Random;

/**
 * Implementation of a B+ tree to allow efficient access to
 * many different indexes of a large data set. 
 * BPTree objects are created for each type of index
 * needed by the program.  BPTrees provide an efficient
 * range search as compared to other types of data structures
 * due to the ability to perform log_m N lookups and
 * linear in-order traversals of the data items.
 * 
 * @author sapan (sapan@cs.wisc.edu)
 *
 * @param <K> key - expect a string that is the type of id for each item
 * @param <V> value - expect a user-defined type that stores all data for a food item
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
     */
    public BPTree(int branchingFactor) {
        if (branchingFactor <= 2) {
            throw new IllegalArgumentException(
               "Illegal branching factor: " + branchingFactor);
        }
        
        this.branchingFactor = branchingFactor;
        this.root = new LeafNode();
    }
    
    
    /*
     * (non-Javadoc)
     * @see BPTreeADT#insert(java.lang.Object, java.lang.Object)
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
    
    
    /*
     * (non-Javadoc)
     * @see BPTreeADT#rangeSearch(java.lang.Object, java.lang.String)
     */
    @Override
    public List<V> rangeSearch(K key, String comparator) {
        if (!comparator.contentEquals(">=") && 
            !comparator.contentEquals("==") && 
            !comparator.contentEquals("<=") )
            return new ArrayList<V>();
        
        return this.root.rangeSearch(key, comparator);
    }
    
    
    /*
     * (non-Javadoc)
     * @see java.lang.Object#toString()
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
     * This abstract class represents any type of node in the tree
     * This class is a super class of the LeafNode and InternalNode types.
     * 
     * @author sapan
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
         * Inserts key and value in the appropriate leaf node 
         * and balances the tree if required by splitting
         *  
         * @param key
         * @param value
         */
        abstract void insert(K key, V value);

        /**
         * Gets the first leaf key of the tree
         * 
         * @return key
         */
        abstract K getFirstLeafKey();
        
        /**
         * Gets the new sibling created after splitting the node
         * 
         * @return Node
         */
        abstract Node split();
        
        /*
         * (non-Javadoc)
         * @see BPTree#rangeSearch(java.lang.Object, java.lang.String)
         */
        abstract List<V> rangeSearch(K key, String comparator);

        /**
         * 
         * @return boolean
         */
        abstract boolean isOverflow();
        
        public String toString() {
            return keys.toString();
        }
    
    } // End of abstract class Node
    
    /**
     * This class represents an internal node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations
     * required for internal (non-leaf) nodes.
     * 
     * @author sapan
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
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
      
            return this.children.get(0).getFirstLeafKey();
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#isOverflow()
         */
        boolean isOverflow() {
        	
        	if (this.keys.size() >= branchingFactor) {
            	return true;
            }
            
            return false;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#insert(java.lang.Comparable, java.lang.Object)
         */
        void insert(K key, V value) {
        	
        	// checks proper index to insert key-value pair for children
        	boolean hasInserted = false;
        	int i = 0;
        	for (i = 0; i < this.keys.size(); i++) {
            	if (key.compareTo(this.keys.get(i)) < 0) {
            		this.children.get(i).insert(key, value);
            		hasInserted = true;
            		break;
            	}
            }
        	
        	// if not inserted, insert the key-value pair for child at the end
        	 if(!hasInserted) {
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
        			 if (childKey.compareTo(this.keys.get(j)) < 0) {
        				 this.keys.add(j, childKey);
        				 keyAdded = true;
        				 break;
        			 }
        		 }
        		 
        		 // if not inserted, insert at the end of internal node's key list
        		 if (!keyAdded) {
        			 this.keys.add(childKey);
        		 }
        		 
        		 // TODO: Add child
        		 this.children.add(i, newChild);
        	 }
            
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#split()
         */
        Node split() {
            // TODO : Complete
        	InternalNode newInternalNode = new InternalNode();
        	int size = this.children.size();
        	
        	for (int i = 0; i < size / 2; i++) {
        		newInternalNode.children.add(this.children.remove(0));
        	}
        	
        	for (int j = 0; j < branchingFactor / 2; j++) {
        		newInternalNode.keys.add(this.keys.remove(0));
        	}
        	
        	if (branchingFactor % 2 == 1) {
        		this.keys.remove(0);
        	} else {
        		newInternalNode.children.add(this.children.remove(0));
        		this.keys.remove(0);
        	}
        	
            return newInternalNode;
        }
        
        /**
         * (non-Javadoc)
         * @see BPTree.Node#rangeSearch(java.lang.Comparable, java.lang.String)
         */
        List<V> rangeSearch(K key, String comparator) {
        	
        	List<V> comparedValues = new ArrayList<V>();
        	
			switch (comparator) {
			case "<=":

				for (int i = 0; i < this.keys.size(); i++) {
					if (this.keys.get(i).compareTo(key) <= 0) {
						comparedValues.addAll(this.children.get(i).rangeSearch(key, comparator));	
					} 
					
					if (this.keys.get(i).compareTo(key) == 0) {
						comparedValues.addAll(this.children.get(i+1).rangeSearch(key, comparator));	
						break;
					} 
					
					if (this.keys.get(i).compareTo(key) > 0) {
						comparedValues.addAll(this.children.get(i).rangeSearch(key, comparator));
						break;
					}

				}
				
				if (this.keys.get(this.keys.size()-1).compareTo(key) < 0) {
					comparedValues.addAll(this.children.get(this.keys.size()).rangeSearch(key, comparator));
				}
				break;

			case ">=":
				
				if (this.keys.get(0).compareTo(key) > 0) {
					comparedValues.addAll(this.children.get(0).rangeSearch(key, comparator));
				}
				
				for (int i = 0; i < this.keys.size(); i++) {
					if (this.keys.get(i).compareTo(key) >= 0) {
						comparedValues.addAll(this.children.get(i+1).rangeSearch(key, comparator));
					} 
					
					if (this.keys.get(i).compareTo(key) < 0) {
						comparedValues.addAll(this.children.get(i+1).rangeSearch(key, comparator));	
					}
				}
				
				break;

			case "==":
				for (int i = 0; i < this.keys.size(); i++) {
					if (key.compareTo(this.keys.get(i)) < 0) {
						comparedValues.addAll(this.children.get(i).rangeSearch(key, comparator));
						break;
					}
					
					if (key.compareTo(this.keys.get(i)) == 0) {
						comparedValues.addAll(this.children.get(i+1).rangeSearch(key, comparator));
						break;
					}
				}
				
				if (key.compareTo(this.keys.get(this.keys.size()-1)) > 0) {
					comparedValues.addAll(this.children.get(this.keys.size()).rangeSearch(key, comparator));
				}
				
				break;
			}

			return comparedValues;
		}
    } // End of class InternalNode
    
    
    /**
     * This class represents a leaf node of the tree.
     * This class is a concrete sub class of the abstract Node class
     * and provides implementation of the operations that
     * required for leaf nodes.
     * 
     * @author sapan
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
         * (non-Javadoc)
         * @see BPTree.Node#getFirstLeafKey()
         */
        K getFirstLeafKey() {
            return this.keys.get(0);
        }
        
        /**
         * (non-Javadoc)
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
         * (non-Javadoc)
         * @see BPTree.Node#insert(Comparable, Object)
         */
        void insert(K key, V value) {
        	
        	// checks for proper index to insert key-value pair in this leaf node
        	boolean hasInserted = false;
            for (int i = 0; i < this.keys.size(); i++) {
            	if (key.compareTo(this.keys.get(i)) < 0) {
            		this.keys.add(i, key);
            		this.values.add(i, value);
            		hasInserted = true;
            		break;
            	}
            }
            
            // if key-value wasn't inserted, add the key-value pair the end of the lists
            if(!hasInserted) {
            	this.keys.add(key);
            	this.values.add(value);
            }
            
        }
        
        /**
         * (non-Javadoc)
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
		 * (non-Javadoc)
		 * 
		 * @see BPTree.Node#rangeSearch(Comparable, String)
		 */
		List<V> rangeSearch(K key, String comparator) {
			List<V> comparedValues = new ArrayList<V>();

			switch (comparator) {
			case "<=":
				for (int i = 0; i < this.keys.size(); i++) {
					if (this.keys.get(i).compareTo(key) <= 0) {
						comparedValues.add(this.values.get(i));
					}
				}
				break;

			case ">=":
				for (int i = 0; i < this.keys.size(); i++) {
					if (this.keys.get(i).compareTo(key) >= 0) {
						comparedValues.add(this.values.get(i));
					}
				}
				break;

			case "==":
				for (int i = 0; i < this.keys.size(); i++) {
					if (this.keys.get(i).compareTo(key) == 0) {
						comparedValues.add(this.values.get(i));
					}
				}
				break;
			}

			//System.out.println(comparedValues);
			return comparedValues;
		}

    } // End of class LeafNode
    
    
    /**
     * Contains a basic test scenario for a BPTree instance.
     * It shows a simple example of the use of this class
     * and its related types.
     * 
     * @param args
     */
    public static void main(String[] args) {
//        // create empty BPTree with branching factor of 3
//        BPTree<Double, Double> bpTree = new BPTree<>(4);
//
//        // create a pseudo random number generator
//        Random rnd1 = new Random();
//
//        // some value to add to the BPTree
//        Double[] dd = {0.0d, 0.5d, 0.2d, 0.8d};
//
//        // build an ArrayList of those value and add to BPTree also
//        // allows for comparing the contents of the ArrayList 
//        // against the contents and functionality of the BPTree
//        // does not ensure BPTree is implemented correctly
//        // just that it functions as a data structure with
//        // insert, rangeSearch, and toString() working.
//        List<Double> list = new ArrayList<>();
//        for (int i = 0; i < 400; i++) {
//            Double j = dd[rnd1.nextInt(4)];
//            list.add(j);
//            bpTree.insert(j, j);
//            //System.out.println("\n\nTree structure:\n" + bpTree.toString());
//        }
//        System.out.println("\n\nTree structure:\n" + bpTree.toString());
//        List<Double> filteredValues = bpTree.rangeSearch(0.2d, "<=");
//        System.out.println("Filtered values: " + filteredValues.toString());
    	
    	BPTree<Integer, String> bp = new BPTree<Integer,String>(5);
    	
    	final int NUM = 30;
    	
    	for (int i = 1; i <= NUM; i++ ) {
    		
    		for (int k = 1; k <= NUM; k++) {
    		    bp.insert(i, i + "");
    		}
    		//bp.insert(i, i + "");
    	}

    	for (int j = 1; j <= NUM; j++) {
    		List<String> filteredValues = bp.rangeSearch(j, "==");
        	System.out.println("Filtered values " + j + ": " + filteredValues.toString());
    	}
    	System.out.println();
    	
    }

} // End of class BPTree
