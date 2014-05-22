package edu.upc.eetac.dsa.cartigas.libros.api.model;

import java.util.List;

import javax.ws.rs.core.Link;

import org.glassfish.jersey.linking.Binding;
import org.glassfish.jersey.linking.InjectLink;
import org.glassfish.jersey.linking.InjectLinks;
import org.glassfish.jersey.linking.InjectLink.Style;

import edu.upc.eetac.dsa.cartigas.libros.api.MediaType;
import edu.upc.eetac.dsa.cartigas.libros.api.ReviewsResource;
import edu.upc.eetac.dsa.cartigas.libros.api.UserResource;

public class Reviews {

	@InjectLinks({
		@InjectLink(resource = ReviewsResource.class, style = Style.ABSOLUTE, rel = "self edit", title = "Reviews", type = MediaType.REVIEWS_API_REVIEW, method = "getReview2", bindings = @Binding(name = "reviewid", value = "${instance.reviewid}")),
		
		 
		})   
	private List<Reviews> reviews;
	private List<Link> links;
	private String reviewid;
	private String libroid;
	private String username;
	private String summary;
	
	public List<Link> getLinks() {
		return links;
	}
	public void setLinks(List<Link> links) {
		this.links = links;
	}
	public String getReviewid() {
		return reviewid;
	}
	public void setReviewid(String reviewid) {
		this.reviewid = reviewid;
	}
	public String getLibroid() {
		return libroid;
	}
	public void setLibroid(String libroid) {
		this.libroid = libroid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getSummary() {
		return summary;
	}
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	
}
