/*
 * Author: Willem Meints
 * http://fizzylogic.nl/author/wmeints/
 * https://twitter.com/willem_meints
 */

package ueu.org.ikastaroAlarmaZerbitzua.data;

import ueu.org.IkastaroAlarmaZerbitzua.Konstanteak.NetworkState;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class NetworkStatusMonitor
{
    private static NetworkState _state;
 
    public NetworkStatusMonitor ()
    {
    }
 
    public static NetworkState get(Context context){
            UpdateNetworkStatus(context);
 
            return _state; 
    }
 
    public static void UpdateNetworkStatus(Context context) {
        _state = NetworkState.Unknown;
 
        // Retrieve the connectivity manager service
        ConnectivityManager connectivityManager = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
 
        // Check if the network is connected or connecting.
        // This means that it will be available, 
        // or become available in a few seconds.
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
 
        if (activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting()) {
            // Now that we know it's connected, determine if we're on WiFi or something else.
            _state = activeNetworkInfo.getType() == ConnectivityManager.TYPE_WIFI ?
                NetworkState.ConnectedWifi : NetworkState.ConnectedData;
        } else {
            _state = NetworkState.Disconnected;
        }
    }
}
