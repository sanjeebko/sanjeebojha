package np.com.sanjeebojha.sanjeeb;

import android.content.Context;
import android.content.SharedPreferences;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by sanjeeb on 22/01/2017.
 */

public class MODSharedPrefManager {
    private static final String SHARED_PREF_NAME="FMCSharedPref";


    private static MODSharedPrefManager mInstance;
    private static Context mCtx;

    private MODSharedPrefManager(Context context){
        mCtx = context;
    }

    public static synchronized MODSharedPrefManager getInstance(Context context){
        if(mInstance==null)
            mInstance = new MODSharedPrefManager(context);
        return mInstance;
    }

    public boolean saveDeviceToken(String token){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_TOKEN,token);
        editor.apply();
        return  true;
    }

    public String getDeviceToken(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.KEY_TOKEN,null);
    }

    public boolean saveUserAccount(String UserName,String Email,String Password,String Token,String Session, Date LoginDate, Date ValidTill){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(Constants.KEY_USERNAME,UserName);
        editor.putString(Constants.KEY_EMAIL,Email);
        editor.putString(Constants.KEY_PASSWORD,Password);
        editor.putString(Constants.KEY_TOKEN,Token);
        editor.putString(Constants.KEY_LOGINDATE, Constants.DATETIME_FORMAT.format(LoginDate));
        editor.putString(Constants.KEY_LOGINVALIDUNTIL,Constants.DATETIME_FORMAT.format(ValidTill));
        editor.putString(Constants.KEY_SESSION,Session);
        editor.apply();
        return  true;
    }

    public String getUserName(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.KEY_USERNAME,null);
    }
    public String getEmail(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.KEY_EMAIL,null);
    }

    public String getPassword(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.KEY_PASSWORD,null);
    }
    public String getToken(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.KEY_TOKEN,null);
    }
    public String getSession(){
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        return  sharedPreferences.getString(Constants.KEY_SESSION,null);
    }
    public Date getLoginDate() throws ParseException {
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        String loginDate = sharedPreferences.getString(Constants.KEY_LOGINDATE,null);
        return  (loginDate==null)?null:Constants.DATETIME_FORMAT.parse(loginDate);
    }
    public Date getLoginValidTillDate() throws ParseException{
        SharedPreferences sharedPreferences= mCtx.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        String loginvaliduntil= sharedPreferences.getString(Constants.KEY_LOGINVALIDUNTIL,null);
        return  (loginvaliduntil==null)?null:Constants.DATETIME_FORMAT.parse(loginvaliduntil);
    }



}
