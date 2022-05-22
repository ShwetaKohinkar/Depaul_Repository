package shop.data;

import shop.command.UndoableCommand;
import java.util.Map;

/**
 * Implementation of command to clear the inventory.
 * @see Data
 */
final class CmdClear implements UndoableCommand {
  private InventorySet _inventory;
  private Map<Video,Record> _oldvalue;
  CmdClear(InventorySet inventory) {
    _inventory = inventory;
  }

  /**
   * Function to execute CmdClear
   */
  public boolean run() {
    if (_oldvalue != null) {
      return false;
    }
    _oldvalue = _inventory.clear();
    _inventory.getHistory().add(this);
    return true;
  }

  /**
   * Function to Undo Command
   */
  public void undo() {
    _inventory.replaceMap(_oldvalue);
  }

  /**
   * Function to Redo Command
   */
  public void redo() {
    _inventory.clear();
  }
}
