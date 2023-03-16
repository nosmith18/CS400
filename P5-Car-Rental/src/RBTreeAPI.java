import java.util.NoSuchElementException;

// --== CS400 File Header Information ==--
// Name: Nolan Smith
// Email: nosmith@wisc.edu
// Team: MA
// Role: Backend Developer
// TA: Harit Vishwakarma
// Lecturer: Prof. Gary Dahl
// Notes to Grader: N/A

/**
 * This class is the full implementation of the RedBlackTree including the remove method.
 * 
 * @author - Nolan Smith
 */
public class RBTreeAPI extends RedBlackTree<Vehicle>{

  /**
   * This remove method calls the removeHelper method to find and remove the correct Vehicle
   * from the tree.
   * 
   * @returns Vehicle - the vehicle data of the node that was removed from tree
   */
  public Vehicle remove(String VIN) {
    //Calls removeHelper method to remove
    return removeHelper(VIN, root);
  }
  
  private Vehicle removeHelper(String VIN, Node<Vehicle> node) throws NoSuchElementException {
    //If node is NULL, this VIN does not exist in the tree
    if (node == null) throw new NoSuchElementException("No Vehicle with given VIN found in Red Black Tree.");
    
    else if (VIN.compareTo(node.data.getVIN()) < 0) { //VIN is less than currentNode's VIN, check leftChild
      return removeHelper(VIN, node.leftChild);
    }
    else if (VIN.compareTo(node.data.getVIN()) > 0) { //VIN is more than currentNode's VIN, check rightChild
      return removeHelper(VIN, node.rightChild);
    }
    else { //the current node matches the VIN being searched for, begin remove 
      if (node.leftChild != null && node.rightChild != null) { //node has two children, removal CASE 3
        Vehicle removedVehicle = node.data; //save node that is being removed
        Node<Vehicle> replacementNode = getSmallest(node.rightChild);
        node.data = replacementNode.data; //copy Vehicle data 
        if (replacementNode.isLeftChild()) {
          replacementNode.parent.leftChild = null;
        }
        else replacementNode.parent.rightChild = null;
        return removedVehicle; //done
      }
      else if (node.leftChild == null && node.rightChild != null) { //node has a rightChild, NOT a LeftChild. Removal Case 2
        
        Node<Vehicle> removedNode = node; //save node that is being removed
        node = node.rightChild; //swap
        node.parent = removedNode.parent; //copy parent from removed node
        node.isBlack = removedNode.isBlack; //copy color

        if (node.parent == null) root = node; //update root if newNode has no parent
        
        if (removedNode.isLeftChild()) { //set parent's correct child to the new swapped node
          node.parent.leftChild = node;
        } else node.parent.rightChild = node;
        return removedNode.data; //done
      }
      else if (node.leftChild != null && node.rightChild == null) { //node has a leftChild, NOT a rightChild. Removal Case 2
        
        Node<Vehicle> removedNode = node; //save node that is being removed
        node = node.leftChild; //swap
        node.parent = removedNode.parent; //copy parent from removed node
        node.isBlack = removedNode.isBlack; //copy color
        
        if (node.parent == null) root = node; //update root if newNode has no parent
        
        if (removedNode.isLeftChild()) { //set parent's correct child to the new swapped node
          node.parent.leftChild = node;
        } else node.parent.rightChild = node;
        
        return removedNode.data; //done
      }
      else { //this node has NO children! Begin hardest remove case - CASE 1 (Red or Black)
        if (node.data.compareTo(root.data) == 0) { //removing the root with no children
          clear();
          return node.data;
        } else
        if (node.isBlack) { //Node is BLACK
          
          Node<Vehicle> removedNode = node; //save node that is being removed
          
          removeDoubleBlack(removedNode);
          
          if (removedNode.isLeftChild()) { //set parent's correct child to the new swapped node
            node.parent.leftChild = null;
          } else node.parent.rightChild = null;
          
          return removedNode.data;
        }
        else { //Node is Red
          
          Node<Vehicle> removedNode = node; //save node that is being removed
          
          if (removedNode.isLeftChild()) { //set parent's correct child to the new swapped node
            node.parent.leftChild = null;
          } else node.parent.rightChild = null;
          
          return removedNode.data; //done
        }
      }
    }
  }
  
  
  /**
   * This private helper method fixes the problem of a DoubleBlack node in a RBTree. Does not return
   * anything, simply rotates and reorders the tree to solve color issues.
   * 
   * @param node - the DoubleBlack node to be fixed
   */
  private void removeDoubleBlack(Node<Vehicle> node) {
    if ( node.parent != null ) { //make sure parent is not equal to null(root), required for getting sibling
      if ( node.isLeftChild()) { //DoubleBlack is leftChild
        if ( node.parent.rightChild.isBlack != true) { //Sibling is Red -> CASE 1
          
//          Node<Vehicle> sibling = node.parent.rightChild; //assign parent and sibling
//          Node<Vehicle> parent = node.parent;
          rotate(node.parent.rightChild, node.parent); //rotate
          node.parent.isBlack = false; //change colors of parent and sibling
          node.parent.rightChild.isBlack = true;
          removeDoubleBlack(node);
          
        } else { //sibling must be black
          
          Node<Vehicle> leftNephew = node.parent.rightChild.leftChild;
          Node<Vehicle> rightNephew = node.parent.rightChild.rightChild;
          
          if ( (leftNephew == null || leftNephew.isBlack) && 
              (rightNephew == null || rightNephew.isBlack)) { //two black nephews -> CASE 2
            node.parent.rightChild.isBlack = false; //sibling to Red
            if (node.parent.isBlack == false) { //parent is Red
              node.parent.isBlack = true;
            }
            else removeDoubleBlack(node.parent); //parent is black
            
          }
          else if ( (leftNephew != null && leftNephew.isBlack != true) //CASE 3!
                   && (rightNephew == null || rightNephew.isBlack)) { //same side nephew is Red, opposite is black
            rotate(node.parent.rightChild.leftChild, node.parent.rightChild); //rotate left nephew and sibling
            node.parent.rightChild.isBlack = true;
            node.parent.rightChild.rightChild.isBlack = false;
            removeDoubleBlack(node); //call method again, now in case 4!
          }
          else if ( rightNephew != null && rightNephew.isBlack == false) { //CASE 4, opposite side nephew is Red
            rotate(node.parent.rightChild, node.parent); //rotate parent and sibling
            boolean parentColor = node.parent.isBlack;
            node.parent.isBlack = true;
            node.parent.parent.isBlack = parentColor; //swap parent and sibling colors
            node.parent.parent.rightChild.isBlack = true; //done
          }
        }
      }
      else { //DoubleBlack is a rightChild
        if ( node.parent.leftChild.isBlack != true) { //Sibling is Red -> CASE 1
          
//        Node<Vehicle> sibling = node.parent.rightChild; //assign parent and sibling
//        Node<Vehicle> parent = node.parent;
        rotate(node.parent.leftChild, node.parent); //rotate
        node.parent.isBlack = false; //change colors of parent and sibling
        node.parent.leftChild.isBlack = true;
        removeDoubleBlack(node);
        
        } else { //sibling must be black
        
          Node<Vehicle> leftNephew = node.parent.leftChild.leftChild;
          Node<Vehicle> rightNephew = node.parent.leftChild.rightChild;
        
          if ( (leftNephew == null || leftNephew.isBlack) && 
              (rightNephew == null || rightNephew.isBlack)) { //two black nephews -> CASE 2
            node.parent.leftChild.isBlack = false; //sibling to Red
            if (node.parent.isBlack == false) { //parent is Red
              node.parent.isBlack = true;
            }
            else removeDoubleBlack(node.parent); //parent is black
          
          }
          else if ( (rightNephew != null && rightNephew.isBlack != true) //CASE 3!
                && (leftNephew == null || leftNephew.isBlack)) { //same side nephew is Red, opposite is black
            rotate(node.parent.leftChild.rightChild, node.parent.leftChild); //rotate right nephew and sibling
            node.parent.leftChild.isBlack = true;
            node.parent.leftChild.leftChild.isBlack = false;
            removeDoubleBlack(node); //call method again, now in case 4!
          }
          else if ( leftNephew != null && leftNephew.isBlack == false) { //CASE 4, opposite side nephew is Red
            rotate(node.parent.leftChild, node.parent); //rotate parent and sibling
            boolean parentColor = node.parent.isBlack;
            node.parent.isBlack = true;
            node.parent.parent.isBlack = parentColor; //swap parent and sibling colors
            node.parent.parent.leftChild.isBlack = true; //done
          }
        }
      }
    }
  }
  
  
  /**
   * This private helper method assists with removal of a node with two children by finding
   * the smallest value in the rightSubtree of the removedNode.
   * 
   * @param Node - current node being checked for leftChild in tree
   * @return Node<Vehicle> - the smallest leftChild node of the tree
   */
  private Node<Vehicle> getSmallest(Node<Vehicle> node) { 
    if (node.leftChild != null) { //there exists a smaller node
      return getSmallest(node.leftChild);
    }
    return node; //else return this node, as it is the smallest
  }
  
  
  /**
   * This method returns the Vehicle that matches the VIN input if it exists in the Tree
   * 
   * @return the Vehicle that matches the VIN input
   */
  public Vehicle getVehicle(String VIN){
    //Calls the getVehicleHelper method that makes recursive calls to itself in order to find the vehicle node,
    //that matches the VIN input
    return getVehicleHelper(VIN, root);
  }
  
  
  /**
   * This private helper method recursively traverses the Tree searching for the Vehicle node that matches the vin parameter
   * 
   * @param VIN - the unique Vehicle ID Number that is being searched for
   * @param node - the current node that is being checked
   * 
   * @throws NoSuchElementException - if the VIN does not match any vehicle in the Tree
   * 
   * @return Vehicle - the Vehicle object that matches the VIN input parameter
   */
  private Vehicle getVehicleHelper(String VIN, Node<Vehicle> node) throws NoSuchElementException {
    if ( node != null) { //check if currNode is NULL
      if ( VIN.compareTo(node.data.getVIN()) == 0) { //Vin matches this node's VIN
        return node.data;
      }
      else if (VIN.compareTo(node.data.getVIN()) < 0) { //VIN is less than currentNode, check leftChild
        return getVehicleHelper(VIN, node.leftChild);
      }
      else if (VIN.compareTo(node.data.getVIN()) > 0) { //VIN is more than currentNode, check rightChild
        return getVehicleHelper(VIN, node.rightChild);
      }
    }
    throw new NoSuchElementException("No Vehicle with given VIN found in Red Black Tree."); //The VIN did not match an vehicle in the Tree, or the vehicle is not in the expected place
  }
  
  
  /**
   * This clear method empties the entire tree by setting root node to null.
   */
  public void clear() {
    root = null;
  }
  
  
  /**
   * This method returns the size of the Tree but uses a recursive helper method to calculate,
   * starting from the private Root node.
   * 
   * @return size - number of vehicles in the Tree
   */
  public int size() {
    //Calls the private sizeHelper method beginning at the root node.
    return sizeHelper(root);
  }
  
  
  /**
   * Private helper method that recursively calls itself to add up all nodes in the Tree
   * 
   * @return size - the calculated size of the Tree, adding up from the lowest node
   */
  private int sizeHelper(Node<Vehicle> node) {
    if (node != null) {
      return 1 + sizeHelper(node.leftChild) + sizeHelper(node.rightChild);
    }
    return 0;
  }
  
  /**
   * This 
   * 
   * @param child is the node being rotated from child to parent position
   *      (between these two node arguments)
   * @param parent is the node being rotated from parent to child position
   *      (between these two node arguments)
   * @throws IllegalArgumentException when the provided child and parent
   *      node references are not initially (pre-rotation) related that way
   */
  private void rotate(Node<Vehicle> child, Node<Vehicle> parent)
      throws IllegalArgumentException {
      // TODO: Implement this method.

      Node<Vehicle> grandParent;
      Node<Vehicle> childsChild;

      if ( child.parent != parent ) {
              throw new IllegalArgumentException ("This child's parent was not the parent passed, <childNode, ParentNode>");
      }
      else if ( child.isLeftChild() ) { //Child is a left child, perform right rotation.

              childsChild = child.rightChild;
              parent.leftChild = childsChild;
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
  
}
