package nz.ac.auckland.se281;

import java.util.*;

public class Graph<T> {
  // Map to store adjacency list for each node
  private Map<T, List<T>> adjNodes;

  // Constructor to initialize the adjacency map
  public Graph() {
    this.adjNodes = new LinkedHashMap<>(); // Use LinkedHashMap to preserve insertion order
  }

  // Method to set the adjacency map
  public void setMap(Map<T, List<T>> adjNodes) {
    this.adjNodes = adjNodes;
  }

  // Method to find the shortest path between two nodes using BFS
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
      // Dequeue the first path from the queue
      List<T> path = queue.poll();
      // Get the last node in the current path
      T current = path.get(path.size() - 1);

      // If the last node is the end node, return the path
      if (current.equals(end)) {
        return path;
      }

      // Explore the neighbors of the current node
      for (T neighbor : adjNodes.get(current)) {
        if (!visited.contains(neighbor)) {
          // Mark the neighbor as visited
          visited.add(neighbor);
          // Create a new path by extending the current path with the neighbor
          List<T> newPath = new ArrayList<>(path);
          newPath.add(neighbor);
          // Enqueue the new path for further exploration
          queue.add(newPath);
        }
      }
    }
    // If no path is found, return null
    return null;
  }
}
