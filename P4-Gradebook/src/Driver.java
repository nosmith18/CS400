// --== CS400 File Header Information ==--
// Name: Nolan Smith
// Email: nosmith@wisc.edu
// Team: MA
// Role: Front End Developer
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.util.Scanner;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.NoSuchElementException;
import java.util.zip.DataFormatException;

/*
 * This Driver class contains the Main method for user interaction with the gradebook
 */
public class Driver {

  protected static HashTableMapAPI roster = new HashTableMapAPI();
  
  /*
   * The Main method that will run the user interface
   */
  public static void main(String[] args) {
    String initErrors = DataHandler.initCourses(roster);
    Scanner input = new Scanner(System.in);
    
    System.out.println("Welcome to the gradebook. " + "Please enter your student ID to view your grades."
                    + "\nIf you are an instructor, enter ADMIN."); 
    String loginID = input.nextLine(); //takes in user input for ID/ADMIN
    while(!loginID.equalsIgnoreCase("q")) { //while loops allows for reprompting if ID is incorrect
      if(loginID.equalsIgnoreCase("admin")) { //Begin ADMIN gradebook setup
        loginID = "q"; //ensures Main does not continue printing endlessly
        System.out.println("File loading logs: \n" + initErrors); //prints errors from input text file
        System.out.println("Hello Instructor!\nEnter one of following commands:\n"
            + "'help' -- to print this list of commands\n"
            + "'list <studentID>' -- List courses for this student\n"
            + "'gpa <studentID>' -- Lists GPA of this student\n"
            + "'description <studentID> <courseName>' -- Gives description for this student's course\n"
            + "'edit <studentID> <courseName> <grade>' -- Change a student's grade for specified course\n"
            + "'import <textFilePath>' -- Imports students and courses from another text file\n"
            + "'importhelp' -- Gives expected format for text file for 'import' command\n"
            + "'quit' -- Closes the gradebook!");
        String command = input.nextLine();
        while(!command.equalsIgnoreCase("quit")) { //if not quit, keep allowing commands
          String[] line = command.split(" "); //splits command into String parts for use in actions
          switch (line[0].toLowerCase()) { //switch for command input
            case "help": //command is "help"
              System.out.println("Commands:\n"
                  + "'help' -- to print this list of commands\n"
                  + "'list <studentID>' -- List courses for this student\n"
                  + "'gpa <studentID>' -- Lists GPA of this student\n"
                  + "'description <studentID> <courseName>' -- Gives description for this student's course\n"
                  + "'edit <studentID> <courseName> <grade>' -- Change a student's grade for specified course\n"
                  + "'import <textFilePath>' -- Imports students and courses from another text file\n" 
                  + "'importhelp' -- Gives expected format for text file for 'import' command\n"
                  + "'quit' -- Closes the gradebook!"); //prints out all commands
              break;
            case "description":
              System.out.println(description(line)); // sends studentID and courseName to description()
              break;
            case "gpa": //command is "gpa"
              System.out.println(gpa(line)); //send studentID to list()
              break;
            case "edit": //command is "edit"
              System.out.println(edit(line)); // sends studentID, courseName, grade to edit()
              break;
            case "list": //command is "list"
              System.out.println(list(line)); //send studentID to list()
              break;
            case "import": //command is "import"
              try {
                DataHandler.importCourse(line[1], roster); //send textFilePath to DataHandler class' importCourse()
              } catch (DataFormatException d) { //If text file is incorrectly formatted, catches exception and prints message 
                System.out.println(d.getMessage());
              } catch (FileNotFoundException f) { //If text file was not found, catches exception and prints message
                System.out.println(f.getMessage());
              } catch (Exception n) {
                System.out.println("Invalid use of 'import' command.\nFormat is 'import <textFilePath>'");
              }
              break;
            case "importhelp": 
              System.out.println(importHelp()); // calls importHelp() which prints out expected format of textFile to be imported
              break;
            default:
              System.out.println("Invalid command. Please view the command list.");
              break;
          } 
          System.out.println("Enter next command:"); // prompts next command
          command = input.nextLine();
        }
        if(command.equalsIgnoreCase("quit")) { // checks if command is "quit"
          System.out.println("Gradebook has been closed!");
          input.close(); // closes scanner
        }
      }
      else { //Not admin, try student input
        try {
          int studentID = Integer.parseInt(loginID); // convert studentID from String to int
          Student newStudent = roster.getStudent(studentID); //copies Student from roster HashMap using StudentID as key
          System.out.println(getSummary(newStudent));
          loginID = "q"; //
        } catch (Exception e) {
          System.out.println("No student found with inputted ID. Please try again.");
          loginID = input.nextLine();
        }
      }
    }
    if (input != null) input.close();
  }

