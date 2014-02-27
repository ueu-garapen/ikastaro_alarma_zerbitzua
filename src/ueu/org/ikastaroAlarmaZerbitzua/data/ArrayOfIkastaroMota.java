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
import java.util.Vector;

import org.ksoap2.serialization.KvmSerializable;
import org.ksoap2.serialization.PropertyInfo;


/*
 * APItik datozen emaitza jasotzeko objektuak 
 */
public class ArrayOfIkastaroMota extends Vector<IkastaroMota> implements KvmSerializable {

	private static final long serialVersionUID = 1L;
	public static final Class<? extends ArrayOfIkastaroMota> ARRAYOFIKASTAROMOTA_CLASS = new ArrayOfIkastaroMota()
			.getClass();

	//Soap objektua
	// Array item=IkastaroMota
	@Override
	public Object getProperty(int index) {
		return this.get(index);
	}

	@Override
	public int getPropertyCount() {
		return 1;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int index, Hashtable properties,
			PropertyInfo info) {
		info.name = "item";
		info.type = IkastaroMota.IKASTAROMOTA_CLASS;
	}

	@Override
	public void setProperty(int index, Object value) {
		this.add(((IkastaroMota) value));
	}

}

