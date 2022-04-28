package hw1;

import junit.framework.Assert;
import junit.framework.TestCase;

// TODO: complete the tests
public class InventoryTest extends TestCase
{
    public InventoryTest(String name)
    {
        super(name);
    }

    InventorySet _inv = new InventorySet();
    final VideoObj video1 = new VideoObj( "T1", 2022, "D1" );
    final VideoObj video2 = new VideoObj( "T2", 2022, "D2" );

    /**
     * Unit test for inventory size
     */
    public void testSize()
    {
        assert(true);
        Assert.assertEquals( 0, _inv.size() );
        _inv.addNumOwned(video1,  1);
        Assert.assertEquals( 1, _inv.size() );
        _inv.addNumOwned(video2,  1);
        Assert.assertEquals( 2, _inv.size() );
        _inv.addNumOwned(video2, -1);
        Assert.assertEquals( 1, _inv.size() );
        _inv.addNumOwned(video1, -1);
        Assert.assertEquals( 0, _inv.size() );

        try
        {
            _inv.addNumOwned(video1, -2);
        } catch ( IllegalArgumentException e ) {}
        Assert.assertEquals( 0, _inv.size() );
    }

    /**
     * Unit test for NumOwned
     */
    public void testAddNumOwned()
    {
        // TODO: complete this test
        Assert.assertEquals( 0, _inv.size() );
        _inv.addNumOwned(video1, 1);
        Assert.assertEquals(1, _inv.get(video1).numOwned);
        _inv.addNumOwned(video1, 2);
        Assert.assertEquals(3, _inv.get(video1).numOwned);
        _inv.addNumOwned(video1, -1);
        Assert.assertEquals(2, _inv.get(video1).numOwned);
        _inv.clear();
    }

    /**
     * Unit test for CheckIn and CheckOut
     */
    public void testCheckOutCheckIn()
    {
        // TODO
        Assert.assertEquals( 0, _inv.size());
        _inv.addNumOwned(video1, 1);
        _inv.checkOut(video1);
        Assert.assertEquals(1, _inv.get(video1).numOut );
        _inv.checkIn(video1);
        Assert.assertEquals( 0, _inv.get(video1).numOut );
        _inv.clear();
    }

    /**
     * Unit test for Clearing Inventory
     */
    public void testClear()
    {
        // TODO
        Assert.assertEquals( 0, _inv.size() );
        _inv.addNumOwned(video1, 1);     Assert.assertEquals(video1, _inv.get(video1).video );
        Assert.assertEquals(1, _inv.size());
        _inv.clear();
    }

    /**
     * Unit test for Equals function
     */
    public void testGet()
    {
        // TODO
        Assert.assertEquals( 0, _inv.size() );
        _inv.addNumOwned(video1, 1);
        Assert.assertEquals( video1, _inv.get(video1).video );
        _inv.clear();
    }
    /**
     * Unit test for Collection
     */
    public void testToCollection()
    {
        // Be sure to test that changing records in the returned
        // collection does not change the original records in the
        // inventory.  ToCollection should return a COPY of the records,
        // not the records themselves.
        // TODO
    }
}
