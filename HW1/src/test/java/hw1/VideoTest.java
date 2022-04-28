package hw1;

import junit.framework.Assert;
import junit.framework.TestCase;

// TODO: complete the tests

public class VideoTest extends TestCase
{
    public VideoTest(String name) {
        super(name);
    }


    public void testConstructorAndAttributes() {
        String title1 = "XX";
        String director1 = "XY";
        String title2 = " XX ";
        String director2 = " XY ";
        int year = 2002;

        VideoObj v1 = new VideoObj(title1, year, director1);
        Assert.assertSame(title1, v1.title());
        Assert.assertEquals(year, v1.year());
        Assert.assertSame(director1, v1.director());

        VideoObj v2 = new VideoObj(title2, year, director2);
        Assert.assertEquals(title1, v2.title());
        Assert.assertEquals(director1, v2.director());
    }

    public void testConstructorExceptionYear() {
        try {
            new VideoObj("X", 1800, "Y");
            fail();
        } catch (IllegalArgumentException e) { }
        try {
            new VideoObj("X", 5000, "Y");
            fail();
        } catch (IllegalArgumentException e) { }
        try {
            new VideoObj("X", 1801, "Y");
            new VideoObj("X", 4999, "Y");
        } catch (IllegalArgumentException e) {
            fail();
        }
    }

    public void testConstructorExceptionTitle()
    {
        try {
            new VideoObj(null, 2002, "Y");
            fail();
        } catch (IllegalArgumentException e) { }
        try {
            new VideoObj("", 2002, "Y");
            fail();
        } catch (IllegalArgumentException e) { }
        try {
            new VideoObj(" ", 2002, "Y");
            fail();
        } catch (IllegalArgumentException e) { }
    }

    public void testConstructorExceptionDirector()
    {
        // TODO
    }

    public void testHashCode()
    {
        Assert.assertEquals
                (-875826552,
                        new VideoObj("None", 2009, "Zebra").hashCode());
        Assert.assertEquals
                (-1391078111,
                        new VideoObj("Blah", 1954, "Cante").hashCode());
    }

    /**
     * Unit test for Equals function
     */
    public void testEquals()
    {
        // TODO
        VideoObj v1 = new VideoObj("T1",2022,"D1");
        VideoObj v2 = new VideoObj("T2",2022,"D2");
        VideoObj v3 = new VideoObj("T3",2022,"D3");


        Assert.assertEquals(false,v1.equals(v2));
        Assert.assertEquals(false, v1.equals(v3));

    }

    /**
     * Unit test for CompareTo function
     */
    public void testCompareTo() {
        // TODO
    }

    /**
     * Unit test for toString function
     */
    public void testToString()
    {
        // TODO
        VideoObj v1 = new VideoObj("T",2022,"D");
        String video = v1.title().concat(" ( ").concat(Integer.toString(v1.year())).concat(" ) : ").concat(v1.director());
        Assert.assertEquals(video,v1.toString());
    }
}
