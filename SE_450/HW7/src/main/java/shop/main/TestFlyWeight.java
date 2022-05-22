package shop.main;

import junit.framework.TestCase;
import shop.data.Data;
import shop.data.Inventory;
import shop.data.Video;

public class TestFlyWeight extends TestCase {

    public void test1() {
        final Inventory inventory = Data.newInventory();
        final Video v1 = Data.newVideo("T1", 2000, "D1");
        final Video v2 = Data.newVideo("T1", 2000, "D1");
        assertTrue(v1==v2);
    }
}
