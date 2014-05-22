package edu.upc.eetac.dsa.cartigas.libros.android.api;

import java.util.HashMap;
import java.util.List;
import java.util.Map;





public class Libros {
	private Map<String, Link> links = new HashMap<String, Link>();
	private String libroid;
	private String titulo;
	private String autor;
	private String lengua;
	private String edicion;
	private String editorial;
	private String subject;
	private String content;
	private long last_modified;
	public Map<String, Link> getLinks() {
		return links;
	}
	public void setLinks(Map<String, Link> links) {
		this.links = links;
	}
	public String getLibroid() {
		return libroid;
	}
	public void setLibroid(String libroid) {
		this.libroid = libroid;
	}
	public String getTitulo() {
		return titulo;
	}
	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}
	public String getAutor() {
		return autor;
	}
	public void setAutor(String autor) {
		this.autor = autor;
	}
	public String getLengua() {
		return lengua;
	}
	public void setLengua(String lengua) {
		this.lengua = lengua;
	}
	public String getEdicion() {
		return edicion;
	}
	public void setEdicion(String edicion) {
		this.edicion = edicion;
	}
	public String getEditorial() {
		return editorial;
	}
	public void setEditorial(String editorial) {
		this.editorial = editorial;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public long getLast_modified() {
		return last_modified;
	}
	public void setLast_modified(long last_modified) {
		this.last_modified = last_modified;
	}
	
	
}
