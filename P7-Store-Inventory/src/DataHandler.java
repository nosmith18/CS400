// --== CS400 File Header Information ==--
// Name: Edward Park
// Email: ejpark@wisc.edu
// Team: MA
// Role: Test Engineer
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: N/A

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Hashtable;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;

/**
 * Class that imports items from the inventory.csv to fill the inventory hash table from the local inventory folder, throwing
 * the appropriate exceptions if need be. 
 * @author Edward Park
 *
 */

public class DataHandler {  
  
  /**
   * Public method loads items into the Inventory by from a given file and builds items from the data on each line.
   * Note: Even as long as a file is .csv, this method will read it all to look for a valid item and skip over bad ones.
   * 
   * @param inventory: the inventory to be filled with the csv's items info
   * @param filePath: the filePath of our desired .csv file to load items from
   * @return true if entire file is read and its items are added to the hash table with no problem, false if not
   */
  public static boolean loadAll(Inventory inventory, String filePath) {
    File rawInven; // store .csv file
    BufferedReader reader; // used to read csv file
    String line; // raw line from the csv
    String[] readLine; // is just line but split along ","s
    Item toAdd = null; // stores item created using makeItem helper method, will add to given inventory hash table
    try {
      if (!Pattern.matches("^.+\\.csv$", filePath))
        throw new FileNotFoundException("Given file path of: " + filePath+" is invalid");     
      rawInven = new File(filePath);     
  
      reader = new BufferedReader(new FileReader(rawInven)); // create buffered file reader to read the csv
      
      // loop through and add items with properly formated info, skipping otherwise
      while((line = reader.readLine()) != null) { // read line by line until we hit the end
        readLine = line.split(","); // split line along ","s
        if (readLine.length == 3 || readLine.length == 4) { // line should be three parts of item name, price, and amount in stock (aisle is optional)
          try {
            toAdd = DataHandler.makeItem(readLine); // call helper method to create our item object
            inventory.add(toAdd); // add item object to hash table
          }       
          catch(IllegalArgumentException e) {
            System.out.println("This line is not valid, it will be skipped: " + line);
          }
          catch(Exception e) { // if any other exceptions thrown, close the reader then throw them upwards
            try {
              reader.close();
              throw e;
            }
            catch (Exception e1) {
              return false;
            }         
          }         
        }
      }
      
      
      reader.close(); // close reader
    }
    catch (Exception e) {
      System.out.println(e.getMessage());
      return false;
    }
    return true;
  }
  
  /**
   * Public method saves items from the inventory into a file of the given name in the Inventory folder.
   * 
   * @param inventory: the inventory who's items are to be saved 
   * @param filePath: the file path of our desired .csv file to save items to inside the Inventory folder
   * @return true if we were able to save the inventory's info, false otherwise
   */
  public static boolean saveAll(Inventory inventory, String filePath) {
    File toWriteTo; // store inventory.csv file
    File folder;
    FileWriter writer = null;
    Item[] inventoryItems = null;
    String fileData = null;
    
    try {
      if (!Pattern.matches("^.+\\.csv$", filePath)) // check we are writing to a .csv file
        throw new FileNotFoundException("Given file path of: " + filePath+" is invalid");      
      
      toWriteTo = new File(filePath);     

      inventoryItems = inventory.getMatching(".*"); // get array of all items in inventory to save info of
      
      for (int i = 0; i < inventoryItems.length; i++) { // now begin writing all item info to a string, each item's stuff on a new line
        if (i == 0)
          fileData = inventoryItems[i].getName().trim()+","+inventoryItems[i].getPrice()+","+inventoryItems[i].getStock()+","+inventoryItems[i].getAisle()+"\n";
        else if (i == inventoryItems.length )
          fileData += inventoryItems[i].getName().trim()+","+inventoryItems[i].getPrice()+","+inventoryItems[i].getStock()+","+inventoryItems[i].getAisle();
        else
          fileData += inventoryItems[i].getName().trim()+","+inventoryItems[i].getPrice()+","+inventoryItems[i].getStock()+","+inventoryItems[i].getAisle()+"\n"; 
      }      
      writer = new FileWriter(toWriteTo); // create file writer
      writer.write(fileData); // write item info to file
      writer.close(); // close it when we are done
    }
    catch(Exception e) {
      try {
        writer.close(); // try to close it if anything goes wrong
        return false; 
      }
      catch(Exception e1) {
        return false;
      }           
    }
    return true;
  }

  /**
   * Helper method to try and create an item object using the given readLine array of information stored
   * in Strings
   * 
   * @param readline: the String array of the potential item's info. Order should be item name, price, and amount in stock
   * @return an item object
   * @throw IllegalArgumentException: if the given item info is invalid, meaning price isn't a double, or number in stock is not integer
   * @throws DataFormatException: if the Item constructor rejects the given item data
   * @throws FileNotFoundException: if the Item constructor cannot find the item's image
   */
  private static Item makeItem(String[] readLine) throws IllegalArgumentException, DataFormatException, FileNotFoundException {
    /** Regex expressions are structured as follows
    * 0: for parsing the item name, where any String is accepted
    * 1: for parsing the item's price, any number of digits is accepted and any number of digits followed by a "." and another 1-2 digits is accepted
    * Ex: 1.2 (valid), 1. (not valid), 00.0 (valid), -1 (invalid), 1.222 (invalid), and .49 (valid)
    * 2: for parsing the amount of the item in stock, must be an integer
    * 3: aisle must be a number
    */
    String[] patterns = new String[] {".*", "\\d*\\.\\d{1,2}$|^\\d*$", "^\\d*$", "^\\d*$|^N\\/A$"}; 
    String name = null;
    String aisle = null;
    double price = -1;
    int numInStock = -1;
    
    for (int i = 0; i < readLine.length; i++) { // check potential item data 
      if (Pattern.matches(patterns[i], readLine[i])) { // apply appropriate regex expression
        switch (i) {
          case 0: // valid item name
            name = readLine[i].trim();
            break;
          case 1: // valid item price
            price = Double.parseDouble(readLine[i]);
            break;
          case 2:  // valid item quantity
            numInStock = Integer.parseInt(readLine[i]);
            break;
          case 3: // valid aisle number
            aisle = readLine[i].trim();
            break;
        } 
      }     
      else { // bad input rejected by regex expression
        throw new IllegalArgumentException("Data in file is not properly formated.");
      }        
    } 
    if (aisle == null)
      aisle = "N/A";
   // create and return Item object
    return new Item(name, price, numInStock, 
        "https://pages.cs.wisc.edu/~caelan/project4/images/"+name.replace(" ", "").toLowerCase()+".png", aisle);
  }
}