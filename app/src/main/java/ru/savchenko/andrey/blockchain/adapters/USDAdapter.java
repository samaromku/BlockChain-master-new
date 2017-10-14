package ru.savchenko.andrey.blockchain.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.text.SimpleDateFormat;

import butterknife.BindView;
import butterknife.ButterKnife;
import ru.savchenko.andrey.blockchain.R;
import ru.savchenko.andrey.blockchain.base.BaseViewHolder;
import ru.savchenko.andrey.blockchain.entities.USD;
import ru.savchenko.andrey.blockchain.interfaces.OnItemClickListener;
import ru.savchenko.andrey.blockchain.storage.Utils;

import static ru.savchenko.andrey.blockchain.storage.Const.BAD;
import static ru.savchenko.andrey.blockchain.storage.Const.BEST;
import static ru.savchenko.andrey.blockchain.storage.Const.GOOD;
import static ru.savchenko.andrey.blockchain.storage.Const.NORMAL;
import static ru.savchenko.andrey.blockchain.storage.Const.TERRIBLE;
import static ru.savchenko.andrey.blockchain.storage.Const.WORST;

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

            switch (Utils.getProfit(usd.getLast())){
                case BEST:
                    llExchange.setBackgroundResource(R.drawable.gradient_five);
                    break;
                case GOOD:
                    llExchange.setBackgroundResource(R.drawable.gradient_four);
                    break;
                case NORMAL:
                    llExchange.setBackgroundResource(R.drawable.gradient_three);
                    break;
                case BAD:
                    llExchange.setBackgroundResource(R.drawable.gradient_two);
                    break;
                case WORST:
                    llExchange.setBackgroundResource(R.drawable.gradient_one);
                    break;
                case TERRIBLE:
                    llExchange.setBackgroundResource(R.drawable.gradient_one);
                    break;
            }
        }
    }
}
