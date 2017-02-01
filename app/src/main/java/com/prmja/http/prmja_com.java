package com.prmja.http;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.JsonReader;
import android.util.Log;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.zip.GZIPInputStream;

/**
 * Created by Malek on 2/11/2016.
 */
public class prmja_com {
    public  String Get(String s ,String[] a) throws ExecutionException, InterruptedException {

        String parameters = "";

        for (int i = 0; i < a.length; i++) {
            try {
                parameters += a[i] + "=" + URLEncoder.encode(a[i + 1], "UTF-8") + "&";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }
        return new DownloadWebpageTaskz().execute(s+"?"+parameters,"GET","").get();
    }

    public  String Post(String s ,String[] a,TextView tv) throws ExecutionException, InterruptedException {

        String parameters ="";

        for(int i=0; i<a.length ;i++){
            try {
                parameters += a[i]+"=" + URLEncoder.encode(a[i+1],"UTF-8")+"&";
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            i++;
        }


        return new DownloadWebpageTaskz().execute(s,"POST",parameters).get();

    }

    public  Bitmap Download_Image(String s) throws ExecutionException, InterruptedException, UnsupportedEncodingException {
        return new Download_Image().execute(s).get();

    }
    private  class DownloadWebpageTaskz extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

// params comes from the execute() call: params[0] is the url.
            try {
                String result =downloadUrl(urls[0] ,urls[1],urls[2] );
                return result;
            } catch (IOException e) {
                return "e:" + e.getMessage();
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
        }

        private String downloadUrl(String myurl ,String method , String data) throws IOException {
            InputStream is = null;
// Only display the first 500 characters of the retrieved
// web page content.


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

                return readJsonStream(is);
                //is = conn.getInputStream();

                // Convert the InputStream into a string


                    //String contentAsString = readIt(is, conn.getContentLength());

                    //return contentAsString;
               

                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (is != null) {
                    is.close();
                }
            }
        }
        public String readJsonStream(InputStream in) throws IOException {
            String result="";
            JsonReader reader = new JsonReader(new InputStreamReader(in, "UTF-8"));
            try {
                List<Message> messages = readMessagesArray(reader);
                for (Message msg:messages) {
                    result += "ID:"+String.valueOf(msg.id) +",phone:"+msg.phone +",message:" + msg.message+"#";
                }
            } finally {
                reader.close();
            }

            return  result;
        }
        public List<Message> readMessagesArray(JsonReader reader) throws IOException {
            List<Message> messages = new ArrayList<Message>();

            reader.beginArray();
            while (reader.hasNext()) {
                messages.add(readMessage(reader));
            }
            reader.endArray();
            return messages;
        }

        public class Message {
            public int id;
            public String phone;
            public String message;
            public Message(int ID,String Phone, String Message){
                this.id = ID;
                this.phone = Phone;
                this.message = Message;
            }

        }
        public Message readMessage(JsonReader reader) throws IOException {
            int id = -1;
            String phone = null;
            String message = null;


            reader.beginObject();
            while (reader.hasNext()) {
                String name = reader.nextName();
                if (name.equals("id")) {
                    id = reader.nextInt();
                } else if (name.equals("phone")) {
                    phone = reader.nextString();
                } else if (name.equals("message")) {
                    message = reader.nextString();
                }
                else {
                    reader.skipValue();
                }
            }
            reader.endObject();
            return new Message(id, phone, message);
        }
        private String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
            int n = 0;
            char[] buffer = new char[1024 * 4];
            InputStreamReader reader = new InputStreamReader(stream, "UTF8");
            StringWriter writer = new StringWriter();
            while (-1 != (n = reader.read(buffer))) writer.write(buffer, 0, n);
            return writer.toString();
        }


    }


    private  class Download_Image extends AsyncTask<String, Integer, Bitmap>{
        private  Bitmap downloadUrl(String strUrl) throws IOException{
            Bitmap bitmap=null;
            InputStream iStream = null;
            try{
                URL url = new URL(strUrl);
                /** Creating an http connection to communcate with url */
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

                /** Connecting to url */
                urlConnection.connect();

                /** Reading data from url */
                iStream = urlConnection.getInputStream();

                /** Creating a bitmap from the stream returned from the url */
                bitmap = BitmapFactory.decodeStream(iStream);

            }catch(Exception e){

            }finally{
                iStream.close();
            }
            return bitmap;
        }

        Bitmap bitmap = null;
        @Override
        protected Bitmap doInBackground(String... url) {
            try{
                bitmap = downloadUrl(url[0]);
            }catch(Exception e){
                Log.d("Background Task",e.toString());
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap result) {
        }
    }
}
