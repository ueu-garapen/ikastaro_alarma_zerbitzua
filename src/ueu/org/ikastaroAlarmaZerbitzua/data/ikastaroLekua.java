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

//Ikastaro lekua modeloa
public class ikastaroLekua {
	private String izena = "";
	private Integer id = -1;
	private boolean aukeratua;
	
	public ikastaroLekua(){	}
	
	public ikastaroLekua(String izena, Integer id){
		this.izena = izena;
		this.id = id;
	}
	
	public String getIzena(){
		return this.izena;
	}
	
	public Integer getId(){
		return this.id;
	}
	
	public void setIzena(String izena){
		this.izena = izena;
	}
	
	public void setId(Integer id){
		this.id = id;
	}
	
	public void setSelected(boolean selected) {
		this.aukeratua = selected;
	}

	public boolean isSelected() {
		return aukeratua;
	}

	//contains metodoak erabiltzeko
    @Override
    public boolean equals(Object v) {
    	boolean retVal = false;

        if (v instanceof ikastaroLekua){
        	ikastaroLekua ptr = (ikastaroLekua) v;
            retVal = ptr.izena.equals(this.izena);
        }

        return retVal;
    }	
    
   //contains metodoak erabiltzeko
    @Override
    public int hashCode() {
        int hash = 7;
        hash = 17 * hash + (this.id != null ? this.id.hashCode() : 0);
        return hash;
    }
    
    @Override
    public String toString(){
    	return this.izena;
    }
}
