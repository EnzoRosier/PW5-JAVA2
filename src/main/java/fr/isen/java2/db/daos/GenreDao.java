package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Genre;

public class GenreDao {

	public List<Genre> listGenres() {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM genre")) {
				try (ResultSet results = statement.executeQuery()) {
					List<Genre> genres = new ArrayList<Genre>();
					while (results.next()) {
						Genre genre = new Genre(
								results.getInt("idgenre"),
								results.getString("name"));
						genres.add(genre);
					}
					return genres;
				}
			}
		} catch (SQLException e) {
			// Manage Exception
			e.printStackTrace();
		}
		return null;
	}

	public Genre getGenre(String name) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM genre WHERE name=?")) {
				statement.setString(1, name);
				try (ResultSet results = statement.executeQuery()) {
					if (results.next()) {
						return new Genre(
								results.getInt("idgenre"),
								results.getString("name"));
					}
				}
			}
		} catch (SQLException e) {
			// Manage Exception
			e.printStackTrace();
		}
		return null;
	}

	public void addGenre(String name) {
		try (Connection connection = DataSourceFactory.getDataSource().getConnection()) {
			try (PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO genre(name) VALUES(?)")) {
				statement.setString(1, name);
				statement.executeUpdate();
			}
		} catch (SQLException e) {
			// Manage Exception
			e.printStackTrace();
		}
	}
}
