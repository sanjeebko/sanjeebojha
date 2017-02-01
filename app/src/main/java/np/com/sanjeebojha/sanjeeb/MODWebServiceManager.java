package np.com.sanjeebojha.sanjeeb;

import android.os.AsyncTask;
import android.util.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPInputStream;

/**
 * Created by sanjeeb on 09/01/2017.
 */

public class MODWebServiceManager {

    public static List<ContactMessage> Post(String s , String[] a) throws ExecutionException, InterruptedException {

        String parameters ="";

        for(int i=0; i<a.length ;i++){
            try {
                parameters += a[i]+"=" + URLEncoder.encode(a[i+1],"UTF-8")+"&";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }
        return new DownloadWebpageTask().execute(s,"POST",parameters).get();

    }
    private static class DownloadWebpageTask extends AsyncTask<String, Void, List<ContactMessage>> {
        public DownloadWebpageTask(){

        }
        @Override
        protected List<ContactMessage> doInBackground(String... urls) {

            List<ContactMessage> contactMessageList = null;
            try {

                contactMessageList= downloadUrl(urls[0] ,urls[1],urls[2] );
            } catch (IOException e) {

                //return "e:" + e.getMessage();
            }
            return  contactMessageList;
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(List<ContactMessage> result) {
        }

        private List<ContactMessage> downloadUrl(String myurl ,String method , String data) throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(myurl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestMethod(method);
                conn.setDoInput(true);
                conn.setRequestProperty("Accept-Encoding", "gzip");
                conn.setRequestProperty("Connection", "close");
                if (method == "POST") {
                    conn.setDoOutput(true);

                    conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

                    PrintWriter out = new PrintWriter(conn.getOutputStream());
                    out.print(data);
                    out.close();
                }
                // Starts the query
                conn.connect();
                int response = conn.getResponseCode();
                if(response==200)
                    is = new GZIPInputStream(conn.getInputStream());

                return readMessagesArray(is);

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }

        public List<ContactMessage> readMessagesArray(InputStream in) throws IOException {
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            List<ContactMessage> messages = new ArrayList<ContactMessage>();

            reader.beginArray();
            while (reader.hasNext()) {
                messages.add(readMessage(reader));
            }
            reader.endArray();
            return messages;
        }


        public ContactMessage readMessage(JsonReader reader) throws IOException {
            int id = -1;
            String phone = null;
            String message = null;
            Date time = null;

            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("id")) {
                    id = reader.nextInt();
                } else if (name.equals("phone")) {
                    phone = reader.nextString();
                } else if (name.equals("message")) {
                    message = reader.nextString();
                } else if(name.equals("time")){
                    String timeStr =reader.nextString();
                    try {
                        time = Common.ConvertShortDate(timeStr);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            if(time==null)
                time = new Date(2016,1,1);
            return new ContactMessage(id, phone, message,time);
        }
    }

}
