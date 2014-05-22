package edu.upc.eetac.dsa.cartigas.libros.android;

import java.text.SimpleDateFormat;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;
import edu.upc.eetac.dsa.cartigas.libros.android.api.Libros;
import edu.upc.eetac.dsa.cartigas.libros.android.api.LibrosAPI;
import edu.upc.eetac.dsa.cartigas.libros.android.api.LibrosAndroidException;

 
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class LibroDetailActivity extends Activity {
	private final static String TAG = LibroDetailActivity.class.getName();
 
	@Override//getextras qe se los pasa el mainactivity
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.libro_detail_layout);
		String urlSting = (String) getIntent().getExtras().get("url");
		(new FetchStingTask()).execute(urlSting);
	}
	//metodo privado a partir del sting recu�rado del servicio damos valores a las etiketas qe hemos dado
	private void loadSting(Libros libro) {
		TextView tvDetailSubject = (TextView) findViewById(R.id.tvDetailSubject);
		TextView tvDetailContent = (TextView) findViewById(R.id.tvDetailContent);
		TextView tvDetailUsername = (TextView) findViewById(R.id.tvDetailUsername);
		TextView tvDetailDate = (TextView) findViewById(R.id.tvDetailDate);
	 
		tvDetailSubject.setText(libro.getTitulo());
		tvDetailContent.setText(libro.getAutor());
		tvDetailUsername.setText(libro.getEditorial());
		tvDetailDate.setText(SimpleDateFormat.getInstance().format(
				libro.getLast_modified()));
	}
	//Clase anidada fetchstingtasktarea en background va a beeterapi, obtiene instancia i llama get sting pasando la url que hemos recperado del recurso
	//decalramos esas strin task , tipo de parametros del background 
	
	
	private class FetchStingTask extends AsyncTask<String, Void, Libros> {
		private ProgressDialog pd;
	 
		@Override
		protected Libros doInBackground(String... params) {
			Libros libro = null;
			try {
				libro = LibrosAPI.getInstance(LibroDetailActivity.this)
						.getSting(params[0]);
			} catch (LibrosAndroidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return libro;
		}
	 
		@Override
		protected void onPostExecute(Libros result) {
			loadSting(result);
			if (pd != null) {
				pd.dismiss();
			}
		}
	 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(LibroDetailActivity.this);
			pd.setTitle("Loading...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	 
	}
}
