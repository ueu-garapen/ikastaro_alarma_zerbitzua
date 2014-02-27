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

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Honiburuz extends Activity{
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.honiburuz);
		TextView bertsioa = (TextView) findViewById(R.id.bertsioa);
		bertsioa.setText("Bertsioa " + bertsioa.getText());
	}

}
