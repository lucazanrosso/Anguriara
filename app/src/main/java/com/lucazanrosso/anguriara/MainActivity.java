package com.lucazanrosso.anguriara;

import android.app.AlarmManager;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;

import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedHashMap;

public class MainActivity extends AppCompatActivity {

    DrawerLayout drawerLayout;
    NavigationView navigationView;
    boolean isDrawerLocked = true;
    static int previousFragment;

    public static LinkedHashMap<Calendar, LinkedHashMap<String, Object>> calendar = new LinkedHashMap<>();
    public static ArrayList<Calendar> days;
    public final static int YEAR = 2017;
    public static String[] daysOfWeek;
    public static String[] months;
    public static Calendar todayInstance = new GregorianCalendar();
//    public static Calendar todayInstance = new GregorianCalendar(2017, 7, 10);
    public static Calendar today = new GregorianCalendar(todayInstance.get(Calendar.YEAR), todayInstance.get(Calendar.MONTH), todayInstance.get(Calendar.DAY_OF_MONTH));
    public static Calendar badDay;

    public static SharedPreferences sharedPreferences;
    private static PendingIntent notificationPendingIntent;
    private static AlarmManager notificationAlarmManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getResources().getString(R.string.calendar));
        setSupportActionBar(toolbar);

        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);
        float density = getResources().getDisplayMetrics().density;
        float width = outMetrics.widthPixels/density;
        if (width < 960) {
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
            isDrawerLocked = false;
        }

        this.navigationView = (NavigationView) findViewById(R.id.navigation_view);
        this.navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                final int itemId = item.getItemId();
                final Fragment fragment;
                switch (itemId) {
                    case R.id.home:
                        fragment = new HomeFragment();
                        break;
                    case R.id.calendar:
                        fragment = new CalendarFragment();
                        break;
                    case R.id.pravolley:
                        fragment = new PravolleyFragment();
                        break;
                    case R.id.news:
                        fragment = new NewsFragment();
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
                if (previousFragment != itemId) {
                    getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation, R.anim.enter_animation, R.anim.exit_animation)
                            .replace(R.id.frame_container, fragment)
                            .addToBackStack("secondary").commit();
                }
                if (! isDrawerLocked)
                    drawerLayout.closeDrawer(navigationView);
                return false;
            }
        });

        MainActivity.daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        MainActivity.months = getResources().getStringArray(R.array.months);
        MainActivity.calendar = setCalendar(this);
        MainActivity.days = new ArrayList<>(calendar.keySet());

        MainActivity.sharedPreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        MainActivity.badDay = new GregorianCalendar(sharedPreferences.getInt("BadWeatherYear", 0), sharedPreferences.getInt("BadWeatherMonth", 0),sharedPreferences.getInt("BadWeatherDay", 0));
        boolean firstStart = sharedPreferences.getBoolean("firstStart2017", true);
        boolean eveningsAlarmIsSet = sharedPreferences.getBoolean("eveningsAlarmIsSet", true);
        boolean firebaseAlarmIsSet = sharedPreferences.getBoolean("firebaseAlarmIsSet", true);
        if (firstStart) {
            if (eveningsAlarmIsSet)
                MainActivity.setEveningsAlarm(this, MainActivity.calendar, true, false);
            if (firebaseAlarmIsSet)
                MainActivity.setFirebaseAlarm(this, true, false);
            MainActivity.sharedPreferences.edit().putBoolean("firstStart2017", false).apply();
        }

        if (savedInstanceState == null) {
            HomeFragment homeFragment = new HomeFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, homeFragment).commit();
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
        if (id == R.id.action_settings && previousFragment != R.id.settings) {
            SettingsFragment settingsFragment = new SettingsFragment();
            getSupportFragmentManager().beginTransaction()
                    .setCustomAnimations(R.anim.enter_animation, R.anim.exit_animation, R.anim.enter_animation, R.anim.exit_animation)
                    .replace(R.id.frame_container, settingsFragment)
                    .addToBackStack("secondary").commit();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public static LinkedHashMap<Calendar, LinkedHashMap<String, Object>> setCalendar(Context context) {
        int[] anguriaraMonths = context.getResources().getIntArray(R.array.anguriara_months);
        int[] anguriaraDaysOfMonth = context.getResources().getIntArray(R.array.anguriara_days_of_month);
        String[] dayEvents = context.getResources().getStringArray(R.array.day_events);
        String[] dayEventsDetails = context.getResources().getStringArray(R.array.day_event_details);
        TypedArray dayEventsImages = context.getResources().obtainTypedArray(R.array.day_event_image);
        String[] dayFoods = context.getResources().getStringArray(R.array.day_foods);
        TypedArray dayFoodsImages = context.getResources().obtainTypedArray(R.array.day_food_image);
        String[] dayOpeningTimes = context.getResources().getStringArray(R.array.day_opening_time);
        LinkedHashMap<Calendar, LinkedHashMap<String, Object>> calendar = new LinkedHashMap<>();
        for (int i = 0; i < dayEvents.length; i++) {
            LinkedHashMap<String, Object> eveningMap = new LinkedHashMap<>();
            eveningMap.put("event", dayEvents[i]);
            eveningMap.put("event_details", dayEventsDetails[i]);
            eveningMap.put("event_image", dayEventsImages.getResourceId(i, 0));
            eveningMap.put("food", dayFoods[i]);
            eveningMap.put("food_image", dayFoodsImages.getResourceId(i, 0));
            eveningMap.put("openingTime", dayOpeningTimes[i]);
            calendar.put(new GregorianCalendar(MainActivity.YEAR, anguriaraMonths[i], anguriaraDaysOfMonth[i]), eveningMap);
        }
        dayEventsImages.recycle();
        dayFoodsImages.recycle();
        return calendar;
    }

    public static void setEveningsAlarm(Context context, LinkedHashMap<Calendar, LinkedHashMap<String, Object>> calendar, boolean setAlarm, boolean isBootReceiver) {
        if (!isBootReceiver)
            MainActivity.sharedPreferences.edit().putBoolean("eveningsAlarmIsSet", setAlarm).apply();
        int i = 0;
        for (LinkedHashMap.Entry<Calendar, LinkedHashMap<String, Object>> entry : calendar.entrySet()) {
            String notificationText = (String) entry.getValue().get("event");
            String notificationFood = (String) entry.getValue().get("food");
            if (! notificationFood.isEmpty())
                notificationText += "\n" + context.getResources().getString(R.string.food) + ": " + notificationFood;
            Intent notificationIntent = new Intent(context, MyNotification.class);
            notificationIntent.putExtra("notification_title", context.getResources().getString(R.string.this_evening));
            notificationIntent.putExtra("notification_text", notificationText);
            MainActivity.notificationPendingIntent = PendingIntent.getBroadcast(context, i, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            MainActivity.notificationAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (setAlarm) {
                Calendar alarmTime = Calendar.getInstance();
                alarmTime.setTimeInMillis(System.currentTimeMillis());
//                alarmTime.set(MainActivity.YEAR, entry.getKey().get(Calendar.MONTH), entry.getKey().get(Calendar.DAY_OF_MONTH), 16, 0);
//                Test
                alarmTime.set(2017, 7, 25, 14, i + 10);
                if (!(alarmTime.getTimeInMillis() < System.currentTimeMillis()))
                    MainActivity.notificationAlarmManager.set(AlarmManager.RTC_WAKEUP, alarmTime.getTimeInMillis(), MainActivity.notificationPendingIntent);
            } else
                MainActivity.notificationAlarmManager.cancel(MainActivity.notificationPendingIntent);
            i++;
        }

        ComponentName receiver = new ComponentName(context, BootReceiver.class);
        PackageManager pm = context.getPackageManager();
        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }

    public static void setFirebaseAlarm (Context context, boolean setAlarm, boolean isBootReceiver) {
        if (!isBootReceiver)
            MainActivity.sharedPreferences.edit().putBoolean("firebaseAlarmIsSet", setAlarm).apply();
//        Intent intent = new Intent(context, NotificationService.class);
        FirebaseJobDispatcher dispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
        if (setAlarm) {
            Job myJob = dispatcher.newJobBuilder()
                    .setService(NotificationJobService.class) // the JobService that will be called
                    .setTag("my-unique-tag")        // uniquely identifies the job
                    .build();
            dispatcher.mustSchedule(myJob);
        } else
            dispatcher.cancel("my-unique-tag");
    }

    @Override
    public void onBackPressed(){
        Fragment fm = getSupportFragmentManager().findFragmentById(R.id.frame_container);
        if (fm instanceof HomeFragment)
            finish();
        else if (fm instanceof DayScreenSlidePagerFragment)
            getSupportFragmentManager().popBackStack();
        else
            getSupportFragmentManager().popBackStack("secondary", FragmentManager.POP_BACK_STACK_INCLUSIVE);
    }
}