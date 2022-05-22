package shop.main;

import org.junit.Test;
import shop.command.RerunnableCommand;
import shop.command.UndoableCommand;
import shop.data.Data;
import shop.data.Inventory;
import shop.data.Video;

import static org.junit.Assert.*;

public class MyTest {
    public MyTest() {
        super();
    }

    /**
     * Unit test to test Undo and Redo
     */
    @Test
    public void test1() {
        final Inventory inventory = Data.newInventory();
        final Video v1 = Data.newVideo("T1", 2010, "D1");
        final Video v2 = Data.newVideo("T2", 2022, "D2");
        final RerunnableCommand UNDO = Data.newUndoCmd(inventory);
        final RerunnableCommand REDO = Data.newRedoCmd(inventory);

        UndoableCommand c = Data.newAddCmd(inventory, v1, 5);
        assertTrue(c.run());
        assertEquals(1, inventory.size());
        assertTrue(!c.run());     // cannot run an undoable command twice
        assertTrue(!Data.newAddCmd(inventory, null, 3).run()); // can't add null!
        assertTrue(Data.newAddCmd(inventory, v2, 2).run());

        assertEquals(2, inventory.size());
        assertTrue(UNDO.run());
        assertEquals(1, inventory.size());
        assertTrue(UNDO.run());
        assertEquals(0, inventory.size());
        assertTrue(!UNDO.run());   // nothing to undo
        assertEquals(0, inventory.size());

        assertTrue(REDO.run());
        assertEquals(1, inventory.size());
        assertTrue(REDO.run());
        assertEquals(2, inventory.size());
        assertTrue(!REDO.run());   // nothing to redo
        assertEquals(2, inventory.size());

        assertTrue(Data.newAddCmd(inventory, v1, -5).run());   // delete!
        assertEquals(1, inventory.size());
        assertTrue(UNDO.run());
        assertEquals(2, inventory.size());

        assertTrue(Data.newOutCmd(inventory, v2).run());
        assertTrue(Data.newOutCmd(inventory, v2).run());
        assertEquals("T2 (2022) : D2 [2,2,2]", inventory.get(v2).toString());
        assertTrue(UNDO.run());
        assertEquals("T2 (2022) : D2 [2,1,1]", inventory.get(v2).toString());
        assertTrue(UNDO.run());
        assertEquals("T2 (2022) : D2 [2,0,0]", inventory.get(v2).toString());
        assertTrue(REDO.run());
        assertEquals("T2 (2022) : D2 [2,1,1]", inventory.get(v2).toString());

        assertTrue(Data.newOutCmd(inventory, v1).run());
        assertEquals("T1 (2010) : D1 [5,1,1]", inventory.get(v1).toString());
        assertTrue(Data.newInCmd(inventory, v1).run());
        assertEquals("T1 (2010) : D1 [5,0,1]", inventory.get(v1).toString());
        assertTrue(UNDO.run());
        assertEquals("T1 (2010) : D1 [5,1,1]", inventory.get(v1).toString());

        assertTrue(Data.newClearCmd(inventory).run());
        assertEquals(0, inventory.size());
        assertTrue(UNDO.run());
        assertEquals(2, inventory.size());


    }

}
