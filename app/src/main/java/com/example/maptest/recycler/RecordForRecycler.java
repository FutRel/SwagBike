package com.example.maptest.recycler;

import java.util.Comparator;

public class RecordForRecycler {

    private int id;
    private String distance;
    private String time;
    private String date;

    public RecordForRecycler(int id, String distance, String time, String date) {
        this.id = id;
        this.distance = distance;
        this.time = time;
        this.date = date;
    }

    public static final Comparator<RecordForRecycler> compareByDistASC = (record1, record2) -> {
        return parseDist(record1) - parseDist(record2);
    };

    public static final Comparator<RecordForRecycler> compareByDistDESC = compareByDistASC.reversed();

    public static final Comparator<RecordForRecycler> compareByTimeASC = (record1, record2) -> {
        return parseTime(record1) - parseTime(record2);
    };

    public static final Comparator<RecordForRecycler> compareByTimeDESC = compareByTimeASC.reversed();

    public static int parseDist(RecordForRecycler record) {
        int index = record.getDistance().indexOf(',');
        int km = Integer.parseInt(record.getDistance().substring(0, index)) * 1000;
        int m = Integer.parseInt(record.getDistance().substring(index + 1, index + 3)) * 10;
        return km + m;
    }

    public static int parseTime(RecordForRecycler record) {
        int h = Integer.parseInt(record.getTime().substring(0,2)) * 3600;
        int m = Integer.parseInt(record.getTime().substring(3,5)) * 60;
        int s = Integer.parseInt(record.getTime().substring(6,8));
        return h + m + s;
    }
        
    public int getId() {
        return id;
    }
    public String getDistance() {
        return distance;
    }
    public String getTime() {
        return time;
    }
    public String getDate() {
        return date;
    }
}
