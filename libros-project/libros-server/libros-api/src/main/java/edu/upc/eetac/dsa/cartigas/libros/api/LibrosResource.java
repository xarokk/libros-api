package edu.upc.eetac.dsa.cartigas.libros.api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

import javax.sql.DataSource;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.ForbiddenException;
import javax.ws.rs.GET;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.ServerErrorException;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.Request;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.SecurityContext;

import edu.upc.eetac.dsa.cartigas.libros.api.MediaType;
import edu.upc.eetac.dsa.cartigas.libros.api.model.Libros;
import edu.upc.eetac.dsa.cartigas.libros.api.model.LibrosCollection;
import edu.upc.eetac.dsa.cartigas.libros.api.model.Reviews;
import edu.upc.eetac.dsa.cartigas.libros.api.model.ReviewsCollection;
import edu.upc.eetac.dsa.cartigas.libros.api.model.User;
import edu.upc.eetac.dsa.cartigas.libros.api.DataSourceSPA;

@Path("/libros")
public class LibrosResource {
	private DataSource ds = DataSourceSPA.getInstance().getDataSource();

	@GET
	@Produces(MediaType.LIBROS_API_LIBRO_COLLECTION)
	public LibrosCollection getLibros(@QueryParam("length") int length,
			@QueryParam("before") long before, @QueryParam("after") long after) {
		LibrosCollection libros = new LibrosCollection();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			boolean updateFromLast = after > 0;
			stmt = conn.prepareStatement(buildGetLibrosQuery(updateFromLast));
			if (updateFromLast) {
				stmt.setTimestamp(1, new Timestamp(after));
			} else {
				if (before > 0)
					stmt.setTimestamp(1, new Timestamp(before));
				else
					stmt.setTimestamp(1, null);
				length = (length <= 0) ? 5 : length;
				stmt.setInt(2, length);
			}
			ResultSet rs = stmt.executeQuery();
			boolean first = true;
			long oldestTimestamp = 0;
			while (rs.next()) {
				Libros libro = new Libros();
				User usuario = new User();
				// falta arreglar esto para que salga el usuario y cambiar lo
				// que pone stings
				libro.setLibroid(rs.getString("libroid"));
				libro.setTitulo(rs.getString("titulo"));
				usuario.setUsername(rs.getString("name"));
				libro.setAutor(rs.getString("autor"));
				libro.setLengua(rs.getString("lengua"));
				libro.setEditorial(rs.getString("editorial"));
				libro.setEdicion(rs.getString("edicion"));
				oldestTimestamp = rs.getTimestamp("fecha").getTime();
				libro.setLast_modified(oldestTimestamp);
				if (first) {
					first = false;
					libros.setNewestTimestamp(libro.getLast_modified());
				}
				libros.addLibros(libro);
			}
			libros.setOldestTimestamp(oldestTimestamp);
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

		return libros;
	}

	@Context
	private SecurityContext security;

	private String buildGetLibrosQuery(boolean updateFromLast) {
		if (updateFromLast)
			return "select l.*, u.name from libros l, users u ,reviews r where u.username = r.username and l.libroid = r.libroid and l.fecha > ? order by fecha desc"; //
		else
			return "select l.*, u.name from libros l, users u ,reviews r where u.username = r.username and l.libroid = r.libroid and l.fecha < ifnull(?, now()) order by fecha desc limit ? "; //
	}

	// a partir de aqui esta todo lo que corresponde a el post se acaba donde la
	// linea.

