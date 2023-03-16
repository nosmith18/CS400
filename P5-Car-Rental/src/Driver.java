// --== CS400 File Header Information ==--
// Name: Edward Park
// Email: ejpark@wisc.edu
// Team: MA
// Role: Test Engineer
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: Helper methods are protected to allow for testing.

import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.zip.DataFormatException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Class that allows for the user to interact with the red black tree.
 * @author Edward Park
 *
 */

public class Driver {

  protected static RBTreeAPI tree = new RBTreeAPI();
  
  /**
   * main method that runs the program using user inputs
   */
  public static void main(String[] args) {
    boolean exitEntered = false; // keeps track of if user has entered exit
    ArrayList<MatchResult> storedCommand = new ArrayList<MatchResult>(); // saves strings from regex stream
    String[] splitCommandNotLowerCase; // array that contains the parsed command split by spaces
    String[] splitCommand; // array that contains parsed command split by spaces in lower case for sorting reasons
    Pattern pattern; // regex pattern
    String rawCommand; // raw user input before being parsed with a regex expression
    System.out.println("Initilization logs:\n" + DataHandler.initCars(tree));
    System.out.println("Welcome to the Car Dealership Inventory Managment System" +
        "\n\nPlease enter a command, type \"help\" for a list of all commands, type \"exit\" to exit.");
    Scanner input = new Scanner(System.in); 
    
    // loop that contains the user interface itself
    while (!exitEntered) {
      try {
        storedCommand.clear(); // clear old command
        rawCommand = input.nextLine(); // take input
        // sort inputs using regex, the stored command will only be inputs that contain commands
        // at the their starts, or for help, addVehicle, exit, and swapVehicle are just one word
        pattern = Pattern.compile("^help$|^addvehicle$|^removevehicle{1}(\\s.*|$)|^rentvehicle{1}(\\s.*|$)"
            + "|^returnvehicle{1}(\\s.*|$)|^swapVehicle$|^getdescription{1}(\\s.*|$)|^importVehicles{1}(\\s.*|$)|^exit$", Pattern.CASE_INSENSITIVE);
        pattern.matcher(rawCommand).results().forEach(m -> storedCommand.add(m)); // store stream results
        if (storedCommand.isEmpty()) // empty means it didn't contain any possibly properly formatted commands
          throw new IllegalStateException("Bad Command, please check command list");
        
        // exit if user entered exit
        if (storedCommand.get(0).group().toLowerCase().equalsIgnoreCase("exit")) {
          exitEntered = true;
          break;
        }
        // split stored command along spaces into array
        splitCommand = storedCommand.get(0).group().toLowerCase().split(" ");
        splitCommandNotLowerCase = storedCommand.get(0).group().split(" ");
        
        if (splitCommand.length != 2) { // sort possible commands that are not 2 parts
          switch (splitCommand[0]) {
            case "help" : // check possible help command
              if (splitCommand.length != 1) // if not of length 1 aka just "help" it is a bad input
                throw new IllegalStateException("Bad command, please check command list");
              else { // otherwise it is a valid "help" command
                System.out.println("Command List:\n\nhelp: Lists all commands."
                  + "\naddVehicle: Adds vehicle to the inventory."
                  + "\nremoveVehicle <vehicle's VIN number>: Removes vehicle from inventory."
                  + "\nrentVehicle <vehicle's VIN number>: Rents vehicle from inventory."
                  + "\nreturnVehicle <vehicle's VIN number>: Returns vehicle to inventory."
                  + "\nswapVehicle <vehicle one> <vehicle two>: Add car one and remove car two from inventory"
                  + "\ngetDescription <vehicle's VIN number>: Gets description of vehicle."
                  + "\nimportVehicles <file path>: Imports car from given file.\n");
              }
              break;
            case "addvehicle" : // check possible addVehicle command
             Driver.addVehicle(input, splitCommandNotLowerCase);
             break;
            case "swapvehicle" : // check possible swapVehicle command
             Driver.swapVehicle(splitCommandNotLowerCase, input);
              break; 
            case "removevehicle" : // badly formatted removeVehicle command
              throw new IllegalStateException("Bad use of the removeVehicle command.\nformat is: "
                  + "removeVehicle <vehicle's VIN number>");
            case "rentvehicle" : // badly formatted rentVehicle command
              throw new IllegalStateException("Bad use of the rentVehicle command.\nformat is: "
                  + "rentVehicle <vehicle's VIN number>");
            case "returnvehicle" : // check possible returnVehicle command
              if (splitCommand.length != 3) {
                throw new IllegalStateException("Bad use of the returnVehicle command.\nformat is: "
                    + "returnVehicle <vehicle's VIN number> <new mileage>");
              }
              else {
                splitCommandNotLowerCase = Arrays.copyOfRange(splitCommandNotLowerCase, 1, splitCommandNotLowerCase.length);
                Driver.returnVehicle(splitCommandNotLowerCase); // command is now just the VIN and possible new mileage
              }
              break;
            case "getdescription" : // badly formatted getDescription command
              throw new IllegalStateException("Bad use of the getDescription command.\nformat is: "
                  + "getDescription <vehicle's VIN number>");
            case "importvehicles" : // badly formatted importVehicles command
              throw new IllegalStateException("Bad use of the importVehicles command.\nformat is: "
                  + "importVehicles <file path>"); 
            default: // in general badly formatted command case if any where not caught earlier
              System.out.println("Bad Command, please check command list");
          }        
        }       
        else { // all commands have been sorted to be a command followed by one word
          // must only pass the 2nd part of the command aka the following word
          splitCommandNotLowerCase = Arrays.copyOfRange(splitCommandNotLowerCase, 1, splitCommandNotLowerCase.length);
          switch (splitCommand[0]) {                              
            case "removevehicle" : 
              Driver.removeVehicle(splitCommandNotLowerCase);         
              break; 
            case "returnvehicle" :  
              throw new IllegalStateException("Bad use of the returnVehicle command.\nformat is: "
                  + "returnVehicle <vehicle's VIN number> <new mileage>");
            case "rentvehicle" :   
              Driver.rentVehicle(splitCommandNotLowerCase);
              break;   
            case "getdescription" : // print description
              Driver.getDescription(splitCommandNotLowerCase);
              break;             
            case "importvehicles" : 
              Driver.importVehicles(splitCommandNotLowerCase);
          }
        }                
      }          
      // catch detailed error messages to print for the user to see
      catch (IllegalStateException e) { 
        System.out.println(e.getMessage());
      }
      catch (NumberFormatException e) {
        System.out.println(e.getMessage());
      }
      catch (IllegalArgumentException e) {
        System.out.println(e.getMessage());
      }
      catch (Exception e) {
        System.out.println("Bad Command, please check command list");
      }
    }
    // prints once exit is entered
    System.out.println("Thank you for using the Car Dealership Inventory Managment System");
    input.close();      
  }
  
