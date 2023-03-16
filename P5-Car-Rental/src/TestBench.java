// --== CS400 File Header Information ==--
// Name: Caelan Kleinhans
// Email: ckleinhans@wisc.edu
// Team: MA
// Role: Test Engineer
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: Can't test addVehicle or swapVehicle in the Driver method as they require user 
//                  input from a Scanner. Also can't test getDescription as it doesn't change the
 //                 tree or return anything.

import static org.junit.jupiter.api.Assertions.*;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.util.zip.DataFormatException;
import org.junit.jupiter.api.Test;

/**
 * Test bench for the Vehicle Database RedBlackTree project.
 * 
 * @author Caelan Kleinhans
 *
 */

class TestBench {

  /**
   * Tests the constructor of the Vehicle class by passing valid and invalid arguments.
   */
  @SuppressWarnings("unused")
  @Test
  void testVehicleConstructor() {
    // test with empty vin
    try {
      Vehicle v1 = new Vehicle("", 0, 2000, 16000, "Prius", "Red", false);
      fail("Empty VIN not caught");
    } catch (IllegalArgumentException e) {};
    
    // test with vin with space
    try {
      Vehicle v1 = new Vehicle("123456789ABCD EFG", 0, 2000, 16000, "Van", "Orange", false);
      fail("VIN with space not caught");
    } catch (IllegalArgumentException e) {};
    
    // test with vin with lowercase letter
    try {
      Vehicle v1 = new Vehicle("123456789ABCDjEFG", 0, 2000, 16000, "Van", "Orange", false);
      fail("VIN with lowercase letter not caught");
    } catch (IllegalArgumentException e) {};
    
    // test with vin with special character
    try {
      Vehicle v1 = new Vehicle("1111$22222ASDFRGQ", 0, 2000, 16000, "Prius", "Silver", false);
      fail("VIN with special character not caught");
    } catch (IllegalArgumentException e) {};
    
    // test with vin with incorrect length smaller
    try {
      Vehicle v1 = new Vehicle("12349FGS93HSFYU7", 0, 2000, 16000, "Prius", "Red", false);
      fail("VIN with 16 length not caught");
    } catch (IllegalArgumentException e) {};
    
    // test with vin with incorrect length larger
    try {
      Vehicle v1 = new Vehicle("123409FG5GS93HSFYU7", 0, 2000, 16000, "Mustang", "Red", false);
      fail("VIN with 18 length not caught");
    } catch (IllegalArgumentException e) {};
    
    // test with negative mileage
    try {
      Vehicle v1 = new Vehicle("01HJEYS789ASDRGTK", -1, 2000, 16000, "Prius", "Red", false);
      fail("Negative milage not caught");
    } catch (IllegalArgumentException e) {};
    
    // test valid mileage and used condition
    try {
      Vehicle v1 = new Vehicle("0123456789ASDRGTK", 16840284, 2000, 16000, "Hummer", "Maroon", false);
      if (v1.isNew())
        fail("Vehicle with non zero mileage not used");
    } catch (IllegalArgumentException e) {
      fail("Valid milage caught");
    };
    
    // test very old year
    try {
      Vehicle v1 = new Vehicle("0123456789ASDRGTK", 2000, 1700, 16000, "F150", "Brown", false);
      fail("Old year not caught");
    } catch (IllegalArgumentException e) {};
    
    // test valid year and new condition
    try {
      Vehicle v1 = new Vehicle("0123456789ASDRGTK", 0, 1890, 16000, "Ferrari", "Yellow", false);
      if (!v1.isNew())
        fail("Zero milage did not result in new condition");
    } catch (IllegalArgumentException e) {
      fail("Valid year 1890 caught");
    };
    
    // test negative price
    try {
      Vehicle v1 = new Vehicle("0123456789ASDRGTK", 2000, 1890, -1000, "Prius", "Red", false);
      fail("Negative price not caught");
    } catch (IllegalArgumentException e) {};
  }
  
