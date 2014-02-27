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

public class Konstanteak {
	public static final int MAIZTASUNA_DEFAULT = 300;
	public static String API_NAMESPACE = "http://www.ueu.org/soap";
	public static String API_URL = "http://www.ueu.org/soap";
	public static String KONFIGURAZIOA = "konfigurazioa";
	public static String KONFIGURAZIOA_LEKUAK = "lekuakHautatuak";
	public static String KONFIGURAZIOA_MOTAK = "ikastaroMotakHautatuak";
	public static String KONFIGURAZIOA_JAKINTZAK = "jakintzaHautatuak";
	public static String KONFIGURAZIOA_MAIZTASUNA = "maiztasunHautatua";
	public static String KONFIGURAZIOA_ABIATUTA = "zerbitzuaAbiatuta";
	public static String IKASTARO_JASOAK = "ikastaroJasoakId";
	public static String ZERBITZU_PARAMETROAK_LUZERA = "luzera";
	public static String ZERBITZU_PARAMETROAK_IKASTAROAK = "ikastaroa-";
	public static String ZERBITZU_PARAMETROAK_IKASTAROA = "ikastaroa";
	
	public static String API_ERAGIKETA_IKASTAROAKIRAGAZKIAK = "ikastaroakiragazkiak";
	public static String API_ERAGIKETA_IKASTAROAKLEKUAK = "ikastaroaklekuak";
	public static String API_ERAGIKETA_IKASTAROAKMOTAK = "ikastaroakmotak";	
	public static String API_ERAGIKETA_IKASTAROAKJAKINTZAK = "ikastaroakjakintzak";
	
	public static String IKASTARO_URL = "http://www.ueu.org/ikasi/ikastaroa-ikusi/";
	
	
	public enum NetworkState
	{ 
	    Unknown,
	    ConnectedWifi,
	    ConnectedData,
	    Disconnected
	}
}
