// --== CS400 File Header Information ==--
// Name: Edward Park
// Email: ejpark@wisc.edu
// Team: MA
// Role: Back end engineer 2
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: N/A


/**
 * The class contains the constructor for Airport objects and their getter methods
 * @author Edward Park
 * @version 1.0
 */

public class Airport {
  
  private String airportCode = null;
  private String airportName = null;
  private String airportCity = null;
  private String country = null;
  private float longitude;
  private float latitude;
  
  
  /**
   * Constructor to create a new Airport object
   * 
   * @param airportCode is the airport's unique code
   * @param latitude is the airport's latitude
   * @param longitude is the airport's longitude
   * @param airportName is the airport's name
   */
  public Airport(String airportCode, float latitude, float longitude, String airportName, String airportCity, String country) {
    this.airportCode = airportCode;
    this.latitude = latitude;
    this.longitude = longitude;
    this.airportName = airportName;
    this.airportCity = airportCity;
    this.country = country;
  }
  
  /**
   * Getter for the airport's code
   * 
   * @return the airport's unique code
   */
  public String getAirportCode() {
    return this.airportCode;
  }
  
  /**
   * Getter for the airport's name
   * 
   * @return the airport's name
   */
  public String getName() {
    return this.airportName;  
  }
  
  /**
   * Getter for the airport's longitude
   * 
   * @return the airport's longitude
   */
  public float getLongitude() {
    return this.longitude;
  }
  
  /**
   * Getter for the airport's latitude
   * 
   * @return the airport's latitude
   */
  public float getLatitude() {
    return this.latitude;
  }

  /**
   * Getter for the airport's city
   * 
   * @return the airport's city
   */
  public String getAirportCity() {
    return this.airportCity;
  }
  
  /**
   * Getter for the airport's code
   * 
   * @return the airport's unique code
   */
  public String getCountry() {
    return this.country;
  }
  
}