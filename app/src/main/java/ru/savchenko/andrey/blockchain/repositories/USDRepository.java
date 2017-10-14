package ru.savchenko.andrey.blockchain.repositories;

import android.support.v7.widget.RecyclerView;

import java.util.Date;
import java.util.List;

import io.realm.Realm;
import ru.savchenko.andrey.blockchain.base.BaseAdapter;
import ru.savchenko.andrey.blockchain.base.BaseRepository;
import ru.savchenko.andrey.blockchain.entities.USD;

/**
 * Created by Andrey on 12.10.2017.
 */

public class USDRepository {
    private Realm realmInstance() {
        return Realm.getDefaultInstance();
    }

    public void writeIdDb(USD usd){
        BaseRepository baseRepository = new BaseRepository<>(USD.class);
        usd.setId(baseRepository.getMaxIdPlusOne());
        usd.setDate(new Date());
        baseRepository.addItem(usd);
    }

    public int writeIdDbReturnInteger(USD usd){
        BaseRepository baseRepository = new BaseRepository<>(USD.class);
        int maxId = baseRepository.getMaxIdPlusOne();
        usd.setId(maxId);
        usd.setDate(new Date());
        baseRepository.addItem(usd);
        return maxId;
    }

    public void addChangeListener(BaseAdapter adapter, RecyclerView rvExchange){
        realmInstance().addChangeListener(realm -> {
            adapter.notifyDataSetChanged();
            rvExchange.smoothScrollToPosition(adapter.getItemCount());
        });
        realmInstance().close();
    }

    public List<USD>getUSDListByDate(Date start, Date end){
        List<USD>usds = realmInstance().where(USD.class)
                .between("date", start, end)
                .findAll();
        realmInstance().close();
        return usds;
    }

    public Integer getMaxLast(){
        int maxLast = realmInstance().where(USD.class)
                .max("mLast")
                .intValue();
        realmInstance().close();
        return maxLast;
    }

    public Integer getMintLast(){
        int minLast = realmInstance().where(USD.class)
                .min("mLast")
                .intValue();
        realmInstance().close();
        return minLast;
    }
}
