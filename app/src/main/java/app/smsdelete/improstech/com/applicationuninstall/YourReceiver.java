package app.smsdelete.improstech.com.applicationuninstall;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by User3 on 04-04-2017.
 */

public class YourReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "Uninstalling Application", Toast.LENGTH_SHORT).show();
        String packageName=intent.getData().getEncodedSchemeSpecificPart();
    }
}