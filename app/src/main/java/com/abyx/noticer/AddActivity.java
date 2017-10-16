package com.abyx.noticer;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

/**
 * This activity is the first activity that's launched when new data has to be added.
 * Basic information such as the occasion and price are asked for here.
 */
public class AddActivity extends AppCompatActivity {
    private EditText occasionLabel;
    private EditText priceLabel;
    private EditText locationLabel;

    public static int REQUEST_DATE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        occasionLabel = findViewById(R.id.occasionLabel);
        locationLabel = findViewById(R.id.locationLabel);
        priceLabel = findViewById(R.id.priceLabel);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add, menu);
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
            boolean check = true;
            //First we check whether all fields are filled
            if (occasionLabel.getText().toString().equals("")){
                occasionLabel.setError(getString(R.string.empty_field_error));
                check = false;
            }

            if(priceLabel.getText().toString().equals("")){
                priceLabel.setError(getString(R.string.empty_field_error));
                check = false;
            }

            if (!check){
                return true;
            }

            Intent temp = new Intent(AddActivity.this, DateActivity.class);
            temp.putExtra("OCCASION", occasionLabel.getText().toString());
            temp.putExtra("LOCATION", locationLabel.getText().toString());
            try {
                temp.putExtra("PRICE", Double.parseDouble(priceLabel.getText().toString()));
            } catch (NumberFormatException e){
                priceLabel.setError(getString(R.string.not_a_number_error));
                return true;
            }

            startActivityForResult(temp, REQUEST_DATE);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_DATE && resultCode == RESULT_OK) {
            IO io = new IO(this);
            io.saveOne((Content) data.getParcelableExtra("DATA"));
            finish();
        }
    }

}
