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

import ueu.org.ikastaroAlarmaZerbitzua.data.IkastaroArrayAdapter;
import ueu.org.ikastaroAlarmaZerbitzua.data.Ikastaroa;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class IkastaroAlarmaZerbitzuaEmaitza extends ListActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		ArrayList<Ikastaroa> zerrenda = new ArrayList<Ikastaroa>();

		// Ez dut lortu ArrayList Parcelable moduan pasatzea. hutsik heltzen da
		// eta ez dago modurik. Horregatik azkean ikastaroak banan banan pasatu
		// dira eta gero berreraikitzen da ArrayLista
		int luzera = getIntent().getIntExtra(Konstanteak.ZERBITZU_PARAMETROAK_LUZERA, 0);
		for (int i = 0; i < luzera; i++) {
			Ikastaroa ikastaroa = (Ikastaroa) getIntent().getParcelableExtra(
					Konstanteak.ZERBITZU_PARAMETROAK_IKASTAROAK + i);
			zerrenda.add(ikastaroa);
		}

		IkastaroArrayAdapter adapter = new IkastaroArrayAdapter(this, zerrenda);
		setListAdapter(adapter);


	}

	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Ikastaroa ikastaro = (Ikastaroa) getListAdapter().getItem(position);
		Intent intent = new Intent(this, ikastaroaIkusi.class);
		intent.putExtra(Konstanteak.ZERBITZU_PARAMETROAK_IKASTAROA, ikastaro);
		startActivity(intent); 
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

}
