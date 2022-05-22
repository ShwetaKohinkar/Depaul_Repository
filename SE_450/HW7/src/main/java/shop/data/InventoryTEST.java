package shop.data;

import junit.framework.Assert;
import junit.framework.TestCase;
import org.junit.Test;
import shop.command.Command;
import shop.command.UndoableCommand;
import shop.command.CommandHistory;
import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

// TODO: complete the tests
public class InventoryTEST extends TestCase {
  public InventoryTEST(String name) {
    super(name);
  }

  private Inventory _inventory = Data.newInventory();

  final Video video1 = Data.newVideo("T1", 2010, "D1");
  final Video video2 = Data.newVideo("T2", 2022, "D2");

  /**
   * Unit test for inventory size
   */
  @Test
  public void testSize() {
    assert (true);
    Command cmdObj;
    assertEquals(0, _inventory.size());

    cmdObj = Data.newAddCmd(_inventory, video1, 1);
    assertTrue(cmdObj.run());
    assertEquals(1, _inventory.size());

    cmdObj = Data.newAddCmd(_inventory, video2, 1);
    assertTrue(cmdObj.run());
    assertEquals(2, _inventory.size());

    cmdObj = Data.newAddCmd(_inventory, video2, -1);
    assertTrue(cmdObj.run());
    assertEquals(1, _inventory.size());

    cmdObj = Data.newAddCmd(_inventory, video1, -1);
    assertTrue(cmdObj.run());
    assertEquals(0, _inventory.size());

    cmdObj = Data.newClearCmd(_inventory);
    assertTrue(cmdObj.run());
    assertEquals(0, _inventory.size());

  }

  /**
   * Unit test for NumOwned
   */
  @Test
  public void testAddNumOwned() {

    // TODO: complete this test
    assertEquals(0, _inventory.size());
    Command cmdObj;

    cmdObj = Data.newAddCmd(_inventory, video1, 1);
    assertTrue(cmdObj.run());
    assertEquals(1, _inventory.get(video1).numOwned());

    cmdObj = Data.newAddCmd(_inventory, video1, 2);
    assertTrue(cmdObj.run());
    assertEquals(3, _inventory.get(video1).numOwned());

    cmdObj = Data.newAddCmd(_inventory, video1, -1);
    assertTrue(cmdObj.run());
    assertEquals(2, _inventory.get(video1).numOwned());

    assertEquals("T1 (2010) : D1 [2,0,0]", _inventory.get(video1).toString());

    cmdObj = Data.newClearCmd(_inventory);
    assertTrue(cmdObj.run());
    assertEquals(0, _inventory.size());

  }


  /**
   * Unit test for CheckIn and CheckOut
   */
  @Test
  public void testCheckOutCheckIn() {
    // TODO

    assertEquals(0, _inventory.size());
    Command cmdObj;

    cmdObj = Data.newAddCmd(_inventory, video1, 1);
    assertTrue(cmdObj.run());
    assertEquals(1, _inventory.get(video1).numOwned());

    cmdObj = Data.newOutCmd(_inventory, video1);
    assertTrue(cmdObj.run());
    assertEquals(1, _inventory.get(video1).numOut());

    cmdObj = Data.newInCmd(_inventory, video1);
    assertTrue(cmdObj.run());
    assertEquals(0, _inventory.get(video1).numOut());

    cmdObj = Data.newClearCmd(_inventory);
    assertTrue(cmdObj.run());
  }

  /**
   * Unit test for Clearing Inventory
   */
  @Test
  public void testClear() {
    // TODO
    assertEquals(0, _inventory.size());
    Command cmdObj;

    cmdObj = Data.newAddCmd(_inventory, video2, 1);
    assertTrue(cmdObj.run());

    assertEquals("T2 (2022) : D2 [1,0,0]", _inventory.get(video2).toString());

    assertEquals(1, _inventory.size());
    cmdObj = Data.newClearCmd(_inventory);
    assertTrue(cmdObj.run());
  }

  /**
   * Unit test for Equals function
   */
  @Test
  public void testGet() {
    // TODO

    assertEquals(0, _inventory.size());
    Command cmdObj;

    cmdObj = Data.newAddCmd(_inventory, video1, 1);
    assertTrue(cmdObj.run());
    assertEquals("T1 (2010) : D1 [1,0,0]", _inventory.get(video1).toString());
    assertEquals(1, _inventory.size());

    cmdObj = Data.newClearCmd(_inventory);
    assertTrue(cmdObj.run());
  }

  /**
   * Unit test for Iterator
   */
  @Test
  public void testIterator1() {

    HashSet<Video> hashList = new HashSet<>();

    hashList.add(video1);
    hashList.add(video2);
    Command cmdObj;
    cmdObj = Data.newAddCmd(_inventory, video1, 2);
    assertTrue(cmdObj.run());

    cmdObj = Data.newAddCmd(_inventory, video2, 4);
    assertTrue(cmdObj.run());

    Iterator<Record> itr = _inventory.iterator();

    while (itr.hasNext()) {
      Record rec = itr.next();
      if (hashList.contains(rec.video())) {
        hashList.remove(rec.video());
      }
    }
    assertEquals(0, hashList.size());
  }

  /**
   * Unit test for Iterator implementing Comparator
   */
  @Test
  public void testIterator2() {
    List<Video> list = new ArrayList<>();

    list.add(video1);
    list.add(video2);

    Command cmdObj;
    cmdObj = Data.newAddCmd(_inventory, video1, 5);
    assertTrue(cmdObj.run());

    cmdObj = Data.newAddCmd(_inventory, video2, 10);
    assertTrue(cmdObj.run());

    NumOwnedComparator nb = new NumOwnedComparator();
    Iterator<Record> itr = _inventory.iterator(nb);



    while (itr.hasNext()) {
      Video rec = itr.next().video();
      assertTrue(0 == (list.get(0).compareTo(rec)));
      list.remove(rec);
    }
    assertEquals(0, list.size());

  }
}
