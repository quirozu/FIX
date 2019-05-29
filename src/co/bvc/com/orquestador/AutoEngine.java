package co.bvc.com.orquestador;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.dao.domain.AutFixRfqDatosCache;
import co.bvc.com.dao.domain.RespuestaConstrucccionMsgFIX;
import co.bvc.com.test.Adapters;
import co.bvc.com.test.CreateMessage;
import co.bvc.com.test.Login;
import co.bvc.com.test.Validaciones;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.fix44.QuoteRequest;

public class AutoEngine {

	   private DataAccess connectionBD = null;
	   private int nextId;
	   private String Id_Ejecucion;
	   public Login login= null;
	   Message message = new Message();
	   CreateMessage createMesage = new CreateMessage();
	   Validaciones validaciones = new Validaciones();
	   
	  
	   
	   //connectionBD = new DataAccess();

	   public AutoEngine(DataAccess connectionBD, Login login, String ID_Ejecucion) throws SQLException {
		   if (connectionBD == null) {
			   this.connectionBD.Conexion();
		   }
		   
		   this.login = login; 
		   this.Id_Ejecucion = ID_Ejecucion;
	   }
	   
	   public AutoEngine() {
		   
	   }
	   
	   // metodo que inicia la ejecucion
	   public void iniciarEjecucion() throws SQLException, SessionNotFound, InterruptedException {
		    
		   SimpleDateFormat SDF = new SimpleDateFormat("yyyMMddHmmss");
			Date dt_1 = new Date();
			long ID_EJECUCION = Long.parseLong(SDF.format(dt_1));
			System.out.println("La fecha actual es : "+ ID_EJECUCION);
		   
		   int primerId = 0;
			
			String queryInicio = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos ORDER BY ID_CASESEQ ASC LIMIT 1";
					
			ResultSet rs = DataAccess.getQuery(queryInicio);
//			System.out.println("RS "+ rs);
			
			while(rs.next()) {
				primerId = rs.getInt("ID_CASESEQ");
				
			}
			
//			System.out.println("RS "+ rs);
			
			rs.first();
			
			ejecutar(rs, ID_EJECUCION);
		}
	   
//	   public void continuarEjecucion(Int idCaseSeq) {
//		   String queryReg = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos WHERE ID_CASESEQ = " + idCaseSeq ;
//		   ResultSet rs = DataAccess.getQuery(queryReg);
//		   
//		   ejecutar(rs); 
//	   }
	   
	   public void ejecutar(ResultSet resultSet, long idEjecucion) throws SQLException, SessionNotFound, InterruptedException {
		   		   
//		   resultSet.first();
		   
		   String msgType = "";
		   String afiliado = "";
		   int escenario = 0;

			   msgType = resultSet.getString("ID_ESCENARIO");
			   afiliado = resultSet.getString("ID_AFILIADO");
			   escenario = resultSet.getInt("ID_CASESEQ");

		   System.out.println("MSGTYPE: " +  msgType + "\nAFILIADO: " +afiliado + "\nESCENARIO: " + escenario);
		   
		   enviarMensaje(msgType, resultSet, escenario, idEjecucion);

	   }
	   
