// --== CS400 File Header Information ==--
// Name: Nolan Oliver Smith
// Email: nosmith@wisc.edu
// Team: MA
// Role: Front End Developer 
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: <optional extra notes>

import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.zip.DataFormatException;

/**
 * 
 * This class tests all the methods and classes involved in the gradebook project
 * 
 * @author binay
 *
 */
public class testGradebook {

  ////////////////////////////////// Course Class Tests //////////////////////////////////////////
  
  /**
   * 
   * This method tests the functionality of the Course class (whether it is instantiated correctly
   * and if all methods return the appropriate values)
   * 
   * @return true if all test cases pass, false otherwise
   */
  private static boolean testCourse() {

    // All of these tests are accessor tests

    Course phy = new Course("physics", "it's a physics class", "Gary", 4);
    if (!phy.getCourseName().equals("physics")) {
      System.out.println("Wrong course name returned");
      return false;
    }
    if (!phy.getDescription().equals("it's a physics class")) {
      System.out.println("Wrong description returned");
      return false;
    }
    if (!phy.getProfessor().equals("Gary")) {
      System.out.println("Wrong Professor name returned");
      return false;
    }
    if (phy.getCredits() != 4) {
      System.out.println("Number of credits returned is wrong");
      return false;
    }

    Course bio = new Course("biology", "it's a biology class", "Dan", 3);
    if (!bio.getCourseName().equals("biology")) {
      System.out.println("Wrong course name returned");
      return false;
    }
    if (!bio.getDescription().equals("it's a biology class")) {
      System.out.println("Wrong description returned");
      return false;
    }
    if (!bio.getProfessor().equals("Dan")) {
      System.out.println("Wrong Professor name returned");
      return false;
    }
    if (bio.getCredits() != 3) {
      System.out.println("Number of credits returned is wrong");
      return false;
    }

    return true;

  }

  ////////////////////////////////// Student Class Tests //////////////////////////////////////////

  /**
   * 
   * This method tests the functionality of the Student class (whether it is instantiated correctly
   * and if all methods return the appropriate values)
   * 
   * @return true if all test cases pass, false otherwise
   */
  private static boolean testStudent() {

    // Class accessors test

    Student one = new Student("a", 756436721);
    if (!one.getName().equals("a")) {
      System.out.println("Wrong name returned");
      return false;
    }

    if (one.getID() != 756436721) {
      System.out.println("Wrong ID returned");
      return false;
    }

    Student two = new Student("b", 756436641);

    if (!two.getName().equals("b")) {
      System.out.println("Wrong name returned");
      return false;
    }

    if (two.getID() != 756436641) {
      System.out.println("Wrong ID returned");
      return false;
    }

    return true;

  }

  /**
   * 
   * This method tests the add course method of the Student class
   * 
   * @return true if all test cases pass, false otherwise
   */
  private static boolean addCourseTest() {

    Student one = new Student("a", 756436721);

    one.addCourse(new Course("english", "it's an english class", "Also Gary", 4), 3.5);

    // we check only the 0th index because we just added one course
    if (one.getCourses().length != 1 && !one.getCourses()[0].getCourseName().equals("english")) {
      System.out.println("First course not added properly");
      return false;
    }

    one.addCourse(new Course("physics", "it's a physics class", "Adam", 4), 4.0);

    if (one.getCourses().length != one.getGrades().length) {
      System.out.println("Number of grades don't match number of courses");
      return false;
    }

    if (one.addCourse(new Course("physics", "it's a physics class", "Adam", 4), 4.0)) {
      System.out.println("Duplicate course added");
      return false;
    }


    one.addCourse(new Course("history", "it's a history class", "Josh", 5), 3.5);
    one.addCourse(new Course("chemistry", "it's a chemistry class", "Jay", 5), 3.5);
    if (one.getCourses().length != 4) {
      System.out.println("Some of the courses were not added properly");
      return false;
    }
    boolean foundCourse = false;
    Course[] courseList = one.getCourses();
    for (int x = 0; x < courseList.length; x++) {
      if (courseList[x].getCourseName().equals("history")) {
        foundCourse = true;
        break;
      }
    }
    if (!foundCourse) {
      System.out.println("Added course not found");
      return false;
    }

    return true;
  }



