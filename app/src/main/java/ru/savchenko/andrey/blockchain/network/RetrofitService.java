package ru.savchenko.andrey.blockchain.network;

import io.reactivex.Observable;
import retrofit2.http.GET;
import ru.savchenko.andrey.blockchain.entities.Exchange;

/**
 * Created by Andrey on 12.09.2017.
 */

public interface RetrofitService {

    @GET("/ru/ticker")
    Observable<Exchange> getExchange();

}
