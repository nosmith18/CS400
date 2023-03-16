// --== CS400 File Header Information ==--
// Name: Alex Vesel
// Email: avesel@wisc.edu
// Team: MA
// TA: Harit Vishwakarma
// Lecturer: Florian Heimerl
// Notes to Grader: None

import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.NoSuchElementException;
import java.io.BufferedReader;
import java.util.zip.DataFormatException;

/**
 * DataHandler class that contains static methods to handle initializing
 * and importing of vehicle inventory data from csv file.  Meets specifications
 * of UW-Madison CS400 Fall 2020 Red Black Tree Activity (P2).
 */
public class DataHandler {
    /**
     * Initializes RBT with all vehicles in cars folder.
     * 
     * @param api - RedBlackTreeAPI to import vehicles into
     * 
     * @return string containing log messages
     */
	public static String initCars(RBTreeAPI api) {
		String logString = new String("");
		File folder = new File("cars");
		// Check if cars folder exists
		if (folder.exists()) {
			// Make sure at least 1 file in cars
			if (folder.list().length == 0)
				logString += ("ERROR: No files in cars folder!\n");
			for (String file : folder.list()) {
				// Try importing file
				try {
					logString = logString.concat(importCars(file, api));
				} catch (Exception e) {
					logString = logString.concat("Failed to import " + file + ". " + e.getMessage());
				}
				logString = logString.concat("Finished importing inventory.\n");
			}
		}
		else {
			folder.mkdir();
			logString = logString.concat("Cars folding does not exist.\n");
			logString = logString.concat("Creating folder. . .\n");
			logString = logString.concat("Please initialize cars folder with inventory csv.\n");
		}
		return logString;
	}
	
    /**
     * Imports all vehicles into RedBlackTree from given filepath.
     * 
     * @param filePath - filepath to vehicle inventory csv file
     * @param api - RedBlackTreeAPI to import vehicles into
     * 
     * @return string containing file loaded message
     * 
     * @throws FileNotFoundException - if filePath doesn't exist
     * @throws DataFormatException - inventory data has incorrect format
     * @throws IOException - buffer reader exception
     */
	public static String importCars(String filePath, RBTreeAPI api) 
		throws FileNotFoundException, DataFormatException, IOException {
			if (filePath == null) 
				throw new FileNotFoundException("null is not a valid filepath.");
	        // Open file
			File file = new File(filePath);
			if(!file.exists()) 
				throw new FileNotFoundException("Filepath does not exist.");
			FileReader fileReader = new FileReader(filePath);
			BufferedReader bufferedReader = null;
			try {
				// Create reader and handle errors
				if (!file.getName().substring(file.getName().length() - 3).equals("csv")) {
					throw new DataFormatException("Vehicles must be in .csv file");
				}
				bufferedReader = new BufferedReader(fileReader);
		        // Read lines of CSV
				String line;
				String[] values;
				// Initialize vehicle properties
			    String vin;
			    int mileage;
			    int year;
			    int price;
			    String model;
			    String color;
			    boolean isRented;
				while ((line = bufferedReader.readLine()) != null) {
					// Split on comma for CSV
					values = line.split(",");
					if (values.length != 7)
						throw new DataFormatException("Lines must be formatted as [VIN number],"
								+ "[mileage],[year],[price],[model],[color],[isRented]");
					// Get vehicle properties
				    vin = values[0].trim();
				    if (vin.length() != 17)
				    	throw new DataFormatException("Vin number " + vin + " has incorrect format.");
				    mileage = Integer.parseInt(values[1].trim());
				    year = Integer.parseInt(values[2].trim());;
				    price = Integer.parseInt(values[3].trim());;
				    model = values[4].trim();
				    color = values[5].trim();
				    isRented = Boolean.parseBoolean(values[6].trim());;
			        // Create vehicle and add to table
				    Vehicle vehicle = null;
				    // Check if vehicle is valid
				    try {
				    	vehicle = new Vehicle(vin, mileage, year, price, model, color, isRented);
				    } catch (Exception e) {
				    	throw new DataFormatException("Data format is incorrect for file " + file.getName());
				    }
				    // Check if vehicle is in RBT, if not add it
			        try {
			        	api.getVehicle(vin);
			        	throw new DataFormatException("The car already exists inside inventory system.");
			        } catch (NoSuchElementException e) {
			        	api.insert(vehicle);
			        }
		        }
				return "Successfully loaded "+ file.getName()+"\n";
			} catch (FileNotFoundException e) {
				throw e;
			} catch (IOException e) {
				throw e;
			} catch (DataFormatException e) {
				throw e;
			} catch (Exception e) {
				System.out.println("Unexpected error: " + e.getMessage());
			} finally {
				try {
					if (bufferedReader != null) bufferedReader.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return "Failed import of "+ file.getName()+"\n";
	}
}
