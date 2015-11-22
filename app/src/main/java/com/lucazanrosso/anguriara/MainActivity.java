package com.lucazanrosso.anguriara;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.support.v7.widget.Toolbar;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    public static Toolbar toolbar;

    private static String[] menu;
    private int[] icons = {R.drawable.ic_today_black_24dp,
            R.drawable.ic_place_black_24dp,
            R.drawable.ic_people_black_24dp,
            R.drawable.ic_store_black_24dp,
            R.drawable.ic_settings_black_24dp};
    private int drawerHeaderImage = R.drawable.logo;
    private Integer[] drawerDividersPosition = {5};
    private Fragment[] fragments = {null,
            new CalendarFragment(),
            new CalendarFragment(),
            new CalendarFragment(),
            new CalendarFragment(),
            null,
            new SettingsFragment()};


    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout mDrawer;
    ActionBarDrawerToggle mDrawerToggle;

    private String fileName = "anguriara.ser";
    public static LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> calendar = new LinkedHashMap<>();
    final static int YEAR = 2015;
    final static int ANGURIARA_NUMBER_OF_DAYS = 31;
    private int[] anguriaraMonths;
    private int[] anguriaraDaysOfMonth;
    private String[] dayEvents;
    private String[] dayEventsDetails;
    private String[] dayFoods;
    private String[] dayOpeningTimes;

    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private boolean alarmIsSet;
    private PendingIntent notificationPendingIntent;
    private AlarmManager notificationAlarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        MainActivity.toolbar = (Toolbar) findViewById(R.id.toolbar);
        MainActivity.toolbar.setTitle(getResources().getString(R.string.calendar));
        setSupportActionBar(toolbar);

        menu = getResources().getStringArray(R.array.menu);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mAdapter = new MyAdapter(menu, icons, drawerHeaderImage, drawerDividersPosition);
        mRecyclerView.setAdapter(mAdapter);

        final GestureDetector mGestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });
        mRecyclerView.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
                View child = rv.findChildViewUnder(e.getX(), e.getY());

                if (child != null && mGestureDetector.onTouchEvent(e)) {

                    if (fragments[rv.getChildAdapterPosition(child)] != null) {
                        mDrawer.closeDrawers();
                        Fragment fragment = fragments[rv.getChildAdapterPosition(child)];
                        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                        transaction.replace(R.id.frame_container, fragment);
                        transaction.addToBackStack("secondary");
                        transaction.commit();
                        return true;
                    }
                }
                return false;
            }

            @Override
            public void onTouchEvent(RecyclerView rv, MotionEvent e) {

            }

            @Override
            public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

            }
        });

        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
            }
        };

        mDrawer.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

        this.anguriaraMonths = getResources().getIntArray(R.array.anguriara_months);
        this.anguriaraDaysOfMonth = getResources().getIntArray(R.array.anguriara_days_of_month);
        this.dayEvents = getResources().getStringArray(R.array.day_events);
        this.dayEventsDetails = getResources().getStringArray(R.array.day_event_details);
        this.dayFoods = getResources().getStringArray(R.array.day_foods);
        this.dayOpeningTimes = getResources().getStringArray(R.array.day_opening_time);

        File file = new File(this.getFilesDir(), this.fileName);
        if (file.exists()) {
            MainActivity.calendar = deserializeCalendar();
        } else {
            MainActivity.calendar = setCalendar();
            serializeCalendar(MainActivity.calendar);
        }

        this.sharedPreferences = getPreferences(Context.MODE_PRIVATE);
        alarmIsSet = false;
        alarmIsSet = sharedPreferences.getBoolean("alarm", alarmIsSet);
        if (!alarmIsSet)
            setAlarm();

        // Check that the activity is using the layout version with
        // the fragment_container FrameLayout
        if (findViewById(R.id.frame_container) != null) {
            // However, if we're being restored from a previous state,
            // then we don't need to do anything and should return or else
            // we could end up with overlapping fragments.
            if (savedInstanceState != null) {
                return;
            }
            CalendarFragment calendarFragment = new CalendarFragment();
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.frame_container, calendarFragment).commit();
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
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_container, settingsFragment);
            transaction.addToBackStack("secondary");
            transaction.commit();
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
            File file = new File(this.getFilesDir(), this.fileName);
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(calendar);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> deserializeCalendar() {
        LinkedHashMap<GregorianCalendar, LinkedHashMap<String, String>> calendar = new LinkedHashMap<>();
        try {
            File file = new File(this.getFilesDir(), this.fileName);
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

    public void setAlarm() {
        int i = 0;
        for (LinkedHashMap.Entry<GregorianCalendar, LinkedHashMap<String, String>> entry : MainActivity.calendar.entrySet()) {
            String notificationText;
            if (!entry.getValue().get("event").isEmpty()) {
                notificationText = entry.getValue().get("event");
                if (!entry.getValue().get("food").isEmpty())
                    notificationText += ", " + entry.getValue().get("food");
                notificationText += " " + getResources().getString(R.string.and_much_more);
            } else
                notificationText = getResources().getString(R.string.open);
            Intent notificationIntent = new Intent(this, MyNotification.class);
            notificationIntent.putExtra("notification_text", notificationText);
            this.notificationPendingIntent = PendingIntent.getBroadcast(this, i, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            this.notificationAlarmManager = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
            Calendar alarmTime = Calendar.getInstance();
            alarmTime.setTimeInMillis(System.currentTimeMillis());
//           alarmTime.set(CalendarFragment.YEAR, entry.getKey().get(Calendar.MONTH), entry.getKey().get(Calendar.DAY_OF_MONTH), 17, 0);
            //Test
            alarmTime.set(2015, 9, 25, 21, i + 10);
            if (!(alarmTime.getTimeInMillis() < System.currentTimeMillis()))
                this.notificationAlarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), this.notificationPendingIntent);
            i++;
        }
        alarmIsSet = true;
        this.editor = sharedPreferences.edit();
        editor.putBoolean("alarm", alarmIsSet);
        editor.apply();

        ComponentName receiver = new ComponentName(this, BootReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public void cancelAlarm() {
        this.notificationAlarmManager.cancel(this.notificationPendingIntent);
        this.alarmIsSet = false;
        this.editor = sharedPreferences.edit();
        editor.putBoolean("alarm", alarmIsSet);
        editor.apply();

        ComponentName receiver = new ComponentName(this, BootReceiver.class);
        PackageManager pm = this.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
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