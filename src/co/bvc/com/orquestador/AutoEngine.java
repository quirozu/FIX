package co.bvc.com.orquestador;

import java.sql.ResultSet;
import java.sql.SQLException;

import co.bvc.com.basicfix.BasicFunctions;
import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.dao.domain.AutFixRfqDatosCache;
import co.bvc.com.dao.domain.RespuestaConstrucccionMsgFIX;
import co.bvc.com.test.CreateMessage;
import co.bvc.com.test.Validaciones;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.Message;

public class AutoEngine {

	CreateMessage createMesage = new CreateMessage();

	// metodo que inicia la ejecucion
	public void iniciarEjecucion() throws SQLException, SessionNotFound, InterruptedException {

		BasicFunctions.createConn();
		int firsIdCaseSec = BasicFunctions.getFirtsIdCaseSeq();

		if (firsIdCaseSec > 0) {
			BasicFunctions.createLogin();
			DataAccess.limpiarCache();
			ejecutarSiguientePaso();
		} else {
			System.out.println("NO HAY DATOS EN LA BASE DE DATOS...");
		}
	}

	public void ejecutarSiguientePaso() throws SQLException, SessionNotFound, InterruptedException {

		System.out.println("ID_CASESEQ: " +BasicFunctions.getIdCaseSeq());
		ResultSet rsDatos = DataAccess.datosMensaje(BasicFunctions.getIdCaseSeq());
		
			while (rsDatos.next()) {
				System.out.println("Continua con el siguiente paso.");
				enviarMensaje(rsDatos);
				Thread.sleep(5000);
				BasicFunctions.setIdCaseSeq(BasicFunctions.getIdCaseSeq() + 1);
			}
			System.out.println("Generar reporte....");
			System.out.println("FIN EJECUCION....");
	}

	public void enviarMensaje(ResultSet resultSet) throws SessionNotFound, SQLException, InterruptedException {

		String msgType;
		AutFixRfqDatosCache datosCache = new AutFixRfqDatosCache();
		msgType = resultSet.getString("ID_ESCENARIO");

		RespuestaConstrucccionMsgFIX respConstruccion = new RespuestaConstrucccionMsgFIX();

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
				datosCache.setIdAfiliado(resultSet.getString("ID_AFILIADO"));
				datosCache.setIdEjecucion(BasicFunctions.getIdEjecution());

				cargarCache(datosCache);
			}

			String idIAfiliado = resultSet.getString("ID_AFILIADO");

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
			
			Session.sendToTarget(respConstruccion.getMessage(), BasicFunctions.getLogin().getSessionID1());


			break;

		case "FIX_S":
			
			System.out.println("*********************");
			System.out.println("** INGRESA A FIX_S **");
			System.out.println("*********************");
			
			String quoteReqId = BasicFunctions.getQuoteReqId();

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

			Session.sendToTarget(respConstruccion.getMessage(), BasicFunctions.getLogin().getSessionID2());
			break;

		case "FIX_AJ":

			System.out.println("**********************");
			System.out.println("** INGRESA A FIX_AJ **");
			System.out.println("**********************");
			
			String quoteId = BasicFunctions.getQuoteId();

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

			   Session.sendToTarget(respConstruccion.getMessage(), BasicFunctions.getLogin().getSessionID1());

			break;
		case "FIX_Z":

			break;