  /*
   * Protected helper method that prints the description of a student's course
   * 
   * @param String objects containing studentID, and courseName in that order
   * @return String containing a course description
   */
  protected static String description(String[] command) {
    if(command.length != 3) { //checks for correct format
      return "Invalid use of 'description' command.\nFormat is 'description <studentID> <courseName>'";
    }
    try {
      Student s = roster.getStudent(Integer.parseInt(command[1])); // copies Student from roster HashMap using StudentID as key
      Course[] courses = s.getCourses(); // gets list of courses from student object
      for(Course c : courses) { 
        if(c.getCourseName().equalsIgnoreCase(command[2])) { // iterates through courses to find which matches courseName parameter
          return "Course Name: " + c.getCourseName() + "\nProfessor: "
              + c.getProfessor() + "\nCredits: " + c.getCredits() + 
              "\nDescription: " + c.getDescription(); // returns description of course with name, and professor
        }
      }
      return "No matching course was found for the given student."; // if courseName does not exist in the this student's list of courses
      } catch (NoSuchElementException n) { // if student is not a part of roster, catches NoSuchElementException
        return "No student found with inputted ID.";
      } catch (NumberFormatException n) {
        return "Invalid use of 'description' command.\nFormat is 'description <studentID> <courseName>'";
      }
  }

  /*
   * Protected helper method that prints expected format of text file to be imported
   * 
   * @return String of expected text file format
   */
  protected static String importHelp() { // returns the expected format of textFiles that are being imported into the Gradebook
    return "Data File Format:\r\n" + "\r\n" + "Name: [Course Name].txt\r\n" + "Contents:\r\n"
        + "[credits] credits\r\n" + "[professor name]\r\n" + "[description]\r\n"
        + "[description contd.] (the description can be as many lines as desired)\r\n"
        + "                      <-- (empty line signals end of description, start of students)\r\n"
        + "[student name 1],[student id 1],[gpa 1]\r\n"
        + "[student name 2],[student id 2],[gpa 2]\r\n"
        + "[student name 3],[student id 3],[gpa 3]\r\n" + "...\r\n"
        + "[student name n],[student id n],[gpa n]\n";
  }

  /*
   * Protected helper method that outputs a summary of a Student's current courses, grades, and overall gpa
   * 
   * @param Student object whose summary will be printed
   * @return String containing the Students summary(course, professor, credits, grade)
   */
  protected static String getSummary(Student stud) {
    String summary = "Hello " + stud.getName() + ".\nYour grade summary: "
        + "\nGPA: " + String.format("%.2f", stud.getGPA()) + "\n"; // creates String to be returned
    Course[] courses = stud.getCourses(); // makes a copy of this student's courses
    Double[] grades = stud.getGrades(); // makes a cop of this student's grades
    int courseIndex = 0; // counter for course to be used in grades.get(courseIndex)
    for(Course c : courses) { // iterates through courses
      summary += "Course: " + c.getCourseName() + "\nProfessor: " + c.getProfessor() +
          "\nCredits: " + c.getCredits() + "\nCurrent Grade: " + grades[courseIndex] + "\n"; 
          // adds course, professor, credit, and grades for this student
      courseIndex++; // increments courseIndex to mirror iteration of student's courses with student's grades
    }
    return summary; // returns String summary for this student
  }

