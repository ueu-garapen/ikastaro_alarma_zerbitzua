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

public class ikastaroMotaAdaptadorea extends ArrayAdapter<IkastaroMota> {

	private final Activity context;
	private List<IkastaroMota> ikastaroMotaZerrenda = new ArrayList<IkastaroMota>();

	public ikastaroMotaAdaptadorea(Activity context, List<IkastaroMota> list) {
		super(context, R.layout.listitem, list);
		this.context = context;
		this.ikastaroMotaZerrenda = list;
	}

	public ArrayList<IkastaroMota> getSelected() {
		ArrayList<IkastaroMota> hautatuak = new ArrayList<IkastaroMota>();
		for (IkastaroMota p : ikastaroMotaZerrenda) {
			if (p.isSelected()) {
				hautatuak.add(p);
			}
		}
		return hautatuak;
	}
	
	public ArrayList<Object> getSelectedEtaString() {
		ArrayList<Object> emaitza = new ArrayList<Object>();
		String testua = "";
		ArrayList<IkastaroMota> hautatuak = new ArrayList<IkastaroMota>();
		for (IkastaroMota p : ikastaroMotaZerrenda) {
			if (p.isSelected()) {
				if (testua.length() == 0) {
					testua = p.getIzena();
				} else {
					testua = testua
							+ ", "
							+ p.getIzena();
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
		return ikastaroMotaZerrenda.size();
	}

	@Override
	public IkastaroMota getItem(int position) {
		return ikastaroMotaZerrenda.get(position);
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
							IkastaroMota element = (IkastaroMota) viewHolder.checkbox
									.getTag();
							element.setSelected(buttonView.isChecked());

						}
					});
			view.setTag(viewHolder);
			viewHolder.checkbox.setTag(ikastaroMotaZerrenda.get(position));
		} else {
			view = convertView;
			((ViewHolder) view.getTag()).checkbox.setTag(ikastaroMotaZerrenda
					.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
		holder.text.setText(ikastaroMotaZerrenda.get(position).getIzena());
		holder.checkbox.setChecked(ikastaroMotaZerrenda.get(position)
				.isSelected());
		return view;
	}

}
