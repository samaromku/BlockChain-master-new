package ru.savchenko.andrey.blockchain.base;

import android.support.annotation.StringRes;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.Toolbar;

import com.arellomobile.mvp.MvpAppCompatActivity;


/**
 * Created by Andrey on 25.09.2017.
 */

public class BaseActivity extends MvpAppCompatActivity{
    protected Toolbar toolbar;
    protected FloatingActionButton fab;

//    protected void initToolbar(@StringRes int title){
//        toolbar = (Toolbar) findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        if(getSupportActionBar()!=null)
//            getSupportActionBar().setTitle(title);
//        toolbar.setTitleTextColor(Color.WHITE);
//    }

    protected void changeToolbarTitle(@StringRes int title){
        if(getSupportActionBar()!=null)
            getSupportActionBar().setTitle(title);
    }

//    protected void initBackButton(){
//        if (getSupportActionBar()!=null)
//            getSupportActionBar().setHomeAsUpIndicator(getResources().getDrawable(R.drawable.ic_arrow_back));
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
