package shop.ui;

/**
Common Interface for UIFormBuilder and UIMenuAction
 */
public interface UIBuilder {

    void add(String _str, UIFormTest _test);

    void add(String prompt, UIMenuAction test);

    UIForm toUIForm(String heading);

    UIMenu toUIMenu(String heading);
}
