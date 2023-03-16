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
import java.util.LinkedList;

public class HashTableMap<K, V> implements MapADT<K, V>{

  private K key;
  private V value;
  private Pair current; //current key-value pair
  private LinkedList[] hashMap; //HashTable
  private int size; //# of key-value pairs
  private int capacity; //Capacity of HashTable
      
  public HashTableMap(int capacity) { // Constructor w/ capacity input
    hashMap = new LinkedList[capacity];
    size = 0;
    this.capacity = capacity;
  }
  
  public HashTableMap() { // Constructor w/o capacity input
    hashMap = new LinkedList[10];
    size = 0;
    this.capacity = 10;
  }
  
  /*
   * This method returns true if the HashTable is above load capacity(80%)
   * 
   * @returns true if over load capacity, false if below load capacity(80%)
   */
  protected boolean atCapacity() {
    if((double)size/capacity*100 >= 80) { //calculates current load capacity
      return true;
    }
    else return false;
  }
  
  /*
   * This method returns the current capacity of the HashMap
   * 
   * @returns capacity
   */
  protected int getCapacity() {
    return capacity;
  }
  
  /*
   * This grow method doubles the size of the hashMap and rehashes all key-value pairs into the newHashMap
   */
  protected void grow() {
    LinkedList<Pair> currentHash = new LinkedList<Pair>();
    capacity = capacity*2; // doubles capacity
    LinkedList[] newHashMap = new LinkedList[capacity]; //makes newHashMap w/ new capacity
    for(int i = 0; i<hashMap.length; i++) { //rehashing all pairs in hashMap to the newHashMap
      if(hashMap[i] != null) {      //checks if this hashIndex is null in old hashMap
        currentHash = hashMap[i];   // Copy of LinkedList at current hashIndex(i)
        for(Pair x : currentHash) {   // Iterates through each Key-Value pair at this hashIndex
          int hashIndex = Math.abs(x.getKey().hashCode())%capacity;   //gets hashIndex of current key using new capacity to calculate
          if(newHashMap[hashIndex] == null) {     //checks if current hashIndex is null
            newHashMap[hashIndex] = new LinkedList<Pair>();   //makes new LinkedList
            newHashMap[hashIndex].add(x);     //adds Pair to LinkedList
          }
          else newHashMap[hashIndex].add(x);      //adds Pair to LinkedList at hashIndex if not null
        }
      }
    }
    hashMap = newHashMap;   //copies newHashMap back to hashMap to finalize growth and rehashing
  }
  
  
  
  /*
   * This method stores new values in the hash table at hash index that corresponds to
   * the key of the key-value pair. 
   * 
   * @param K key, V value of key-value pair to be added
   * 
   * @returns true if Pair is added, false if it already exists/is a duplicate
   */
  @Override
  public boolean put(K key, V value){
    this.key = key; //saves key and value to private variables
    this.value = value;
    current = new Pair(key,value); // makes new Pair for the current key-value pair that put is given
    if(containsKey(key)) { return false;} // returns false without changing hash table if 
    else  if(atCapacity()) { //checks if hashMap is at capacity
        grow(); //grows the hashMap to new size and rehashes key-value pairs
    }
    int hashIndex = Math.abs(key.hashCode())%capacity; //hashIndex that corresponds to the key
    if(hashMap[hashIndex] == null) {
      hashMap[hashIndex] = new LinkedList<Pair>(); //if no value at this hashIndex, makes new LinkedList and adds Pair
      hashMap[hashIndex].add(current);
      size++; // increments size
      if(atCapacity()) { //checks if hashMap is at capacity
        grow(); //grows the hashMap to new size and rehashes key-value pairs
      }
      return true;
    } else
      hashMap[hashIndex].add(current); //if not null adds current Pair to LinkedList at this hashIndex
      size++; //increments size
      if(atCapacity()) { //checks if hashMap is at capacity
        grow(); //grows the hashMap to new size and rehashes key-value pairs
    }
      return true;
  }

  /*
   * This method returns the value that corresponds to the K key that is passed to get(). 
   * 
   * @param K key that will be searched for in the hashMap
   * 
   * @returns V value that is paired with the K key parameter
   * 
   * @throws NoSuchElementException if a key-value Pair matching the key parameter does not exist in 
   *         the hashMap
   */
  @Override
  public V get(K key) throws NoSuchElementException {
    int hashIndex = Math.abs(key.hashCode())%capacity; //calculates hashIndex of key
    try {
      LinkedList<Pair> currentHash = new LinkedList<Pair>(); // creates and copies LinkedList of hashIndex
      currentHash = hashMap[hashIndex];
      for(Pair x : currentHash) { // checks each Pair in LinkedList at this hashIndex
        if(x.getKey() == key) { // looks for matching key
          V value = (V) x.getValue(); // copies value of key-value pair match
          return value; // returns value of found key-value pair
        }
      }
    } catch (NullPointerException e) { // catches NullPointerException if there is no data at hashIndex that matches key
      throw new NoSuchElementException("This key is not in the Hash Table");
    }
    throw new NoSuchElementException("This key is not in the Hash Table");
  }

  /*
   * This method returns the size or number of key-value pairs in the HashTable
   * 
   * @returns int size - the number of pairs in the HashMap
   */
  @Override
  public int size() {
    return size;
  }

  /*
   * This method returns true if this key-value pair is already in the HashTable at the 
   * hashIndex correlating to the (K key)
   * 
   * @param the K key of the key-value pair that is to be checked 
   * 
   * @returns true - if Key-Value pair is already in hash table
   *          false - otherwise
   */
  @Override
  public boolean containsKey(K key) {
    int hashIndex = Math.abs(key.hashCode())%capacity; // calculates key's hashIndex
    LinkedList<Pair> index = hashMap[hashIndex]; // copies hashIndex LinkedList to new LinkedList
    if(index == null) return false; // returns false if no LinkedList at this hashIndex
    else {
      for(Pair x : index) { // checks each key-value Pair at this HashIndex
        if(x.getKey().equals(key)) return true; // return true if key is found
      }
      return false; // return false otherwise
    }
  }

  /*
   * This method removes a key-value pair from the Hash Table and returns the V value
   * that was removed.
   * 
   * @param the K key of the key-value pair to be removed
   * 
   * @returns the V value that is paired with the k input
   */
  @Override
  public V remove(K key) {
    int hashIndex = Math.abs(key.hashCode())%capacity; //calculates hashIndex of key
    if(hashMap[hashIndex] == null) { return null; } //checks if this hashIndex is null
    LinkedList<Pair> index = hashMap[hashIndex]; //copy of LinkedList at hashIndex
    for(Pair x : index) {
      if(x.getKey() == key) { // checks each Pair in LinkedList
        V value = (V) x.getValue(); //copies value
        index.remove(x); //removes Pair from LinkedList
        hashMap[hashIndex] = index; //copies new LinkedList to hashIndex with Pair removed
        size--; // size decrement
        return value; //return value removed
      }
    }
    return null; // null if Pair is not removed
  }

  /*
   * This Method clears the array, setting all hashIndexes to Null and the size to 0;
   */
  @Override
  public void clear() {
    for(int i = 0; i<hashMap.length; i++) { // sets each index in students to null
      hashMap[i] = null;
      size = 0; // sets size to 0
    }
  }

  
  
  
  
}
