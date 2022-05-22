package shop.ui;

/**
 * @see IUIMenu
 */
final class UIMenu extends CommonFormMenu implements IUIMenu{

  private final GenericPair<String, UIMenuAction>[] _genericPair;
  UIMenu(String heading, GenericPair<String, UIMenuAction>[] _form) {

    super(heading);
    _genericPair = _form;
  }

  public void runAction(int i) {

    _genericPair[i].getTestAction().run();
  }

  public int size() {
    return _genericPair.length;
  }


  public String getPrompt(int i) {
    return _genericPair[i].getPrompt();
  }

}
