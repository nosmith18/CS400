// --== CS400 File Header Information ==--
// Name: Nolan Oliver Smith
// Email: nosmith@wisc.edu
// Team: MA
// TA: Harit Vishwakarma
// Lecturer: Gary Dahl
// Notes to Grader: None

import java.util.LinkedList;
//import org.junit.Test;
//import static org.junit.Assert.*;

/**
 * Binary Search Tree implementation with a Node inner class for representing
 * the nodes within a binary search tree.  You can use this class' insert
 * method to build a binary search tree, and its toString method to display
 * the level order (breadth first) traversal of values in that tree.
 */
public class RedBlackTree<T extends Comparable<T>> {

    /**
     * This class represents a node holding a single value within a binary tree
     * the parent, left, and right child references are always be maintained.
     */
    protected static class Node<T> {
        public T data;
        public Node<T> parent; // null for root node
        public Node<T> leftChild;
        public Node<T> rightChild;
        public boolean isBlack; //isBlack boolean variable
        
        /*
         * Constructor for Node class
         */
        public Node(T data) { 
          this.data = data; 
          isBlack = false;
          }
        /**
         * @return true when this node has a parent and is the left child of
         * that parent, otherwise return false
         */
        public boolean isLeftChild() {
            return parent != null && parent.leftChild == this;
        }
        /**
         * This method performs a level order traversal of the tree rooted
         * at the current node.  The string representations of each data value
         * within this tree are assembled into a comma separated string within
         * brackets (similar to many implementations of java.util.Collection).
         * @return string containing the values of this tree in level order
         */
        @Override
        public String toString() { // display subtree in order traversal
            String output = "[";
            LinkedList<Node<T>> q = new LinkedList<>();
            q.add(this);
            while(!q.isEmpty()) {
                Node<T> next = q.removeFirst();
                if(next.leftChild != null) q.add(next.leftChild);
                if(next.rightChild != null) q.add(next.rightChild);
                output += next.data.toString();
                if(!q.isEmpty()) output += ", ";
            }
            return output + "]";
        }
    }


    protected Node<T> root; // reference to root node of tree, null when empty

    /**
     * Performs a naive insertion into a binary search tree: adding the input
     * data value to a new node in a leaf position within the tree.  After
     * this insertion, no attempt is made to restructure or balance the tree.
     * This tree will not hold null references, nor duplicate data values.
     * @param data to be added into this binary search tree
     * @throws NullPointerException when the provided data argument is null
     * @throws IllegalArgumentException when the tree already contains data
     */
    public void insert(T data) throws NullPointerException,
                                      IllegalArgumentException {
        // null references cannot be stored within this tree
        if(data == null) throw new NullPointerException(
            "This RedBlackTree cannot store null references.");

        Node<T> newNode = new Node<>(data);
        if(root == null) { root = newNode; } // add first node to an empty tree
        else insertHelper(newNode,root); // recursively insert into subtree
        root.isBlack = true;
    }

    /**
     * Recursive helper method to find the subtree with a null reference in the
     * position that the newNode should be inserted, and then extend this tree
     * by the newNode in that position.
     * @param newNode is the new node that is being added to this tree
     * @param subtree is the reference to a node within this tree which the
     *      newNode should be inserted as a descenedent beneath
     * @throws IllegalArgumentException when the newNode and subtree contain
     *      equal data references (as defined by Comparable.compareTo())
     */
    private void insertHelper(Node<T> newNode, Node<T> subtree) {
        int compare = newNode.data.compareTo(subtree.data);
        // do not allow duplicate values to be stored within this tree
        if(compare == 0) throw new IllegalArgumentException(
            "This RedBlackTree already contains that value.");

        // store newNode within left subtree of subtree
        else if(compare < 0) {
            if(subtree.leftChild == null) { // left subtree empty, add here
                subtree.leftChild = newNode;
                newNode.parent = subtree; 
            // otherwise continue recursive search for location to insert
            } else { 
              insertHelper(newNode, subtree.leftChild);
              enforceRBTreePropertiesAfterInsert(newNode);
            }
        }

        // store newNode within the right subtree of subtree
        else {
            if(subtree.rightChild == null) { // right subtree empty, add here
                subtree.rightChild = newNode;
                newNode.parent = subtree;
            // otherwise continue recursive search for location to insert
            } else {
              insertHelper(newNode, subtree.rightChild);
              enforceRBTreePropertiesAfterInsert(newNode);
            }
        }
    }

    /**
     * This method performs a level order traversal of the tree. The string
     * representations of each data value within this tree are assembled into a
     * comma separated string within brackets (similar to many implementations
     * of java.util.Collection, like java.util.ArrayList, LinkedList, etc).
     * @return string containing the values of this tree in level order
     */
    @Override
    public String toString() { return root.toString(); }

