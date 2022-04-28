package shop.data;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;

import static org.junit.Assert.*;


// TODO: complete the tests

public class VideoTEST {
    public VideoTEST() {
        super();
    }

    /**
     * Unit test for ConstructorAndAttributes
     */
    @Test
    public void testConstructorAndAttributes() {
        String title1 = "XX";
        String director1 = "XY";
        String title2 = " XX ";
        String director2 = " XY ";
        int year = 2002;

        VideoObj v1 = new VideoObj(title1, year, director1);
        assertSame(title1, v1.title());
        assertEquals(year, v1.year());
        assertSame(director1, v1.director());

        VideoObj v2 = new VideoObj(title2, year, director2);
        assertEquals(title1, v2.title());
        assertEquals(director1, v2.director());
    }

    /**
     * Unit test for ConstructorExceptionYear
     */
    @Test
    public void testConstructorExceptionYear() {
        try {
            new VideoObj("X", 1800, "Y");
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            new VideoObj("X", 5000, "Y");
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            new VideoObj("X", 1801, "Y");
            new VideoObj("X", 4999, "Y");
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    /**
     * Unit test for ConstructorExceptionTitle
     */
    @Test
    public void testConstructorExceptionTitle() {
        try {
            new VideoObj(null, 2002, "Y");
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            new VideoObj("", 2002, "Y");
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            new VideoObj(" ", 2002, "Y");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Unit test for ConstructorExceptionDirector
     */
    @Test
    public void testConstructorExceptionDirector() {
        try {
            new VideoObj("Title", 2002, null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            new VideoObj("Title", 2002, "");
            fail();
        } catch (IllegalArgumentException e) {
        }
        try {
            new VideoObj("Title", 2002, " ");
            fail();
        } catch (IllegalArgumentException e) {
        }
    }

    /**
     * Unit test for HashCode
     */
    @Test
    public void testHashCode() {
        assertEquals
                (-819023634,
                        new VideoObj("None", 2009, "Zebra").hashCode());
        assertEquals
                (-1335049862,
                        new VideoObj("Blah", 1954, "Cante").hashCode());
    }

    /**
     * Unit test for Equals function
     */
    @Test
    public void testEquals() {
        // TODO
        VideoObj v1 = new VideoObj("T1", 2022, "D1");
        VideoObj v2 = new VideoObj("T2", 2022, "D2");
        VideoObj v3 = new VideoObj("T3", 2022, "D3");


        assertEquals(false, v1.equals(v2));
        assertEquals(false, v1.equals(v3));

    }

    /**
     * Unit test for CompareTo function
     */
    @Test
    public void testCompareTo() {
        // TODO
        VideoObj v1 = new VideoObj("T1", 2010, "D1");
        VideoObj v2 = new VideoObj("T2", 2022, "D2");
        VideoObj v3 = new VideoObj("T3", 2022, "D3");
        assertEquals(-3, v1.compareTo(v2));
    }

    /**
     * Unit test for toString function
     */
    @Test
    public void testToString() {
        // TODO
        VideoObj v1 = new VideoObj("T", 2022, "D");
        String video = v1.title().concat(" (").concat(Integer.toString(v1.year())).concat(") : ").concat(v1.director());
        assertEquals(video, v1.toString());
    }
}
