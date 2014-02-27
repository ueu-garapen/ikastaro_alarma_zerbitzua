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

public class jakintzaZerrendaModeloa implements Comparable<jakintzaZerrendaModeloa>{

	private String jakintza;
	private boolean aukeratua;
	private Integer pk;

	public jakintzaZerrendaModeloa(){}
	
	public jakintzaZerrendaModeloa(String jakintza) {
		this.jakintza = jakintza;
		aukeratua = false;
	}

	public jakintzaZerrendaModeloa(String jakintza, int pk) {
		this.jakintza = jakintza;
		aukeratua = false;
		this.pk = pk;
	}	
	
	public String getName() {
		return jakintza;
	}

	public void setName(String name) {
		this.jakintza = name;
	}

	public boolean isSelected() {
		return aukeratua;
	}

	public void setSelected(boolean selected) {
		this.aukeratua = selected;
	}
	
	public int getPK(){
		return this.pk;
	}
	
	public void setPK(int pk){
		this.pk = pk;
	}


	@Override
	public int compareTo(jakintzaZerrendaModeloa another) {
	    return this.jakintza.compareTo(another.jakintza);
	}
	@Override
	public String toString(){
		return this.jakintza;
	}
	
	//contains metodoak erabiltzeko
    @Override
    public boolean equals(Object v) {
    	boolean retVal = false;

        if (v instanceof jakintzaZerrendaModeloa){
        	jakintzaZerrendaModeloa ptr = (jakintzaZerrendaModeloa) v;
            retVal = ptr.jakintza.equals(this.jakintza);
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