    /**
     * Performs the rotation operation on the provided nodes within this BST.
     * When the provided child is a leftChild of the provided parent, this
     * method will perform a right rotation (sometimes called a left-right
     * rotation).  When the provided child is a rightChild of the provided
     * parent, this method will perform a left rotation (sometimes called a
     * right-left rotation).  When the provided nodes are not related in one
     * of these ways, this method will throw an IllegalArgumentException.
     * @param child is the node being rotated from child to parent position
     *      (between these two node arguments)
     * @param parent is the node being rotated from parent to child position
     *      (between these two node arguments)
     * @throws IllegalArgumentException when the provided child and parent
     *      node references are not initially (pre-rotation) related that way
     */
    private void rotate(Node<T> child, Node<T> parent)
        throws IllegalArgumentException {
        // TODO: Implement this method.

        Node<T> grandParent;
        Node<T> childsChild;

        if ( child.parent != parent ) {
                throw new IllegalArgumentException ("This child's parent was not the parent passed, <childNode, ParentNode>");
        }
        else if ( child.isLeftChild() ) { //Child is a left child, perform right rotation.
                childsChild = child.rightChild;
                parent.leftChild = childsChild;
                if(childsChild != null) childsChild.parent = parent;
                child.rightChild = parent;
                if ( parent.parent == null ) { //Parent is base node(no parent of parent)
                        parent.parent = child;
                        child.parent = null;
                        root = child;
                }
                else {
                        grandParent = parent.parent;
                        if ( parent.isLeftChild() ) { //parent is a left child
                                grandParent.leftChild = child;
                        }
                        else { //Parent is a right child
                                grandParent.rightChild = child;
                        }
                        parent.parent = child;
                        child.parent = grandParent;
                }
        }
        else { //Child is a right child, perform a left rotation
                childsChild = child.leftChild;
                parent.rightChild = childsChild;
                if(childsChild != null) childsChild.parent = parent;
                child.leftChild = parent;
                if ( parent.parent == null ) { //If Parent is base node of tree
                        parent.parent = child;
                        child.parent = null;
                        root = child;
                }
                else {
                        grandParent = parent.parent;
                        if ( parent.isLeftChild() ) { //Parent is a left child
                          grandParent.leftChild = child;
                        }
                        else { //Parent is a rightChild
                          grandParent.rightChild = child;
                        }
                        parent.parent = child;
                        child.parent = grandParent;
             }
        }
     }
    
    /*
     * This method enforces the Red-Black properties of the Tree
     * 
     * @param Node newNode that was either the newest node added, or a node passed
     *        via recursion from this method
     */
    private void enforceRBTreePropertiesAfterInsert(Node<T> newNode) {
      Node<T> parent = null;
      Node<T> parentSibling = null;
      Node<T> grandParent = null;
      
      parent = newNode.parent;
      if (parent != null) {
        grandParent = parent.parent;
      }
      
      if ( !newNode.equals(root)) {
        if ( parent.isLeftChild() && grandParent != null && grandParent.rightChild != null) { //assign parentSibling to correct node, if one exists
          parentSibling = grandParent.rightChild;
        } else if (!parent.isLeftChild() && grandParent != null && grandParent.leftChild != null) {
          parentSibling = grandParent.leftChild;
        }
      
        if ( parent != null && !parent.isBlack) { //check if parent is RED and not NULL, enter if it is
          if ( parentSibling == null) { //ParentSibling is Null, same as if it was black (Case 2 or 3)
            if ( newNode.isLeftChild() == parent.isLeftChild()) { //Case 2
              rotate(parent, grandParent);
              parent.isBlack = true;
              grandParent.isBlack = false;
            } else { //Case 3
              rotate(newNode, parent);
              enforceRBTreePropertiesAfterInsert(parent);
            }
          }
          else if ( !parentSibling.isBlack ) { //parentSibling is Red, CASE 1
            parent.isBlack = true; //change colors and recurse up the tree (grandParent)
            parentSibling.isBlack = true;
            grandParent.isBlack = false;
            enforceRBTreePropertiesAfterInsert(grandParent);
          } else if ( parentSibling.isBlack ) { //CASE 2 or 3
            if ( newNode.isLeftChild() == parentSibling.isLeftChild()) { //CASE 3: Rotate into CASE 2 and enforce color properties
              rotate(newNode, parent);
              enforceRBTreePropertiesAfterInsert(parent);
            } else { // CASE 2: Rotate and change colors
              rotate(parent, grandParent);
              parent.isBlack = true;
              grandParent.isBlack = false;
            }
          }
        }
      }
    }  
 }
