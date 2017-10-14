package ru.savchenko.andrey.blockchain;

import android.app.Application;

import io.realm.Realm;
import ru.savchenko.andrey.blockchain.network.RequestManager;
import ru.savchenko.andrey.blockchain.services.UpdateExchangeService;

/**
 * Created by Andrey on 12.10.2017.
 */

public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RequestManager.init();
        Realm.init(this);
        startService(UpdateExchangeService.newIntent(this));
    }
}
