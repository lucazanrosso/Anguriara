package com.lucazanrosso.anguriara;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    public static Toolbar toolbar;

    DrawerLayout drawerLayout;
    NavigationView navigationView;

    public static LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> calendar = new LinkedHashMap<>();
    public static ArrayList<GregorianCalendar> days;
    final static int YEAR = 2016;
    final static int ANGURIARA_NUMBER_OF_DAYS = 31;
    private int[] anguriaraMonths;
    private int[] anguriaraDaysOfMonth;
    private String[] dayEvents;
    private String[] dayEventsDetails;
    private String[] dayFoods;
    private String[] dayOpeningTimes;

    public static SharedPreferences sharedPreferences;
    private static SharedPreferences.Editor editor;
    private static PendingIntent notificationPendingIntent;
    private static AlarmManager notificationAlarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        MainActivity.toolbar = (Toolbar) findViewById(R.id.toolbar);
        MainActivity.toolbar.setTitle(getResources().getString(R.string.calendar));
        setSupportActionBar(toolbar);

        this.drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerLayoutToggle = new ActionBarDrawerToggle(this, this.drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };
        this.drawerLayout.addDrawerListener(drawerLayoutToggle);
        this.drawerLayout.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        drawerLayoutToggle.syncState();

        this.navigationView = (NavigationView) findViewById(R.id.navigation_view);
        this.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                final int itemId = item.getItemId();
                final Fragment fragment;
                switch (itemId) {
                    case R.id.calendar:
                        fragment = new CalendarFragment();
                        break;
                    case R.id.pravolley:
                        fragment = new PravolleyFragment();
                        break;
                    case R.id.where_we_are:
                        fragment = new WhereWeAreFragment();
                        break;
                    case R.id.who_we_are:
                        fragment = new WhoWeAreFragment();
                        break;
                    case R.id.settings:
                        fragment = new SettingsFragment();
                        break;
                    default:
                        throw new IllegalArgumentException("No Fragment for the given item");
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, fragment).addToBackStack("secondary").commit();
                drawerLayout.closeDrawer(navigationView);
                return false;
            }
        });

        this.anguriaraMonths = getResources().getIntArray(R.array.anguriara_months);
        this.anguriaraDaysOfMonth = getResources().getIntArray(R.array.anguriara_days_of_month);
        this.dayEvents = getResources().getStringArray(R.array.day_events);
        this.dayEventsDetails = getResources().getStringArray(R.array.day_event_details);
        this.dayFoods = getResources().getStringArray(R.array.day_foods);
        this.dayOpeningTimes = getResources().getStringArray(R.array.day_opening_time);

        File file = new File(this.getFilesDir(), "anguriara.ser");
        if (file.exists()) {
            MainActivity.calendar = deserializeCalendar(this);
        } else {
            MainActivity.calendar = setCalendar();
            serializeCalendar(MainActivity.calendar);
        }
        days = new ArrayList<>(calendar.keySet());

        MainActivity.sharedPreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        boolean firstStart = sharedPreferences.getBoolean("firstStart2015", true);
        boolean alarmisSet = sharedPreferences.getBoolean("alarmIsSet", true);
        if (firstStart && alarmisSet) {
            MainActivity.setAlarm(this, MainActivity.calendar, true, false);
            MainActivity.editor = MainActivity.sharedPreferences.edit();
            editor.putBoolean("firstStart2015", false).apply();
        }

        if (savedInstanceState == null) {
            CalendarFragment calendarFragment = new CalendarFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, calendarFragment).commit();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            SettingsFragment settingsFragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_container, settingsFragment).addToBackStack("secondary").commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> setCalendar() {
        LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> calendar = new LinkedHashMap<>();
        for (int i = 0; i < ANGURIARA_NUMBER_OF_DAYS; i++) {
            LinkedHashMap<String, String> eveningMap = new LinkedHashMap<>();
            eveningMap.put("event", this.dayEvents[i]);
            eveningMap.put("event_details", this.dayEventsDetails[i]);
            eveningMap.put("food", this.dayFoods[i]);
            eveningMap.put("openingTime", this.dayOpeningTimes[i]);
            calendar.put(new GregorianCalendar(MainActivity.YEAR, this.anguriaraMonths[i], this.anguriaraDaysOfMonth[i]), eveningMap);
        }
        return calendar;
    }

    public void serializeCalendar(LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> calendar) {
        try {
            File file = new File(this.getFilesDir(), "anguriara.ser");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(calendar);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> deserializeCalendar(Context context) {
        LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> calendar = new LinkedHashMap<>();
        try {
            File file = new File(context.getFilesDir(), "anguriara.ser");
            FileInputStream fileInputStream = new FileInputStream(file);
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            calendar = (LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }

    public static void setAlarm(Context context, LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> calendar, boolean setAlarm, boolean isBootReceiver) {
        int i = 0;
        for (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry : calendar.entrySet()) {
            String notificationText;
            if (!entry.getValue().get("event").isEmpty()) {
                notificationText = context.getResources().getString(R.string.event) + ": " + entry.getValue().get("event");
                if (!entry.getValue().get("food").isEmpty())
                    notificationText += ". " + context.getResources().getString(R.string.food) + ": " + entry.getValue().get("food");
            } else
                notificationText = context.getResources().getString(R.string.open);
            Intent notificationIntent = new Intent(context, MyNotification.class);
            notificationIntent.putExtra("notification_text", notificationText);
            MainActivity.notificationPendingIntent = PendingIntent.getBroadcast(context, i, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            MainActivity.notificationAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (setAlarm) {
                Calendar alarmTime = Calendar.getInstance();
                alarmTime.setTimeInMillis(System.currentTimeMillis());
//              alarmTime.set(CalendarFragment.YEAR, entry.getKey().get(Calendar.MONTH), entry.getKey().get(Calendar.DAY_OF_MONTH), 17, 0);
                //Test
                alarmTime.set(2016, 2, i, 18, 0);
                if (!(alarmTime.getTimeInMillis() < System.currentTimeMillis()))
                    MainActivity.notificationAlarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), MainActivity.notificationPendingIntent);
                if (!isBootReceiver) {
                    editor = MainActivity.sharedPreferences.edit();
                    editor.putBoolean("alarmIsSet", true).apply();
                }
            } else {
                MainActivity.notificationAlarmManager.cancel(MainActivity.notificationPendingIntent);
                if (!isBootReceiver) {
                    editor = MainActivity.sharedPreferences.edit();
                    editor.putBoolean("alarmIsSet", false).apply();
                }
            }
            i++;
        }

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    @Override
    public void onBackPressed(){
        Fragment fm = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fm instanceof CalendarFragment)
            finish();
        else if (!(fm instanceof DayFragment))
            getSupportFragmentManager().popBackStack("secondary", FragmentManager.POP_BACK_STACK_INCLUSIVE);
        else
            getSupportFragmentManager().popBackStack();
    }
}