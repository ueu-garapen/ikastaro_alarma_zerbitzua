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
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;

/*
 * Datuen iragazketa inplementatuta
 */
public class JakintzaZerrendaAdaptadorea extends
		ArrayAdapter<jakintzaZerrendaModeloa> implements Filterable {

	private List<jakintzaZerrendaModeloa> jakintzaZerrenda;
	private final Activity context;
	private JakintzaFilter jakintzaFilter;
	private List<jakintzaZerrendaModeloa> origJakintzaZerrenda = new ArrayList<jakintzaZerrendaModeloa>();

	public JakintzaZerrendaAdaptadorea(Activity context,
			List<jakintzaZerrendaModeloa> list) {
		super(context, R.layout.listitem, list);
		this.context = context;
		this.jakintzaZerrenda = list;
		this.origJakintzaZerrenda = list;
	}
	
	public ArrayList<jakintzaZerrendaModeloa> getSelected(){
		ArrayList<jakintzaZerrendaModeloa> hautatuak = new ArrayList<jakintzaZerrendaModeloa>();
		for (jakintzaZerrendaModeloa p : origJakintzaZerrenda){
			if(p.isSelected()){
				hautatuak.add(p);
			}
		}
		return hautatuak;
	}
	
	/*
	 * Jakintzen zerrenda eta zerrenda testu formatuan itzultzen du
	 */
	public ArrayList<Object> getSelectedEtaString() {
		ArrayList<Object> emaitza = new ArrayList<Object>();
		String testua = "";
		ArrayList<jakintzaZerrendaModeloa> hautatuak = new ArrayList<jakintzaZerrendaModeloa>();
		for (jakintzaZerrendaModeloa p : origJakintzaZerrenda) {
			if (p.isSelected()) {
				if (testua.length() == 0) {
					testua = p.getName();
				} else {
					testua = testua
							+ ", "
							+ p.getName();
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
        return jakintzaZerrenda.size();
    }

    @Override
    public jakintzaZerrendaModeloa getItem(int position) {
        return jakintzaZerrenda.get(position);
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
							jakintzaZerrendaModeloa element = (jakintzaZerrendaModeloa) viewHolder.checkbox
									.getTag();
							element.setSelected(buttonView.isChecked());

						}
					});
			view.setTag(viewHolder);
				viewHolder.checkbox.setTag(jakintzaZerrenda.get(position));

		} else {
			view = convertView;

				((ViewHolder) view.getTag()).checkbox.setTag(jakintzaZerrenda
					.get(position));
		}
		ViewHolder holder = (ViewHolder) view.getTag();
			holder.text.setText(jakintzaZerrenda.get(position).getName());
			holder.checkbox.setChecked(jakintzaZerrenda.get(position).isSelected());
		return view;
	}

	public void resetData() {
		jakintzaZerrenda = origJakintzaZerrenda;
	}

	@Override
	public Filter getFilter() {
		if (jakintzaFilter == null)
			jakintzaFilter = new JakintzaFilter();

		return jakintzaFilter;
	}

	private class JakintzaFilter extends Filter {
		@Override
		protected FilterResults performFiltering(CharSequence constraint) {
			if (origJakintzaZerrenda == null) {
				origJakintzaZerrenda = jakintzaZerrenda; // Jatorrizko zerrenda gorde
            }
			FilterResults results = new FilterResults();
			// Iragazkiaren logika
			if (constraint == null || constraint.length() == 0) {
				// Iragazkia hutsik
				results.values = origJakintzaZerrenda;
				results.count = origJakintzaZerrenda.size();
			} else {
				// Iragazketa
				List<jakintzaZerrendaModeloa> nJakintzaList = new ArrayList<jakintzaZerrendaModeloa>();

				for (jakintzaZerrendaModeloa p : origJakintzaZerrenda) {
					if (p.getName().toUpperCase()
							.startsWith(constraint.toString().toUpperCase())){
						nJakintzaList.add(p);
					}
				}

				results.values = nJakintzaList;
				results.count = nJakintzaList.size();
			}
			return results;
		}

		@SuppressWarnings("unchecked")
		@Override
		protected void publishResults(CharSequence constraint,
				FilterResults results) {
			// adaptadoreari iragarri edukiaren aldaketaz
			if (results.count == 0)
				notifyDataSetInvalidated();
			else {
				jakintzaZerrenda = (List<jakintzaZerrendaModeloa>) results.values;
				notifyDataSetChanged();
			}
		}

	}

	public void unSelectAll() {
		for (jakintzaZerrendaModeloa p : origJakintzaZerrenda){
			if(p.isSelected()){
				p.setSelected(false);				
			}
		}
		notifyDataSetChanged();
	}
}