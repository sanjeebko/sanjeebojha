package np.com.sanjeebojha.sanjeeb;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

/**
 * Created by sanjeeb on 22/01/2017.
 */

public class Constants {
    public static final String STRING_TOKEN_NOT_GENERATED="Token not generated.";
    public static final String URL_REGISTERDEVICE="http://sanjeebojha.azurewebsites.net/webservices/RegisterDevice";
    public static final String URL_REGISTER="http://sanjeebojha.azurewebsites.net/webservices/Register";
    public static final String URL_LOGIN="http://sanjeebojha.azurewebsites.net/webservices/CreateSession";
    public static final String TAG="sanjeebTAG";
    public static final String TAG_ACTIVITY_LOGIN="sanjeebTAG_Login";
    public static final String TAG_ACTIVITY_REGISTRATION="sanjeebTAG_Reg";
    public static final String TAG_FIREBASE_IDSERVICE="sanjeebTAG_FBIS";
    public static final String TAG_FIREBASE_MESSAGESERVICE="sanjeebTAG_FBMS";
    public static final String KEY_USERNAME="UserName";
    public static final String KEY_EMAIL="UserName";
    public static final String KEY_PASSWORD="Password";
    public static final String KEY_TOKEN="Token";
    public static final String KEY_LOGINDATE="LoginDate";
    public static final String KEY_LOGINVALIDUNTIL="LoginValidUntil";
    public static final String KEY_SESSION="Session";

    public static final DateFormat DATETIME_FORMAT = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM/yyyy");
}
