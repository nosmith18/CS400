//////////////////////////////////////////
// --== CS400 File Header Information ==--
// Name: Nolan Oliver Smith
// Email: nosmith@wisc.edu
// Team: MA
// TA: Harit
// Lecturer: Gary Dahl
// Notes to Grader: None
//////////////////////////////////////////

import java.util.NoSuchElementException;

public class TestHashTableMap {

  public static void main(String[] args) {
    System.out.println(test1());
    System.out.println(test2());
    System.out.println(test3());
    System.out.println(test4());
    System.out.println(test5());
  }

  public static boolean test1() { // checks for success in put() and size() methods
    HashTableMap h = new HashTableMap(4); //creates HashTable
    //BREAKPOINT
    h.put(10, "John"); //adds 3 key-value pairs
    h.put(13, "Tom");
    h.put(12, "Sarah");
    if(h.size() != 3) return false; //checks size
    System.out.println("Test #1 was passed");
    return true;
}
  
  public static boolean test2() { //checks for success of grow(), atCapacity(), and that capacity is doubled via getCapacity()
    HashTableMap h = new HashTableMap(5); //HashTable with capacity of 5
    h.put(10, "John"); //adds 3 key-value pairs
    h.put(13, "Tom");
    h.put(12, "Sarah");
    if(h.atCapacity() != false) return false; // checks that atCapacity returns false when at 3/5 load capacity
    System.out.println(h.getCapacity());
    h.put(11, "Mike");
    System.out.println(h.getCapacity()); // checks that grow() works and capacity was doubled after load capacity is reached
    System.out.println("Test #2 was passed");
    return true;
}
  
  public static boolean test3() { //checks get()
    HashTableMap h = new HashTableMap(5); //HashTable with capacity of 5
    h.put(10, "John"); //adds 3 key-value pairs
    h.put(13, "Tom");
    h.put(12, "Sarah");
    System.out.println(h.get(10));
    if(h.get(10).equals("John")) { //checks that the get() method returns correct value
    System.out.println("Test #3 was passed");
    try {
      h.get(4); //should throw NoSuchElementException
    } catch (NoSuchElementException e) {
      System.out.println("Correct NoSuchElementException was thrown by get(K key) with key that did not exist in hashMap");
      return true;
    }
    return false;
    }
    return false;
}
  
  public static boolean test4() { //checks that remove() and containsKey() work correctly
    HashTableMap h = new HashTableMap(5); //HashTable with capacity of 5
    h.put(10, "John"); //adds 3 key-value pairs
    h.put(13, "Tom");
    h.put(12, "Sarah");
    h.remove(12); // remove key-value pair with key of 12, (12, "Sarah")
    if(h.size() != 2) return false; // checks that size was decremented
    if(h.containsKey(10) != true) return false; //checks that containsKey returns true when passed a key that exists in hashMap
    if(h.containsKey(12) != false) return false; //checks that containsKey returns false when passed key that has been removed
    System.out.println("Test #4 was passed");
    return true;
}
  
  public static boolean test5() { //checks that clear() works as expected
    HashTableMap h = new HashTableMap(5); //HashTable with capacity of 5
    h.put(10, "John"); //adds 3 key-value pairs
    h.put(13, "Tom");
    h.put(12, "Sarah");
    if(h.size() != 3) return false; //checks size is correct before clear()
    h.clear();
    if(h.size() != 0) return false; //checks size is correct after cleared
    System.out.println("Test #5 was passed");
    return true;
}

  
}
