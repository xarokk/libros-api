package edu.upc.eetac.dsa.cartigas.libros.android.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;




public class LibrosCollection {
	
	private Map<String, Link> links = new HashMap<String, Link>();
	private List<Libros> libro = new ArrayList<Libros>();;
	private long newestTimestamp;
	private long oldestTimestamp;
	
	public void addlibro(Libros libror) {
		libro.add(libror);
	}
	
	
	public Map<String, Link> getLinks() {
		return links;
	}
	public void setLinks(Map<String, Link> links) {
		this.links = links;
	}
	public List<Libros> getLibro() {
		return libro;
	}
	public void setLibro(List<Libros> libro) {
		this.libro = libro;
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
