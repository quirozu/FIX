package co.bvc.com.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.bind.SchemaOutputResolver;

import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.orquestador.AutoEngine;
import quickfix.Application;
import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.IncorrectDataFormat;
import quickfix.IncorrectTagValue;
import quickfix.Message;
import quickfix.RejectLogon;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.UnsupportedMessageType;
import quickfix.field.ClOrdID;
import quickfix.field.OrdStatus;
import quickfix.field.Password;
import quickfix.field.PossDupFlag;
import quickfix.field.TransactTime;
import quickfix.field.Username;
import quickfix.fix44.ExecutionReport;
import quickfix.fix44.Logon;
import quickfix.fix44.MessageCracker;
import quickfix.fix44.Quote;
import quickfix.fix44.QuoteRequest;
import quickfix.fix44.QuoteResponse;
import quickfix.fix44.QuoteStatusReport;

public class Adapters extends MessageCracker implements Application {
	Login inicio = new Login();
//	Translate translate = new Translate();
	Validaciones validar = new Validaciones();
	AutoEngine autoEngine = new AutoEngine();

	private static String IDQuoteFound;
	private static String IDQuoteFound1;
	
	public Adapters(Login inicio) {
		this.inicio = inicio;
	}

	public Adapters() {
		
	}	
	
	public static String getIDQuoteFound() {
		return IDQuoteFound;
	}

	public static void setIDQuoteFound(String iDQuoteFound) {
		IDQuoteFound = iDQuoteFound;
	}

	public static String getIDQuoteFound1() {
		return IDQuoteFound1;
	}

	public static void setIDQuoteFound1(String iDQuoteFound1) {
		IDQuoteFound1 = iDQuoteFound1;
	}

	@Override
	public void onCreate(SessionID sessionId) {
		System.out.println("*****************\nonCreate - sessionId: " + sessionId);

	}

	@Override
	public void onLogon(SessionID sessionId) {
		System.out.println("*****************\nonLogon - sessionId: " + sessionId);

	}

	@Override
	public void onLogout(SessionID sessionId) {
		System.out.println("*****************\nonLogout - sessionId: " + sessionId);

	}

