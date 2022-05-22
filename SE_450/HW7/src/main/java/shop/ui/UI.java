package shop.ui;

public interface UI {
  public void processMenu(IUIMenu menu);
  public String[] processForm(IUIForm form);
  public void displayMessage(String message);
  public void displayError(String message);
}
