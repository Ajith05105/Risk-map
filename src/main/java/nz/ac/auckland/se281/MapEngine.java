package nz.ac.auckland.se281;

import java.util.*;

public class MapEngine {
  private Map<String, Country> countryDetailsMap;
  private Map<Country, List<Country>> adjacenciesMap;
  private Graph<Country> graph;
  List<String> routeNames;
  List<String> continents;
  int taxes;

  public MapEngine() {
    countryDetailsMap = new LinkedHashMap<>();
    adjacenciesMap = new LinkedHashMap<>();
    graph = new Graph<>();
    loadMap(); // keep this method invocation
  }

  private void loadMap() {
    List<String> countries = Utils.readCountries();
    for (String c : countries) {
      String[] parts = c.split(",");
      Country country = new Country(parts[0], parts[1], Integer.parseInt(parts[2]));
      countryDetailsMap.put(parts[0], country);
      adjacenciesMap.put(
          country, new ArrayList<>()); // Initialize the adjacency list for each country
    }

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
        } else {
          System.err.println("Warning: Country " + parts[i] + " not found in the country map.");
        }
      }
      Country fromCountry = countryDetailsMap.get(fromCountryName);
      if (fromCountry != null) {
        adjacenciesMap.put(fromCountry, adjacentCountries);
      } else {
        System.err.println(
            "Warning: Country " + fromCountryName + " not found in the country map.");
      }
    }
    graph.setMap(adjacenciesMap);
  }

  public void showInfoCountry() {
    boolean validCountry = false;
    MessageCli.INSERT_COUNTRY.printMessage();

    while (!validCountry) {
      String countryName = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
      try {
        isCountryValid(countryName);
        Country country = countryDetailsMap.get(countryName);
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
    if (route != null && route.size() > 1) {
      MessageCli.ROUTE_INFO.printMessage(String.valueOf(route));
    } else {
      MessageCli.NO_CROSSBORDER_TRAVEL.printMessage();
    }
  }

  public List<String> findShortestRoute(String fromCountryName, String toCountryName) {
    Country fromCountry = countryDetailsMap.get(fromCountryName);
    Country toCountry = countryDetailsMap.get(toCountryName);

    List<Country> route = graph.findShortestPath(fromCountry, toCountry);
    if (route == null) {
      return null;
    }

    List<String> routeNames = new ArrayList<>();
    for (Country c : route) {
      routeNames.add(c.getCountryName());
    }

    if (route.size() > 1) {
      continents = findContinents(routeNames);
      taxes = calculateTaxes(routeNames);
      MessageCli.TAX_INFO.printMessage(String.valueOf(taxes));
      MessageCli.CONTINENT_INFO.printMessage(String.valueOf(continents));
    }
    return routeNames;
  }

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

  public int calculateTaxes(List<String> routeNames) {
    int totalTaxes = 0;

   

    // Loop through the list, skipping the first and last elements
    for (int i = 1; i < routeNames.size() ; i++) {
        String countryName = routeNames.get(i);
        Country country = countryDetailsMap.get(countryName);
        totalTaxes += country.getTaxRate();
    }

    return totalTaxes;
}

}
