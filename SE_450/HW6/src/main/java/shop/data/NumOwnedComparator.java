package shop.data;

import java.util.Comparator;

public class NumOwnedComparator implements Comparator {
    /**
     * Function to compare two objects
     */
    public int compare(Object o1, Object o2) {
        Record r1 = (Record) o1;
        Record r2 = (Record) o2;
        if (r1.video().year() < r2.video().year())
            return -1;
        else if (r1.video().year() > r2.video().year())
            return 1;
        else return 0;
    }
}