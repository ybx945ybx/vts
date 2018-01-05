package examp.com.vts.net;

/**
 * Created by a55 on 2017/11/28.
 */

public class ErrorException extends Exception{
    public int code;
    public String msg;

    public static ErrorException createException(int code, String msg) {
        ErrorException ex = new ErrorException(code, msg);
        ex.code = code;
        ex.msg = msg;
        return ex;
    }

    public ErrorException(int code, String msg) {
        super(code + "###" + msg);
    }

}
