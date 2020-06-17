package com.galagala.findchinaapp.dbhandler;

import android.content.Context;
import android.util.Log;
import com.galagala.findchinaapp.AppController;
import com.galagala.findchinaapp.utils.Preference;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class DbUtils {
    public File getDatabasePath(Context context, String str) {
        return context.getDatabasePath(str);
    }

    public static void attachDB(Context context) {
        Log.d("Galagala", "attachDB++");
        if (!Preference.isDBAttached()) {
            File databasePath = context.getDatabasePath("dest");
            Log.d("Galagala", "attachDB databasePath.getAbsolutePath(): " + databasePath.getAbsolutePath());
            if (getDbFile(context, "rca.db", databasePath.getAbsolutePath()) && AppController.getDbHelper().attach(databasePath, false)) {
                Preference.setAttachedDb();
            }
        }
        Log.d("Galagala", "attachDB-- Preference.isDBAttached(): " + Preference.isDBAttached());
    }

    public static boolean getDbFile(Context context, String str, String str2) {
        try {
            InputStream open = context.getAssets().open(str);
            FileOutputStream fileOutputStream = new FileOutputStream(str2);
            Log.v("Tag assets: ", fileOutputStream.toString());
            byte[] bArr = new byte[1024];
            while (true) {
                int read = open.read(bArr);
                if (read > 0) {
                    fileOutputStream.write(bArr, 0, read);
                } else {
                    open.close();
                    fileOutputStream.flush();
                    fileOutputStream.close();
                    return true;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean isDatabaseAttached(Context context, String str) {
        if (getDatabasePath(context, str) == null) {
            return false;
        }
        return Preference.isDBAttached();
    }
}
