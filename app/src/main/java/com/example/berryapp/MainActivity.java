package com.example.berryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.stream.Collectors;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setStateVariables();
    }

    private void setStateVariables(){
        textView = findViewById(R.id.textView);
        button = findViewById(R.id.button);
        button.setOnClickListener(v -> {
            textView.append(".");
            // compare both the following functions, first one will hang UI second one will not
            // timeTakingFunc();
            timeTakingFuncWithAsync();
        });
    }

    private void timeTakingFuncWithAsync(){
        new WebContentTask(this).execute();
    }

    // AsyncTask<Params, Progress, Result>
    private static class WebContentTask extends AsyncTask<Void, Void, String> {

        private final WeakReference<MainActivity> activityReference;

        // only retain a weak reference to the activity
        WebContentTask(MainActivity context) {
            activityReference = new WeakReference<>(context);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return getWebContent();
        }

        @Override
        protected void onPostExecute(String result) {

            // get a reference to the activity if it is still there
            MainActivity activity = activityReference.get();
            if (activity == null || activity.isFinishing()) return;

            // modify the activity's UI
            TextView textView = activity.findViewById(R.id.textView);
            textView.append(result);

            // access Activity member variables
            // activity.mSomeMemberVariable = 321;
        }

        public String getWebContent(){
            String line;
            try {
                URL url = new URL("https://api.timezonedb.com/v2.1/get-time-zone?key=3ZN5USVAZ546&format=xml&by=zone&zone=Asia/Karachi");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();

                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

                line = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N ?
                        reader.lines().collect(Collectors.joining()) :
                        "Please use Android Nougat.";
            }
            catch (Exception e){
                line = "some error";
                e.printStackTrace();
                Log.i("TAG", "Error: " + e);
            }

            return line;
        }

    }
}