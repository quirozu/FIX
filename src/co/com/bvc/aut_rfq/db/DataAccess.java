package co.com.bvc.aut_rfq.db;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.sql.Types;
import java.util.Enumeration;
import java.util.LinkedList;
import java.util.Properties;

import co.com.bvc.aut_rfq.basicfix.BasicFunctions;
import co.com.bvc.aut_rfq.dao.domain.AutFixRfqDatosCache;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;

public class DataAccess {
	
	private String usuario;
	private String password;
	private String driver;
	private static Connection conn = null;
	
	public DataAccess(){
		
		Properties prop = new Properties();
		InputStream is = null;
		
		try {
			is = new FileInputStream("resources//aut_rfq.properties");
			prop.load(is);
		} catch(IOException e) {
			System.out.println(e.toString());
		}
		
		String url = "jdbc:mysql://" + prop.getProperty("servidor.ip").trim() + ":" 
		+ prop.getProperty("servidor.port").trim()	+ "/" + prop.getProperty("servidor.database").trim();
		
//		System.out.println("URL: " + url);
		
		driver = prop.getProperty("servidor.driver").trim();
		usuario = prop.getProperty("servidor.username").trim();
		password = prop.getProperty("servidor.password").trim();
		  
		try{
		    Class.forName(driver);
		    conn = DriverManager.getConnection(url, usuario, password);
		}
		catch(Exception e){
		    e.printStackTrace();
		}
		    
	}	
	
