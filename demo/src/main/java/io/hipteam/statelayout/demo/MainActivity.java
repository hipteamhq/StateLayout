package io.hipteam.statelayout.demo;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import io.hipteam.statelayoutlib.StateLayout;

public class MainActivity extends AppCompatActivity {

    private StateLayout stateLayout;
    private TextView content;
    private View errorView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        stateLayout = (StateLayout) findViewById(R.id.state_layout);
        content = (TextView) findViewById(R.id.content);
        errorView = LayoutInflater.from(getApplicationContext()).inflate(R.layout.custom_error_from_code_by_inflate, null);

        findViewById(R.id.success).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new SuccessOperation().execute();
            }
        });
        findViewById(R.id.error).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // new ErrorOperation().execute();
                final int[] selectedPosition = {-1};
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Choose type");
                builder.setSingleChoiceItems(new String[]{"Default", "Set error view by layout resource id", "Set error view by inflated view"}, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        selectedPosition[0] = i;
                    }
                });
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        switch (selectedPosition[0]) {
                            case 0:
                                stateLayout.setErrorView(null);
                                stateLayout.setErrorViewResId(io.hipteam.statelayoutlib.R.layout.layout_state_layout_default_error_view);
                                new ErrorOperation().execute();
                                break;
                            case 1:
                                stateLayout.setErrorViewResId(R.layout.custom_error);
                                new ErrorOperation().execute();
                                break;
                            case 2:
                                stateLayout.setErrorView(errorView);
                                new ErrorOperation().execute();
                                break;
                        }
                    }
                });
                builder.show();

            }
        });
        findViewById(R.id.empty).setOnClickListener(new View.OnClickListener() {
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
            stateLayout.showContent();
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
            stateLayout.showError();
            if (stateLayout.getErrorView().findViewById(R.id.reload) != null) {
                stateLayout.getErrorView().findViewById(R.id.reload).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MainActivity.this, "RELOAD", Toast.LENGTH_SHORT).show();
                    }
                });
            }
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
