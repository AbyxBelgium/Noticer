package com.abyx.noticer;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


public class MainActivity extends AppCompatActivity implements DialogListener {
    private ListView mainList;
    private TextView totalExpensesLabel;

    private RowAdapter adapter;
    private List<Content> data;
    private Currency totalExpenses = new Currency(0.0);
    private AlarmManager alarmMgr;
    private SharedPreferences prefs;
    private MenuItem sortButton;
    private boolean sortedDescending;
    private final String sortedString = "sorted_descending";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainList = (ListView) findViewById(R.id.mainList);
        totalExpensesLabel = (TextView) findViewById(R.id.totalExpensesLabel);

        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        data = new ArrayList<>();
        adapter = new RowAdapter(this, data);
        mainList.setAdapter(adapter);

        adapter.refresh(data);

        mainList.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        mainList.setMultiChoiceModeListener(new MultiChoiceListViewListener<>(data, this));

        loadData();
        setAlarms();
        refreshPriceField();

        //The sortedDescending value from the last time the user used this app
        SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
        sortedDescending = sharedPref.getBoolean(sortedString, true);
        sort(sortedDescending);
        adapter.refresh(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        sortButton = menu.findItem(R.id.action_sort);
        setSortIcon(sortedDescending);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_about){
            Intent intent = new Intent(this, AboutActivity.class);
            startActivity(intent);
            return true;
        } else if (id == R.id.action_remove_all) {
            RemoveAllDialog temp = new RemoveAllDialog();
            temp.show(getFragmentManager(), "SOME_TAG");
        } else if (id == R.id.action_sort){
            reverse();
            sortedDescending = !sortedDescending;
            setSortIcon(sortedDescending);
            adapter.refresh(data);
            //Save the sort preference of the user, so he doesn't has to choose this everytime
            //the app starts
            SharedPreferences sharedPref = getPreferences(Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean(sortedString, sortedDescending);
            editor.apply();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * Add new object to the apps memory
     * @param temp The object that should be added
     */
    private void addData(Content temp){
        data.add(temp);
        totalExpenses.add(temp.getExpenses());
        setTotalExpensesLabel();
        adapter.refresh(data);
    }

    /**
     * Method used by the floating action button to add new user data to the app.
     * This method launches a new activity where the user can enter new data.
     * @param view
     */
    public void newData(View view){
        Intent temp = new Intent(MainActivity.this, AddActivity.class);
        startActivity(temp);
    }

    @Override
    public void onPause(){
        //Save the app's data everytime this activity closes
        IO temp = new IO(this);
        temp.save(data);
        super.onPause();
    }

    @Override
    public void onResume(){
        //Load previously saved data from the device's internal storage
        loadData();
        setAlarms();
        super.onResume();
    }

    /**
     * This function reads all data that's been saved before from disk and loads them into
     * the apps memory. It also refreshes the ListView so that its contents are updated
     */
    private void loadData(){
        data.clear();
        totalExpenses.setPrice(0);
        IO temp = new IO(this);
        for(Content content: temp.load()){
            addData(content);
        }
        sort(sortedDescending);
        adapter.refresh(data);
    }

    /**
     * This function recalculates the total amount of expenses. Sometimes is necessary to
     * recalculate the total price because of the removal of one or more data objects.
     */
    public void refreshPriceField(){
        totalExpenses.setPrice(0.0);
        for (Content cnt: data){
            totalExpenses.add(cnt.getExpenses());
        }
        setTotalExpensesLabel();
    }

    private void setTotalExpensesLabel(){
        totalExpensesLabel.setText(totalExpenses.toString());
    }

    private void setAlarms(){
        alarmMgr = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, 0, intent, 0);

        // Cancel previously set alarms
        alarmMgr.cancel(alarmIntent);

        if (prefs.getBoolean("notifications_enabled", false)) {
            // Set the alarm to start at the specified time
            Calendar c = Calendar.getInstance();

            String selectedHour = prefs.getString("alarm_time", "14:00");
            int hours = Integer.parseInt(selectedHour.split(":")[0]);
            int minutes = Integer.parseInt(selectedHour.split(":")[1]);

            c.set(Calendar.HOUR_OF_DAY, hours);
            c.set(Calendar.MINUTE, minutes);

            //Set the alarm to tomorrow otherwise it would be triggered right now
            if (c.getTime().before(Calendar.getInstance().getTime())) {
                c.add(Calendar.DATE, 1);
            }

            // Set a new alarm that fires every day at the desired hour, but only when the user
            // enabled alarms in this apps settings.
            alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, alarmIntent);
        }
    }

    /**
     * Change the sort icon placed in the action bar according to the user's preferences
     * @param descending
     */
    private void setSortIcon(boolean descending){
        if (descending){
            sortButton.setIcon(R.drawable.ic_sort_descending_white_48dp);
        } else {
            sortButton.setIcon(R.drawable.ic_sort_ascending_white_48dp);
        }
    }

    /**
     * This method sorts all data that's currently available. When sortedDescending is
     * true, it's sorted from A-Z and otherwise it's sorted from Z-A.
     *
     * This function runs in O(n*log(n))-time
     *
     * @param sortedDescending
     */
    private void sort(boolean sortedDescending){
        if (!prefs.getBoolean("sort_by_date", false)) {
            if (sortedDescending) {
                Collections.sort(data, new Comparator<Content>() {
                    public int compare(Content cnt1, Content cnt2) {
                        return cnt1.getLocation().toLowerCase().compareTo(cnt2.getLocation().toLowerCase());
                    }
                });
            } else {
                Collections.sort(data, new Comparator<Content>() {
                    public int compare(Content cnt1, Content cnt2) {
                        return cnt2.getLocation().toLowerCase().compareTo(cnt1.getLocation().toLowerCase());
                    }
                });
            }
        } else {
            if (sortedDescending) {
                Collections.sort(data, new Comparator<Content>() {
                    public int compare(Content cnt1, Content cnt2) {
                        return cnt1.getDate().compareTo(cnt2.getDate());
                    }
                });
            } else {
                Collections.sort(data, new Comparator<Content>() {
                    public int compare(Content cnt1, Content cnt2) {
                        return cnt2.getDate().compareTo(cnt1.getDate());
                    }
                });
            }
        }
        adapter.refresh(data);
    }

    /**
     * This function reverses the order of all data that's currently available. Do not resort
     * data just to change the sort preference (descending - ascending) as reversing is
     * faster.
     *
     * This function runs in O(n)-time
     */
    private void reverse(){
        //Runs in O(n) time. We can do this because all data is sorted alphabetically by default
        Collections.reverse(data);
        adapter.refresh(data);
    }

    @Override
    public void onPositiveClick(){
        data.clear();
        adapter.refresh(data);
        refreshPriceField();
    }

    @Override
    public void onNegativeClick(){
        //Do nothing!!!
    }
}
