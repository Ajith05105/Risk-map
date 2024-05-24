package nz.ac.auckland.se281;

public class InvalidCountry extends Exception {
  public InvalidCountry(String country) {
    super(MessageCli.INVALID_COUNTRY.getMessage(country));
  }
}