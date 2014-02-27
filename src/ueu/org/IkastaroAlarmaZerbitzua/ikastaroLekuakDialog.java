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
import java.util.ArrayList;
import java.util.List;

import ueu.org.IkastaroAlarmaZerbitzua.R;
import ueu.org.ikastaroAlarmaZerbitzua.data.ikastaroLekua;
import ueu.org.ikastaroAlarmaZerbitzua.data.ikastaroLekuaAdaptadorea;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import android.widget.Button;
import android.widget.ListView;


public class ikastaroLekuakDialog extends Dialog implements android.view.View.OnClickListener {

	private String izenburua;
	private Context context;
	private List<ikastaroLekua> ikastaroLekuakArray = new ArrayList<ikastaroLekua>();
	private ikastaroLekuaAdaptadorea adapter;
	private Boolean ezetzi = true;
		
	public ikastaroLekuakDialog(Context context, String izenburua, List<ikastaroLekua> lekuak) {
		super(context);
      this.context = context;
      this.izenburua = izenburua;
      this.ikastaroLekuakArray = lekuak;
	}
	

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ikastaro_lekuak_dialog);
        this.setTitle(izenburua);

        ListView lv = (ListView) findViewById(R.id.ikastaro_lekuak_zerrenda);
        Button ok = (Button) findViewById(R.id.ikastaro_lekuak_save);
        ok.setOnClickListener(this);
        Button cancel = (Button) findViewById(R.id.ikastaro_lekuak_cancel);
        cancel.setOnClickListener(this);

        adapter = new ikastaroLekuaAdaptadorea((Activity) context, ikastaroLekuakArray);
		adapter.notifyDataSetChanged();
		lv.setAdapter(adapter);
        //
  }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
	    	case R.id.ikastaro_lekuak_save:
	    		this.ezetzi = false;
	    		dismiss();
	    		break;
	    	case R.id.ikastaro_lekuak_cancel:
	    		this.ezetzi = true;
	    		dismiss();
	    		break;
		}
	}
	
	public ArrayList<ikastaroLekua> getSelectedLekuak(){ //hautatutako lekuen zerrenda edo zerrenda hutsa
		return ((ikastaroLekuaAdaptadorea) adapter).getSelected();
	}
	
	public ArrayList<Object> getSelectedMotakEtaString(){
		return ((ikastaroLekuaAdaptadorea) adapter).getSelectedEtaString();
	}	

	public Boolean getEzetzi(){
		return this.ezetzi;
	}

}