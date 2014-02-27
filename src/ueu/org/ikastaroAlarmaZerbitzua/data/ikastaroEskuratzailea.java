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

import ueu.org.IkastaroAlarmaZerbitzua.Konstanteak;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
//import java.util.Vector;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;


/* 
 * SOAP API bezero klasea
 */
public class ikastaroEskuratzailea {

	/*
	 *  ikastaroak lekuak, ikastaroMotak eta jakintzen arabera iragaziak
	 */
	public ArrayList<Ikastaroa> getIkastaroak(List<?> lekuak, List<?> ikastaroMotak, List<?> jakintzaArloak) throws IOException, XmlPullParserException {
		ArrayList<Ikastaroa> ikastaroak = new ArrayList<Ikastaroa>();
		// Soap eskaera hasieratu + parametroak gehitu
		SoapObject request = new SoapObject(Konstanteak.API_NAMESPACE, Konstanteak.API_ERAGIKETA_IKASTAROAKIRAGAZKIAK);

		// parametroak gehitu
		
		request.addProperty("ordena", true);
		//Prestatu lekuen zerrenda APIra bidaltzeko
		if(lekuak != null && lekuak.size() > 0){ //lekuak konfigurazio aukera hautatu bada
			SoapObject soapLekuak = new SoapObject(Konstanteak.API_NAMESPACE, "lekuak");
			for (Object lekua : lekuak){
				soapLekuak.addProperty("string", ((ikastaroLekua)lekua).getIzena() );
			}
			request.addSoapObject(soapLekuak);
		}
		
		//Hautatutako IkastaroMota izenak bidali APIari
		SoapObject soapIkastaroMotak = new SoapObject(Konstanteak.API_NAMESPACE, "ikastaroMotak");
		for (Object mota : ikastaroMotak){
			soapIkastaroMotak.addProperty("string", ((IkastaroMota) mota).getIzena() );
		}		
		request.addSoapObject(soapIkastaroMotak);
		
		//Hautatuako Jakintza-arloak bidali APIari
		SoapObject soapJakintzaArloak = new SoapObject(Konstanteak.API_NAMESPACE, "jakintzaArloak");
		for (Object jk : jakintzaArloak){
			soapJakintzaArloak.addProperty("int", ((jakintzaZerrendaModeloa) jk).getPK() );
		}		
		request.addSoapObject(soapJakintzaArloak);		


		// SOAP eskaeraren bertsioa zehaztu
		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER12);
		envelope.dotNet = true;
		envelope.addMapping(Konstanteak.API_NAMESPACE, "Ikastaroa",
				Ikastaroa.IKASTAROA_CLASS);
		envelope.setOutputSoapObject(request);

		// Interneterako deia beharrezkoa
		HttpTransportSE androidHttpTransport = new HttpTransportSE(Konstanteak.API_URL);
		androidHttpTransport.debug = true;

		// Hemen web-zerbituari deia egiten zaio
		androidHttpTransport.call(Konstanteak.API_URL + "#" + Konstanteak.API_ERAGIKETA_IKASTAROAKIRAGAZKIAK, envelope);
		
