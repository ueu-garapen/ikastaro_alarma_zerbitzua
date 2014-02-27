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

import android.app.Activity;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class IkastaroArrayAdapter	extends ArrayAdapter<Ikastaroa> {
		  private final Activity context;
		  private final ArrayList<Ikastaroa> ikastaroak;

		  static class ViewHolder {
		    public TextView text;
		  }

		  public IkastaroArrayAdapter(Activity context, ArrayList<Ikastaroa> ikastaroak) {
		    super(context, 0);
		    this.context = context;
		    this.ikastaroak = ikastaroak;
		  }
		  
		   @Override
		    public int getCount() {
		        return ikastaroak.size();
		    }

		    @Override
		    public Ikastaroa getItem(int position) {
		        return ikastaroak.get(position);
		    }

		    @Override
		    public long getItemId(int position) {
		        return position;
		    }		  

		  //Bista sortzeko metodo optimizatua
		  @Override
		  public View getView(int position, View convertView, ViewGroup parent) {
		    View rowView = convertView;
		    if (rowView == null) {
		    	LayoutInflater inflater = (LayoutInflater)
		    	        context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
		    	//Defektuzko item bakarreko Layout-a erabili
		    	rowView = inflater.inflate(android.R.layout.simple_list_item_1, null);
		    	ViewHolder viewHolder = new ViewHolder();
		    	viewHolder.text = (TextView) rowView.findViewById(android.R.id.text1);
		    	viewHolder.text.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		    	rowView.setTag(viewHolder);
		    }

		    ViewHolder holder = (ViewHolder) rowView.getTag();
		    Ikastaroa s = ikastaroak.get(position);
		    holder.text.setText(s.getIzenburua());

		    return rowView;
		  }
}
