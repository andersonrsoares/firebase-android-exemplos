package br.com.anderson.firebasechat.model;

import java.util.Date;

/**
 * Created by DevMaker on 10/24/16.
 */

public class Message {
    private String text;
    private String from;
    private String to;
    private long date;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public long getDate() {
        return date;
    }

    public void setDate(long date) {
        this.date = date;
    }
}
