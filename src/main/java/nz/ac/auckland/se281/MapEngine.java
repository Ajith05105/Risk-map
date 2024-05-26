package nz.ac.auckland.se281;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** This class is the main entry point. */
public class MapEngine {

  private Map<String, Country> countryDetailsMap;
  private Map<String, List<String>> adjacencyMap;

  public MapEngine() {
    countryDetailsMap = new HashMap<>();
    adjacencyMap = new HashMap<>();
    loadMap(); // keep this method invocation
  }

  /** Invoked one time only when constructing the MapEngine class. */
  private void loadMap() {
    List<String> countries = Utils.readCountries();

    for (String c : countries) {
      String[] parts = c.split(",");
      Country country = new Country(parts[0], parts[1], Integer.parseInt(parts[2]));
      countryDetailsMap.put(parts[0], country);
    }

    List<String> adjacencies = Utils.readAdjacencies();
    for (String adjacency : adjacencies) {
      String[] parts = adjacency.split(",");
      String countryName = parts[0];
      List<String> adjacentCountries =
          Arrays.asList(parts).subList(1, parts.length); // Exclude the country name itself
      adjacencyMap.put(countryName, adjacentCountries);
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
    // Implementation for route command
    Graph<Country> graph = new Graph<Country>();
    for (String countryName : adjacencyMap.keySet()) {
      Country country = countryDetailsMap.get(countryName);
      graph.addNode(country);

      for (String adjacentCountryString : adjacencyMap.get(countryName)) {
        String[] parts = adjacentCountryString.split(",");
        for (String adjacentCountryName : parts) {
          Country adjacentCountry = countryDetailsMap.get(adjacentCountryName);
          graph.addEdge(country, adjacentCountry);
        }
      }
    }
  }
}
