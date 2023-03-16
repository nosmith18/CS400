// --== CS400 File Header Information ==--
// Name: Alex Vesel
// Email: avesel@wisc.edu
// Team: MA
// Role: Test Engineer 2
// TA: Harit Vishwakarma
// Lecturer: Florian Heimerl
// Notes to Grader: None


import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;


/**
 * TestBench class for CS400 Fall 2020 Graph/Dijkstra's Algo Project using JUnit5
 */
public class TestBench {
  protected FlightAPI api = null;	// API that will be used for tests
	
  /**
   * Initializes FlightAPI with flight data
   */
  @BeforeEach
  void initialize() {
	  api = new FlightAPI();
	  try {
		  DataHandler.buildGraph(api);
	  } catch (Exception e) {
		  fail(e.getMessage());
	  }
  }
  
  /**
   * Test airport constructor and get methods perform correctly
   */
  @Test
  void testAirportConstructor() {
	  Airport airport = new Airport("ABC", 10.0f, 10.0f, 
			  "Awesome Airport", "Madison", "USA");
	  if (airport.getAirportCity() != "Madison")
		  fail("Airport returned incorrect city");
	  if (airport.getLongitude() != 10.0f)
		  fail("Airport returned incorrect longitude");
	  if (airport.getName() != "Awesome Airport")
		  fail("Airport returned incorrect name");
  }
  
  /**
   * Tests DataHandler throws exception when given bad input argument
   */
  @Test
  void testBadDataHandlerInput() {
	FlightAPI badAPI = null;
	try {
		DataHandler.buildGraph(badAPI);
		fail("DataHandler.buildGraph() should fail with bad API input");
	} catch (Exception e) {
		;
	}
  }
  
  /**
   * Tests if correct cities are in graph.
   */
  @Test
  void testCityInGraph() {
	  // Check for city that is in graph
	  if (api.checkCity("Chicago") == null)
		  fail("Chicago should be in graph.");
	  // Check for city that is not in graph
	  if (api.checkCity("asdfasdf") != null)
		  fail("Fake city should not be in graph.");
  }
  
  /**
   * Tests if correct airports are in graph.
   */
  @Test
  void testAirportInGraph() {
	  // Check for airport that is in graph
	  if (api.containsAirport("MKE") == false)
		  fail("Airport MKE should be in graph.");
	  // Check for city that is not in graph
	  if (api.containsAirport("asdfasdf") != false)
		  fail("Fake airport should not be in graph.");
  }
  
  /**
   * Tests if correct flight connections are in graph.
   */
  @Test
  void testConnectionInGraph() {
	  // Check for connection that is in graph
	  if (api.containsConnection("ANC", "SEA") == false)
		  fail("Connection between ANC and SEA should be in graph.");
	  // Check for connection that is not in graph
	  if (api.containsConnection("SEA", "MKE") != false)
		  fail("Connection between SEA and MKE should not be in graph.");
	  // Check throws error for airport that is not in graph
	  try {
		  api.containsConnection("ASDF", "FDAS");
		  fail("Fake airport should not be in graph.");
	  } catch (Exception e) {
		  ;
	  }
	  // Check inputting null airports
	  try {
		  api.containsConnection(null, null);
		  fail("Should not be allowed to check connection between null airports.");
	  } catch (Exception e) {
		  ;
	  }
  }
  
  /**
   * Tests that direct flights (flights containing only 1 edge) return the correct
   * distance from FlightAPI.calcPath().
   */
  @Test
  void testDirectFlight() {
	  if (api.calcPath("ANC", "SEA").distance != 1746)
		  fail("Incorrect flightpath for direct connection ANC to SEA");
	  if (api.calcPath("CVG", "MSN").distance != 390)
		  fail("Incorrect flightpath for direct connection CVG to MSN");
	  if (api.calcPath("JFK", "SXM").distance != 1007)
		  fail("Incorrect flightpath for direct connection JFK to SXM");
  }
  
  /**
   * Tests that connection flights (flights containing more than 1 edge) return the 
   * correct distance from FlightAPI.calcPath().
   */
  @Test
  void testConnectionFlight() {
	  if (api.calcPath("MKE", "SEA").distance != 3425)
		  fail("Incorrect flightpath for direct connection MKE to SEA");
	  if (api.calcPath("MKE", "JFK").distance != 1198)
		  fail("Incorrect flightpath for direct connection MKE to JFK");
	  if (api.calcPath("MAD", "MKE").distance != 6934)
		  fail("Incorrect flightpath for direct connection MAD to MKE");
  }

  /**
   * Tests that connections can be removed from graph.
   */
  @Test
  void testRemoveConnection() {
	 api.removeConnection("ANC", "SEA");
	 // Check that connection was removed
	  if (api.containsConnection("ANC", "SEA") == true)
		  fail("Connection between ANC and SEA should be removed from graph.");
	 api.removeConnection("CVG", "MSN");
	 // Check that connection was removed
	  if (api.containsConnection("CVG", "MSN") == true)
		  fail("Connection between CVG and MSN should be removed from graph.");
  }
}
