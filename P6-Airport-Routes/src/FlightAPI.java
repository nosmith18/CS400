// --== CS400 File Header Information ==--
// Name: Edward Park
// Email: ejpark@wisc.edu
// Team: MA
// Role: Back end engineer 2
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: N/A

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * The class contains the FlightAPI's methods.
 * 
 * @author Edward Park
 * @version 1.0
 */

public class FlightAPI {

  // hash table of airports related to their keys, mostly to easily check if a given airport exists
  private HashMap<String, Airport> airports = new HashMap<String, Airport>();
  // maintain two lists of cities and their airports related by their index, the list of airports of
  // a city
  // are at the same index the city is stored in the names list
  private LinkedList<LinkedList<Airport>> citiesAirports = new LinkedList<LinkedList<Airport>>();
  private LinkedList<String> citiesNames = new LinkedList<String>();
  // graph of all the airport connections
  private CS400Graph<Airport> airportGraph = new CS400Graph<Airport>();

  /**
   * Calculates distances between two airports indicated by their unique codes
   * 
   * @param start the start airport's unique code
   * @param end   the end airport's unique code
   * @return true if adding a connection (inserting an edge) was successful, false otherwise
   */
  public boolean addConnection(String start, String end)
      throws NullPointerException, IllegalArgumentException {
    Airport startAirport;
    Airport endAirport;

    if (start == null || end == null) // check if key inputs are valid (not null and their related
                                      // airports exist)
      throw new NullPointerException("Null values are not a valid input.");
    else if (!airports.containsKey(start))
      throw new IllegalArgumentException("Given start airport does not exist.");
    else if (!airports.containsKey(end))
      throw new IllegalArgumentException("Given end airport does not exist.");
    startAirport = airports.get(start); // get the airport object to add outgoing connection to
    endAirport = airports.get(end); // get the airport object to add incoming connection to

    // insert an edge and use the helper method calcDistance to work out the distance between the
    // two airports
    return airportGraph.insertEdge(startAirport, endAirport,
        calcDistance(startAirport, endAirport));
  }

  /**
   * Adds an airport to our hash table and as a vertex in our graph
   * 
   * @param toAdd the airport to add
   * @return true if adding the airport was successful, false otherwise
   */
  public boolean addAirport(Airport toAdd) {
    int index;
    LinkedList<Airport> airportList;
    if (toAdd == null) // check input
      return false; // if airport is null clearly we can't add it
    if (!airports.containsKey(toAdd.getAirportCode())) { // check if airport actually exists
      airports.put(toAdd.getAirportCode(), toAdd); // add airport to hash table if it isn't in
                                                   // already
      if (citiesNames.contains(toAdd.getAirportCity().toLowerCase())) { // check if city has
                                                                        // airports already
        index = citiesNames.indexOf(toAdd.getAirportCity().toLowerCase()); // if it does then add
                                                                           // airport to the list of
                                                                           // airports it has
        citiesAirports.get(index).add(toAdd);
      } else { // otherwise add it to our list of cities and create a new linked list with just our
               // first airport
        citiesNames.add(toAdd.getAirportCity().toLowerCase());
        airportList = new LinkedList<Airport>();
        airportList.add(toAdd);
        citiesAirports.add(airportList);
      }
      return airportGraph.insertVertex(toAdd); // add as new vertex to our graph
    } else // if not return false
      return false;
  }

  /**
   * Removes an airport from our hash table, and graphs
   * 
   * @param toRemove the unique code of the airport to remove
   * @return true if removing the airport was successful, false otherwise
   */
  public boolean removeAirport(String toRemove) {
    int index;
    LinkedList<Airport> airportList;
    boolean removeHashes;
    if (toRemove == null) // check input
      return false;
    else if (!airports.containsKey(toRemove)) // check if airport is in table
      return false;
    // remove airport as a vertex and from the tables and lists
    index = citiesNames.indexOf(airports.get(toRemove).getAirportCity().toLowerCase());
    airportList = citiesAirports.get(index);
    // if it is the only airport a city has we must set its list of airports to null and remove the
    // city from the list of cities
    if (airportList.size() == 1) {
      citiesAirports.set(index, null);
      citiesNames.remove(index);
    } else { // otherwise remove it from the list of airports the city has
      airportList.remove(airports.get(toRemove));
      citiesAirports.set(index, airportList); // put new list back in
    }

    return airportGraph.removeVertex(airports.get(toRemove)) && airports.remove(toRemove) == null;
  }

  /**
   * Checks if an airport is in our hash table and graph
   * 
   * @param check the unique code of the airport to check
   * @return true if the airport is in our hash table and graph, false otherwise
   */
  public boolean containsAirport(String check) {
    if (check == null) // check input
      return false;

    if (airports.containsKey(check)) // check if it is actually in our table
      return airportGraph.containsVertex(airports.get(check)); // verify just in case it is also in
                                                               // the graph
    return false;
  }

