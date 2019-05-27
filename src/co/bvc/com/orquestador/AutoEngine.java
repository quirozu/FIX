package co.bvc.com.orquestador;

import java.sql.ResultSet;
import java.sql.SQLException;

import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.test.Adapters;
import co.bvc.com.test.CreateMessage;
import co.bvc.com.test.Login;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.fix44.Message;

public class AutoEngine {

	   private DataAccess connectionBD = null;
	   private int nextId;
	   Login inicio = new Login();
	   Message message = new Message();
	   
	   
	   
	   CreateMessage createMesage = new CreateMessage();
	   
	   //connectionBD = new DataAccess();

	   public AutoEngine(DataAccess connectionBD) throws SQLException {
		   if (connectionBD == null) {
			   this.connectionBD.Conexion();
		   }
		   
		   
	   }
	   
	   // metodo que inicia la ejecucion
	   public void iniciarEjecucion() throws SQLException, SessionNotFound, InterruptedException {
		    int primerId = 0;
			
			String queryInicio = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos ORDER BY ID_CASESEQ ASC LIMIT 1";
					
			ResultSet rs = DataAccess.getQuery(queryInicio);
			System.out.println("RS "+ rs);
			
			while(rs.next()) {
				primerId = rs.getInt("ID_CASESEQ");
				System.out.println("*********SALIDA*********"+primerId);
				
			}
			
			System.out.println("RS "+ rs);
			
			//rs.first();
			
			ejecutar(rs);
		}
	   
//	   public void continuarEjecucion(Int idCaseSeq) {
//		   String queryReg = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos WHERE ID_CASESEQ = " + idCaseSeq ;
//		   ResultSet rs = DataAccess.getQuery(queryReg);
//		   
//		   ejecutar(rs); 
//	   }
	   
	   public void ejecutar(ResultSet resultSet) throws SQLException, SessionNotFound, InterruptedException {
		   
//		   String queryReg = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos WHERE ID_CASESEQ = " + primerId() ;
		   
//		   ResultSet rs = DataAccess.getQuery(queryReg);
		   System.out.println("***********RS**********" + resultSet);
		   
		   resultSet.first();
		   
		   String msgType = "";
		   String afiliado = "";
		   int escenario = 0;
//		   Session session = null;
		   
//		   while(resultSet.next()) {
			   
			   
			   msgType = resultSet.getString("ID_ESCENARIO");
			   afiliado = resultSet.getString("ID_AFILIADO");
			   escenario = resultSet.getInt("ID_CASESEQ");
			   
//		   }
		   
		   System.out.println("MSGTYPE: " +  msgType + "\nAFILIADO: " +afiliado + "\nESCENARIO: " + escenario);
		   
		  // session = getSession(afiliado);
		   
//		   session = "FIX.4.4:001/001B27->EXC";
		   String idQuoteReqFound;	
		   switch(msgType) {
		   case "FIX_R" : 
			   
			   message = createMesage.createR(escenario);
			   System.out.println(message);
			   
			   Session.sendToTarget(message, inicio.getSessionID1());
			   
			   Thread.sleep(5000);
				idQuoteReqFound = Adapters.getIDQuoteFound();
				Thread.sleep(5000);
				System.out.println("*********************"+ "\n" +"EL VALOR DEL NUEVO ID ES: "+ idQuoteReqFound + "\n"  + "*********************" );
			   break;
			   
			   			   
//		   case "FIX_S" : 
//			   mess = createMesage.createS(escenario);
//			   System.out.println(mess);
//			   Session.sendToTarget(mess);
//			   break;
//			   
//			  
//			case "FIX_AJ" : 
//				   mess = createMesage.createAJ(escenario);
//				   System.out.println(mess);
//				   Session.sendToTarget(mess);
//				   break;
//				   
//				 
//			case "FIX_Z" : 
//				   mess = createMesage.createZ(escenario);
//				   System.out.println(mess);
//				   Session.sendToTarget(mess);
//				   break;
			   
	   default:
		   break;

		   }

	   }
	   
	   
	   //Metodo que genera el mensaje R apartir del objeto recibido de BD  
	   public String generarMensajeR() {
		   
		return null;
		
	   }
	   
	   //Metodo que guarda el registro en base de datos  
	   public String cargarCache() {
		return null;
		 
	   }
	   
	   
	   //Metodos get y set del Conexión base de datos
	   public DataAccess getConnectionBD() {
		   
		   return connectionBD;
	   }

	   public void setConnectionBD(DataAccess connectionBD) {
	     	this.connectionBD = connectionBD;
	  }

	
	   
	   
	   
}
