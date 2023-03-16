import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.NoSuchElementException;

//--== CS400 File Header Information ==--
//Name: Nolan Smith
//Email: nosmith@wisc.edu
//Team: MA
//TA: Harit Vishwakarma
//Lecturer: Gary Dahl
//Notes to Grader: None

/**
 * Tests the implementation of CS400Graph for the individual component of
 * Project Three: the implementation of Dijsktra's Shortest Path algorithm.
 */
public class GraphTest {

    private CS400Graph<Integer> graph;
    @BeforeEach
    /**
     * Instantiate graph from last week's shortest path activity.
     */
    public void createGraph() {
        graph = new CS400Graph<>();
        // insert vertices 0-9
        for(int i=0;i<11;i++)
            graph.insertVertex(i);
        // insert edges from Week 08. Dijkstra's Activity
        graph.insertEdge(0,2,1);
        graph.insertEdge(1,7,2);
        graph.insertEdge(1,8,4);
        graph.insertEdge(2,4,4);
        graph.insertEdge(2,6,3);
        graph.insertEdge(3,1,6);
        graph.insertEdge(3,7,2);
        graph.insertEdge(4,5,4);
        graph.insertEdge(5,0,2);
        graph.insertEdge(5,1,4);
        graph.insertEdge(5,9,1);
        graph.insertEdge(6,3,1);
        graph.insertEdge(7,0,3);
        graph.insertEdge(7,6,1);
        graph.insertEdge(8,9,3);
        graph.insertEdge(9,4,5);
    }

    /**
     * Checks the distance/total weight cost from the vertex labelled 0 to 8
     * (should be 15), and from the vertex labelled 9 to 8 (should be 17).
     */
    @Test
    public void providedTestToCheckPathCosts() {
        assertTrue(graph.getPathCost(0,8) == 15);    
        assertTrue(graph.getPathCost(9,8) == 17);
    }

    /**
     * Checks the ordered sequence of data within vertices from the vertex 
     * labelled 0 to 8, and from the vertex labelled 9 to 8.
     */
    @Test
    public void providedTestToCheckPathContents() {
        assertTrue(graph.shortestPath(0, 8).toString().equals(
            "[0, 2, 6, 3, 1, 8]"
        ));
        assertTrue(graph.shortestPath(9, 8).toString().equals(
            "[9, 4, 5, 1, 8]"
        ));
    }
    
    /**
     * Checks the distance of longest path from last week's Dijkstra's Algorithm
     * Activity. 
     */
    @Test
    public void testLongestPathFromActivity() {
        assertTrue(graph.getPathCost(3,5) == 14);
    }
    
    /**
     * Checks the sequence of values returned from the longest path from last
     * week's Dijkstra's Algorithm Activity. 
     */
    @Test
    public void testPathFromActivity() {
        assertTrue(graph.shortestPath(3,5).toString().equals("[3, 7, 0, 2, 4, 5]"));
    }
    
    /**
     * Checks that a NoSuchElementException is thrown when no path can be found between 
     * two nodes of the graph. In this example, node 11 has no edges to the rest of the nodes.
     * @throws NoSuchElementException - when no path is found
     */
    @Test
    public void testNoSuchElementExceptionNoPath() {
        try {
          List<Integer> path = graph.shortestPath(3,10); //Should throw a NoSuchElementException
        } catch (NoSuchElementException n) {
          System.out.println("Successfully caught exception when no Path exists!");
        }
    }
    
    /**
     * Checks that a NoSuchElementException is thrown when a call is made for a node that
     * does not exist in the graph.
     * @throws NoSuchElementException - when node does not exist in the graph
     */
    @Test
    public void testNoSuchElementExceptionNoElementInGraph() {
        try {
          List<Integer> path = graph.shortestPath(3,12); //Should throw a NoSuchElementException
        } catch (NoSuchElementException n) {
          System.out.println("Successfully caught exception with invalid node!");
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

}
