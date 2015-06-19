package edu.kit.psegruppe3.mensax.datamodels;

import java.util.ArrayList;

public class DailyMenu {
    private long timestamp;
    private ArrayList<Line> lineList;

    public DailyMenu(long timestamp, ArrayList<Line> lineList) {
        this.timestamp = timestamp;
        this.lineList = lineList;
    }

    public void addLinie() {
    }

    public long getTimestamp() {
        return timestamp;
    }
}
