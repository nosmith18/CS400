// --== CS400 File Header Information ==--
// Name: Nolan Smith
// Email: nosmith@wisc.edu
// Team: MA
// Role: Front End Developer
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.util.ArrayList;

/**
 * This class implements the Student class backend for the University Gradebook
 * CS400 Fall 2020 - Project One Hash Table programming assignment.
 */
public class Student {
	private String name;				// student name
	private final int id;				// student id
	private ArrayList<Course> courses;	// parallel arrays to store courses
	private ArrayList<Double> grades;   // parallel arrays to store grades

  /**
   * Constructor method that initializes student with given name and id
   * 
   * @param name - name of Student
   * @param id - id of Student
   */
	public Student(String name, int id) {
		this.name = name;
		this.id = id;
		this.courses = new ArrayList<Course>();
		this.grades = new ArrayList<Double>();
	}
	
  /**
   * Getter method for student name
   * 
   * @return student name
   */
	public String getName() {
		return this.name;
	}
	
  /**
   * Getter method for student id
   * 
   * @return student id
   */
	public int getID() {
		return this.id;
	}
	
  /**
   * Calculates credit weighted GPA on scale of 0-4
   * 
   * @return credit weighted GPA
   */
	public double getGPA() {
		// iterate over parallel arrays to get weighted grade score for each course
		double totalCredits = 0;
		double weightedGrade = 0;
		Course course;
		for (int i=0; i < this.courses.size(); i++) {
			course = this.courses.get(i);
			totalCredits += (double)course.getCredits();
			weightedGrade += (double)course.getCredits() * this.grades.get(i);
		}
		return weightedGrade / totalCredits;
	}
	
  /**
   * Getter method for student's courses
   * 
   * @return student courses
   */
	public Course[] getCourses() {
		return this.courses.toArray(new Course[this.courses.size()]);
	}
	
  /**
   * Getter method for student's grades
   * 
   * @return student grades
   */
	public Double[] getGrades() {
		return this.grades.toArray(new Double[this.grades.size()]);
	}
	
  /**
   * Changes students grade for course with given name
   * 
   * @param courseName 	- name of course to change grade for
   * @param grade 		- new grade
   * 
   * @return true if grade successfully changed, false otherwise
   */
	public boolean editGrade(String courseName, double grade) {
		Course course;
		for (int i=0; i < this.courses.size(); i++) {
			course = this.courses.get(i);
			if (course.getCourseName().equals(courseName)) {
			  // tries to update file before updating internal hashtable
			    if (!DataHandler.exportGrade(course, this, grade))
			        System.out.println("Couldn't find " + courseName + ".txt in the courses folder."
			            + " The edit will not persist after this application instance is quit.");
			    this.grades.set(i, grade);
				return true;
			}
		}
		// course does not exist for student
		return false;
	}
	
  /**
   * Adds new course to Student
   * 
   * @param course		- new course to add
   * @param grade		- student's grade for new course
   * 
   * @return true if course successfully added, false otherwise
   */
	public boolean addCourse(Course course, double grade) {
		// check if course currently exists
		Course existing;
		for (int i=0; i < this.courses.size(); i++) {
			existing = this.courses.get(i);
			if (course.contentEquals(existing))
				return false;
		}
		// course doesn't exist, add it
		this.courses.add(course);
		this.grades.add(grade);
		return true;
	}

}