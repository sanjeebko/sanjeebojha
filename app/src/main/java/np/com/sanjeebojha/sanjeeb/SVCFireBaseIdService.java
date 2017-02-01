package np.com.sanjeebojha.sanjeeb;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by sanjeeb on 22/01/2017.
 */

public class SVCFireBaseIdService extends FirebaseInstanceIdService {
    private String TAG=Constants.TAG_FIREBASE_IDSERVICE;
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // TODO: Implement this method to send any registration to your app's servers.

        //sendRegistrationToServer(refreshedToken);
        storeToken(refreshedToken);
    }

    private void storeToken(String token){
        Log.d(TAG,"Storing Token.");
        // store token in shared preferences
        MODSharedPrefManager.getInstance(getApplicationContext()).saveDeviceToken(token);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"Checking Firebase Token.");
        if(MODSharedPrefManager.getInstance(getApplicationContext()).getDeviceToken()==null){
            //create and save token
            onTokenRefresh();

        }
    }
}
