package com.jairoesc.simpleserver.simpleserver;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class SshService extends Service {
    public SshService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
}
