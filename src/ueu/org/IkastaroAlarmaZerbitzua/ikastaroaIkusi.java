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

import ueu.org.ikastaroAlarmaZerbitzua.data.Ikastaroa;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ikastaroaIkusi extends Activity{
	TextView textViewIzenburua;
	TextView textViewHasiera;
	TextView textViewBukaera;
	TextView textViewTokia;
	TextView textViewOrdutegia;
	TextView textViewIzaera;
	TextView textViewOrduKop;
	TextView textViewMatrikulaHasiera;
	TextView textViewMatrikulaBukaera;
	WebView webViewSarrera;
	Button bisitatuBotoia;
	Ikastaroa ikastaroa = null;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.ikastaro_fitxa);
		
		//ikastaroaren informazioa eskuratu, zerrendatik jasoa
		ikastaroa = (Ikastaroa) getIntent().getParcelableExtra(
				"ikastaroa");
		
		//prestatu layout-a
		textViewIzenburua = (TextView) findViewById(R.id.IkastaroFitxaIzenburua);
		textViewHasiera = (TextView) findViewById(R.id.IkastaroFitxaHasiera);
		textViewBukaera = (TextView) findViewById(R.id.IkastaroFitxaBukaera);
		textViewTokia = (TextView) findViewById(R.id.IkastaroFitxaTokia);
		textViewOrdutegia = (TextView) findViewById(R.id.IkastaroFitxaOrdutegia);
		textViewIzaera = (TextView) findViewById(R.id.IkastaroFitxaIzaera);
		textViewOrduKop = (TextView) findViewById(R.id.IkastaroFitxaOrdukop);
		textViewMatrikulaHasiera = (TextView) findViewById(R.id.IkastaroFitxaMatrikulaHasiera);
		textViewMatrikulaBukaera = (TextView) findViewById(R.id.IkastaroFitxaMatrikulaBukaera);
		webViewSarrera = (WebView) findViewById(R.id.IkastaroFitxaSarrera2);
		bisitatuBotoia = (Button) findViewById(R.id.buttonUEU);
		
		if(ikastaroa != null){ //ikastaroa hutsik ez badago bistaratu informazioa
			textViewIzenburua.setText(ikastaroa.getIzenburua());
			textViewHasiera.setText(ikastaroa.getHasieraData());
			textViewBukaera.setText(ikastaroa.getBukaeraData());
			textViewTokia.setText(ikastaroa.getTokia());
			textViewOrdutegia.setText(ikastaroa.getOrdutegia());
			textViewIzaera.setText(ikastaroa.getIzaeraData());
			textViewOrduKop.setText(ikastaroa.getOrduKopurua());
			textViewMatrikulaHasiera.setText(ikastaroa.getMatrikulaHasieraData());
			textViewMatrikulaBukaera.setText(ikastaroa.getMatrikulaBukaeraData());
			String testua = ikastaroa.getSarrera();
			String htmlText = "<html><head><style>a:link{color:#FFFFFF;}</style></head><body style=\"text-align:justify;background-color:#000000;color:#FFFFFF;\"> %s </body></html>";
			webViewSarrera.loadData(String.format(htmlText, testua), "text/html", "utf-8");
			webViewSarrera.setBackgroundColor(00000000);
			
			bisitatuBotoia.setOnClickListener(new OnClickListener() {
		            public void onClick(View v) {
		            	openWebBrowser(v, Konstanteak.IKASTARO_URL + ikastaroa.getId());
		             }
		        });
		}

	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}	
	
    private void openWebBrowser(View v, String url)
    {
        Intent webPageIntent = new Intent(Intent.ACTION_VIEW);
        webPageIntent.setData(Uri.parse(url));
        try { 
        	startActivity(webPageIntent);
        } catch (ActivityNotFoundException ex) { 
        	Toast.makeText(this, getResources().getText(R.string.nabigatzailearik_ez), Toast.LENGTH_LONG).show(); 
        }
    }

}
