package com.lau.guesslogo;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class easy extends AppCompatActivity {
    ImageView img;

    public class DownloadTask extends AsyncTask<String, Void, String> {

        protected String doInBackground(String... strings) {
//            Log.i("URL: ", strings[0]);
//            return "Done";
            String result = "";
            URL url;
            HttpURLConnection urlConnection = null;

            try {

                url = new URL(strings[0]);
                urlConnection = (HttpURLConnection) url.openConnection();

                InputStream in = urlConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);

                int data = reader.read();
                Log.i("Data Result", String.valueOf(data));
                while (data != -1) {

                    char current = (char) data;
                    result += current;
//                    Log.i("Test", result);
                    data = reader.read();
                }

                return result;
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }


            //Setting connection to server
//            return null;
        }


    }

    public class ImageDownloader extends AsyncTask<String, Void, Bitmap> {
        protected Bitmap doInBackground(String... urls) {

            try {
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                InputStream in = connection.getInputStream();

                Bitmap DownloadedImage = BitmapFactory.decodeStream(in);

                return DownloadedImage;


            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }


        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        DownloadTask task = new DownloadTask();
        //TextView txt = (TextView) findViewById(R.id.imageView);
        String result = null;
        try {
            result = task.execute("https://lau.edu.lb").get();
             //  result = task.execute("https://www.pcmag.com/picks/best-android-apps").get();
        } catch (Exception e) {
            e.printStackTrace();
        }
      //  Log.i("TEST ",result);
        Pattern p = Pattern.compile("img src=\"(.*?)\"");
        Matcher m = p.matcher(result);
        if (m.find()) {
            //txt.setText(m.group(1));

            img = (ImageView) findViewById(R.id.imageView);
            ImageDownloader task2 = new ImageDownloader();
            Bitmap downloadedImage;
            try {

                downloadedImage = task2.execute(m.group(1)).get();
                Log.i("test : ", m.group(1));
               // img.setImageBitmap(downloadedImage);

            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }
}