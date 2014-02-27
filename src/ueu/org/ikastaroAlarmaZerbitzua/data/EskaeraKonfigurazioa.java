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

import java.util.ArrayList;
import java.util.List;

import ueu.org.IkastaroAlarmaZerbitzua.Konstanteak;

import android.content.Context;
import android.content.SharedPreferences;

/*
 * Aplikazioaren konfigurazioa kudeatzeko klasea
 */
public class EskaeraKonfigurazioa {

	private ArrayList<jakintzaZerrendaModeloa> jakintzaArloak = null;
	private ArrayList<IkastaroMota> ikastaroMotak = null;
	private List<ikastaroLekua> ikastaroLekuak = null;
	private int maiztasuna = Konstanteak.MAIZTASUNA_DEFAULT;
	private Context context = null;
	private Boolean gordeta = false;

	public EskaeraKonfigurazioa(Context context) {
		this.context = context;
		loadArrayLekuak();
		loadArrayJakintzak();
		loadArrayIkastaroMotak();
		loadMaiztasunKonf();
		this.setGordeta(true);
	}

	public EskaeraKonfigurazioa(
			ArrayList<jakintzaZerrendaModeloa> jakintzaArloak,
			ArrayList<IkastaroMota> ikastaroMotak,
			List<ikastaroLekua> ikastaroLekuak, int maiztasuna, Context context) {
		this.ikastaroLekuak = ikastaroLekuak;
		this.ikastaroMotak = ikastaroMotak;
		this.jakintzaArloak = jakintzaArloak;
		this.maiztasuna = maiztasuna;
		this.context = context;
	}

	public ArrayList<jakintzaZerrendaModeloa> getJakintzak() {
		return jakintzaArloak;
	}

	public ArrayList<IkastaroMota> getIkastaroMotak() {
		return ikastaroMotak;
	}

	public List<ikastaroLekua> getIkastaroLekuak() {
		return ikastaroLekuak;
	}

	public int getMaiztasuna() {
		return maiztasuna;
	}

	public void setJakintzak(ArrayList<jakintzaZerrendaModeloa> jakintzak) {
		this.jakintzaArloak = jakintzak;
	}

	public void setLekuak(List<ikastaroLekua> lekuakHautatuak) {
		this.ikastaroLekuak = lekuakHautatuak;
	}

	public void setIkataroMotak(ArrayList<IkastaroMota> ikastaroMotak) {
		this.ikastaroMotak = ikastaroMotak;

	}

	public void setMaiztasuna(int maiztasuna) {
		this.maiztasuna = maiztasuna;

	}

	public void setContext(Context context) {
		this.context = context;
	}

	public Boolean getGordeta() {
		return gordeta;
	}

	public void setGordeta(Boolean gordeta) {
		this.gordeta = gordeta;
	}
	
	public Boolean konfigurazioaGorde() {
		if (konfigurazioaZuzena()) {
			this.setGordeta(true);
			return saveArray(this.ikastaroLekuak, Konstanteak.KONFIGURAZIOA_LEKUAK) &&
			saveArray(this.ikastaroLekuak, Konstanteak.KONFIGURAZIOA_LEKUAK) &&
			saveArray(this.ikastaroMotak, Konstanteak.KONFIGURAZIOA_MOTAK) &&
			saveArray(this.jakintzaArloak, Konstanteak.KONFIGURAZIOA_JAKINTZAK) &&
			saveMaiztasuna(Konstanteak.KONFIGURAZIOA_MAIZTASUNA);
		}else
			//errorea dago konfigurazioa ez delako zuzena
			return false;
	}

