package com.legacy07.selswitch;

import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.Switch;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

public class MainActivity extends Activity {

    CheckBox aSwitch;
    Typeface typeface, typeface1;
    AssetManager am, am1;
    TextView tv, banner, title,tv11;
    Animation aniFade, aniBlink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv = findViewById(R.id.tv);
        tv11=findViewById(R.id.textView2);
        title = findViewById(R.id.textView);
        aSwitch = findViewById(R.id.selswitch);
        banner = findViewById(R.id.legacy);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        am = getApplicationContext().getAssets();
        typeface = Typeface.createFromAsset(am, String.format(Locale.US, "fonts/%s", "GreatVibes-Regular.otf"));
        am1 = getApplicationContext().getAssets();
        typeface1 = Typeface.createFromAsset(am1, String.format(Locale.US, "fonts/%s", "Pacifico.ttf"));


        title.setTypeface(typeface);
        aSwitch.setTypeface(typeface);
        tv11.setTypeface(typeface);

        tv.setTypeface(typeface1);
        //banner.setTypeface(typeface1);


        aniFade = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.fade);
        aniBlink = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.blink);
        banner.startAnimation(aniBlink);

        if (runcommand("su -c 'getenforce'").equals("")) {
            tv.setText("No Root Detected");
            tv.setTextSize(30);
            aSwitch.setVisibility(View.INVISIBLE);
        } else {

            if (runcommand("su -c 'getenforce'").contains("Enforcing")) {
                aSwitch.setChecked(true);
                tv.setText("Status : Enforcing");
            } else tv.setText("Status : Permissive");

            aSwitch.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    tv.startAnimation(aniFade);

                    if (runcommand("su -c 'getenforce'").contains("Enforcing")) {
                        runcommand("su -c " + '"' + '"' + "setenforce 0" + '"' + '"');
                        aSwitch.setChecked(false);
                    } else {
                        runcommand("su -c " + '"' + '"' + "setenforce 1" + '"' + '"');
                        aSwitch.setChecked(true);
                    }
                    tv.setText("Status : " + runcommand("su -c 'getenforce'"));
                }
            });
        }

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

/*
repo init -u https://gitlab.com/OrangeFox/Manifest.git -b fox_8.1
repo sync -j8 --force-sync
git clone https://github.com/rk779/twrp_device_asus_X01BD.git device/asus/X01BD
cd device/asus/X01BD
echo add_lunch_combo omni_X01BD-eng>vendorsetup.sh
chmod +x vendorsetup.sh
cd ../../..
 . build/envsetup.sh
export ALLOW_MISSING_DEPENDENCIES=true
export OF_DISABLE_MIUI_SPECIFIC_FEATURES="1"
export OF_FLASHLIGHT_ENABLE="0"
export FOX_NOTCH="1"
export FOX_RESET_SETTINGS="1"
export OF_MAINTAINER="legacy07"
export BUILD_TYPE=Stable
export TW_DEVICE_VERSION="R10.0-5"
lunch omni_X01BD-eng
mka recoveryimage

 */
