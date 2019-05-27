package co.bvc.com.orquestador;

import java.sql.ResultSet;
import java.sql.SQLException;
import co.bvc.com.test.CreateMessage;
import co.bvc.com.test.Login;
import co.bvc.com.test.TestApplicationImpl;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.fix44.Message;

public class AdapterOut {
	
	Login inicio = new Login();
	
	public void processR(ResultSet resultSet) throws SessionNotFound, SQLException, InterruptedException {
			
		inicio.initiation();

		// Declaración de variables
        String idQuoteReqFound;			
		CreateMessage message = new CreateMessage();			
		Message mess = new Message();
		
		System.out.println("*******************************"+"\n"+ "ENVIANDO MENSAJE R"+ "\n" +" ******************************");
		//mess = message.createR(inicio.getSessionID1(), inicio.getcIdRandom(), resultSet);
		System.out.println("Mensaje MSS "+mess);
		Session.sendToTarget(mess, inicio.getSessionID1());
		Thread.sleep(8000);
		idQuoteReqFound = TestApplicationImpl.getIDQuoteFound();
		Thread.sleep(5000);
		System.out.println("*********************"+ "\n" +"EL VALOR DEL NUEVO ID ES: "+ idQuoteReqFound + "\n"  + "*********************" );
//		file.Write("R' = " + idQuoteReqFound);	
		}

}
