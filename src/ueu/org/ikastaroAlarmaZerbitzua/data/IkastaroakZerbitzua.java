/**
 * Copyright (C) 2013  Udako Euskal Unibertsitatea informatikaria@ueu.org

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
 * 
 */

package ueu.org.ikastaroAlarmaZerbitzua.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.xmlpull.v1.XmlPullParserException;

import ueu.org.IkastaroAlarmaZerbitzua.IkastaroAlarmaZerbitzuaEmaitza;
import ueu.org.IkastaroAlarmaZerbitzua.Konstanteak;
import ueu.org.IkastaroAlarmaZerbitzua.R;
import ueu.org.IkastaroAlarmaZerbitzua.Konstanteak.NetworkState;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.IBinder;
import android.widget.Toast;

public class IkastaroakZerbitzua extends Service {

	private static final int ID_NOTIFICACION_SORTU = R.string.lekuak_etiketa;
	private NotificationManager nm;
	private ikastaroEskuratzailea api;
	SharedPreferences konfigurazioa;
	List<ikastaroLekua> lekuakHautatuak;
	List<IkastaroMota> ikastaroMotakHautatuak;
	List<jakintzaZerrendaModeloa> jakintzaHautatuak;
	List<Ikastaroa> ikastaroJasoakId;

	Context context = this;

	@Override
	public IBinder onBind(Intent arg0) {
		return null;
	}

	@Override
	public void onCreate() {
		// Eskuratu konfigurazioa
		konfigurazioa = getApplicationContext().getSharedPreferences(
				Konstanteak.KONFIGURAZIOA, Activity.MODE_PRIVATE);
		api = new ikastaroEskuratzailea();
		nm = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

	}

	@Override
	public int onStartCommand(Intent intenc, int flags, int idAbioa) {

		// kargatu konfigurazioa
		EskaeraKonfigurazioa konf = new EskaeraKonfigurazioa(this.context);
		if (konf.konfigurazioaZuzena()) { //Ez bada zuzena, adib: ezabatu dutelako konfigurazioa. Orduan ez exekutatu
			lekuakHautatuak = konf.getIkastaroLekuak();
			ikastaroMotakHautatuak = konf.getIkastaroMotak();
			jakintzaHautatuak = konf.getJakintzak();

			// Egiaztatu sarearik badagoen
			NetworkState _state = NetworkStatusMonitor.get(context);
			if (_state != NetworkState.Unknown
					&& _state != NetworkState.Disconnected) {
				// Kargatu aurretik ikastaroren bat jaso den
				ikastaroJasoakId = loadIkastaroJasoakId();
				// Ikastaroak jaso beste hari batean
				IkastaroakEskuratuTask ataza = new IkastaroakEskuratuTask(
						context);
				ataza.execute();

			} else {
				Toast.makeText(context,
						getResources().getString(R.string.sare_errorea),
						Toast.LENGTH_SHORT).show();
			}
		}
		return START_STICKY;
	}

	@Override
	public void onDestroy() {
		Toast.makeText(this,
				getResources().getString(R.string.zerbitzua_gelditua),
				Toast.LENGTH_SHORT).show();
		// TODO agian ez da komeni notifikazioa ezabatzea, behintzat irakurri
		// arte
		nm.cancel(ID_NOTIFICACION_SORTU);
	}

	public List<Ikastaroa> loadIkastaroJasoakId() {
		SharedPreferences prefs = konfigurazioa;
		int size = prefs.getInt(Konstanteak.IKASTARO_JASOAK + "_size", 0);
		List<Ikastaroa> array = new ArrayList<Ikastaroa>();
		for (int i = 0; i < size; i++) {
			Ikastaroa ikastaro = new Ikastaroa(prefs.getString(
					Konstanteak.IKASTARO_JASOAK + "_izena_" + i, ""),
					prefs.getInt(Konstanteak.IKASTARO_JASOAK + "_id_" + i, 0));
			array.add(ikastaro);
		}
		return array;
	}

