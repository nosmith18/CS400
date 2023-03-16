// --== CS400 File Header Information ==--
// Name: Caelan Kleinhans
// Email: ckleinhans@wisc.edu
// Team: MA
// Role: Back End Developer 2
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 * Class representing the inventory of items in a store using their names as keys for easy access.
 * 
 * @author Caelan Kleinhans
 * 
 */
public class Inventory {
  
  private Hashtable<String, Item> inventory;
  private int size;
  
  /**
   * Creates a new Inventory object.
   */
  public Inventory() {
    inventory = new Hashtable<String, Item>();
    this.size = 0;
  }
  
  /**
   * Gets the current number of unique elements in the inventory.
   * 
   * @return the size of the inventory
   */
  public int size() {
    return size;
  }
  
  /**
   * Adds the given item to the inventory.
   * 
   * @param item to add
   * @return true if the item was added to the inventory
   * @throws IllegalArgumentException if a matching item already exists in the inventory
   */
  public boolean add(Item item) throws IllegalArgumentException {
    if (inventory.containsKey(item.getName().toLowerCase()))
      throw new IllegalArgumentException("Item already exists in the inventory with the given name.");
    inventory.put(item.getName().toLowerCase(), item);
    size++;
    return true;
  }
  
  /**
   * Removes the item with the given name from the inventory.
   * 
   * @param name of item to remove
   * @return true if the item existed in the inventory and was removed, false otherwise
   */
  public boolean remove(String name) {
    if (!inventory.containsKey(name.toLowerCase()))
      return false;
    size--;
    inventory.remove(name.toLowerCase());
    return true;
  }
  
  /**
   * Checks if the inventory has no items in it.
   * 
   * @return true if there are no items in the inventory, false otherwise.
   */
  public boolean isEmpty() {
    return size == 0;
  }

  /**
   * Checks if an item with the given name is in the inventory.
   * 
   * @param name of the item to check
   * @return true if an item with the given name is in the inventory, false otherwise
   */
  public boolean contains(String name) {
    return inventory.containsKey(name.toLowerCase());
  }
  
  /**
   * Gets a list of all items in the inventory which names match the given regular expression.
   * 
   * @param regex to match to the item names
   * @return an array of all items which match
   */
  public Item[] getMatching(String regex) {
    Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
    Enumeration<String> keys = inventory.keys();
    ArrayList<Item> results = new ArrayList<Item>();
    int numMatches = 0;
    while (keys.hasMoreElements()) {
      String key = keys.nextElement();
      if (pattern.matcher(key).matches()) {
        results.add(inventory.get(key));
        numMatches++;
      }
    }
    return results.toArray(new Item[numMatches]);
  }
  
  /**
   * Gets an iterator that can be used to iterate over all Items within the inventory.
   * 
   * @return Iterator over all items
   */
  public Iterator<Item> iterator() {
    return inventory.values().iterator();
  }
  
}
