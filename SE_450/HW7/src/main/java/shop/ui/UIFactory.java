package shop.ui;


/**
 *  Factory Class for UI
 */
public class UIFactory {
    private UIFactory() {
    }

    static public UI popUI() {
        return new PopupUI();
    }

    static public UI textUI() {
        return new TextUI();
    }

    static public UIBuilder builderUI() {
        return new CommonUIBuilder();
    }
}
