package Message;

import java.io.Serializable;

public class LoginRequestMessage implements Message, Serializable {
    private String UserName;
    private String PassWord;

    public LoginRequestMessage(String userName, String passWord) {
        UserName = userName;
        PassWord = passWord;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPassWord() {
        return PassWord;
    }

    public void setPassWord(String passWord) {
        PassWord = passWord;
    }



    @Override
    public int getType() {
        return Message.LoginRequestType;
    }

    @Override
    public String toString() {
        return "LoginRequestMessage{" +
                "UserName='" + UserName + '\'' +
                ", PassWord='" + PassWord + '\'' +
                '}';
    }
}
