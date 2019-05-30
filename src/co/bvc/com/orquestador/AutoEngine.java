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
	   private Login login= null;
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
			
			enviarMensaje(rs, ID_EJECUCION, null);
		}
	   
//	   public void continuarEjecucion(Int idCaseSeq) {
//		   String queryReg = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos WHERE ID_CASESEQ = " + idCaseSeq ;
//		   ResultSet rs = DataAccess.getQuery(queryReg);
//		   
//		   ejecutar(rs); 
//	   }
	   
//	   public void ejecutar(ResultSet resultSet, long idEjecucion) throws SQLException, SessionNotFound, InterruptedException {
//		   		   
////		   resultSet.first();
//		
//		   String afiliado = resultSet.getString("ID_AFILIADO");			   
//
//		   System.out.println("\nAFILIADO: " +afiliado);
//		   
//		   enviarMensaje( resultSet, idEjecucion);
//
//	   }
	   
	   public void enviarMensaje( ResultSet resultSet, long Idejecucion, String quoteReqId) throws SessionNotFound, SQLException, InterruptedException {
		   
		  String msgType;
		  AutFixRfqDatosCache datosCache = new AutFixRfqDatosCache();
		  int idCaseseq = 0;
		  idCaseseq = resultSet.getInt("ID_CASESEQ");
		  
		  msgType = resultSet.getString("ID_ESCENARIO");
		  
		  RespuestaConstrucccionMsgFIX respConstruccion = new RespuestaConstrucccionMsgFIX();
		  
		  
		  switch (msgType) {
		  
		  case "FIX_R":
			
			   System.out.println(resultSet);
			   respConstruccion = createMesage.createR(idCaseseq, resultSet);
			   
			   for (String session: respConstruccion.getListSessiones()) {
				   
				
				   // Construir mensaje a cache.
				   
				   
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
        	
        	 respConstruccion = createMesage.createS(idCaseseq, resultSet, quoteReqId);
			   
        	 System.out.println("************* INGRESA A FIX_S ****************");
			   for (String session: respConstruccion.getListSessiones()) {
				   
				   // Construir mensaje a cache.

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
			   
			   idIAfiliado = resultSet.getString("ID_AFILIADO");
			   
			// Construir mensaje a cache de la propia session.
			   		   
			   datosCache.setReceiverSession(idIAfiliado);
			   datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
			   datosCache.setIdCase(resultSet.getInt("ID_CASE"));
			   datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
			   datosCache.setEstado(resultSet.getString("ESTADO"));
			   //datosCache.setFixQuoteReqId(resultSet.getString("FIX_QUOTE_REQ_ID"));
			   datosCache.setIdAfiliado(idIAfiliado);
			   datosCache.setIdEjecucion(Idejecucion);
			   
			   cargarCache(datosCache);
			   
			   Session.sendToTarget(respConstruccion.getMessage(), login.getSessionID2());
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
	   
	   
	   //Metodo que guarda el registro en base de datos  
	   public void cargarCache(AutFixRfqDatosCache datosCache) throws SQLException {

		   DataAccess.cargarCache(datosCache);
		 
	   }

	   
	   //Metodo que elimina el registro en cache (base de datos)  
	   public void eliminarDatoCache(String session) throws SQLException, InterruptedException {
		
		   System.out.println("******************SESSION ***************"+session);
		   Thread.sleep(15000);
		   String queryDelete = "DELETE FROM bvc_automation_db.aut_fix_rfq_cache WHERE RECEIVER_SESSION = "+session;
		   DataAccess.setQuery(queryDelete);		 
	   }	   
	   
	   
	   //Metodo que extraer el registro en base de datos  
	   public AutFixRfqDatosCache obtenerCache(String session) throws SQLException {
		
		System.out.println("SESS: " + session);
		return DataAccess.obtenerCache(session);
		 
	   }
	   
	   public void validarR(SessionID sessionId, quickfix.Message messageIn) throws SQLException, InterruptedException, FieldNotFound, SessionNotFound {
		   
		   // Obtener el ID_AFILIADO de la session.
			String IdContraFirm = sessionId.toString().substring(8,11);
			
		   //getcache
		   AutFixRfqDatosCache datosCache = obtenerCache(IdContraFirm);
		   validaciones.ValidarRPrima(datosCache, messageIn);
		   
		   //Eliminar Registro en Cache.
		   eliminarDatoCache(IdContraFirm);
		   
		   String IdAfiliado = datosCache.getIdAfiliado();
		   
		   String idQuoteReq = messageIn.getString(131);   
		   
		   if(DataAccess.validarContinuidadEjecucion(IdAfiliado)) {
			   
			  
			   ejecutarSiguientePaso(datosCache.getIdCaseseq(), datosCache.getIdEjecucion(), idQuoteReq);
			
			   System.out.println("** CONTINUAR ***");
		   }else {
			   System.out.println("**** ESPERAR ****");
		   }
		   
		   
//		   DataAccess.validarContinuidadEjecucion(IdAfiliado);
		   
		   //obtenerSiguienteRegistro()
		   
		   //obtenerIdQuote
		   // generarS
		   
		   
	   }
	   
	   public void validarS(SessionID sessionId, quickfix.Message messageIn) throws InterruptedException, SQLException, FieldNotFound, SessionNotFound {
		   
		// Obtener el ID_AFILIADO de la session
		   String IdContraFirm = sessionId.toString().substring(8,11);
		   
		 //getcache
		   AutFixRfqDatosCache datosCache = obtenerCache(IdContraFirm);
		   
		   validaciones.ValidarSPrima(datosCache, messageIn);
		   
		   eliminarDatoCache(IdContraFirm);
		   
           String IdAfiliado = datosCache.getIdAfiliado();
		   
		   String idQuoteReq = messageIn.getString(131);   
		   
		   if(DataAccess.validarContinuidadEjecucion(IdAfiliado)) {
			   
			   //ejecutarSiguientePaso(datosCache.getIdCaseseq(), datosCache.getIdEjecucion(), idQuoteReq);
			
			   System.out.println("** CONTINUAR ***");
		   }else {
			   System.out.println("**** ESPERAR ****");
		   }
		   
		   
		   
	   }
	   
	   public void validarAI(SessionID sessionId, quickfix.Message messageIn) throws SQLException, InterruptedException {
		   
		   
		   System.out.println("*********** VALIDANDO AI ************");
		   String sIdAfiliado = sessionId.toString().substring(8,11);
		   AutFixRfqDatosCache datosCache = obtenerCache(sIdAfiliado);
		   validaciones.ValidaAI(datosCache, messageIn);
		  
		   //Eliminar Registro en Cache.
		   eliminarDatoCache(sIdAfiliado);
		   
		   System.out.println("*********** SALIENDO DE VALIDACION ************");
		   
	   }
	   
	   
	   /**
	    *  Metodo para recuperar el objeto siguiente de la tabla de datos.
	    * @param idCaseSeqActual
	    * @return
	 * @throws SQLException 
	 * @throws InterruptedException 
	 * @throws SessionNotFound 
	    */
	   
	   public void ejecutarSiguientePaso(int idCaseSeqActual, long idEjecucion, String quoteReqId) throws SQLException, SessionNotFound, InterruptedException {
		   
		   int idCaseSeqSiguiente = idCaseSeqActual++;
		   
		   System.out.println("******************* IDCASE  **********"+idCaseSeqSiguiente);
		   
		   ResultSet resultSet = null;
		   String _query = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos ORDER BY ID_CASESEQ ="+ idCaseSeqSiguiente;
		   
		   resultSet = DataAccess.getQuery(_query);
		   
		   System.out.println(resultSet);
		   
		   while(resultSet.next()) {
		   if (resultSet != null) {
			   
			   System.out.println("Continua con el siguiente paso.");
//			   resultSet.first();
			   // seleccionar el siguiente paso a ejecutar.
			   enviarMensaje(resultSet, idEjecucion, quoteReqId);
			   
		   }else {
			   //ejecutar reporte.
			   System.out.println("Generar reporte....");
			   //Termina ejecucion.
			   System.out.println("FIN EJECUCION....");
		       }
		   }
		   
		
	   }
	   
	   public int getEscenario(ResultSet resultSet) throws SQLException {
		   
		   int idCaseseq = resultSet.getInt("ID_CASESEQ");
		   		  
		return idCaseseq;
		   
	   }
	   
	   
		public static void printMessage(String typeMsg, SessionID sessionId, quickfix.Message message2) throws FieldNotFound {
			System.out.println("********************\nTIPO DE MENSAJE: " + typeMsg + "- SESSION:" + sessionId + "\nMENSAJE :"
					+ message2 + "\n----------------------------");

		}
		
		//Metodos get y set del Conexion base de datos
		   public DataAccess getConnectionBD() {
			   
			   return connectionBD;
		   }

		   public void setConnectionBD(DataAccess connectionBD) {
		     	this.connectionBD = connectionBD;
		  }
		   
		   public Login getLogin() {
				return login;
			}

			public void setLogin(Login login) {
				this.login = login;
			}		   
	   
}
