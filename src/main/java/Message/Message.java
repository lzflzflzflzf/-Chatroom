package Message;

public interface Message {

    public static final int LoginRequestType = 1;
    public static final int LoginResponseType = 2;
    public static final int RegisterRequestType = 3;
    public static final int RegisterResponseType = 4;
    public static final int ChatRequestType = 5;
    public static final int ChatResponseType = 6;
    public static final int ChatType = 7;

    abstract int getType();//获得这个message的类型



}
