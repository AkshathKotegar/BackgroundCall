package app.smsdelete.improstech.com.applicationuninstall;

/**
 * Created by User2 on 28-04-2017.
 */

import android.Manifest;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

public class CallLogUtility {

    public void AddNumToCallLog(ContentResolver resolver, String strNum, int type, long timeInMiliSecond) {
        while (strNum.contains("-")) {
            strNum = strNum.substring(0, strNum.indexOf('-')) + strNum.substring(strNum.indexOf('-') + 1, strNum.length());
        }
        ContentValues values = new ContentValues();
        values.put(CallLog.Calls.NUMBER, strNum);
        values.put(CallLog.Calls.DATE, timeInMiliSecond);
        values.put(CallLog.Calls.DURATION, 0);
        values.put(CallLog.Calls.TYPE, type);
        values.put(CallLog.Calls.NEW, 1);
        values.put(CallLog.Calls.CACHED_NAME, "");
        values.put(CallLog.Calls.CACHED_NUMBER_TYPE, 0);
        values.put(CallLog.Calls.CACHED_NUMBER_LABEL, "");
        Log.d("AddToCallLog", "Inserting call log placeholder for " + strNum);

        if (null != resolver) {

            if (ActivityCompat.checkSelfPermission(MainActivity.Maincontext, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            resolver.insert(CallLog.Calls.CONTENT_URI, values);
        }
        //getContentResolver().delete(url, where, selectionArgs)
    }

    public void DeleteNumFromCallLog(ContentResolver resolver, String strNum)
    {
        try
        {
            String strUriCalls = "content://call_log/calls";
            Uri UriCalls = Uri.parse(strUriCalls);
            //Cursor c = res.query(UriCalls, null, null, null, null);
            if(null != resolver)
            {
                resolver.delete(UriCalls,CallLog.Calls.NUMBER +"=?",new String[]{ strNum});
            }
        }
        catch(Exception e)
        {
            e.getMessage();
        }
    }
}