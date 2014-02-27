package ueu.org.IkastaroAlarmaZerbitzua;


import ueu.org.ikastaroAlarmaZerbitzua.data.EskaeraKonfigurazioa;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;


public class MaiztasunaDialog {
	
	private Boolean ezetzi = true;
	private int izenburua;
	private Context context;
	private int maiztasuna = Konstanteak.MAIZTASUNA_DEFAULT;
	private Dialog dialogoa = null;
	private int aukerak = R.array.maiztasuna_arrays;
	
	public MaiztasunaDialog(Context context, int izenburua, int aukerak, int maiztasuna) {
      this.context = context;
      this.izenburua = izenburua;
      this.aukerak = aukerak;
      this.maiztasuna = maiztasuna;
      this.sortu();
      
	}
	
    private void sortu() {
		final AlertDialog.Builder maiztasunDialogBuilder = new AlertDialog.Builder(
				context);
		maiztasunDialogBuilder.setTitle(this.izenburua);
		int hautatua = EskaeraKonfigurazioa
				.maiztasunaToPos(maiztasuna);
		// Sortu dialogoa
		maiztasunDialogBuilder.setSingleChoiceItems(
				aukerak, hautatua,
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int item) {
						// onclick
						String testuaHautatua = (String) ((AlertDialog) dialog)
								.getListView().getItemAtPosition(item);
						maiztasuna = EskaeraKonfigurazioa
								.maiztasunTextToMaiztasuna(testuaHautatua);
						ezetzi = false;
						dialog.dismiss();
					}
				});

		dialogoa = maiztasunDialogBuilder
				.create();
	}
    
    public Dialog getDialog(){
    	return dialogoa;
    }
    public Boolean getEzetzi(){
    	return ezetzi;	
    }
    public int getMaiztasuna(){
    	return maiztasuna;
    }
}