  /*
   * Protected helper method that edits a student's grade in specified course
   * 
   * @param String objects containing StudentID, courseName, grade in that order
   * @return String saying whether or not the grade was successfully changed
   */
  protected static String edit(String[] command) {
    if(command.length != 4) { //checks for correct format
      return "Invalid use of 'edit' command.\nFormat is 'edit <studentID> <courseName> <grade>'";
    }
    try {
    Student stud = roster.getStudent(Integer.parseInt(command[1])); // copies Student from roster HashMap using StudentID as key
    Double newGrade = Double.parseDouble(command[3]); // makes copy of grade from String input parameter 
    if(newGrade > 4.0 || newGrade < 0.0) { // checks that grade is within acceptable range
      return "Grade must be on a 4-point scale, e.g. 4.0, 2.5.";
    }
    if(stud.editGrade(command[2], newGrade)) { // edits students grade, if returns true enters if loop
      return "Successfully changed " + stud.getName() + "'s grade in " + command[2] + " to " 
              + String.format("%.2f", newGrade);
    } else { // grade could not be changed, editGrade() returned false
      return "Could not change grade for student; file write error or student not enrolled in the class!";
    }
    } catch (NoSuchElementException n) { // catch exception if student does not exist in roster
      return "No student found with inputted ID.";
    } catch (NumberFormatException n) {
      return "Invalid use of 'edit' command.\nFormat is 'edit <studentID> <courseName> <grade>'";
    }

  }

  /*
   * Protected helper method that gives the students GPA
   * 
   * @param String object containing studentID
   * @return String containing the student's GPA value
   */
  protected static String gpa(String[] command) {
    if(command.length != 2) { //checks for correct format
      return "Invalid use of 'gpa' command.\nFormat is 'gpa <studentID>'";
    }
    try {
    Student s = roster.getStudent(Integer.parseInt(command[1])); // copies Student from roster HashMap using StudentID as key
    String gpa = "GPA for " + s.getName() + ": " + String.format("%.2f", s.getGPA()); // gets overall gpa of student into String
    return gpa; 
    } catch (NoSuchElementException n) { // catches Exception if student does not exist in roster
      return "No student found with inputted ID.";
    } catch (NumberFormatException n) {
      return "Invalid use of 'gpa' command.\nFormat is 'gpa <studentID>'";
    }
  }

  /*
   * Protected helper method that gives a list of this student's courses and grades
   */
  protected static String list(String[] command) {
    if(command.length != 2) { //checks for correct format
      return "Invalid use of 'list' command.\nFormat is 'list <studentID>'";
    }
    try {
      String list = ""; // String to be returned
      Student stud = roster.getStudent(Integer.parseInt(command[1])); // copies Student from roster HashMap using StudentID as key
      Course[] courses = stud.getCourses(); // makes copy of this student's courses
      Double[] grades = stud.getGrades(); // makes cop of this student's grades
      int courseIndex = 0; // counter to ensure correct grade is displayed for each course 
      for(Course c : courses) { // iterates courses
        list += "Course: " + c.getCourseName() + "\nProfessor: " + c.getProfessor() +
            "\nCredits: " + c.getCredits() + "\nCurrent Grade: " + grades[courseIndex] + "\n"; // adds course, prof, credits, and grade to String
        courseIndex++; // increments courseIndex to mirror iteration of student's courses with student's grades
      }
      return list; // returns String of courses
    } catch (NoSuchElementException n) { // catches exception if student doesn't exist in roster
      return "No student found with inputted ID.";
    } catch (NumberFormatException n) {
      return "Invalid use of 'list' command.\nFormat is 'list <studentID>'";
    }
  }  
}
