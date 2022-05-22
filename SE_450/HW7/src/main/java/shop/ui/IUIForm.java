package shop.ui;

public interface IUIForm extends UIFormMenu{
    public boolean checkInput(int i, String input);
    public String getPrompt(int i);
    public int size();
}
