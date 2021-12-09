package UserService;

import java.util.Date;
import java.util.concurrent.CopyOnWriteArrayList;

public class Group {
    private String GroupName;
    private Date date;
    private String CreatePerson;

    public Group(String groupName, Date date, String createPerson) {
        GroupName = groupName;
        this.date = date;
        CreatePerson = createPerson;
    }



    public String getGroupName() {
        return GroupName;
    }

    public void setGroupName(String groupName) {
        GroupName = groupName;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getCreatePerson() {
        return CreatePerson;
    }

    public void setCreatePerson(String createPerson) {
        CreatePerson = createPerson;
    }
}
