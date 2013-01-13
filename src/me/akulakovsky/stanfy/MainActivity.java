package me.akulakovsky.stanfy;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Toast;
import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockListActivity;
import com.actionbarsherlock.view.Menu;
import me.akulakovsky.stanfy.adapters.ProcessAdapter;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends SherlockListActivity {

    public static final String LOG_TAG = "MainActivity";

    private ProcessAdapter adapter;
    private Timer myTimer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        adapter = new ProcessAdapter(this);
        getListView().setAdapter(adapter);
        getListView().setCacheColorHint(Color.TRANSPARENT);
        getListView().setDivider(getResources().getDrawable(android.R.color.darker_gray));
        getListView().setDividerHeight(1);
        registerForContextMenu(getListView());
        myTimer = new Timer();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        com.actionbarsherlock.view.MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.options_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(com.actionbarsherlock.view.MenuItem item) {
        startActivity(new Intent(MainActivity.this, ModulesListActivity.class));
        return super.onOptionsItemSelected(item);    //To change body of overridden methods use File | Settings | File Templates.
    }

    @Override
    protected void onResume() {
        super.onResume();
        startRefreshing();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopRefresing();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        getMenuInflater().inflate(R.menu.kill_menu, menu);
        menu.setHeaderTitle("Select action");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        ApplicationInfo applicationInfo = adapter.getItem(info.position);
        Toast.makeText(MainActivity.this, applicationInfo.processName + " killed.", Toast.LENGTH_SHORT).show();

        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        mActivityManager.killBackgroundProcesses(applicationInfo.packageName);
        refresh();
        return super.onContextItemSelected(item);
    }

    private void startRefreshing(){
        myTimer.scheduleAtFixedRate(new MyTimerTask(), 0, 5000);
    }

    private void stopRefresing(){
        myTimer.cancel();
    }

    private void refresh(){
        //Toast.makeText(this, "Refreshed", Toast.LENGTH_SHORT).show();
        adapter.clear();
        final PackageManager pm = getApplicationContext().getPackageManager();
        ActivityManager activityManager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
        for(ActivityManager.RunningAppProcessInfo processInfo: procInfos){
            try {
                if((pm.getApplicationInfo(processInfo.processName, 0).flags & ApplicationInfo.FLAG_SYSTEM) == 1
                        || processInfo.processName.equals("me.akulakovsky.stanfy")){
                    continue;
                }
                adapter.add(pm.getApplicationInfo(processInfo.processName, 0));
            } catch (PackageManager.NameNotFoundException e) {
                Log.d(LOG_TAG, "Failed to add to adapter! " + e);
            }
        }
    }

    private class MyTimerTask extends TimerTask {
        @Override
        public void run() {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    refresh();
                }
            });
        }
    }
}
