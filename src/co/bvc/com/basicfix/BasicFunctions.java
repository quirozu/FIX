package co.bvc.com.basicfix;

import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.bvc.com.dao.domain.RespuestaConstrucccionMsgFIX;
import co.bvc.com.test.AdapterIO;
import co.bvc.com.test.Login;
import quickfix.Application;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.MessageFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

public class BasicFunctions {

	private static Connection conn;
	private static Login login;
	private static String quoteReqId;
	private static String quoteId;
	private static long idEjecution;
	private static int idCaseSeq;
	private static AdapterIO adapterIO;
	private static int idCase;
//	private static RespuestaConstrucccionMsgFIX cache;

	public static int getIdCase() {
		return idCase;
	}

	public static void setIdCase(int d) {
		BasicFunctions.idCase = d;
	}

	public static Connection getConn() {
		return conn;
	}

	public static void setConn(Connection conn) {
		BasicFunctions.conn = conn;
	}

	public static Login getLogin() {
		return login;
	}

	public static void setLogin(Login login) {
		BasicFunctions.login = login;
	}

	public static String getQuoteReqId() {
		return quoteReqId;
	}

	public static void setQuoteReqId(String quoteReqId) {
		BasicFunctions.quoteReqId = quoteReqId;
	}

	public static String getQuoteId() {
		return quoteId;
	}

	public static void setQuoteId(String quoteId) {
		BasicFunctions.quoteId = quoteId;
	}

	public static long getIdEjecution() {
		return idEjecution;
	}

	public static void setIdEjecution(long idEjecution) {
		BasicFunctions.idEjecution = idEjecution;
	}

	public static int getIdCaseSeq() {
		return idCaseSeq;
	}

	public static void setIdCaseSeq(int idCaseSeq) {
		BasicFunctions.idCaseSeq = idCaseSeq;
	}

	public static AdapterIO getAdapterIO() {
		return adapterIO;
	}

	public static void setAdapterIO(AdapterIO adapterIO) {
		BasicFunctions.adapterIO = adapterIO;
	}
	
//	public static RespuestaConstrucccionMsgFIX getCache() {
//		return cache;
//	}
//
//	public static void setCache(RespuestaConstrucccionMsgFIX cache) {
//		BasicFunctions.cache = cache;
//	}

	/**
	 * Crea la conexión a la db y se la asigna a la variable conn de BasicFunctions
	 * 
	 * @return
	 */
	public static boolean createConn() {
		boolean retorno = false;

		//
		BasicFunctions.conn = DataAccess.getConnection();
		if (BasicFunctions.conn != null) {
			retorno = true;
		}

		return retorno;
	}

	/**
	 * Se crea el adaptador y las sessiones y el login con el motor de INET
	 * 
	 * @return
	 */
	public static void createLogin() {
		if (BasicFunctions.adapterIO == null) {
			BasicFunctions.adapterIO = new AdapterIO();
		
		}

		if (BasicFunctions.login == null) {
			BasicFunctions.login = new Login();
			BasicFunctions.login.initiation();
		}
	}

	public static void startVariables() {
		SimpleDateFormat SDF = new SimpleDateFormat("yyyMMddHmmss");
		long id_ejecution = Long.parseLong(SDF.format(new Date()));
		System.out.println("ID_EJECUCION GENERADO : " + id_ejecution);

		BasicFunctions.setIdEjecution(id_ejecution);
		
	}
	
	public static int getFirtsIdCaseSeq(int escenarioEjecucion) throws SQLException {
		int firstIdDB = DataAccess.getFirstIdCaseSeq(escenarioEjecucion);
		return firstIdDB;
	}

	
	public static void imprimir(String vari) {
		System.out.println("\n#####################\nCLASE: "+ vari.getClass()+ "VARIABLE: "+ vari + "\n#####################");
	}
	public static void imprimir(int vari) {
		System.out.println("\n#####################\nVARIABLE ENTERA: "+ vari + "\n#####################");
	}
	public static void imprimir(boolean vari) {
		System.out.println("\n#####################\nVARIABLE BOOLEAN: "+ vari + "\n#####################");
	}

}
