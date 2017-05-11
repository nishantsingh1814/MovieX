package com.eventx.moviex.MovieModels;

/**
 * Created by Nishant on 4/23/2017.
 */

public class Navigation {
    int icon;
    String menuItem;

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getMenuItem() {
        return menuItem;
    }

    public void setMenuItem(String menuItem) {
        this.menuItem = menuItem;
    }

    public Navigation(int icon, String menuItem) {
        this.icon = icon;
        this.menuItem = menuItem;

    }
}
