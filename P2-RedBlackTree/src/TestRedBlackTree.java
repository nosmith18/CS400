// --== CS400 File Header Information ==--
// Name: Nolan Oliver Smith
// Email: nosmith@wisc.edu
// Team: MA
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: None

import org.junit.Test;
import static org.junit.Assert.fail;
import org.junit.Before;

public class TestRedBlackTree {

  protected RedBlackTree<Integer> test = null;
  
  @Before
  public void createTest() {
    test = new RedBlackTree<>();
  }
  
  /*
   * This test method tests a small RBTree with a null parentSibling for correct insertion
   * Root node should be Black, two children should be RED
   * 
   * @Fails if root node and two children are wrong colors (Black, two reds)
   */
  @Test
  public void testNullParentSiblingInsert() {
    test.insert(20);
    test.insert(10);
    test.insert(5);
    System.out.println("\n" + test.toString());
    System.out.println("Root is Black: " + test.root.isBlack);
    System.out.println("LeftChild is Black: " + test.root.leftChild.isBlack);
    System.out.println("RightChild is Black: " + test.root.rightChild.isBlack);
    if (test.root.isBlack != true) {
      fail("TestRegularInsertColors failed the tests.");
    }
    if (test.root.leftChild.isBlack != false) {
      fail("TestRegularInsertColors failed the tests.");
    }
    if (test.root.rightChild.isBlack != false) {
      fail("TestRegularInsertColors failed the tests.");
    }
    System.out.println("TestRegularInsertColors passed all tests!!!");
    //Passed
    
  }
  
  /*
   * This test method tests a small RBTree insertion for an insert Case 1, parent and sibling are RED
   * Root node should be Black, two children should be Black, lowest node is RED
   * 
   * @Fails if root node, two children, or lowest node is the wrong color (3 Blacks (Black Height of 2), and one RED)
   */
  @Test
  public void testInsertCase1() { 
    test.insert(20);
    test.insert(10);
    test.insert(30);
    //this addition is a CASE 1
    test.insert(5);
    System.out.println(test.toString());
    System.out.println("Root is Black: " + test.root.isBlack);
    System.out.println("LeftChild is Black: " + test.root.leftChild.isBlack);
    System.out.println("RightChild is Black: " + test.root.rightChild.isBlack);
    System.out.println("Left of LeftChild is Black: " + test.root.leftChild.leftChild.isBlack);
    if (test.root.isBlack != true) {
      fail("TestInsertCase1 failed the tests.");
    }
    if (test.root.leftChild.isBlack == false) {
      fail("TestInsertCase1 failed the tests.");
    }
    if (test.root.rightChild.isBlack == false) {
      fail("TestInsertCase1 failed the tests.");
    }
    if (test.root.leftChild.leftChild.isBlack != false) {
      fail("TestInsertCase1 failed the tests.");
    }
    System.out.println("TestInsertCase1 passed all tests!!!");
    //Passed
    
  }
  
  /*
   * This test method tests a small RBTree insertion for an insert Case 2. Parent is RED, sibling is BLACK 
   * and on the opposite side as the newly inserted node
   * 
   * Root node should be Black, two children should be RED, lowest node is Black
   * 
   * @Fails if root node, two children, or lowest node is the wrong color (2 Blacks (Black Height of 2), and two REDs)
   */
  @Test
  public void testInsertCase2() {
    test.insert(20);
    test.insert(10);
    test.insert(30);
    test.root.rightChild.isBlack = true;
    System.out.println("\n" + test.toString());
    System.out.println("Root is Black: " + test.root.isBlack);
    System.out.println("LeftChild is Black: " + test.root.leftChild.isBlack);
    System.out.println("RightChild is Black: " + test.root.rightChild.isBlack);
    //insert on opposite side, with ParentSibling as a black node
    test.insert(5);
    System.out.println(test.toString()); //checks for correct rotation
    System.out.println("Root is Black: " + test.root.isBlack);
    System.out.println("LeftChild is Black: " + test.root.leftChild.isBlack);
    System.out.println("RightChild is Black: " + test.root.rightChild.isBlack);
    System.out.println("Right of RightChild is Black: " + test.root.rightChild.rightChild.isBlack);
    if (test.root.isBlack != true) {
      fail("TestRegularInsertColors failed the tests.");
    }
    if (test.root.leftChild.isBlack != false) {
      fail("TestRegularInsertColors failed the tests.");
    }
    if (test.root.rightChild.isBlack != false) {
      fail("TestRegularInsertColors failed the tests.");
    }
    if (test.root.rightChild.rightChild.isBlack != true) {
      fail("TestRegularInsertColors failed the tests.");
    }
    System.out.println("TestInsertCase2 passed all tests!!!");
    //Passed
    
  }
  
  /*
   * This test method tests a small RBTree insertion for an insert Case 3. Parent is RED, sibling is Black
   * and on the same side as newly inserted node(both right children in this case)
   * 
   * Root node should be Black, two children should be RED, lowest node is Black. Also checks for correct rotation
   * 
   * @Fails if root node, two children, or lowest node is the wrong color (3 Blacks (Black Height of 2), and one RED)
   */
  @Test
  public void testInsertCase3() {
    test.insert(20);
    test.insert(10);
    test.insert(30);
    test.root.rightChild.isBlack = true;
    System.out.println("\n" + test.toString());
    System.out.println("Root is Black: " + test.root.isBlack);
    System.out.println("LeftChild is Black: " + test.root.leftChild.isBlack);
    System.out.println("RightChild is Black: " + test.root.rightChild.isBlack);
    //insert on opposite side, with ParentSibling as a black node
    test.insert(12);
    System.out.println(test.toString()); //checks for correct rotation
    System.out.println("Root is Black: " + test.root.isBlack);
    System.out.println("LeftChild is Black: " + test.root.leftChild.isBlack);
    System.out.println("RightChild is Black: " + test.root.rightChild.isBlack);
    System.out.println("Right of RightChild is Black: " + test.root.rightChild.rightChild.isBlack);
    if (test.root.isBlack != true) {
      fail("TestInsertCase3 failed the tests.");
    }
    if (test.root.leftChild.isBlack != false) {
      fail("TestInsertCase3 failed the tests.");
    }
    if (test.root.rightChild.isBlack != false) {
      fail("TestInsertCase3 failed the tests.");
    }
    if (test.root.rightChild.rightChild.isBlack != true) {
      fail("TestInsertCase3 failed the tests.");
    }
    System.out.println("TestInsertCase3 passed all tests!!!");
  }
  
  
}