  /**
   * Method that adds a new vehicle to inventory.
   * 
   * @param command a string array of the user's command
   * @param userInput a Scanner object to take user inputs.
   */
  protected static void addVehicle(Scanner userInput, String[] command) throws IllegalStateException {
    try {
      if (command.length != 1) { // if not of length 1 aka just "addVehicle" it is a bad input
        throw new IllegalStateException("Bad use of the addVehicle command.\nformat is: "
            + "addVehicle");
      }
      else { // otherwise it is a valid addVehicle command
        try {
          tree.insert(Driver.vehicleMaker(userInput)); // call helper method
          System.out.println("Car added successfully.");
        }
        catch (IllegalArgumentException e) { // in case vehicle is already in tree
          System.out.println("Car is already in the tree.");
        }                      
      }
    }
    catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }    
  }
  
  /**
   * Method that swaps two vehicles, removing an old one then adding a new one after.
   * 
   * @param command a string array of the user's command
   * @param userInput a Scanner object to take user inputs.
   */
  protected static void swapVehicle(String[] command, Scanner userInput) throws IllegalStateException {
    boolean validIn = false;
    boolean validOut = false;
    Vehicle swapIn = null;
    Vehicle swapOut = null;
    Vehicle storage = null;
    String rawCommand = "";
    try {
      if (command.length != 1) { // if not of length 1 aka just "swapVehicle" it is a bad input
        throw new IllegalStateException("Bad use of the swapVehicle command.\nformat is: "
            + "swapVehicle");
      }
      else { // otherwise it is a valid swapVehicle     
        try { // take info of car being added
          while (!validOut) { // run loop until a valid car is being removed
            System.out.println("Enter VIN of the car being removed from inventory.");
            rawCommand = userInput.nextLine();
            // call helper method to check if possible VIN is properly formatted
            if (Driver.checkVIN(rawCommand)) { 
              try {
                storage = tree.remove(rawCommand); // store removed car just in case of error
                validOut = true;
              }
              catch (NoSuchElementException e) {
                System.out.println("Car is not in tree, try again."); // VIN is not in the tree case
              }                               
            }
            else { // print out message if VIN is rejected
              System.out.println("Bad VIN, try again.");
            }
          }
          System.out.println("Enter info of the car being put into inventory.");
          swapIn = Driver.vehicleMaker(userInput); // call helper method
          tree.insert(swapIn); // try to insert
          validIn = true; 
          System.out.println("Cars swapped successfully.");
        }
        catch (IllegalArgumentException e) {
          System.out.println("Swap failed, car is already in tree");
        }      
        catch (Exception e) { // runs if there was an issue inserting the new car
          tree.insert(storage); // readds the old car
          System.out.println("Bad inputs, enter new command.");
        }
      }
    }
    catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }   
  }
  
  /**
   * Method that removes vehicles from the dealership's inventory.
   * 
   * @param command a string array of the user's command
   */
  protected static void removeVehicle(String[] command) {
    try {
      if (command.length != 1) { // check if command of of not one part is somehow passed
        throw new IllegalArgumentException("Bad use of the removeVehicle command.\nformat is: "
            + "removeVehicle <vehicle's VIN number>");
      }
      if (!Driver.checkVIN(command[0])) // check if 2nd part of command is good VIN
        throw new IllegalArgumentException("Car removal failed: badly formatted VIN number.");   
      
      try {                
        tree.remove(command[0]); // try to remove using given valid VIN
        System.out.println("Car removed."); // car successfully removed
        }                            
      catch (NoSuchElementException e) { // runs if car is not in tree
        throw new IllegalArgumentException("Car removal failed: VIN number not found.");
      }
    }
    catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
   
  }
    
  /**
   * Method that rents vehicles from the dealership's inventory.
   * 
   * @param command a string array of the user's command
   */
  protected static void rentVehicle(String[] command) {
    Vehicle carToCheck;
    try {
      if (command.length != 1) { // check if command of of not one part is somehow passed
        throw new IllegalArgumentException("Bad use of the rentVehicle command.\nformat is: "
            + "rentVehicle <vehicle's VIN number>");
      }
      if (!Driver.checkVIN(command[0])) // check if 2nd part of command is good VIN
        throw new IllegalArgumentException("Car rental failed: badly formatted VIN number.");
        
      try {
        carToCheck = tree.getVehicle(command[0]); // check if VIN is in tree
      }
      catch (NoSuchElementException e) { // runs if VIN is not in tree
        throw new IllegalArgumentException("Car rental failed: VIN number not found.");
      }
      
      if (carToCheck.isRented() == true) // runs if car is in tree but rented already
        throw new IllegalArgumentException("Car rental failed: already rented.");
      else { // rent car
        carToCheck.changeRent(); // switch rented status to not rented
        System.out.println("Car rented successfully.");
      } 
    }
    catch (IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }
  }
  
  /**
   * Method that returns a vehicle to inventory by changing its rented status and setting its new
   * mileage.
   * 
   * @param command a string array of the user's command
   */
  protected static void returnVehicle(String[] command) {
    Vehicle carToCheck;
    try {
      if (command.length != 2) { // check if command of of not two parts is somehow passed
        throw new IllegalArgumentException("Bad use of the returnVehicle command.\nformat is: "
            + "returnVehicle <vehicle's VIN number> <new mileage>"); 
      }
      if (!Driver.checkVIN(command[0])) { // check if 2nd part of command is good VIN
        throw new IllegalArgumentException("Car return failed: badly formatted VIN number.");
      }
      
      try {
        carToCheck = tree.getVehicle(command[0]); // check if VIN is in tree
      }
      catch (NoSuchElementException e) { // runs if VIN is not in tree
        throw new IllegalArgumentException("Car return failed: VIN number not found.");      
      }
      
      if (carToCheck.isRented() == false) { // runs if car is in tree but not rented
        throw new IllegalArgumentException("Car return failed: not rented.");
      }
      else { // else try to return the car
        if (Integer.parseInt(command[1]) >= carToCheck.getMileage()) { // check new mileage
          carToCheck.changeRent(); // switch rented status to not rented 
          carToCheck.changeMileage(Integer.parseInt(command[1]));
          System.out.println("Car successfully returned.");
        }
        else {
          throw new IllegalArgumentException("Car return failed: new mileage is not greater than " 
              + "old mileage of " + carToCheck.getMileage()); 
        }
      }

    }
    catch(NumberFormatException e) {
      System.out.println("Car return failed: new mileage is not an integer number.");
    } 
    catch(IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }      
  }
  
  /**
   * Helper method that creates a vehicle object and sorts valid user inputs.
   * 
   * @param input a Scanner object to be used for taking user inputs.
   */
  private static Vehicle vehicleMaker(Scanner input) {
    boolean vehicleMade = false;
    String vin = ""; // store user entered VIN
    int mileage = -1; // store user entered mileage
    int year = -1; // store user entered year
    int price = -1; // store user entered price
    int toCheck; 
    String model = ""; // store user entered model
    String color = ""; // store user entered color
    String rawCommand;
    String command;
    boolean goodVin = false;
    boolean goodModel = false;
    boolean goodColor = false;
    Vehicle toReturn = null;
    ArrayList<MatchResult> storedCommand = new ArrayList<MatchResult>();
    String[] instruction = new String[6]; // store list of instructions
    int i = 0; // index to track which part of the vehicle info we are asking for (1 = vin, 5 = color)
    Pattern[] patternSet = new Pattern[6]; // store list of various regex expressions to be used
    patternSet[0] = Pattern.compile("^.*$", Pattern.CASE_INSENSITIVE); // for the VIN
    instruction[0] = "Please enter the vehicle's VIN number.";
    patternSet[1] = Pattern.compile("^[0-9]*$", Pattern.CASE_INSENSITIVE); // for the mileage
    instruction[1] = "Please enter the vehicle's mileage.";
    patternSet[2] = Pattern.compile("^[0-9]*$", Pattern.CASE_INSENSITIVE); // for the year
    instruction[2] = "Please enter the vehicle's year.";
    patternSet[3] = Pattern.compile("^[0-9]*$", Pattern.CASE_INSENSITIVE); // for the price
    instruction[3] = "Please enter the vehicle's price.";
    patternSet[4] = Pattern.compile("^.*$", Pattern.CASE_INSENSITIVE); // for the model
    instruction[4] = "Please enter the vehicle's model.";
    patternSet[5] = Pattern.compile("^.*$", Pattern.CASE_INSENSITIVE); // for the color
    instruction[5] = "Please enter the vehicle's color.";
    
    while(!vehicleMade) {
      try {
        System.out.println(instruction[i]);
        storedCommand.clear();
        rawCommand = input.nextLine().trim();
        patternSet[i].matcher(rawCommand).results().forEach(m -> storedCommand.add(m)); // store input
        
        if (storedCommand.isEmpty()) { // empty means no info entered
          System.out.println("Bad input, try again.");
        }
        else if (storedCommand.size() != 1) { // command should be one part
          System.out.println("Bad input, try again.");
        }
        else { 
          switch (i) {
            case 0: // checking VIN inputs
              command = storedCommand.get(0).group();
              if (Driver.checkVIN(command)) { // check if valid vin
                try {
                  if (tree.getVehicle(command) != null); // check if car already exists
                  System.out.println("VIN number is already in inventory.");               
                }
                catch (NoSuchElementException e) { // otherwise store input
                  vin = command;
                  System.out.println("VIN entered successfully.");
                  goodVin = true; // vin number entered
                  i++; // increment to ask for mileage next
                }                
              }
              else 
                System.out.println("Bad VIN number.");                             
              break;             
            case 1: // checking mileage inputs
              toCheck = Integer.parseInt(storedCommand.get(0).group()); // try to convert string to integer
              if (toCheck >= 0) { // check if mileage must be greater than or equal to 0
                mileage = toCheck;
                System.out.println("Mileage entered successfully.");
                i++; // increment to ask for year next
              }
              else 
                System.out.println("Bad mileage number (must be greater than or equal to 0)"); 
              break;
            case 2:
              toCheck = Integer.parseInt(storedCommand.get(0).group()); // try to convert string to integer
              if (!(toCheck < 1885)) { 
                year = toCheck; // if year is valid store it
                System.out.println("Year entered successfully.");
                i++;
              }
              else 
                System.out.println("Bad year number (must be at least 1885)."); 
              break;
            case 3: // check price input 
              toCheck = Integer.parseInt(storedCommand.get(0).group()); // try to convert string to integer
              if (toCheck >= 100) { // price must obviously be more than 100
                price = toCheck;
                System.out.println("Price entered successfully.");
                i++;
              }
              else 
                System.out.println("Bad price (must be greater than 100)"); 
              break;
            case 4: // check model input, really most any string is acceptable
              model = storedCommand.get(0).group();
              i++; 
              goodModel = true;
              System.out.println("Model entered successfully.");
              break;
            case 5:// check color input, really most any string is acceptable
              color = storedCommand.get(0).group();
              i++;
              goodColor = true;
              System.out.println("Color entered successfully.");
              break;
          }         
        }
        // runs only if all info has been entered for a valid vehicle
        if (goodVin && mileage != -1 && year != -1 && price != -1 && goodModel && goodColor) {
          toReturn = new Vehicle(vin, mileage, year, price, model, color, false);
          vehicleMade = true;        
        }                    
      }
      catch (NumberFormatException e) { // means an input could not be put into an integer
        System.out.println("Bad input, try again.");           
      }
      catch (IllegalArgumentException e) { // means vehicle could not be created
        System.out.println("Bad inputs, use command again.");  
        vehicleMade = true;       
      }      
    }
    return toReturn;
  }
  
  /**
   * Method that prints the description of a vehicle's info.
   * 
   * @param command a string array of the user's command
   */
  protected static void getDescription(String[] command) {
    Vehicle carToCheck; 
    try {
      if (command.length != 1) { // check if command of of not one part is somehow passed
        throw new IllegalArgumentException("Bad use of the getDescription command.\nformat is: "
            + "getDescription <vehicle's VIN number>"); 
      }
      if (!Driver.checkVIN(command[0])) // check if 2nd part of command is a VIN number
        throw new IllegalArgumentException("Description not found: badly formatted VIN number.");
      try {
        carToCheck = tree.getVehicle(command[0]); // check if car is in tree
      }
      catch (NoSuchElementException e) { // if not, cannot print description
        throw new IllegalArgumentException("Description not found: VIN number not found.");
      }
      System.out.println(Driver.getDescriptionHelper(carToCheck)); // otherwise call helper method
    }
    catch(IllegalArgumentException e) {
      System.out.println(e.getMessage());
    }   
  }
  
  /**
   * Method that imports vehicles from a given file by using DataHandler's import method
   * 
   * @param command a string array of the user's command
   */
  protected static void importVehicles(String[] command) {
    if (command.length != 1) { // check if command of of not one part is somehow passed
      throw new IllegalArgumentException("Bad use of the importVehicles command.\nformat is: "
          + "importVehicles <file path>"); 
    }
    try {
      DataHandler.importCars(command[0], tree);
    }
    catch (FileNotFoundException e){
     System.out.println("Import failed, " + e.getMessage());      
    }
    catch (DataFormatException e) {
      System.out.println("Import failed, " + e.getMessage());      
    }
    catch (IOException e) {
      System.out.println("Import failed, " + e.getMessage());      
    }
  }
  
  /**
   * Helper method that creates a string of the car's info
   */  
  protected static String getDescriptionHelper(Vehicle car) {
    String toReturn = "Car Info: "; // create string
    toReturn = toReturn.concat("\nVIN Number: " + car.getVIN() + ", Model: " + car.getModel()
      + ", Color: " + car.getColor() + ", Year: " + car.getYear() + "\nMileage: " + car.getMileage()
      + ", Price: " + car.getPrice());
    if (car.isRented()) // check if rented or not
      toReturn = toReturn.concat(", Rented: Yes"); 
    else
      toReturn = toReturn.concat(", Rented: No");
        
    return toReturn;
  }
  
  /**
   * Helper method that checks if a VIN is PROPERLY FORMATTED, it does not check if it is in the 
   * tree
   */  
  protected static boolean checkVIN(String vin) {
    char[] characterCheck = new char[17]; 
    if (vin.length() != 17) // check length
      return false;;
    characterCheck = new char[17]; 
    characterCheck = vin.toCharArray(); // check if inputs are just numbers and letters
    for (int i = 0; i < 17; i++) {
      if (!Character.isLetterOrDigit(characterCheck[i])) 
        return false;
      if (Character.isLetter(characterCheck[i])) { // if letter must be upper case
          if (!Character.isUpperCase(characterCheck[i]))  
        return false;     
      }      
    }
    return true;
  }
  
}