  /**
   * Tests the public vehicle methods (compareTo, changeMileage, changeRent, setPrice)
   */
  @Test
  void testVehicleMethods() {
    Vehicle v1 = new Vehicle("0123456789ASDRGTK", 0, 1890, 20000, "Tesla", "Black", true);
    
    // Test isRented and changeRent
    assertEquals(v1.isRented(), true, "rented field not set properly");
    v1.changeRent();
    assertEquals(v1.isRented(), false, "isRented didn't change rent status");
    v1.changeRent();
    assertEquals(v1.isRented(), true, "isRented didn't change rent status");
    
    // test changing mileage and condition
    assertEquals(v1.isNew(), true, "condition not set properly");
    assertEquals(v1.changeMileage(-200), false, "decreasing mileage not allowed");
    assertEquals(v1.changeMileage(0), true, "setting mileage to current mileage should be allowed");
    assertEquals(v1.getMileage(), 0, "Mileage not updated properly");
    assertEquals(v1.changeMileage(200), true, "setting mileage to larger mileage should be allowed");
    assertEquals(v1.getMileage(), 200, "Mileage not updated properly");
    assertEquals(v1.isNew(), false, "condition not updated when mileage changed");
    
    // test updating the price to a higher and lower value
    assertEquals(v1.setPrice(20001), false, "increasing price not allowed");
    assertEquals(v1.setPrice(20000), true, "setting price to current price should be allowed");
    assertEquals(v1.getPrice(), 20000, "Price not updated properly");
    assertEquals(v1.setPrice(200), true, "setting price to lower price should be allowed");
    assertEquals(v1.getPrice(), 200, "Price not updated properly");
    assertEquals(v1.setPrice(-100), false, "setting price to negative should not be allowed");
    
    // test compareTo method
    Vehicle v2 = new Vehicle("0123456789ASDRGTK", 2000, 1890, 10000, "Prius", "Red", false);
    assertEquals(v1.compareTo(v2), 0, "same VIN should result in compareTo 0");
    Vehicle v3 = new Vehicle("0123456789BSDRGTK", 2000, 1890, 10000, "Prius", "Red", false);
    if (v1.compareTo(v3) >= 0)
      fail("larger VIN should result in negative compareTo");
    Vehicle v4 = new Vehicle("0123456389BSDRGTK", 0, 1890, 10000, "Prius", "Red", true);
    if (v1.compareTo(v4) <= 0)
      fail("smaller VIN should result in positive compareTo");
  }
  
  /**
   * Tests the RBTreeAPI methods (size, insert, getVehicle, remove, clear)
   */
  @Test
  void testRBTreeAPI() {
    RBTreeAPI tree = new RBTreeAPI();
    assertEquals(tree.size(), 0, "size not initially 0");
    
    tree.insert(new Vehicle("01234567890123456", 0, 2000, 18000, "6", "Blue", false));
    tree.insert(new Vehicle("0123456789012345A", 0, 2000, 18000, "A", "Red", false));
    tree.insert(new Vehicle("0123456789012345C", 0, 2000, 18000, "C", "Green", false));
    tree.insert(new Vehicle("0123456789012345U", 0, 2000, 18000, "U", "Blue", false));
    tree.insert(new Vehicle("0123456789012345G", 0, 2000, 18000, "G", "Blue", false));
    tree.insert(new Vehicle("0123456789012345P", 0, 2000, 18000, "P", "Blue", false));
    
    // test size method
    assertEquals(tree.size(), 6, "size not calculated properly");
    
    // test inserting vehicle with same VIN
    try {
      tree.insert(new Vehicle("0123456789012345U", 1000, 2015, 18000, "U2", "Orange", false));
      fail("Didn't catch inserting same vehicle");
    } catch (IllegalArgumentException e) {};
    
    // test getVehicle, elements that exist and don't exist
    assertEquals(tree.getVehicle("0123456789012345G").getModel(), "G", "unexpeced vehicle returned by getVehicle");
    assertEquals(tree.getVehicle("0123456789012345A").getModel(), "A", "unexpeced vehicle returned by getVehicle");
    try {
      tree.getVehicle("0123456789012345O");
      fail("tree did not throw exception when getting nonexistant vehicle");
    } catch (NoSuchElementException e) {};
    
    // test remove method on element that exists
    assertEquals(tree.remove("0123456789012345C").getModel(), "C", "remove didn't return correct vehicle");
    assertEquals(tree.size(), 5, "size not correct after remove method");
    try {
      tree.getVehicle("0123456789012345C");
      fail("tree didn't throw exception when getting a removed vehicle");
    } catch (NoSuchElementException e) {};
    
    // test remove method on element that doesn't exist
    try {
      tree.getVehicle("01234567890123455");
      fail("tree didn't throw exception when removing a non-existant vehicle");
    } catch (NoSuchElementException e) {};
    assertEquals(tree.size(), 5, "size not correct after trying to remove an imaginary vehicle");
    
    // test clear method
    tree.clear();
    assertEquals(tree.size(), 0, "clear did not set size to 0");
    try {
      tree.getVehicle("01234567890123456");
      fail("tree didn't throw exception when getting a vehicle after tree cleared");
    } catch (NoSuchElementException e) {};
  }
  
