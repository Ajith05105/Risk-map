package nz.ac.auckland.se281;

import java.util.*;

public class Graph<T> {
  // Map to store adjacency list for each node
  private Map<T, List<T>> adjNodes;

  // Constructor to initialize the adjacency map
  public Graph() {
    this.adjNodes = new LinkedHashMap<>(); // Use LinkedHashMap to preserve insertion order
  }

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
    // Check if start or end nodes are not in the adjacency map
    if (!adjNodes.containsKey(start) || !adjNodes.containsKey(end)) {
      return null;
    }

    // Queue to store paths during BFS
    Queue<List<T>> queue = new LinkedList<>();
    queue.add(Collections.singletonList(start)); // Add the start node as the initial path
    // Set to keep track of visited nodes
    Set<T> visited = new HashSet<>();
    visited.add(start);

    // BFS loop to explore the graph
    while (!queue.isEmpty()) {
      List<T> path = queue.poll();
      T current = path.get(path.size() - 1);

      if (current.equals(end)) {
        return path;
      }

      for (T neighbor : adjNodes.get(current)) {
        if (!visited.contains(neighbor)) {
          visited.add(neighbor);
          List<T> newPath = new ArrayList<>(path);
          newPath.add(neighbor);
          queue.add(newPath);
        }
      }
    }
    // If no path is found, return null
    return null;
  }
}
