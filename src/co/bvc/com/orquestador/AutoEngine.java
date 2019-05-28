package co.bvc.com.orquestador;

import java.sql.ResultSet;
import java.sql.SQLException;

import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.test.Adapters;
import co.bvc.com.test.CreateMessage;
import co.bvc.com.test.Login;
import co.bvc.com.test.Validaciones;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.fix44.Message;

public class AutoEngine {

	   private DataAccess connectionBD = null;
	   private int nextId;
	   private String Date;
	   public Login login= null;
	   Message message = new Message();
	   CreateMessage createMesage = new CreateMessage();
	   Validaciones validaciones = new Validaciones();
	   
	   //connectionBD = new DataAccess();

	   public AutoEngine(DataAccess connectionBD, Login login, String date) throws SQLException {
		   if (connectionBD == null) {
			   this.connectionBD.Conexion();
		   }
		   
		   this.login = login; 
		   this.Date = date;
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
			
//			System.out.println("RS "+ rs);
			
			rs.first();
			
			ejecutar(rs);
		}
	   
//	   public void continuarEjecucion(Int idCaseSeq) {
//		   String queryReg = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos WHERE ID_CASESEQ = " + idCaseSeq ;
//		   ResultSet rs = DataAccess.getQuery(queryReg);
//		   
//		   ejecutar(rs); 
//	   }
	   
	   public void ejecutar(ResultSet resultSet) throws SQLException, SessionNotFound, InterruptedException {
		   		   
//		   resultSet.first();
		   
		   String msgType = "";
		   String afiliado = "";
		   int escenario = 0;

			   msgType = resultSet.getString("ID_ESCENARIO");
			   afiliado = resultSet.getString("ID_AFILIADO");
			   escenario = resultSet.getInt("ID_CASESEQ");

		   System.out.println("MSGTYPE: " +  msgType + "\nAFILIADO: " +afiliado + "\nESCENARIO: " + escenario);
		   
		   enviarMensaje(msgType, resultSet, escenario);

	   }
	   
	   public void enviarMensaje(String msgType, ResultSet resultSet, int escenario) throws SessionNotFound, SQLException, InterruptedException {
		   
		  String idQuoteReqFound;
		   
		  switch (msgType) {
		  
		  case "FIX_R":
			
			   System.out.println(resultSet);
			   message = createMesage.createR(escenario, resultSet);
			   Session.sendToTarget(message, login.getSessionID1());
			   
//			   String queryPersistencia = "INSERT INTO aut_fix_rfq_cache values (" + login.getSessionID1() +", "
//			   		+ "SELECT * FROM aut_fix_rfq_datos WHERE ID_CASESEQ = "+ escenario;
			   
//			   DataAccess.getQuery(queryPersistencia);
			   
			 
			break;
			
        case "FIX_S":
        	
        	
        	
			
			break;
			
        case "FIX_AJ":
	
	        break;
         case "FIX_Z":
	
	        break;

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
	   
	   //Metodo que extraer el registro en base de datos  
	   public ResultSet obtenerCache(Session session) {
		return null;
		 
	   }
	   
	   
	   //Metodos get y set del Conexión base de datos
	   public DataAccess getConnectionBD() {
		   
		   return connectionBD;
	   }

	   public void setConnectionBD(DataAccess connectionBD) {
	     	this.connectionBD = connectionBD;
	  }

	   public void validarR(Session session, Message message) throws SQLException, InterruptedException {
		   
		   //getcache
		   
//		   obtenerCache(session);
//		   validaciones.ValidarRPrima(resultSetCache, message);
		   
		   //obtenerSiguienteRegistro()
		   
		   //obtenerIdQuote
		   // generarS
		   
		   
	   }
	   
	   public void validarAI() {
		   
	   }
	   
}
