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

package ueu.org.IkastaroAlarmaZerbitzua;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.TimeZone;

import org.xmlpull.v1.XmlPullParserException;

import ueu.org.IkastaroAlarmaZerbitzua.Konstanteak.NetworkState;
import ueu.org.ikastaroAlarmaZerbitzua.data.AlarmReceiver;
import ueu.org.ikastaroAlarmaZerbitzua.data.EskaeraKonfigurazioa;
import ueu.org.ikastaroAlarmaZerbitzua.data.JakintzaZerrendaAdaptadorea;
import ueu.org.ikastaroAlarmaZerbitzua.data.NetworkStatusMonitor;
import ueu.org.ikastaroAlarmaZerbitzua.data.ikastaroEskuratzailea;
import ueu.org.ikastaroAlarmaZerbitzua.data.IkastaroMota;
import ueu.org.ikastaroAlarmaZerbitzua.data.ikastaroLekua;
import ueu.org.ikastaroAlarmaZerbitzua.data.jakintzaZerrendaModeloa;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Display;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ListView;

public class IkastaroAlarmaZerbitzuaActivity extends Activity {

	final Context context = this;

	// zerbitzuarekin komunikatzeko aldagaiak
	public static final int GELDITU = 0;
	public static final int ABIATU = 1;
	public final static String BIDALTZAILEA = "bidaltzailea";

	// Aukeratutako konfigurazio-aukerak bistaratu textViewtan
	private TextView textViewMotakHautatuak;
	private TextView textViewJakintzakHautatuak;
	private TextView textViewLekuakHautatuak;
	private TextView maiztasun_hautatua = null;

	ArrayList<jakintzaZerrendaModeloa> jakintzakarray = new ArrayList<jakintzaZerrendaModeloa>();
	ArrayList<ikastaroLekua> lekuakArray = new ArrayList<ikastaroLekua>();
	ArrayList<IkastaroMota> ikastaroMotakArray = new ArrayList<IkastaroMota>();

	// Aukeren zerrendak
	ArrayList<jakintzaZerrendaModeloa> jakintzaHautatuak = new ArrayList<jakintzaZerrendaModeloa>();
	List<ikastaroLekua> lekuakHautatuak = new ArrayList<ikastaroLekua>();
	ArrayList<IkastaroMota> ikastaroMotakHautatuak = new ArrayList<IkastaroMota>();

	// Zerbitzuaren kontrolerako botoiak
	ImageButton abiatuBotoia = null;
	ImageButton geldituBotoia = null;
	ImageButton konfigurazioaGorde = null;
	EskaeraKonfigurazioa konf = null;

	// Menua
	ImageView lekuakBotoia = null;
	ImageView jakintzakBotoia = null;
	ImageView motakBotoia = null;
	ImageView maiztasunaBotoia = null;

	private Boolean konfigurazioaGordea = false;

	private ProgressDialog progressDialog;

	private int maiztasuna_segundo = Konstanteak.MAIZTASUNA_DEFAULT; // defektuz
																		// orduro
																		// egiaztatuko
																		// da
																		// ikastaro
																		// berriak
																		// dauden

