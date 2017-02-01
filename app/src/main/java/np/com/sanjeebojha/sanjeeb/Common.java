package np.com.sanjeebojha.sanjeeb;

import android.content.Context;
import android.widget.Toast;

import java.text.ParseException;
import java.util.Date;

/**
 * Created by sanjeeb on 09/01/2017.
 */

public class Common {

    public static String ConvertDate(Date date){
        return Constants.DATETIME_FORMAT.format(date);
    }
    public static Date ConvertDate(String date) throws ParseException {
        return Constants.DATETIME_FORMAT.parse(date);
    }
    public static String ConvertShortDate(Date date) {
        return Constants.DATE_FORMAT.format(date);
    }
    public static Date ConvertShortDate(String date) throws ParseException {
        return Constants.DATE_FORMAT.parse(date);
    }

    public static void SetToast(Context context, String Message, boolean isLong){
        int toastLength = (isLong?Toast.LENGTH_LONG:Toast.LENGTH_SHORT);
        Toast toast = Toast.makeText(context,Message,toastLength);
        toast.show();
    }
}
