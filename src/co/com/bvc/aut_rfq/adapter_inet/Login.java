package co.com.bvc.aut_rfq.adapter_inet;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import co.com.bvc.aut_rfq.basicfix.BasicFunctions;
import co.com.bvc.aut_rfq.db.DataAccess;
import co.com.bvc.aut_rfq.orchestrator.AutoEngine;
import co.com.bvc.aut_rfq.orchestrator.Path;
import quickfix.Application;
import quickfix.DefaultMessageFactory;
import quickfix.FileLogFactory;
import quickfix.FileStoreFactory;
import quickfix.MessageFactory;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

public class Login {

//	private SessionID sessionID1;
//	private SessionID sessionID2;
//	private SessionID sessionID3;
//	private SessionID sessionID4;
//	private SessionID sessionID5;
	
	private static Application application;
	//private static Map<String, String> mapFilesSetting = new HashMap<String, String>();
	private static Map<String, SessionID> mapSessiones = new HashMap<String, SessionID>();
	
	public Login() {
		
	}

	public Login(AdapterIO adapterIO) throws InterruptedException {
		if(application == null) {
			application = adapterIO;
		}
//		application = adapterIO;
		Properties prop = new Properties();
		InputStream is = null;
		
		try {
			is = new FileInputStream("resources//session_setting.properties");
			prop.load(is);
		} catch(IOException e) {
			System.out.println(e.toString());
		}
		
		//Por cada archivo configurado en session_setting.properties se crea una sesion
		for (Enumeration<Object> e = prop.keys(); e.hasMoreElements(); ) {
			// Obtenemos el objeto
			Object obj = e.nextElement();
			String k = obj.toString();
			String v = prop.getProperty(k);
			
			System.out.println(obj + ": " + v);
			
			try {
				SessionID sessionId = startSession(v);
				if (sessionId == null) {
					System.out.println("Conexion "+k+" errada");
				} else {
					Session.lookupSession(sessionId).logon();
					mapSessiones.put(sessionId.getSenderCompID(), sessionId);
					// BasicFunctions.logon(sessionID1);
					System.out.println("SESSION ADICIONADA. " + sessionId.getSenderCompID() + " : " + sessionId);
				}
			} catch (Exception e2) {
				System.out.println(e2.toString());
			}
		}
		
		Thread.sleep(3000);

		System.out.println("*************************************");
		System.out.println("***  SESIONES CREADAS E INICIADAS ***");
		System.out.println("*************************************");
		
	}
	
	private SessionID startSession(String fileConf) {
		SocketInitiator socketInitiator = null;
		try {
			//SessionSettings sessionSettings = new SessionSettings("resources\\" + fileConf);
			SessionSettings sessionSettings = new SessionSettings(fileConf);

			FileStoreFactory fileStoreFactory = new FileStoreFactory(sessionSettings);
			FileLogFactory fileLogFactory = new FileLogFactory(sessionSettings);
			MessageFactory messageFactory = new DefaultMessageFactory();
			socketInitiator = new SocketInitiator(application, fileStoreFactory, sessionSettings, fileLogFactory,
					messageFactory);

			// Se ejecuta onCreate de AdapterIO
			socketInitiator.start();
			SessionID sessionId = socketInitiator.getSessions().get(0);

			return sessionId;
		} catch (Exception e) {
			System.out.println("Error de conexion INET. Mensaje: " + e.getMessage());
			return null;
		}
	}
	
	public void endSessions() {

		try {
			for (Map.Entry<String, SessionID> sesion : mapSessiones.entrySet()) {
				Session.lookupSession(sesion.getValue()).logout();
			}

			System.out.println("**************************");
			System.out.println("***  SESIONES CERRADAS ***");
			System.out.println("**************************");

		} catch (Exception exp) {
			exp.printStackTrace();
		} 
//		finally {
//			System.out.println(" ");
//
//		}

	}

	public SessionID getSessionOfAfiliado(String afiliado) {
		return mapSessiones.get(afiliado);
	}
	
