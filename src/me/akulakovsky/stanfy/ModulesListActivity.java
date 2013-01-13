package me.akulakovsky.stanfy;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.actionbarsherlock.app.SherlockActivity;

import java.util.List;

public class ModulesListActivity extends SherlockActivity {

    Button btnGit;
    Button btnSpace;
    TextView tv;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.module_list);
        setTitle("Modules");
        btnGit = (Button) findViewById(R.id.module_git);
        btnSpace = (Button) findViewById(R.id.module_space);
        tv = (TextView) findViewById(R.id.modules_none);
        setListeners();
    }

    @Override
    protected void onResume() {
        super.onResume();
        btnGit.setVisibility(View.GONE);
        btnSpace.setVisibility(View.GONE);
        tv.setVisibility(View.VISIBLE);
        findModules();
    }

    private void findModules(){
        final PackageManager pm = getApplicationContext().getPackageManager();
        List<PackageInfo> packageInfoList = pm.getInstalledPackages(PackageManager.GET_ACTIVITIES);
        int count = 0;
        for (PackageInfo packageInfo: packageInfoList){
            if (packageInfo.packageName.equals(Constants.MODULE_GIT)){
                btnGit.setVisibility(View.VISIBLE);
                count++;
            } else if (packageInfo.packageName.equals(Constants.MODULE_SPACE)){
                btnSpace.setVisibility(View.VISIBLE);
                count++;
            }
        }

        if (count != 0){
            tv.setVisibility(View.GONE);
        }
    }

    private void setListeners(){
        btnGit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("me.akulakovsky.module.stanfy.github.SHOW"));
            }
        });

        btnSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent("me.akulakovsky.stanfy.graph.SHOW"));
            }
        });
    }
}