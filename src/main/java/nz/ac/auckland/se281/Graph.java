package nz.ac.auckland.se281;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

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

  public List<String> breathFirstTraversal(String root) {
    List<String> visited = new ArrayList<>();
    Queue<String> queue = new LinkedList<>();
    queue.add(root);
    visited.add(root);
    while (!queue.isEmpty()) {
      String node = queue.poll();
      for (String n : adjNodes.get(node)) {
        if (!visited.contains(n)) {
          visited.add(n);
          queue.add(n);
        }
      }
    }
    return visited;
  }
}
