package ru.savchenko.andrey.blockchain.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.savchenko.andrey.blockchain.R;
import ru.savchenko.andrey.blockchain.base.BaseRepository;
import ru.savchenko.andrey.blockchain.base.BaseViewHolder;
import ru.savchenko.andrey.blockchain.entities.USD;
import ru.savchenko.andrey.blockchain.interfaces.OnItemClickListener;

import static ru.savchenko.andrey.blockchain.activities.MainActivity.TAG;

/**
 * Created by Andrey on 12.10.2017.
 */

public class USDAdapter extends ru.savchenko.andrey.blockchain.base.BaseAdapter<USD> {
    @Override
    public BaseViewHolder<USD> onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_exchange, parent, false);
        return new USDViewHolder(view);
    }

    class USDViewHolder extends BaseViewHolder<USD>{
        @BindView(R.id.tvActualPrice)TextView tvActualPrice;
        @BindView(R.id.tvTime)TextView tvTime;
        @BindView(R.id.llExchange)LinearLayout llExchange;

        public USDViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        @Override
        public void bind(USD usd, OnItemClickListener clickListener) {
            super.bind(usd, clickListener);
            tvActualPrice.setText(String.valueOf(usd.getLast()));
            tvTime.setText(new SimpleDateFormat("yyyy.MM.dd HH:mm").format(usd.getDate()));

            Log.i(TAG, "bind: " + getMiddleValue());
            double y = usd.getLast();
            if(y>=getMiddleValue()+10){
                llExchange.setBackgroundResource(R.drawable.gradient_five);
            }if(y>=getMiddleValue()+5){
                llExchange.setBackgroundResource(R.drawable.gradient_four);
            }if(y>=getMiddleValue()){
                llExchange.setBackgroundResource(R.drawable.gradient_three);
            }if(y<=getMiddleValue()){
                llExchange.setBackgroundResource(R.drawable.gradient_two);
            }if(y<=getMiddleValue()-10){
                llExchange.setBackgroundResource(R.drawable.gradient_one);
            }
        }
    }

    private double getMiddleValue(){
        List<Double>doublesUSD = new ArrayList<>();
        for(USD usd: new BaseRepository<>(USD.class).getAll()){
            doublesUSD.add(usd.getLast());
        }
        double allValues = 0;
        for (int i = 0; i < doublesUSD.size(); i++) {
            allValues = allValues + doublesUSD.get(i);
        }
        return allValues/doublesUSD.size();
    }
}
