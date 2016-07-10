package com.lucazanrosso.anguriara;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.support.v7.widget.Toolbar;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

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

    public static LinkedHashMap<Calendar, LinkedHashMap<String, String>> calendar = new LinkedHashMap<>();
    public static ArrayList<Calendar> days;
    final static int YEAR = 2016;
    final static int ANGURIARA_NUMBER_OF_DAYS = 31;
    public static String[] daysOfWeek;
    public static String[] months;
    public static Calendar todayInstance = new GregorianCalendar();
    public static Calendar today = new GregorianCalendar(MainActivity.YEAR, todayInstance.get(Calendar.MONTH), todayInstance.get(Calendar.DAY_OF_MONTH));
    public static Calendar badDay;
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

        MainActivity.daysOfWeek = getResources().getStringArray(R.array.days_of_week);
        MainActivity.months = getResources().getStringArray(R.array.months);
        this.anguriaraMonths = getResources().getIntArray(R.array.anguriara_months);
        this.anguriaraDaysOfMonth = getResources().getIntArray(R.array.anguriara_days_of_month);
        this.dayEvents = getResources().getStringArray(R.array.day_events);
        this.dayEventsDetails = getResources().getStringArray(R.array.day_event_details);
        this.dayFoods = getResources().getStringArray(R.array.day_foods);
        this.dayOpeningTimes = getResources().getStringArray(R.array.day_opening_time);

        if (new File(this.getFilesDir(), "anguriara.ser").exists()) {
            MainActivity.calendar = deserializeCalendar(this);
        } else {
            MainActivity.calendar = setCalendar();
            serializeCalendar(MainActivity.calendar);
        }
        days = new ArrayList<>(calendar.keySet());

        if (new File(this.getFilesDir(), "bad_day.ser").exists())
            MainActivity.badDay = deserializeBadDay(this);

        days = new ArrayList<>(calendar.keySet());

        MainActivity.sharedPreferences = getSharedPreferences("PREFERENCES", Context.MODE_PRIVATE);
        boolean firstStart = sharedPreferences.getBoolean("firstStart2016-3", true);
        boolean alarmisSet = sharedPreferences.getBoolean("alarmIsSet", true);
        if (firstStart && alarmisSet) {
            MainActivity.setAlarm(this, MainActivity.calendar, true, false);
            MainActivity.editor = MainActivity.sharedPreferences.edit();
            editor.putBoolean("firstStart2016-3", false).apply();
        }

        if (savedInstanceState == null) {
            CalendarFragment calendarFragment = new CalendarFragment();
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame_container, calendarFragment).commit();
            getFirebaseDatabase(this);
