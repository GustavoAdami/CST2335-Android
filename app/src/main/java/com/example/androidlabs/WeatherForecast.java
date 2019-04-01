package com.example.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WeatherForecast extends AppCompatActivity {
    private ProgressBar progressBar;
    private ImageView weatherIcon;

//    private String windSpeed;
//    private String minTemperature;
//    private String maxTemperature;
//    private String currentTemperature;
//    private double uvRating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);

        ForecastQuery networkThread = new ForecastQuery();
        networkThread.execute("http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=7e943c97096a9784391a981c4d878b22&mode=json&units=metric"); //this starts doInBackground on other thread

        progressBar = findViewById(R.id.weatherProgressBar);
        progressBar.setVisibility(View.VISIBLE);


    }

    private class ForecastQuery extends AsyncTask<String, Integer, String>{
        private String windSpeed;
        private String minTemperature;
        private String maxTemperature;
        private String currentTemperature;
        private Bitmap bitmap;
        private double uvRating;

        @Override
        protected String doInBackground(String... params) {
            try{
                String myUrl = params[0];

                //create the network connection:
                URL url = new URL(myUrl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = urlConnection.getInputStream();

                // json is UTF-8 by default
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();

                String line = null;
                while ((line = reader.readLine()) != null)
                {
                    sb.append(line + "\n");
                }
                String result = sb.toString();

                // Create JSON object
                JSONObject jObject = new JSONObject(result);

                // String mainToken = jObject.getString("main");
                windSpeed = jObject.getJSONObject("wind").getString("speed");
                publishProgress(25);

                minTemperature = jObject.getJSONObject("main").getString("temp_min");
                publishProgress(50);

                maxTemperature = jObject.getJSONObject("main").getString("temp_max");
                publishProgress(75);

                currentTemperature = jObject.getJSONObject("main").getString("temp");

                String iconName = jObject.getJSONArray("weather").getJSONObject(0).getString("icon");

                // get uvRating
                URL uvUrl = new URL("http://api.openweathermap.org/data/2.5/uvi?appid=7e943c97096a9784391a981c4d878b22&lat=45.348945&lon=-75.759389");
                HttpURLConnection uvUrlConnection = (HttpURLConnection) uvUrl.openConnection();
                InputStream uvInputStream = uvUrlConnection.getInputStream();

                // json is UTF-8 by default
                BufferedReader uvReader = new BufferedReader(new InputStreamReader(uvInputStream, "UTF-8"), 8);
                StringBuilder uvSb = new StringBuilder();

                String uvLine = null;
                while ((uvLine = uvReader.readLine()) != null)
                {
                    uvSb.append(uvLine + "\n");
                }
                String uvResult = uvSb.toString();

                JSONObject uvJObject = new JSONObject(uvResult);
                uvRating = uvJObject.getDouble("value");

                urlConnection.disconnect();
                uvUrlConnection.disconnect();

                if(fileExistance(iconName + ".png")){
                    // Read from local storage
                    // read the icon file from local storage
                    File file = getBaseContext().getFileStreamPath(iconName + ".png");
                    FileInputStream fis = new FileInputStream(file);
                    bitmap = BitmapFactory.decodeStream(fis);
                    bitmap = BitmapFactory.decodeFile(iconName + ".png");

                    String x = "";


                } else {
                    // if file does not exist, Download file
                    URL imgUrl = new URL("http://openweathermap.org/img/w/" + iconName + ".png");
                    HttpURLConnection urlConnection2 = (HttpURLConnection) imgUrl.openConnection();
                    urlConnection2.connect();
//                    int responseCode = urlConnection.getResponseCode();
//                    if (responseCode == 200) {
                    bitmap = BitmapFactory.decodeStream(urlConnection2.getInputStream());
//                    }

                    FileOutputStream outputStream = openFileOutput( iconName + ".png", Context.MODE_PRIVATE);
                    //image.compress(Bitmap.CompressFormat.PNG, 80, outputStream);
                    outputStream.flush();
                    outputStream.close();
                    urlConnection2.disconnect();


                }

                publishProgress(100);
                Thread.sleep(2000);

            } catch (Exception ex){
                Log.e("JSON Crash :( !!", ex.getMessage() );
            }

            //Log.e("END Task", "Crash" );
            //return type 3, which is String:
            return "ENDDD";

        }

        @Override
        public void onProgressUpdate(Integer ... values){
            progressBar.setVisibility(View.VISIBLE);
            progressBar.setProgress(values[0]);
        }

        @Override
        public void onPostExecute(String s){
//            private Bitmap bitmap;

            TextView minTemp = findViewById(R.id.minTemperature);
            minTemp.setText(String.format("Min: %s", minTemperature));

            TextView curTemp = findViewById(R.id.currentTemperature);
            curTemp.setText(String.format("Current: %s", currentTemperature));

            TextView maxTemp = findViewById(R.id.maxTemperature);
            maxTemp.setText(String.format("Max: %s", maxTemperature));

            TextView wind = findViewById(R.id.windSpeed);
            wind.setText(String.format("Wind speed: %s", windSpeed));

            TextView uv = findViewById(R.id.uvRating);
            uv.setText(String.format("UV Rating: %s", uvRating));

            // weatherIcon.setImageBitmap(bitmap);

            progressBar.setVisibility(View.INVISIBLE);

        }

        public boolean fileExistance(String fname){
            File file = getBaseContext().getFileStreamPath(fname);
            return file.exists();
        }
    }
}
