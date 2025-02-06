package fr.isen.java2.db.daos;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import java.sql.Connection;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import fr.isen.java2.db.entities.Genre;
import fr.isen.java2.db.entities.Movie;

public class MovieDaoTestCase {

	private MovieDao movieDao = new MovieDao();

	@BeforeEach
	public void initDb() throws Exception {
		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement stmt = connection.createStatement();
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS genre (idgenre INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT , name VARCHAR(50) NOT NULL);");
		stmt.executeUpdate(
				"CREATE TABLE IF NOT EXISTS movie (\r\n"
				+ "  idmovie INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\r\n" + "  title VARCHAR(100) NOT NULL,\r\n"
				+ "  release_date DATETIME NULL,\r\n" + "  genre_id INT NOT NULL,\r\n" + "  duration INT NULL,\r\n"
				+ "  director VARCHAR(100) NOT NULL,\r\n" + "  summary MEDIUMTEXT NULL,\r\n"
				+ "  CONSTRAINT genre_fk FOREIGN KEY (genre_id) REFERENCES genre (idgenre));");
		stmt.executeUpdate("DELETE FROM movie");
		stmt.executeUpdate("DELETE FROM genre");
		stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='movie'");
		stmt.executeUpdate("DELETE FROM sqlite_sequence WHERE name='genre'");
		stmt.executeUpdate("INSERT INTO genre(idgenre,name) VALUES (1,'Drama')");
		stmt.executeUpdate("INSERT INTO genre(idgenre,name) VALUES (2,'Comedy')");
		stmt.executeUpdate("INSERT INTO movie(idmovie,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (1, 'Title 1', '2015-11-26 12:00:00.000', 1, 120, 'director 1', 'summary of the first movie')");
		stmt.executeUpdate("INSERT INTO movie(idmovie,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (2, 'My Title 2', '2015-11-14 12:00:00.000', 2, 114, 'director 2', 'summary of the second movie')");
		stmt.executeUpdate("INSERT INTO movie(idmovie,title, release_date, genre_id, duration, director, summary) "
				+ "VALUES (3, 'Third title', '2015-12-12 12:00:00.000', 2, 176, 'director 3', 'summary of the third movie')");
		stmt.close();
		connection.close();
	}
	
	 @Test
	 public void shouldListMovies() {
		// WHEN
		List<Movie> genres = movieDao.listMovies();
		// THEN
		assertThat(genres).hasSize(3);
		assertThat(genres).extracting("title", "director").containsOnly(tuple("Title 1", "director 1"), tuple("My Title 2", "director 2"),
				tuple("Third title", "director 3"));
	 }
	
	 @Test
	 public void shouldListMoviesByGenre() {
		 // WHEN
		List<Movie> genres = movieDao.listMoviesByGenre("Comedy");
		// THEN
		assertThat(genres).hasSize(2);
		assertThat(genres).extracting("title", "director").containsOnly(tuple("My Title 2", "director 2"),
				tuple("Third title", "director 3"));
	 }
	
	 @Test
	 public void shouldAddMovie() throws Exception {
		Movie movie = new Movie("4th Title", new Date(2015-12-11).toLocalDate(), new Genre(1, "Drama"), 120, "director 4", "summary of the new movie");
		Movie result_movie = movieDao.addMovie(movie);

		Connection connection = DataSourceFactory.getDataSource().getConnection();
		Statement statement = connection.createStatement();
		ResultSet resultSet = statement.executeQuery("SELECT * FROM movie JOIN genre ON movie.genre_id = genre.idgenre WHERE movie.title='4th Title'");
		assertThat(resultSet.next()).isTrue();
		assertThat(resultSet.getInt("idmovie")).isEqualTo(result_movie.getId());
		assertThat(resultSet.getString("title")).isEqualTo("4th Title");
		assertThat(resultSet.next()).isFalse();
		assertThat(result_movie.getTitle()).isEqualTo("4th Title");
		resultSet.close();
		statement.close();
		connection.close();
	 }
}
