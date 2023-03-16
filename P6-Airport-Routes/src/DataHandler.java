import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.zip.DataFormatException;

// --== CS400 File Header Information ==--
// Name: Nolan Smith
// Email: nosmith@wisc.edu
// Team: MA
// Role: Data Wrangler
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: None

/**
 * This DataHandler class imports the Airport and Route data from two csv files 
 * into the FlightAPI graph.
 * 
 * @author Nolan Smith
 *
 */
public class DataHandler {

  /**
   * This method takes in the FlightAPI graph that will be populated with Airports
   * and routes between the airports. Also checks that the files exist. 
   * 
   * @param FlightAPI graph - instance of FlightAPI to be populated
   * @return True - if all airports and routes are added without error
   * @throws FileNotFoundException - when a csv file could not be found in routes or airports
   * @throws IOException - if the BufferedReader in the import helper methods encounters an IOException
   * @throws DataFormatException - if the formatting of a file is incorrect
   */
  public static boolean buildGraph(FlightAPI graph) 
      throws FileNotFoundException, DataFormatException, IOException {
      
      //Folder of routes
      File folder = new File("routes");
      if (folder.exists()) {
          //Check for files in this folder
          if (folder.list().length == 0) throw new FileNotFoundException("No route files were found in the routes folder.");
          for (String routeFile : folder.list()) { //send file to importRoutes
              importRoutes("./routes/" + routeFile, graph);
          }
      } else {
          throw new FileNotFoundException("The routes folder was not found. Unable to import.");
      }
      
      return true;
  }

  
  /**
   * This method takes in a fileName and FlightAPI instance and populates the graph with the contents
   * of the csv file.
   * 
   * @param String fileName - the name of the routes csv file to be read
   * @param FlightAPI graph - the graph to be populated 
   * @return True - if all routes are added to the graph
   * @throws FileNotFoundException - when a csv file could not be found in routes or airports
   * @throws IOException - if the BufferedReader in the import helper methods encounters an IOException
   * @throws DataFormatException - if the formatting of a file is incorrect
   */
  protected static boolean importRoutes(String fileName, FlightAPI graph) 
      throws FileNotFoundException, DataFormatException, IOException {
    
      //File routeFile = new File(fileName);
      FileReader read = new FileReader(fileName);
      BufferedReader buffer = new BufferedReader(read);
      String currRow;
      String[] cols;
      
      while ((currRow = buffer.readLine()) != null ) { //check that end of Stream has not been reached
          cols = currRow.split(","); //split the line by the commas
          if (cols.length != 2) { 
              buffer.close();
              throw new DataFormatException("This route file is formatted incorrectly. Correct format is: "
                  + "[Source Airport Code],[Destination Airport Code]");
          }
          
          //Check if airports are already in graph. If not, add them
          if (graph.containsAirport(cols[0].trim()) == false) {
              loadAirportFiles(cols[0].trim(), graph);
          }
          if (graph.containsAirport(cols[1].trim()) == false) {
              loadAirportFiles(cols[1].trim(), graph);
          }
          //Now make a connection
          try {
              graph.addConnection(cols[0].trim(), cols[1].trim());
          } catch (Exception e) {
              buffer.close();
              throw new DataFormatException("This route file is formatted incorrectly. Correct format is: "
                  + "[Source Airport Code],[Destination Airport Code]");
          }
      }
    
      buffer.close();
      return true;
  }

  /**
   * This method loads the Airport CSV files for proper airport importing. If file isnt found,
   * throws exceptions.
   * @param String airportCode - the codeName of an airport
   * @param FlightAPI graph - the graph to be populated
   * @return True - if the airport files
   */
  protected static boolean loadAirportFiles(String airportCode, FlightAPI graph) 
      throws FileNotFoundException, DataFormatException, IOException {
      File airportFiles = new File("airports"); //load the airports folder with CSV files
      if (airportFiles.exists() == false) {
        throw new FileNotFoundException("Airport folder was not found. Failed to load airports.");
      } else { //continue with importing
          if (airportFiles.list().length == 0 ) throw new FileNotFoundException("No CSV files within this folder.");
          for (String fileName : airportFiles.list()) {
              //Send this file to the importAirport method
              if (importAirport("./airports/" + fileName, airportCode, graph)) return true;
          }
      }
      return false; //returns false if airport is not loaded
  }

  /**
   * This import method takes in the fileNames of airport files and adds the Airports to the graph
   * by constructing new Airport instances from the CSV file data.
   * @param String fileName - the airport file to be read and loaded into graph
   * @param String airportCode - 
   * @param FlightAPI graph - the graph to be populated
   * @return
   */
  protected static boolean importAirport(String fileName, String airportCode, FlightAPI graph) throws DataFormatException, IOException{
      FileReader read = new FileReader(fileName);
      BufferedReader buffer = new BufferedReader(read);
      String currLine;
      String[] cols;
      
      while ((currLine = buffer.readLine()) != null) { //While nextLine is not null(empty file or end of file)
          cols = currLine.split(",");
          if (cols[0].trim().equals(airportCode)) { //Found the desired airportCode
              //check for correct format of airport line
              if (cols.length != 6) {
                buffer.close();
                throw new DataFormatException("Invalid airport data format. Correct"
                    + " format for airport data is [Airport Code], [Full Name], [City], [Country], [Latitude], [Longitude]");
              }
              //Make Airport object and add it to the graph
              try { 
                  Airport newAirport = new Airport(cols[0].trim(), Float.parseFloat(cols[4].trim()), Float.parseFloat(cols[5].trim()), 
                        cols[1].trim(), cols[2].trim(), cols[3].trim());
                  graph.addAirport(newAirport); //add airport to graph
                  return true; //true if successfully added with no error
              } catch (Exception e) {
                  buffer.close();
                  throw new DataFormatException("Invalid airport data format. Correct"
                    + " format for airport data is [Airport Code], [Full Name], [City], [Country], [Latitude], [Longitude]");
              }
          }
      }
      buffer.close();
      return false;
  }
  
  
}
