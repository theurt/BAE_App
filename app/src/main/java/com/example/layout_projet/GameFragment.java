package com.example.layout_projet;

import androidx.fragment.app.Fragment;

public class GameFragment extends Fragment {

    private boolean win;
    private boolean lose;

    public boolean getWin(){

        return this.win;
    }
    public boolean getLose(){

        return this.lose;
    }

    public void setWin(boolean win){
        this.win = win;
    }

    public void setLose(boolean lose){
        this.lose = lose;
    }

}

