package examp.com.vts.net;

/**
 * Created by a55 on 2017/11/28.
 */

public class ApiModel<T> {
    public String msg;

    public int status;

    public T data;

    public T getData() {
        return data;
    }
}
