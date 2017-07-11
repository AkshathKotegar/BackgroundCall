package app.smsdelete.improstech.com.applicationuninstall;

import android.Manifest;
import android.app.PendingIntent;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInstaller;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Handler;
import android.provider.CallLog;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {
    Button txt;
    int flag = 0;
    static Context Maincontext;

    // String app_pkg_name = "app.smsdelete.improstech.com.applicationuninstall";
    //int UNINSTALL_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        txt = (Button) findViewById(R.id.call);
        Maincontext = getApplicationContext();

        /*String numberStr = "8970864496";
        CallLogUtility utility = new CallLogUtility();
        utility.DeleteNumFromCallLog(getBaseContext().getContentResolver(), numberStr);
        Toast.makeText(this, "Deleted", Toast.LENGTH_LONG).show();*/

        txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                makecall();
            }
        });


    }

    public void makecall() {
        try {
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:9900350924"));
            //callIntent.setClassName("com.android.phone", "com.android.phone.OutgoingCallBroadcaster");
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            startActivity(callIntent);
            finish();
            Runnable showDialogRun = new Runnable() {
                public void run() {
                    Intent showDialogIntent = new Intent(MainActivity.this, DialogActivity.class);
                    //showDialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(showDialogIntent);
                    finish();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            killCall(MainActivity.this);
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    deleteNumber("9900350924");
                                    Intent showDialogIntent = new Intent(MainActivity.this, MainActivity.class);
                                    //showDialogIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(showDialogIntent);
                                    finish();
                                }
                            }, 5000);
                        }
                    }, 10000);
                }
            };
            Handler h = new Handler();
            h.postDelayed(showDialogRun, 1300);
        } catch (ActivityNotFoundException activityException) {
            Throwable e = null;
            //Log.e("helloandroid dialing example", "Callfailed", e);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addCategory(Intent.CATEGORY_HOME);
        startActivity(intent);
    }
/*  @Override
    protected void onResume() {
        super.onResume();
        String numberStr = "8970864496";
        CallLogUtility utility = new CallLogUtility();
        utility.DeleteNumFromCallLog(getBaseContext().getContentResolver(), numberStr);
        Toast.makeText(this, "Deleted", Toast.LENGTH_LONG).show();
    }*/

    public boolean killCall(Context context) {
        try {
            // Get the boring old TelephonyManager
            TelephonyManager telephonyManager =
                    (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

            // Get the getITelephony() method
            Class classTelephony = Class.forName(telephonyManager.getClass().getName());
            Method methodGetITelephony = classTelephony.getDeclaredMethod("getITelephony");

            // Ignore that the method is supposed to be private
            methodGetITelephony.setAccessible(true);

            // Invoke getITelephony() to get the ITelephony interface
            Object telephonyInterface = methodGetITelephony.invoke(telephonyManager);

            // Get the endCall method from ITelephony
            Class telephonyInterfaceClass =
                    Class.forName(telephonyInterface.getClass().getName());
            Method methodEndCall = telephonyInterfaceClass.getDeclaredMethod("endCall");

            // Invoke endCall()
            methodEndCall.invoke(telephonyInterface);


        } catch (Exception ex) { // Many things can go wrong with reflection calls
            Log.d("exe", "PhoneStateReceiver **" + ex.toString());
            return false;
        }
        return true;
    }

    private void deleteNumber() {
        try {
            String strNumberOne[] = {"9900350924"};
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            Cursor cursor = getContentResolver().query(CallLog.Calls.CONTENT_URI, null, CallLog.Calls.NUMBER + " = ? ", strNumberOne, "");
            boolean bol = cursor.moveToFirst();
            if (bol) {
                do {
                    int idOfRowToDelete = cursor.getInt(cursor.getColumnIndex(CallLog.Calls._ID));
                    getContentResolver().delete(Uri.withAppendedPath(CallLog.Calls.CONTENT_URI, String.valueOf(idOfRowToDelete)), "", null);
                } while (cursor.moveToNext());
            }
        } catch (Exception ex) {
            System.out.print("Exception here ");
        }
    }

    private void deleteNumber(String numberStr) {

        // delete this number from call log
        CallLogUtility utility = new CallLogUtility();
        utility.DeleteNumFromCallLog(getBaseContext().getContentResolver(), numberStr);
        Toast.makeText(this, "Deleted", Toast.LENGTH_LONG).show();
    }
    /*@Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == UNINSTALL_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Log.d("TAG", "onActivityResult: user accepted the (un)install");
            } else if (resultCode == RESULT_CANCELED) {
                Log.d("TAG", "onActivityResult: user canceled the (un)install");
            } else if (resultCode == RESULT_FIRST_USER) {
                Log.d("TAG", "onActivityResult: failed to (un)install");
            }
        }
    }*/

    public void DeleteCallLogByNumber(String number) {
        String queryString = "NUMBER=" + number;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        this.getContentResolver().delete(CallLog.Calls.CONTENT_URI, queryString, null);
    }
}


