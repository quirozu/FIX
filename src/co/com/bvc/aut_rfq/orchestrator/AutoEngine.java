package co.com.bvc.aut_rfq.orchestrator;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import co.com.bvc.aut_rfq.adapter_inet.AdapterIO;
import co.com.bvc.aut_rfq.adapter_inet.Login;
import co.com.bvc.aut_rfq.basicfix.BasicFunctions;
import co.com.bvc.aut_rfq.dao.domain.AutFixRfqDatosCache;
import co.com.bvc.aut_rfq.dao.domain.RespuestaConstrucccionMsgFIX;
import co.com.bvc.aut_rfq.db.DataAccess;
import co.com.bvc.aut_rfq.report.CreateReport;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.field.QuoteStatus;
import quickfix.field.SenderSubID;
import quickfix.field.Symbol;
import quickfix.field.TargetCompID;
import quickfix.field.Text;
import quickfix.Message;

public class AutoEngine {

	CreateMessage createMesage = new CreateMessage();
	public static AdapterIO adapterIO;
	public static Login login;
	public static AutFixRfqDatosCache datosCache;
	public static RespuestaConstrucccionMsgFIX cache;
	
	// metodo que inicia la ejecucion
	public void iniciarEjecucion(int escenarioInicial, int escenarioFinal)
			throws SQLException, SessionNotFound, InterruptedException, IOException, FieldNotFound {

//		BasicFunctions.createConn();
		DataAccess.getConnection();
		int firsIdCaseSec = BasicFunctions.getFirtsIdCaseSeq(escenarioInicial);
		BasicFunctions.setEscenarioPrueba(escenarioInicial);
		BasicFunctions.setEscenarioFinal(escenarioFinal);
		if (firsIdCaseSec > 0) {
			//Dentro de startVariables se inicializa ID_EJECUCION
			BasicFunctions.startVariables();
			
			//Limpiar tabla de anulaciones 10 en adelante
			String queryAnulaciones = "DELETE FROM aut_fix_tcr_datos WHERE ID_CASESEQ > 20;";
			DataAccess.setQuery(queryAnulaciones);
			
			//Se crea el login
			adapterIO = new AdapterIO();
			login = new Login(adapterIO);
			DataAccess.limpiarCache();
			ejecutarSiguientePaso();
		} else {
			System.out.println("NO HAY DATOS EN LA BASE DE DATOS...");
		}
	}

	public void ejecutarSiguientePaso()
			throws SQLException, SessionNotFound, InterruptedException, IOException, FieldNotFound {

		int caso = BasicFunctions.getEscenarioFinal();
		Thread.sleep(5000);
		System.out.println("ID_CASESEQ: " + BasicFunctions.getIdCaseSeq());
		ResultSet rsDatos = DataAccess.datosMensaje(BasicFunctions.getIdCaseSeq());
		while (rsDatos.next()) {

			BasicFunctions.setIdCase(rsDatos.getInt("ID_CASE"));
			System.out.println("Continua con el siguiente paso.");
			System.out.println("************************** " + caso);

			if (caso < BasicFunctions.getIdCase()) {
				Thread.sleep(5000);
				System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
				System.out.println("++++++++++++++ FIN DE EJECUCION ++++++++++++");
				System.out.println("++++++++++++++++++++++++++++++++++++++++++++");
				caso++;
				System.out.println("GENERAR REPORTE....");
				CreateReport.maina();
				login.endSessions();
//				BasicFunctions.FinalLogin();
			} else {
				enviarMensaje(rsDatos);
				Thread.sleep(5000);
				BasicFunctions.setIdCaseSeq(BasicFunctions.getIdCaseSeq() + 1);
				System.out.println("++++++++++++++++ SECUENCIA ++++++++ " + BasicFunctions.getIdCaseSeq());
			}

		}

	}

