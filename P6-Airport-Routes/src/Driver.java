// --== CS400 File Header Information ==--
// Name: Caelan Kleinhans
// Email: ckleinhans@wisc.edu
// Team: MA
// Role: Front End Developer 2
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.util.Scanner;

/**
 * Driver method for the Flight Path Calculator Application.
 * 
 * @author Caelan Kleinhans
 *
 */
public class Driver {
  
  protected static FlightAPI flightMap;
  
  /**
   * Main driver method which is run to start the Flight Path Calculator Application.
   * 
   * @param args unused
   */
  public static void main(String[] args) {
    
    System.out.println("\n\n--------------------------------------------------------");
    System.out.println("   Welcome to the Flight Path Calculator Application");
    System.out.println("--------------------------------------------------------\n");
    
    flightMap = new FlightAPI();
    
    // Load all airport and routes using DataHandler
    System.out.println("Loading airports and routes...");
    try {
      DataHandler.buildGraph(flightMap);
    } catch (Exception e) {
      System.out.println("Error while loading the airports and flight map:");
      System.out.println(e.getMessage());
      return;
    }
    System.out.println("Successfully loaded airports and routes!");
    
    // Print initial help for user
    help();
    
    Scanner scanner = new Scanner(System.in);
    String[] input = scanner.nextLine().trim().split(" ");
    
    // While user hasn't quit, run loop and call command's corresponding function
    while (!input[0].equals("quit")) {
      switch (input[0]) {
        case "help":
          help();
          break;
        case "destinations":
          listAirports();
          break;
        case "search":
          searchAirports(input);
          break;
        case "route":
          getRoute(input);
          break;
        case "info":
          getInfo(input);
          break;
        default:
          if (!input[0].equals("quit")) {
            System.out.println("Error: " + input[0] + " is an unrecognized commmand.");
            System.out.println("Use 'help' to see a list of valid commands.");
          }
      }
      input = scanner.nextLine().trim().split(" ");
    }
    
    // Close scanner and print exit message
    scanner.close();
    
    System.out.println("\n--------------------------------------------------------");
    System.out.println("    Thank you for using the Flight Path Calculator!");
    System.out.println("--------------------------------------------------------\n");
  }
  
  /**
   * Prints a command list with usages to the console.
   */
  protected static void help() {
    System.out.println("\n--------------------------------------------------------");
    System.out.println("Commands:");
    System.out.println("help: displays this command list");
    System.out.println("destinations: displays a list of all airports");
    System.out.println("search <city>: searches for an airport given a city name");
    System.out.println("info <code>: displays information given the airport code");
    System.out.println("route <start> <end>: finds the shortest route between");
    System.out.println("                     airports given their codes");
    System.out.println("quit: exit the application");
    System.out.println("--------------------------------------------------------");
  }
  
  /**
   * Prints a list of all airports and their codes to the console.
   */
  protected static void listAirports() {
    Airport[] airports = flightMap.listAll();
    System.out.println("\n--------------------------------------------------------");
    System.out.println("Airport List:");
    for (Airport airport : airports) {
      System.out.println(airport.getAirportCode() + " - " + airport.getName() + " ("
          + airport.getAirportCity() + ", " + airport.getCountry() + ")"); // TODO
    }
    System.out.println("--------------------------------------------------------");
  }
  
  /**
   * Searches through the loaded airports for a matching city and returns the airport information.
   * 
   * @param args user input
   */
  protected static void searchAirports(String[] args) {
    if (args.length < 2) {
      System.out.println("Error: Improper usage. Usage: search <city>");
      return;
    }
    
    // Gets the city name and finds a list of all matching airports
    String city = "";
    for (int i = 1; i < args.length; i++) {
      city += args[i] + " ";
    }
    Airport[] airports = flightMap.checkCity(city.trim());
    
    // Prints all airports with given city
    System.out.println("\n--------------------------------------------------------");
    System.out.println("Search results for '" + city.trim() + "' :");
    if (airports == null) {
      System.out.println("No results");
    } else {
      for (Airport airport : airports) {
        System.out.println(airport.getAirportCode() + " - " + airport.getName());
      }
    }
    System.out.println("--------------------------------------------------------");
  }
  
  /**
   * Displays info on an airport given the airport code.
   * 
   * @param args user input
   */
  protected static void getInfo(String[] args) {
    if (args.length < 2) {
      System.out.println("Error: Improper usage. Usage: info <code>");
      return;
    }
    if (!flightMap.containsAirport(args[1].toUpperCase())) {
      System.out.println("Error: Airport with code " + args[1].toUpperCase() + " not found.");
      return;
    }
    
    Airport airport = flightMap.searchByCode(args[1].toUpperCase());
    System.out.println("\n--------------------------------------------------------");
    System.out.println("Name: " + airport.getName());
    System.out.println("Airport Code: " + airport.getAirportCode());
    System.out.println("Location: " + airport.getAirportCity() + ", " + airport.getCountry());
    System.out.println("--------------------------------------------------------");
  }
  
  /**
   * Finds the shortest path between two airports and prints the details.
   * 
   * @param args user input
   */
  protected static void getRoute(String[] args) {
    if (args.length < 3) {
      System.out.println("Error: Improper usage. Usage: route <start> <end>");
      return;
    }
    
    args[1] = args[1].toUpperCase();
    args[2] = args[2].toUpperCase();
    
    // Try to find a route, if error, catch and display the error message
    CS400Graph<Airport>.Path route;
    try {
      route = flightMap.calcPath(args[1], args[2]);
    } catch (Exception e) {
      // IllegalArgumentException if the start or end doesn't exist
      // NoSuchElementException if no path is found from the start to the end
      System.out.println("Error: " + e.getMessage());
      return;
    }
    
    // Route found. Display the path
    System.out.println("\n--------------------------------------------------------");
    System.out.println("Shortest route from " + args[1] + " to " + args[2] + ":");
    Airport airport = route.dataSequence.get(0);
    System.out.println(airport.getAirportCode() + " - " + airport.getName());
    for (int i = 1; i< route.dataSequence.size(); i++) {
      airport = route.dataSequence.get(i);
      System.out.println("  -> " + airport.getAirportCode() + " - " + airport.getName());
    }
    System.out.println("\nTotal Distance: " + route.distance + " kilometers");
    System.out.println("--------------------------------------------------------");
  }

}
