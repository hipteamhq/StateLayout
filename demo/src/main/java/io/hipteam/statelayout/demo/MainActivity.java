package io.hipteam.statelayout.demo;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import io.hipteam.statelayoutlib.StateLayout;

public class MainActivity extends AppCompatActivity {

    private StateLayout stateLayout;
    private TextView content;

    private Button success;
    private Button error;
    private Button empty;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateLayout = (StateLayout) findViewById(R.id.state_layout);
        content = (TextView) findViewById(R.id.content);

        success = (Button) findViewById(R.id.success);
        error = (Button) findViewById(R.id.error);
        empty = (Button) findViewById(R.id.empty);

        success.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SuccessOperation().execute();
            }
        });

        error.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new ErrorOperation().execute();
            }
        });

        empty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new EmptyOperation().execute();
            }
        });
    }

    private class SuccessOperation extends AsyncTask<Void, Void, Result> {

        @Override
        protected Result doInBackground(Void... voids) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Result(Result.SUCCESS, "Content");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            stateLayout.showLoading();
        }



        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            content.setText(result.getMessage());
            stateLayout.hideLoading();
        }
    }

    private class ErrorOperation extends AsyncTask<Void, Void, Result> {

        @Override
        protected Result doInBackground(Void... voids) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Result(Result.ERROR, "");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            stateLayout.showLoading();
        }



        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            content.setText(result.getMessage());
            stateLayout.hideLoading();
            stateLayout.showError();
        }
    }

    private class EmptyOperation extends AsyncTask<Void, Void, Result> {

        @Override
        protected Result doInBackground(Void... voids) {
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return new Result(Result.SUCCESS, "");
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            stateLayout.showLoading();
        }

        @Override
        protected void onPostExecute(Result result) {
            super.onPostExecute(result);
            content.setText(result.getMessage());
            stateLayout.hideLoading();
            stateLayout.showEmpty();
        }
    }


    private class Result {

        public static final int SUCCESS = 0;
        public static final int ERROR = 1;

        private int state;
        private String message;

        public Result(int state, String message) {
            this.state = state;
            this.message = message;
        }

        public int getState() {
            return state;
        }

        public String getMessage() {
            return message;
        }
    }

}
