package com.laitianliang.myapplication;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by 頼天亮 on 6/12/2016.
 */
public class WeChatFragment extends Fragment {
    private View mView ;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if(mView != null){
            return mView ;
        }
        mView = inflater.inflate(R.layout.view_game,null) ;
        return mView;
    }
}
