package com.evilflora.warframesentinel;

class TimestampToDate{

    static String convert(long timestamp, boolean accurary){
        long MINUTE = 60;
        long HOUR = 60 * 60;
        long HOURS_IN_DAY = 24;
        long DAY = 60 * 60 * 24;

        long _timestamp = timestamp / 1000;
        double _day = (double)_timestamp/ DAY;
        double _hour = (_timestamp / HOUR) % HOURS_IN_DAY;
        double _minute = (_timestamp % HOUR) / MINUTE;
        double _seconde = _timestamp % MINUTE;

        if (accurary) {
            if (_day >= 1L) {
                return (int)_day + "d " + (int)_hour + "h " + (int)_minute + "m " + (int)_seconde + "s";
            } else if (_hour >= 1L) {
                return (int)_hour + "h " + (int)_minute + "m " + (int)_seconde + "s";
            } else {
                return (int)_minute + "m " + (int)_seconde + "s";
            }
        } else {
            return (_day > 1L ? (int)_day + "d" : (_hour > 1L ? (int)_hour + "h" : (int)_minute + "m"));
        }
    }
}