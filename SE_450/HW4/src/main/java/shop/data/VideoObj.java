package shop.data;

/**
 * Implementation of Video interface.
 *
 * @see Data
 */
final class VideoObj implements Video {
    private final String _title;
    private final int _year;
    private final String _director;

    /**
     * Initialize all object attributes.
     */
    VideoObj(String title, int year, String director) {
        checkInvariants(title, year, director);
        _title = title.trim();
        _director = director.trim();
        _year = year;
    }

    public String director() {
        // TODO
        return this._director;
    }

    public String title() {
        // TODO
        return this._title;
    }

    public int year() {
        // TODO
        return this._year;
    }

    public boolean equals(Object thatObject) {
        // TODO
        if (super.equals(thatObject)) {
            return true;
        }
        return false;

    }

    public int hashCode() {
        int value = 17;
        value = 37 * value + ((this.title() != null) ? this.title().hashCode() : 0);
        value = 37 * value + ((this._year != 0) ? String.valueOf(this._year).hashCode() : 0);
        value = 37 * value + ((this.director() != null) ? this.director().hashCode() : 0);
        return value;

    }

    public int compareTo(Video that) {
        // TODO
        return this._title.compareTo(that.title()) + Integer.valueOf(this._year).compareTo(Integer.valueOf(that.year()))
                + this._director.compareTo(that.director());
    }

    public String toString() {
        // TODO
        return this._title + " (" + this._year + ") : " + this._director;
    }

    private boolean checkInvariants(String _title, int _year, String _dir) {
        if (_title != null && !_title.trim().isEmpty() &&
                _dir != null && !_dir.trim().isEmpty() &&
                (_year > 1800 && _year < 5000)) {
            return true;
        } else
            throw new IllegalArgumentException();
    }
}
