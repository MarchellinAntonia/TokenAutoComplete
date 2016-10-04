package com.tokenautocomplete.helper;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Created by iosdev on 5/16/16.
 */
public class HelperAppsInstaller {
    public void installKataApps(Context context){
        File file = new File(Environment.getExternalStorageDirectory()+ File.separator+"citypoint-kata-release.apk");

        if(!isPackageInstalled(context)){
            AssetManager assetManager = context.getAssets();
            InputStream in = null;
            OutputStream out = null;

            try{
                in = assetManager.open("citypoint-kata-release.apk");
                out = new FileOutputStream(file);
                byte[] buffer = new byte[1024];
                int read;
                while((read=in.read(buffer))!=-1){
                    out.write(buffer, 0, read);
                }
                in.close();
                in = null;
                out.flush();
                out.close();
                out = null;
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                context.startActivity(intent);
            }catch (Exception e){
                Log.d("Terjadi Error", e.getMessage());
            }
        }else{
            if(file.exists()){
                file.delete();
            }
            Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.gcm.citypoint.kata");
            context.startActivity(intent);
        }
    }


    public boolean isPackageInstalled(Context context){
        PackageManager pm = context.getPackageManager();
        String packagename = "com.gcm.citypoint.kata";
        try{
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        }catch(PackageManager.NameNotFoundException e){
            return false;
        }
    }
}
