package edu.upc.eetac.dsa.cartigas.libros.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.cartigas.libros.api.LibrosResource;
import edu.upc.eetac.dsa.cartigas.libros.api.MediaType;
import edu.upc.eetac.dsa.cartigas.libros.api.ReviewsResource;

public class Libros {
	@InjectLinks({
		@InjectLink(resource = LibrosResource.class, style = Style.ABSOLUTE, rel = "self edit", title = "Libros", type = MediaType.LIBROS_API_LIBRO, method = "getLibro", bindings = @Binding(name = "libroid", value = "${instance.libroid}")),
		
		 
		})   
	private List<Link> links;
	private String libroid;
	private String titulo;
	private String autor;
	private String lengua;
	private String edicion;
	private String editorial;
	private String subject;
	private String content;
	private long last_modified;
	
	
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
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
	public long getLast_modified() {
		return last_modified;
	}
	public void setLast_modified(long last_modified) {
		this.last_modified = last_modified;
	}
	

}
