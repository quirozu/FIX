package co.bvc.com.test;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.xml.bind.SchemaOutputResolver;

import co.bvc.com.basicfix.BasicFunctions;
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

public class AdapterIO extends MessageCracker implements Application {

	AutoEngine autoEngine = new AutoEngine();

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
				e.printStackTrace();
			} catch (SQLException e) {
				e.printStackTrace();
			}

//			message.setField(new PossDupFlag(true));

		}

		try {
			crack(message, sessionId);
		} catch (UnsupportedMessageType e) {
			e.printStackTrace();
		} catch (FieldNotFound e) {
			e.printStackTrace();
		} catch (IncorrectTagValue e) {
			e.printStackTrace();
		}

		System.out.println("*****************\n toAdmin - SALIDA : \n" + message + "\nPara la sessionId: " + sessionId);

	}

	@Override
	public void toApp(Message message, SessionID sessionId) throws DoNotSend {

		try {
			printMessage("toApp", sessionId, message);
//			crack(message, sessionId);
		} catch (FieldNotFound e1) {
			e1.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public void fromAdmin(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, RejectLogon {

		printMessage("fromAdmin-Input", sessionId, message);

		try {
			crack(message, sessionId);
		} catch (UnsupportedMessageType e) {
			e.printStackTrace();
		}
	}

	@Override
	public void fromApp(Message message, SessionID sessionId)
			throws FieldNotFound, IncorrectDataFormat, IncorrectTagValue, UnsupportedMessageType {
		
		printMessage("fromApp-Input", sessionId, message);
		
		if (message instanceof Quote ) {

			printMessage("MENSAJE S_PRIMA  ", sessionId, message);

			try {
				autoEngine.validarS(sessionId, message);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (SessionNotFound e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		
		
		if (message instanceof QuoteRequest) {

//			printMessage("MENSAJE R_PRIMA ", sessionId, message);

			String idAfiliado = sessionId.toString().substring(8, 11);
			BasicFunctions.addQuoteReqId(idAfiliado, message.getString(131));

			System.out.println("\nID ESTABLECIDO EN " + BasicFunctions.getQuoteReqIdOfAfiliado(idAfiliado));

			try {
				Thread.sleep(5000);
				autoEngine.validarR(sessionId, message);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (SessionNotFound e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		if (message instanceof QuoteStatusReport) {

			printMessage("MENSAJE AI ", sessionId, message);
			try {
				autoEngine.validarAI(sessionId, message);
			} catch (SQLException | InterruptedException e) {
				e.printStackTrace();
			} catch (SessionNotFound e) {
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

		crack(message, sessionId);
	}

	public void onMessage(ExecutionReport message, SessionID sessionID) throws FieldNotFound {

			printMessage("MENSAJE ER", sessionID, message);

			try {
				autoEngine.validarAJ(sessionID, message);
			} catch (SQLException e) {
				e.printStackTrace();
			} catch (InterruptedException e) {
				e.printStackTrace();
			} catch (SessionNotFound e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		
	
	}

	public void onMessage(quickfix.fix44.QuoteStatusRequest message, SessionID sessionID) throws FieldNotFound {
		printMessage("QuoteStatusRequest", sessionID, message);

	}

	public void onMessage(quickfix.fix44.QuoteStatusReport message, SessionID sessionID) throws FieldNotFound {

	}

	public void onMessage(quickfix.fix44.QuoteRequest message, SessionID sessionID) throws FieldNotFound {
		
		
	}

	public void onMessage(quickfix.fix44.Quote message, SessionID sessionID) throws FieldNotFound {
		
	}

	public void onMessage(quickfix.fix44.QuoteCancel message, SessionID sessionID) throws FieldNotFound {

		printMessage("QuoteCancel", sessionID, message);
	}

	public static void printMessage(String typeMsg, SessionID sID, Message msg) throws FieldNotFound {
		System.out.println("********************\nTIPO DE MENSAJE: " + typeMsg + "- SESSION:" + sID + "\nMENSAJE :"
				+ msg + "\n----------------------------");

	}
}