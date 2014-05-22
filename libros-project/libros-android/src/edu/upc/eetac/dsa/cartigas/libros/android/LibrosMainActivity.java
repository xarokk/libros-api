package edu.upc.eetac.dsa.cartigas.libros.android;

import java.net.Authenticator;
import java.net.PasswordAuthentication;
import java.util.ArrayList;
import android.annotation.TargetApi;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import edu.upc.eetac.dsa.cartigas.libros.android.api.Libros;
import edu.upc.eetac.dsa.cartigas.libros.android.api.LibrosAPI;
import edu.upc.eetac.dsa.cartigas.libros.android.api.LibrosAdapter;
import edu.upc.eetac.dsa.cartigas.libros.android.api.LibrosAndroidException;
import edu.upc.eetac.dsa.cartigas.libros.android.api.LibrosCollection;



@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class LibrosMainActivity extends ListActivity {

	private class FetchStingsTask extends
			AsyncTask<Void, Void, LibrosCollection> {
		private ProgressDialog pd;

		@Override
		protected LibrosCollection doInBackground(Void... params) {
			LibrosCollection stings = null;
			try {
				stings = LibrosAPI.getInstance(LibrosMainActivity.this)
						.getLibros();
			} catch (LibrosAndroidException e) {
				e.printStackTrace();
			}
			return stings;
		}

		@Override
		protected void onPostExecute(LibrosCollection result) {
			addStings(result);
			if (pd != null) {
				pd.dismiss();
			}
		}

		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(LibrosMainActivity.this);
			pd.setTitle("Searching...");
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}

	}
	private void addStings(LibrosCollection libraco){
		librosList.addAll(libraco.getLibro());
		adapter.notifyDataSetChanged();
	}
	private ArrayList<Libros> librosList;
	private LibrosAdapter adapter;

	private final static String TAG = LibrosMainActivity.class.toString();

	/*
	 * private static final String[] items = { "lorem", "ipsum", "dolor", "sit",
	 * "amet", "consectetuer", "adipiscing", "elit", "morbi", "vel", "ligula",
	 * "vitae", "arcu", "aliquet", "mollis", "etiam", "vel", "erat", "placerat",
	 * "ante", "porttitor", "sodales", "pellentesque", "augue", "purus" };
	 * private ArrayAdapter<String> adapter;
	 * 
	 * /** Called when the activity is first created.
	 */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.libros_layout);
		SharedPreferences prefs = getSharedPreferences("beeter-profile",
				Context.MODE_PRIVATE);
		final String username = prefs.getString("username", null);
		final String password = prefs.getString("password", null);
	 
		Authenticator.setDefault(new Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password
						.toCharArray());
			}
		});
		Log.d(TAG, "authenticated with " + username + ":" + password);
	 
		librosList = new ArrayList<Libros>();
		adapter = new LibrosAdapter(this, librosList);
		setListAdapter(adapter);
		(new FetchStingsTask()).execute();
	}
	

	@Override
	// detecta click en la pantalla
	protected void onListItemClick(ListView l, View v, int position, long id) {
		Libros sting = librosList.get(position);
		Log.d(TAG, sting.getLinks().get("self").getTarget());

		Intent intent = new Intent(this, LibroDetailActivity.class);
		intent.putExtra("url", sting.getLinks().get("self").getTarget());
		startActivity(intent);
	}
/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.libros_actions, menu);
		return true;
	}
	/*
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.miWrite:
			Intent intent = new Intent(this, WriteLibroActivity.class);
			startActivity(intent);
			return true;

		default:
			return super.onOptionsItemSelected(item);
		}

	} */

}
