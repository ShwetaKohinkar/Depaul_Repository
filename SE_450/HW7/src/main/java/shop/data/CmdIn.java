package shop.data;

import shop.command.UndoableCommand;

/**
 * Implementation of command to check in a video.
 * @see Data
 */
final class CmdIn implements UndoableCommand {
  private InventorySet _inventory;
  private Record _oldvalue;
  private Video _video;
  CmdIn(InventorySet inventory, Video video) {
    _inventory = inventory;
    _video = video;
  }

  /**
   * Function to execute CmdIn
   */
  public boolean run() {
    // TODO
    try {

      _oldvalue = _inventory.checkIn(_video);

      _inventory.getHistory().add(this);

      return true;
    } catch (IllegalArgumentException e) {
      //System.out.println("IAE");
      return false;
    } catch (ClassCastException e) {
      //System.out.println("CCE");
      return false;
    }
  }

  /**
   * Function to Undo Command
   */
  public void undo() {
    // TODO
    _inventory.replaceEntry(_video,_oldvalue);

  }

  /**
   * Function to Redo Command
   */
  public void redo() {
    // TODO
    _inventory.checkIn(_video);

  }
}
