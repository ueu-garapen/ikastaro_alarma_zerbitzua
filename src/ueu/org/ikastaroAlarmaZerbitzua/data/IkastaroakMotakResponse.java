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

/*
 * APItik datozen objektuak jasotzeko
 */
public class IkastaroakMotakResponse implements KvmSerializable {

	public static final Class<? extends IkastaroakMotakResponse> IKASTAROMOTAK_RESPONSE_CLASS = new IkastaroakMotakResponse()
			.getClass();
	private ArrayOfIkastaroMota ikastaromotak = new ArrayOfIkastaroMota();
	private String result = "";

	@Override
	public Object getProperty(int index) {
		switch(index){
			case 0:
				return this.result;
			case 1:
				return this.ikastaromotak;
		}
		return null;
	}

	@Override
	public int getPropertyCount() {
		return 2;
	}

	//SOAP objektua
	// return=result; result= ArrayOfIkastaroMota
	
	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int index, Hashtable properties,
			PropertyInfo info) {
		switch(index){
			case 0:
				info.name = "result";
				info.type = String.class;
				break;
			case 1:
				info.name = "return";
				info.type = ArrayOfIkastaroMota.ARRAYOFIKASTAROMOTA_CLASS;
			default:break;
		}
	}

	@Override
	public void setProperty(int index, Object value) {
		  switch(index)
	        {
	        case 0:
	            this.result = value.toString();
	            break;
	        case 1:
	        	this.ikastaromotak = (ArrayOfIkastaroMota) value;
	        	break;
	        default:
	            break;
	        }
	}

}