	   public void enviarMensaje(String msgType, ResultSet resultSet, int escenario, long Idejecucion) throws SessionNotFound, SQLException, InterruptedException {
		   
		  String idQuoteReqFound;
		  
		  RespuestaConstrucccionMsgFIX respConstruccion = new RespuestaConstrucccionMsgFIX();
		  
		  
		  switch (msgType) {
		  
		  case "FIX_R":
			
			   System.out.println(resultSet);
			   respConstruccion = createMesage.createR(escenario, resultSet);
			   
			   for (String session: respConstruccion.getListSessiones()) {
				   
				
				   // Construir mensaje a cache.
				   AutFixRfqDatosCache datosCache = new AutFixRfqDatosCache();
				   
				   datosCache.setReceiverSession(session);
				   datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
				   datosCache.setIdCase(resultSet.getInt("ID_CASE"));
				   datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
				   datosCache.setEstado(resultSet.getString("ESTADO"));
				   //datosCache.setFixQuoteReqId(resultSet.getString("FIX_QUOTE_REQ_ID"));
				   datosCache.setIdAfiliado(resultSet.getString("ID_AFILIADO"));
				   datosCache.setIdEjecucion(Idejecucion);
				   		   
				   cargarCache(datosCache);
			   }
			   
			   String idIAfiliado = resultSet.getString("ID_AFILIADO");
			   
			// Construir mensaje a cache de la propia session.
			   AutFixRfqDatosCache datosCache = new AutFixRfqDatosCache();			   
			   datosCache.setReceiverSession(idIAfiliado);
			   datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
			   datosCache.setIdCase(resultSet.getInt("ID_CASE"));
			   datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
			   datosCache.setEstado(resultSet.getString("ESTADO"));
			   //datosCache.setFixQuoteReqId(resultSet.getString("FIX_QUOTE_REQ_ID"));
			   datosCache.setIdAfiliado(idIAfiliado);
			   datosCache.setIdEjecucion(Idejecucion);
			   
			   cargarCache(datosCache);
			   
			   Session.sendToTarget(respConstruccion.getMessage(), login.getSessionID1());
			   
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

	   
	   //Metodo que elimina el registro en cache (base de datos)  
	   public void eliminarDatoCache(String session) throws SQLException {
		
		   String queryDelete = "DELETE FROM bvc_automation_db.aut_fix_rfq_cache WHERE RECEIVER_SESSION = "+session;
		   DataAccess.setQuery(queryDelete);		 
	   }	   
	   
	   
	   //Metodo que extraer el registro en base de datos  
	   public AutFixRfqDatosCache obtenerCache(String session) throws SQLException {
		
		System.out.println("SESS: " + session);
		return DataAccess.obtenerCache(session);
		 
	   }
	   
	   
	   //Metodos get y set del Conexi�n base de datos
	   public DataAccess getConnectionBD() {
		   
		   return connectionBD;
	   }

	   public void setConnectionBD(DataAccess connectionBD) {
	     	this.connectionBD = connectionBD;
	  }

	   public void validarR(SessionID sessionId, quickfix.Message messageIn) throws SQLException, InterruptedException, FieldNotFound {
		   
		   // Obtener el ID_AFILIADO de la session.
			String sIdAfiliado = sessionId.toString().substring(8,11);
		   //getcache
		   AutFixRfqDatosCache datosCache = obtenerCache(sIdAfiliado);
		   validaciones.ValidarRPrima(datosCache, messageIn);
		   

		   //Eliminar Registro en Cache.
		   eliminarDatoCache(sIdAfiliado);
		   
		   //obtenerSiguienteRegistro()
		   
		   //obtenerIdQuote
		   // generarS
		   
		   
	   }
	   
	   public void validarAI() {
		   
	   }
	   
	   
	   /**
	    *  Metodo para recuperar el objeto siguiente de la tabla de datos.
	    * @param idCaseSeqActual
	    * @return
	 * @throws SQLException 
	    */
	   
	   public void ejecutarSiguientePaso(int idCaseSeqActual) throws SQLException {
		   
		   ResultSet resultSet = null;
		   String _query = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos ORDER BY ID_CASESEQ ="+ idCaseSeqActual++;
		   
		   resultSet = DataAccess.getQuery(_query);
		   
		   if (resultSet != null) {
			   
			   // seleccionar el siguiente paso a ejecutar.
			   System.out.println("Continua con el siguiente paso.");
			   
		   }else {
			   //ejecutar reporte.
			   System.out.println("Generar reporte....");
			   //Termina ejecucion.
			   System.out.println("FIN EJECUCION....");
		   }
		   
		   
		
	   }
	   
	   
		public static void printMessage(String typeMsg, SessionID sessionId, quickfix.Message message2) throws FieldNotFound {
			System.out.println("********************\nTIPO DE MENSAJE: " + typeMsg + "- SESSION:" + sessionId + "\nMENSAJE :"
					+ message2 + "\n----------------------------");

		}
	   
}
