package co.bvc.com.orquestador;

import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Iterator;
import co.bvc.com.basicfix.BasicFunctions;
import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.dao.domain.AutFixRfqDatosCache;
import co.bvc.com.dao.domain.RespuestaConstrucccionMsgFIX;
import co.bvc.com.test.CreateMessage;
import co.bvc.com.test.Login;
import co.bvc.com.test.Validaciones;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.Message;

public class AutoEngine {

	CreateMessage createMesage = new CreateMessage();

	// metodo que inicia la ejecucion
	public void iniciarEjecucion(int escenarioEjecucion) throws SQLException, SessionNotFound, InterruptedException, IOException, FieldNotFound {

		BasicFunctions.createConn();
		int firsIdCaseSec = BasicFunctions.getFirtsIdCaseSeq(escenarioEjecucion);
		BasicFunctions.setEscenarioPrueba(escenarioEjecucion);

		if (firsIdCaseSec > 0) {
			BasicFunctions.startVariables();
			BasicFunctions.createLogin();
			DataAccess.limpiarCache();
			ejecutarSiguientePaso();
		} else {
			System.out.println("NO HAY DATOS EN LA BASE DE DATOS...");
		}
	}

	int caso = BasicFunctions.getEscenarioPrueba();
	public void ejecutarSiguientePaso() throws SQLException, SessionNotFound, InterruptedException, IOException, FieldNotFound {

		System.out.println("ID_CASESEQ: " + BasicFunctions.getIdCaseSeq());
		ResultSet rsDatos = DataAccess.datosMensaje(BasicFunctions.getIdCaseSeq());
		while (rsDatos.next()) {
			
			BasicFunctions.setIdCase(rsDatos.getInt("ID_CASE"));
			System.out.println("Continua con el siguiente paso.");
			enviarMensaje(rsDatos);
			Thread.sleep(5000);
			BasicFunctions.setIdCaseSeq(BasicFunctions.getIdCaseSeq() + 1);
			System.out.println("++++++++++++++++ SECUENCIA ++++++++ "+ BasicFunctions.getIdCaseSeq());
//			if (caso<BasicFunctions.getIdCase()) {
//				System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
//				System.out.println("++++++++++++++ FIN DE EJECUCION ++++++++++++");
//				System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
//				caso++;
//				System.out.println("Generar reporte....");
//				CreateReport.maina();
//			}
			
			
		}
		
		System.out.println("FIN EJECUCION....");
	}

