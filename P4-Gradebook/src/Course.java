// --== CS400 File Header Information ==--
// Name: Nolan Smith
// Email: nosmith@wisc.edu
// Team: MA
// Role: Front End Developer
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: None

/**
 * This class implements the Course class backend for the University Gradebook
 * CS400 Fall 2020 - Project One Hash Table programming assignment.
 */
public class Course {
    private String courseName;      // name of course
    private String description;     // brief course description
    private String professor;       // professor of course
    private int credits;            // credits of course
    
  /**
   * Constructor method that initializes course with given name, description,
   * professor, and credits
   * 
   * @param name        - name of course
   * @param description - brief description of course
   * @param professor   - professor name of course
   * @param credits     - integer number of credits for course
   */
    public Course(String name, String description, String professor, int credits) {
        this.courseName = name;
        this.description = description;
        this.professor = professor;
        this.credits = credits;
    }
    
  /**
   * Getter method for course name
   * 
   * @return course name
   */
    public String getCourseName() {
        return this.courseName;
    }
    
  /**
   * Getter method for course credits
   * 
   * @return course credits
   */
    public int getCredits() {
        return this.credits;
    }
    
  /**
   * Getter method for course professor name
   * 
   * @return course professor name
   */
    public String getProfessor() {
        return this.professor;
    }
    
  /**
   * Getter method for course description
   * 
   * @return course description
   */
    public String getDescription() {
        return this.description;
    }
    
  /**
   * Checks if passed course has the same content as this course
   * 
   * @return true if two courses have same content, false otherwise
   */
    public boolean contentEquals(Course course) {
        if (this.courseName.equals(course.getCourseName()) &&
            this.professor.equals(course.getProfessor()) &&
            this.credits == course.getCredits())
            return true;
        else
            return false;   
    }
}