
package com.v380.tv;

import android.app.Activity;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.KeyEvent;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MainActivity extends Activity {

    LinearLayout mainLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(Gravity.CENTER);
        mainLayout.setPadding(40, 30, 40, 30);
        mainLayout.setBackgroundColor(Color.rgb(12, 18, 32));

        TextView title = new TextView(this);
        title.setText("V380 TV");
        title.setTextSize(38);
        title.setTextColor(Color.WHITE);
        title.setGravity(Gravity.CENTER);

        TextView subtitle = new TextView(this);
        subtitle.setText("SECURITY CAMERA SYSTEM");
        subtitle.setTextSize(16);
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setGravity(Gravity.CENTER);

        TextView cameras = new TextView(this);
        cameras.setText("KAMERAT E MIA");
        cameras.setTextSize(24);
        cameras.setTextColor(Color.WHITE);
        cameras.setGravity(Gravity.CENTER);
        cameras.setPadding(20, 40, 20, 40);

        TextView status = new TextView(this);
        status.setText("Asnje kamere nuk eshte lidhur ende.");
        status.setTextSize(18);
        status.setTextColor(Color.GRAY);
        status.setGravity(Gravity.CENTER);

        mainLayout.addView(title);
        mainLayout.addView(subtitle);
        mainLayout.addView(cameras);
        mainLayout.addView(status);

        setContentView(mainLayout);
    }
}
