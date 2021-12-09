package Message;

import java.io.Serializable;
import java.util.Date;

public class ChatRequestMessage implements Message, Serializable {

    private String from;
    private String to;
    private String content;
    private Date date;

    public ChatRequestMessage(String from, String to, String content, Date date) {
        this.from = from;
        this.to = to;
        this.content = content;
        this.date = date;
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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int getType() {
        return ChatRequestType;
    }
}
