package Message;

import java.io.Serializable;
import java.util.Date;

public class ChatMessage implements Message, Serializable {

    private String from;
    private String content;
    private Date date;

    public ChatMessage(String from, String content, Date date) {
        this.from = from;
        this.content = content;
        this.date = date;
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
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
        return ChatType;
    }
}
