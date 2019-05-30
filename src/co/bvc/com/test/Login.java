package co.bvc.com.test;

import co.bvc.com.basicfix.BasicFunctions;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DefaultMessageFactory;
import quickfix.MemoryStoreFactory;
import quickfix.ScreenLogFactory;
import quickfix.SessionID;
import quickfix.SessionSettings;
import quickfix.SocketInitiator;

public class Login {

	private SessionID sessionID1;
	private static SessionID sessionID2;
	private SessionID sessionID3;
	private SessionID sessionID4;
	private SessionID sessionID5;
	
	public void initiation() {

		try {
			sessionID1 = BasicFunctions.connecto("sessionSettings1_27.cfg");
			if (sessionID1 == null) {
				System.out.println("Conexión 1 errada");
			} else {
				BasicFunctions.logon(sessionID1);
				System.out.println("Session ID: " + sessionID1);
			}

			sessionID2 = BasicFunctions.connecto("sessionSettings2_35.cfg");
			if (sessionID2 == null) {
				System.out.println("Conexión 2 errada");
			} else {
				BasicFunctions.logon(sessionID2);
				System.out.println("Session ID: " + sessionID2);
			}

			sessionID3 = BasicFunctions.connecto("sessionSettings2_37.cfg");
			if (sessionID3 == null) {
				System.out.println("Conexión 3 errada");
			} else {
				BasicFunctions.logon(sessionID3);
				System.out.println("Session ID: " + sessionID3);
			}

			sessionID4 = BasicFunctions.connecto("sessionSettings1_19.cfg");
			if (sessionID4 == null) {
				System.out.println("Conexión 4 errada");
			} else {
				BasicFunctions.logon(sessionID4);
				System.out.println("Session ID: " + sessionID4);
				BasicFunctions.logout(sessionID4);
				System.out.println("Deslogueado");
			}
			
			sessionID5 = BasicFunctions.connecto("sessionSettings1_20.cfg");
			if (sessionID5 == null) {
				System.out.println("Conexión 4 errada");
			} else {
				BasicFunctions.logon(sessionID4);
				System.out.println("Session ID: " + sessionID5);
			}

			System.out.println("************************************");

			Thread.sleep(3000);

			// Ejecución

			System.out.println("***********************************");
			System.out.println("Procesando....");
			Thread.sleep(3000);
			System.out.println("***********************************");

		} catch (Exception exp) {
			exp.printStackTrace();
		} finally {
			System.out.println(" ");

		}

	}

	public SessionID getSessionID1() {
		return sessionID1;
	}

	public static SessionID getSessionID2() {
		return sessionID2;
	}

	public SessionID getSessionID3() {
		return sessionID3;
	}

	

}
