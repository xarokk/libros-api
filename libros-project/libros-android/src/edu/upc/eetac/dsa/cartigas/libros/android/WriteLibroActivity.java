package edu.upc.eetac.dsa.cartigas.libros.android;


import android.annotation.TargetApi;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import edu.upc.eetac.dsa.cartigas.libros.android.api.Libros;
import edu.upc.eetac.dsa.cartigas.libros.android.api.LibrosAPI;
import edu.upc.eetac.dsa.cartigas.libros.android.api.LibrosAndroidException;

 
@TargetApi(Build.VERSION_CODES.CUPCAKE)
public class WriteLibroActivity extends Activity {
	private final static String TAG = WriteLibroActivity.class.getName();
 
	private class PostStingTask extends AsyncTask<String, Void, Libros> {
		private ProgressDialog pd;
 
		@Override
		protected Libros doInBackground(String... params) {
			Libros sting = null;
			try {
				sting = LibrosAPI.getInstance(WriteLibroActivity.this).createLibro(params[0], params[1], params[2],params[3]);
			} catch (LibrosAndroidException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return sting;
		}
 
		@Override
		protected void onPostExecute(Libros result) {
			showStings();
			if (pd != null) {
				pd.dismiss();
			}
		}
 
		@Override
		protected void onPreExecute() {
			pd = new ProgressDialog(WriteLibroActivity.this);
 
			pd.setCancelable(false);
			pd.setIndeterminate(true);
			pd.show();
		}
	}
 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.write_sting_layout);
 
	}
 
	public void cancel(View v) {
		finish();
	}
 
	public void postSting(View v) {
		EditText etSubject = (EditText) findViewById(R.id.etSubject);
		EditText etContent = (EditText) findViewById(R.id.etContent);
 
		String subject = etSubject.getText().toString();
		String content = etContent.getText().toString();
 
		(new PostStingTask()).execute(subject, content);
	}
 
	private void showStings() {
		Intent intent = new Intent(this, LibrosMainActivity.class);
		startActivity(intent);
	}
 
}