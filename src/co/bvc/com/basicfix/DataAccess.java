package co.bvc.com.basicfix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class DataAccess {

	private static String _usuario;
	private static String _pwd;
	private static String _db;
	private static String HOST;
	private static String PORT;
	private static Connection conn = null;

	public void Conexion() throws SQLException {
		ParametersRead p = new ParametersRead();
		String[] lineas = p.leerConexion();
		_usuario = lineas[0].split("=")[1].trim();
		_pwd = lineas[1].split("=")[1].trim();
		_db = lineas[2].split("=")[1].trim();
		HOST = lineas[3].split("=")[1].trim();
		PORT = lineas[4].split("=")[1].trim();
		String _url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + _db;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(_url, _usuario, _pwd);
			if (conn != null) {
				System.out.println("CONECTADO ");
			}
		} catch (ClassNotFoundException c) {
			System.out.println("error");
		}
	}

	public static ResultSet getQuery(String _query) throws SQLException {
		Statement state = null;
		ResultSet resultSet = null;
		try {
			state = (Statement) conn.createStatement();
			resultSet = state.executeQuery(_query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public static void setQuery(String _query) throws SQLException {
		Statement state = null;
		int resultSet;
		try {
			state = (Statement) conn.createStatement();
			resultSet = state.executeUpdate(_query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
