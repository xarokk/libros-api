package edu.upc.eetac.dsa.cartigas.libros.api.model;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.cartigas.libros.api.MediaType;
import edu.upc.eetac.dsa.cartigas.libros.api.ReviewsResource;
import edu.upc.eetac.dsa.cartigas.libros.api.UserResource;

public class ReviewsCollection {
	
	@InjectLinks({ @InjectLink(resource = ReviewsResource.class, style = Style.ABSOLUTE, rel = "Reviws", title = "Reviews", type = MediaType.REVIEWS_API_REVIEW), })
	
	private List<Link> links;
	private List<Reviews> reviews;
	
	 public ReviewsCollection() {
		// TODO Auto-generated constructor stub
		super();

		reviews = new ArrayList<Reviews>();
	} 
		
	
	


	public List<Link> getLinks() {
		return links;
	}


	public void setLinks(List<Link> links) {
		this.links = links;
	}


	public List<Reviews> getReviews() {
		return reviews;
	}


	public void setReviews(List<Reviews> reviews) {
		this.reviews = reviews;
	}
	
	public void addReview(Reviews review) {
		reviews.add(review);
	}
}