	public void enviarMensaje(ResultSet resultSet) throws SessionNotFound, SQLException, InterruptedException, FieldNotFound {

		String msgType;
		AutFixRfqDatosCache datosCache = new AutFixRfqDatosCache();
		msgType = resultSet.getString("ID_ESCENARIO");
		
		RespuestaConstrucccionMsgFIX respConstruccion = new RespuestaConstrucccionMsgFIX();

		System.out.println("*********************\n"+ msgType + "\n*********************");
		switch (msgType) {
		
		case "FIX_R":

			System.out.println("*********************");
			System.out.println("** INGRESA A FIX_R **");
			System.out.println("*********************");

			respConstruccion = createMesage.createR(resultSet);

			for (String session : respConstruccion.getListSessiones()) {

				// Construir mensaje a cache.
				datosCache.setReceiverSession(session);
				datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
				datosCache.setIdCase(resultSet.getInt("ID_CASE"));
				datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
				datosCache.setEstado(resultSet.getString("ESTADO"));
				// datosCache.setFixQuoteReqId(resultSet.getString("FIX_QUOTE_REQ_ID"));
				datosCache.setIdAfiliado(session);
				datosCache.setIdEjecucion(BasicFunctions.getIdEjecution());

				cargarCache(datosCache);
			}

			String idIAfiliado = resultSet.getString("ID_AFILIADO");

			Session.sendToTarget(respConstruccion.getMessage(), Login.getSessionOfAfiliado(idIAfiliado));

			break;

		case "FIX_S":

			DataAccess.limpiarCache();
			System.out.println("*********************");
			System.out.println("** INGRESA A FIX_S **");
			System.out.println("*********************");

			String idAfiliado = resultSet.getString("ID_AFILIADO");
			
			Iterator<String> it = BasicFunctions.getQuoteReqId().keySet().iterator();
			while(it.hasNext()){
			  String key = (String) it.next();
			  System.out.println("Clave: " + key + " -> Valor: " + BasicFunctions.getQuoteReqId().get(key));
			}
			
			String quoteReqId = BasicFunctions.getQuoteReqIdOfAfiliado(idAfiliado);
		
			System.out.println("AFILIADO: " +  idAfiliado + " QUOTERQID: " + quoteReqId);

			respConstruccion = createMesage.createS(resultSet, quoteReqId);

			System.out.println("************* INGRESA A FIX_S ****************");
			
			for (String session : respConstruccion.getListSessiones()) {

				// Construir mensaje a cache.
				datosCache.setReceiverSession(session);
				datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
				datosCache.setIdCase(resultSet.getInt("ID_CASE"));
				datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
				datosCache.setEstado(resultSet.getString("ESTADO"));
				// datosCache.setFixQuoteReqId(resultSet.getString("FIX_QUOTE_REQ_ID"));
				datosCache.setIdAfiliado(resultSet.getString("ID_AFILIADO"));
				datosCache.setIdEjecucion(BasicFunctions.getIdEjecution());

				cargarCache(datosCache);
			}

			idIAfiliado = resultSet.getString("ID_AFILIADO");

			// Construir mensaje a cache de la propia session.
			datosCache.setReceiverSession(idIAfiliado);
			datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
			datosCache.setIdCase(resultSet.getInt("ID_CASE"));
			datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
			datosCache.setEstado(resultSet.getString("ESTADO"));
			// datosCache.setFixQuoteReqId(resultSet.getString("FIX_QUOTE_REQ_ID"));
			datosCache.setIdAfiliado(idIAfiliado);
			datosCache.setIdEjecucion(BasicFunctions.getIdEjecution());

			cargarCache(datosCache);
			Session.sendToTarget(respConstruccion.getMessage(), Login.getSessionOfAfiliado(idIAfiliado));
			break;

		case "FIX_AJ":

			System.out.println("**********************");
			System.out.println("** INGRESA A FIX_AJ **");
			System.out.println("**********************");

			String quoteId = BasicFunctions.getQuoteId();
			
			idIAfiliado = resultSet.getString("ID_AFILIADO");

			respConstruccion = createMesage.createAJ(resultSet, quoteId);

			for (String session : respConstruccion.getListSessiones()) {

				// Construir mensaje a cache.
				datosCache.setReceiverSession(session);
				datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
				datosCache.setIdCase(resultSet.getInt("ID_CASE"));
				datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
				datosCache.setEstado(resultSet.getString("ESTADO"));
				// datosCache.setFixQuoteReqId(resultSet.getString("FIX_QUOTE_REQ_ID"));
				datosCache.setIdAfiliado(resultSet.getString("ID_AFILIADO"));
				datosCache.setIdEjecucion(BasicFunctions.getIdEjecution());

				cargarCache(datosCache);
			}

			Session.sendToTarget(respConstruccion.getMessage(), Login.getSessionOfAfiliado(idIAfiliado));

			break;
            
		case "FIX_Z":
			
			System.out.println("**********************");
			System.out.println("** INGRESA A FIX_Z **");
			System.out.println("**********************");
			
			idIAfiliado = resultSet.getString("ID_AFILIADO");
			
			respConstruccion = createMesage.createZ(Login.getSessionOfAfiliado(idIAfiliado), BasicFunctions.getQuoteIdGenered());
			
			System.out.println("ID DE Z " + BasicFunctions.getQuoteIdGenered());
			
			for (String session : respConstruccion.getListSessiones()) {

				// Construir mensaje a cache.
				System.out.println("\n************************ INGRESA AL FOR \n************************ " + session);
				datosCache.setReceiverSession(session);
				datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
				datosCache.setIdCase(resultSet.getInt("ID_CASE"));
				datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
				datosCache.setEstado(resultSet.getString("ESTADO"));
				// datosCache.setFixQuoteReqId(resultSet.getString("FIX_QUOTE_REQ_ID"));
				datosCache.setIdAfiliado(resultSet.getString("ID_AFILIADO"));
				datosCache.setIdEjecucion(BasicFunctions.getIdEjecution());

				cargarCache(datosCache);
				
				System.out.println("\n************************ SALE DEL FOR \n************************");
			}
	
			
            Session.sendToTarget(respConstruccion.getMessage(), Login.getSessionOfAfiliado(idIAfiliado));

			break;

		default:
			break;
		}

	}

	// Metodo que guarda el registro en base de datos
	public void cargarCache(AutFixRfqDatosCache datosCache) throws SQLException, InterruptedException {
		DataAccess.cargarCache(datosCache);
	}

	// Metodo que elimina el registro en cache (base de datos)
	public void eliminarDatoCache(String session) throws SQLException, InterruptedException {

		String queryDelete = "DELETE FROM bvc_automation_db.aut_fix_rfq_cache WHERE RECEIVER_SESSION = " + "'" + session
				+ "'" + ";";

		DataAccess.setQuery(queryDelete);
	}

	// Metodo que extraer el registro en base de datos
	public AutFixRfqDatosCache obtenerCache(String session) throws SQLException, InterruptedException {

		System.out.println("SESS: " + session);
		return DataAccess.obtenerCache(session);

	}

