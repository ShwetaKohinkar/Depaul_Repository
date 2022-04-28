package hw2;

import junit.framework.Assert;
import junit.framework.TestCase;

/**
 * Class to write Junit
 */
public class MyLinkedTest extends TestCase
{
    /**
     * Test function to delete nodes
     */
    public void testDelete()
    {
        MyLinked b = new MyLinked();

        for (double i = 1; i < 13; i++)
        {
            b.add(i);
        }
        b.print("Total element : ", b);
        Assert.assertEquals(12, b.size());

        b.delete(12);
        b.print("deleted at the end", b);
        Assert.assertEquals(11, b.size());

        b.delete(1);
        b.print("deleted at the beginning", b);
        Assert.assertEquals(10, b.size());

        b.delete(5);
        b.print("deleted at middle", b);
        Assert.assertEquals(9, b.size());

        b.print("Test", b);


    }

    /**
     * Test Function to remove nodes
     */
    public void testRemove()
    {
        MyLinked b = new MyLinked();

        for (double i = 1; i <= 6; i++)
        {
            b.add(i);
            b.add(i);
        }

        b.remove(2);
        b.print("removed 8's from the list", b);
        Assert.assertEquals(10, b.size());

        b.remove(5);
        b.print("removed all 5's from the list", b);
        Assert.assertEquals(8, b.size());

        b.remove(1);
        b.print("removed all 1's from the list", b);
        Assert.assertEquals(6, b.size());

        b.remove(3);
        b.print("removed all 3's from the list", b);
        Assert.assertEquals(4, b.size());

        b.remove(4);
        b.print("removed all 4's from the list", b);
        Assert.assertEquals(2, b.size());

        b.remove(6);
        b.print("removed all 6's from the list. Now the list is empty", b);
        Assert.assertEquals(0, b.size());
    }


    /**
     * Test function to reverse the linked list
     */
    public void testReverse() {
        MyLinked b = new MyLinked();
        b.reverse();
        b.print("reverse empty", b);

        b.add(1);
        b.add(2);
        b.print("list", b);
        b.reverse();
        b.print("reverse two", b);

        for (double i = 3; i <= 7; i++) {
            b.add(i);
            b.add(i);
        }
        b.print("bigger List : ", b);
        b.reverse();
        b.print("reversed List : ", b);

    }
}
