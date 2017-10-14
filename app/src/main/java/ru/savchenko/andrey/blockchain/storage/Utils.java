package ru.savchenko.andrey.blockchain.storage;

import java.util.ArrayList;
import java.util.List;

import ru.savchenko.andrey.blockchain.base.BaseRepository;
import ru.savchenko.andrey.blockchain.entities.USD;

import static ru.savchenko.andrey.blockchain.storage.Const.BEST;
import static ru.savchenko.andrey.blockchain.storage.Const.BUY;
import static ru.savchenko.andrey.blockchain.storage.Const.SELL;
import static ru.savchenko.andrey.blockchain.storage.Const.TERRIBLE;
import static ru.savchenko.andrey.blockchain.storage.Const.WAIT;
import static ru.savchenko.andrey.blockchain.storage.Const.WORST;

/**
 * Created by savchenko on 14.10.17.
 */

public class Utils {

    private static double getMiddleValue(){
        List<Double> doublesUSD = new ArrayList<>();
        for(USD usd: new BaseRepository<>(USD.class).getAll()){
            doublesUSD.add(usd.getLast());
        }
        double allValues = 0;
        for (int i = 0; i < doublesUSD.size(); i++) {
            allValues = allValues + doublesUSD.get(i);
        }
        return allValues/doublesUSD.size();
    }

    public static int getProfit(double last){
        if(last >=getMiddleValue()+10){
            return 10;
        }if(last >=getMiddleValue()+5){
            return 5;
        }if(last >=getMiddleValue()){
            return 0;
        }if(last <=getMiddleValue()){
            return -5;
        }if(last <=getMiddleValue()-10){
            return -10;
        }
        return -20;
    }

    public static String getBestAndWorstString(Double last){
        if(Utils.getProfit(last)==BEST){
            return SELL;
        }else if(Utils.getProfit(last)==WORST || Utils.getProfit(last)==TERRIBLE){
            return BUY;
        }
        return WAIT;
    }

}
