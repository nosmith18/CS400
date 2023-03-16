// --== CS400 File Header Information ==--
// Name: Nolan Smith
// Email: nosmith@wisc.edu
// Team: MA
// Role: Test Engineer 1
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;
import org.junit.jupiter.api.Test;

public class TestBench {
  
  @Test
  void testImportingFiles() {
      Inventory test = new Inventory();
      

      
      // Test importing non-existent csv file and a txt file
      assertEquals(false, DataHandler.loadAll(test, "./testInventory/notAFile.csv"),
          "Loading non-existent file threw no error");
      assertEquals(false, DataHandler.loadAll(test, "./testInventory/textFile.txt"), 
          "Loading a .txt file threw no errors");
      //Check that size wasn't changed
      assertEquals(0, test.size(), "Inventory was modified by an invalid file"
          + "(non-existent csv or a .txt file)");
      
      
      //Import file with invalid format, only adds correctly formatted lines to Inventory
      assertEquals(true, DataHandler.loadAll(test, "./testInventory/InvalidFormat.csv"),
          "Loads poorly formatted file, only adds items with correct formatting!");
      assertEquals(false, test.contains("Bagel"), "Should not contain an item with invalid format");
      //Check for correct size, not all items were imported
      assertEquals(9, test.size(), "Wrong number of items loaded from InvalidFormat.csv");
      
      
      //Reset test Inventory
      test = new Inventory();
      
      
      //Import correctly formatted file
      assertEquals(true, DataHandler.loadAll(test, "./testInventory/Valid.csv"),
          "Importing a valid csv file threw an unexpected error");
      //Check contains the items that were loaded
      assertEquals(true, test.contains("Orange"), "Inventory doesn't contain an item that it should.");
      assertEquals(true, test.contains("Avocado"), "Inventory doesn't contain an item that it should.");
      assertEquals(true, test.contains("Ketchup"), "Inventory doesn't contain an item that it should.");
      assertEquals(true, test.contains("Chicken"), "Inventory doesn't contain an item that it should.");
      assertEquals(10, test.size(), "Inventory doesn't contain an item that it should.");
      
      
      //Reset test Inventory
      test = new Inventory();
      
      
      //Import Duplicate file
      assertEquals(true, DataHandler.loadAll(test, "./testInventory/Duplicates.csv"),
          "Importing dupliate file threw an unexpected exception.");
      //Check size and correct stock of an item where a duplicate was found in the import file
      assertEquals(11, test.size(), "Wrong number of items in the Inventory");
      assertEquals(10, test.getMatching("Bread")[0].getStock(), "Incorrect stock of Bread item");
      
      
      //SaveAll from this inventory
      assertEquals(true, DataHandler.saveAll(test, "./testInventory/saveTest.csv"), 
          "Attempting saveAll to a file should return true");
      //Reset inventory and import the saved data
      test = new Inventory();
      //Import the saveTest file
      assertEquals(true, DataHandler.loadAll(test, "./testInventory/saveTest.csv"),
          "Loading from the saveTest inventory csv file returned false");
      assertEquals(11, test.size(), "Wrong number of items in the inventory after loading saveTest");
      
      
      //Delete saveTest File
      File saved = new File("./testInventory/saveTest.csv");
      saved.delete();
      
  }
  
  @Test 
  void testItemConstructor() {
      try {
        // Item with negative price
        try {
          Item invalidPrice = new Item("1", -100, 100, "./images/default.png");
          fail("An exception was not thrown - 1");
        } catch (IllegalArgumentException e) {
        }
        // Item with negative stock
        try {
          Item invalidStock = new Item("2", 100, -100, "./images/default.png");
          fail("An exception was not thrown - 2");
        } catch (IllegalArgumentException e) {
        }
        // Item with all valid inputs
        try {
          Item validItem = new Item("3", 100, 100, "./images/default.png");
        } catch (IllegalArgumentException e) {
          fail("An exception was thrown when it shouldn't have!");
        }
        // Item with non-existent imagePath
        try {
          Item invalidImage = new Item("4", -100, 100, "./images/NotAnImage.png");
          fail("An exception was not thrown - 4");
        } catch (IllegalArgumentException e) {
        }
      } catch (IOException e) {
        fail("Default image file was not found.");
      }
  }
  
  @Test
  void testItemMethods() {
      Item testItem = null;
      Item compareToItem = null;
      try {
        testItem = new Item("1", 10, 100, "./images/default.png");
        compareToItem = new Item(null, 0, 0, "");
      } catch (Exception e) {
        fail("An exception was thrown when one shouldn't have been.");
      }
      
      //Check the getter methods of Item
      assertEquals("1", testItem.getName(), "testItem's Name was incorrectly stored.");
      assertEquals(10, testItem.getPrice(), "testItem's Price was incorrectly stored.");
      assertEquals(100, testItem.getStock(), "testItem's Stock was incorrectly stored.");
      assertEquals("." + File.separatorChar + "images" + File.separatorChar + "default.png", testItem.getImagePath(), 
          "testItem's ImagePath was incorrectly stored.");
      
      //Test the setter methods
      testItem.setPrice(20);
      testItem.setStock(99); //One of this item has been removed
      assertEquals(20, testItem.getPrice(), "testItem's Price was incorrectly changed.");
      assertEquals(99, testItem.getStock(), "testItem's Stock was incorrectly changed.");
      try {
        testItem.setPrice(-10);
        fail("An exception SHOULD have been thrown by setPrice().");
      } catch (Exception e) {}
      
      try {
        testItem.setStock(-10);
        fail("An exception SHOULD have been thrown by setStock().");
      } catch (Exception e) {}
      
      //Test the Item's equals() 
      assertEquals(true, testItem.equals(testItem), "Equals() should've returned true.");
      assertEquals(false, testItem.equals(compareToItem), "Equals() should've returned false.");
      
  }
  