  /**
   * 
   * This method tests the editGrade method of the Student class
   * 
   * @return true if all test cases pass, false otherwise
   */
  private static boolean editGradeTest() {

    Student one = new Student("a", 756436721);

    // Edit course test

    one.addCourse(new Course("english", "it's an english class", "Yoda", 4), 3.5);
    one.addCourse(new Course("history", "it's a history class", "Josh", 5), 3.5);

    one.editGrade("english", 4.0);
    Course[] courseList = one.getCourses();
    Double[] grades = one.getGrades();
    for (int x = 0; x < courseList.length; x++) {
      if (courseList[x].getCourseName().equals("english")) {
        if (grades[x] != 4.0) {
          System.out.println("Grade not edited");
          return false;
        }
        break;
      }
    }

    if (one.editGrade("physics", 3.0)) {
      System.out.println("Edited grade for a non-existent course");
      return false;
    }

    return true;

  }

  /**
   * 
   * This method tests the getGpa method of the Student class
   * 
   * @return true if all test cases pass, false otherwise
   */
  private static boolean getGPATest() {

    Student one = new Student("a", 756436721);
    one.addCourse(new Course("english", "it's an english class", "Yoda", 4), 3.5);
    if (Math.abs(one.getGPA() - 3.5) >= 0.01) {
      System.out.println(
          "Wrong GPA calculated with a single course: got " + one.getGPA() + " expected " + 3.5);
      return false;
    }
    one.addCourse(new Course("history", "it's a history class", "Josh", 5), 3.0);
    if (Math.abs(one.getGPA() - 3.22) >= 0.01) {
      System.out.println(
          "Wrong GPA calculated with two courses: got " + one.getGPA() + " expected " + 3.22);
      return false;
    }
    one.addCourse(new Course("chemistry", "it's a chemistry class", "Jay", 5), 4.0);
    if (Math.abs(one.getGPA() - 3.50) >= 0.01) {
      System.out.println(
          "Wrong GPA calculated with three courses: got " + one.getGPA() + " expected " + 3.50);
      return false;
    }
    return true;
  }

  ////////////////////////////////// DataHandler Class Tests ////////////////////////////////// 
  
  /**
   * This method tests whether the text data read from the file is imported properly into the
   * program
   *
   * @return true if all test cases pass, false otherwise
   */
  private static boolean importCourseTest() {
    // Importing from a file that does not exist
    try {
      DataHandler.importCourse("./courses/Nonexistent.txt", Driver.roster);
      System.out.println("Program reading from some non-existent file.");
      return false;
    } catch (FileNotFoundException fnfe) {

    } catch (Exception exc) {
      System.out.println("Expected FileNotFoundException but got something else");
      System.out.println(exc);
      return false;
    }
    // Importing from a file that does exist and is properly formatted
    try {
      DataHandler.importCourse("./courses/english.txt", Driver.roster);
    } catch (FileNotFoundException exc) {
      System.out.println("Existent file not found");
      return false;
    } catch (Exception exc) {
      System.out.println("Something went wrong while importing data.");
      System.out.println(exc);
      return false;
    }
    // Checking the imported data to see if everything is correct (To be written)

    try {
      Driver.roster.getStudent(977732145);
    } catch (NoSuchElementException exc) {
      System.out.println("One of the added students wasn't found: 977732145");
      return false;

    }

    try {
      Driver.roster.getStudent(276432641);
    } catch (NoSuchElementException exc) {
      System.out.println("One of the added students wasn't found: 276432641");
      return false;

    }

    try {
      Driver.roster.getStudent(372380640);
    } catch (NoSuchElementException exc) {
      System.out.println("One of the added students wasn't found: 372380640");
      return false;

    }

    // Importing from a poorly formatted file
    try {
      DataHandler.importCourse("./courses/physics.txt", Driver.roster);
      System.out.println("Expected a data format exception but got nothing");
      return false;
    } catch (DataFormatException dfe) {
    } catch (Exception exc) {
      System.out.println("Expected data format exception but got something else");
      System.out.println(exc);
      return false;
    }

    return true;
  }

