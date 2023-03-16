//////////////////////////////////////////
// --== CS400 File Header Information ==--
// Name: Nolan Oliver Smith
// Email: nosmith@wisc.edu
// Team: MA
// TA: Harit
// Lecturer: Gary Dahl
// Notes to Grader: None
//////////////////////////////////////////
public class Pair<K,V> {
  
  private V value;
  private K key;
  
  public Pair(K key, V value) {
    this.key = key;
    this.value = value;
  }
  
  /*
   * This method returns the key of this key-value pair
   * 
   * @returns K key of this pair
   */
  public K getKey() {
    return key;
  }
  
  /*
   * This method returns the value of this key-value pair
   * 
   * @returns V value of this pair
   */
  public V getValue() {
    return value;
  }
  
  
}
