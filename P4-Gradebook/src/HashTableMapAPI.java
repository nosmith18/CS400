// --== CS400 File Header Information ==--
// Name: Nolan Smith
// Email: nosmith@wisc.edu
// Team: MA
// Role: Front End Developer
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.util.NoSuchElementException;

/**
 * This class implements all interfacing to HashTableMap class backend for the 
 * University Gradebook CS400 Fall 2020 - Project One Hash Table programming assignment.
 */
public class HashTableMapAPI {
	private HashTableMap<Integer, Student> hashTable;	// hashtable to store Students
	
  /**
   * Constructor method that initializes capacity at capacity parameter
   * 
   * @param capacity - capacity to initialize hashTable at
   */
	public HashTableMapAPI(int capacity) {
		hashTable = new HashTableMap<Integer, Student>(capacity);
	}	
	
  /**
   * Constructor method that initializes capacity at 10
   */
	public HashTableMapAPI() {
		hashTable = new HashTableMap<Integer, Student>();
	}
	
  /**
   * Inserts new Student into hashTable.
   * 
   * @param  id	   - id for which to address Student
   * @param  name  - name of student
   * @return true if Student successfully added, false otherwise
   */
	public boolean addStudent(Student student) {
		return this.hashTable.put(student.getID(), student);
	}
	
  /**
   * Checks if hashTable contains given key
   * 
   * @param  id - student id to check if exists
   * @return true if key within hashTable, false otherwise
   */
	public boolean containsKey(Integer id) {
		return this.hashTable.containsKey(id);
	}
	
  /**
   * Gets the value at the location of the given id within the hashTable.
   * 
   * @param  id - id of student to retrieve
   * @return the Student stored with given id in hash table.
   * 
   * @throws NoSuchElementException if Student not in hashTable.
   */
	public Student getStudent(Integer id) throws NoSuchElementException {
		try {
			return this.hashTable.get(id);
		} catch (NoSuchElementException e) {
			throw new NoSuchElementException("Student not found in hashTable");
		}
	}
	
  /**
   * Adds course to the given student.
   *    
   * @param id 		- student id of student to modify course of
   * @param course	- course to be modified
   * @param grade	- student's new grade for new course
   * 
   * @return true when grade successfully changed, false otherwise
   * @throws NoSuchElementException if the student id not in hash table
   */
  public boolean addCourse(Integer id, Course course, double grade) throws NoSuchElementException {
		  try {
			  return this.getStudent(id).addCourse(course, grade);	
		  } catch (NoSuchElementException e) {
			  throw e;
		  }
	  }
	
  /**
   * Changes course grade for the given student.
   *    
   * @param id 		- student id of student to add course to
   * @param course	- new course to be added to student
   * @param grade	- student's grade for new course
   * 
   * @return true when course successfully added, false otherwise
   * @throws NoSuchElementException if the student id not in hash table
   */
  public boolean editGrade(Integer id, Course course, double grade) throws NoSuchElementException {
		  try {
			  return this.getStudent(id).editGrade(course.getCourseName(), grade);
		  } catch (NoSuchElementException e) {
			  throw e;
		  }
	  }
  
  /**
   * Removes the value at the given key from the hashTable.
   * 
   * @param  id - what student to remove
   * @return element removed, null if Student not in table
   */
	public Student remove(Integer id) {
		return this.hashTable.remove(id);
	}
	  
  /**
   * Returns total number of elements in hashTable
   * 
   * @return total number of elements in hashTable
   */
	public int size() {
		return this.hashTable.size();
	}
	
  /**
   * Clears all elements from hashTable.
   */
	public void clear() {
		this.hashTable.clear();
	}
}