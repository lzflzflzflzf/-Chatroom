package Message;

import java.io.Serializable;

public class LoginResponseMessage implements Message, Serializable {

    private boolean success;
    private String reason;

    public LoginResponseMessage(boolean success, String reason) {
        this.success = success;
        this.reason = reason;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    @Override
    public String toString() {
        return "LoginResponseMessage{" +
                "success=" + success +
                ", reason='" + reason + '\'' +
                '}';
    }

    @Override
    public int getType() {
        return Message.LoginResponseType;
    }
}
