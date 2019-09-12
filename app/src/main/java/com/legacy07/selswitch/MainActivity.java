package com.legacy07.selswitch;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class MainActivity extends AppCompatActivity {

    Switch aSwitch;
    TextView tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        aSwitch = findViewById(R.id.selswitch);

        if (runcommand("su -c 'getenforce'").equals(""))
        {
            tv.setText("No Root Detected");
            tv.setTextSize(30);
            aSwitch.setVisibility(View.INVISIBLE);
        }


        if (runcommand("su -c 'getenforce'").contains("Enforcing")) {
            aSwitch.setChecked(true);
        }



        aSwitch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (runcommand("su -c 'getenforce'").contains("Enforcing")) {
                    runcommand("su -c " + '"' + '"' + "setenforce 0" + '"' + '"');
                    aSwitch.setChecked(false);
                } else {
                    runcommand("su -c " + '"' + '"' + "setenforce 1" + '"' + '"');
                    aSwitch.setChecked(true);
                }
                tv.setText("Changed to " + runcommand("su -c 'getenforce'"));
            }
        });

    }

    public String runcommand(String command) {
        StringBuilder log = new StringBuilder();
        try {
            Process process = Runtime.getRuntime().exec(command);
            process.waitFor();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line).append("\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return log.toString();
    }
}
