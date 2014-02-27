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

import java.util.ArrayList;
import java.util.List;

import ueu.org.IkastaroAlarmaZerbitzua.R;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

public class ikastaroLekuaAdaptadorea extends ArrayAdapter<ikastaroLekua> {

	private final Activity context;
	private List<ikastaroLekua> ikastaroLekuakZerrenda = new ArrayList<ikastaroLekua>();

	public ikastaroLekuaAdaptadorea(Activity context, List<ikastaroLekua> list) {
		super(context, R.layout.listitem, list);
		this.context = context;
		this.ikastaroLekuakZerrenda = list;
	}

	/*
	 * Aukeratutako lekuen zerrenda bueltatzen du edo zerrenda hutsa bestela
	 */
	public ArrayList<ikastaroLekua> getSelected() {
		ArrayList<ikastaroLekua> hautatuak = new ArrayList<ikastaroLekua>();
		if (ikastaroLekuakZerrenda != null) {
			for (ikastaroLekua p : ikastaroLekuakZerrenda) {
				if (p.isSelected()) {
					hautatuak.add(p);
				}
			}
		}
		return hautatuak;
	}
	
	/*
	 * Tokien zerrenda eta zerrenda testu formatuan itzultzen du
	 */
	public ArrayList<Object> getSelectedEtaString() {
		ArrayList<Object> emaitza = new ArrayList<Object>();
		String testua = "";
		ArrayList<ikastaroLekua> hautatuak = new ArrayList<ikastaroLekua>();
		String lekua = "";
		for (ikastaroLekua p : ikastaroLekuakZerrenda) {
			if(p != null && p.getIzena().toLowerCase().equals("eae".toLowerCase()))
				lekua = p.getIzena().toUpperCase(); //EAE maiuskuletan
			else if(p != null)			
				lekua =p.getIzena().substring(0,1).toUpperCase() + p.getIzena().substring(1);
			if (p.isSelected()) {
				if (testua.length() == 0) {
					testua = lekua;
				} else {
					testua = testua
							+ ", "
							+ lekua;
				}
				hautatuak.add(p);
			}
		}
		emaitza.add(hautatuak);
		emaitza.add(testua);
		return emaitza;
	}	

	@Override
	public int getCount() {
		return ikastaroLekuakZerrenda.size();
	}

	@Override
	public ikastaroLekua getItem(int position) {
		return ikastaroLekuakZerrenda.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	static class ViewHolder {
		protected TextView text;
		protected CheckBox checkbox;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View view = null;
		if (convertView == null) {
			LayoutInflater inflator = context.getLayoutInflater();
			view = inflator.inflate(R.layout.listitem, null);
			final ViewHolder viewHolder = new ViewHolder();
			viewHolder.text = (TextView) view.findViewById(R.id.label);
			viewHolder.checkbox = (CheckBox) view.findViewById(R.id.check);
			viewHolder.checkbox
					.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							ikastaroLekua element = (ikastaroLekua) viewHolder.checkbox
									.getTag();
							element.setSelected(buttonView.isChecked());

						}
					});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(ikastaroLekuakZerrenda.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(ikastaroLekuakZerrenda
					.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		String testua = ikastaroLekuakZerrenda.get(position).getIzena();
		if(testua != null && testua.toLowerCase().equals("eae".toLowerCase()))
			holder.text.setText(testua.toUpperCase());
		else if(testua != null)			
			holder.text.setText(testua.substring(0,1).toUpperCase() + testua.substring(1));
		//holder.text.setText(ikastaroLekuakZerrenda.get(position).getIzena());				
		holder.checkbox.setChecked(ikastaroLekuakZerrenda.get(position)
				.isSelected());
		return view;
	}

}
