package com.github.paolorotolo.example.direct_notifications;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.RemoteInput;
import android.content.Intent;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private Button notificationButton;
    private TextView userReplyTextView;
    private static String REMOTE_INPUT_KEY = "remote_key";
    private static int NOTIFICATION_ID = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userReplyTextView = (TextView) findViewById(R.id.user_reply);
        notificationButton = (Button) findViewById(R.id.notification_button);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

        if (getIntent().getExtras() != null){
            if (getIntent().getExtras().getBoolean("notification")){
                userReplyTextView.setText(getMessageText(getIntent()));
                sendSecondNotification();
            }
        }
    }

    private CharSequence getMessageText(Intent intent) {
        Bundle remoteInput = RemoteInput.getResultsFromIntent(intent);
        if (remoteInput != null){
            return remoteInput.getCharSequence(REMOTE_INPUT_KEY);
        }

        return null;
    }

    private void sendNotification(){
        RemoteInput remoteInput = new RemoteInput.Builder(REMOTE_INPUT_KEY)
                .setLabel("Reply")
                .build();

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("notification", true);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Notification.Action actionNotification = new Notification.Action.Builder(
                R.drawable.ic_notification_icon,
                "Reply", pendingIntent)
                .addRemoteInput(remoteInput)
                .build();

        Notification notification = new Notification.Builder(getApplicationContext())
                .setContentTitle("New message!")
                .setContentText("Hi!")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setActions(actionNotification)
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, notification);
    }

    private void sendSecondNotification(){
        Notification secondNotification =
                new Notification.Builder(getApplicationContext())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentText("Message sent!")
                .build();

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(this);
        notificationManagerCompat.notify(NOTIFICATION_ID, secondNotification);
    }
}