	@POST
	@Consumes(MediaType.LIBROS_API_LIBRO)
	@Produces(MediaType.LIBROS_API_LIBRO)
	public Libros createLibro(Libros libro) {

		validateLibro(libro); // va al método validateSting();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildInsertLibro();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS); // te
																				// devuelve
																				// la
																				// clave
																				// primaria
																				// que
																				// se
																				// ha
																				// genetado
			// devuelve la id de quien lo ha creado, y con esto si que puede
			// obenter las generatedKeys

			// la linea de security como la aplico? tengo la base de datos sin
			// username en libros
			// stmt.setString(1, security.getUserPrincipal().getName());
			stmt.setString(1, libro.getTitulo());
			stmt.setString(2, libro.getLengua());
			stmt.setString(3, libro.getEdicion());
			stmt.setString(4, libro.getAutor());
			stmt.executeUpdate();
			// se ha insertado en la base de datos

			// si ha ido bien la inserción
			ResultSet rs = stmt.getGeneratedKeys();
			if (rs.next()) {
				int stingid = rs.getInt(1);

				libro = getLibrosFromDatabase(Integer.toString(stingid));
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

		return libro;
	}

	private String buildInsertLibro() {
		return "insert into libros (titulo, lengua, edicion, autor) value (?, ?, ?,?)";
	}

	private void validateLibro(Libros libro) {
		if (libro.getAutor() == null)
			throw new BadRequestException("author can't be null.");
		if (libro.getLengua() == null)
			throw new BadRequestException("lengua cant be null.");
		if (libro.getEdicion() == null)
			throw new BadRequestException("edicion cant be null.");
		if (libro.getTitulo() == null)
			throw new BadRequestException("titulo cant be null");

		if (libro.getAutor().length() > 254)
			throw new BadRequestException(
					"Subject can't be greater than 255 characters.");
		if (libro.getLengua().length() > 254)
			throw new BadRequestException(
					"Content can't be greater than 254 characters.");
		if (libro.getTitulo().length() > 70)
			throw new BadRequestException(
					"Content can't be greater than 69 characters.");
		if (libro.getEdicion().length() > 255)
			throw new BadRequestException(
					"Content can't be greater than 255 characters.");
	}

	private Libros getLibrosFromDatabase(String libroid) {
		Libros libro = new Libros();

		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			stmt = conn.prepareStatement(buildGetLibroByIdQuery());
			stmt.setInt(1, Integer.valueOf(libroid));
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				libro.setLibroid(rs.getString("libroid"));
				libro.setTitulo(rs.getString("titulo"));
				libro.setAutor(rs.getString("autor"));
				libro.setLengua(rs.getString("lengua"));
				libro.setEditorial(rs.getString("editorial"));
				libro.setEdicion(rs.getString("edicion"));

				libro.setLast_modified(rs.getTimestamp("fecha").getTime());
			} else {
				throw new NotFoundException("no hay ningun libro con el id="
						+ libroid);
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

		return libro;
	}

	private String buildGetLibroByIdQuery() {
		return "select l.*, u.name from libros l, users u , reviews r where u.username=r.username and l.libroid=?";
	}

	// --------------------------------------------------------------------------------------------
	// aqui ehay el GET de 1

	@GET
	@Path("/{libroid}")
	@Produces(MediaType.LIBROS_API_LIBRO)
	public Response getLibro(@PathParam("libroid") String libroid,
			@Context Request request) {
		// Create CacheControl
		CacheControl cc = new CacheControl();

		Libros libro = getLibrosFromDatabase(libroid);

		// Calculate the ETag on last modified date of user resource
		EntityTag eTag = new EntityTag(Long.toString(libro.getLast_modified()));

		// Verify if it matched with etag available in http request
		Response.ResponseBuilder rb = request.evaluatePreconditions(eTag);

		// If ETag matches the rb will be non-null;
		// Use the rb to return the response without any further processing
		if (rb != null) {
			return rb.cacheControl(cc).tag(eTag).build();
		}

		// If rb is null then either it is first time request; or resource is
		// modified
		// Get the updated representation and return with Etag attached to it
		rb = Response.ok(libro).cacheControl(cc).tag(eTag);

		return rb.build();
	}

	// ------------------------------------------------------------------------------------

	@DELETE
	@Path("/{libroid}")
	public void deleteLibro(@PathParam("libroid") String libroid) {
		// validateUser(libroid);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildDeleteLibro();
			stmt = conn.prepareStatement(sql);
			stmt.setInt(1, Integer.valueOf(libroid));

			int rows = stmt.executeUpdate();

			if (rows == 0) {
				throw new NotFoundException("There's no sting with stingid="
						+ libroid);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {

				// Haya ido bien o haya ido mal cierra las conexiones
				if (stmt != null)
					stmt.close();
				conn.close();
			} catch (SQLException e) {
				throw new ServerErrorException(e.getMessage(),
						Response.Status.INTERNAL_SERVER_ERROR);
			}
		}
	}

	private String buildDeleteLibro() {
		return "delete from libros where libroid=?";
	}

	/*
	 * private void validateUser(String stingid) { Stings currentSting =
	 * getStingFromDatabase(stingid); if (!security.getUserPrincipal().getName()
	 * .equals(currentSting.getUsername())) throw new ForbiddenException(
	 * "You are not allowed to modify this sting."); }
	 */

	// -------------------------------------------------------------------------
	// aqui empieza el editar

	@PUT
	@Path("/{librosid}")
	@Consumes(MediaType.LIBROS_API_LIBRO)
	@Produces(MediaType.LIBROS_API_LIBRO)
	public Libros updateLibroFromDatabase(@PathParam("libroid") String libroid,
			Libros libro) {
		// validateUser(stingid);
		validateUpdateLibro(libro);
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}

		PreparedStatement stmt = null;
		try {
			String sql = buildUpdateLibro();
			stmt = conn.prepareStatement(sql);
			stmt.setString(1, libro.getTitulo());
			stmt.setString(2, libro.getLengua());
			stmt.setString(3, libro.getEdicion());
			stmt.setString(4, libro.getAutor());

			stmt.setInt(5, Integer.valueOf(libroid));

			int rows = stmt.executeUpdate();
			if (rows == 1)
				libro = getLibrosFromDatabase(libroid);
			else {
				throw new NotFoundException("There's no book with libroid="
						+ libroid);
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

		return libro;
	}

	private String buildUpdateLibro() {
		return "update libros set titulo=ifnull(?, titulo), lengua=ifnull(?, lengua) ,edicion=ifnull(?, edicion), autor=ifnull(?, autor) where libroid=?";
	}

	private void validateUpdateLibro(Libros libro) {
		if (libro.getTitulo() != null && libro.getTitulo().length() > 70)
			throw new BadRequestException(
					"El titulo no puede ser mayor que 70!");
		if (libro.getAutor() != null && libro.getAutor().length() > 255)
			throw new BadRequestException(
					"El autor no puede ser mayor que 255!.");

		if (libro.getLengua() != null && libro.getLengua().length() > 255)
			throw new BadRequestException(
					"la lengua no puede ser mayor que 255!.");

		if (libro.getEditorial() != null && libro.getEditorial().length() > 255)
			throw new BadRequestException(
					"El editorial no puede ser mayor que 255!.");

		if (libro.getEdicion() != null && libro.getEdicion().length() > 255)
			throw new BadRequestException(
					"la edicion no puede ser mayor que 255!.");
	}

	@GET
	@Path("/search")
	@Produces(MediaType.LIBROS_API_LIBRO_COLLECTION)
	public LibrosCollection searchByContentSubject(
			@QueryParam("autor") String Autor,

			@QueryParam("titulo") String Titulo,
			@QueryParam("length") int length) {
		LibrosCollection libro = new LibrosCollection();
		Connection conn = null;
		try {
			conn = ds.getConnection();
		} catch (SQLException e) {
			throw new ServerErrorException("Could not connect to the database",
					Response.Status.SERVICE_UNAVAILABLE);
		}
		PreparedStatement stmt = null;
		try {
			// asi estoy pasandole la query que es lo que hay en
			// buildgetstingsubjectcontent
			stmt = conn.prepareStatement(buildGetLibroSubjectContent(Autor,
					Titulo, length));

			if (Autor != null && Titulo != null) {
				if (length <= 0) {
					stmt.setString(1, "%" + Autor + "%");
					stmt.setString(2, "%" + Titulo + "%");
				} else {
					stmt.setString(1, "%" + Autor + "%");
					stmt.setString(2, "%" + Titulo + "%");
					stmt.setInt(3, length);
				}

			}

			if (Autor != null && Titulo == null) {
				if (length <= 0) {
					stmt.setString(1, "%" + Titulo + "%");
				} else {
					stmt.setString(1, "%" + Titulo + "%");
					stmt.setInt(2, length);
				}
			}
			if (Autor != null && Titulo == null) {
				if (length <= 0) {
					stmt.setString(1, "%" + Autor + "%");
				} else {
					stmt.setString(1, "%" + Autor + "%");
					stmt.setInt(2, length);
				}

			}
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				Libros l = new Libros();
				l.setAutor(rs.getString("autor"));
				l.setTitulo(rs.getString("titulo"));
				l.setEdicion(rs.getString("edicion"));
				l.setEditorial(rs.getString("editorial"));
				l.setLibroid(rs.getString("editorial"));
				l.setLengua(rs.getString("lengua"));

				// lo primero es la coleccion sting, luego es el sting en si (s)
				libro.addLibros(l);

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

		return libro;
	}

	@Path("/{idlibro}/reviews")
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
			String sql = query();
			stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			stmt.setString(1, revi.getLibroid());
			stmt.setString(2, revi.getUsername());
			ResultSet rs = stmt.executeQuery();
			boolean a = false;

			if (rs.next()) {
				review.setLibroid(rs.getString("libroid"));
				review.setUsername(rs.getString("username"));

				a = true;

			}
			stmt.close();

			if (a != true) {
				String sql2 = buildInsertReview();

				stmt = conn.prepareStatement(sql2,
						Statement.RETURN_GENERATED_KEYS);
				stmt.setString(1, revi.getLibroid());
				stmt.setString(2, revi.getReviewid());
				stmt.setString(3, revi.getSummary());
				stmt.setString(4, revi.getUsername());
				stmt.executeUpdate();// te
										// devuelve
			} else {
				throw new NotFoundException("ya hay un review asi");
			} // la
				// clave
				// primaria
			// que
			// se
			// ha
			// genetado
			// devuelve la id de quien lo ha creado, y con esto si que puede
			// obenter las generatedKeys

			// la linea de security como la aplico? tengo la base de datos sin
			// username en libros
			// stmt.setString(1, security.getUserPrincipal().getName());

			// se ha insertado en la base de datos

			// si ha ido bien la inserción
			ResultSet rs2 = stmt.getGeneratedKeys();
			if (rs.next()) {
				int reviid = rs2.getInt(1);

				// revi = getLibrosFromDatabase(Integer.toString(reviid));
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

	private String query() {
		return "select libroid, username from reviews where libroid = ? and username = ?";
	}

	public static int lengthDefecto = 5;

	private String buildGetLibroSubjectContent(String Autor, String Titulo,
			int length) {
		String query = null;

		if (Autor != null && Titulo != null) {
			if (length <= 0) {
				System.out.print("entrooooo1");
				query = "Select l.* , u.name from libros l, users u,  reviews r where u.username=r.username && autor like ? && titulo like ? limit "
						+ lengthDefecto;
			} else {
				System.out.print("entrooooo2");
				query = "Select l.* , u.name from libros l, users u,revies r  where u.username=r.username && autor like ? && titulo like ? limit ? ";
			}
		}
		if (Autor != null && Titulo == null) {
			if (length <= 0) {
				System.out.print("entrooooo3");
				query = "Select l.*, u.name from libros l, users u, reviews r where u.username=r.username && autor like ? limit "
						+ lengthDefecto;
			} else {
				System.out.print("entrooooo4");
				query = "Select l.*, u.name from libros l, users u , reviews r where u.username=r.username && autor like ? limit ?";
			}
		}
		if (Titulo != null && Autor == null) {
			if (length <= 0) {
				System.out.print("entrooooo5");
				query = "Select l.*,u.name from libros l , users u, reviews r where u.username=r.username && titulo like ? limit "
						+ lengthDefecto;
			} else {
				System.out.print("entrooooo6");
				query = "Select l.*,u.name from libros l , users u, reviews r where u.username=r.username && titulo like ? limit ? ";
			}
		}
		if (Titulo == null && Autor == null) {
			if (length <= 0) {
				System.out.print("entrooooo7");
				query = "Select l.* , u.name from libros l, users u, reviews r where u.username=l.username limit "
						+ lengthDefecto;
			} else {
				System.out.print("entrooooo8");
				query = "Select l.* , u.name from libros l, users u, reviews r where u.username=l.username limit ?";
			}
		}
		return query;
	}
	@Path("/{idlibro}/reviews")
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
	@Path("/{idlibro}/reviews/{reviewid}")
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
	
}

