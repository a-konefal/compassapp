package pl.wsiz.compassapp;

import android.app.Activity;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

public class MainActivity extends Activity implements SensorEventListener {

    // zdefiniowanie obrazka kompasu
    private ImageView image;

    // zapisanie kątu obrazu kompasu
    private int currentDegree = 0;

    // sensormanager urzadzenia
    private SensorManager mSensorManager;

    TextView heading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // obrazek kompasu
        image = (ImageView) findViewById(R.id.imageViewCompass);

        // TextView wskazujace uzytkownikowi kierunek oraz stopnie
        heading = (TextView) findViewById(R.id.heading);

        // inicjalizacja mozliwosci czujnika w androidzie poprzez sensormanager
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();

        // zarejestrowany listener z czujnika orientacji systemu
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION),
                SensorManager.SENSOR_DELAY_GAME);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // zatrzymanie listenera aby nie tracic baterii
        mSensorManager.unregisterListener(this);
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        // obrót kątu w okół osi Z
        int degree = Math.round(event.values[0]);

        heading.setText(getCurrentHeading(degree)+" (" + Integer.toString(degree) + "\u00B0" + ")");

        // utworzenie obrotu animacji poprzez odwrocenie stopni
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // jak dlugo bedzie trwala animacja
        ra.setDuration(210);

        // ustawienie animacji po zakończeniu
        ra.setFillAfter(true);

        // Rozpoczecie animacji
        image.startAnimation(ra);
        currentDegree = -degree;

    }
    public String getCurrentHeading(int degree) {
        if (degree >= 293 && degree <338 )
         return "PN-Z" ;
        else if (degree >= 23 && degree <67 )
            return "PN-W" ;
        else if (degree >= 67 && degree <112 )
            return "W" ;
        else if (degree >= 112 && degree <158 )
            return "PD-W" ;
        else if (degree >= 158 && degree <203)
            return "PD" ;
        else if (degree >= 203 && degree <248 )
            return "PD-Z" ;
        else if (degree >= 248 && degree <293 )
            return "Z" ;
        else
            return "PN" ;
    }
    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }
}