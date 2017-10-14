package ru.savchenko.andrey.blockchain.activities;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.reactivex.Maybe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.savchenko.andrey.blockchain.R;
import ru.savchenko.andrey.blockchain.adapters.USDAdapter;
import ru.savchenko.andrey.blockchain.base.BaseActivity;
import ru.savchenko.andrey.blockchain.base.BaseRepository;
import ru.savchenko.andrey.blockchain.dialogs.BuyOrSellDialog;
import ru.savchenko.andrey.blockchain.dialogs.DateDialog;
import ru.savchenko.andrey.blockchain.entities.USD;
import ru.savchenko.andrey.blockchain.interfaces.OnItemClickListener;
import ru.savchenko.andrey.blockchain.interfaces.SetDataFromDialog;
import ru.savchenko.andrey.blockchain.network.RequestManager;
import ru.savchenko.andrey.blockchain.repositories.USDRepository;

import static ru.savchenko.andrey.blockchain.storage.Const.USD_ID;

public class MainActivity extends BaseActivity implements OnItemClickListener, SetDataFromDialog {
    @BindView(R.id.constraintMain)
    LinearLayout constraintMain;
    @BindView(R.id.rvExchange)
    RecyclerView rvExchange;
    @BindView(R.id.srlRefresher)
    SwipeRefreshLayout srlRefresher;
    USDAdapter adapter;
    private List<USD> usds;

    public static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initRv();
        int usdId = getIntent().getIntExtra(USD_ID, 0);

        if (usdId != 0) {
            BuyOrSellDialog buyOrSellDialog = new BuyOrSellDialog();
            buyOrSellDialog.setUsd(new BaseRepository<>(USD.class).getItemById(usdId));
            buyOrSellDialog.show(getSupportFragmentManager(), "buyOrSell");
        }

        srlRefresher.setOnRefreshListener(() ->
                RequestManager.getRetrofitService().getExchange()
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(exchange -> {
                            new USDRepository().writeIdDb(exchange.getUSD());
                            adapter.notifyDataSetChanged();
                            srlRefresher.setRefreshing(false);
                        }, Throwable::printStackTrace));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_date:
                DateDialog dateDialog = new DateDialog();
                dateDialog.setSetDataFromDialog(this);
                dateDialog.show(getSupportFragmentManager(), "date");
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initRv() {
        rvExchange.setLayoutManager(new LinearLayoutManager(this));
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(rvExchange.getContext(), DividerItemDecoration.HORIZONTAL);
        rvExchange.addItemDecoration(dividerItemDecoration);
        adapter = new USDAdapter();
        adapter.setClickListener(this);
        usds = new BaseRepository<>(USD.class).getAll();
        new USDRepository().addChangeListener(adapter, rvExchange);
        adapter.setDataList(usds);
        rvExchange.setAdapter(adapter);
    }

    @Override
    public void onClick(int position) {
        BuyOrSellDialog buyOrSellDialog = new BuyOrSellDialog();
        buyOrSellDialog.setUsd(new BaseRepository<>(USD.class).getItemById(usds.get(position).getId()));
        buyOrSellDialog.show(getSupportFragmentManager(), "buyOrSell");
    }

    @Override
    public void setData(int year, int month, int day) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day, 0, 0);

        Date start = calendar.getTime();
        calendar.set(Calendar.DAY_OF_MONTH, day + 1);
        Date end = calendar.getTime();

        Maybe.fromCallable(() -> new USDRepository().getUSDListByDate(start, end))
                .subscribe(usds1 -> {
                    adapter.setDataList(usds1);
                    adapter.notifyDataSetChanged();
                });
    }
}
