package co.bvc.com.basicfix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

import co.bvc.com.dao.domain.AutFixRfqDatosCache;

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

	public static void cargarCache(AutFixRfqDatosCache datosCache) throws SQLException {

		PreparedStatement ps = conn
				.prepareStatement("INSERT INTO `bvc_automation_db`.`aut_fix_rfq_cache` VALUES (?, ?, ?, ?, ?, ?, ?)");
		ps.setString(1, datosCache.getReceiverSession());
		ps.setInt(2, datosCache.getIdCaseseq());
		ps.setInt(3, datosCache.getIdCase());
		ps.setInt(4, datosCache.getIdSecuencia());
		ps.setString(5, datosCache.getEstado());
		ps.setString(6, datosCache.getFixQuoteReqId());
		ps.setString(7, datosCache.getIdAfiliado());
		ps.executeUpdate();

	}

	public static AutFixRfqDatosCache obtenerCache(String session) throws SQLException {

		AutFixRfqDatosCache datosCache = null;
		String queryInicio = "SELECT * FROM bvc_automation_db.aut_fix_rfq_cache WHERE  RECEIVER_SESSION = " + session;
		ResultSet rs = DataAccess.getQuery(queryInicio);
		System.out.println("RS " + rs);

		while (rs.next()) {
			// Crea el objeto recuperado.
			datosCache = new AutFixRfqDatosCache();
			// Establecer el objeto recibido.
			datosCache.setReceiverSession(rs.getString("RECEIVER_SESSION"));
			datosCache.setIdCaseseq(rs.getInt("ID_CASESEQ"));
			datosCache.setIdCase(rs.getInt("ID_CASE"));
			datosCache.setIdSecuencia(rs.getInt("ID_SECUENCIA"));
			datosCache.setEstado(rs.getString("ESTADO"));
			datosCache.setFixQuoteReqId(rs.getString("FIX_QUOTE_REQ_ID"));
			datosCache.setIdAfiliado(rs.getString("ID_AFILIADO"));

		}
		return datosCache;

	}

}
