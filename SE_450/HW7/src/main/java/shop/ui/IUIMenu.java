package shop.ui;

public interface IUIMenu extends UIFormMenu {
    public void runAction(int i);
    public int size();
    public String getPrompt(int i);
}
