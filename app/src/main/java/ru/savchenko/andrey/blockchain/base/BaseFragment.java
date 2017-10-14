package ru.savchenko.andrey.blockchain.base;

import com.arellomobile.mvp.MvpAppCompatFragment;

import ru.savchenko.andrey.blockchain.interfaces.OnChangeTitle;


/**
 * Created by Andrey on 25.09.2017.
 */

public class BaseFragment extends MvpAppCompatFragment{
    protected OnChangeTitle onChangeTitle;

    public void setOnChangeTitle(OnChangeTitle onChangeTitle) {
        this.onChangeTitle = onChangeTitle;
    }
}
