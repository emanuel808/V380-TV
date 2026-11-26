
package com.v380.tv;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.view.KeyEvent;
import android.widget.*;
import android.content.SharedPreferences;
import android.text.InputType;

import org.json.JSONArray;
import org.json.JSONObject;

public class MainActivity extends Activity {

    LinearLayout mainLayout;
    LinearLayout cameraList;
    SharedPreferences preferences;

    final int BG = Color.rgb(12, 18, 32);
    final int BLUE = Color.rgb(35, 110, 220);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences = getSharedPreferences("V380_CAMERAS", MODE_PRIVATE);

        buildScreen();
    }

    int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density);
    }

    TextView makeText(String text, int size) {
        TextView view = new TextView(this);
        view.setText(text);
        view.setTextSize(size);
        view.setTextColor(Color.WHITE);
        return view;
    }

    void buildScreen() {

        ScrollView scroll = new ScrollView(this);

        mainLayout = new LinearLayout(this);
        mainLayout.setOrientation(LinearLayout.VERTICAL);
        mainLayout.setGravity(Gravity.CENTER_HORIZONTAL);
        mainLayout.setPadding(dp(30), dp(25), dp(30), dp(30));
        mainLayout.setBackgroundColor(BG);

        TextView title = makeText("V380 TV", 36);
        title.setGravity(Gravity.CENTER);

        TextView subtitle = makeText("SECURITY CAMERA SYSTEM", 14);
        subtitle.setTextColor(Color.LTGRAY);
        subtitle.setGravity(Gravity.CENTER);

        TextView heading = makeText("KAMERAT E MIA", 23);
        heading.setGravity(Gravity.CENTER);
        heading.setPadding(0, dp(30), 0, dp(20));

        Button addButton = new Button(this);
        addButton.setText("+ SHTO KAMERE");
        addButton.setTextSize(18);
        addButton.setBackgroundColor(BLUE);
        addButton.setTextColor(Color.WHITE);
        addButton.setFocusable(true);
        addButton.setOnClickListener(v -> showAddCameraDialog());

        cameraList = new LinearLayout(this);
        cameraList.setOrientation(LinearLayout.VERTICAL);
        cameraList.setPadding(0, dp(20), 0, 0);

        mainLayout.addView(title);
        mainLayout.addView(subtitle);
        mainLayout.addView(heading);
        mainLayout.addView(addButton);

        mainLayout.addView(cameraList);

        scroll.addView(mainLayout);
        setContentView(scroll);

        loadCameras();

        addButton.requestFocus();
    }

    void showAddCameraDialog() {

        LinearLayout form = new LinearLayout(this);
        form.setOrientation(LinearLayout.VERTICAL);
        form.setPadding(dp(20), dp(10), dp(20), 0);

        EditText nameInput = new EditText(this);
        nameInput.setSingleLine(true);
        nameInput.setHint("Emri i kameres");
        nameInput.setTextColor(Color.BLACK);
        nameInput.setHintTextColor(Color.GRAY);

        EditText idInput = new EditText(this);
        idInput.setSingleLine(true);
        idInput.setHint("ID e kameres");
        idInput.setInputType(InputType.TYPE_CLASS_TEXT);
        idInput.setTextColor(Color.BLACK);
        idInput.setHintTextColor(Color.GRAY);

        form.addView(nameInput);
        form.addView(idInput);

        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("SHTO KAMERE")
                .setMessage("Vendos emrin dhe ID e kameres. Nuk nevojitet username ose password.")
                .setView(form)
                .setPositiveButton("SHTO", null)
                .setNegativeButton("ANULO", null)
                .create();

        dialog.setOnShowListener(d -> {

            dialog.getButton(AlertDialog.BUTTON_POSITIVE)
                    .setOnClickListener(v -> {

                String name = nameInput.getText().toString().trim();
                String id = idInput.getText().toString().trim();

                if (name.isEmpty()) {
                    nameInput.setError("Vendos emrin");
                    return;
                }

                if (id.isEmpty()) {
                    idInput.setError("Vendos ID e kameres");
                    return;
                }

                JSONArray cameras = getCameras();

                try {
                    for (int i = 0; i < cameras.length(); i++) {
                        JSONObject camera = cameras.getJSONObject(i);

                        if (camera.getString("id").equalsIgnoreCase(id)) {
                            idInput.setError("Kjo ID ekziston");
                            return;
                        }
                    }

                    JSONObject camera = new JSONObject();
                    camera.put("name", name);
                    camera.put("id", id);

                    cameras.put(camera);

                    saveCameras(cameras);
                    dialog.dismiss();
                    loadCameras();

                    Toast.makeText(this,
                            "Kamera u ruajt me sukses!",
                            Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    Toast.makeText(this,
                            "Gabim gjate ruajtjes",
                            Toast.LENGTH_SHORT).show();
                }
            });
        });

        dialog.show();
    }

    JSONArray getCameras() {
        try {
            return new JSONArray(
                    preferences.getString("camera_list", "[]")
            );
        } catch (Exception e) {
            return new JSONArray();
        }
    }

    void saveCameras(JSONArray cameras) {
        preferences.edit()
                .putString("camera_list", cameras.toString())
                .apply();
    }

    void loadCameras() {

        cameraList.removeAllViews();

        JSONArray cameras = getCameras();

        if (cameras.length() == 0) {
            TextView empty = makeText(
                    "Asnje kamere nuk eshte shtuar ende.",
                    17
            );

            empty.setTextColor(Color.LTGRAY);
            empty.setGravity(Gravity.CENTER);
            empty.setPadding(0, dp(30), 0, dp(30));

            cameraList.addView(empty);
            return;
        }

        for (int i = 0; i < cameras.length(); i++) {

            try {
                JSONObject camera = cameras.getJSONObject(i);

                String name = camera.getString("name");
                String id = camera.getString("id");

                addCameraCard(name, id);

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    void addCameraCard(String name, String id) {

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(15), dp(12), dp(15), dp(12));
        card.setBackgroundColor(Color.rgb(27, 39, 59));

        LinearLayout.LayoutParams cardParams =
                new LinearLayout.LayoutParams(
                        -1, -2
                );

        cardParams.setMargins(0, 0, 0, dp(12));

        TextView cameraName = makeText(name, 20);
        cameraName.setGravity(Gravity.CENTER_VERTICAL);

        TextView cameraId = makeText("ID: " + id, 14);
        cameraId.setTextColor(Color.LTGRAY);
        cameraId.setPadding(0, dp(5), 0, dp(10));

        Button openButton = new Button(this);
        openButton.setText("HAP KAMEREN");
        openButton.setFocusable(true);

        openButton.setOnClickListener(v -> {

            Toast.makeText(this,
                    "ID u gjet. Nevojitet integrimi i lidhjes V380 per pamjen LIVE.",
                    Toast.LENGTH_LONG).show();
        });

        Button deleteButton = new Button(this);
        deleteButton.setText("FSHI KAMEREN");
        deleteButton.setFocusable(true);

        deleteButton.setOnClickListener(v -> {

            new AlertDialog.Builder(this)
                    .setTitle("Fshi kamerën")
                    .setMessage("Deshiron ta fshish kete kamere?")
                    .setPositiveButton("PO", (dialog, which) -> {

                        removeCamera(id);
                        loadCameras();
                    })
                    .setNegativeButton("JO", null)
                    .show();
        });

        card.addView(cameraName);
        card.addView(cameraId);
        card.addView(openButton);
        card.addView(deleteButton);

        cameraList.addView(card, cardParams);
    }

    void removeCamera(String id) {

        JSONArray oldCameras = getCameras();
        JSONArray newCameras = new JSONArray();

        try {
            for (int i = 0; i < oldCameras.length(); i++) {

                JSONObject camera = oldCameras.getJSONObject(i);

                if (!camera.getString("id").equals(id)) {
                    newCameras.put(camera);
                }
            }

            saveCameras(newCameras);

            Toast.makeText(this,
                    "Kamera u fshi!",
                    Toast.LENGTH_SHORT).show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
