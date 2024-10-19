package com.example.adimsayar;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor stepCounter;
    private boolean isCounterSensorPresent;
    private int stepCount = 0;
    private TextView stepCountView, calorieBurnedView;
    private EditText weightInput , heightInput;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Layout elemanları
        stepCountView = findViewById(R.id.stepCountView);
        calorieBurnedView = findViewById(R.id.calorieBurnedView);
        weightInput = findViewById(R.id.weightInput);
        heightInput = findViewById(R.id.heightInput);

        //Sensör yöneticisi
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        if(sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!= null) {
            stepCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            isCounterSensorPresent = true;
        }else {
            stepCountView.setText("Adım Sayar Sensörü Yok");
            isCounterSensorPresent = false;
        }


    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if(event.sensor == stepCounter) {
            stepCount = (int) event.values[0];
            stepCountView.setText("Adım sayısı : " + stepCount);
            //calculateCalories();
        }

    }
    private void calculateCalories() {
        //Kullanıcıdan kilo ve boy alma
        String weightStr = weightInput.getText().toString();
        String heightStr = heightInput.getText().toString();

        if(!weightStr.isEmpty()&&!heightStr.isEmpty()) {
            double weight = Double.parseDouble(weightStr);
            double height = Double.parseDouble(heightStr);
            //Kalori hesabı değiştirilebilir
            //Ortalama adım başı kalori 0.04 seçildi
            double caloriesPerStep = 0.04;
            double totalCaloriesBurned = stepCount * caloriesPerStep;
            calorieBurnedView.setText("Yakılan Kalori : " + String.format("%.2f",totalCaloriesBurned)+"kcal");
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //boş bıraktım

    }
    @Override
    protected void onResume() {
        super.onResume();
        if(isCounterSensorPresent) {
            sensorManager.registerListener(this,stepCounter,SensorManager.SENSOR_DELAY_NORMAL);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        if(isCounterSensorPresent) {
            sensorManager.unregisterListener(this);
        }
    }
}