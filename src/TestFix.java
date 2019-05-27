import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.basicfix.ParametersRead;
import co.bvc.com.orquestador.AutoEngine;
import co.bvc.com.test.Login;
import co.bvc.com.test.CreateMessage;
import co.bvc.com.test.Adapters;
import co.bvc.com.test.Translate;
import co.bvc.com.test.Validaciones;
import quickfix.ConfigError;
import quickfix.DataDictionary;
import quickfix.DoNotSend;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionNotFound;
import quickfix.fix44.Message;

public class TestFix {

	public static DataAccess bd = new DataAccess();
	static Validaciones validar = new Validaciones();
	static Adapters testAplication = new Adapters();
	static ResultSet resultSet1;
	private static String idFound;
	
	

	public static String getIdFound() {
		return idFound;
	}

	public static void setIdFound(String idFound) {
		TestFix.idFound = idFound;
	}
	
	

	public static void main(String[] args) throws SessionNotFound, InterruptedException, DoNotSend, SQLException, ConfigError, FieldNotFound {
		bd.Conexion();
		Login inicio = new Login();

		inicio.initiation();
		
		Adapters adapters = new Adapters();
		AutoEngine autoEngine = new AutoEngine(bd);
		
		autoEngine.iniciarEjecucion();
	
		
//		String idQuoteReqFound;
//		String idQuoteReqFound1;
//		CreateMessage message = new CreateMessage();
		
//		Thread.sleep(3000);
//		Message mess = new Message();
		
//		mess = message.createR(inicio.getSessionID1(), inicio.getcIdRandom());
//		System.out.println("******************************\nMENSAJE R...\n");
//		Session.sendToTarget(mess, inicio.getSessionID1());
//		
//		Thread.sleep(5000);
//		idQuoteReqFound = Adapters.getIDQuoteFound();
//		System.out.println("EL VALOR DEL NUEVO ID ES: " + idQuoteReqFound);
//		System.out.println("******************************\n");
//		System.out.println("ESPERANDO CREACIÓN DEL MENSAJE S...\n");
//		Thread.sleep(3000);
//
//		mess = message.createS(inicio.getSessionID2(), inicio.getcIdRandom(), idQuoteReqFound);
//
//		System.out.println("******************************\nMENSAJE S...\n");
//
//		Session.sendToTarget(mess, inicio.getSessionID2());
//
//		Thread.sleep(5000);
//		idQuoteReqFound1 = Adapters.getIDQuoteFound1();
//		System.out.println("EL VALOR DEL NUEVO ID PARA EL AJ ES : " + idQuoteReqFound1);
//		System.out.println("******************************\n");
//		System.out.println("ESPERANDO CREACIÓN DEL MENSAJE AJ...\n");
//		Thread.sleep(3000);
//
//		mess = message.createAJ(inicio.getSessionID1(), inicio.getcIdRandom(), idQuoteReqFound1);
//		Thread.sleep(3000);
//		System.out.println("******************************\nFINAL DE EJECUCION...");
		

	}
}