  /**
   * Checks if two airports have an edge between them using their two unique codes
   * 
   * @param start the start airport's unique code
   * @param end   the end airport's unique code
   * @return true if a connection exists, false otherwise
   */
  public boolean containsConnection(String start, String end)
      throws NullPointerException, IllegalArgumentException {
    if (start == null || end == null) // check if key inputs are valid (not null and their related
                                      // airports exist)
      throw new NullPointerException("Null values are not a valid input.");
    else if (!airports.containsKey(start))
      throw new IllegalArgumentException("Given start airport does not exist.");
    else if (!airports.containsKey(end))
      throw new IllegalArgumentException("Given end airport does not exist.");
    // check if the airports have an edge connecting them
    return airportGraph.containsEdge(airports.get(start), airports.get(end));
  }

  /**
   * Removes the connection between two airports using their two unique codes
   * 
   * @param start the start airport's unique code
   * @param end   the end airport's unique code
   * @return true if removing their connection was successful, false otherwise
   */
  public boolean removeConnection(String start, String end)
      throws NullPointerException, IllegalArgumentException {
    if (start == null || end == null) // check if key inputs are valid (not null and their related
                                      // airports exist)
      throw new NullPointerException("Null values are not a valid input.");
    else if (!airports.containsKey(start))
      throw new IllegalArgumentException("Given start airport does not exist.");
    else if (!airports.containsKey(end))
      throw new IllegalArgumentException("Given end airport does not exist.");
    // remove edge between the two airports, if a connection doesn't exist then it's just false
    // anyway
    return airportGraph.removeEdge(airports.get(start), airports.get(end));
  }

  /**
   * Calculates the shortest path between two airports using their two unique codes
   * 
   * @param start the start airport's unique code
   * @param end   the end airport's unique code
   * @return the shortest path between the two airports
   */
  public CS400Graph<Airport>.Path calcPath(String start, String end)
      throws NullPointerException, IllegalArgumentException {
    if (start == null || end == null) // check if key inputs are valid (not null and their related
                                      // airports exist)
      throw new NullPointerException("Null values are not a valid input.");
    else if (!airports.containsKey(start))
      throw new IllegalArgumentException("Given start airport does not exist.");
    else if (!airports.containsKey(end))
      throw new IllegalArgumentException("Given end airport does not exist.");
    // calculate shortest path
    return airportGraph.dijkstrasShortestPath(airports.get(start), airports.get(end));
  }

  /**
   * Creates an array of all our airport objects using our hash table
   * 
   * @return an array of all our airport objects
   */
  public Airport[] listAll() {
    Airport[] listed = new Airport[airports.size()];
    airports.values().toArray(listed);
    return listed;
  }

  /**
   * Creates an array of the airports related to the given city name
   * 
   * @param cityName the city name to search our lists of airports and cities with
   * @return an array of the airports related to the given city name, null if the city has no
   *         airports
   */
  public Airport[] checkCity(String cityName) {
    int index = citiesNames.indexOf(cityName.toLowerCase()); // get city name's index (-1 means it
                                                             // isn't in our list)
    LinkedList<Airport> airportList;
    if (index == -1) // if it isn't in our list return null
      return null;
    else { // fetch list of airports related to the city and return it
      airportList = citiesAirports.get(index);
      return airportList.toArray(new Airport[airportList.size()]);
    }
  }

  /**
   * Returns the airport related to the given code.
   * 
   * @param code the code of the airport we are looking for
   * @return an airport object or null if the code doesn't correspond to an airport
   */
  public Airport searchByCode(String code) {
    return airports.get(code);
  }

  /**
   * Calculates the distance between two airports using the Haversine formula
   * 
   * @return the truncated integer distance of the double calculation of the distance between the
   *         two airports
   */
  private static int calcDistance(Airport starting, Airport ending) {
    double longDiff = (ending.getLongitude() - starting.getLongitude()) * Math.PI / 180;
    double latDiff = (ending.getLatitude() - ending.getLatitude()) * Math.PI / 180;

    double insideRoot =
        Math.pow(Math.sin(latDiff / 2), 2) + Math.cos(starting.getLatitude() * Math.PI / 180)
            * Math.cos(ending.getLatitude() * Math.PI / 180) * Math.pow(Math.sin(longDiff / 2), 2);
    int distanceBetween =
        (int) (12742 * Math.atan2(Math.sqrt(insideRoot), Math.sqrt(1 - insideRoot))); // 12742 =
                                                                                      // 2*(radius
                                                                                      // of the
                                                                                      // earth)

    return distanceBetween;

  }
}