  /**
   * Tests the Driver removeVehicle method
   */
  @Test
  void testDriverRemoveVehicle() {
    // Create a new tree instance
    Driver.tree = new RBTreeAPI();
    
    // Add some vehicles to the inventory
    Driver.tree.insert(new Vehicle("01234567890123456", 0, 2000, 18000, "6", "Blue", false));
    Driver.tree.insert(new Vehicle("0123456789012345A", 0, 2000, 18000, "A", "Red", false));
    Driver.tree.insert(new Vehicle("0123456789012345C", 0, 2000, 18000, "C", "Green", false));
    
    // Test with 0 arguments
    Driver.removeVehicle(new String[0]);
    assertEquals(Driver.tree.size(), 3, "removeVehicle with 0 arguments altered the tree");
    
    // Test with 2 arguments
    Driver.removeVehicle(new String[] {"01234567890123456", "D"});
    assertEquals(Driver.tree.size(), 3, "removeVehicle with 2 arguments altered the tree");
    
    // Test with invalid VIN
    Driver.removeVehicle(new String[] {"1234678"});
    assertEquals(Driver.tree.size(), 3, "removeVehicle with invalid VIN altered the tree");
    
    // Test with valid VIN not in the tree
    Driver.removeVehicle(new String[] {"0123456789012345H"});
    assertEquals(Driver.tree.size(), 3, "removeVehicle with non-existant VIN altered the tree");
    
    // Test with valid VIN in the tree
    Driver.removeVehicle(new String[] {"01234567890123456"});
    assertEquals(Driver.tree.size(), 2, "removeVehicle valid VIN did not alter the tree");
    // Already tested RBTreeAPI methods so we know it removed the correct vehicle if size changed
  }
  
  /**
   * Tests the Driver rentVehicle method
   */
  @Test
  void testDriverRentVehicle() {
    // Create a new tree instance
    Driver.tree = new RBTreeAPI();
    
    // Add some vehicles to the inventory
    Driver.tree.insert(new Vehicle("01234567890123456", 0, 2000, 18000, "6", "Blue", false));
    Driver.tree.insert(new Vehicle("0123456789012345A", 0, 2000, 18000, "A", "Red", true));
    Driver.tree.insert(new Vehicle("0123456789012345C", 0, 2000, 18000, "C", "Green", false));
    
    // test with 0 arguments (no way to verify success/fail other than exception)
    Driver.rentVehicle(new String[0]);
    
    // test with 2 arguments
    Driver.rentVehicle(new String[] {"0123456789012345C", "G"});
    assertEquals(Driver.tree.getVehicle("0123456789012345C").isRented(), false, 
        "Passing 2 arguments caused a vehicle to get rented");
    
    // test with invalid VIN (no way to verify success/fail other than exception)
    Driver.rentVehicle(new String[] {"012345678901345T"});
    
    // test with valid VIN, not in the tree (no way to verify success/fail other than exception)
    Driver.rentVehicle(new String[] {"0123456789012345T"});
    
    // test with valid VIN, vehicle already rented
    Driver.rentVehicle(new String[] {"0123456789012345A"});
    assertEquals(Driver.tree.getVehicle("0123456789012345A").isRented(), true, 
        "Passing already rented vehicle caused vehicle to be un-rented");
    
    // test with valid VIN, vehicle not rented
    Driver.rentVehicle(new String[] {"01234567890123456"});
    assertEquals(Driver.tree.getVehicle("01234567890123456").isRented(), true, 
        "Passing un-rented vehicle didn't cause the vehicle to be rented");
  }
  