		// Eskuratu SOAP erantzuna estalkiaren gorputzetik eta ikastaroen zerrenda eraiki
		if (!(envelope.bodyIn instanceof SoapFault)) {
			SoapObject result = (SoapObject) envelope.bodyIn;
			if (result != null) {
				// return SoapObject eskuratu
				SoapObject ikastaro_zerrenda = (SoapObject) result.getProperty(1);
				for(int i=0; i<ikastaro_zerrenda.getPropertyCount();i++){
					Ikastaroa ikastaroa = (Ikastaroa) ikastaro_zerrenda.getProperty(i);
					ikastaroak.add(ikastaroa);
				}
			}
		}		
		return ikastaroak;
		
	}

	/*
	 *  APItik aukeran dauden ikastaro lekuak eskuratu
	 */
	public ArrayList<ikastaroLekua> getIkastaroLekuak(List<ikastaroLekua> lekuakHautatuak) throws IOException, XmlPullParserException {
		ArrayList<ikastaroLekua> lekuak = new ArrayList<ikastaroLekua>();
		SoapObject request = new SoapObject(Konstanteak.API_NAMESPACE, Konstanteak.API_ERAGIKETA_IKASTAROAKLEKUAK);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER12);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(Konstanteak.API_URL);
		androidHttpTransport.debug = true;

		androidHttpTransport.call(Konstanteak.API_URL + "#" + Konstanteak.API_ERAGIKETA_IKASTAROAKLEKUAK, envelope);


		// Ikastaro lekuak zerrenda eraiki SOAP objektutik eta hautatua dagoen zehaztu
		if (!(envelope.bodyIn instanceof SoapFault)) {
			SoapObject result = (SoapObject) envelope.bodyIn;
			if (result != null) {
				SoapObject lekuakSoap_v = (SoapObject) result
						.getProperty("return");
				for (int i = 0; i < lekuakSoap_v.getPropertyCount(); i++) {
					String leku_izena = lekuakSoap_v.getProperty(i).toString();
					ikastaroLekua lekua = new ikastaroLekua(leku_izena ,i);
					if(lekuakHautatuak.contains(lekua)){
						lekua.setSelected(true);
					}
					lekuak.add(lekua);
				}
			}
		}
		return lekuak;

	}

	/*
	 *  APItik aukeran dauden ikastaro motak eskuratu
	 */
	public ArrayList<IkastaroMota> getIkastaroMotak(ArrayList<IkastaroMota> ikastaroMotakHautatuak) throws IOException, XmlPullParserException {
		IkastaroMota mota = null;
		ArrayList<IkastaroMota> motak = new ArrayList<IkastaroMota>();

		SoapObject request = new SoapObject(Konstanteak.API_NAMESPACE, Konstanteak.API_ERAGIKETA_IKASTAROAKMOTAK);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER12);
		envelope.dotNet = true;

		envelope.setOutputSoapObject(request);
		// SOAP objektuak serializatzeko mapeoak
		envelope.addMapping(Konstanteak.API_NAMESPACE, "ikastaroakmotakResponse",
				IkastaroakMotakResponse.IKASTAROMOTAK_RESPONSE_CLASS);
		envelope.addMapping(Konstanteak.API_NAMESPACE, "ArrayOfIkastaromota",
				ArrayOfIkastaroMota.ARRAYOFIKASTAROMOTA_CLASS);
		envelope.addMapping(Konstanteak.API_NAMESPACE, "IkastaroMota",
				IkastaroMota.IKASTAROMOTA_CLASS);
		HttpTransportSE androidHttpTransport = new HttpTransportSE(Konstanteak.API_URL);
		androidHttpTransport.debug = true;

		androidHttpTransport.call(Konstanteak.API_URL + "#" + Konstanteak.API_ERAGIKETA_IKASTAROAKMOTAK, envelope);

		//eraiki ikastaro-moten zerrenda soap objektuetatik eta hautatua dagoen zehaztu
		if (!(envelope.bodyIn instanceof SoapFault)) {
			IkastaroakMotakResponse ikastaroakmotakresponse = (IkastaroakMotakResponse) envelope.bodyIn;
			if (ikastaroakmotakresponse != null){
				ArrayOfIkastaroMota mota_array = (ArrayOfIkastaroMota) ikastaroakmotakresponse
					.getProperty(1);

				for (int i = 0; i < mota_array.size(); i++) {
					mota = (IkastaroMota) mota_array.getProperty(i);
					if(ikastaroMotakHautatuak.contains(mota)){
						mota.setSelected(true);
					}
					motak.add(mota);
				}
			}
		}
		return motak;

	}
	
	/*
	 *  APItik aukeran dauden ikastaro jakintza-arloak eskuratu
	 */
	public ArrayList<jakintzaZerrendaModeloa> getJakintzaAzpiArloak(ArrayList<jakintzaZerrendaModeloa> jakintzaHautatuak) throws IOException, XmlPullParserException {
		ArrayList<jakintzaZerrendaModeloa> jakintzak = new ArrayList<jakintzaZerrendaModeloa>();
		SoapObject request = new SoapObject(Konstanteak.API_NAMESPACE, Konstanteak.API_ERAGIKETA_IKASTAROAKJAKINTZAK);

		SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER12);
		envelope.dotNet = true;
		envelope.setOutputSoapObject(request);

		HttpTransportSE androidHttpTransport = new HttpTransportSE(Konstanteak.API_URL);
		androidHttpTransport.debug = true;

		androidHttpTransport.call(Konstanteak.API_URL + "#" + Konstanteak.API_ERAGIKETA_IKASTAROAKJAKINTZAK , envelope);

		//eraiki jakintzeb zerrenda soap objektuetatik  eta hautatua dagoen zehaztu
		if (!(envelope.bodyIn instanceof SoapFault)) {
			SoapObject result = (SoapObject) envelope.bodyIn;
			if (result != null) {
				SoapObject jakintzak_soap = (SoapObject) result
						.getProperty("return");
				for (int i = 0; i < jakintzak_soap.getPropertyCount(); i++) {
					jakintzaZerrendaModeloa jakintza = new jakintzaZerrendaModeloa(((SoapObject) jakintzak_soap.getProperty(i)).getProperty("izena").toString()
							,Integer.valueOf(((SoapObject) jakintzak_soap.getProperty(i)).getProperty("pk").toString()));
					if(jakintzaHautatuak.contains(jakintza)){
						jakintza.setSelected(true);
					}
					jakintzak.add(jakintza);
				}
			}
		}
		return jakintzak;

	}	
}
