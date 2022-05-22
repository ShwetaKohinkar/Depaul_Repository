package shop.ui;

/**
 * @see IUIForm
 */
final class UIForm extends CommonFormMenu implements IUIForm {

    final GenericPair<String, UIFormTest>[] _genericPair;
    UIForm(String heading, GenericPair<String, UIFormTest>[] _form) {
        super(heading);
        _genericPair = _form;

    }

    public boolean checkInput(int i, String input) {

        if (null == _genericPair[i])
            return false;
        return _genericPair[i].getTestAction().run(input);
    }

    public int size() {
        return _genericPair.length;
    }


    public String getPrompt(int i) {
        return _genericPair[i].getPrompt();
    }

}
