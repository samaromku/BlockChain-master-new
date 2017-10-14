package ru.savchenko.andrey.blockchain.dialogs;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import lecho.lib.hellocharts.formatter.SimpleAxisValueFormatter;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.LineChartView;
import ru.savchenko.andrey.blockchain.R;
import ru.savchenko.andrey.blockchain.base.BaseRepository;
import ru.savchenko.andrey.blockchain.entities.USD;
import ru.savchenko.andrey.blockchain.repositories.USDRepository;
import ru.savchenko.andrey.blockchain.storage.Utils;

/**
 * Created by savchenko on 14.10.17.
 */

public class BuyOrSellDialog extends DialogFragment{
    @BindView(R.id.tvBuyOrSell)TextView tvBuyOrSell;
    @BindView(R.id.rlDiagram)RelativeLayout rlDiagram;
    private USD usd;
    private LineChartView chart;
    private LineChartData data;

    public void setUsd(USD usd) {
        this.usd = usd;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_buy_or_sell, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        tvBuyOrSell.setText(Utils.getBestAndWorstString(usd.getLast()));
        chart = view.findViewById(R.id.chart);
        generateTempoData();
    }

    private void generateTempoData() {
        // I got speed in range (0-50) and height in meters in range(200 - 300). I want this chart to display both
        // information. Differences between speed and height values are large and chart doesn't look good so I need
        // to modify height values to be in range of speed values.

        // The same for displaying Tempo/Height chart.

        float minHeight = new USDRepository().getMaxLast();
        float maxHeight = new USDRepository().getMintLast();
        float tempoRange = 0.2f; // from 0min/km to 15min/km

        float scale = tempoRange / maxHeight;
        float sub = (minHeight * scale) / 2;

        Line line;
        List<PointValue> values;
        List<Line> lines = new ArrayList<Line>();

        // Height line, add it as first line to be drawn in the background.
        values = new ArrayList<>();

        List<USD>usdList = new BaseRepository<>(USD.class).getAll();
        for (int i = 0; i < usdList.size(); i++) {
            float rawHeight = (float) (usdList.get(i).getLast() + 0);
            float normalizedHeight = rawHeight * scale - sub;
            values.add(new PointValue(i, normalizedHeight));
        }

        line = new Line(values);
        line.setColor(Color.RED);
        line.setHasPoints(false);
        line.setFilled(true);
        line.setStrokeWidth(1);
        lines.add(line);

        // Data and axes
        data = new LineChartData(lines);

        // Distance axis(bottom X) with formatter that will ad [km] to values, remember to modify max label charts
        // value.
        Axis distanceAxis = new Axis();
        distanceAxis.setName("Время");
        distanceAxis.setTextColor(ChartUtils.COLOR_ORANGE);
        distanceAxis.setMaxLabelChars(4);
        distanceAxis.setFormatter(new SimpleAxisValueFormatter().setAppendedText("мин".toCharArray()));
        distanceAxis.setHasLines(true);
        distanceAxis.setHasTiltedLabels(true);
        data.setAxisXBottom(distanceAxis);

        // Tempo uses minutes so I can't use auto-generated axis because auto-generation works only for decimal
        // system. So generate custom axis values for example every 15 seconds and set custom labels in format
        // minutes:seconds(00:00), you could do it in formatter but here will be faster.
//        List<AxisValue> axisValues = new ArrayList<AxisValue>();
//        for (float i = 0; i < tempoRange; i += 0.25f) {
//            // I'am translating float to minutes because I don't have data in minutes, if You store some time data
//            // you may skip translation.
//            axisValues.add(new AxisValue(i).setLabel(formatMinutes(tempoRange - i)));
//        }

//        Axis tempoAxis = new Axis(axisValues).setName("Tempo [min/km]").setHasLines(true).setMaxLabelChars(4)
//                .setTextColor(ChartUtils.COLOR_RED);
//        data.setAxisYLeft(tempoAxis);

        // *** Same as in Speed/Height chart.
        // Height axis, this axis need custom formatter that will translate values back to real height values.
        data.setAxisYRight(new Axis().setName("Цена в $").setMaxLabelChars(3).setTextColor(ChartUtils.COLOR_RED)
                .setFormatter(new HeightValueFormatter(scale, sub, 0)));

        // Set data
        chart.setLineChartData(data);

        // Important: adjust viewport, you could skip this step but in this case it will looks better with custom
        // viewport. Set
        // viewport with Y range 0-12;
        Viewport v = chart.getMaximumViewport();
        v.set(v.left, tempoRange, v.right, 0);
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);

    }

    private String formatMinutes(float value) {
        StringBuilder sb = new StringBuilder();

        // translate value to seconds, for example
        int valueInSeconds = (int) (value * 60);
        int minutes = (int) Math.floor(valueInSeconds / 60);
        int seconds = (int) valueInSeconds % 60;

        sb.append(String.valueOf(minutes)).append(':');
        if (seconds < 10) {
            sb.append('0');
        }
        sb.append(String.valueOf(seconds));
        return sb.toString();
    }

    /**
     * Recalculated height values to display on axis. For this example I use auto-generated height axis so I
     * override only formatAutoValue method.
     */
    private static class HeightValueFormatter extends SimpleAxisValueFormatter {

        private float scale;
        private float sub;
        private int decimalDigits;

        public HeightValueFormatter(float scale, float sub, int decimalDigits) {
            this.scale = scale;
            this.sub = sub;
            this.decimalDigits = decimalDigits;
        }

        @Override
        public int formatValueForAutoGeneratedAxis(char[] formattedValue, float value, int autoDecimalDigits) {
            float scaledValue = (value + sub) / scale;
            return super.formatValueForAutoGeneratedAxis(formattedValue, scaledValue, this.decimalDigits);
        }
    }
}