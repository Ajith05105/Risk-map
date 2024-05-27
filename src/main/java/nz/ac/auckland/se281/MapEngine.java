package nz.ac.auckland.se281;

import java.util.*;

public class MapEngine {

  private Map<String, Country> countryDetailsMap;
  private Graph<Country> graph;

  public MapEngine() {
    countryDetailsMap = new HashMap<>();
    graph = new Graph<>();
    loadMap(); // keep this method invocation
  }

  /** Invoked one time only when constructing the MapEngine class. */
  private void loadMap() {
    List<String> countries = Utils.readCountries();

    // Creating country nodes in the graph
    for (String c : countries) {
      String[] parts = c.split(",");
      Country country = new Country(parts[0], parts[1], Integer.parseInt(parts[2]));
      countryDetailsMap.put(parts[0], country);
      graph.addNode(country);
    }

    // Creating edges between countries
    List<String> adjacencies = Utils.readAdjacencies();
    for (String adjacency : adjacencies) {
      String[] parts = adjacency.split(",");
      String fromCountryName = parts[0];
      List<String> adjacentCountries = Arrays.asList(parts).subList(1, parts.length);

      Country fromCountry = countryDetailsMap.get(fromCountryName);
      for (String adjacentCountryName : adjacentCountries) {
        Country adjacentCountry = countryDetailsMap.get(adjacentCountryName);
        graph.addEdge(fromCountry, adjacentCountry);
      }
    }
  }

  /** This method is invoked when the user runs the command info-country. */
  public void showInfoCountry() {
    boolean validCountry = false;
    MessageCli.INSERT_COUNTRY.printMessage();

    while (!validCountry) {
      String countryName = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
      try {
        isCountryValid(countryName);
        Country country = countryDetailsMap.get(countryName);
        country.toString();
        MessageCli.COUNTRY_INFO.printMessage(
            country.getCountryName(), country.getContinent(), String.valueOf(country.getTaxRate()));
        validCountry = true;
      } catch (InvalidCountry e) {
        MessageCli.INVALID_COUNTRY.printMessage(countryName);
      }
    }
  }

  public void isCountryValid(String countryName) throws InvalidCountry {
    if (!countryDetailsMap.containsKey(countryName)) {
      throw new InvalidCountry(countryName);
    }
  }

  /** This method is invoked when the user runs the command route. */
  public void showRoute() {
    boolean validFromCountry = false;
    boolean validToCountry = false;
    String fromCountryName = "";
    String toCountryName = "";

    MessageCli.INSERT_SOURCE.printMessage();
    while (!validFromCountry) {
      fromCountryName = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
      try {
        isCountryValid(fromCountryName);
        validFromCountry = true;
      } catch (InvalidCountry e) {
        MessageCli.INVALID_COUNTRY.printMessage(fromCountryName);
      }
    }

    MessageCli.INSERT_DESTINATION.printMessage();
    while (!validToCountry) {
      toCountryName = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
      try {
        isCountryValid(toCountryName);
        validToCountry = true;
      } catch (InvalidCountry e) {
        MessageCli.INVALID_COUNTRY.printMessage(toCountryName);
      }
    }

    List<String> route = findShortestRoute(fromCountryName, toCountryName);
    if (route != null) {
      MessageCli.ROUTE_INFO.printMessage(String.valueOf(route));
    } else {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
    }
  }

  private List<String> findShortestRoute(String fromCountryName, String toCountryName) {
    Country fromCountry = countryDetailsMap.get(fromCountryName);
    Country toCountry = countryDetailsMap.get(toCountryName);

    // BFS to find the shortest path
    Queue<List<Country>> queue = new LinkedList<>();
    Set<Country> visited = new HashSet<>();

    queue.add(Arrays.asList(fromCountry));
    visited.add(fromCountry);

    while (!queue.isEmpty()) {
      List<Country> path = queue.poll();
      Country currentCountry = path.get(path.size() - 1);

      if (currentCountry.equals(toCountry)) {
        return convertPathToStringList(path);
      }

      for (Country neighbor : graph.getNeighbors(currentCountry)) {
        if (!visited.contains(neighbor)) {
          visited.add(neighbor);
          List<Country> newPath = new LinkedList<>(path);
          newPath.add(neighbor);
          queue.add(newPath);
        }
      }
    }
    return null; // No route found
  }

  private List<String> convertPathToStringList(List<Country> path) {
    List<String> stringPath = new LinkedList<>();
    for (Country country : path) {
      stringPath.add(country.getCountryName());
    }
    return stringPath;
  }
}
