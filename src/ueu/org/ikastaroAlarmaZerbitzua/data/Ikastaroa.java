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

import java.util.Hashtable;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * APItik ikastaroak objektuak jasotzeko eta gero SharedPreferences-n bidez gordetzeko
 */
public class Ikastaroa implements Parcelable, KvmSerializable {

	public static final Class<? extends Ikastaroa> IKASTAROA_CLASS = new Ikastaroa()
			.getClass();
	private String izenburua = "";
	private String hasiera_data = "";
	private String bukaera_data = "";
	private String izaera = "";
	private String ordu_kop = "";
	private String ordutegia = "";
	private String matrikula_hasiera_data = "";
	private String matrikula_bukaera_data = "";
	private String tokia = "";
	private String sarrera = "";
	private Integer id = -1;

	public Ikastaroa() {
	};

	private Ikastaroa(Parcel in) {
		izenburua = in.readString();
		hasiera_data = in.readString();
		bukaera_data = in.readString();
		izaera = in.readString();
		ordu_kop = in.readString();
		ordutegia = in.readString();
		matrikula_hasiera_data = in.readString();
		matrikula_bukaera_data = in.readString();
		tokia = in.readString();
		sarrera = in.readString();
		id = in.readInt();
	}

	public Ikastaroa(String izenburua, Integer id) {
		this.hasiera_data = "";
		this.izenburua = izenburua;
		this.id = id;
	}

	public Ikastaroa(String izenburua, String hasiera_data, Integer id) {
		this.izenburua = izenburua;
		this.hasiera_data = hasiera_data;
		this.id = id;
	}

	// Setterrak
	public void setIzenburua(String izenburua) {
		this.izenburua = izenburua;
	}

	public void setHasieraData(String hasiera_data) {
		this.hasiera_data = hasiera_data;
	}

	public void setBukaeraData(String bukaera_data) {
		this.bukaera_data = bukaera_data;
	}

	public void setIzaeraData(String izaera) {
		this.izaera = izaera;
	}

	public void setMatrikulaHasieraData(String matrikula_hasiera_data) {
		this.matrikula_hasiera_data = matrikula_hasiera_data;
	}

	public void setMatrikulaBukaeraData(String matrikula_hasiera_data) {
		this.matrikula_hasiera_data = matrikula_hasiera_data;
	}

	public void setOrdutegia(String ordutegia) {
		this.ordutegia = ordutegia;
	}

	public void setOrduKopurua(String ordu_kop) {
		this.ordu_kop = ordu_kop;
	}

	public void setTokia(String tokia) {
		this.tokia = tokia;
	}

	public void setSarrera(String sarrera) {
		this.sarrera = sarrera;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getIzenburua() {
		return this.izenburua;
	}

	public String getHasieraData() {
		return this.hasiera_data;
	}

	public String getBukaeraData() {
		return this.bukaera_data;
	}

	public String getIzaeraData() {
		return this.izaera;
	}

	public String getMatrikulaHasieraData() {
		return this.matrikula_hasiera_data;
	}

	public String getMatrikulaBukaeraData() {
		return this.matrikula_hasiera_data;
	}

	public String getOrdutegia() {
		return this.ordutegia;
	}

	public String getOrduKopurua() {
		return this.ordu_kop;
	}

	public String getTokia() {
		return this.tokia;
	}

	public String getSarrera() {
		return this.sarrera;
	}

	public Integer getId() {
		return this.id;
	}

	@Override
	public boolean equals(Object v) {
		boolean retVal = false;

		if (v instanceof Ikastaroa) {
			Ikastaroa ptr = (Ikastaroa) v;
			retVal = ptr.id.intValue() == this.id;
		}

		return retVal;
	}

	@Override
	public int hashCode() {
		int hash = 7;
		hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
		return hash;
	}

	@Override
	public String toString() {
		return this.izenburua + " , " + this.hasiera_data + " , " + this.id;
	}

	@Override
	public int describeContents() {
		return 0;
	}
	
	/*
	 * Parcelable
	 */
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		arg0.writeString(izenburua);
		arg0.writeString(hasiera_data);
		arg0.writeString(bukaera_data);
		arg0.writeString(izaera);
		arg0.writeString(ordu_kop);
		arg0.writeString(ordutegia);
		arg0.writeString(matrikula_hasiera_data);
		arg0.writeString(matrikula_bukaera_data);
		arg0.writeString(tokia);
		arg0.writeString(sarrera);
		arg0.writeInt(id);
	}

	public static final Parcelable.Creator<Ikastaroa> CREATOR = new Parcelable.Creator<Ikastaroa>() {
		public Ikastaroa createFromParcel(Parcel in) {
			return new Ikastaroa(in);
		}

		public Ikastaroa[] newArray(int size) {
			return new Ikastaroa[size];
		}
	};

	// kvmserialize

	@Override
	public Object getProperty(int arg0) {
		switch (arg0) {
		case 0:
			return izenburua;
		case 1:
			return hasiera_data;
		case 2:
			return bukaera_data;
		case 3:
			return izaera;
		case 4:
			return ordu_kop;
		case 5:
			return ordutegia;
		case 6:
			return matrikula_hasiera_data;
		case 7:
			return matrikula_bukaera_data;
		case 8:
			return tokia;
		case 9:
			return sarrera;
		case 10:
			return id;
		}

		return null;
	}

	@Override
	public int getPropertyCount() {
		return 11;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
		switch (index) {
		case 0:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "izenburua";
			break;
		case 1:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "hasiera_data";
			break;
		case 2:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "bukaera_data";
			break;
		case 3:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "izaera";
			break;
		case 4:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ordu_kop";
			break;
		case 5:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "ordutegia";
			break;
		case 6:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "matrikula_hasiera_data";
			break;
		case 7:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "matrikula_bukaera_data";
			break;
		case 8:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "tokia";
			break;
		case 9:
			info.type = PropertyInfo.STRING_CLASS;
			info.name = "sarrera";
			break;
		case 10:
			info.type = PropertyInfo.INTEGER_CLASS;
			info.name = "id";
			break;
		default:
			break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		switch (index) {
		case 0:
			izenburua = value.toString();
			break;
		case 1:
			hasiera_data = value.toString();
			break;
		case 2:
			bukaera_data = value.toString();
			break;
		case 3:
			izaera = value.toString();
			break;
		case 4:
			ordu_kop = value.toString();
			break;
		case 5:
			ordutegia = value.toString();
			break;
		case 6:
			matrikula_hasiera_data = value.toString();
			break;
		case 7:
			matrikula_bukaera_data = value.toString();
			break;
		case 8:
			tokia = value.toString();
			break;
		case 9:
			sarrera = value.toString();
			break;
		case 10:
			id = Integer.parseInt(value.toString());
			break;
		default:
			break;
		}

	}

}