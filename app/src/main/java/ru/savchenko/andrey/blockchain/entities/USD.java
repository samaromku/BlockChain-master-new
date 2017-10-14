
package ru.savchenko.andrey.blockchain.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

import javax.annotation.Generated;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

@Generated("net.hexar.json2pojo")
@SuppressWarnings("unused")
public class USD extends RealmObject{
    @PrimaryKey
    private int id;
    @SerializedName("15m")
    private Double m5m;
    @SerializedName("buy")
    private Double mBuy;
    @SerializedName("last")
    private Double mLast;
    @SerializedName("sell")
    private Double mSell;
    @SerializedName("symbol")
    private String mSymbol;
    private Date date;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Double get5m() {
        return m5m;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getBuy() {
        return mBuy;
    }

    public void setBuy(Double buy) {
        mBuy = buy;
    }

    public Double getLast() {
        return mLast;
    }

    public void setLast(Double last) {
        mLast = last;
    }

    public Double getSell() {
        return mSell;
    }

    public void setSell(Double sell) {
        mSell = sell;
    }

    public String getSymbol() {
        return mSymbol;
    }

    public void setSymbol(String symbol) {
        mSymbol = symbol;
    }

}
