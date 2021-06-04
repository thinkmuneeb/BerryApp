package com.example.berryapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
        textView = (TextView) findViewById(R.id.textView);
        button = (Button) findViewById(R.id.button);
        button.setOnClickListener(v -> {
            textView.append(".");

            // compare both the following functions, first one will hang UI second one will not
            //timeTakingFunc();
            asyncTimeTakingFunc();
        });
    }

    private void timeTakingFunc(){
        for(int j=0;j<10000000;j++)
            i++;
        textView.append(" " + i);
    }
    private void asyncTimeTakingFunc(){
        AsyncTask<Integer,Integer,Integer> task = new AsyncTask<Integer,Integer,Integer>(){
            protected Integer doInBackground(Integer... params){
                // do task
                for(int j=0;j<10000000;j++)
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