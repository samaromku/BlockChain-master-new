package ru.savchenko.andrey.blockchain.base;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import ru.savchenko.andrey.blockchain.interfaces.OnItemClickListener;


/**
 * Created by Andrey on 25.09.2017.
 */

public class BaseViewHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener{
    private OnItemClickListener clickListener;

    public void bind(T t, OnItemClickListener clickListener){
        this.clickListener = clickListener;
    }

    public BaseViewHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        clickListener.onClick(getAdapterPosition());
    }
}
