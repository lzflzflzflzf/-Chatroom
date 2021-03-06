package Message;

import java.io.Serializable;

public class RegisterRequestMessage implements Message, Serializable {

    private String name;
    private String password;

    public RegisterRequestMessage(String name, String password) {
        this.name = name;
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public int getType() {
        return RegisterRequestType;
    }
}
