// --== CS400 File Header Information ==--
// Name: Caelan Kleinhans
// Email: ckleinhans@wisc.edu
// Team: MA
// Role: Back End Developer 2
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Class that represents unique items in the shopping system.
 * 
 * @author Caelan Kleinhans
 *
 */
public class Item {
  
  // Would be used if we could host images locally with the website, but since that didn't work
  // this default path is currently unused.
  private static String DEFAULT_IMG_PATH = "./images/default.png";
  
  private String name;
  private int stock;
  private double price;
  private String imagePath;
  private String aisleLocation;
  
  /**
   * Creates a new Item object to represent a unique type of item using a custom image. If the given
   * image doesn't exist, it uses the default image.
   * 
   * @param name of the item
   * @param price of the item
   * @param initialStock of the item
   * @param imgPath path to the .png file for the item
   * @param aisleLocation the aisle number where the item exists
   * @throws IllegalArgumentException if any of the inputs are invalid
   * @throws FileNotFoundException if the image path is not found
   */
  public Item(String name, double price, int initialStock, String imgPath, String aisleLocation) 
      throws IllegalArgumentException, FileNotFoundException{
    if (price < 0) 
      throw new IllegalArgumentException("The item price must not be a negative number.");
    if (initialStock < 0)
      throw new IllegalArgumentException("The initial stock must not be a negative number");
    
    // We don't check if the images exist anymore to allow the website to function as there were
    // some difficulties accessing the images from the HTML page.
    
    File image = new File(imgPath);
//    if (!image.exists()) {
//      image = new File(DEFAULT_IMG_PATH);
//      if (!image.exists())
//        throw new FileNotFoundException("The default item image doesn't exist in the location "
//            + DEFAULT_IMG_PATH + ". Please replace the default image.");
//    }
//    if (!image.getName().matches(".*\\.png"))
//      throw new IllegalArgumentException("The image must be a .png file.");
    imgPath = image.getPath();
    
    this.name = name;
    this.price = Math.round(price*100.0)/100.0;
    this.stock = initialStock;
    this.imagePath = imgPath;
    this.aisleLocation = aisleLocation;
  }
  
  /**
   * Constructor if an aisle location is not given, defaults to N/A for the aisle.
   * 
   * @param name of the item
   * @param price of the item
   * @param initialStock of the item
   * @param imgPath path to the .png file for the item
   * @throws IllegalArgumentException if any of the inputs are invalid
   * @throws FileNotFoundException if the image path is not found
   */
  public Item(String name, double price, int initialStock, String imgPath) 
      throws IllegalArgumentException, FileNotFoundException{
    this(name, price, initialStock, imgPath, "N/A");
  }
  
  
  /**
   * Gets the name of the item.
   * 
   * @return the item's name
   */
  public String getName() {
    return name;
  }

  /**
   * Gets the current stock of the item.
   * 
   * @return the stock
   */
  public int getStock() {
    return stock;
  }

  /**
   * Sets the current stock of the item.
   * 
   * @param stock the stock to set
   */
  public void setStock(int stock) {
    if (stock < 0)
      throw new IllegalArgumentException("The stock must not be a negative number");
    this.stock = stock;
  }

  /**
   * Gets the current price of the item.
   * 
   * @return the price
   */
  public double getPrice() {
    return price;
  }
  
  /**
   * Gets the aisle the item is in in the store.
   * 
   * @return the aisle the item is in
   */
  public String getAisle() {
    return aisleLocation;
  }

  /**
   * Sets the current price of the item.
   * 
   * @param price the price to set
   */
  public void setPrice(double price) {
    if (price < 0)
      throw new IllegalArgumentException("The price must not be a negative number");
    this.price = Math.round(price*100.0)/100.0;
  }

  /**
   * Gets the path of the image of the item.
   * 
   * @return the image path
   */
  public String getImagePath() {
    return imagePath;
  }
  
  /**
   * Checks if two Items are the same. Items are the same if their names are the same.
   * 
   * @param other item to compare to
   * @return true if the items are equal, false otherwise
   */
  public boolean equals(Item other) {
    return this.name.equals(other.getName());
  }
  
}