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
import ru.savchenko.andrey.blockchain.network.RequestManager;
import ru.savchenko.andrey.blockchain.repositories.USDRepository;


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
                    String text = "Закупочная " + exchange.getUSD().getBuy() + "$ 15 мин назад: " + exchange.getUSD().get5m();
                    Log.i(TAG, text);
                    sendNotify(text);
                    new USDRepository().writeIdDb(exchange.getUSD());
                }, Throwable::printStackTrace);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void sendNotify(String message){
        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                //.setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND)
                .setPriority(Notification.PRIORITY_MAX)
                .setSmallIcon(R.drawable.ic_cake)
                .setContentTitle("BlockChain")
                .setContentText(message)
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
