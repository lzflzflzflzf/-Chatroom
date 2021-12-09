package Message;

import java.io.Serializable;

public class ChatResponseMessage implements Message, Serializable {

    private boolean success;
    private String reason;

    public ChatResponseMessage(boolean success, String reason) {
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
    public int getType() {
        return ChatResponseType;
    }
}
