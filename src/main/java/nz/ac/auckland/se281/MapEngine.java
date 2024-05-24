package nz.ac.auckland.se281;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/** This class is the main entry point. */
public class MapEngine {

  private Map<String, List<String>> countryDetailsMap;
  private Map<String, List<String>> adjacencyMap;
  private String country;

  public MapEngine() {
    countryDetailsMap = new HashMap<>();
    adjacencyMap = new HashMap<>();
    loadMap(); // keep this method invocation
  }

  /** Invoked one time only when constructing the MapEngine class. */
  private void loadMap() {
    List<String> countries = Utils.readCountries();

    for (String country : countries) {
      String[] parts = country.split(",");
      String countryName = parts[0];
      List<String> countryDetails = Arrays.asList(parts);
      countryDetailsMap.put(countryName, countryDetails);
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
      String country = Utils.capitalizeFirstLetterOfEachWord(Utils.scanner.nextLine());
      try {
        isCountryValid(country);
        List<String> countryDetails = countryDetailsMap.get(country);
        MessageCli.COUNTRY_INFO.printMessage(country, countryDetails.get(1), countryDetails.get(2));
        validCountry = true;
      } catch (InvalidCountry e) {
        MessageCli.INVALID_COUNTRY.printMessage(country);
      }
    }
  }
  

  public void isCountryValid (String country) throws InvalidCountry{
    
    if (!countryDetailsMap.containsKey(country)) {
      throw new InvalidCountry(country);
    }

  }

  /** This method is invoked when the user runs the command route. */
  public void showRoute() {
    // Implementation for route command
  }
}
