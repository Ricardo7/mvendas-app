package lr.maisvendas.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import lr.maisvendas.sincronizacao.Sincronizacao;

/**
 * Created by Ronaldo on 15/02/2018.
 */

public class BootReciever extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        Intent myIntent = new Intent(context, Sincronizacao.class);
        //Intent myIntent = new Intent(context, Alarme.class);
        context.startService(myIntent);
        context.stopService(myIntent);
    }
}
