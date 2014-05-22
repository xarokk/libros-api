package edu.upc.eetac.dsa.cartigas.libros.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;

import edu.upc.eetac.dsa.cartigas.libros.api.model.Libros;
import edu.upc.eetac.dsa.cartigas.libros.api.model.Reviews;
import edu.upc.eetac.dsa.cartigas.libros.api.model.ReviewsCollection;
import edu.upc.eetac.dsa.cartigas.libros.api.model.User;
@Path("/reviews")
public class ReviewsResource {
	
	
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@GET	
	@Produces(MediaType.REVIEWS_API_REVIEW)
	public ReviewsCollection getReview() {
		
		ReviewsCollection revi = new ReviewsCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			
			stmt = conn.prepareStatement(getReviewsFromDatabase());
			
			ResultSet rs = stmt.executeQuery();
			
		
			while (rs.next()) {
				Reviews review = new Reviews();
				//falta arreglar esto para que salga el usuario y cambiar lo que pone stings
				review.setLibroid(rs.getString("libroid"));
				review.setUsername(rs.getString("username"));
				review.setReviewid(rs.getString("reviewid"));
				review.setSummary(rs.getString("summary"));
				revi.addReview(review);
				
				
			}
			
		} catch (SQLException e) {
			throw new ServerErrorException(e.getMessage(),
					Response.Status.INTERNAL_SERVER_ERROR);
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}

		return revi;
	}
	
	private String getReviewsFromDatabase()
	{
		
	return "select * from reviews ";
	}
	
	
	@GET
	@Path("/{reviewid}")
	@Produces(MediaType.LIBROS_API_LIBRO)
	public Reviews getReview2(@PathParam("reviewid") String reviewid,
			@Context Request request) {
		// Create CacheControl
		CacheControl cc = new CacheControl();

		Reviews review = new Reviews();
		
		
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		
		try {
			stmt = conn.prepareStatement(getReviewFromDatabase());
			stmt.setInt(1, Integer.valueOf(reviewid));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				review.setLibroid(rs.getString("libroid"));
				review.setSummary(rs.getString("summary"));
				review.setUsername(rs.getString("username"));
				review.setReviewid(rs.getString("reviewid"));
				
				
			} else {
				throw new NotFoundException("no hay ningun review con el id="
						+ reviewid);
			}

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
			}
		}
		

		// Calculate the ETag on last modified date of user resource
		

		// Verify if it matched with etag available in http request
		

		// If ETag matches the rb will be non-null;
		// Use the rb to return the response without any further processing
		

		// If rb is null then either it is first time request; or resource is
		// modified
		// Get the updated representation and return with Etag attached to it
		

		return review;
	}
	private String getReviewFromDatabase()
	{
		
		return "select * from reviews where reviewid = ?";
	}
	
//____________________________________________________________________________________________________________________________	
	
	
	@POST
	@Consumes(MediaType.REVIEWS_API_REVIEW)
	@Produces(MediaType.REVIEWS_API_REVIEW)
	public Reviews createReview(Reviews revi) {
		

		validateReview(revi); // va al método validateSting();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		
		PreparedStatement stmt = null;
		Reviews review = new Reviews();
		try {
			String  sql = query();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);		
			stmt.setString(1, revi.getLibroid());
			stmt.setString(2, revi.getUsername());
			ResultSet rs = stmt.executeQuery();
			boolean a = false;
			
			if (rs.next()) {
				review.setLibroid(rs.getString("libroid"));
				review.setUsername(rs.getString("username"));
				
					a =true;
				
			}	
			stmt.close();
			
			if(a != true )
			{
			String sql2 = buildInsertReview();
			
			stmt = conn.prepareStatement(sql2, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, revi.getLibroid());
			stmt.setString(2, revi.getReviewid());
			stmt.setString(3, revi.getSummary());
			stmt.setString(4, revi.getUsername());
			stmt.executeUpdate();// te
																				// devuelve
			}	else {
				throw new NotFoundException("ya hay un review asi"
						);
			}																// la
																				// clave
																				// primaria
																			// que
																				// se
																				// ha
																				// genetado
			// devuelve la id de quien lo ha creado, y con esto si que puede
			// obenter las generatedKeys
			
			// la linea de security como la aplico? tengo la base de datos sin username en libros
		//	stmt.setString(1, security.getUserPrincipal().getName());
			
			
		
			// se ha insertado en la base de datos

			// si ha ido bien la inserción
			ResultSet rs2 = stmt.getGeneratedKeys();
			if (rs.next()) {
				int reviid = rs2.getInt(1);

			//	revi = getLibrosFromDatabase(Integer.toString(reviid));
				// se utiliza el método sting para pasarle el stingid
				// para crear el sting -> JSON
			} else {
				// Something has failed...
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}

		return revi;
	}
	private void validateReview(Reviews review) {
		if (review.getLibroid() == null)
			throw new BadRequestException("libroid can't be null.");
		if (review.getReviewid() == null)
			throw new BadRequestException("reviewid cant be null.");
		if (review.getSummary() == null)
			throw new BadRequestException("summary cant be null.");
		if (review.getUsername() == null)
			throw new BadRequestException("username cant be null");
		
		if (review.getLibroid().length() > 254)
			throw new BadRequestException(
					"libroid can't be greater than 255 characters.");
		if (review.getReviewid().length() > 254)
			throw new BadRequestException(
					"Reviewid can't be greater than 254 characters.");
		if (review.getSummary().length() > 254)
			throw new BadRequestException(
					"Content can't be greater than 69 characters.");
		if (review.getUsername().length() > 255)
			throw new BadRequestException(
					"Username can't be greater than 255 characters.");
	}
	
	
	private String buildInsertReview() {
		return "insert into reviews (libroid, reviewid,  summary, username) value (?, ?, ?,?)";
	}
	private String query()
	{
		return "select libroid, username from reviews where libroid = ? and username = ?";
	}
	
}
