package wecare.beecalm;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

import java.nio.charset.StandardCharsets;

/**
 * Created by joleary and noon on 2/19/16 at very late in the night. (early in the morning?)
 */
public class WatchListenerService extends WearableListenerService {

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        Log.d("T", "in WatchListenerService, got: " + messageEvent.getPath());

        DataContainer dc = DataContainer.getInstance();

        if( messageEvent.getPath().equalsIgnoreCase( dc.MANTRAS ) ) {
            String mantras_list = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            dc.updateMantrasFromPhone(mantras_list);
        } else if (messageEvent.getPath().equalsIgnoreCase( dc.ACTIVITY )) {
            String activity_list = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            dc.updateActivityFromPhone(activity_list);
        } else if (messageEvent.getPath().equalsIgnoreCase( dc.YOGACONFIGURE )) {
            String yoga_list = new String(messageEvent.getData(), StandardCharsets.UTF_8);
            dc.updateYogaFromPhone(yoga_list);
        }else {
            super.onMessageReceived( messageEvent );
        }
        dc.writeConfigure(getApplicationContext());
    }
}