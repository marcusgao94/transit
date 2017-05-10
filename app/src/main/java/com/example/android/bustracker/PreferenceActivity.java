package com.example.android.bustracker;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RadioButton;

/**
 * Created by yishuyan on 5/8/17.
 */

public class PreferenceActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.preference);

    }

    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        boolean checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.first_radio1:
                if (checked)
                    break;
            default:
                if (checked)
                    break;
        }
    }
    public void backHome(View view) {
        Intent intent = new Intent(PreferenceActivity.this, MainActivity.class);
        startActivity(intent);
    }
}