	public Boolean saveIkastaroJasoakId(List<Ikastaroa> array) {
		SharedPreferences.Editor editor = konfigurazioa.edit();
		if (array.size() > 0) {
			int size = konfigurazioa.getInt(Konstanteak.IKASTARO_JASOAK
					+ "_size", 0);
			editor.putInt(Konstanteak.IKASTARO_JASOAK + "_size", array.size()
					+ size);

			for (int i = 0; i < array.size(); i++) {
				editor.putInt(
						Konstanteak.IKASTARO_JASOAK + "_id_" + (i + size),
						array.get(i).getId());
				editor.putString(Konstanteak.IKASTARO_JASOAK + "_izena_"
						+ (i + size), array.get(i).getIzenburua());
			}
		}
		return editor.commit();
	}

	private class IkastaroakEskuratuTask extends
			AsyncTask<Void, Void, ArrayList<Ikastaroa>> {
		private Context context;

		public IkastaroakEskuratuTask(final Context context) {
			super();
			this.context = context;
		}

		// Ikastaroak APItik eskuratu
		@Override
		protected ArrayList<Ikastaroa> doInBackground(Void... urls) {
			ArrayList<Ikastaroa> emaitza = new ArrayList<Ikastaroa>();
			try {
				NetworkState _state = NetworkStatusMonitor.get(context);
				if (_state != NetworkState.Unknown
						&& _state != NetworkState.Disconnected) {
					emaitza = api.getIkastaroak(lekuakHautatuak,
							ikastaroMotakHautatuak, jakintzaHautatuak);
				}
			} catch (IOException io) {
				System.out.println("Sare arazoa");
			} catch (XmlPullParserException e) {
				System.out.println("Sare arazoa");
			}
			return emaitza;
		}

		@SuppressWarnings("deprecation")
		@Override
		protected void onPostExecute(ArrayList<Ikastaroa> ikastaroak) {
			// Behin ikastaroak eskuratuta oharra sortu eta argitaratu
			if (ikastaroak != null && ikastaroak.size() > 0) {
				ArrayList<Ikastaroa> ikastaroBerriakId = new ArrayList<Ikastaroa>();
				for (Ikastaroa ikastaro : ikastaroak) {
					if (!ikastaroJasoakId.contains(ikastaro)) {
						ikastaroBerriakId.add(ikastaro);
					}
				}
				if (ikastaroBerriakId.size() > 0
						&& saveIkastaroJasoakId(ikastaroBerriakId)) {
					// saveIkastaroJasoakId(ikastaroBerriakId);
					Notification notifikazioa = new Notification(
							R.drawable.oharra_icon, getResources().getString(
									R.string.ikastaro_berriak_oharra),
							System.currentTimeMillis());
					notifikazioa.defaults |= Notification.DEFAULT_SOUND;
					notifikazioa.defaults |= Notification.DEFAULT_VIBRATE;
					// Ezetzi oharra bisitatzean
					notifikazioa.flags |= Notification.FLAG_AUTO_CANCEL;
					Intent emaitza = new Intent(context,
							IkastaroAlarmaZerbitzuaEmaitza.class);
					emaitza.putExtra(Konstanteak.ZERBITZU_PARAMETROAK_LUZERA,
							ikastaroBerriakId.size());

					// Eskuratutako ikastaroak pasatu
					for (int i = 0; i < ikastaroBerriakId.size(); i++) {
						emaitza.putExtra(
								Konstanteak.ZERBITZU_PARAMETROAK_IKASTAROAK + i,
								ikastaroBerriakId.get(i));
					}

					// Prestatu Oharrari pasatu beharreko informazioa
					PendingIntent intencionPendiente = PendingIntent
							.getActivity(context, 0, emaitza,
									PendingIntent.FLAG_UPDATE_CURRENT);
					notifikazioa.setLatestEventInfo(context, getResources()
							.getString(R.string.oharra_izenburua),
							getResources().getString(R.string.oharra_testua),
							intencionPendiente);

					nm.notify(ID_NOTIFICACION_SORTU, notifikazioa);

				}
			}
		}
	}

}
