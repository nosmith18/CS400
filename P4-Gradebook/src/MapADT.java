// --== CS400 File Header Information ==--
// Name: Nolan Smith
// Email: nosmith@wisc.edu
// Team: MA
// Role: Front End Developer
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.util.NoSuchElementException;

public interface MapADT<KeyType,ValueType> {    
  
  public boolean put(KeyType key, ValueType value);    
  public ValueType get(KeyType key) throws NoSuchElementException;    
  public int size();    
  public boolean containsKey(KeyType key);    
  public ValueType remove(KeyType key);    
  public void clear();
  
}