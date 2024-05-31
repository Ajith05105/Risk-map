package nz.ac.auckland.se281;

import java.util.*;

public class MapEngine {
  private Map<String, Country> countryDetailsMap;
  private Map<Country, List<Country>> adjacenciesMap;
  private Graph<Country> graph;
  private List<String> continents;
  private int taxes;

  public MapEngine() {
    countryDetailsMap = new LinkedHashMap<>();
    adjacenciesMap = new LinkedHashMap<>();
    graph = new Graph<>();
    loadMap(); // Load country and adjacency data
  }

  private void loadMap() {
    // Read country data from external source
    List<String> countries = Utils.readCountries();
    for (String c : countries) {
      String[] parts = c.split(",");
      Country country = new Country(parts[0], parts[1], Integer.parseInt(parts[2]));
      countryDetailsMap.put(parts[0], country);
      adjacenciesMap.put(
          country, new ArrayList<>()); // Initialize the adjacency list for each country
    }

    // Read adjacency data from external source
    List<String> adjacencies = Utils.readAdjacencies();
    for (String adjacency : adjacencies) {
      String[] parts = adjacency.split(",");
      String fromCountryName = parts[0];
      List<Country> adjacentCountries =
          new ArrayList<>(); // List to store adjacent countries as Country objects
      for (int i = 1; i < parts.length; i++) {
        Country adjacentCountry = countryDetailsMap.get(parts[i]);
        if (adjacentCountry != null) {
          adjacentCountries.add(adjacentCountry);
        }
      }
      Country fromCountry = countryDetailsMap.get(fromCountryName);
      if (fromCountry != null) {
        adjacenciesMap.put(fromCountry, adjacentCountries);
      }
    }
    // Set the map in the graph
    graph.setMap(adjacenciesMap);
  }

  /*
   * This method shows the information of a country.
   */
  public void showInfoCountry() {
    boolean validCountry = false;
    MessageCli.INSERT_COUNTRY.printMessage();

    while (!validCountry) {
      String countryName = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
      try {
        isCountryValid(countryName); // Check if the country is valid
        Country country = countryDetailsMap.get(countryName);
        // Print country information
        MessageCli.COUNTRY_INFO.printMessage(
            country.getCountryName(), country.getContinent(), String.valueOf(country.getTaxRate()));
        validCountry = true;
      } catch (InvalidCountry e) {
        MessageCli.INVALID_COUNTRY.printMessage(countryName);
      }
    }
  }

  /**
   * This method checks if the country is valid.
   *
   * @param countryName the name of the country
   * @throws InvalidCountry if the country is not valid
   */
  public void isCountryValid(String countryName) throws InvalidCountry {
    if (!countryDetailsMap.containsKey(countryName)) {
      throw new InvalidCountry(countryName);
    }
  }

  /** This method shows the information of a country. */
  public void showRoute() {
    boolean validFromCountry = false;
    boolean validToCountry = false;
    String fromCountryName = "";
    String toCountryName = "";

    MessageCli.INSERT_SOURCE.printMessage();
    while (!validFromCountry) {
      fromCountryName = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
      try {
        isCountryValid(fromCountryName); // Check if the source country is valid
        validFromCountry = true;
      } catch (InvalidCountry e) {
        MessageCli.INVALID_COUNTRY.printMessage(fromCountryName);
      }
    }

    MessageCli.INSERT_DESTINATION.printMessage();
    while (!validToCountry) {
      toCountryName = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
      try {
        isCountryValid(toCountryName); // Check if the destination country is valid
        validToCountry = true;
      } catch (InvalidCountry e) {
        MessageCli.INVALID_COUNTRY.printMessage(toCountryName);
      }
    }

    // Find and show the shortest route
    List<String> route = findShortestRoute(fromCountryName, toCountryName);
    if (route != null && route.size() > 1) {
      MessageCli.ROUTE_INFO.printMessage(String.valueOf(route));
    } else {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
    }
  }

  /**
   * This method finds the shortest route between two countries.
   *
   * @param fromCountryName the name of the source country
   * @param toCountryName the name of the destination country
   * @return the list of country names in the shortest route
   */
  public List<String> findShortestRoute(String fromCountryName, String toCountryName) {
    Country fromCountry = countryDetailsMap.get(fromCountryName);
    Country toCountry = countryDetailsMap.get(toCountryName);

    // Find the shortest path using the graph
    List<Country> route = graph.findShortestPath(fromCountry, toCountry);
    if (route == null) {
      return null;
    }

    // Convert the route to a list of country names
    List<String> routeNames = new ArrayList<>();
    for (Country c : route) {
      routeNames.add(c.getCountryName());
    }

    if (route.size() > 1) {
      // Find the continents and calculate taxes for the route
      continents = findContinents(routeNames);
      taxes = calculateTaxes(routeNames);
      MessageCli.TAX_INFO.printMessage(String.valueOf(taxes));
      MessageCli.CONTINENT_INFO.printMessage(String.valueOf(continents));
    }
    return routeNames;
  }

  /**
   * This method finds the continents visited in the route.
   *
   * @param routeNames the list of country names in the route
   * @return the list of continents visited
   */
  public List<String> findContinents(List<String> routeNames) {
    List<String> continents = new ArrayList<>();
    for (String countryName : routeNames) {
      Country country = countryDetailsMap.get(countryName);
      if (!continents.contains(country.getContinent())) {
        continents.add(country.getContinent());
      }
    }
    return continents;
  }

  /**
   * This method calculates the total taxes for the route.
   *
   * @param routeNames the list of country names in the route
   * @return the total taxes for the route
   */
  public int calculateTaxes(List<String> routeNames) {
    int totalTaxes = 0;

    // Loop through the list, skipping the first and last elements
    for (int i = 1; i < routeNames.size(); i++) {
      String countryName = routeNames.get(i);
      Country country = countryDetailsMap.get(countryName);
      totalTaxes += country.getTaxRate();
    }

    return totalTaxes;
  }
}
