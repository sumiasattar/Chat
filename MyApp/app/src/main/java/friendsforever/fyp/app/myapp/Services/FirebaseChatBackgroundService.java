package friendsforever.fyp.app.myapp.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.messaging.RemoteMessage;

import friendsforever.fyp.app.myapp.MainActivity;
import friendsforever.fyp.app.myapp.Model.Message;
import friendsforever.fyp.app.myapp.R;

/**
 * Created by usama on 5/30/2019.
 */

public class FirebaseChatBackgroundService extends Service {
    DatabaseReference mMessagesUserDatabaseRef;
    private ValueEventListener handler;
    String currentUserUid;
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //FirebaseApp.initializeApp(this);
        mMessagesUserDatabaseRef= FirebaseDatabase.getInstance().getReference().child("conversations");
        currentUserUid= FirebaseAuth.getInstance().getCurrentUser().getUid();

        handler=new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Message message=dataSnapshot.getValue(Message.class);
                if (message.getSendTo().equals(currentUserUid)){
                    postNotification(message.getTextMessage());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
        mMessagesUserDatabaseRef.addValueEventListener(handler);
    }

    private void postNotification(String textMessage) {
        if (textMessage==null){
            textMessage="Receive New Photos";
        }

        NotificationManager notificationManager=(NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int icon= R.drawable.ic_email;
        Notification notification=new Notification(icon,"New Notification",System.currentTimeMillis());
        Context context=getApplicationContext();
        String contentTitle="New Messages";
        Intent notificationIntent=new Intent(context, MainActivity.class);
        PendingIntent pendingIntent= PendingIntent.getActivity(context,0,notificationIntent,0);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(context)
                .setColor(ContextCompat.getColor(context, R.color.colorPrimary))
                .setSmallIcon(icon)
                .setContentTitle(contentTitle)
                .setContentText(textMessage)
                .setAutoCancel(true);
        notificationManager.notify(1,notificationBuilder.build());


    }
}
