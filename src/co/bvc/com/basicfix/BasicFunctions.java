package co.bvc.com.basicfix;

import co.bvc.com.test.TestApplicationImpl;
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
	
	public static boolean logon(SessionID sessionID) {
		boolean sLogon = false;
		
		Session.lookupSession(sessionID).logon();
		
		return sLogon;
	}
	
	public static SessionID connecto(String fileConf) {
		SocketInitiator socketInitiator = null;
		try {
			SessionSettings sessionSettings = new SessionSettings("C:\\eclipse_projects\\quickfixbvc1\\resources\\" + fileConf);
			
			Application application = new TestApplicationImpl();
			FileStoreFactory fileStoreFactory = new FileStoreFactory(sessionSettings);
			FileLogFactory fileLogFactory = new FileLogFactory(sessionSettings);
			MessageFactory messageFactory = new DefaultMessageFactory();
			socketInitiator = new SocketInitiator(application, fileStoreFactory, sessionSettings, fileLogFactory, messageFactory);
			socketInitiator.start(); // Llama onCreate
			SessionID sessionID = socketInitiator.getSessions().get(0);
			//System.out.println("ANTES DE LOGON \n"+socketInitiator.getSettings());
			return sessionID;
		} catch (Exception e) {
			System.out.println("Error de conexión."+e.getMessage());
			return null;
		} 	
	}
}