  /**
   * 
   * This method tests whether the exported data is properly written into the program
   * 
   * @return true if all test cases pass, false otherwise
   */
  private static boolean exportGradeTest() {
    DataHandler.initCourses(Driver.roster);
    String[] newGrades = new String[] {"1.5", "2.0", "2.5", "3.5", "4.0"};
    int index = new Random().nextInt(5);
    String grade = newGrades[index];
    Driver.edit(new String[] {"edit", "876433541", "english", grade});
    DataHandler.initCourses(Driver.roster);
    Course[] test = Driver.roster.getStudent(876433541).getCourses();
    Double[] grades = Driver.roster.getStudent(876433541).getGrades();

    boolean changed = false;
    for (int x = 0; x < test.length; x++) {
      if (test[x].getCourseName().equals("english")) {
        if (grades[x] == Double.parseDouble(grade)) {
          changed = true;
        }
      }
    }
    if (!changed) {
      System.out.println("Changed grade not exported to file");
      return false;
    }
    return true;
  }

  ////////////////////////////// HashTableMap Class Tests ///////////////////////////////////

  /**
   * This method tests the put method of the hashmap
   * 
   * @return true if all test cases pass, false otherwise
   */
  public static boolean hashmapPutTest() {
    HashTableMap<String, Integer> map = new HashTableMap<>();
    map.put("Hello", 1);
    map.put("world", 2);
    try {
      if (map.get("Hello") != 1) {
        System.out.println("Wrong value returned");
        return false;
      }
      if (map.get("world") != 2) {
        System.out.println("Wrong value returned");
      }
    } catch (NoSuchElementException exc) {
      System.out.println("An element added to the hashmap wan't found.");
      return false;
    }
    if (map.put("Hello", 3) != false) {
      System.out.println("Hashmap accepting duplicate keys");
      return false;
    }
    return true;
  }

  /**
   * This method tests the get and containsKey method of the hashmap
   * 
   * @return true if all test cases pass, false otherwise
   */
  public static boolean hashmapGetandContainsKeyTest() {
    HashTableMap<Integer, String> map = new HashTableMap<>();
    map.put(1, "a");
    if (!map.containsKey(1)) {
      System.out.println("Existent key not found");
      return false;
    } else {
      if (!map.get(1).equals("a")) {
        System.out.println("wrong value returned");
        return false;
      }
    }
    map.remove(1);
    try {
      map.get(1);
      System.out.println("Expected exception, got nothing");
      return false;
    } catch (NoSuchElementException exc) {

    }
    if (map.containsKey(3)) {
      System.out.println("Non-existent element found");
      return false;
    }
    return true;
  }

  /**
   * This method tests whether the array consisting of the key-value pairs expands at the correct
   * load factor or not.
   * 
   * @return true if all test cases pass, false otherwise
   */
  public static boolean hashmapExpansionTest() {
    HashTableMap<String, Integer> map = new HashTableMap<>(2);
    try {
      map.put("a", 1);
      map.put("b", 2);
      if (map.getCapacity() != 4) {
        System.out.println("expected capacity 4 " + "got " + map.getCapacity());
        return false;
      }
      try {
        map.get("a");
      } catch (NoSuchElementException nse) {
        System.out.println("Elements missing after expansion");
        return false;
      }
      map.put("c", 3);
      map.put("d", 4);
      if (map.getCapacity() != 8) {
        System.out.println("expected capacity 8 " + "got " + map.getCapacity());
        return false;
      }
      try {
        map.get("d");
      } catch (NoSuchElementException nse) {
        System.out.println("Elements missing after expansion");
        return false;
      }
      map.put("e", 5);
      map.put("f", 6);
      map.put("g", 7);
      if (map.getCapacity() != 16) {
        System.out.println(map.size() + ", " + map.getCapacity());
        System.out.println("expected 16 " + "got " + map.getCapacity());
        return false;
      }
      try {
        map.get("f");
      } catch (NoSuchElementException nse) {
        System.out.println("Elements missing after expansion");
        return false;
      }
    } catch (ArrayIndexOutOfBoundsException exc) {
      System.out.println("Array did not expand");
      return false;
    }

    return true;
  }

