package friendsforever.fyp.app.myapp.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by usama on 5/30/2019.
 */

public class StartFireChatAtBoot extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,FirebaseChatBackgroundService.class));
    }
}
