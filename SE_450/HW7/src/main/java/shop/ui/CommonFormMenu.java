package shop.ui;

import static java.awt.SystemColor.menu;

/**
 * Common Class for Form and Menu
 */

abstract class CommonFormMenu implements UIFormMenu{

    private final String _heading;

    CommonFormMenu(String heading) {
        _heading = heading;
    }

    public String getHeading() {
        return _heading;
    }

}



