package nz.ac.auckland.se281;

public class Country {
  private String countryName;
  private String continent;
  private int taxRate;

  public Country(String countryName, String continent, int taxRate) {
    this.countryName = countryName;
    this.continent = continent;
    this.taxRate = taxRate;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((countryName == null) ? 0 : countryName.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    if (obj == null) return false;
    if (getClass() != obj.getClass()) return false;
    Country other = (Country) obj;
    if (countryName == null) {
      if (other.countryName != null) return false;
    } else if (!countryName.equals(other.countryName)) return false;
    return true;
  }

  public String getContinent() {
    return continent;
  }

  public int getTaxRate() {
    return taxRate;
  }

  public String getCountryName() {
    return countryName;
  }
  @Override
  public String toString() {
    return countryName;
  }
}