	public static Connection getConnection() {
		if(conn == null){
			new DataAccess();
		}
		
		return conn;
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

	public static int getFirstIdCaseSeq(int escenarioEjecucion) throws SQLException {

		String queryInicio = "SELECT ID_CASESEQ FROM aut_fix_rfq_datos WHERE ID_CASE= "
				+ escenarioEjecucion + " ORDER BY ID_CASESEQ ASC LIMIT 1";

		ResultSet rs = DataAccess.getQuery(queryInicio); 
		int idCaseSeq = -1;
		
		while (rs.next()) {
			idCaseSeq = rs.getInt("ID_CASESEQ");
			BasicFunctions.setIdCaseSeq(idCaseSeq);
			
//			BasicFunctions.imprimir(idCaseSeq);
		}

		System.out.println("PRIMER ID_CASESEQ: "+idCaseSeq);
		return idCaseSeq;
	}

	public static ResultSet datosMensaje(int idCaseSeq) throws SQLException {

		String queryDatos = "SELECT * FROM aut_fix_rfq_datos WHERE ID_CASESEQ=" + idCaseSeq;
		ResultSet rsDatos = DataAccess.getQuery(queryDatos);

		return rsDatos;
	}

	public static void setQuery(String _query) throws SQLException {
		Statement state = null;
		int resultSet = 0;
		try {
			state = (Statement) conn.createStatement();
			resultSet = state.executeUpdate(_query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public static void cargarCache(AutFixRfqDatosCache datosCache) throws SQLException {

		PreparedStatement ps = conn.prepareStatement(
				"INSERT INTO `aut_fix_rfq_cache` VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
		ps.setString(1, datosCache.getReceiverSession());
		ps.setInt(2, datosCache.getIdCaseseq());
		ps.setInt(3, datosCache.getIdCase());
		ps.setInt(4, datosCache.getIdSecuencia());
		ps.setString(5, datosCache.getEstado());
		ps.setString(6, datosCache.getFixQuoteReqId());
		ps.setString(7, datosCache.getIdAfiliado());
		ps.setLong(8, datosCache.getIdEjecucion());
		ps.executeUpdate();

	}


	public static AutFixRfqDatosCache obtenerCache(String sessionRec) throws SQLException, InterruptedException {
		Thread.sleep(5000);
		AutFixRfqDatosCache datosCache = null;
		String queryInicio = "SELECT * FROM aut_fix_rfq_cache WHERE RECEIVER_SESSION = '" + sessionRec
				+ "'";
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
			datosCache.setIdEjecucion(rs.getLong("ID_EJECUCION"));

		}
		return datosCache;

	}

	public static void cargarLogsExitosos(Message message, long ID_EJECUCION, String clave, String valor,
			String idEscenario, String idCase, int idSecuencia, String clavePrima) throws SQLException {

		PreparedStatement ps = conn.prepareStatement(
				"INSERT INTO `aut_log_ejecucion`(`ID_EJECUCION`, `ID_ESCENARIO`, `COD_CASO`, `ID_SECUENCIA`, `FECHA_EJECUCION`, `ESTADO_EJECUCION`, `DESCRIPCION_VALIDACION`, `MENSAJE`, `CODIGO_ERROR`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		ps.setLong(1, ID_EJECUCION);
		ps.setString(2, idEscenario);
		ps.setString(3, idCase);
		ps.setInt(4, idSecuencia);
		ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
		ps.setString(6, "EXITOSO");
		ps.setString(7, "(" + clavePrima + ") MSG: " + clave + " BD: " + valor);
		ps.setString(8, "");
		ps.setNull(9, Types.INTEGER);
		ps.executeUpdate();

	}

	public static void limpiarCache() throws SQLException {
		String strQueryLimpiar = "DELETE FROM `aut_fix_rfq_cache` WHERE  RECEIVER_SESSION <> ''";
		setQuery(strQueryLimpiar);
	}

	public static void cargarLogsFallidos(Message message, long ID_EJECUCION, String clave, String valor,
			String idEscenario, String idCase, int idSecuencia, String clavePrima) throws SQLException {

		PreparedStatement ps = conn.prepareStatement(
				"INSERT INTO `aut_log_ejecucion` (`ID_EJECUCION`, `ID_ESCENARIO`, `COD_CASO`, `ID_SECUENCIA`, `FECHA_EJECUCION`, `ESTADO_EJECUCION`, `DESCRIPCION_VALIDACION`, `MENSAJE`, `CODIGO_ERROR`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");
		ps.setLong(1, ID_EJECUCION);
		ps.setString(2, idEscenario);
		ps.setString(3, idCase);
		ps.setInt(4, idSecuencia);
		ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));
		ps.setString(6, "FALLIDO");
		ps.setString(7, "(" + clavePrima + ") MSG: " + clave + " BD: " + valor);
		ps.setString(8, message.toString());
		ps.setNull(9, Types.INTEGER);
		ps.executeUpdate();

	}

	public static boolean validarContinuidadEjecucion() throws SQLException {

		String query = "SELECT count(1) as cantidad FROM aut_fix_rfq_cache";

		ResultSet i = DataAccess.getQuery(query);
		int cantidadEscenarios = 0;

		while (i.next()) {
			cantidadEscenarios = i.getInt("cantidad");
			System.out.println(
					"*************** CANTIDAD MENSAJES POR VALIDAR: " + cantidadEscenarios + "\n*********************");
		}

		if (cantidadEscenarios > 0) {
			return false;
		} else {

			return true;
		}
	}

	public static void cargarLogs3(Message message, long ID_EJECUCION, String idEscenario, String idCase,
			int idSecuencia) throws SQLException, FieldNotFound {

		PreparedStatement ps = conn.prepareStatement(

				"INSERT INTO `aut_log_ejecucion`(`ID_EJECUCION`, `ID_ESCENARIO`, `COD_CASO`, `ID_SECUENCIA`, `FECHA_EJECUCION`, `ESTADO_EJECUCION`, `DESCRIPCION_VALIDACION`, `MENSAJE`, `CODIGO_ERROR`) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)");

		ps.setLong(1, ID_EJECUCION);

		ps.setString(2, idEscenario);

		ps.setString(3, idCase);

		ps.setInt(4, idSecuencia);

		ps.setTimestamp(5, new Timestamp(System.currentTimeMillis()));

		ps.setString(6, "FALLIDO");

		ps.setString(7, message.getString(58));

		ps.setString(8, message.toString());

		ps.setNull(9, Types.INTEGER);

		ps.executeUpdate();

	}
	
	public void modificarEscenrios() {
		
//		-- Mensaje de rechazo por contraparte compra
//		UPDATE `bvc_automation_db`.`aut_fix_tcr_datos` SET `AE_RECTREPTYPE2`='98' WHERE `ID_CASESEQ`='25';
//		UPDATE `bvc_automation_db`.`aut_fix_tcr_datos` SET `AE_TRADEREPTYPE`='98', `AE_RECTREPTYPE2`='98' WHERE `ID_CASESEQ`='26';
//		-- Mensaje de rechazo por contraparte venta
//		UPDATE `bvc_automation_db`.`aut_fix_tcr_datos` SET `AE_RECTREPTYPE2`='98' WHERE `ID_CASESEQ`='31';
//		UPDATE `bvc_automation_db`.`aut_fix_tcr_datos` SET `AE_TRADEREPTYPE`='98', `AE_RECTREPTYPE2`='98' WHERE `ID_CASESEQ`='32';
//		-- Bolsa rechaza anulacion
//		UPDATE `bvc_automation_db`.`aut_fix_tcr_datos` SET `AE_RECTREPTYPE2`='98' WHERE `ID_CASESEQ`='33';
//		UPDATE `bvc_automation_db`.`aut_fix_tcr_datos` SET `AE_RECTREPTYPE2`='98', `AMP_DCV`='0' WHERE `ID_CASESEQ`='34';
//		-- Mensaje mal armado por precio
//		UPDATE `bvc_automation_db`.`aut_fix_tcr_datos` SET `AE_RECTREPTYPE2`='98', `AE_LASTPX`='4200' WHERE `ID_CASESEQ`='37';
//		UPDATE `bvc_automation_db`.`aut_fix_tcr_datos` SET `AE_RECTREPTYPE2`='98', `AE_LASTPX`='4200' WHERE `ID_CASESEQ`='38';
//		-- Bolsa rechaza anulacion
//		UPDATE `bvc_automation_db`.`aut_fix_tcr_datos` SET `AE_RECTREPTYPE2`='98' WHERE `ID_CASESEQ`='39';
//		UPDATE `bvc_automation_db`.`aut_fix_tcr_datos` SET `AE_RECTREPTYPE2`='98', `AMP_DCV`='0' WHERE `ID_CASESEQ`='40';

	}
	


}
