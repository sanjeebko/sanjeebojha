package np.com.sanjeebojha.sanjeeb;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by sanjeeb on 22/01/2017.
 * tutorial https://www.simplifiedcoding.net/firebase-cloud-messaging-android/
 *
 * Created a new controller for web services.  http://sanjeebojha.azurewebsites.net/WebServices
 * TODO: User this service to register account, register device and login.
 */

public class SVCFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG=Constants.TAG_FIREBASE_MESSAGESERVICE;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        if(remoteMessage.getData().size()>0){
            Log.e(TAG,"Data Payload:" + remoteMessage.getData().toString());
            try{
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                sendPushNotification(json);
            }catch (Exception e){
                Log.e(TAG,"Exception:" +e.getMessage() );
            }
        }
    }

    private void sendPushNotification(JSONObject json) {
        Log.e(TAG,"Notification JSON " + json.toString());
        try{
            JSONObject data = json.getJSONObject("data");
            String title = data.getString("title");
            String message= data.getString("message");
            String imageURL = data.getString("image");

            MODNotificationManager modNotificationManager = new MODNotificationManager(getApplicationContext());

            Intent intent = new Intent(getApplicationContext(),MainActivity.class);

            if(imageURL.equals("null")){
                modNotificationManager.showSmallNotification(title,message,intent);
            }else{
                modNotificationManager.showBigNotification(title,message,imageURL,intent);
            }
        }catch (JSONException e){
            Log.e(TAG,"JSON Exception:" + e.getMessage());
        }catch(Exception e){
            Log.e(TAG,"Exception: " + e.getMessage());
        }
    }
}