  @Test
  void testInventoryMethods() {
      Inventory test = new Inventory();
      
      
      //Check the isEmpty() method before and after an insertion
      assertEquals(true, test.isEmpty(), "Inventory was not empty before insertion.");
      try {
        test.add(new Item("Apple", 1.50, 100, ""));
      } catch (Exception e) { fail("Error thrown trying to add an item. 1"); }
      assertEquals(false, test.isEmpty(), "Inventory was empty after insertion.");
      
      
      //Reset
      test = new Inventory();
      
      
      //Check size before and after an addition of an Item
      assertEquals(0, test.size(), "Size was not 0 before insertion.");
      try {
        test.add(new Item("Apple", 1.50, 100, ""));
      } catch (Exception e) { fail("Error thrown trying to add an item. 2"); }
      assertEquals(1, test.size(), "Size was not 1 after insertion.");
      
      
      //Check that a proper Exception thrown when item already exists in Inventory
      try {
        test.add(new Item("Apple", 2, 100, ""));
        fail("IllegalArgumentException was not thrown when adding duplicate item");
      } catch (IllegalArgumentException i) {
      } catch (FileNotFoundException f) {
        fail("FileNotFoundException was thrown.");
      }
      
      
      //Check that contains() correctly return true and false
      assertEquals(true, test.contains("Apple"), "Contains() returned false instead of true."); //Should work case-insensitive
      //assertEquals(test.contains("apple"), true, "Contains() returned false instead of true.");
      assertEquals(false, test.contains("Velociraptor"), "Contains() returned true instead of false.");
      
      
      //Add more Items
      try {
        test.add(new Item("Orange", 1.50, 100, ""));
        test.add(new Item("Pear", 1.50, 100, ""));
        test.add(new Item("Bread", 1.50, 100, ""));
        test.add(new Item("Bagel", 1.50, 100, ""));
        test.add(new Item("Burger Patty", 1.50, 100, ""));
        test.add(new Item("Banana", 1.50, 100, ""));
      } catch (Exception e) { fail("Error thrown trying to add an item. 3"); }
      
      
      //Test getMatching()
      assertEquals(4, test.getMatching("b.*").length, "Regex did not return 4 items.");
      assertEquals(1, test.getMatching("Bread").length, "Regex did not return 1 item.");
      assertEquals(0, test.getMatching("Waffle").length, "Regex did not return 0 items.");
      assertEquals(1, test.getMatching("o.*").length, "Regex did not return 1 item.");
      
      
  }
  
  
  @Test
  void testDriver() {
      Driver test = new Driver();
      
      //Test items() before loading
      assertEquals(Driver.items(), 0, "Items() returned false instead of true.");
      
      //Test the add method
      assertEquals(true, Driver.add(new String[] {"add", "Apple", "1.5", "100", "1"}), "Add returned false instead of true.");
      assertEquals(false, Driver.add(new String[] {"add", "Apple", "1.5", "10", "2"}), "Adding a duplicate returned true instead of false.");
      assertEquals(true, Driver.add(new String[] {"add", "Pepper", "2", "10", "3"}), "Add returned false instead of true.");
      assertEquals(true, Driver.add(new String[] {"add", "Banana", "0.75", "10", "4"}), "Add returned false instead of true.");
      assertEquals(true, Driver.add(new String[] {"add", "Bell", "Pepper", "0.99", "10", "5"}), "Add returned false instead of true.");
      
      //Test items() before removal
      assertEquals(4, Driver.items(), "Items() returned false instead of true.");
      
      
      //Test the remove method and check stock after removal
      assertEquals(true, Driver.remove(new String[] {"remove", "pepper", "5"}), 
          "Remove() returned false instead of true. - 1");
      assertEquals(5, Driver.inventory.getMatching("pepper")[0].getStock(), 
          "Stock of Pepper was not 7 after removing 5");
      assertEquals(true, Driver.remove(new String[] {"remove", "banana", "10"}), 
          "Remove() returned false instead of true. - 2");
      assertEquals(0, Driver.inventory.getMatching("banana").length, 
          "Banana was not removed from inventory after removing all 10 from its stock");
      assertEquals(0, Driver.search(new String[] {"search", "banana"}), 
          "Banana was found in inventory after being completely sold out");
      assertEquals(false, Driver.remove(new String[] {"remove", "bell", "pepper", "11"}), 
          "Removing more than an item's current stock returned true instead of false");
      assertEquals(10, Driver.inventory.getMatching("bell pepper")[0].getStock(), 
          "Stock of Bell Pepper was not 10.");
      assertEquals(false, Driver.remove(new String[] {"remove", "shark", "1"}), 
          "Remove() returned true instead of false. - 4");
      
      
      //Test the items method after removing some
      assertEquals(3, Driver.items(), "Items() returned false instead of true.");
      
      
      //Test the search() method
      assertEquals(1, Driver.search(new String[] {"search", "pepper"}), "search() returned the wrong number of items. - 1");
      assertEquals(1, Driver.search(new String[] {"search", "apple"}), "search() returned the wrong number of items. - 2");
      assertEquals(1, Driver.search(new String[] {"search", "bell", "PePPer"}), "search() returned the wrong number of items. - 3");
      assertEquals(0, Driver.search(new String[] {"search", "apple", "cider"}), "search() returned the wrong number of items. - 4");
      
  }
  
}