	/*
	 * Gorde maiztasuna hurrengoetarako (Shared preferences)
	 */
	private boolean saveMaiztasuna(String izena) {
		SharedPreferences prefs = this.context.getSharedPreferences(
				Konstanteak.KONFIGURAZIOA, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putInt(izena, this.maiztasuna);
		return editor.commit();
	}

	/*
	 * Gorde maiztasuna ikastaro-motak,lekuak edota jakintzak hurrengoetarako
	 * (Shared preferences)
	 */
	private boolean saveArray(List<?> array, String arrayName) {
		SharedPreferences prefs = this.context.getSharedPreferences(
				Konstanteak.KONFIGURAZIOA, Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		int luz = 0;
		if (array != null && !array.isEmpty() && array.size() > 0) {
			luz = array.size();
		}
		//Array-a definiturik ez badago 0 luz gordetzen dugu eta ez ditugu elementuak gordetzen
		editor.putInt(arrayName + "_size", luz);

		if (arrayName.equals(Konstanteak.KONFIGURAZIOA_MOTAK)) { // hautatutako
															// ikastaro motak
			for (int i = 0; i < luz; i++) {
				editor.putString(arrayName + "_izena_" + i,
						((IkastaroMota) array.get(i)).getIzena());
				editor.putInt(arrayName + "_pk_" + i,
						((IkastaroMota) array.get(i)).getId());
			}
		} else if (arrayName.equals(Konstanteak.KONFIGURAZIOA_LEKUAK)) { // hautatutako
															// ikastaro lekuak
			for (int i = 0; i < luz; i++) {
				editor.putString(arrayName + "_" + i,
						((ikastaroLekua) array.get(i)).getIzena());
				// TODO agian pk ere tratatu behar da
			}
		} else if (arrayName.equals(Konstanteak.KONFIGURAZIOA_JAKINTZAK)) {
			for (int i = 0; i < luz; i++) {
				editor.putString(arrayName + "_izena_" + i,
						((jakintzaZerrendaModeloa) array.get(i)).getName());
				editor.putInt(arrayName + "_pk_" + i,
						((jakintzaZerrendaModeloa) array.get(i)).getPK());
			}
		}

		// }
		return editor.commit();
	}

	/*
	 * Gordetako leku-aukeraketa eskuratu
	 */
	private void loadArrayLekuak() {
		SharedPreferences prefs = this.context.getSharedPreferences(
				Konstanteak.KONFIGURAZIOA, Context.MODE_PRIVATE);
		List<ikastaroLekua> arrayLekuak = new ArrayList<ikastaroLekua>();
		int size = prefs.getInt(Konstanteak.KONFIGURAZIOA_LEKUAK + "_size", 0);
		for (int i = 0; i < size; i++) {
			ikastaroLekua lekua = new ikastaroLekua(prefs.getString(
					Konstanteak.KONFIGURAZIOA_LEKUAK + "_" + i, null), i);
			arrayLekuak.add(lekua);
		}
		this.ikastaroLekuak = arrayLekuak;
	}

	/*
	 * Gordetako jakintza-arloak eskuratu
	 */
	public void loadArrayJakintzak() {
		SharedPreferences prefs = this.context.getSharedPreferences(
				Konstanteak.KONFIGURAZIOA, Context.MODE_PRIVATE);
		ArrayList<jakintzaZerrendaModeloa> arrayJakintzak = new ArrayList<jakintzaZerrendaModeloa>();
		int size = prefs.getInt(Konstanteak.KONFIGURAZIOA_JAKINTZAK + "_size", 0);
			for (int i = 0; i < size; i++) {
				jakintzaZerrendaModeloa jakintza = new jakintzaZerrendaModeloa();
				jakintza.setPK(prefs.getInt(Konstanteak.KONFIGURAZIOA_JAKINTZAK + "_pk_" + i, 0));
				jakintza.setName(prefs.getString(Konstanteak.KONFIGURAZIOA_JAKINTZAK + "_izena_" + i,
						null));
				arrayJakintzak.add(jakintza);
			}
		this.jakintzaArloak = arrayJakintzak;
	}

	/*
	 * Gordetako ikastaro motak eskuratu
	 */
	public void loadArrayIkastaroMotak() {
		SharedPreferences prefs = this.context.getSharedPreferences(
				Konstanteak.KONFIGURAZIOA, Context.MODE_PRIVATE);
		ArrayList<IkastaroMota> arrayMotak = new ArrayList<IkastaroMota>();
		int size = prefs.getInt(Konstanteak.KONFIGURAZIOA_MOTAK + "_size", 0);
			for (int i = 0; i < size; i++) {
				IkastaroMota mota = new IkastaroMota();
				mota.setId(prefs.getInt(Konstanteak.KONFIGURAZIOA_MOTAK + "_pk_" + i, 0));
				mota.setIzena(prefs.getString(Konstanteak.KONFIGURAZIOA_MOTAK + "_izena_" + i, null));
				arrayMotak.add(mota);
			}
		this.ikastaroMotak = arrayMotak;
	}

	/*
	 * Gordetako maiztasuna errekuperatu
	 */
	public void loadMaiztasunKonf() {
		SharedPreferences prefs = this.context.getSharedPreferences(
				Konstanteak.KONFIGURAZIOA, Context.MODE_PRIVATE);
		this.maiztasuna = prefs.getInt(Konstanteak.KONFIGURAZIOA_MAIZTASUNA, Konstanteak.MAIZTASUNA_DEFAULT);
	}

	public Boolean konfigurazioaZuzena() {
		Boolean emaitza = false;
		if (this.jakintzaArloak != null && this.jakintzaArloak.size() > 0)
			emaitza = true;
		if (this.ikastaroLekuak != null && this.ikastaroLekuak.size() > 0)
			emaitza = true;
		if (this.ikastaroMotak != null && this.ikastaroMotak.size() > 0)
			emaitza = true;
		return emaitza && this.maiztasuna > 0;
	}

	// Maiztasunaren konbertsioak. Maiztasuna beti segundoetan
	public static int maiztasunaToPos(int maiztasuna) {
		int pos = 0;
		switch (maiztasuna) {
		// case 3600: // 1 ordu
		case 300: // 1 ordu
			pos = 0;
			break;
		case 21600: // 6 ordu
			pos = 1;
			break;
		case 43200: // 12 ordu
			pos = 2;
			break;
		case 86400: // 1 egun
			pos = 3;
			break;
		}
		return pos;
	}

	public static int maiztasunTextToMaiztasuna(String maiztasunText) {
		int maiztasuna = 300; //segundo
		if (maiztasunText.equals("1 ordu")) {
			maiztasuna = 300;
		} else if (maiztasunText.equals("6 ordu")) {
			maiztasuna = 21600;
		} else if (maiztasunText.equals("12 ordu")) {
			maiztasuna = 43200;
		} else if (maiztasunText.equals("1 egun")) {
			maiztasuna = 86400;
		}
		return maiztasuna;
	}

	public static String maiztasunaToMaiztasunaText(int maiztasuna) {
		String maiztasuna_text = "";
		switch (maiztasuna) {
		case 300: // 1 ordu
			maiztasuna_text = "1 ordu";
			break;
		case 21600: // 6 ordu
			maiztasuna_text = "6 ordu";
			break;
		case 43200: // 12 ordu
			maiztasuna_text = "12 ordu";
			break;
		case 86400: // 1 egun
			maiztasuna_text = "1 egun";
			break;
		}
		return maiztasuna_text;
	}
	
	@Override
	public String toString(){
		String emaitza = "";
		if(jakintzaArloak != null)
			emaitza = emaitza + "jakintzak: " + jakintzaArloak.size();
		if(this.ikastaroLekuak != null)
			emaitza = emaitza + "lekuak: " + this.ikastaroLekuak.size();
		if(this.ikastaroMotak != null)
			emaitza = emaitza + "motak: " + this.ikastaroMotak.size();
		emaitza = emaitza + "maiztasuna: " + this.maiztasuna;
		return emaitza;
		
	}

}
