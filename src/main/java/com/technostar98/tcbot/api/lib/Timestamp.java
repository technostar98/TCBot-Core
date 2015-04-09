package com.technostar98.tcbot.api.lib;

import java.util.Locale;

/**
 * Holds an immutable numerical representation of the exact time this was made, from hours to milliseconds
 */
public class Timestamp implements Comparable<Timestamp>{
    private final long systemMillis = System.currentTimeMillis(); //The time this timestamp was created
    private final int hours, minutes, seconds, milliseconds;
    private final Locale locale;

    /**
     * This class is used to hold an immutable timestamp and locale
     * @param time Time in millis from start
     * @param locale Used for time transformations across locales
     */
    public Timestamp(long time, Locale locale){
        this.hours = (int)(time / 3600000); time %= 3600000; //3600000 milliseconds in an hour
        this.minutes = (int)(time / 60000); time %= 60000; //60000 milliseconds in a minute
        this.seconds = (int)(time / 1000); time %= 1000; //1000 milliseconds in a second
        this.milliseconds = (int)time;
        this.locale = locale;
    }

    public int getHours() {
        return hours;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMilliseconds() {
        return milliseconds;
    }

    public Locale getLocale() {
        return locale;
    }

    public long getSystemTimeMillis(){
        return this.systemMillis;
    }

    //Return the total in its original form
    public long getTimeStampRaw(){
        return (long)(hours * 3600000 + minutes * 60000 + seconds * 1000 + milliseconds);
    }

    public String getFormattedTimestamp(String format){
        //TODO write formatted return
        return "";
    }

    public String getFormattedSystemTimeMillis(String format){
        //TODO write formatted return
        return "";
    }

    /**
     *
     * @return A string formatted as "HH:MM:SS:mmm" where M = minutes and m = milliseconds
     */
    @Override
    public String toString() {
        String hours = this.hours >= 10 ? "" + this.hours : "0" + this.hours;
        String minutes = this.minutes >= 10 ? "" + this.minutes : "0" + this.minutes;
        String seconds = this.seconds >= 10 ? "" + this.seconds : "0" + this.seconds;
        String millis = this.milliseconds >= 100 ? "" + this.milliseconds : this.milliseconds >= 10 ? "0" + this.milliseconds : "00" + this.milliseconds;
        return hours + ":" + minutes + ":" + seconds + ":" + millis;
    }

    /**
     * This compares two {@link Timestamp} to each other, most likely going by their times and not locales
     * @param o
     * @return A positive int if this is greater than @param o, 0 if it's equal, or a negative number if it's less than
     */
    @Override
    public int compareTo(Timestamp o) {
        if(this.locale == null)
            if (this.getHours() == o.getHours())
                if (this.getMinutes() == o.getMinutes())
                    if (this.getSeconds() == o.getSeconds())
                        if (this.getMilliseconds() == o.getMilliseconds())
                            return 0;
                        else
                            return this.getMilliseconds() - o.getMilliseconds();
                    else
                        return this.getSeconds() - o.getSeconds();
                else
                    return this.getMinutes() - o.getMinutes();
            else
                return this.getHours() - o.getHours();
        else
            if(this.getLocale().equals(o.getLocale()))
                if (this.getHours() == o.getHours())
                    if (this.getMinutes() == o.getMinutes())
                        if (this.getSeconds() == o.getSeconds())
                            if (this.getMilliseconds() == o.getMilliseconds())
                                return 0;
                            else
                                return this.getMilliseconds() - o.getMilliseconds();
                        else
                            return this.getSeconds() - o.getSeconds();
                    else
                        return this.getMinutes() - o.getMinutes();
                else
                    return this.getHours() - o.getHours();
            else
                return this.getLocale().getDisplayCountry().compareTo(o.getLocale().getDisplayCountry()); //TODO compare two locales

    }
}
