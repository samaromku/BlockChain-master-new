package ru.savchenko.andrey.blockchain.services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.savchenko.andrey.blockchain.R;
import ru.savchenko.andrey.blockchain.activities.MainActivity;
import ru.savchenko.andrey.blockchain.entities.USD;
import ru.savchenko.andrey.blockchain.network.RequestManager;
import ru.savchenko.andrey.blockchain.repositories.USDRepository;
import ru.savchenko.andrey.blockchain.storage.Utils;

import static ru.savchenko.andrey.blockchain.storage.Const.USD_ID;


/**
 * Created by Andrey on 12.10.2017.
 */

public class UpdateExchangeService extends IntentService{
    public static final String TAG = "UpdateExchangeService";

    public UpdateExchangeService() {
        super(TAG);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static Intent newIntent(Context context){
        return new Intent(context, UpdateExchangeService.class)
                .addFlags(Intent.FLAG_RECEIVER_FOREGROUND);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Observable.interval(15, TimeUnit.MINUTES)
                .flatMap(aLong -> RequestManager.getRetrofitService().getExchange())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe( exchange -> {
                    int usdId = new USDRepository().writeIdDbReturnInteger(exchange.getUSD());
                    sendNotify(exchange.getUSD(),usdId);
                }, Throwable::printStackTrace);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotify(USD usd, int usdId){
        Intent intent = new Intent(this, MainActivity.class).putExtra(USD_ID, usdId);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        String text = "Закупочная " + usd.getBuy() + "$ 15 мин назад: " + usd.get5m();

        Log.i(TAG, text);
        String title = Utils.getBestAndWorstString(usd.getLast());

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                //.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_monetization)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setVibrate(new long[] { 1000, 1000})
                .setLights(Color.WHITE, 3000, 3000)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}
