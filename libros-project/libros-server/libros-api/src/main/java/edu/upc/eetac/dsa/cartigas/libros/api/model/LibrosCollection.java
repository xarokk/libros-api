package edu.upc.eetac.dsa.cartigas.libros.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import edu.upc.eetac.dsa.cartigas.libros.api.model.Libros;

public class LibrosCollection {
	
	private List<Link> links;
	private List<Libros> libros;
	private long newestTimestamp;
	private long oldestTimestamp;
 
	public LibrosCollection() {
		super();
		libros = new ArrayList<Libros>();
	}
 
	public List<Libros> getStings() {
		return libros;
	}
 
	public void setStings(List<Libros> stings) {
		this.libros = stings;
	}
 
	public void addLibros(Libros libro) {
		libros.add(libro);
	}
 
	public List<Link> getLinks() {
		return links;
	}
 
	public void setLinks(List<Link> links) {
		this.links = links;
	}
 
	public long getNewestTimestamp() {
		return newestTimestamp;
	}
 
	public void setNewestTimestamp(long newestTimestamp) {
		this.newestTimestamp = newestTimestamp;
	}
 
	public long getOldestTimestamp() {
		return oldestTimestamp;
	}
 
	public void setOldestTimestamp(long oldestTimestamp) {
		this.oldestTimestamp = oldestTimestamp;
	}

}
