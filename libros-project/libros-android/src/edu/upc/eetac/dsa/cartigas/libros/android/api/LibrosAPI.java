package edu.upc.eetac.dsa.cartigas.libros.android.api;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
 


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
 


import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;
 
public class LibrosAPI {
	private final static String TAG = LibrosAPI.class.getName();
	private static LibrosAPI instance = null;
	private URL url;
 
	private LibrosRootAPI rootAPI = null;
 
	private LibrosAPI(Context context) throws IOException,
			LibrosAndroidException {
		super();
 
		AssetManager assetManager = context.getAssets();
		Properties config = new Properties();
		config.load(assetManager.open("config.properties"));//carga fichero configuracion 
		String serverAddress = config.getProperty("server.address");//obtiene los valores de es fichero
		String serverPort = config.getProperty("server.port");
		url = new URL("http://" + serverAddress + ":" + serverPort
				+ "/libros-api"); //se qeda cn la base url esta si utilizamos hateoas nunca cambia
 
		Log.d("LINKS", url.toString());
		getRootAPI();
	}
 
	public final static LibrosAPI getInstance(Context context)
			throws LibrosAndroidException {
		if (instance == null)
			try {
				instance = new LibrosAPI(context);//context es la actividad, para recuperar valores del fichero conf.
			} catch (IOException e) {
				throw new LibrosAndroidException(
						"Can't load configuration file");
			}
		return instance;
	}
 
	private void getRootAPI() throws LibrosAndroidException { //rea un modelo y ataka al servicio
		Log.d(TAG, "getRootAPI()");
		rootAPI = new LibrosRootAPI();
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);// true por defecto, significa que qiero leer
			urlConnection.connect();
		} catch (IOException e) {
			throw new LibrosAndroidException(
					"Can't connect to Beeter API Web Service");
		}
 
		BufferedReader reader;
		try {//lee json que le devuelve htps://localhost:8080/beeterapi
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());// aparti de un string y objeto json lo convierte
			JSONArray jsonLinks = jsonObject.getJSONArray("links");//asi poder manipular y obtener get, arrays.. 
			parseLinks(jsonLinks, rootAPI.getLinks());//lo proceso con el metodo priado de esta clase y lo guardas en el modelo rootAPI
		} catch (IOException e) {
			throw new LibrosAndroidException(
					"Can't get response from Beeter API Web Service");
		} catch (JSONException e) {
			throw new LibrosAndroidException("Error parsing Beeter Root API");
		}
 
	}
 
	public LibrosCollection getLibros() throws LibrosAndroidException {
		Log.d(TAG, "getStings()");
		LibrosCollection Libro = new LibrosCollection();
 
		HttpURLConnection urlConnection = null;
		try {
			urlConnection = (HttpURLConnection) new URL(rootAPI.getLinks()
					.get("libros").getTarget()).openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
		} catch (IOException e) {
			throw new LibrosAndroidException(
					"Can't connect to Beeter API Web Service");
		}
 
		BufferedReader reader;
		try {
			reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
 
			JSONObject jsonObject = new JSONObject(sb.toString());
			JSONArray jsonLinks = jsonObject.getJSONArray("links");//atributoss
			parseLinks(jsonLinks, Libro.getLinks());
 
			Libro.setNewestTimestamp(jsonObject.getLong("newestTimestamp"));//atributo
			Libro.setOldestTimestamp(jsonObject.getLong("oldestTimestamp"));//atributo qe recuperamos
			JSONArray jsonStings = jsonObject.getJSONArray("libros");
			for (int i = 0; i < jsonStings.length(); i++) {
				Libros l = new Libros();//creo un sting
				JSONObject jsonSting = jsonStings.getJSONObject(i);// le doy valor a traves del array y lo a�ado a la coleccion qe es lo qe lo devuelves
				l.setAutor(jsonSting.getString("autor"));
				l.setLengua(jsonSting.getString("lengua"));//se van a�adiendo
				l.setLast_modified(jsonSting.getLong("fecha"));
				l.setEdicion(jsonSting.getString("edicion"));
				l.setEditorial(jsonSting.getString("editorial"));
				jsonLinks = jsonSting.getJSONArray("links");
				parseLinks(jsonLinks, l.getLinks());
				Libro.getLibro().add(l);
			}
		} catch (IOException e) {
			throw new LibrosAndroidException(
					"Can't get response from Beeter API Web Service");
		} catch (JSONException e) {
			throw new LibrosAndroidException("Error parsing Beeter Root API");
		}
 
		return Libro;
	}
 
	private void parseLinks(JSONArray jsonLinks, Map<String, Link> map)
			throws LibrosAndroidException, JSONException {
		for (int i = 0; i < jsonLinks.length(); i++) {
			Link link = SimpleLinkHeaderParser
					.parseLink(jsonLinks.getString(i));
			//REL PODIA ser multiple rel=" home boomark self" -> 3 enlaces qe obtienes a traves del mapa
			String rel = link.getParameters().get("rel");//tb podria obteet el title i el target(?) pRA QITARME LOS ESPACIOS BLANCOS DE ENCIAM
			String rels[] = rel.split("\\s");
			for (String s : rels)
				map.put(s, link);
		}
	}
	
	public Libros getSting(String urllibro) throws LibrosAndroidException {
		Libros l = new Libros();
	 
		HttpURLConnection urlConnection = null;
		try {
			URL url = new URL(urllibro);
			urlConnection = (HttpURLConnection) url.openConnection();
			urlConnection.setRequestMethod("GET");
			urlConnection.setDoInput(true);
			urlConnection.connect();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			JSONObject jsonSting = new JSONObject(sb.toString()); //revuperado json, i procesado json
			l.setAutor(jsonSting.getString("autor"));
			
			l.setLengua(jsonSting.getString("lengua"));//se van a�adiendo
			l.setLast_modified(jsonSting.getLong("fecha"));
			l.setEdicion(jsonSting.getString("edicion"));
			l.setEditorial(jsonSting.getString("editorial"));
			JSONArray jsonLinks = jsonSting.getJSONArray("links");
			parseLinks(jsonLinks, l.getLinks());
		} catch (MalformedURLException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibrosAndroidException("Bad sting url");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibrosAndroidException("Exception when getting the sting");
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibrosAndroidException("Exception parsing response");
		}
	 
		return l;
	}
	public Libros createLibro(String autor, String titulo, String edicion , String editorial) throws LibrosAndroidException {
		Libros l = new Libros();
		l.setAutor(autor);
		l.setEdicion(edicion);
		l.setEditorial(editorial);
		l.getLengua();
		
		HttpURLConnection urlConnection = null;
		try {
			JSONObject jsonSting = createJsonLibro(l);
			URL urlPostStings = new URL(rootAPI.getLinks().get("create-stings")
					.getTarget());
			urlConnection = (HttpURLConnection) urlPostStings.openConnection();
			urlConnection.setRequestProperty("Accept",
					MediaType.LIBROS_API_LIBRO);
			urlConnection.setRequestProperty("Content-Type",
					MediaType.LIBROS_API_LIBRO);
			urlConnection.setRequestMethod("POST");
			urlConnection.setDoInput(true);
			urlConnection.setDoOutput(true);
			urlConnection.connect();
			PrintWriter writer = new PrintWriter(
					urlConnection.getOutputStream());
			writer.println(jsonSting.toString());
			writer.close();
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					urlConnection.getInputStream()));
			StringBuilder sb = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line);
			}
			jsonSting = new JSONObject(sb.toString());
	 
