package com.example.berryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView textView;
    Button button;
    Integer i = 0;

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

//            getWebContent();

            // compare both the following functions, first one will hang UI second one will not
//            timeTakingFunc();
             timeTakingFuncWithAsync();
        });
    }

    String line = "";
    private void getWebContent(){
        try {
            URL url = new URL("https://api.timezonedb.com/v2.1/get-time-zone?key=3ZN5USVAZ546&format=xml&by=zone&zone=Asia/Karachi");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.connect();

            String content = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            while(true){
                line = reader.readLine();
                if(line == null)
                    break;
                content = content.concat(line);
            }
            line = content;
            Log.i("TAG", content);
            textView.setText(line);
        }
        catch (Exception e){
            e.printStackTrace();
            Log.i("TAG", "Error: " + e);
        }
    }

    private void timeTakingFunc(){
        for(int j=0;j<10000000;j++)
            i++;
        textView.append(" " + i);
    }
    private void timeTakingFuncWithAsync(){
        getWebContent();
        AsyncTask<Integer,Integer,Integer> task = new AsyncTask<Integer,Integer,Integer>(){
            protected Integer doInBackground(Integer... params){
                // do task
//                getWebContent();
//                for(int j=0;j<10000000;j++)
                    i++;
                return i;
            }
            protected void onPostExecute(Integer result){
                // update UI
                textView.append(" " + result);
            }
        };
        task.execute();
    }
}