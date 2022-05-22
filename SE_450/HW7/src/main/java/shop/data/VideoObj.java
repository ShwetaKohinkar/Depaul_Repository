package shop.data;

import java.util.List;
import java.util.WeakHashMap;

/**
 * Implementation of Video interface.
 *
 * @see Data
 */
final class VideoObj implements Video {
    private final String _title;
    private final int _year;
    private final String _director;
    private static WeakHashMap<Integer, Video> _cache = new WeakHashMap<>();

    /**
     * Initialize all object attributes.
     * Title and director are "trimmed" to remove leading and final space.
     *
     * @throws IllegalArgumentException if object invariant violated.
     */
    VideoObj(String title, int year, String director) {

        checkInvariants(title, year, director);

        _title = title.trim();
        _director = director.trim();
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
     *
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
     *
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

    public static Video newVideo(String title, int year, String director) {

        int key = key(title, year, director);
        if (!_cache.containsKey(key)) {
            _cache.put(key, new VideoObj(title, year, director));
        }
        return _cache.get(key);
    }

    private static int key(String title, int year, String director) {
        //TODO
        int key = 17;
        key = 37 * key + (title != null ? title.hashCode() : 0);
        key = 37 * key + (String.valueOf(year).hashCode());
        key = 37 * key + (director != null ? director.hashCode() : 0);

        return key;
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
