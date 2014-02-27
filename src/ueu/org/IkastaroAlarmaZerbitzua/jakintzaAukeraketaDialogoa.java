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
import java.util.Collections;

import ueu.org.ikastaroAlarmaZerbitzua.data.JakintzaZerrendaAdaptadorea;
import ueu.org.ikastaroAlarmaZerbitzua.data.jakintzaZerrendaModeloa;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class jakintzaAukeraketaDialogoa {
	private Dialog dialog;
	private Boolean ezetzi = true;

	public jakintzaAukeraketaDialogoa(Context context,
			ArrayList<jakintzaZerrendaModeloa> jakintzakarray) {
		// ///// Custom Dialogoa sortu Jakintza aukeraketarako
		dialog = new Dialog(context);

		dialog.setContentView(R.layout.jakintza_dialog);
		ListView lv = (ListView) dialog.findViewById(R.id.jakintza_zerrenda);
		EditText jiragazkia = (EditText) dialog
				.findViewById(R.id.jakintza_iragazkia);
		// Ordenatu String array
		Collections.sort(jakintzakarray);
		final ArrayAdapter<jakintzaZerrendaModeloa> adapter = new JakintzaZerrendaAdaptadorea(
				(Activity) context, jakintzakarray);
		adapter.notifyDataSetChanged();
		lv.setAdapter(adapter);

		// Testu iragazkia ezarri bilaketak egiteko
		lv.setTextFilterEnabled(true);
		jiragazkia.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				adapter.getFilter().filter(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});

		dialog.setCancelable(true);
		
		Button gordeButton = (Button) dialog.findViewById(R.id.dialogButtonOK);
		Button ezetziButton = (Button) dialog.findViewById(R.id.dialogJakintzaEzetzi);
		TextView garbituButton = (TextView) dialog
				.findViewById(R.id.dialogButtonGarbitu);

		gordeButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ezetzi = false;
				dialog.dismiss();
			}
		});
		
		ezetziButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ezetzi = true;
				dialog.dismiss();
			}
		});
		

		garbituButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ListView lv = (ListView) dialog
						.findViewById(R.id.jakintza_zerrenda);
				JakintzaZerrendaAdaptadorea adapter = (JakintzaZerrendaAdaptadorea) lv
						.getAdapter();
				adapter.unSelectAll();
			}
		});
	}

	public Dialog getDialogoa(){
		return this.dialog;
	}
	public Boolean getEzetzi(){
		return ezetzi;
	}
}