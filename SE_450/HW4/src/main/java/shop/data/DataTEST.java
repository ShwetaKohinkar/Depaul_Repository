package shop.data;

import junit.framework.Assert;
import junit.framework.TestCase;

public class DataTEST extends TestCase {
    public DataTEST(String name) {
        super(name);
    }

    /**
     * Unit test for ConstructorAndAttributes
     */
    public void testConstructorAndAttributes() {
        String title1 = "XX";
        String director1 = "XY";
        String title2 = " XX ";
        String director2 = " XY ";
        int year = 2002;

        Video v1 = Data.newVideo(title1, year, director1);
        assertSame(title1, v1.title());
        assertEquals(year, v1.year());
        assertSame(director1, v1.director());

        Video v2 = Data.newVideo(title2, year, director2);
        assertEquals(title1, v2.title());
        assertEquals(director1, v2.director());
    }

    /**
     * Unit test for ConstructorExceptionYear
     */
    public void testConstructorExceptionYear() {
        try {
            Data.newVideo("X", 1800, "Y");
            fail();
        } catch (IllegalArgumentException e) { }
        try {
            Data.newVideo("X", 5000, "Y");
            fail();
        } catch (IllegalArgumentException e) { }
        try {
            Data.newVideo("X", 1801, "Y");
            Data.newVideo("X", 4999, "Y");
        } catch (IllegalArgumentException e) {
            fail();
        }
    }
    /**
     * Unit test for ConstructorExceptionTitle
     */
    public void testConstructorExceptionTitle() {
        try {
            Data.newVideo(null, 2002, "Y");
            Assert.fail();
        } catch (IllegalArgumentException e) { }
        try {
            Data.newVideo("", 2002, "Y");
            fail();
        } catch (IllegalArgumentException e) { }
        try {
            Data.newVideo(" ", 2002, "Y");
            fail();
        } catch (IllegalArgumentException e) { }
    }

    /**
     * Unit test for ConstructorExceptionDirector
     */
    public void testConstructorExceptionDirector() {
        try {
            Data.newVideo("T1", 2002, null);
            fail();
        } catch (IllegalArgumentException e) { }
        try {
            Data.newVideo("T1", 2002, "");
            fail();
        } catch (IllegalArgumentException e) { }
        try {
            Data.newVideo("T1", 2002, " ");
            fail();
        } catch (IllegalArgumentException e) { }
    }

    /**
     * Unit test for toString function
     */
    public void testToString() {
        String s = Data.newVideo("A",2000,"B").toString();
        assertEquals( s, "A (2000) : B" );
        s = Data.newVideo(" A ",2000," B ").toString();
        assertEquals( s, "A (2000) : B" );
    }
}
