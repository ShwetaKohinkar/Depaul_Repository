package shop.ui;

import java.util.ArrayList;
import java.util.List;

/**
 * Common Builder Class for Form Builder and Menu Builder
 */
final class CommonUIBuilder implements UIBuilder {

    private final List<GenericPair> _menu = new ArrayList<GenericPair>();

/*
   Function to add
 */
    public void add(String prompt, UIFormTest test) {
        if (null == test)
            throw new IllegalArgumentException();
        _menu.add(new GenericPair(prompt, test));
    }

    public void add(String prompt, UIMenuAction action) {
        if (null == action)
            throw new IllegalArgumentException();
        _menu.add(new GenericPair(prompt, action));
    }

    public UIForm toUIForm(String heading) {
        GenericPair[] array = getArray(heading);
        return new UIForm(heading, array);
    }

    public UIMenu toUIMenu(String heading) {
        GenericPair[] array = getArray(heading);
        return new UIMenu(heading, array);
    }

    private GenericPair[] getArray(String heading) {
        GenericPair[] array = new GenericPair[_menu.size()];
        for (int i = 0; i < _menu.size(); i++)
            array[i] = _menu.get(i);
       return array;
    }
}
