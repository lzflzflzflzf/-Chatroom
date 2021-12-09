package Message;

import java.io.Serializable;
import java.util.Date;

public class RcvGroupMessage implements Message, Serializable {

    private String from;
    private String GroupName;
    private String[] names;
    private Date date;

    public RcvGroupMessage(String from, String groupName, String[] names, Date date) {
        this.from = from;
        GroupName = groupName;
        this.names = names;
        this.date = date;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public String[] getNames() {
        return names;
    }

    public void setNames(String[] names) {
        this.names = names;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    @Override
    public int getType() {
        return 0;
    }
}