	public static Map<String, SessionID> getMapSessiones() {
		return mapSessiones;
	}

//	private void setMapSessiones(Map<String, SessionID> mapSessiones) {
//		Login.mapSessiones = mapSessiones;
//	}
//

//	public void initiation() {
//		
//		try {
//			sessionID1 = startSession(Path.PATH_CONFIG1_27.getCode());
//			if (sessionID1 == null) {
//				System.out.println("Conexion 1 errada");
//			} else {
//				Session.lookupSession(sessionID1).logon();
//				mapSessiones.put(sessionID1.getSenderCompID(), sessionID1);
//				// BasicFunctions.logon(sessionID1);
//				System.out.println("SESSION ADICIONADA. " + sessionID1.getSenderCompID() + " : " + sessionID1);
//			}
//
//			sessionID2 = startSession(Path.PATH_CONFIG2_35.getCode());
//			if (sessionID2 == null) {
//				System.out.println("Conexion 2 errada");
//			} else {
//				Session.lookupSession(sessionID2).logon();
////				BasicFunctions.logon(sessionID2);
//				mapSessiones.put(sessionID2.getSenderCompID(), sessionID2);
//
//				System.out.println("SESSION ADICIONADA. " + sessionID2.getSenderCompID() + " : " + sessionID2);
//			}
//
//			sessionID3 = startSession(Path.PATH_CONFIG2_37.getCode());
//			if (sessionID3 == null) {
//				System.out.println("Conexion 3 errada");
//			} else {
//				Session.lookupSession(sessionID3).logon();
//				mapSessiones.put(sessionID3.getSenderCompID(), sessionID3);
//				System.out.println("SESSION ADICIONADA. " + sessionID3.getSenderCompID() + " : " + sessionID3);
//			}
//
//			sessionID4 = startSession(Path.PATH_CONFIG1_19.getCode());
//			if (sessionID4 == null) {
//				System.out.println("Conexion 4 errada");
//			} else {
//				Session.lookupSession(sessionID4).logon();
//
//				mapSessiones.put(sessionID4.getSenderCompID(), sessionID4);
//
//				System.out.println("SESSION ADICIONADA. " + sessionID4.getSenderCompID() + " : " + sessionID4);
//			}
//
//			sessionID5 = startSession(Path.PATH_CONFIG1_20.getCode());
//			if (sessionID5 == null) {
//				System.out.println("Conexion 5 errada");
//			} else {
//				Session.lookupSession(sessionID5).logon();
//
//				mapSessiones.put(sessionID5.getSenderCompID(), sessionID5);
//
//				System.out.println("SESSION ADICIONADA. " + sessionID5.getSenderCompID() + " : " + sessionID5);
//			}
//
//			System.out.println("************************************");
//
//			Thread.sleep(3000);
//
//			System.out.println("*************************************");
//			System.out.println("***  SESIONES CREADAS E INICIADAS ***");
//			System.out.println("*************************************");
//
//		} catch (Exception exp) {
//			exp.printStackTrace();
//		} finally {
//			System.out.println(" ");
//
//		}
//
//	}
	
//	public static boolean createConn() {
//		boolean retorno = false;
//
//		//
//		BasicFunctions.conn = DataAccess.getConnection();
//		if (BasicFunctions.conn != null) {
//			retorno = true;
//		}
//
//		return retorno;
//	}

	/**
	 * Se crea el adaptador y las sessiones y el login con el motor de INET
	 * 
	 * @return
	 */
//	public static void createLogin(AdapterIO adaperIO) {
//		if (adapterIO == null) {
//			adapterIO = new AdapterIO();
//
//		}
//
//		if (BasicFunctions.login == null) {
//			BasicFunctions.login = new Login();
//			BasicFunctions.login.initiation();
//		}
//	}
//	public static void FinalLogin() {
//		
//			BasicFunctions.login = new Login();
//			BasicFunctions.login.Final();
//		
//	}
// --------------------------------

	
//	public SessionID getSessionID1() {
//		return sessionID1;
//	}
//
//	public SessionID getSessionID2() {
//		return sessionID2;
//	}
//
//	public SessionID getSessionID3() {
//		return sessionID3;
//	}
//
//	public SessionID getSessionID4() {
//		return sessionID4;
//	}
//
//	public SessionID getSessionID5() {
//		return sessionID5;
//	}

//	public String getSessionID(String Afiliado) {
//		return SessionID;
//	}

	

}
