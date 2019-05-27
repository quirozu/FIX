//package co.bvc.com.orquestador;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//
//import co.bvc.com.orquestador.files.File;
//import co.bvc.com.test.Login;
//import co.bvc.com.test.SendMessage;
//import co.bvc.com.test.TestApplicationImpl;
//import quickfix.FieldNotFound;
//import quickfix.Session;
//import quickfix.SessionNotFound;
//import quickfix.fix44.Message;
//
//public class AutoEngine {
//	
//	SendMessage sendMessage;
//	Login inicio = new Login();
//	Message mess = new Message();
//    SendMessage message = new SendMessage();
//    
//	
//	public void enviarMensajes(ArrayList<ResultSet> datosIn) throws SQLException, FieldNotFound, SessionNotFound, InterruptedException {
//	
//		inicio.initiation();
//	    
//	    for(int i=0; i<=datosIn.size(); i++) 
//	    {
//          while(datosIn.get(i).next()) 
//          {     
//        	  String typeMessage = datosIn.get(i).getString("RQ_MSGTYPE");
////          	  System.out.println(datosIn.get(i).getString("RQ_MSGTYPE"));
//          	  switch(typeMessage) {
//          	  
//          	  case "R":
//          		  SendR(datosIn.get(i));
//          		  break;
//          	  
//          	  }
//          
//          	  
//          }           
//		}
//	
//	 }
//	 
//	
//	
//    public void SendR(ResultSet rSet) throws SessionNotFound, SQLException, InterruptedException {
//    	
//    	inicio.initiation();
//    	String idQuoteReqFound;
//    	
//    	mess = message.sendR(inicio.getSessionID1(), inicio.getcIdRandom(), rSet);
//    	Session.sendToTarget(mess, inicio.getSessionID1());
//    	Thread.sleep(5000);
//    	idQuoteReqFound = TestApplicationImpl.getIDQuoteFound();
//    	Thread.sleep(5000);
//		System.out.println("*********************"+ "\n" +"EL VALOR DEL NUEVO ID ES: "+ idQuoteReqFound + "\n"  + "*********************" );
//    	File.Write(idQuoteReqFound);
//   	   	
//    }
//    
//    public void SendS(ResultSet rSet) {
//    	inicio.initiation();
//    	
////    	sendMessage.sendS(inicio.getSessionID2(), inicio.getcIdRandom(), strQReqId, resultset)
//    	
//    }
//
//
//	}
//
//    
//	
//	
