package nz.ac.auckland.se281;

import java.util.*;

public class Graph<T> {
  private Map<T, List<T>> adjNodes;

  public Graph() {
    this.adjNodes = new LinkedHashMap<>(); // Use LinkedHashMap to preserve insertion order
  }


  public void setMap(Map<T, List<T>> adjNodes) {
    this.adjNodes = adjNodes;
  }

  

  public List<T> findShortestPath(T start, T end) {
    if (!adjNodes.containsKey(start) || !adjNodes.containsKey(end)) {
      return null;
    }

    Queue<List<T>> queue = new LinkedList<>();
    queue.add(Collections.singletonList(start));
    Set<T> visited = new HashSet<>();
    visited.add(start);

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
    return null;
  }
}
