// --== CS400 File Header Information ==--
// Name: Nolan Smith
// Email: nosmith@wisc.edu
// Team: MA
// Role: Backend Developer
// TA: Harit Vishwakarma
// Lecturer: Prof. Gary Dahl
// Notes to Grader: N/A

/*
 * Vehicle class whose instances will be stored within the RBTree. Each Vehicle contains a VIN number,
 * mileage, color, model, year, and values representing if the Vehicle is new/used and rented out.
 * 
 * @author - Nolan Smith
 */
public class Vehicle implements Comparable<Vehicle>{

  private String VIN;
  private int mileage;
  private int year;
  private int price;
  private String model;
  private String color;
  private boolean isRented;
  
  /**
   * Constructor for Vehicle class
   * 
   * @param vin -       the vin number of this vehicle
   * @param mileage -   the mileage of this vehicle
   * @param year -      the year of this vehicle
   * @param price -     the price of this vehicle
   * @param model -     the model of this vehicle
   * @param color -     the color of this vehicle
   * @param isRented -  T/F of if this vehicle is rented out
   */
  public Vehicle(String VIN, int mileage, int year, int price, String model,
      String color, boolean isRented) throws IllegalArgumentException {
    //check that vin is valid length and valid characters
    if (VIN.length() != 17 || VIN.matches("^[A-Z0-9]*$") != true) throw new 
      IllegalArgumentException("VIN number must be exactly 17 alphanumeric characters long.");
    this.VIN = VIN;
    
    //check that mileage is valid
    if (mileage < 0) throw new IllegalArgumentException("Mileage must be non-negative.");
    this.mileage = mileage;
    
    //check that year is valid
    if (year < 1885) throw new IllegalArgumentException("Year must be at least 1885.");
    this.year = year;
    
    //check that price is valid
    if (price < 100) throw new IllegalArgumentException("Price must be greater than $100");
    this.price = price;
    
    //rest of variables
    this.model = model;
    this.color = color;
    this.isRented = isRented;
    
  }
  
  /**
   * This method gets the VIN of this vehicle
   * 
   * @returns the VIN number of this vehicle
   */
  public String getVIN() {
    return VIN;
  }
  
  /**
   * This method gets the mileage of this vehicle
   * 
   * @returns the mileage of this Vehicle
   */
  public int getMileage() { return mileage; }
  
  /**
   * This method gets the year of this vehicle
   * 
   * @returns the year this vehicle was made
   */
  public int getYear() { return year; }
  
  /**
   * This method gets the price of this vehicle
   * 
   * @returns the price of this vehicle
   */
  public int getPrice() { return price; }
  
  /**
   * This method gets the boolean isRented of this vehicle
   * 
   * @returns isRented boolean, True if vehicle is rented out, false otherwise
   */
  public boolean isRented() { return isRented; }
  
  /**
   * This method gets the model of the vehicle
   * 
   * @return - Model of the Vehicle
   */
  public String getModel() { return model; }
  
  /**
   * This method gets the color of the Vehicle
   * 
   * @return - Color of the Vehicle
   */
  public String getColor() { return color; }
  
  /**
   * This method returns whether or not the Vehicle is new base on mileage
   * 
   * @return True - if mileage is 0, False if otherwise
   */
  public boolean isNew() {
    if ( mileage == 0 ) {
      return true;
    }
    return false;
  }
  
  /**
   * This method changes mileage to newMileage value 
   * newMileage must be larger than current mileage
   * 
   * @param newMileage - the new Mileage of the Vehicle
   * @returns True if successfully changed, false if mileage was invalid(smaller than current mileage)
   */
  public boolean changeMileage(int newMileage) {
    if ( newMileage >= mileage ) {
      mileage = newMileage;
      return true;
    }
    else return false;
  }
  
  /**
   * This method always switches the value of isRented to the opposite boolean value
   *    if true it switches to false, and if false it switches to true.
   */
  public void changeRent() {
    isRented = !isRented;
  }
  
  /**
   * This method sets price if newPrice is less than current price
   * 
   * @param newPrice
   * @return true if price is changed to newPrice, false otherwise
   */
  public boolean setPrice(int newPrice) {
    if (newPrice <= price && newPrice >= 100) {
      price = newPrice;
      return true;
    }
    return false;
  }
  
  /**
   * @returns a String representation of this vehicle
   */
  public String toString() {
    return color + " " + model + " with VIN: " + VIN;
  }
  
  /**
   * @param other - another vehicle object to be compared to this one
   * @return 0 if Vehicles have the same VIN number, a number > 0 if this VIN is larger,
   *        and a negative number if this VIN is smaller
   */
  @Override
  public int compareTo(Vehicle other) {
    return VIN.compareTo(other.getVIN()); 
  }

}
