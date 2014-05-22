package edu.upc.eetac.dsa.cartigas.libros.android.api;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

import edu.upc.eetac.dsa.cartigas.libros.android.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class LibrosAdapter extends BaseAdapter {
	private static class ViewHolder {
		TextView tvSubject;
		TextView tvUsername;
		TextView tvDate;
	}
	private ArrayList<Libros> data;
	private LayoutInflater inflater;
	
//numero total de datos en el get count
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data.size();
	}
//devuelve el modelo de una determinada posucuion
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}
//devuelve un valor unico par a una determinada posucuin , los identificamos unicamente
	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return Long.parseLong(((Libros)getItem(position)).getLibroid());
	}
//esto devuelve el layout osea la vista
	
	//si existe creo el view holder si no lo creo
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder viewHolder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.list_row_libro, null);
			viewHolder = new ViewHolder();
			viewHolder.tvSubject = (TextView) convertView
					.findViewById(R.id.tvSubject);
			viewHolder.tvUsername = (TextView) convertView
					.findViewById(R.id.tvUsername);
			viewHolder.tvDate = (TextView) convertView
					.findViewById(R.id.tvDate);
			convertView.setTag(viewHolder);
		} else {
			viewHolder = (ViewHolder) convertView.getTag();
		}
		//aqui se recuperan los datos por posiciones
		String subject = data.get(position).getTitulo();
		String username = data.get(position).getAutor();
		String date = SimpleDateFormat.getInstance().format(
				data.get(position).getLast_modified());
		viewHolder.tvSubject.setText(subject);
		viewHolder.tvUsername.setText(username);
		viewHolder.tvDate.setText(date);
		return convertView;
	}
	public LibrosAdapter(Context context, ArrayList<Libros> data) {
		super();
		inflater = LayoutInflater.from(context);
		this.data = data;
	}
}
