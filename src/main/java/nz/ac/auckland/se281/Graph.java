package nz.ac.auckland.se281;

import java.util.*;

public class Graph<T> {
  private Map<T, List<T>> adjNodes;

  public Graph() {
    this.adjNodes = new HashMap<>();
  }

  public void addNode(T node) {
    adjNodes.putIfAbsent(node, new ArrayList<>());
  }

  public void addEdge(T node1, T node2) {
    addNode(node1);
    addNode(node2);
    adjNodes.get(node1).add(node2);
    adjNodes.get(node2).add(node1);
  }

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