		default:
			break;
		}

	}

	// Metodo que guarda el registro en base de datos
	public void cargarCache(AutFixRfqDatosCache datosCache) throws SQLException {

		DataAccess.cargarCache(datosCache);
	}

	// Metodo que elimina el registro en cache (base de datos)
	public void eliminarDatoCache(String session) throws SQLException, InterruptedException {

		String queryDelete = "DELETE FROM bvc_automation_db.aut_fix_rfq_cache WHERE RECEIVER_SESSION = " + "'" + session
				+ "'" + ";";

		DataAccess.setQuery(queryDelete);
	}

	// Metodo que extraer el registro en base de datos
	public AutFixRfqDatosCache obtenerCache(String session) throws SQLException {

		System.out.println("SESS: " + session);
		return DataAccess.obtenerCache(session);

	}

	public void validarR(SessionID sessionId, Message messageIn)
			throws SQLException, InterruptedException, FieldNotFound, SessionNotFound {

		System.out.println("************************");
		System.out.println("** INGRESA A validarR **");
		System.out.println("************************");
		
		// Obtener el ID_AFILIADO de la session.
		String IdContraFirm = sessionId.toString().substring(8, 11);
		Validaciones validaciones = new Validaciones();

		// getcache
		AutFixRfqDatosCache datosCache = obtenerCache(IdContraFirm);
		validaciones.ValidarRPrima(datosCache, (quickfix.fix44.Message) messageIn);

		// Eliminar Registro en Cache.
		eliminarDatoCache(IdContraFirm);

		String IdAfiliado = datosCache.getIdAfiliado();

		String idQuoteReq = messageIn.getString(131);
		BasicFunctions.setQuoteReqId(idQuoteReq);

		if (DataAccess.validarContinuidadEjecucion(IdAfiliado)) {

			ejecutarSiguientePaso();

			System.out.println("** CONTINUAR ***");
		} else {
			System.out.println("**** ESPERAR ****");
		}

		System.out.println("*********** SALIENDO DE validarR ************");

	}

	public void validarS(SessionID sessionId, Message messageIn)
			throws InterruptedException, SQLException, FieldNotFound, SessionNotFound {

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

		String IdAfiliado = datosCache.getIdAfiliado();

		String quoteId = messageIn.getString(117);
		BasicFunctions.setQuoteId(quoteId);

		if (DataAccess.validarContinuidadEjecucion(IdAfiliado)) {

			ejecutarSiguientePaso();

			System.out.println("** CONTINUAR ***");
		} else {
			System.out.println("**** ESPERAR ****");
		}
		
		System.out.println("*********** SALIENDO DE validarS ************");

	}

	public void validarAJ(SessionID sessionId, Message messageIn)
			throws InterruptedException, SQLException, FieldNotFound, SessionNotFound {

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

		String IdAfiliado = datosCache.getIdAfiliado();

		if (DataAccess.validarContinuidadEjecucion(IdAfiliado)) {
			System.out.println("** CONTINUAR ***");

		} else {
			System.out.println("**** ESPERAR ****");
		}
		
		System.out.println("*********** SALIENDO DE validarAJ ************");
	}

	public void validarAI(SessionID sessionId, Message messageIn) throws SQLException, InterruptedException, SessionNotFound {

		System.out.println("*************************");
		System.out.println("** INGRESA A validarAI **");
		System.out.println("*************************");
		
		String sIdAfiliado = sessionId.toString().substring(8, 11);
		AutFixRfqDatosCache datosCache = obtenerCache(sIdAfiliado);
		Validaciones validaciones = new Validaciones();
		validaciones.validaAI(datosCache, (quickfix.fix44.Message) messageIn);

		// Eliminar Registro en Cache.
		eliminarDatoCache(sIdAfiliado);
		String IdAfiliado = datosCache.getIdAfiliado();

		if (DataAccess.validarContinuidadEjecucion(IdAfiliado)) {

			ejecutarSiguientePaso();
			System.out.println("** CONTINUAR ***");
		} else {
			System.out.println("**** ESPERAR ****");
		}

		

		System.out.println("*********** SALIENDO DE validarAI ************");

	}

	/**
	 * Metodo para recuperar el objeto siguiente de la tabla de datos.
	 * 
	 * @param idCaseSeqActual
	 * @return
	 * @throws SQLException
	 * @throws InterruptedException
	 * @throws SessionNotFound
	 */

//	   public void ejecutarSiguientePaso(int idCaseSeqActual, long idEjecucion, String quoteReqId, String quoteId) throws SQLException, SessionNotFound, InterruptedException {

	public static void printMessage(String typeMsg, SessionID sessionId, Message message)
			throws FieldNotFound {
		System.out.println("********************\nTIPO DE MENSAJE: " + typeMsg + "- SESSION:" + sessionId
				+ "\nMENSAJE :" + message + "\n----------------------------");

	}
}
