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
 * Serializagarria APItik jaso eta bidaltzeko. Ikastaro mota modeloa
 */
public class IkastaroMota implements KvmSerializable {
	private String izena = "";
	private Integer pk = -1;
	private boolean aukeratua;

	public static final Class<? extends IkastaroMota> IKASTAROMOTA_CLASS = new IkastaroMota().getClass();
	
	public IkastaroMota(){};
	
	public IkastaroMota(String izena, Integer id){
		this.izena = izena;
		this.pk = id;
	}
	
	public String getIzena(){
		return this.izena;
	}
	
	public Integer getId(){
		return this.pk;
	}
	
	public void setIzena(String izena){
		this.izena = izena;
	}
	
	public void setId(Integer id){
		this.pk = id;
	}
	
	public boolean isSelected() {
		return aukeratua;
	}

	public void setSelected(boolean selected) {
		this.aukeratua = selected;
	}
	
	@Override
	public String toString(){
		return this.izena;
	}

	@Override
	public Object getProperty(int arg0) {
	       switch(arg0)
	        {
	        case 0:
	            return izena;
	        case 1:
	            return pk;
	        }
	        
	        return null;
	}

	@Override
	public int getPropertyCount() {
		return 2;
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void getPropertyInfo(int index, Hashtable arg1, PropertyInfo info) {
        switch(index)
        {
        case 0:
            info.type = PropertyInfo.STRING_CLASS;
            info.name = "izena";
            break;
        case 1:
            info.type = PropertyInfo.INTEGER_CLASS;
            info.name = "pk";
            break;
        default:break;
        }
		
	}

	@Override
	public void setProperty(int index, Object value) {
        switch(index)
        {
        case 0:
            izena = value.toString();
            break;
        case 1:
            pk = Integer.parseInt(value.toString());
            break;
        default:
            break;
        }
		
	}
	
	//contains metodoak erabiltzeko equals eta hashCode
    @Override
    public boolean equals(Object v) {
    	boolean retVal = false;

        if (v instanceof IkastaroMota){
        	IkastaroMota ptr = (IkastaroMota) v;
            retVal = ptr.izena.equals(this.izena);
        }

        return retVal;
    }	
    
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.pk != null ? this.pk.hashCode() : 0);
        return hash;
    }	

}