//            setBadDayNotification(this);
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

    public LinkedHashMap<Calendar, LinkedHashMap<String, String>> setCalendar() {
        LinkedHashMap<Calendar, LinkedHashMap<String, String>> calendar = new LinkedHashMap<>();
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

    public void serializeCalendar(LinkedHashMap<Calendar, LinkedHashMap<String, String>> calendar) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(this.getFilesDir(), "anguriara.ser"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(calendar);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static LinkedHashMap<Calendar, LinkedHashMap<String, String>> deserializeCalendar(Context context) {
        LinkedHashMap<Calendar, LinkedHashMap<String, String>> calendar = new LinkedHashMap<>();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(context.getFilesDir(), "anguriara.ser"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            calendar = (LinkedHashMap<Calendar, LinkedHashMap<String, String>>) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return calendar;
    }

//    public static void setBadDayNotification(Context context) {
////        Intent intent = new Intent(context, BadDayNotification.class);
////        context.sendBroadcast(intent);
//        Intent intent = new Intent(context, BadDayIntentService.class);
//        context.startService(intent);
//    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        // Register for the particular broadcast based on ACTION string
//        IntentFilter filter = new IntentFilter(MyTestService.ACTION);
//        LocalBroadcastManager.getInstance(this).registerReceiver(testReceiver, filter);
//        // or `registerReceiver(testReceiver, filter)` for a normal broadcast
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        // Unregister the listener when the application is paused
//        LocalBroadcastManager.getInstance(this).unregisterReceiver(testReceiver);
//        // or `unregisterReceiver(testReceiver)` for a normal broadcast
//    }
//
//    private BroadcastReceiver testReceiver = new BroadcastReceiver() {
//        @Override
//        public void onReceive(Context context, Intent intent) {
//            Toast.makeText(MainActivity.this, "Ciao", Toast.LENGTH_SHORT).show();
//        }
//    };

    public void getFirebaseDatabase(final Context context) {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("test");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
//                if (dataSnapshot.child("test").getValue(Boolean.class)) {
//                    NotificationCompat.Builder mBuilder =
//                            new NotificationCompat.Builder(context)
//                                    .setSmallIcon(R.drawable.notification)
//                                    .setContentTitle(context.getResources().getString(R.string.this_evening))
//                                    .setContentText(context.getResources().getString(R.string.bad_weather));
//                    Intent resultIntent = new Intent(context, MyNotification.class);
//                    TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
//                    stackBuilder.addParentStack(MainActivity.class);
//                    stackBuilder.addNextIntent(resultIntent);
//                    PendingIntent resultPendingIntent =
//                            stackBuilder.getPendingIntent(
//                                    0,
//                                    PendingIntent.FLAG_UPDATE_CURRENT
//                            );
//                    mBuilder.setContentIntent(resultPendingIntent);
//                    MainActivity.notificationPendingIntent = PendingIntent.getBroadcast(context, -1, resultIntent, PendingIntent.FLAG_UPDATE_CURRENT);
//                    NotificationManager mNotificationManager =
//                            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                    mNotificationManager.notify(0, mBuilder.build());
//                } else {
                    boolean isBadDay = dataSnapshot.child("bad_weather").getValue(Boolean.class);
                    if (isBadDay) {
                        MainActivity.badDay = new GregorianCalendar(dataSnapshot.child("year").getValue(Integer.class), dataSnapshot.child("month").getValue(Integer.class), dataSnapshot.child("day").getValue(Integer.class));
                        serializeBadDay(context);
                        if (!MainActivity.today.equals(MainActivity.badDay)) {
                            badDay = null;
                            new File(getFilesDir(), "bad_day.ser").delete();
                        }
                    } else if (MainActivity.badDay != null) {
                        badDay = null;
                        new File(getFilesDir(), "bad_day.ser").delete();
                    }
                    CalendarFragment.thisDayText.setText(CalendarFragment.setDateText(MainActivity.today, context));
                    CalendarFragment.thisDayImage.setImageResource(CalendarFragment.setThisDayImage(MainActivity.today));
                }
//            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public static void serializeBadDay(Context context) {
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(new File(context.getFilesDir(), "bad_day.ser"));
            ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
            objectOutputStream.writeObject(MainActivity.badDay);
            objectOutputStream.close();
            fileOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static GregorianCalendar deserializeBadDay(Context context) {
        GregorianCalendar badDay = new GregorianCalendar();
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(context.getFilesDir(), "bad_day.ser"));
            ObjectInputStream objectInputStream = new ObjectInputStream(fileInputStream);
            badDay = (GregorianCalendar) objectInputStream.readObject();
            objectInputStream.close();
            fileInputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return badDay;

    }

    public static void setAlarm(Context context, LinkedHashMap<Calendar, LinkedHashMap<String, String>> calendar, boolean setAlarm, boolean isBootReceiver) {
        int i = 0;
        for (LinkedHashMap.Entry<Calendar, LinkedHashMap<String, String>> entry : calendar.entrySet()) {
            String notificationText = entry.getValue().get("event");
            if (!entry.getValue().get("food").isEmpty())
                notificationText += ". " + context.getResources().getString(R.string.food) + ": " + entry.getValue().get("food");
            Intent notificationIntent = new Intent(context, MyNotification.class);
            notificationIntent.putExtra("notification_text", notificationText);
            MainActivity.notificationPendingIntent = PendingIntent.getBroadcast(context, i, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
            MainActivity.notificationAlarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            if (setAlarm) {
                Calendar alarmTime = Calendar.getInstance();
                alarmTime.setTimeInMillis(System.currentTimeMillis());
                alarmTime.set(MainActivity.YEAR, entry.getKey().get(Calendar.MONTH), entry.getKey().get(Calendar.DAY_OF_MONTH), 17, 0);
                //Test
//                alarmTime.set(2016, 5, 2, 11, i + 20);
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