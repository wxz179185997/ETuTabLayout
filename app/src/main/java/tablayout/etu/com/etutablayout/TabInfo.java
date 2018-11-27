package tablayout.etu.com.etutablayout;

import com.etu.tablayout.listener.CommonTabEnity;

public class TabInfo implements CommonTabEnity {

    public String mTitle;
    public int mSelectedIcon;
    public int mUnSelectedIcon;


    public TabInfo(String title, int selectedIcon, int unSelectedIcon) {
        this.mTitle = title;
        this.mSelectedIcon = selectedIcon;
        this.mUnSelectedIcon = unSelectedIcon;


    }

    @Override
    public String getTabTitle() {
        return mTitle;
    }

    @Override
    public int getSeletedIcon() {
        return mSelectedIcon;
    }

    @Override
    public int getUnSelectedIcon() {
        return mUnSelectedIcon;
    }
}