	// Atzeko planoan dabiltzaten prozesuak sinkronizatzeko kudeatzailea
	// Konfigurazio aukerak kargatutakoan eskuragarri jarri
	private final Handler handler = new Handler() {
		@Override
		public void handleMessage(final Message msg) {

			switch (msg.what) {
			case 0: // Konfigurazio aukerak jaso dira saretik
				if (progressDialog != null && progressDialog.isShowing())
					progressDialog.dismiss();
				// konf zuzena bada
				Boolean alarmaMartxan = kargatuAlarmaEgoera();
				if (konf != null && konf.konfigurazioaZuzena()
						&& !alarmaMartxan) {
					abiatuBotoia.setEnabled(true);
					geldituBotoia.setEnabled(false);
					jakintzakBotoia.setClickable(true);
					lekuakBotoia.setClickable(true);
					motakBotoia.setClickable(true);
					maiztasunaBotoia.setClickable(true);
				} else { // konfigurazioa ez da zuzena edo alarma dagoeneko
							// martxan dago
					geldituBotoia.setEnabled(alarmaMartxan);
					abiatuBotoia.setEnabled(false);
					jakintzakBotoia.setClickable(!alarmaMartxan);
					lekuakBotoia.setClickable(!alarmaMartxan);
					motakBotoia.setClickable(!alarmaMartxan);
					maiztasunaBotoia.setClickable(!alarmaMartxan);
				}
				break;
			case 1: // Errorea egon da sareko konexioarekin eta ezin izan dira
					// konfigurazio aukerak lortu
				if (progressDialog != null && progressDialog.isShowing())
					progressDialog.dismiss();

				final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
						IkastaroAlarmaZerbitzuaActivity.this);
				alertDialogBuilder.setTitle(R.string.errorea);
				alertDialogBuilder
						.setMessage(
								getResources().getString(R.string.sare_errorea))
						.setCancelable(false)
						.setPositiveButton(
								getResources().getString(R.string.ados),
								new DialogInterface.OnClickListener() {
									public void onClick(DialogInterface dialog,
											int id) {
										// if this button is clicked, close
										// current activity
										IkastaroAlarmaZerbitzuaActivity.this
												.finish();
									}
								});
				// Sortu dialogoa
				AlertDialog alertDialog = alertDialogBuilder.create();
				alertDialog.show();

				// IkastaroAlarmaZerbitzuaActivity.this.finish();
				break;
			}
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// Layout-eko elementuak eskuratu
		textViewMotakHautatuak = (TextView) findViewById(R.id.textViewMotakHautatuak);
		textViewJakintzakHautatuak = (TextView) findViewById(R.id.textViewJakintzakHautatuak);
		textViewLekuakHautatuak = (TextView) findViewById(R.id.textViewLekuakHautatuak);
		maiztasun_hautatua = (TextView) findViewById(R.id.maiztasunHautatua);

		// menua
		lekuakBotoia = (ImageView) findViewById(R.id.hasi_menu_lekuak);
		jakintzakBotoia = (ImageView) findViewById(R.id.hasi_menu_jakintzak);
		motakBotoia = (ImageView) findViewById(R.id.hasi_menu_mota);
		maiztasunaBotoia = (ImageView) findViewById(R.id.hasi_menu_maiztasuna);

		// Botoiak
		abiatuBotoia = (ImageButton) findViewById(R.id.zerbitzuaAbiatu);

		geldituBotoia = (ImageButton) findViewById(R.id.zerbitzuaGelditu);
		geldituBotoia.setClickable(false);
		geldituBotoia.setEnabled(false);

		konfigurazioaGorde = (ImageButton) findViewById(R.id.konfigurazioaGorde);
		konfigurazioaGorde.setEnabled(false);

		// Haiseratu konfigurazioa
		konf = new EskaeraKonfigurazioa(this.context);

		if (savedInstanceState == null) { // Egoera gordeta ez badago edo
											// parametroak ez badira ondo
											// kargatu APItik eskuratu informazioa
			// Hasieratu APIa
			ikastaroEskuratzailea api = new ikastaroEskuratzailea();
			// Egiaztatu sarea badagoela
			NetworkState _state = NetworkStatusMonitor.get(context);
			if (_state != NetworkState.Unknown
					&& _state != NetworkState.Disconnected) {
				this.progressDialog = ProgressDialog.show(this, getResources()
						.getString(R.string.lanean),
						getResources().getString(R.string.konf_aukerak), true,
						false);

				konfigurazioaGordea = kargatuKonfigurazioAukerak(api); // kofigurazioa
																		// gordea
																		// eta
																		// zuzena
			} else {
				// Sarerik ez
				handler.sendEmptyMessage(1);
			}
		}

	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {

		// Gorde APItik jasotako ikastaro-motak eta uneko aukeraketa
		int luz = 0;
		if (ikastaroMotakArray != null && !ikastaroMotakArray.isEmpty()
				&& ikastaroMotakArray.size() > 0) {
			luz = ikastaroMotakArray.size();
		}
		outState.putInt("ikastaroMotakArray_size", luz);
		for (int i = 0; i < luz; i++) {
			outState.putString("ikastaroMotakArray_izena_" + i,
					((IkastaroMota) ikastaroMotakArray.get(i)).getIzena());
			outState.putInt("ikastaroMotakArray_pk_" + i,
					((IkastaroMota) ikastaroMotakArray.get(i)).getId());
			outState.putBoolean("ikastaroMotakArray_selected_" + i,
					((IkastaroMota) ikastaroMotakArray.get(i)).isSelected());

		}
		// Gorde APItik jasotako jakintza-arloak eta uneko aukeraketa
		luz = 0;
		if (jakintzakarray != null && !jakintzakarray.isEmpty()
				&& jakintzakarray.size() > 0) {
			luz = jakintzakarray.size();
		}
		outState.putInt("jakintzakarray_size", luz);
		for (int i = 0; i < luz; i++) {
			outState.putString("jakintzakarray_izena_" + i,
					((jakintzaZerrendaModeloa) jakintzakarray.get(i)).getName());
			outState.putInt("jakintzakarray_pk_" + i,
					((jakintzaZerrendaModeloa) jakintzakarray.get(i)).getPK());
			outState.putBoolean("jakintzakarray_selected_" + i,
					((jakintzaZerrendaModeloa) jakintzakarray.get(i))
							.isSelected());
		}
		// Gorde APItik jasotako lekuak eta uneko aukeraketa
		luz = 0;
		if (lekuakArray != null && !lekuakArray.isEmpty()
				&& lekuakArray.size() > 0) {
			luz = lekuakArray.size();
		}
		outState.putInt("lekuakArray_size", luz);
		for (int i = 0; i < luz; i++) {
			outState.putString("lekuakArray_izena_" + i,
					((ikastaroLekua) lekuakArray.get(i)).getIzena());
			outState.putInt("lekuakArray_pk_" + i,
					((ikastaroLekua) lekuakArray.get(i)).getId());
			outState.putBoolean("lekuakArray_selected_" + i,
					((ikastaroLekua) lekuakArray.get(i)).isSelected());
		}

		// Gorde uneko maiztasuna
		outState.putInt("uneko_maiztasuna", maiztasuna_segundo);

		// Gorde alarma martxan dagoen
		// Berez gelditzeko eta abiatzeko botoiak sakatzen direnean hauxe egiten
		// da. Beraz, ez da gorde behar
		outState.putBoolean("abiatuta_gaituta", abiatuBotoia.isEnabled());
		outState.putBoolean("geldituta_gaituta", geldituBotoia.isEnabled());

		// Gorde konfigurazioa gorde behar den ala ez
		outState.putBoolean("gorde_gaituta", konfigurazioaGorde != null
				&& konfigurazioaGorde.isEnabled());

		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle state) {
		super.onRestoreInstanceState(state);
		
		String testua = "";

		int size = 0;
		// Berreskuratu APItik jasotako ikastaro-motak eta uneko aukeraketa
		size = state.getInt("ikastaroMotakArray_size", 0);
		for (int i = 0; i < size; i++) {
			IkastaroMota mota = new IkastaroMota();
			mota.setId(state.getInt("ikastaroMotakArray_pk_" + i, 0));
			mota.setIzena(state.getString("ikastaroMotakArray_izena_" + i));
			mota.setSelected(state.getBoolean("ikastaroMotakArray_selected_"
					+ i));
			if (mota.isSelected()) {
				if (testua.length() == 0){
					testua = mota.toString();
					ikastaroMotakHautatuak.add(mota);
				}
				else
					testua = testua + ", " + mota.toString();
			}
			ikastaroMotakArray.add(mota);
		}
		textViewMotakHautatuak.setText(testua);

		// Berreskuratu APItik jasotako jakintza-arloak eta uneko aukeraketa
		testua = "";
		size = state.getInt("jakintzakarray_size", 0);
		for (int i = 0; i < size; i++) {
			jakintzaZerrendaModeloa jakintza = new jakintzaZerrendaModeloa();
			jakintza.setPK(state.getInt("jakintzakarray_pk_" + i, 0));
			jakintza.setName(state.getString("jakintzakarray_izena_" + i));
			jakintza.setSelected(state.getBoolean("jakintzakarray_selected_"
					+ i));
			if (jakintza.isSelected()) {
				if (testua.length() == 0){
					testua = jakintza.toString(); //Join-erako egokitu da toString metodoa
					jakintzaHautatuak.add(jakintza);
				}
				else
					testua = testua + ", " + jakintza.toString();
			}
			jakintzakarray.add(jakintza);
		}
		textViewJakintzakHautatuak.setText(testua);

		// Berreskuratu APItik jasotako tokiak eta uneko aukeraketa
		testua = "";
		size = state.getInt("lekuakArray_size", 0);
		for (int i = 0; i < size; i++) {
			ikastaroLekua lekua = new ikastaroLekua();
			lekua.setId(state.getInt("lekuakArray_pk_" + i, 0));
			lekua.setIzena(state.getString("lekuakArray_izena_" + i));
			lekua.setSelected(state.getBoolean("lekuakArray_selected_" + i));
			if (lekua.isSelected()) {
				if (testua.length() == 0){
					testua = lekua.getIzena().substring(0,1).toUpperCase() + lekua.getIzena().substring(1);
					lekuakHautatuak.add(lekua);
				}
				else
					testua = testua + ", " + lekua.getIzena().substring(0,1).toUpperCase() + lekua.getIzena().substring(1);
			}
			lekuakArray.add(lekua);
		}
		textViewLekuakHautatuak.setText(testua);

		// Berreskuratu maiztasuna
		maiztasuna_segundo = state.getInt("uneko_maiztasuna",
				Konstanteak.MAIZTASUNA_DEFAULT);
		maiztasun_hautatua.setText(EskaeraKonfigurazioa
				.maiztasunaToMaiztasunaText(maiztasuna_segundo));
		
		// Berreskuratu gorde botoiaren egoera
		Boolean gordeGaituta = state.getBoolean("gorde_gaituta");
		konfigurazioaGorde.setEnabled(gordeGaituta);

		// Berreskuratu zerbitzua abiatzeko/gelditzeko botoien egoera				
		abiatuBotoia.setEnabled(state.getBoolean("abiatuta_gaituta"));
		geldituBotoia.setEnabled(state.getBoolean("geldituta_gaituta"));
		
		// Berreskuratu kofigurazio botoien egoera
		lekuakBotoia.setClickable(!geldituBotoia.isEnabled());
		lekuakBotoia.setEnabled(!geldituBotoia.isEnabled());
		jakintzakBotoia.setClickable(!geldituBotoia.isEnabled());
		jakintzakBotoia.setEnabled(!geldituBotoia.isEnabled());
		motakBotoia.setClickable(!geldituBotoia.isEnabled());
		motakBotoia.setEnabled(!geldituBotoia.isEnabled());
		maiztasunaBotoia.setClickable(!geldituBotoia.isEnabled());
		maiztasunaBotoia.setEnabled(!geldituBotoia.isEnabled());

	}

	@Override
	public void onResume() {
		super.onResume();

		// Prestatu menua
		lekuakBotoia.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// lortu android bertsioa
				int version = android.os.Build.VERSION.SDK_INT;
				Display display = getWindowManager().getDefaultDisplay();
				int width;
				int height;
				if (version >= 13) {
					Point size = new Point();
					display.getSize(size);
					width = size.x;
					height = size.y;
				} else {
					width = display.getWidth();
					height = display.getHeight();
				}
				final ikastaroLekuakDialog lekuakDialog = new ikastaroLekuakDialog(
						context, getResources().getString(
								R.string.lekuakDialogIzenburua), lekuakArray);

				lekuakDialog.getWindow().setLayout((6 * width) / 7,
						(4 * height) / 5);
				lekuakDialog.setOnDismissListener(new OnDismissListener() {
					@SuppressWarnings("unchecked")
					public void onDismiss(final DialogInterface dialog) {
						// Lekuak aukeratzeko dialogoa itxiko denean
						// aukeratutakoak
						// gorde
						// eta pantailan erakutsi

						// Array-ak bi elementu ditu moten zerrenda eta moten
						// zerrenda testu moduan
						ArrayList<Object> lekuakHautatuakMap = lekuakDialog
								.getSelectedMotakEtaString();
						lekuakHautatuak = (ArrayList<ikastaroLekua>) lekuakHautatuakMap
								.get(0);
						String testua = (String) lekuakHautatuakMap.get(1);
						textViewLekuakHautatuak.setText(testua);
						// gaitu/desgaitu beharrezko botoiak
						if (!lekuakDialog.getEzetzi()) {
							konf.setLekuak(lekuakHautatuak); // konfigurazioa
																// eguneratu
																// baina gorde
																// gabe
							if (!konfigurazioaGorde.isEnabled()
									&& konf.konfigurazioaZuzena())
								konfigurazioaGorde.setEnabled(true);
							if (abiatuBotoia.isEnabled()) // konfigurazioa gorde
															// arte
															// ezin da zerbitzua
															// abiatu
								abiatuBotoia.setEnabled(false);
						}
					}
				});
				lekuakDialog.show();
			}
		});

		jakintzakBotoia.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// lortu android bertsioa
				int version = android.os.Build.VERSION.SDK_INT;
				Display display = getWindowManager().getDefaultDisplay();
				int width;
				int height;
				if (version >= 13) {
					Point size = new Point();
					display.getSize(size);
					width = size.x;
					height = size.y;
				} else {
					width = display.getWidth();
					height = display.getHeight();
				}
				final jakintzaAukeraketaDialogoa jakintzaDialog = new jakintzaAukeraketaDialogoa(
						context, jakintzakarray);
				Dialog dialog = jakintzaDialog.getDialogoa();
				dialog.setTitle(getResources().getString(
						R.string.jakintzakDialogIzenburua));
				dialog.setOnDismissListener(new OnDismissListener() {
					@SuppressWarnings("unchecked")
					@Override
					public void onDismiss(DialogInterface dialog) {
						ListView lv = (ListView) ((Dialog) dialog)
								.findViewById(R.id.jakintza_zerrenda);
						JakintzaZerrendaAdaptadorea adapter = (JakintzaZerrendaAdaptadorea) lv
								.getAdapter();
						ArrayList<Object> jakintzaHautatuakMap = adapter
								.getSelectedEtaString();
						jakintzaHautatuak = (ArrayList<jakintzaZerrendaModeloa>) jakintzaHautatuakMap
								.get(0);
						String testua = (String) jakintzaHautatuakMap.get(1);
						textViewJakintzakHautatuak.setText(testua);

						if (!jakintzaDialog.getEzetzi()) {
							konf.setJakintzak(jakintzaHautatuak);
							if (!konfigurazioaGorde.isEnabled()
									&& konf.konfigurazioaZuzena())
								konfigurazioaGorde.setEnabled(true);
							if (abiatuBotoia.isEnabled()) // konfigurazioa gorde
															// arte
															// ezin
															// da zerbitzua
															// abiatu
								abiatuBotoia.setEnabled(false);
						}
					}
				});
				// Dialogoaren tamaina ezarri
				dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
				dialog.show();
			}
		});

		motakBotoia.setOnClickListener(new OnClickListener() {
			@SuppressWarnings("deprecation")
			@Override
			public void onClick(View arg0) {
				// lortu android bertsioa
				int version = android.os.Build.VERSION.SDK_INT;
				Display display = getWindowManager().getDefaultDisplay();
				int width;
				int height;
				if (version >= 13) {
					Point size = new Point();
					display.getSize(size);
					width = size.x;
					height = size.y;
				} else {
					width = display.getWidth();
					height = display.getHeight();
				}
				final ikastaroMotakDialog motaDialog = new ikastaroMotakDialog(
						context, getResources().getString(
								R.string.motakDialogIzenburua),
						ikastaroMotakArray);

				motaDialog.getWindow().setLayout((6 * width) / 7,
						(4 * height) / 5);
				motaDialog.setOnDismissListener(new OnDismissListener() {
					@SuppressWarnings("unchecked")
					public void onDismiss(final DialogInterface dialog) {
						// Array-ak bi elementu ditu moten derrenda eta moten
						// zerrenda testu moduan
						ArrayList<Object> ikastaroMotakHautatuakMap = motaDialog
								.getSelectedMotakEtaString();
						ikastaroMotakHautatuak = (ArrayList<IkastaroMota>) ikastaroMotakHautatuakMap
								.get(0);
						String testua = (String) ikastaroMotakHautatuakMap
								.get(1);
						textViewMotakHautatuak.setText(testua);
						if (!motaDialog.getEzetzi()) {
							// gaitu/desgaitu beharrezko botoiak
							konf.setIkataroMotak(ikastaroMotakHautatuak);
							if (!konfigurazioaGorde.isEnabled()
									&& konf.konfigurazioaZuzena())
								konfigurazioaGorde.setEnabled(true);
							if (abiatuBotoia.isEnabled()) // konfigurazioa gorde
															// arte
															// ezin da zerbitzua
															// abiatu
								abiatuBotoia.setEnabled(false);
						}
					}
				});
				motaDialog.show();
			}
		});

		maiztasunaBotoia.setOnClickListener(new OnClickListener() {
			// int emaitza_maiztasun;

			@Override
			public void onClick(View arg0) {
				final MaiztasunaDialog maiztasunDlg = new MaiztasunaDialog(
						context, R.string.maiztasuna_prompt,
						R.array.maiztasuna_arrays, maiztasuna_segundo);
				Dialog maiztasunDialog = maiztasunDlg.getDialog();
				maiztasunDialog.setOnDismissListener(new OnDismissListener() {
					public void onDismiss(final DialogInterface dialog) {
						int emaitza_maiztasun = maiztasunDlg.getMaiztasuna();
						if (maiztasuna_segundo != emaitza_maiztasun) { // maiztasun
																		// diferente
																		// bat
																		// aukeratu
																		// bada
							maiztasuna_segundo = emaitza_maiztasun;

							// Erakutsi aukeratutako maiztasuna hasierako
							// pantailan
							maiztasun_hautatua.setText(EskaeraKonfigurazioa
									.maiztasunaToMaiztasunaText(maiztasuna_segundo));
							// gaitu/desgaitu beharrezko botoiak
							konf.setMaiztasuna(maiztasuna_segundo);
							if (konfigurazioaGorde != null
									&& !konfigurazioaGorde.isEnabled()
									&& konf.konfigurazioaZuzena())
								konfigurazioaGorde.setEnabled(true);
							if (abiatuBotoia.isEnabled()) // konfigurazioa gorde
															// arte
															// ezin da zerbitzua
															// abiatu
								abiatuBotoia.setEnabled(false);
						}
					}
				});
				maiztasunDialog.show();
			}
		});

		// abiatuBotoia.setClickable(false);

		// abiatuBotoia.setEnabled(konfigurazioaGordea); // kargatutako
		// konfigurazioa zuzena
		// bada gaitu abiatzeko
		// zerbitzua
		abiatuBotoia.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {

				// Zerbitzua abiatzeko data ezarri (11:45 etan)
				Calendar updateTime = Calendar.getInstance();
				updateTime.setTimeZone(TimeZone.getTimeZone("GMT"));
				// updateTime.set(Calendar.HOUR_OF_DAY, 11);
				// updateTime.set(Calendar.MINUTE, 45);

				// sistemako alarmari AlarmReceiver Broadcast-a lotu
				Intent ikastaroReceiver = new Intent(context,
						AlarmReceiver.class);
				PendingIntent ikastaroEskuratzaileaBegizta = PendingIntent
						.getBroadcast(context, 0, ikastaroReceiver,
								PendingIntent.FLAG_CANCEL_CURRENT);
				AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				// frogetarako maiztasuna 1 min, Ezabatu hurrengo komentarioa
				//maiztasuna_segundo = 60;
				alarms.setInexactRepeating(AlarmManager.RTC_WAKEUP,
						System.currentTimeMillis(), maiztasuna_segundo * 1000,
						ikastaroEskuratzaileaBegizta);
				gordeAlarmaEgoera(true); // Alarma Martxan
				Toast.makeText(
						context,
						context.getResources().getString(
								R.string.zerbitzua_martxan), Toast.LENGTH_SHORT)
						.show();

				// desgaitu zerbitzua abiatzeko botoia abiatuta dagoelako
				abiatuBotoia.setClickable(false);
				abiatuBotoia.setEnabled(false);
				// Desgaitu konfigurazio aukerak
				jakintzakBotoia.setClickable(false);
				jakintzakBotoia.setEnabled(false);
				lekuakBotoia.setClickable(false);
				lekuakBotoia.setEnabled(false);
				motakBotoia.setClickable(false);
				motakBotoia.setEnabled(false);
				maiztasunaBotoia.setClickable(false);
				maiztasunaBotoia.setEnabled(false);
				// gaitu zerbitzua gelditzeko botoia
				geldituBotoia.setClickable(true);
				geldituBotoia.setEnabled(true);
				// desgaitu konfigurazioa gordetzeko botoia
				konfigurazioaGorde.setEnabled(false);
			}

		});

		geldituBotoia.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Intent ikastaroReceiver = new Intent(context,
						AlarmReceiver.class);
				PendingIntent ikastaroEskuratzaileaBegizta = PendingIntent
						.getBroadcast(context, 0, ikastaroReceiver,
								PendingIntent.FLAG_CANCEL_CURRENT);
				AlarmManager alarms = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
				alarms.cancel(ikastaroEskuratzaileaBegizta);
				gordeAlarmaEgoera(false); // Alarma geldirik
				// Gaitu konfigurazio aukerak
				jakintzakBotoia.setClickable(true);
				jakintzakBotoia.setEnabled(true);
				lekuakBotoia.setClickable(true);
				lekuakBotoia.setEnabled(true);
				motakBotoia.setClickable(true);
				motakBotoia.setEnabled(true);
				maiztasunaBotoia.setClickable(true);
				maiztasunaBotoia.setEnabled(true);
				Toast.makeText(context,
						getResources().getString(R.string.zerbitzua_gelditua),
						Toast.LENGTH_SHORT).show();
				if (konfigurazioaGordea) { // konfigurazioa gordeta badago eta
											// zuzena bada gaitu abiatzeko
											// botoia
					abiatuBotoia.setClickable(true);
					abiatuBotoia.setEnabled(true);
				}
				geldituBotoia.setClickable(false);
				geldituBotoia.setEnabled(false);
			}

		});

		konfigurazioaGorde.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				konfigurazioaGorde();
				if (konfigurazioaGordea)
					konfigurazioaGorde.setEnabled(false);
			}

		});

	}

	@Override
	protected void onStop() {
		if (progressDialog != null && progressDialog.isShowing()) {
			progressDialog.dismiss();
		}
		super.onStop();
		// Gorde alarma martxan dagoen
		gordeAlarmaEgoera(geldituBotoia != null && geldituBotoia.isEnabled());
	}

	private Boolean gordeAlarmaEgoera(Boolean egoera) {
		SharedPreferences prefs = this.context.getSharedPreferences(
				Konstanteak.KONFIGURAZIOA, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putBoolean(Konstanteak.KONFIGURAZIOA_ABIATUTA, egoera);
		return editor.commit();
	}

	private Boolean kargatuAlarmaEgoera() {
		SharedPreferences prefs = this.context.getSharedPreferences(
				Konstanteak.KONFIGURAZIOA, Context.MODE_PRIVATE);
		return prefs.getBoolean(Konstanteak.KONFIGURAZIOA_ABIATUTA, false);
	}

	/*
	 * Aukera-menua kargatu
	 * 
	 * @see android.app.Activity#onCreateOptionsMenu(android.view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.hasieramenu, menu);
		return true;
	}

	// Zerbitzua abiatuta badago ez utzi konfigurazioa aldatzen
	// lehenego gelditu eta gero aldatu konfigurazioa
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		int menu_luz = menu.size();

		for (int i = 0; i < menu_luz; i++) {
			MenuItem item = menu.getItem(i);
			if (!menu
					.getItem(i)
					.getTitle()
					.equals(getResources().getString(
							R.string.menu_item_laguntza))
					&& !menu.getItem(i)
							.getTitle()
							.equals(getResources().getString(
									R.string.menu_item_honiburuz)))
				item.setEnabled(!geldituBotoia.isEnabled());
		}
		return true;
	}

	/*
	 * Soilik erabilgarri zerbitzua geldirik dagoenean
	 * 
	 * @see android.app.Activity#onOptionsItemSelected(android.view.MenuItem)
	 */
	@SuppressWarnings("deprecation")
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// lortu android bertsioa
		int version = android.os.Build.VERSION.SDK_INT;
		Display display = getWindowManager().getDefaultDisplay();
		int width;
		int height;
		if (version >= 13) {
			Point size = new Point();
			display.getSize(size);
			width = size.x;
			height = size.y;
		} else {
			width = display.getWidth();
			height = display.getHeight();
		} // Menuaren aukerak kudeatu
		switch (item.getItemId()) {
		case R.id.jakintzakitem: // Jakintza-arloa aukeraketa
			final jakintzaAukeraketaDialogoa jakintzaDialog = new jakintzaAukeraketaDialogoa(
					context, jakintzakarray);
			Dialog dialog = jakintzaDialog.getDialogoa();
			dialog.setTitle(getResources().getString(
					R.string.jakintzakDialogIzenburua));
			dialog.setOnDismissListener(new OnDismissListener() {
				@SuppressWarnings("unchecked")
				@Override
				public void onDismiss(DialogInterface dialog) {
					ListView lv = (ListView) ((Dialog) dialog)
							.findViewById(R.id.jakintza_zerrenda);
					JakintzaZerrendaAdaptadorea adapter = (JakintzaZerrendaAdaptadorea) lv
							.getAdapter();
					ArrayList<Object> jakintzaHautatuakMap = adapter
							.getSelectedEtaString();
					jakintzaHautatuak = (ArrayList<jakintzaZerrendaModeloa>) jakintzaHautatuakMap
							.get(0);
					String testua = (String) jakintzaHautatuakMap.get(1);
					textViewJakintzakHautatuak.setText(testua);
					if (!jakintzaDialog.getEzetzi()) {
						konf.setJakintzak(jakintzaHautatuak);
						if (!konfigurazioaGorde.isEnabled()
								&& konf.konfigurazioaZuzena())
							konfigurazioaGorde.setEnabled(true);
						if (abiatuBotoia.isEnabled()) // konfigurazioa gorde
														// arte
														// ezin
														// da zerbitzua abiatu
							abiatuBotoia.setEnabled(false);
					}
				}
			});
			// Dialogoaren tamaina ezarri
			dialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
			dialog.show();
			return true;
		case R.id.lekuakitem: // Ikastaro lekuak aukeraketa
			final ikastaroLekuakDialog lekuakDialog = new ikastaroLekuakDialog(
					context, getResources().getString(
							R.string.lekuakDialogIzenburua), lekuakArray);

			lekuakDialog.getWindow().setLayout((6 * width) / 7,
					(4 * height) / 5);
			lekuakDialog.setOnDismissListener(new OnDismissListener() {
				@SuppressWarnings("unchecked")
				public void onDismiss(final DialogInterface dialog) {
					// Lekuak aukeratzeko dialogoa itxiko denean aukeratutakoak
					// gorde
					// eta pantailan erakutsi

					// Array-ak bi elementu ditu moten zerrenda eta moten
					// zerrenda testu moduan
					ArrayList<Object> lekuakHautatuakMap = lekuakDialog
							.getSelectedMotakEtaString();
					lekuakHautatuak = (ArrayList<ikastaroLekua>) lekuakHautatuakMap
							.get(0);
					String testua = (String) lekuakHautatuakMap.get(1);
					textViewLekuakHautatuak.setText(testua);
					if (!lekuakDialog.getEzetzi()) {
						// gaitu/desgaitu beharrezko botoiak
						konf.setLekuak(lekuakHautatuak); // konfigurazioa
															// eguneratu
															// baina gorde gabe
						if (!konfigurazioaGorde.isEnabled()
								&& konf.konfigurazioaZuzena())
							konfigurazioaGorde.setEnabled(true);
						if (abiatuBotoia.isEnabled()) // konfigurazioa gorde
														// arte
														// ezin da zerbitzua
														// abiatu
							abiatuBotoia.setEnabled(false);
					}
				}
			});
			lekuakDialog.show();

			return true;
		case R.id.ikastaroMotakitem: // ikastaro-moten aukeraketa
			final ikastaroMotakDialog motaDialog = new ikastaroMotakDialog(
					context, getResources().getString(
							R.string.motakDialogIzenburua), ikastaroMotakArray);

			motaDialog.getWindow().setLayout((6 * width) / 7, (4 * height) / 5);
			motaDialog.setOnDismissListener(new OnDismissListener() {
				@SuppressWarnings("unchecked")
				public void onDismiss(final DialogInterface dialog) {
					// Array-ak bi elementu ditu moten derrenda eta moten
					// zerrenda testu moduan
					ArrayList<Object> ikastaroMotakHautatuakMap = motaDialog
							.getSelectedMotakEtaString();
					ikastaroMotakHautatuak = (ArrayList<IkastaroMota>) ikastaroMotakHautatuakMap
							.get(0);
					String testua = (String) ikastaroMotakHautatuakMap.get(1);
					textViewMotakHautatuak.setText(testua);
					if (!motaDialog.getEzetzi()) {
						System.out.println("Ez da ezetzi");
						// gaitu/desgaitu beharrezko botoiak
						konf.setIkataroMotak(ikastaroMotakHautatuak);
						if (!konfigurazioaGorde.isEnabled()
								&& konf.konfigurazioaZuzena())
							konfigurazioaGorde.setEnabled(true);
						if (abiatuBotoia.isEnabled()) // konfigurazioa gorde
														// arte
														// ezin da zerbitzua
														// abiatu
							abiatuBotoia.setEnabled(false);
					}
				}
			});
			motaDialog.show();

			return true;
		case R.id.maiztasunaitem: // Maiztasun aukeraketa
			final MaiztasunaDialog maiztasunDlg = new MaiztasunaDialog(context,
					R.string.maiztasuna_prompt, R.array.maiztasuna_arrays,
					maiztasuna_segundo);
			Dialog maiztasunDialog = maiztasunDlg.getDialog();
			maiztasunDialog.setOnDismissListener(new OnDismissListener() {
				public void onDismiss(final DialogInterface dialog) {
					int emaitza_maiztasun = maiztasunDlg.getMaiztasuna();
					if (maiztasuna_segundo != emaitza_maiztasun) { // maiztasun
																	// diferente
																	// bat
																	// aukeratu
																	// bada
						maiztasuna_segundo = emaitza_maiztasun;

						// Erakutsi aukeratutako maiztasuna hasierako pantailan
						maiztasun_hautatua.setText(EskaeraKonfigurazioa
								.maiztasunaToMaiztasunaText(maiztasuna_segundo));
						// gaitu/desgaitu beharrezko botoiak
						konf.setMaiztasuna(maiztasuna_segundo);
						if (konfigurazioaGorde != null
								&& !konfigurazioaGorde.isEnabled()
								&& konf.konfigurazioaZuzena())
							konfigurazioaGorde.setEnabled(true);
						if (abiatuBotoia.isEnabled()) // konfigurazioa gorde
														// arte
														// ezin da zerbitzua
														// abiatu
							abiatuBotoia.setEnabled(false);
					}
				}
			});
			maiztasunDialog.show();
			return true;
		case R.id.laguntza:
			Intent intent_laguntza = new Intent(context, Laguntza.class);
			startActivity(intent_laguntza);
			return true;

		case R.id.honi_buruz:
			Intent intent_honiburuz = new Intent(context, Honiburuz.class);
			startActivity(intent_honiburuz);
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}

	private void konfigurazioaGorde() {
		konf = new EskaeraKonfigurazioa(jakintzaHautatuak,
				ikastaroMotakHautatuak, lekuakHautatuak, maiztasuna_segundo,
				this.context);
		System.out.println(konf.toString());
		Boolean zuzena = konf.konfigurazioaGorde();
		if (zuzena) { // errorerik egon ez bada konfigurazioa gordetzean eta
						// konfigurazioa zuzena bada
			if (!geldituBotoia.isEnabled()) { // Martxan dagoen zerbitzua
												// gelditu aurretik
				abiatuBotoia.setClickable(true);
				abiatuBotoia.setEnabled(true);
			}
			konfigurazioaGordea = true;
		} else {
			// Erakutsi alerta bat konfigurazioa aukeratzeko
			// Konfigurazio aukera guztiak zehaztu
			konfigurazioaGordea = false;
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setMessage(getResources().getString(R.string.konf_errorea));
			builder.setCancelable(true);
			builder.setPositiveButton(getResources().getString(R.string.ados),
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int id) {
							dialog.cancel();
						}
					});

			AlertDialog alertaDialogoa = builder.create();
			alertaDialogoa.show();
		}
	}

	/*
	 * Konfigurazio aukerak kargatu saretik eta konfigurazioa zuzena den
	 * bueltatu
	 */
	private Boolean kargatuKonfigurazioAukerak(final ikastaroEskuratzailea api) {
		// lehendik konfigurazio aukeraren bat egon bada aukeratuta, kargatu
		// konf = new EskaeraKonfigurazioa(this.context);

		// lehendik konfigurazio aukeraren bat egon bada aukeratuta, kargatu
		String testua = "";
		// lekuak
		lekuakHautatuak = konf.getIkastaroLekuak();
		testua = "";
		testua = TextUtils.join(", ", lekuakHautatuak); // IkastaroLekua.toString()
														// erabiltzen du
		textViewLekuakHautatuak.setText(testua);

		// ikastaroMotak
		ikastaroMotakHautatuak = konf.getIkastaroMotak();
		testua = "";
		testua = TextUtils.join(", ", ikastaroMotakHautatuak);
		textViewMotakHautatuak.setText(testua);

		// jakintza arloak
		jakintzaHautatuak = konf.getJakintzak();
		testua = "";
		testua = TextUtils.join(", ", jakintzaHautatuak);
		textViewJakintzakHautatuak.setText(testua);

		// Maiztasuna
		maiztasuna_segundo = konf.getMaiztasuna();
		maiztasun_hautatua.setText(EskaeraKonfigurazioa
				.maiztasunaToMaiztasunaText(maiztasuna_segundo));
		// TODO agian hobeto zerbitzu batekin
		new Thread(new Runnable() {
			public void run() {
				try {
					ikastaroMotakArray = api
							.getIkastaroMotak(ikastaroMotakHautatuak);
					lekuakArray = api.getIkastaroLekuak(lekuakHautatuak);
					jakintzakarray = api
							.getJakintzaAzpiArloak(jakintzaHautatuak);
					handler.sendEmptyMessage(0);
				} catch (IOException io) {
					handler.sendEmptyMessage(1);

				} catch (XmlPullParserException e) {
					handler.sendEmptyMessage(1);
				}

			}
		}).start();
		return konf.konfigurazioaZuzena();
	}
}
