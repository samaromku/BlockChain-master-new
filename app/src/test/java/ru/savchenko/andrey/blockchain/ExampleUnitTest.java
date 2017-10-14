package ru.savchenko.andrey.blockchain;

import org.junit.Test;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        Observable.timer(1000, TimeUnit.SECONDS)
                .doOnNext(aLong -> System.out.println("next"))
                .subscribe(aLong -> System.out.println(aLong), Throwable::printStackTrace, () -> System.out.println("sldflds"));
    }
}