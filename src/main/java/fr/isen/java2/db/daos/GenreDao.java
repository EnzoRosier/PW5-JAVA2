package fr.isen.java2.db.daos;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import fr.isen.java2.db.entities.Genre;

//ROSIER ENZO

public class GenreDao {

	public List<Genre> listGenres() {
		//Conection to the database
		try (Connection connection = DataSourceFactory.getConnection()) {
			//Construct statement
			try (PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM genre")) {
				try (ResultSet results = statement.executeQuery()) {
					List<Genre> genres = new ArrayList<Genre>();
					//We add each row to the list
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
		//Conection to the database
		try (Connection connection = DataSourceFactory.getConnection()) {
			//Construct statement
			try (PreparedStatement statement = connection.prepareStatement(
					"SELECT * FROM genre WHERE name=?")) {
				statement.setString(1, name); //Parameter for name
				try (ResultSet results = statement.executeQuery()) {
					if (results.next()) {
						//Return genre
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
		//Conection to the database
		try (Connection connection = DataSourceFactory.getConnection()) {
			//Construct statement
			try (PreparedStatement statement = connection.prepareStatement(
					"INSERT INTO genre(name) VALUES(?)")) {
				statement.setString(1, name); //Paramater for name
				statement.executeUpdate(); //Execute the statement
			}
		} catch (SQLException e) {
			// Manage Exception
			e.printStackTrace();
		}
	}
}
