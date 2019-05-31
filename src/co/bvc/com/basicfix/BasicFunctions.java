package co.bvc.com.basicfix;

import co.bvc.com.test.Adapters;
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
	private static int IdCaseSeg;
	
	public static int getIdCaseSeg() {
		return IdCaseSeg;
	}

	public static void setIdCaseSeg(int idCaseSeg) {
		IdCaseSeg = idCaseSeg;
	}

	public static boolean logon(SessionID sessionID) {
		boolean sLogon = false;
		
		Session.lookupSession(sessionID).logon();
		
		return sLogon;
	}
	
	public static boolean logout(SessionID sessionID) {
		boolean sLogon = false;
		
		Session.lookupSession(sessionID).logout();
		
		return sLogon;
	}
	
	
	public static SessionID connecto(String fileConf) {
		SocketInitiator socketInitiator = null;
		try {
			SessionSettings sessionSettings = new SessionSettings("resources\\" + fileConf);
			
			Application application = new Adapters();
			FileStoreFactory fileStoreFactory = new FileStoreFactory(sessionSettings);
			FileLogFactory fileLogFactory = new FileLogFactory(sessionSettings);
			MessageFactory messageFactory = new DefaultMessageFactory();
			socketInitiator = new SocketInitiator(application, fileStoreFactory, sessionSettings, fileLogFactory, messageFactory);
			socketInitiator.start(); // Llama onCreate
			SessionID sessionID = socketInitiator.getSessions().get(0);
			//System.out.println("ANTES DE LOGON \n"+socketInitiator.getSettings());
			return sessionID;
		} catch (Exception e) {
			System.out.println("Error de conexi�n."+e.getMessage());
			return null;
		} 	
	}
}
