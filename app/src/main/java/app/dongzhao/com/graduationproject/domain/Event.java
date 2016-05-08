package app.dongzhao.com.graduationproject.domain;

import java.io.Serializable;

public class Event implements Serializable {
    private String title;
    private String content;
    private long time;


    public Event(String title, String content, long time) {
        this.title = title;
        this.content = content;
        this.time = time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }


}
