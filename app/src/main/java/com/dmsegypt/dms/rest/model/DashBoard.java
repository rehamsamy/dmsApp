package com.dmsegypt.dms.rest.model;

import com.dmsegypt.dms.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amr on 15/02/2018.
 */

public class DashBoard {
   private String title;
    private int drawable;

    public DashBoard(String title, int drawable) {
        this.title = title;
        this.drawable = drawable;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public static List<DashBoard>getDashboardList(){
        List<DashBoard>dashBoards=new ArrayList<>();
        dashBoards.add(new DashBoard("View Accounts", R.drawable.view_accounts_icon));
        dashBoards.add(new DashBoard("Send Notification", R.drawable.ring));
        dashBoards.add(new DashBoard("App Reviews",R.drawable.ic_comment_black_24dp));
    return dashBoards;
    }

}
