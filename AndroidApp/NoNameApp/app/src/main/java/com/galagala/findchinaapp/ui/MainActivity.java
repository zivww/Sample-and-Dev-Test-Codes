package com.galagala.findchinaapp.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender.SendIntentException;
import android.content.pm.PackageInfo;
import android.content.res.Resources.NotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.text.HtmlCompat;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.galagala.findchinaapp.AppController;
import com.galagala.findchinaapp.R;
import com.galagala.findchinaapp.adapter.ItemlistAdapter;
import com.galagala.findchinaapp.listener.OnItemClickListener;
import com.galagala.findchinaapp.wrapper.AppInfo;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallState;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.OnSuccessListener;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnItemClickListener {
    private final int RC_APP_UPDATE = 201;
    /* access modifiers changed from: private */
    public List<AppInfo> appInfos = new ArrayList();
    /* access modifiers changed from: private */
    public TextView app_found_count;
    /* access modifiers changed from: private */
    public ImageView doneIcon;
    /* access modifiers changed from: private */
    public ImageView dragonIcon;
    InstallStateUpdatedListener installStateUpdatedListener = new InstallStateUpdatedListener() {
        public void onStateUpdate(InstallState installState) {
            try {
                if (installState.installStatus() == InstallStatus.DOWNLOADED) {
                    MainActivity.this.popupSnackbarForCompleteUpdate();
                } else if (installState.installStatus() != InstallStatus.INSTALLED) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("InstallStateUpdatedListener: state: ");
                    sb.append(installState.installStatus());
                    Log.i("MainActivity", sb.toString());
                } else if (MainActivity.this.mAppUpdateManager != null) {
                    MainActivity.this.mAppUpdateManager.unregisterListener(MainActivity.this.installStateUpdatedListener);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    };
    private boolean isDeleteClick;
    /* access modifiers changed from: private */
    public ItemlistAdapter itemlistAdapter;
    /* access modifiers changed from: private */
    public RelativeLayout listLayout;
    AppUpdateManager mAppUpdateManager;
    /* access modifiers changed from: private */
    public RelativeLayout noappsfoundLayout;
    /* access modifiers changed from: private */
    public ProgressBar progressBar;
    private RecyclerView recycler_view;
    private Button rescan_now;
    Button scan_now;
    /* access modifiers changed from: private */
    public RelativeLayout scan_ui;

    class AppUninstalledreceiver extends BroadcastReceiver {
        AppUninstalledreceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            MainActivity.this.getApps();
        }
    }

    class GetAppsAsync extends AsyncTask<Void, Void, List<AppInfo>> {
        GetAppsAsync() {
        }

        /* access modifiers changed from: protected */
        public void onPreExecute() {
            super.onPreExecute();
            MainActivity.this.progressBar.setVisibility(View.VISIBLE);
            MainActivity.this.scan_ui.setVisibility(View.GONE);
        }

        /* access modifiers changed from: protected */
        public List<AppInfo> doInBackground(Void... voidArr) {
            return AppController.getDbHelper().isExist(MainActivity.this.getInstalledApps(), MainActivity.this.appInfos);
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(List<AppInfo> list) {
            super.onPostExecute(list);
            MainActivity.this.progressBar.setVisibility(View.GONE);
            MainActivity.this.scan_ui.setVisibility(View.GONE);
            if (list.size() > 0) {
                MainActivity.this.listLayout.setVisibility(View.VISIBLE);
                MainActivity.this.noappsfoundLayout.setVisibility(View.GONE);
                MainActivity.this.app_found_count.setText(HtmlCompat.fromHtml(MainActivity.this.getResources().getString(R.string.app_found_count, new Object[]{Integer.valueOf(list.size())}), HtmlCompat.FROM_HTML_MODE_LEGACY));
            } else {
                MainActivity.this.listLayout.setVisibility(View.GONE);
                MainActivity.this.noappsfoundLayout.setVisibility(View.VISIBLE);
            }
            MainActivity.this.dragonIcon.setVisibility(View.GONE);
            MainActivity.this.doneIcon.setVisibility(View.VISIBLE);
            MainActivity.this.itemlistAdapter.notifyDataSetChanged();
            if (list.size() == 0) {
                Collections.sort(MainActivity.this.appInfos, new Comparator<AppInfo>() {
                    public int compare(AppInfo appInfo, AppInfo appInfo2) {
                        return appInfo.appName.compareToIgnoreCase(appInfo2.appName);
                    }
                });
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView((int) R.layout.activity_main);
        getSupportActionBar().setHomeAsUpIndicator((int) R.mipmap.ic_launcher);
        this.progressBar = (ProgressBar) findViewById(R.id.progressBar);
        this.dragonIcon = (ImageView) findViewById(R.id.dragonIcon);
        this.doneIcon = (ImageView) findViewById(R.id.doneIcon);
        this.scan_now = (Button) findViewById(R.id.scan_now);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        this.listLayout = (RelativeLayout) findViewById(R.id.listLayout);
        this.scan_ui = (RelativeLayout) findViewById(R.id.scan_ui);
        this.noappsfoundLayout = (RelativeLayout) findViewById(R.id.noappsfoundLayout);
        this.app_found_count = (TextView) findViewById(R.id.app_found_count);
        this.rescan_now = (Button) findViewById(R.id.rescan_now);
        this.itemlistAdapter = new ItemlistAdapter(this, this.appInfos, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(this.itemlistAdapter);
        ((TextView) findViewById(R.id.scan_now_txt)).setText(HtmlCompat.fromHtml(getString(R.string.clickscantext),HtmlCompat.FROM_HTML_MODE_LEGACY));
        this.scan_now.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.getApps();
            }
        });
        this.rescan_now.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                MainActivity.this.getApps();
            }
        });
        checkAppUpdate();
    }

    private void checkAppUpdate() {
        try {
            this.mAppUpdateManager = AppUpdateManagerFactory.create(this);
            this.mAppUpdateManager.registerListener(this.installStateUpdatedListener);
            this.mAppUpdateManager.getAppUpdateInfo().addOnSuccessListener(new OnSuccessListener<AppUpdateInfo>() {
                public void onSuccess(AppUpdateInfo appUpdateInfo) {
                    if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE && appUpdateInfo.isUpdateTypeAllowed( AppUpdateType.FLEXIBLE)) {
                        try {
                            MainActivity.this.mAppUpdateManager.startUpdateFlowForResult(appUpdateInfo, AppUpdateType.FLEXIBLE, MainActivity.this, 201);
                        } catch (SendIntentException e) {
                            e.printStackTrace();
                        }
                    } else if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                        MainActivity.this.popupSnackbarForCompleteUpdate();
                    } else {
                        Log.e("MainActivity", "checkForAppUpdateAvailability: something else");
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public void onActivityResult(int i, int i2, Intent intent) {
        super.onActivityResult(i, i2, intent);
        if (i == 201 && i2 != -1) {
            Log.e("onActivityResult", "onActivityResult: app download failed");
        }
    }

    /* access modifiers changed from: private */
    public void popupSnackbarForCompleteUpdate() {
        try {
            Snackbar make = Snackbar.make(findViewById(R.id.scan_ui), (CharSequence) "An update has just been downloaded.", BaseTransientBottomBar.LENGTH_INDEFINITE);
            make.setAction((CharSequence) "RESTART", (OnClickListener) new OnClickListener() {
                public void onClick(View view) {
                    if (MainActivity.this.mAppUpdateManager != null) {
                        MainActivity.this.mAppUpdateManager.completeUpdate();
                    }
                }
            });
            make.setActionTextColor(getResources().getColor(R.color.green));
            make.show();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: private */
    public void getApps() {
        new GetAppsAsync().execute(new Void[0]);
    }

    /* access modifiers changed from: protected */
    public void onResume() {
        super.onResume();
        if (this.isDeleteClick) {
            new Handler().postDelayed(new Runnable() {
                public void run() {
                    MainActivity.this.getApps();
                }
            }, 1500);
        }
        this.isDeleteClick = false;
    }

    public void onItemClick(int i) {
        this.isDeleteClick = true;
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void openUrl(String str) {
        startActivity(new Intent("android.intent.action.VIEW", Uri.parse(str)));
    }

    private boolean isSystemPackage(PackageInfo packageInfo) {
        return (packageInfo.applicationInfo.flags & 1) != 0;
    }

    /* access modifiers changed from: private */
    public List<AppInfo> getInstalledApps() {
        ArrayList arrayList = new ArrayList();
        List installedPackages = getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < installedPackages.size(); i++) {
            PackageInfo packageInfo = (PackageInfo) installedPackages.get(i);
            if (!isSystemPackage(packageInfo)) {
                AppInfo appInfo = new AppInfo();
                appInfo.appName = packageInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                appInfo.packageName = packageInfo.packageName;
                appInfo.versionName = packageInfo.versionName;
                appInfo.versionCode = packageInfo.versionCode;
                appInfo.icon = packageInfo.applicationInfo.loadIcon(getPackageManager());
                long length = new File(packageInfo.applicationInfo.publicSourceDir).length() / 1048576;
                StringBuilder sb = new StringBuilder();
                sb.append(length);
                sb.append(" MB");
                appInfo.size = sb.toString();
                arrayList.add(appInfo);
            }
        }
        return arrayList;
    }
}
