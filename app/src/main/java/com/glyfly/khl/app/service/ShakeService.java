package com.glyfly.khl.app.service;

import android.app.Service;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.Vibrator;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2017/5/18.
 */

public class ShakeService extends Service {

    private static final String TAG = "ShakeService";
    private SensorManager sensorManager;
    private Vibrator vibrator;
    private static final int SENSOR_SHAKE = 10;

    private ShakeListener shakeListener;

    public interface ShakeListener{
        void shaking();
    }

    public void setShakeListener(ShakeListener shakeListener){
        this.shakeListener = shakeListener;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return new MBinder();
    }

    public class MBinder extends Binder {

        public Service getService(){
            return ShakeService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
        sensorManager.registerListener(mShakeListener,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        sensorManager.unregisterListener(mShakeListener);
    }

    private final SensorEventListener mShakeListener = new SensorEventListener() {
        private static final float SENSITIVITY = 30;
        private static final int BUFFER = 5;
        private float[] gravity = new float[3];
        private float average = 0;
        private int fill = 0;

        @Override
        public void onAccuracyChanged(Sensor sensor, int acc) {
        }

        public void onSensorChanged(SensorEvent event) {
            final float alpha = 0.8F;

            for (int i = 0; i < 3; i++) {
                gravity[i] = alpha * gravity[i] + (1 - alpha) * event.values[i];
            }

            float x = event.values[0] - gravity[0];
            float y = event.values[1] - gravity[1];
            float z = event.values[2] - gravity[2];

            if (fill <= BUFFER) {
                average += Math.abs(x) + Math.abs(y) + Math.abs(z);
                fill++;
            } else {
                if (average / BUFFER >= SENSITIVITY) {
                    if (shakeListener != null){
                        shakeListener.shaking();
                    }
                }
                average = 0;
                fill = 0;
            }
        }
    };

}
