package co.bvc.com.orquestador;

import java.sql.ResultSet;
import java.sql.SQLException;

import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.dao.domain.AutFixRfqDatosCache;
import co.bvc.com.test.Adapters;
import co.bvc.com.test.CreateMessage;
import co.bvc.com.test.Login;
import co.bvc.com.test.Validaciones;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.fix44.Message;
import quickfix.fix44.QuoteRequest;

public class AutoEngine {

	   private DataAccess connectionBD = null;
	   private int nextId;
	   public Login login= null;
	   Message message = new Message();
	   CreateMessage createMesage = new CreateMessage();
	   Validaciones validaciones = new Validaciones();
	  
	   
	   //connectionBD = new DataAccess();

	   public AutoEngine(DataAccess connectionBD, Login login) throws SQLException {
		   if (connectionBD == null) {
			   this.connectionBD.Conexion();
		   }
		   
		   this.login = login; 
		   
	   }
	   
	   public AutoEngine() {
		   
	   }
	   
	   // metodo que inicia la ejecucion
	   public void iniciarEjecucion() throws SQLException, SessionNotFound, InterruptedException {
		    int primerId = 0;
			
			String queryInicio = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos ORDER BY ID_CASESEQ ASC LIMIT 1";
					
			ResultSet rs = DataAccess.getQuery(queryInicio);
//			System.out.println("RS "+ rs);
			
			while(rs.next()) {
				primerId = rs.getInt("ID_CASESEQ");
				
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
			   
			   // Construir mensaje a cache.
			   AutFixRfqDatosCache datosCache = new AutFixRfqDatosCache();
			   datosCache.setReceiverSession("002");
			   datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
			   datosCache.setIdCase(resultSet.getInt("ID_CASE"));
			   datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
			   datosCache.setEstado(resultSet.getString("ESTADO"));
			   //datosCache.setFixQuoteReqId(resultSet.getString("FIX_QUOTE_REQ_ID"));
			   datosCache.setIdAfiliado(resultSet.getString("ID_AFILIADO"));
			   
			   
			   cargarCache(datosCache);
			   Session.sendToTarget(message, login.getSessionID1());
			   
			   //idQuoteReqFound = Adapters.getIDQuoteFound();
			   //Thread.sleep(5000);
			   //System.out.println("*********************EL VALOR DEL NUEVO ID ES: "+ idQuoteReqFound + "\n"  + "*********************" );
			   
//			   String queryPersistencia = "INSERT INTO aut_fix_rfq_cache values (" + login.getSessionID1() +", "
//			   		+ "SELECT * FROM aut_fix_rfq_datos WHERE ID_CASESEQ = "+ escenario;
			   
//			   DataAccess.getQuery(queryPersistencia);
			   
			   
			   
			 
			break;
			
        case "FIX_S":
        	
//        	 message = createMesage.createS(escenario, resultSet, idQuote);
//			 cargarCache(resultSet, "002");
//			 Session.sendToTarget(message, login.getSessionID1());
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
	   public void cargarCache(AutFixRfqDatosCache datosCache) throws SQLException {
		
		   DataAccess.cargarCache(datosCache);
		 
	   }
	   
	   //Metodo que extraer el registro en base de datos  
	   public AutFixRfqDatosCache obtenerCache(SessionID session) throws SQLException {
		
		String sIdAfiliado = session.toString().substring(8,11);
		System.out.println("SESS: " + sIdAfiliado);
		return DataAccess.obtenerCache(sIdAfiliado);
		 
	   }
	   
	   
	   //Metodos get y set del Conexión base de datos
	   public DataAccess getConnectionBD() {
		   
		   return connectionBD;
	   }

	   public void setConnectionBD(DataAccess connectionBD) {
	     	this.connectionBD = connectionBD;
	  }

	   public void validarR(SessionID sessionId, quickfix.Message message2) throws SQLException, InterruptedException, FieldNotFound {
		   
		   //getcache
		   AutFixRfqDatosCache datosCache = obtenerCache(sessionId);
		   validaciones.ValidarRPrima(datosCache, (QuoteRequest) message);
		   
		   //obtenerSiguienteRegistro()
		   
		   //obtenerIdQuote
		   // generarS
		   
		   
	   }
	   
	   public void validarAI() {
		   
	   }
	   
		public static void printMessage(String typeMsg, SessionID sessionId, quickfix.Message message2) throws FieldNotFound {
			System.out.println("********************\nTIPO DE MENSAJE: " + typeMsg + "- SESSION:" + sessionId + "\nMENSAJE :"
					+ message2 + "\n----------------------------");

		}
	   
}
