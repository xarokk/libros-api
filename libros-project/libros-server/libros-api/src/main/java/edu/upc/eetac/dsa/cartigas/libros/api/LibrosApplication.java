package edu.upc.eetac.dsa.cartigas.libros.api;

import org.glassfish.jersey.linking.DeclarativeLinkingFeature;
import org.glassfish.jersey.server.ResourceConfig;
 
public class LibrosApplication extends ResourceConfig {
	public LibrosApplication() {
		super();
		register(DeclarativeLinkingFeature.class);//registra el uso del hiper link
	}
}