	public void validarR(SessionID sessionId, Message messageIn)
			throws SQLException, InterruptedException, FieldNotFound, SessionNotFound, IOException {

		System.out.println("************************");
		System.out.println("** INGRESA A VALIDAR R **");
		System.out.println("************************");
		
		System.out.println("##################################  Session: " + sessionId + "Message: " + messageIn);
		// Obtener el ID_AFILIADO de la session.
		String IdContraFirm = sessionId.toString().substring(8, 11);
		Validaciones validaciones = new Validaciones();

		// getcache
		AutFixRfqDatosCache datosCache = obtenerCache(IdContraFirm);
		validaciones.ValidarRPrima(datosCache, (quickfix.fix44.Message) messageIn);
		Thread.sleep(5000);

		// Eliminar Registro en Cache.
		eliminarDatoCache(IdContraFirm);

		String IdAfiliado = datosCache.getIdAfiliado();
//
		String idQuoteReq = messageIn.getString(131);
//		
		BasicFunctions.addQuoteReqId(IdAfiliado, idQuoteReq);

		if (DataAccess.validarContinuidadEjecucion()) {

			ejecutarSiguientePaso();

			System.out.println("** CONTINUAR ***");
	
		} else {
			System.out.println("**** ESPERAR ****");
		}

		System.out.println("*********** SALIENDO DE validarR ************");

	}

	public void validarS(SessionID sessionId, Message messageIn)
			throws InterruptedException, SQLException, FieldNotFound, SessionNotFound, IOException {

		System.out.println("************************");
		System.out.println("** INGRESA A validarS **");
		System.out.println("************************");

		// Obtener el ID_AFILIADO de la session
		String IdContraFirm = sessionId.toString().substring(8, 11);
		Validaciones validaciones = new Validaciones();

		// getcache
		AutFixRfqDatosCache datosCache = obtenerCache(IdContraFirm);

		validaciones.ValidarSPrima(datosCache, messageIn);

		eliminarDatoCache(IdContraFirm);

//		String IdAfiliado = datosCache.getIdAfiliado();

		String quoteId = messageIn.getString(117);
		BasicFunctions.setQuoteId(quoteId);

		if (DataAccess.validarContinuidadEjecucion()) {
			
			ejecutarSiguientePaso();

			System.out.println("** CONTINUAR ***");
		} else {
			System.out.println("**** ESPERAR ****");
		}

		System.out.println("*********** SALIENDO DE validarS ************");

	}

	public void validarAJ(SessionID sessionId, Message messageIn)
			throws InterruptedException, SQLException, FieldNotFound, SessionNotFound, IOException {

		System.out.println("*************************");
		System.out.println("** INGRESA A validarAJ **");
		System.out.println("*************************");
		// Obtener el ID_AFILIADO de la session
		String IdContraFirm = sessionId.toString().substring(8, 11);

		// getcache
		AutFixRfqDatosCache datosCache = obtenerCache(IdContraFirm);
		Validaciones validaciones = new Validaciones();

		validaciones.validarOcho(datosCache, (quickfix.fix44.Message) messageIn);

		eliminarDatoCache(IdContraFirm);

//		String IdAfiliado = datosCache.getIdAfiliado();

		if (DataAccess.validarContinuidadEjecucion()) {
			ejecutarSiguientePaso();
			System.out.println("** CONTINUAR ***");

		} else {
			System.out.println("**** ESPERAR ****");
		}

		System.out.println("*********** SALIENDO DE validarAJ ************");
	}

	public void validarAI(SessionID sessionId, Message messageIn)
			throws SQLException, InterruptedException, SessionNotFound, IOException, FieldNotFound {

		System.out.println("*************************");
		System.out.println("** INGRESA A validarAI **");
		System.out.println("*************************");

		String sIdAfiliado = sessionId.toString().substring(8, 11);
		AutFixRfqDatosCache datosCache = obtenerCache(sIdAfiliado);
		Validaciones validaciones = new Validaciones();
		validaciones.validaAI(datosCache, (quickfix.fix44.Message) messageIn);

		// Eliminar Registro en Cache.
		eliminarDatoCache(sIdAfiliado);
//		String IdAfiliado = datosCache.getIdAfiliado();

		if (DataAccess.validarContinuidadEjecucion()) {
			ejecutarSiguientePaso();
			System.out.println("** CONTINUAR ***");
		} else {
			System.out.println("**** ESPERAR ****");
		}

		System.out.println("*********** SALIENDO DE validarAI ************");

	}

	public void validarZ(SessionID sessionId, Message messageIn) throws SQLException, InterruptedException, SessionNotFound, IOException, FieldNotFound {
		
		System.out.println("*************************");
		System.out.println("** INGRESA A VALIDAR Z **");
		System.out.println("*************************");
		
		String sIdAfiliado = sessionId.toString().substring(8, 11);
		AutFixRfqDatosCache datosCache = obtenerCache(sIdAfiliado);
		Validaciones validaciones = new Validaciones();
		validaciones.validarZ(datosCache, (quickfix.fix44.Message) messageIn);
		
		// Eliminar Registro en Cache.
		eliminarDatoCache(sIdAfiliado);
		
		if (DataAccess.validarContinuidadEjecucion()) {
			ejecutarSiguientePaso();
			System.out.println("** CONTINUAR ***");
		} else {
			System.out.println("**** ESPERAR ****");
		}

		System.out.println("*********** SALIENDO DE VALIDAR Z ************");
		Thread.sleep(5000);
	}


	public static void printMessage(String typeMsg, SessionID sessionId, Message message) throws FieldNotFound {
		System.out.println("********************\nTIPO DE MENSAJE: " + typeMsg + "- SESSION:" + sessionId
				+ "\nMENSAJE :" + message + "\n----------------------------");

	}

}