	public void enviarMensaje(ResultSet resultSet)
			throws SessionNotFound, SQLException, InterruptedException, FieldNotFound {

		String msgType = resultSet.getString("ID_ESCENARIO");
		String idAfiliado = resultSet.getString("ID_AFILIADO");
		String idCase = resultSet.getString("ID_CASE");
		//System.out.println(resultSet);
		datosCache = new AutFixRfqDatosCache();
		RespuestaConstrucccionMsgFIX respConstruccion = new RespuestaConstrucccionMsgFIX();

		System.out.println("*********************\n" + msgType + "\n*********************");
		switch (msgType) {

		case "FIX_R":
			
			
			System.out.println("************************************************************");
			System.out.println("************************************************************");
			System.out.println("********                                            ********");
			System.out.println("********  COMIENZA ESCENARIO "+ idCase + "                      ********");
			System.out.println("********                                            ********");
			System.out.println("************************************************************");
			System.out.println("************************************************************");
			
			

			System.out.println("*********************");
			System.out.println("** INGRESA A FIX_R **");
			System.out.println("*********************");

			cache = createMesage.createR(resultSet);
			System.out.println(cache.getMessage());
//			for (String session : respConstruccion.getListSessiones()) {
//
//				// Construir mensaje a cache.
//				datosCache.setReceiverSession(session);
//				datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
//				datosCache.setIdCase(resultSet.getInt("ID_CASE"));
//				datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
//				datosCache.setEstado(resultSet.getString("ESTADO"));
//				datosCache.setIdAfiliado(resultSet.getString("ID_AFILIADO"));
//				datosCache.setIdEjecucion(BasicFunctions.getIdEjecution());
//
//				//cargarCache(datosCache);
//			}

			Session.sendToTarget(cache.getMessage(), login.getSessionOfAfiliado(idAfiliado));

			break;

		case "FIX_S":

			DataAccess.limpiarCache();
			System.out.println("*********************");
			System.out.println("** INGRESA A FIX_S **");
			System.out.println("*********************");

			Iterator<String> it = BasicFunctions.getQuoteReqId().keySet().iterator();
			while (it.hasNext()) {
				String key = (String) it.next();
				System.out.println("Clave: " + key + " -> Valor: " + BasicFunctions.getQuoteReqId().get(key));
			}

			String quoteReqId = BasicFunctions.getQuoteReqIdOfAfiliado(idAfiliado);

			System.out.println("AFILIADO: " + idAfiliado + " QUOTERQID: " + quoteReqId);

			cache = createMesage.createS(resultSet, quoteReqId);
			System.out.println(cache.getMessage());

//			for (String session : respConstruccion.getListSessiones()) {
//				System.out.println("SESSION EN S" + session);
//
//				// Construir mensaje a cache.
//				datosCache.setReceiverSession(session);
//				datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
//				datosCache.setIdCase(resultSet.getInt("ID_CASE"));
//				datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
//				datosCache.setEstado(resultSet.getString("ESTADO"));
//				datosCache.setIdAfiliado(resultSet.getString("ID_AFILIADO"));
//				datosCache.setIdEjecucion(BasicFunctions.getIdEjecution());
//
////				cargarCache(datosCache);
//			}

			Session.sendToTarget(cache.getMessage(), login.getSessionOfAfiliado(idAfiliado));
			break;

		case "FIX_AJ":

			System.out.println("**********************");
			System.out.println("** INGRESA A FIX_AJ **");
			System.out.println("**********************");

			String quoteId = BasicFunctions.getQuoteId();

//			idIAfiliado = resultSet.getString("ID_AFILIADO");

			cache = createMesage.createAJ(resultSet, quoteId);
			System.out.println(cache.getMessage());

//			for (String session : respConstruccion.getListSessiones()) {
//
//				// Construir mensaje a cache.
//				datosCache.setReceiverSession(session);
//				datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
//				datosCache.setIdCase(resultSet.getInt("ID_CASE"));
//				datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
//				datosCache.setEstado(resultSet.getString("ESTADO"));
//				// datosCache.setFixQuoteReqId(resultSet.getString("FIX_QUOTE_REQ_ID"));
//				datosCache.setIdAfiliado(resultSet.getString("ID_AFILIADO"));
//				datosCache.setIdEjecucion(BasicFunctions.getIdEjecution());
//
////				cargarCache(datosCache);
//			}

			Session.sendToTarget(cache.getMessage(), login.getSessionOfAfiliado(idAfiliado));

			break;

		case "FIX_Z":

			System.out.println("**********************");
			System.out.println("** INGRESA A FIX_Z **");
			System.out.println("**********************");

			cache = createMesage.createZ(login.getSessionOfAfiliado(idAfiliado), resultSet);
			System.out.println(cache.getMessage());

			System.out.println("ID DE Z " + BasicFunctions.getQuoteIdGenered());

//			for (String session : respConstruccion.getListSessiones()) {
//
//				// Construir mensaje a cache.
//				datosCache.setReceiverSession(session);
//				datosCache.setIdCaseseq(resultSet.getInt("ID_CASESEQ"));
//				datosCache.setIdCase(resultSet.getInt("ID_CASE"));
//				datosCache.setIdSecuencia(resultSet.getInt("ID_SECUENCIA"));
//				datosCache.setEstado(resultSet.getString("ESTADO"));
//				// datosCache.setFixQuoteReqId(resultSet.getString("FIX_QUOTE_REQ_ID"));
//				datosCache.setIdAfiliado(resultSet.getString("ID_AFILIADO"));
//				datosCache.setIdEjecucion(BasicFunctions.getIdEjecution());
//
////				cargarCache(datosCache);
//
//			}

			Session.sendToTarget(cache.getMessage(), login.getSessionOfAfiliado(idAfiliado));

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

		System.out.println("************************************");
		System.out.println("** INGRESA A VALIDAR R AUTOENGINE **");
		System.out.println("************************************");

		// Obtener el ID_AFILIADO de la session.
//		String IdContraFirm = sessionId.toString().substring(8, 11);
		String IdContraFirm = sessionId.getSenderCompID(); 
		
//		Validaciones validaciones = new Validaciones();
		Thread.sleep(3000);
		// Se valida si R_prima llega al mismo iniciador
		String targetCompId = messageIn.getHeader().getString(TargetCompID.FIELD);
		System.out.println("TARGET COMP ID: " + targetCompId + " IDCONTRAFIRM: " + IdContraFirm + "INICIADOR "
				+ BasicFunctions.getIniciator());

		try {

			if (IdContraFirm.equals(BasicFunctions.getIniciator())) {
				IdContraFirm = IdContraFirm + "R";
				System.out.println("VALOR DE IDCONTRA:" + IdContraFirm);
			}

			System.out.println("VALOR DE IDCONTRA:" + IdContraFirm);

			// getcache
//			AutFixRfqDatosCache datosCache = obtenerCache(IdContraFirm);
//			validaciones.ValidarRPrima(datosCache, (quickfix.fix44.Message) messageIn);

			// Eliminar Registro en Cache.
//			eliminarDatoCache(IdContraFirm);
			cache.getListSessiones().remove(IdContraFirm);
//			String IdAfiliado = datosCache.getIdAfiliado();
			String IdAfiliado = cache.getMessage().getHeader().getString(TargetCompID.FIELD);
			String idQuoteReq = messageIn.getString(131);

			BasicFunctions.addQuoteReqId(IdAfiliado, idQuoteReq);

//			if (DataAccess.validarContinuidadEjecucion()) {
			if(cache.getListSessiones().size() == 0) {
				ejecutarSiguientePaso();

				System.out.println("** CONTINUAR ***");
			} else {
				System.out.println("**** ESPERAR ****");
			}
		} catch (Exception e) {
//			System.out.println("Erorrrr/===============: " + e.getStackTrace() + e.getMessage());
//			e.printStackTrace();
		}
		System.out.println("*********** SALIENDO DE validarR ************");

	}

	public void validarS(SessionID sessionId, Message messageIn)
			throws InterruptedException, SQLException, FieldNotFound, SessionNotFound, IOException {

		System.out.println("************************************");
		System.out.println("** INGRESA A VALIDAR S AUTOENGINE **");
		System.out.println("************************************");

		// Obtener el ID_AFILIADO de la session
//		String IdContraFirm = sessionId.toString().substring(8, 11);
		String IdContraFirm = sessionId.getSenderCompID();
//		Validaciones validaciones = new Validaciones();
		Thread.sleep(3000);
		// getcache
//		AutFixRfqDatosCache datosCache = obtenerCache(IdContraFirm);

//		validaciones.ValidarSPrima(datosCache, messageIn);

//		eliminarDatoCache(IdContraFirm);
		cache.getListSessiones().remove(IdContraFirm);

		String quoteId = messageIn.getString(117);
		BasicFunctions.setQuoteId(quoteId);

//		if (DataAccess.validarContinuidadEjecucion()) {
		if(cache.getListSessiones().size() == 0) {
			ejecutarSiguientePaso();

			System.out.println("** CONTINUAR ***");
		} else {
			System.out.println("**** ESPERAR ****");
		}

		System.out.println("*********** SALIENDO DE validarS ************");

	}

	public void validarER(SessionID sessionId, Message message)
			throws InterruptedException, SQLException, FieldNotFound, SessionNotFound, IOException {

		System.out.println("*************************************");
		System.out.println("** INGRESA A CARGAR ER AUTOENGINE **");
		System.out.println("*************************************");
		
		String idAfiliado =message.getHeader().getString(TargetCompID.FIELD);
		String account = message.getString(1);
		
		if(idAfiliado.equals(BasicFunctions.getIniciator())) {
			String contraFirm=null, symbol=null, rueda=null, eti487=null,
					rec856_0 = null, rec856_1=null, rec856_2=null, lastPX=null, lastQty=null, transTime=null,
					validUntilTime=null, settDate=null, tradeDate=null, trMatchId=null, 
					arTTT=null, arTraderRepType=null, arTraStatus=null, ampDCV=null;
			int side = 0;
			
			symbol = message.getString(Symbol.FIELD);
			rueda = message.getString(762);
			
//			eti487 = idAfiliado.equals(BasicFunctions.getIniciator()) ? "0" : "2";
			eti487 = "0";
			rec856_0 = "96";
			rec856_2 = "99";
			lastPX = message.getString(31);
			lastQty = message.getString(32);
			transTime = message.getString(60);
			validUntilTime = "2019-08-30 21:00:00";
			settDate = message.getString(64);
			tradeDate = message.getString(64);
			trMatchId = message.getString(880);
			side = message.getInt(54);
			account = message.getString(1);
			arTTT = "0";
			arTraderRepType = "96";
			arTraStatus = "0";
			ampDCV = null;
			
			String queryInsertIni = "INSERT INTO `aut_fix_tcr_datos` (`ID_CASESEQ`, `ID_CASE`, `ID_SECUENCIA`, `ESTADO`, `ID_ESCENARIO`, "
					+ "`MERCADO`, `ACCION`, `ID_AFILIADO`, `CONTRAFIRM`, `AE_POSDUPFLAG`, `AE_SYMBOL`, `AE_SECSUBTYPE`, `AE_SECIDSOURCE`, "
					+ "`AE_TRADTRANTYPE`, `AE_TRADEREPTYPE`, `AE_RECTREPTYPE1`, `AE_RECTREPTYPE2`, `AE_TRDTYPE`, `AE_EXECTYPE`, `AE_MATCHSTATUS`, "
					+ "`AE_CURRENCY`, `AE_LASTPX`, `AE_LASTQTY`, `AE_TRANSTIME`, `AE_VALIDUNTILTIME`, `AE_SETTDATE`, `AE_TRADEDATE`, `AE_GROSSTRADEAMT`, "
					+ "`AE_TRMATCHID`, `AE_NOSIDES`, `AE_SIDE`, `AE_PARTYIDSOURCE`, `AE_ACCOUNT`, `AR_TRADTRANTYPE`, `AR_TRADEREPTYPE`, `AR_TRDRPTSTATUS`, "
					+ "`AR_TRANSTIME`, `ER_EXECTYPE`,`AMP_DCV`) " 
					+ " VALUES ("+BasicFunctions.getIdTcrSeq()+", "+BasicFunctions.getNumEscenario()+", '1', 'A', 'FIX_AE',"
					+ " 'DV', 'N', '"+BasicFunctions.getIniciator()+"', '"+BasicFunctions.getReceptor()+"', 'Y', '"+ symbol+"', '"+rueda+"', 'M',"
					+ " '0', '96', NULL, '99', '22', 'F', '0', "
					+ " 'COP', "+lastPX+", "+lastQty+", '"+transTime+"', NULL, '"+settDate+"', '"+tradeDate+"', '250', '"
					+ trMatchId+"', '1', '"+side+"', 'C', '"+account+"', '0', '96', '0', '20190716-10:00:00.000', 'H',NULL);";
			
			System.out.println("CONSULTA INSERCION INICIADOR: "+queryInsertIni);
			
			DataAccess.setQuery(queryInsertIni);
			
			BasicFunctions.setIdTcrSeq(BasicFunctions.getIdTcrSeq()+1);
			
			String queryInsertRec = "INSERT INTO `aut_fix_tcr_datos` (`ID_CASESEQ`, `ID_CASE`, `ID_SECUENCIA`, `ESTADO`, `ID_ESCENARIO`, "
					+ "`MERCADO`, `ACCION`, `ID_AFILIADO`, `CONTRAFIRM`, `AE_POSDUPFLAG`, `AE_SYMBOL`, `AE_SECSUBTYPE`, `AE_SECIDSOURCE`, "
					+ "`AE_TRADTRANTYPE`, `AE_TRADEREPTYPE`, `AE_RECTREPTYPE1`, `AE_RECTREPTYPE2`, `AE_TRDTYPE`, `AE_EXECTYPE`, `AE_MATCHSTATUS`, "
					+ "`AE_CURRENCY`, `AE_LASTPX`, `AE_LASTQTY`, `AE_TRANSTIME`, `AE_VALIDUNTILTIME`, `AE_SETTDATE`, `AE_TRADEDATE`, `AE_GROSSTRADEAMT`, "
					+ "`AE_TRMATCHID`, `AE_NOSIDES`, `AE_SIDE`, `AE_PARTYIDSOURCE`, `AE_ACCOUNT`, `AR_TRADTRANTYPE`, `AR_TRADEREPTYPE`, `AR_TRDRPTSTATUS`, "
					+ "`AR_TRANSTIME`, `ER_EXECTYPE`,`AMP_DCV`) " 
					+ " VALUES ("+BasicFunctions.getIdTcrSeq()+", '"+BasicFunctions.getNumEscenario()+"', '2', 'A', 'FIX_AE_R',"
					+ " 'DV', 'N', '"+BasicFunctions.getReceptor()+"', '"+BasicFunctions.getIniciator()+"', 'Y', '"+ symbol+"', '"+rueda+"', 'M',"
					+ " '2', '97', '14', '99', '22', 'F', '0', "
					+ " 'COP', "+lastPX+", "+lastQty+", '"+transTime+"', '2019-08-30 21:00:00','"+settDate+"', '"+tradeDate+"', '250', '"
					+ trMatchId+"', '1', '"+(3-side)+"', 'C', '"+account+"', NULL, NULL, NULL, NULL, 'H','1');";
			
			System.out.println("CONSULTA INSERCION RECEPTOR: "+queryInsertRec);
			
			DataAccess.setQuery(queryInsertRec);
			BasicFunctions.setIdTcrSeq(BasicFunctions.getIdTcrSeq()+1);
			BasicFunctions.setNumEscenario(BasicFunctions.getNumEscenario()+1);

		} 
		
		// Obtener el ID_AFILIADO de la session
//		String IdContraFirm = sessionId.toString().substring(8, 11);
		String IdContraFirm = sessionId.getSenderCompID();

		// getcache
//		AutFixRfqDatosCache datosCache = obtenerCache(IdContraFirm);
//		Validaciones validaciones = new Validaciones();
//
//		validaciones.validarOcho(datosCache, (quickfix.fix44.Message) messageIn);
//		Thread.sleep(3000);
//		eliminarDatoCache(IdContraFirm);
		cache.getListSessiones().remove(IdContraFirm);

//		if (DataAccess.validarContinuidadEjecucion()) {
		if(cache.getListSessiones().size() == 0) {
			ejecutarSiguientePaso();
			System.out.println("** CONTINUAR ***");

		} else {
			System.out.println("**** ESPERAR ****");
		}
		
		String rolle = idAfiliado.equals(BasicFunctions.getIniciator()) ? "INICIADOR." : "RECEPTOR.";

		System.out.println("*********** SALIENDO DE VALIDAR ER " + rolle );
	}
	

	public void validarAI(SessionID sessionId, Message messageIn)
			throws SQLException, InterruptedException, SessionNotFound, IOException, FieldNotFound {

		System.out.println("*************************************");
		System.out.println("** INGRESA A VALIDAR AI AUTOENGINE **");
		System.out.println("*************************************");

		int quoteStatus = messageIn.getInt(QuoteStatus.FIELD);
		String sIdAfiliado = sessionId.getSenderCompID();

		if (quoteStatus == 9 || quoteStatus == 17) {

			String msgRechazo = messageIn.getString(Text.FIELD);
			System.out.println("AI DE RECHAZO. "+msgRechazo);
			// Persistir msgRechazo
//			String sIdAfiliado = sessionId.toString().substring(8, 11);
//			String sIdAfiliado = sessionId.getSenderCompID();
			
//			datosCache = obtenerCache(sIdAfiliado);
			Validaciones validaciones = new Validaciones();
			validaciones.validar3(datosCache, (quickfix.fix44.Message) messageIn);
			// quitar sesiones en cache
			DataAccess.limpiarCache();
			ejecutarSiguienteEscenario();

		} else {
		
			cache.getListSessiones().remove(sIdAfiliado);
	
	//		if (DataAccess.validarContinuidadEjecucion()) {
			if(cache.getListSessiones().size() == 0) {
				ejecutarSiguientePaso();
				System.out.println("** CONTINUAR ***");
	
			} else {
				System.out.println("**** ESPERAR ****");
			}
	
			System.out.println("*********** SALIENDO DE VALIDAR AJ  ************");
		}
		
//		AutFixRfqDatosCache datosCache = obtenerCache(sIdAfiliado);
//		Validaciones validaciones = new Validaciones();
//		validaciones.validaAI(datosCache, (quickfix.fix44.Message) messageIn);

		// Eliminar Registro en Cache.
//		eliminarDatoCache(sIdAfiliado);
//
//		if (DataAccess.validarContinuidadEjecucion()) {
//			ejecutarSiguientePaso();
//			System.out.println("** CONTINUAR ***");
//		} else {
//			System.out.println("**** ESPERAR ****");
//		}
//
//		System.out.println("*********** SALIENDO DE validarAI ************");

	}

	public void validarZ(SessionID sessionId, Message messageIn)
			throws SQLException, InterruptedException, SessionNotFound, IOException, FieldNotFound {

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

	public void validarAG(SessionID sessionId, Message message)
			throws SQLException, InterruptedException, SessionNotFound, IOException, FieldNotFound {

		System.out.println("*************************");
		System.out.println("** INGRESA A validar AG **");
		System.out.println("*************************");

		String sIdAfiliado = sessionId.toString().substring(8, 11);
		AutFixRfqDatosCache datosCache = obtenerCache(sIdAfiliado);
		Validaciones validaciones = new Validaciones();
		validaciones.validarAG(datosCache, (quickfix.fix44.Message) message);

		// Eliminar Registro en Cache.
		DataAccess.limpiarCache();

		ejecutarSiguienteEscenario();
		System.out.println("** CONTINUAR ***");

		System.out.println("*********** SALIENDO DE validarAG ************");
	}

	public void ejecutarSiguienteEscenario()
			throws SQLException, SessionNotFound, InterruptedException, IOException, FieldNotFound {

		int sec = BasicFunctions.getIdCase();
		sec = sec + 1;
		System.out.println("+++++++++++++++++ " + sec);
		String query = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos" + " WHERE ID_CASE= " + sec
				+ " ORDER BY ID_CASESEQ ASC LIMIT 1;";
		System.out.println(query);
		ResultSet resultset = DataAccess.getQuery(query);
		while (resultset.next()) {
			int cas = resultset.getInt("ID_CASESEQ");
			System.out.println("+++++++++++++++++++++++++++++++ " + cas);
			BasicFunctions.setIdCaseSeq(cas);
			ejecutarSiguientePaso();
		}
	}

	public void validar3(SessionID sessionId, Message messageIn)

			throws SQLException, InterruptedException, SessionNotFound, IOException, FieldNotFound {

		System.out.println("*************************");

		System.out.println("** INGRESA A VALIDAR 3 **");

		System.out.println("*************************");

		String sIdAfiliado = sessionId.toString().substring(8, 11);

		AutFixRfqDatosCache datosCache = obtenerCache(sIdAfiliado);

		Validaciones validaciones = new Validaciones();

		validaciones.validar3(datosCache, (quickfix.fix44.Message) messageIn);

		// Eliminar Registro en Cache.

		eliminarDatoCache(sIdAfiliado);

		DataAccess.limpiarCache();

		ejecutarSiguienteEscenario();

		System.out.println("** CONTINUAR ***");

		System.out.println("*********** SALIENDO DE VALIDAR 3 ************");

		Thread.sleep(5000);

	}

}