	@Override
	public void toAdmin(Message message, SessionID sessionId) {
		try {
			printMessage("toAdmin - ENTRADA", sessionId, message);
		} catch (FieldNotFound e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.out.println("*****************\ntoAdmin - ENTRADA");

		if (message instanceof Logon) {
			try {
				String negociador = message.getHeader().getString(50);
				System.out.println("+++++++++++++++++++++\nNEGOCIADOR: " + negociador);
				ArrayList<String> listUsers = new ArrayList<String>();
				ArrayList<String> listPass = new ArrayList<String>();
				ArrayList<String> listID = new ArrayList<String>();
				DataAccess d = new DataAccess();
				ResultSet resultSet = DataAccess.getQuery("select A.USUARIO , A.CLAVE, A.ID_USUARIO\r\n"
						+ "from bvc_automation_db.AUT_USUARIO A\r\n"
						+ "inner join bvc_automation_db.aut_negociadores B\r\n" + "on A.ID_USUARIO = B.ID_USUARIO ");
				while (resultSet.next()) {
					listUsers.add(resultSet.getString("USUARIO"));
					listPass.add(resultSet.getString("CLAVE"));
					listID.add(resultSet.getString("ID_USUARIO"));
				}
				switch (negociador) {
				case "001B27":
					System.out.println("User: " + listUsers.get(0));
					message.setField(new Username(listUsers.get(0)));
					message.setField(new Password(listPass.get(0)));
					break;
				case "002B35":
					System.out.println("User: " + listUsers.get(1));
					message.setField(new Username(listUsers.get(1)));
					message.setField(new Password(listPass.get(1)));
					break;
				case "007B26":
					System.out.println("User: " + listUsers.get(5));
					message.setField(new Username(listUsers.get(5)));
					message.setField(new Password(listPass.get(5)));
					break;
					
				case "010BWN":
					System.out.println("User: " + listUsers.get(2));
					message.setField(new Username(listUsers.get(2)));
					message.setField(new Password(listPass.get(2)));
					break;
				case "013B17":
					System.out.println("User: " + listUsers.get(3));
					message.setField(new Username(listUsers.get(3)));
					message.setField(new Password(listPass.get(3)));
					break;
				default:
					break;
				}

			} catch (FieldNotFound e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			message.setField(new PossDupFlag(true));

		}
		
		try {
			crack(message, sessionId);
		} catch (UnsupportedMessageType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FieldNotFound e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IncorrectTagValue e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		System.out.println("*****************\n toAdmin - SALIDA : \n" + message + "\nPara la sessionId: " + sessionId);
		
		

	}



//		setIDQuoteFound(message.getString(131));

		// System.out.println("*****************\n\tID QuoteRequest: " +
		// getIDQuoteFound() + "\n-----------------------");

//		printMessage("fromAdmin-Output", sessionId, message);
	

	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {

		try {
			printMessage("toApp", sessionId, message);
			 crack(message, sessionId);
			// printMessage("toApp-Output", sessionId, message);
		} catch (FieldNotFound e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	

	@Override
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {
		printMessage("fromAdmin-Input", sessionId, message);
		// System.out.println("*****************\n fromAdmin - Message : \n" + message +		// "\nPara la sessionId: "+ sessionId);

		try {
			crack(message, sessionId);
		} catch (UnsupportedMessageType e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	
	// Crear switch para el FromApp
	
    
	@Override
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		
		printMessage("fromApp-Input", sessionId, message);
		
		

		if (message instanceof QuoteRequest && sessionId.toString().equals("FIX.4.4:002/002B35->EXC")) {

			printMessage("MENSAJE R_PRIMA ", sessionId, message);
			
			try {
				 Thread.sleep(15000);
				this.inicio.initiation();
				autoEngine.setLogin(this.inicio);
				autoEngine.validarR(sessionId, message);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (SessionNotFound e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
			setIDQuoteFound(message.getString(131));
			System.out.println("ID ESTABLECIDO EN " + getIDQuoteFound());

		}

		if (message instanceof QuoteStatusReport && sessionId.toString().equals("FIX.4.4:001/001B27->EXC")) {
			String mess = "" + message;
			
			printMessage("MENSAJE AI PARA SESSION 1 ", sessionId, message);

//			validar.setCadenaAI(mess);
//			try {
//				validar.ValidaR();
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		}

		if (message instanceof QuoteStatusReport && sessionId.toString().equals("FIX.4.4:002/002B35->EXC")) {
			String mess = "" + message;

			printMessage("MENSAJE AI PARA SESSION 2 ", sessionId, message);

			
//			validar.setCadenaAI(mess);
//			try {
//				this.inicio.initiation();
//				autoEngine.setLogin(this.inicio);
//				autoEngine.validarS(sessionId, message);
//			} catch (SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			} catch (SessionNotFound e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}

		}

		if (message instanceof Quote && sessionId.toString().equals("FIX.4.4:001/001B27->EXC")) {
			
			printMessage("MENSAJE Q PARA SESSION 1 ", sessionId, message);

			try {
				this.inicio.initiation();
				autoEngine.setLogin(this.inicio);
				autoEngine.validarS(sessionId, message);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SessionNotFound e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		if (message instanceof QuoteResponse) {
			
			printMessage("MENSAJE AJ ", sessionId, message);

//			setIDQuoteFound1(message.getString(117));
//			System.out.println("ID ESTABLECIDO PARA EL MENSAJE AJ " + getIDQuoteFound1());
//			String mess = "" + message;
//
//			validar.setCadenaSPrima(mess);
//			try {
//				validar.ValidarSPrima();
//			} catch (SQLException | InterruptedException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//
//			}
		}

		crack(message, sessionId);

		//printMessage("fromApp-Output", sessionId, message);
	}
	
	

	public void onMessage(quickfix.fix44.ExecutionReport message, SessionID sessionID) throws FieldNotFound {
		if (message instanceof ExecutionReport  && sessionID.toString().equals("FIX.4.4:002/002B35->EXC")) {
			String mess = "" + message;

			validar.setCadenaOcho(mess);
			try {
//				this.inicio.initiation();
//				autoEngine.setLogin(this.inicio);
				autoEngine.validarAJ(sessionID, message);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SessionNotFound e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		if (message instanceof ExecutionReport  && sessionID.toString().equals("FIX.4.4:001/001B27->EXC")) {
			String mess = "" + message;

			validar.setCadenaOcho(mess);
			try {
//				this.inicio.initiation();
//				autoEngine.setLogin(this.inicio);
				autoEngine.validarAJ(sessionID, message);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (SessionNotFound e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		printMessage("ExecutionReport", sessionID, message);

	}

	public void onMessage(quickfix.fix44.QuoteStatusRequest message, SessionID sessionID) throws FieldNotFound {
		printMessage("QuoteStatusRequest", sessionID, message);

	}

	public void onMessage(quickfix.fix44.QuoteStatusReport message, SessionID sessionID) throws FieldNotFound {
		printMessage("QuoteStatusReport", sessionID, message);
		
			try {
				autoEngine.validarAI(sessionID, message);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		
		
		
		// System.out.println("*****************\n\tID QuoteStatusReport: " +
		// getIDQuoteFound() + "\n-----------------------");

	}

	public void onMessage(quickfix.fix44.QuoteRequest message, SessionID sessionID) throws FieldNotFound {

		printMessage("QuoteRequest", sessionID, message);
		String mess = "" + message;
		
		// instanciar AutoEngine
		// AE.validarR(msg, sId)
		
		
//		validar.setCadenaRPrima(mess);

//			try {
//				validar.ValidarRPrima();
//			} catch (InterruptedException | SQLException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
		

		// System.out.println("*****************\n\tID QuoteRequest: " +
		// getIDQuoteFound() + "\n-----------------------");

	}

	public void onMessage(quickfix.fix44.Quote message, SessionID sessionID) throws FieldNotFound {

		printMessage("Quote", sessionID, message);

	}

	public void onMessage(quickfix.fix44.QuoteCancel message, SessionID sessionID) throws FieldNotFound {

		printMessage("QuoteCancel", sessionID, message);

	}

	

	public static void printMessage(String typeMsg, SessionID sID, Message msg) throws FieldNotFound {
		System.out.println("********************\nTIPO DE MENSAJE: " + typeMsg + "- SESSION:" + sID + "\nMENSAJE :"
				+ msg + "\n----------------------------");

	}

	

}