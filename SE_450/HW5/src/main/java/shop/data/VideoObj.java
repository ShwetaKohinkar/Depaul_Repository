package shop.data;

/**
 * Implementation of Video interface.
 * @see Data
 */
final class VideoObj implements Video {
  private final String _title;
  private final int    _year;
  private final String _director;

  /**
   * Initialize all object attributes.
   * Title and director are "trimmed" to remove leading and final space.
   * @throws IllegalArgumentException if object invariant violated.
   */
  VideoObj(String title, int year, String director) {

    checkInvariants(title, year, director);

    _title = title;
    _director = director;
    _year = year;
  }

  /**
   * Function to return Director
   */
  public String director() {
    // TODO
    return this._director;
  }

  /**
   * Function to return Title
   */
  public String title() {
    // TODO
    return this._title;
  }

  /**
   * Function to return year
   */
  public int year() {
    // TODO
    return this._year;
  }

  /**
   * Function to check if object is equal
   * @param thatObject Object to checked
   */
  public boolean equals(Object thatObject) {
    // TODO
    if (super.equals(thatObject)) {
      return true;
    }
    return false;

  }

  /**
   * Returns hashcode
   */
  public int hashCode() {
    //TODO
    int value = 17;
    value = 37 * value + ((this.title() != null) ? this.title().hashCode() : 0);
    value = 37 * value + ((this._year != 0) ? String.valueOf(this._year).hashCode() : 0);
    value = 37 * value + ((this.director() != null) ? this.director().hashCode() : 0);
    return value;

  }

  /**
   * Function to Compare
   * @param that Video Object used for comparison
   */
  public int compareTo(Video that) {
    // TODO
    return this._title.compareTo(that.title()) + Integer.valueOf(this._year).compareTo(Integer.valueOf(that.year()))
            + this._director.compareTo(that.director());
  }

  /**
   * Function to return a string
   */
  public String toString() {
    // TODO
    return this._title + " (" + this._year + ") : " + this._director;
  }

  /**
   * Checks the year constraints
   */
  private boolean checkInvariants(String _title, int _year, String _dir) {
    if (_title != null && !_title.trim().isEmpty() &&
            _dir != null && !_dir.trim().isEmpty() &&
            (_year > 1800 && _year < 5000)) {
      return true;
    } else
      throw new IllegalArgumentException();
  }
}