  /**
   * This method tests the remove and clear methods of the hashmap
   * 
   * @return true if all test cases pass, false otherwise
   */
  public static boolean hashmapRemoveTest() {
    HashTableMap<String, Integer> map = new HashTableMap<>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);
    map.put("d", 4);
    map.put("e", 5);

    if (map.remove("f") != null) {
      System.out.println("Hashmap removed a non-existent element");
      return false;
    }

    map.remove("a");
    map.remove("c");
    if (map.size() != 3) {
      System.out.println("Hashmap returned wrong size after removing elements");
      return false;
    }

    if (map.containsKey("a") || map.containsKey("c")) {
      System.out.println("removed key found");
      return false;
    }

    map.clear();
    if (map.size() != 0) {
      return false;
    }

    return true;
  }

  /**
   * This method tests the size method through a series of insertions and removals of the hashmap
   * 
   * @return true if all test cases pass, false otherwise
   */
  public static boolean hashmapSizetest() {
    HashTableMap<String, Integer> map = new HashTableMap<>();
    map.put("a", 1);
    map.put("b", 2);
    if (map.size() != 2) {
      return false;
    }
    map.remove("a");
    if (map.size() != 1) {
      return false;
    }
    map.put("c", 3);
    map.put("d", 4);
    map.put("e", 5);
    map.remove("d");
    if (map.size() != 3) {
      return false;
    }
    return true;
  }

  ////////////////////////////// HashTableMapAPI Class Tests /////////////////////////////////


  /**
   * 
   * This method tests the addStudent method in the HashtTableMapAPI class
   * 
   * @return true if all test cases pass, false otherwise
   */
  private static boolean addStudentTest() {
    HashTableMapAPI api = new HashTableMapAPI();
    api.addStudent(new Student("Binay", 345265981));
    api.addStudent(new Student("Naruto", 745235286));
    try {
      if (!api.getStudent(745235286).getName().equals("Naruto")) {
        System.out.println("Wrong value returned");
        return false;
      }
      if (!api.getStudent(345265981).getName().equals("Binay")) {
        System.out.println("Wrong value returned");
        return false;
      }
    } catch (NoSuchElementException exc) {
      System.out.println("An added student wasn't found.");
      return false;
    }
    if (api.addStudent(new Student("Naruto", 745235286))) {
      System.out.println("API accepting duplicate students");
      return false;
    }
    return true;
  }

  /**
   * 
   * This method tests the getStudent method in the HashTableMapAPI class
   * 
   * @return true if all test cases pass, false otherwise
   */
  private static boolean getStudentTest() {
    HashTableMapAPI api = new HashTableMapAPI();
    api.addStudent(new Student("Binay", 345265981));
    api.addStudent(new Student("Naruto", 745235286));
    if (!api.containsKey(345265981)) {
      System.out.println("Existent key not found");
      return false;
    } else {
      if (!api.getStudent(345265981).getName().equals("Binay")) {
        System.out.println("wrong value returned");
        return false;
      }
    }
    api.remove(345265981);
    try {
      api.getStudent(345265981);
      System.out.println("Expected NoSuchElementException, got nothing");
      return false;
    } catch (NoSuchElementException exc) {

    }
    if (api.containsKey(654888932)) {
      System.out.println("Non-existent element found");
      return false;
    }
    return true;
  }

  /**
   * 
   * This method tests the addCourse method in the HashTableMapAPI class
   * 
   * @return true if all test cases pass, false otherwise
   */
  private static boolean addCourseAPITest() {

    HashTableMapAPI api = new HashTableMapAPI();
    api.addStudent(new Student("Binay", 345265981));
    if (!api.addCourse(345265981, new Course("english", "it's an english class", "Joe", 4), 3.5)) {
      System.out.println("New course english couldn't be added");
      return false;
    }
    if (!api.addCourse(345265981, new Course("biology", "it's a biology class", "Dan", 3), 4.0)) {
      System.out.println("New course biology couldn't be added");
      return false;
    }
    Course[] courses = api.getStudent(345265981).getCourses();
    boolean found = false;
    for (int x = 0; x < courses.length; x++) {
      if (courses[x].getCourseName().equals("biology")) {
        found = true;
      }
    }
    if (!found) {
      System.out.println("Added course not found");
      return false;
    }

    if (api.addCourse(345265981, new Course("english", "it's an english class", "Joe", 4), 3.5)) {
      System.out.println("Duplicate Course added");
      return false;
    }

    try {
      api.addCourse(876342224, new Course("biology", "it's a biology class", "Dan", 3), 4.0);
      System.out.println("Course added to a non-existent student");
      return false;
    } catch (NoSuchElementException exc) {

    } catch (Exception exc) {
      System.out.println("Expected NoSuchElementException but got something else");
      return false;
    }

    return true;

  }



  ////////////////////////////// Driver Class Tests //////////////////////////////////////

  /**
   *
   * This method tests the gpa method of the driver class
   *
   * @return true if all test cases pass, false otherwise
   */
  public static boolean driverGPATest() {
    DataHandler.initCourses(Driver.roster);

    if (!Driver.gpa(new String[] {"gpa", "Yoda", "Boo"})
        .equals("Invalid use of 'gpa' command." + "\nFormat is 'gpa <studentID>'")) {
      System.out.println("gpa method accepting invalid formats: Test1");
      return false;
    }

    if (!Driver.gpa(new String[] {"gpa", "Bro"})
        .equals("Invalid use of 'gpa' command." + "\nFormat is 'gpa <studentID>'")) {
      System.out.println("gpa method accepting invalid formats: Test2");
      return false;
    }

    if (Driver.gpa(new String[] {"gpa", "372380640"}).equals("No student found with inputted ID.")
        || Driver.list(new String[] {"gpa", "372380640"})
            .equals("Invalid use of 'gpa' command." + "\nFormat is 'gpa <studentID>'")) {
      System.out.println("gpa could not find existent ID");
      return false;
    }

    if (!Driver.gpa(new String[] {"gpa", "811111123"})
        .equals("No student found with inputted ID.")) {
      System.out
          .println("gpa either found a non-existent ID or could not recognize a valid format");
      return false;
    }

    return true;

  }

  /**
   * This method tests the list method of the driver class
   *
   * @return true if all test cases pass, false otherwise
   */
  public static boolean driverListTest() {
    DataHandler.initCourses(Driver.roster);

    if (!Driver.list(new String[] {"list", "Naruto", "HAHA"})
        .equals("Invalid use of 'list' command." + "\nFormat is 'list <studentID>'")) {
      System.out.println("List method accepting invalid formats: Test1");
      return false;
    }

    if (!Driver.list(new String[] {"list", "Bro"})
        .equals("Invalid use of 'list' command." + "\nFormat is 'list <studentID>'")) {
      System.out.println("List method accepting invalid formats: Test2");
      return false;
    }

    if (Driver.list(new String[] {"list", "372380640"}).equals("No student found with inputted ID.")
        || Driver.list(new String[] {"list", "372380640"})
            .equals("Invalid use of 'list' command." + "\nFormat is 'list <studentID>'")) {
      System.out.println("List could not find existent ID");
      return false;
    }

    if (!Driver.list(new String[] {"list", "811111123"})
        .equals("No student found with inputted ID.")) {
      System.out
          .println("List either found a non-existent ID or could not recognize a valid format");
      return false;
    }

    return true;
  }


  /**
   *
   * This method tests the edit method of the driver class
   *
   * @return true if all test cases pass, false otherwise
   */
  public static boolean driverEditTest() {
    DataHandler.initCourses(Driver.roster);

    if (!Driver.edit(new String[] {"edit", "Naruto", "HAHA"}).equals(
        "Invalid use of 'edit' command.\nFormat is 'edit <studentID> <courseName> <grade>'")) {
      System.out.println("Edit method accepting invalid formats: Test1");
      return false;
    }

    if (!Driver.edit(new String[] {"edit", "english", "372380640", "3.5"}).equals(
        "Invalid use of 'edit' command.\nFormat is 'edit <studentID> <courseName> <grade>'")) {
      System.out.println("Edit method accepting invalid formats: Test2");
      return false;
    }

    if (Driver.edit(new String[] {"edit", "372380640", "english", "2.5"})
        .equals("No student found with inputted ID.")
        || Driver.edit(new String[] {"edit", "372380640", "english", "2.5"}).equals(
            "Invalid use of 'edit' command.\nFormat is 'edit <studentID> <courseName> <grade>'")) {
      System.out.println("Edit could not find existent ID");
      return false;
    }

    if (!Driver.edit(new String[] {"edit", "372380640", "english", "4.5"})
        .equals("Grade must be on a 4-point scale, e.g. 4.0, 2.5.")) {
      System.out.println("Edit accepting grades that are not within the expected 4.0 scale");
      return false;
    }

    if (!Driver.edit(new String[] {"edit", "811111123", "english", "2.5"})
        .equals("No student found with inputted ID.")) {
      System.out.println("Edit editting grades to non-existing IDs");
      return false;
    }

    return true;
  }

  /**
   * 
   * This method tests the description method of the Driver class
   * 
   * @return true if all tests pass, false otherwise
   */
  private static boolean driverDescriptionTest() {
    DataHandler.initCourses(Driver.roster);
    String returnedDesc =
        Driver.description(new String[] {"description", "366932681", "chemistry"});
    if (returnedDesc.equals("No matching course was found for the given student.")
        || returnedDesc.equals("No student found with inputted ID.")
        || returnedDesc.equals("Invalid use of 'description' command."
            + "\nFormat is 'description <studentID> <courseName>'")) {
      System.out.println("Wrong description returned");
      return false;
    }

    if (!Driver.description(new String[] {"description", "chemistry", "366932681"})
        .equals("Invalid use of 'description' command."
            + "\nFormat is 'description <studentID> <courseName>'")) {
      System.out.println("Description method accepted wrong format");
      return false;
    }

    if (!Driver.description(new String[] {"description", "366932681", "physics"})
        .equals("No matching course was found for the given student.")) {
      System.out.println("Nonexistent course description for given student returned");
      return false;
    }

    if (!Driver.description(new String[] {"description", "786769444", "physics"})
        .equals("No student found with inputted ID.")) {
      System.out.println("Nonexistent student accepted as input");
      return false;
    }

    return true;
  }


  public static void main(String[] args) {
    
    System.out.println("Course class test: " + testCourse());
    System.out.println("Student Class test: " + testStudent());
    System.out.println("Add Course test: " + addCourseTest());
    System.out.println("Edit Grade test: " + editGradeTest());
    System.out.println("GPA test: " + getGPATest());
    System.out.println("Hashmap Put test: " + hashmapPutTest());
    System.out.println("Hashmap Get and containsKey test: " + hashmapGetandContainsKeyTest());
    System.out.println("Hashmap Expansion test: " + hashmapExpansionTest());
    System.out.println("Hashmap Remove test: " + hashmapRemoveTest());
    System.out.println("Hashmap Size test: " + hashmapSizetest());
    System.out.println("Add Student test: " + addStudentTest());
    System.out.println("Get Student test: " + getStudentTest());
    System.out.println("Add Course (API) test: " + addCourseAPITest());
    System.out.println("Import Course test: " + importCourseTest());
    System.out.println("Driver List Test: " + driverListTest());
    System.out.println("Driver Edit Test: " + driverEditTest());
    System.out.println("Driver GPA Test: " + driverGPATest());
    System.out.println("Export Grade Test: " + exportGradeTest());
    System.out.println("Driver description Test: " + driverDescriptionTest());
  }

}