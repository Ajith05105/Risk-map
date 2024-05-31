package nz.ac.auckland.se281;

import java.util.*;

public class Graph<T> {
  // Map to store adjacency list for each node
  private Map<T, List<T>> adjNodes;

  // Constructor to initialize the adjacency map
  public Graph() {
    this.adjNodes = new LinkedHashMap<>(); // Use LinkedHashMap to preserve insertion order
  }

  /**
   * This method sets the adjacency map for the graph.
   *
   * @param adjNodes the adjacency map
   */
  public void setMap(Map<T, List<T>> adjNodes) {
    this.adjNodes = adjNodes;
  }

  /**
   * This method finds the shortest path between two nodes in the graph using BFS.
   *
   * @param start the start node
   * @param end the end node
   * @return the shortest path as a list of nodes, or null if no path is found
   */
  public List<T> findShortestPath(T start, T end) {
    if (!adjNodes.containsKey(start) || !adjNodes.containsKey(end)) {
      return null; // Return null if start or end node doesn't exist
    }

    // Queue for BFS
    Queue<T> queue = new LinkedList<>();
    queue.add(start);

    // Maps to store the paths
    Map<T, T> predecessors = new HashMap<>();
    Set<T> visited = new HashSet<>();
    visited.add(start);

    while (!queue.isEmpty()) {
      T current = queue.poll();

      // If we reach the end node, build the path
      if (current.equals(end)) {
        return buildPath(predecessors, start, end);
      }

      // Explore neighbors
      for (T neighbor : adjNodes.get(current)) {
        if (!visited.contains(neighbor)) {
          queue.add(neighbor);
          visited.add(neighbor);
          predecessors.put(neighbor, current);
        }
      }
    }
    return null; // Return null if no path is found
  }

  /**
   * This method builds the path from the start node to the end node using the predecessors map.
   *
   * @param predecessors the map of predecessors
   * @param start the start node
   * @param end the end node
   * @return the path as a list of nodes, or null if the start node is not in the path
   */
  private List<T> buildPath(Map<T, T> predecessors, T start, T end) {
    LinkedList<T> path = new LinkedList<>();
    for (T at = end; at != null; at = predecessors.get(at)) {
      path.addFirst(at);
    }
    // Check if the start node is in the path
    if (path.getFirst().equals(start)) {
      return path;
    } else {
      return null;
    }
  }
}
