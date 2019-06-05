package com.evilflora.warframesentinel.Modele;

/**
 * Created by guill on 03/06/2019 for WarframeSentinel
 */

public class TimestampToTimeleft{

    private static final long MINUTE = 60;
    private static final long HOUR = 3600;
    private static final long DAY = 86400;
    private static final long HOURS_IN_DAY = 24;

    private TimestampToTimeleft() {}

    public static String convert(long timestamp, boolean accurary){

        long localTimestamp = timestamp / 1000;
        double day = (double)localTimestamp/ DAY;
        double hour = (localTimestamp / HOUR) % HOURS_IN_DAY;
        double minute = (localTimestamp % HOUR) / MINUTE;
        double seconde = localTimestamp % MINUTE;

        if (accurary) {
            if (day >= 1L) {
                return String.format("%dd %2dh %02dm %02ds", (int)day, (int)hour, (int)minute, (int)seconde);
            } else if (hour >= 1L) {
                return String.format("%dh %02dm %02ds", (int)hour, (int)minute, (int)seconde);
            } else {
                return String.format("%dm %02ds", (int)minute, (int)seconde);
            }
        } else {
            if (day > 1L) {
                return (int)day + "d";
            } else if (hour > 1L) {
                return (int)hour + "h";
            } else {
                return (int)minute + "m";
            }
        }
    }
}