l.setAutor(jsonSting.getString("autor"));
			
			l.setLengua(jsonSting.getString("lengua"));//se van a�adiendo
			l.setLast_modified(jsonSting.getLong("fecha"));
			l.setEdicion(jsonSting.getString("edicion"));
			l.setEditorial(jsonSting.getString("editorial"));
			JSONArray jsonLinks = jsonSting.getJSONArray("links");
			parseLinks(jsonLinks, l.getLinks());
		} catch (JSONException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibrosAndroidException("Error parsing response");
		} catch (IOException e) {
			Log.e(TAG, e.getMessage(), e);
			throw new LibrosAndroidException("Error getting response");
		} finally {
			if (urlConnection != null)
				urlConnection.disconnect();
		}
		return l;
	}
	//writesting activity progrso void i sting tipo de retorno stings..params (aqi esta tanto el subject como el content) 
	//onpostexecute recarga lista con todos los stings inclusive el nuevo k hemos creado
	//oncreate carga layout
	//dosmetodos poststing y cancel
	//finish acaba actividad y vuelve a la anterior en este caso a la lista de stings , mostrat actividad tal i como estaba
	//en el showstings parecido al finish pero con la lista actualizada
	//post obtenemos  do input leemos, doutpu envamios, createstingjson, atraves de el metodo pivado, se crea json object i se van colocando valores 
	private JSONObject createJsonLibro(Libros l) throws JSONException {
		JSONObject jsonSting = new JSONObject();
		jsonSting.put("subject", l.getSubject());
		jsonSting.put("content", l.getContent());
	 
		return jsonSting;
	}
}