  /**
   * Tests the Driver returnVehicle method
   */
  @Test
  void testDriverReturnVehicle() {
    // Create a new tree instance
    Driver.tree = new RBTreeAPI();
    
    // Add some vehicles to the inventory
    Driver.tree.insert(new Vehicle("01234567890123456", 0, 2000, 18000, "6", "Blue", false));
    Driver.tree.insert(new Vehicle("0123456789012345A", 500, 2000, 18000, "A", "Red", true));
    Driver.tree.insert(new Vehicle("0123456789012345C", 0, 2000, 18000, "C", "Green", false));
    
    // test with 0 arguments (no way to verify success/fail other than exception)
    Driver.returnVehicle(new String[0]);
    
    // test with 1 argument
    Driver.returnVehicle(new String[] {"0123456789012345A"});
    assertEquals(Driver.tree.getVehicle("0123456789012345A").isRented(), true, 
        "Passing 1 argument caused a vehicle to get returned");
    
    // test with non integer mileage
    Driver.returnVehicle(new String[] {"0123456789012345A", "asdf"});
    assertEquals(Driver.tree.getVehicle("0123456789012345A").isRented(), true, 
        "Passing non-integer mileage caused a vehicle to get returned");
    
    // test with invalid VIN (no way to verify success/fail other than exception)
    Driver.returnVehicle(new String[] {"012345678901345T", "1000"});
    
    // test with valid VIN, not in the tree (no way to verify success/fail other than exception)
    Driver.returnVehicle(new String[] {"0123456789012345T", "1000"});
    
    // test with valid VIN, vehicle already returned
    Driver.returnVehicle(new String[] {"01234567890123456", "1000"});
    assertEquals(Driver.tree.getVehicle("01234567890123456").isRented(), false, 
        "Passing already returned vehicle caused vehicle to be un-returned");
    
    // test with valid VIN, vehicle not returned, mileage less than original
    Driver.returnVehicle(new String[] {"0123456789012345A", "250"});
    assertEquals(Driver.tree.getVehicle("0123456789012345A").isRented(), true, 
        "Passing mileage less than current caused the vehicle to be returned");
    
    // test with valid VIN, vehicle not returned, mileage less than original
    Driver.returnVehicle(new String[] {"0123456789012345A", "1000"});
    assertEquals(Driver.tree.getVehicle("0123456789012345A").isRented(), false, 
        "Passing valid return caused the vehicle to not be returned");
    assertEquals(Driver.tree.getVehicle("0123456789012345A").getMileage(), 1000, 
        "Passing valid return didn't update mileage");
  }
  
  /**
   * Tests the DataHandler importCars method for loading files of cars.
   * Not testing initCars method because it just calls the importCars method on a folder of files.
   */
  @Test
  void testImportCars() {
    // Create a new tree instance
    RBTreeAPI tree = new RBTreeAPI();
    try {
      // Test loading a non-csv file
      try {
        DataHandler.importCars("./test/valid.txt", tree);
        fail("No exception thrown when loading .txt file");
      } catch (DataFormatException e) {};
      assertEquals(tree.size(), 0, "loading .txt file changed the tree");
      
      // Test loading a file with gibberish
      try {
        DataHandler.importCars("./test/gibberish.csv", tree);
        fail("No exception thrown when loading gibberish file");
      } catch (DataFormatException e) {};
      assertEquals(tree.size(), 0, "loading gibberish file changed the tree");
      
      // Test loading a file with some cars, then gibberish
      try {
        DataHandler.importCars("./test/gibberish2.csv", tree);
        fail("No exception thrown when loading gibberish file");
      } catch (DataFormatException e) {};
      assertEquals(tree.size(), 8, "Bad loading valid cars before gibberish");
      tree.clear();
      
      // Test loading a file with valid and poorly formatted cars
      try {
        DataHandler.importCars("./test/invalidformat.csv", tree);
        fail("No exception thrown when loading improperly formatted file");
      } catch (DataFormatException e) {};
      assertEquals(tree.size(), 6, "Bad loading valid cars before invalid format");
      tree.clear();
      
      // Test loading a file with repeated cars
      try {
        DataHandler.importCars("./test/repeated.csv", tree);
        fail("No exception thrown when loading repeated cars from file");
      } catch (DataFormatException e) {};
      assertEquals(tree.size(), 19, "Bad loading valid cars before repeated");
      tree.clear();
      
      // Test loading a valid file with 50 cars
      try {
        DataHandler.importCars("./test/valid.csv", tree);
      } catch (DataFormatException e) {
        fail("Exception thrown when loading valid file");
      };
      assertEquals(tree.size(), 100, "Bad loading valid cars into tree");
      assertEquals(tree.getVehicle("83257474922408506").getMileage(), 36372, 
          "Couldn't get a loaded car from the valid file");
    
    } catch (IOException e) {
      fail("IOException thrown");
    }
  }
  
}
