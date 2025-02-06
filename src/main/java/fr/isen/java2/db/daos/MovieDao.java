package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Genre;
import fr.isen.java2.db.entities.Movie;

public class MovieDao {

	public List<Movie> listMovies() {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM movie JOIN genre ON movie.genre_id = genre.idgenre")) {
				try (ResultSet results = statement.executeQuery()) {
					List<Movie> movies = new ArrayList<Movie>();
					while (results.next()) {

						GenreDao genreDao = new GenreDao();
						Genre genre = genreDao.getGenre(results.getString("name"));
						Movie movie = new Movie(
								results.getString("title"),
								results.getDate("release_date").toLocalDate(),
								genre,
								results.getInt("duration"),
								results.getString("director"),
								results.getString("summary"));
						movies.add(movie);
					}
					return movies;
				}
			}
		} catch (SQLException e) {
			// Manage Exception
			e.printStackTrace();
		}
		return null;
	}

	public List<Movie> listMoviesByGenre(String genreName) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM movie JOIN genre ON movie.genre_id = genre.idgenre WHERE genre.name = ?")) {
				statement.setString(1, genreName);
				try (ResultSet results = statement.executeQuery()) {
					List<Movie> movies = new ArrayList<Movie>();
					while (results.next()) {

						GenreDao genreDao = new GenreDao();
						Genre genre = genreDao.getGenre(results.getString("name"));
						Movie movie = new Movie(
								results.getString("title"),
								results.getDate("release_date").toLocalDate(),
								genre,
								results.getInt("duration"),
								results.getString("director"),
								results.getString("summary"));
						movies.add(movie);
					}
					return movies;
				}
			}
		} catch (SQLException e) {
			// Manage Exception
			e.printStackTrace();
		}
		return null;
	}

	public Movie addMovie(Movie movie) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO movie(title,release_date,genre_id,duration,director,summary) VALUES(?,?,?,?,?,?)",
					Statement.RETURN_GENERATED_KEYS)) {
				statement.setString(1, movie.getTitle());
				statement.setDate(2, java.sql.Date.valueOf(movie.getReleaseDate()));
				statement.setInt(3, movie.getGenre().getId());
				statement.setInt(4, movie.getDuration());
				statement.setString(5, movie.getDirector());
				statement.setString(6, movie.getSummary());
				statement.executeUpdate();
				ResultSet ids = statement.getGeneratedKeys();
				if (ids.next()) {
					movie.setId(ids.getInt(1));
					return movie;
				}
				
			}
		} catch (SQLException e) {
			// Manage Exception
			e.printStackTrace();
		}
		return null;
	}
}
