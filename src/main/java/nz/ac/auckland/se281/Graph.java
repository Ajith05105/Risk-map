package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

  public List<T> getNeighbors(T node) {
    return adjNodes.getOrDefault(node, new ArrayList<>());
  }
}
