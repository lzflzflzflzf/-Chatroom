package UserService;

import java.util.concurrent.CopyOnWriteArrayList;

public class GroupOperator {
    private CopyOnWriteArrayList<Group> allgroups = new CopyOnWriteArrayList<Group>();

    public void add(Group agroup)
    {
        allgroups.add(agroup);
    }

    public void remove(String groupName)
    {
        int index = 0;
        for (int i = 0; i < allgroups.size(); i++) {
            if(allgroups.get(i).getGroupName().equals(groupName))
            {
                index = i;
                break;
            }
        }
        allgroups.remove(index);
    }


}
