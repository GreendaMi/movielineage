package tool;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;

public class NetworkTypeInfo {
	
	public static NetworkType getNetworkType(Context context) {

		ConnectivityManager conMan = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

		NetworkInfo networkInfo = conMan == null ? null : conMan.getActiveNetworkInfo();
		

		if (networkInfo == null || !networkInfo.isConnected()) {
			return NetworkType.NoNetwork;
		}
		

		if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
			return NetworkType.Wifi;
		} 
		else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

			int subtype = networkInfo.getSubtype();

			if (subtype == TelephonyManager.NETWORK_TYPE_GPRS
					|| subtype == TelephonyManager.NETWORK_TYPE_EDGE
					|| subtype == TelephonyManager.NETWORK_TYPE_CDMA) {
				return NetworkType.Network2G;
			} else {
				return NetworkType.Global;
			}
		} 
		else if (networkInfo.getType() == ConnectivityManager.TYPE_ETHERNET) {
			return NetworkType.Cable;
		}
		

		return NetworkType.Unknown;
	}
}
