// --== CS400 File Header Information ==--
// Name: Nolan Smith
// Email: nosmith@wisc.edu
// Team: MA
// Role: Front End Developer
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

/**
 * Class that handles the data input and output for the Gradebook.
 * 
 * @author Caelan Kleinhans
 */
public class DataHandler {
  
  /**
   * Initializes and loads all course files in the courses folder upon startup.
   * 
   * @param roster the HashTableMap API object representing the gradebook
   * @return a String of all errors that occurred when loading courses
   */
  public static String initCourses(HashTableMapAPI roster) {
    String errorLog = "";
    
    // Checks for the courses folder, if it doesn't exist creates it
    File folder = new File("courses");
    if (!folder.exists()) {
      folder.mkdir();
      System.out.println("Courses folder not found, creating one.");
      System.out.println("Transfer all course files to the courses folder to load them on start.");
    }
    
    // Loads each file in the courses folder and saves the log to the errorLog string
    File[] courses = folder.listFiles();
    for (File f : courses) {
      try {
        importCourse(f.getPath(), roster);
        errorLog += "Successfully loaded " + f.getName() + "\n";
      } catch (Exception e) {
        errorLog += "Error loading " + f.getName() + ":\n";
        errorLog += e.getMessage() + "\n";
      }
    }
    return errorLog;
  }

  /**
   * Method imports a .txt file representing a class roster of students and grades into the
   * gradebook.
   * 
   * @param filePath the path to the class .txt file
   * @throws FileNotFoundException if the given file path is not found
   * @throws DataFormatException if the file is incorrectly formatted
   */
  public static void importCourse(String filePath, HashTableMapAPI roster) 
      throws FileNotFoundException, DataFormatException {
    if (filePath == null) throw new FileNotFoundException("null is not a valid file path");
    
    File file = new File(filePath);
    Scanner fileRead = null;
    try {
      fileRead = new Scanner(file);
      if (!file.getName().substring(file.getName().length() - 4).equalsIgnoreCase(".txt"))
        throw new DataFormatException("Course file must be a .txt file");
      if (file.getName().split(" ").length > 1)
        throw new DataFormatException("Course file name must be 1 word (no spaces)");
      
      // Load and create Course object from file
      String courseName = file.getName().substring(0, file.getName().length()-4);
      String[] creditLine = fileRead.nextLine().split(" ");
      int credits = Integer.parseInt(creditLine[0].trim());
      if (credits < 1 || credits > 12)
        throw new DataFormatException("The number of credits must be in the range 1-12 inclusive.");
      String professorName = fileRead.nextLine();
      String description = "";
      boolean loop = true;
      while (loop) {
        String next = fileRead.nextLine();
        if (next.trim().equals("")) loop = false;
        else description += next.trim() + "\n";
      }
      if (!description.equals("")) description = description.substring(0, description.length() - 1);
      Course course = new Course(courseName, description, professorName, credits);
      
      // Load student,ID,grade values
      while(fileRead.hasNext()) {
        String[] values = fileRead.nextLine().split(",");
        if (values.length < 3) 
          throw new DataFormatException("Lines must be formatted as [name],[ID],[grade]");
        
        // Get student information and check formatting
        String studentName = values[0].trim();
        int studentId = Integer.parseInt(values[1].trim());
        if (Integer.toString(studentId).length() != 9)
          throw new DataFormatException("Student IDs must be 9 digits");
        double grade = Double.parseDouble(values[2].trim());
        if (grade > 4.0 || grade < 0.0) 
          throw new DataFormatException("Grades must be between 0.0 and 4.0 inclusive");
        
        // Check for student in table, add course or create student accordingly
        if (roster.containsKey(studentId)) {
          roster.getStudent(studentId).addCourse(course, grade);
        } else {
          Student student = new Student(studentName, studentId);
          student.addCourse(course, grade);
          roster.addStudent(student);
        }
      }
      
    } catch (NumberFormatException e) { // if credits, student id or grade not a number
      throw new DataFormatException("Unparseable number; check that course credits, student IDs" +
          " and grades are located and formatted correctly");
    } catch (NoSuchElementException e) { // if scanner reaches unexpected end of file
      throw new DataFormatException("Reached end of file unexpectedly; check formatting");
    } catch (Exception e) {
      if (e instanceof FileNotFoundException || e instanceof DataFormatException) throw e;
      throw new DataFormatException(
          "Unexpected exception occured; check file format. Detailed message: " + e.getMessage());
    } finally {
      if (fileRead != null) fileRead.close();
    }
  }
  
  /**
   * Updates the given student's grade within the course file corresponding to the course object.
   * 
   * @param course the course for which to update the student's grade
   * @param student the student for which to update their grade
   * @param newGrade the new grade to give the student
   * @return boolean if the grade update was successful or not
   */
  protected static boolean exportGrade(Course course, Student student, double newGrade) {
    Scanner fileScan = null;
    FileWriter output = null;
    
    // If any exception thrown, student grade not updated successfully
    try {
      File folder = new File("courses");
      File[] courseFiles = folder.listFiles();
      File file = null;
      
      // Find correct course file
      for (int i = 0; i < courseFiles.length; i++) {
        if (courseFiles[i].getName().equals(course.getCourseName() + ".txt")) {
          file = courseFiles[i];
          break;
        }
      }
      fileScan =  new Scanner(file);
      String fileContents = "";
      
      // Construct a new string of all file contents to write out to file
      while (fileScan.hasNext()) {
        String line = fileScan.nextLine();
        String[] values = line.split(",");
        if (values.length != 3) {
          fileContents += line + "\n";
          continue;
        }
        
        // If ID matches, update old grade to new grade
        if (values[1].trim().equals(Integer.toString(student.getID()))) {
          values[2] = String.format("%.2f", newGrade);
        }
        fileContents += "" + values[0].trim() + "," + values[1].trim() + "," + values[2]
            + "\n";
      }
      
      // Remove final newline character and write updated file contents
      fileContents = fileContents.substring(0, fileContents.length() - 1);
      output = new FileWriter(file);
      output.write(fileContents);
    } catch (Exception e) { // If any exception, error writing file, return false
      return false;
    } finally {
      
      // Close input/output streams after finished
      try {
        if (fileScan != null) fileScan.close();
        if (output != null) output.close();
      } catch (Exception e) {
        return false;
      }
    }
    return true; // Updated successfully, return true
  }
}