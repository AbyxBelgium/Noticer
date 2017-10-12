package com.abyx.noticer;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.DatePicker;

import com.amulyakhare.textdrawable.util.ColorGenerator;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;


public class DateActivity extends AppCompatActivity {
    private DatePicker datePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_date);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_date, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_done) {
            Intent intent = getIntent();
            String occasion = intent.getStringExtra("OCCASION");
            String location = intent.getStringExtra("LOCATION");
            Double price = intent.getDoubleExtra("PRICE", 0.0);
            Calendar cal = Calendar.getInstance();
            cal.set(datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());
            ColorGenerator gen = ColorGenerator.MATERIAL;
            Content created = new Content(occasion, location, new Currency(price), cal, gen.getRandomColor());
            Intent temp = new Intent();
            temp.putExtra("DATA", created);
            setResult(RESULT_OK, temp);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
