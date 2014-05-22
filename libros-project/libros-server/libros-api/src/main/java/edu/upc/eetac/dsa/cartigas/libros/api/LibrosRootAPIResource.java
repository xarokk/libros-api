package edu.upc.eetac.dsa.cartigas.libros.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
 

import edu.upc.eetac.dsa.cartigas.libros.api.model.LibrosRootAPI;
 //esta es raiz porque solo hay una barra
@Path("/")
public class LibrosRootAPIResource {
	@GET
	public LibrosRootAPI getRootAPI() {
		LibrosRootAPI api = new LibrosRootAPI();
		return api;
	}
}