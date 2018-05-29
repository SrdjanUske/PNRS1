package rtrk.pnrs.jniexample;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends Activity implements View.OnClickListener {

    private static final String LOG_TAG = "MainActivity";

    private EditText mInput;
    private TextView mResult;
    private FibonacciNative mFibonacci;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mInput = (EditText) findViewById(R.id.input_number);
        mResult = (TextView) findViewById(R.id.result);

        Button calculate = (Button) findViewById(R.id.action_calculate);
        calculate.setOnClickListener(this);
        
        mFibonacci = new FibonacciNative();
    }

    @Override
    public void onClick(View view) {
        try {
            int n = Integer.parseInt(mInput.getText().toString());
            int Fn = mFibonacci.get(n);
            mResult.setText(getResources().getString(R.string.str_result) + Fn);
        } catch (NumberFormatException e) {
            Log.e(LOG_TAG, "Invalid input value", e);
        }
    }
}
