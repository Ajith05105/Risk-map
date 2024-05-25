package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Graph {
  private Map<String, List<String>> adjNodes;

  public Graph() {
    this.adjNodes = new HashMap<>();
  }

  public void addNode(String node) {
    adjNodes.putIfAbsent(node, new ArrayList<>());
  }

  public void addEdge(String node1, String node2) {
    addNode(node1);
    addNode(node2);
    adjNodes.get(node1).add(node2);
    adjNodes.get(node2).add(node1);
  }
}
