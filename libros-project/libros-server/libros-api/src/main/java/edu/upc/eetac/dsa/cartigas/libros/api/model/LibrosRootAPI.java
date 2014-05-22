package edu.upc.eetac.dsa.cartigas.libros.api.model;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLink.Style;
import org.glassfish.jersey.linking.InjectLinks;

import edu.upc.eetac.dsa.cartigas.libros.api.LibrosResource;
import edu.upc.eetac.dsa.cartigas.libros.api.LibrosRootAPIResource;
import edu.upc.eetac.dsa.cartigas.libros.api.MediaType;
 
public class LibrosRootAPI {
	@InjectLinks({
		@InjectLink(resource = LibrosRootAPIResource.class, style = Style.ABSOLUTE, rel = "self bookmark home", title = "Libros Root API", method = "getRootAPI"),@InjectLink(resource = LibrosResource.class, style = Style.ABSOLUTE, rel = "libros", title = "Latest stings", type = MediaType.LIBROS_API_LIBRO_COLLECTION)})
	
	//@InjectLink(resource = StingResource.class, style = Style.ABSOLUTE, rel = "create-stings", title = "Latest stings", type = MediaType.BEETER_API_STING) }
	private List<Link> links;
 
	public List<Link> getLinks() {
		return links;
	}
 
	public void setLinks(List<Link> links) {
		this.links = links;
	}
}