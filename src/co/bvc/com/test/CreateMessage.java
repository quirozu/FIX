package co.bvc.com.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import co.bvc.com.basicfix.BasicFunctions;
import co.bvc.com.basicfix.Constantes;
import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.dao.domain.RespuestaConstrucccionMsgFIX;
import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.StringField;
import quickfix.field.BeginString;
import quickfix.field.NoPartyIDs;
import quickfix.field.OfferSize;
import quickfix.field.OfferYield;
import quickfix.field.OrderQty;
import quickfix.field.PartyID;
import quickfix.field.PartyIDSource;
import quickfix.field.PartyRole;
import quickfix.field.QuoteCancelType;
import quickfix.field.QuoteID;
import quickfix.field.QuoteReqID;
import quickfix.field.QuoteRespID;
import quickfix.field.QuoteRespType;
import quickfix.field.SecurityIDSource;
import quickfix.field.SecuritySubType;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.ValidUntilTime;
import quickfix.fix44.Quote;
import quickfix.fix44.QuoteCancel;
import quickfix.fix44.QuoteRequest;
import quickfix.fix44.QuoteResponse;
import quickfix.fix44.Message;
import quickfix.fix44.Message.Header;

public class CreateMessage {
	
	public RespuestaConstrucccionMsgFIX createR(int i, ResultSet resultSet) throws SessionNotFound, SQLException {

		RespuestaConstrucccionMsgFIX respuestaMessage = new RespuestaConstrucccionMsgFIX();
		
//		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos "
//				+ "WHERE ID_CASESEQ =" + i;

		String queryParties = "SELECT linea.ID_ESCENARIO, partes.RQ_PARTYID, partes.RQ_PARTYIDSOURCE, partes.RQ_PARTYROLE, partes.RECEIVER_SESSION\r\n"
				+ "FROM aut_fix_rfq_datos linea INNER JOIN aut_fix_rfqparty_datos partes\r\n"
				+ "	ON linea.ID_CASESEQ = partes.RFQ_IDCASE\r\n" + "WHERE linea.ID_CASESEQ =" + i;

//		ResultSet resultset;
		ResultSet resultSetParties;
		String cIdRandom = Integer.toString((int) ((Math.random() * 80_000_000) + 1_000_000)); 
		try {
//			resultset = DataAccess.getQuery(queryMessageR);
			resultSetParties = DataAccess.getQuery(queryParties);

			QuoteReqID quoteReqID = new QuoteReqID(""+cIdRandom); // 131
			QuoteRequest quoteRequest = new QuoteRequest(quoteReqID); // 35 --> R
			Header header = (Header) quoteRequest.getHeader();
			header.setField(new BeginString(Constantes.PROTOCOL_FIX_VERSION)); // 8
			QuoteRequest.NoRelatedSym noRelatedSym = new QuoteRequest.NoRelatedSym();

//			while (resultSet.next()) {
				noRelatedSym.set(new Symbol(resultSet.getString("RQ_SYMBOL")));
				noRelatedSym.setField(new SecurityIDSource("M"));
				noRelatedSym.setField(new OrderQty(resultSet.getDouble("RQ_ORDERQTY")));
				//noRelatedSym.setField(new StringField(38, resultSet.getString("RQ_ORDERQTY")));
				noRelatedSym.setField(new StringField(54, resultSet.getString("RQ_SIDE")));
				noRelatedSym.setField(new SecuritySubType(resultSet.getString("RQ_SECSUBTYPE")));
				noRelatedSym.setField(new NoPartyIDs());
//			}

			QuoteRequest.NoRelatedSym.NoPartyIDs parte = new QuoteRequest.NoRelatedSym.NoPartyIDs();
			
			List<String> list = new ArrayList<String>();

//			 Parties
			while (resultSetParties.next()) {
				
				String rSession = resultSetParties.getString("RECEIVER_SESSION");
				
				if (rSession != null) {

					list.add(rSession);					
				}

				parte.set(new PartyID(resultSetParties.getString("RQ_PARTYID")));
				parte.set(new PartyIDSource('C'));
				parte.set(new PartyRole(resultSetParties.getInt("RQ_PARTYROLE")));

				noRelatedSym.addGroup(parte);
			}
			
			// datosCache.setListSession(list);

			
			
			quoteRequest.addGroup(noRelatedSym);
			
			respuestaMessage.setMessage(quoteRequest);
			respuestaMessage.setListSessiones(list);

			System.out.println("******************************\n" + quoteRequest + "****************\n");

			return respuestaMessage;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Message sendCancelR(SessionID sessionId, String cid) throws SessionNotFound, SQLException {

		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos "
				+ "WHERE ID_ESCENARIO = 'FIX_R' and ID_CASE = 1";

		String queryParties = "SELECT linea.ID_ESCENARIO, partes.RQ_PARTYID, partes.RQ_PARTYIDSOURCE, partes.RQ_PARTYROLE\r\n"
				+ "FROM aut_fix_rfq_datos linea INNER JOIN aut_fix_rfqparty_datos partes\r\n"
				+ "	ON linea.ID_CASESEQ = partes.RFQ_IDCASE\r\n" + "WHERE linea.ID_CASESEQ = 1";

		ResultSet resultset;
		ResultSet resultSetParties;
		try {
			resultset = DataAccess.getQuery(queryMessageR);
			resultSetParties = DataAccess.getQuery(queryParties);

			QuoteReqID quoteReqID = new QuoteReqID(cid); // 131
			QuoteRequest quoteRequest = new QuoteRequest(quoteReqID); // 35 --> R
			Header header = (Header) quoteRequest.getHeader();
			header.setField(new BeginString(Constantes.PROTOCOL_FIX_VERSION)); // 8
			QuoteRequest.NoRelatedSym noRelatedSym = new QuoteRequest.NoRelatedSym();

			while (resultset.next()) {
				noRelatedSym.set(new Symbol(resultset.getString("RQ_SYMBOL")));
				noRelatedSym.setField(new SecurityIDSource("M"));
				noRelatedSym.setField(new OrderQty(0));
				noRelatedSym.setField(new StringField(54, resultset.getString("RQ_SIDE")));
				noRelatedSym.setField(new SecuritySubType(resultset.getString("RQ_SECSUBTYPE")));
				// noRelatedSym.setField(new StringField(453,
				// resultset.getString("RQ_NOPARTYIDS")));
				noRelatedSym.setField(new NoPartyIDs());

			}

			QuoteRequest.NoRelatedSym.NoPartyIDs parte = new QuoteRequest.NoRelatedSym.NoPartyIDs();

			// Parties
			while (resultSetParties.next()) {
				parte.set(new PartyID(resultSetParties.getString("RQ_PARTYID")));
				parte.set(new PartyIDSource('C'));
				parte.set(new PartyRole(resultSetParties.getInt("RQ_PARTYROLE")));

				noRelatedSym.addGroup(parte);
			}

			quoteRequest.addGroup(noRelatedSym);

			System.out.println("******************************\n" + quoteRequest);

			return quoteRequest;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * The S-type message is generated
	 * 
	 * @param sessionId
	 * @param strQuoteId
	 * @param strQReqId
	 * @param cant
	 * @param symbol
	 * @param strVUT
	 * @throws SessionNotFound
	 */

	public RespuestaConstrucccionMsgFIX createS(int idCaseseq, ResultSet resultset, String idQuoteRedId)
			throws SessionNotFound, SQLException {

		RespuestaConstrucccionMsgFIX respuestaMessage = new RespuestaConstrucccionMsgFIX();

		String queryParties = "SELECT linea.ID_ESCENARIO, partes.RQ_PARTYID, partes.RQ_PARTYIDSOURCE, partes.RQ_PARTYROLE, partes.RECEIVER_SESSION\r\n"
				+ "FROM aut_fix_rfq_datos linea INNER JOIN aut_fix_rfqparty_datos partes\r\n"
				+ "	ON linea.ID_CASESEQ = partes.RFQ_IDCASE\r\n" + "WHERE linea.ID_CASESEQ =" + BasicFunctions.getIdCaseSeg();
		// -------------------
		System.out.println("Esta es la consulta +++++++\n"+queryParties);
//		ResultSet resultset;
		ResultSet resultSetParties;
		String cIdRandom = Integer.toString((int) ((Math.random() * 80_000_000) + 1_000_000)); 

		try {
//			resultset = DataAccess.getQuery(queryMessageS);
			resultSetParties = DataAccess.getQuery(queryParties);

			QuoteID quoteID = new QuoteID(cIdRandom);
			Quote quote = new Quote(quoteID); // 35 --> S

			Header header = (Header) quote.getHeader();
			header.setField(new BeginString(Constantes.PROTOCOL_FIX_VERSION)); // 8

			quote.setField(new QuoteReqID(idQuoteRedId)); // 131

//			while (resultset.next()) {
				quote.set(new Symbol(resultset.getString("RQ_SYMBOL")));
				quote.setField(new SecuritySubType(resultset.getString("RQ_SECSUBTYPE")));
				quote.setField(new OfferSize(resultset.getDouble("RQ_OFFERSIZE")));
				quote.setField(new OfferYield(resultset.getDouble("RQ_OFFERYIELD")));

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");
				LocalDateTime dateTime = LocalDateTime.parse(resultset.getString("RQ_VALIDUNTILTIME"), formatter);
				quote.setField(new ValidUntilTime(dateTime)); // "20190404-23:00:00";
//			}

			// Parties
			Quote.NoPartyIDs parte = new Quote.NoPartyIDs();

			List<String> list = new ArrayList<String>();
					
			while (resultSetParties.next()) {
				
				String rSession = resultSetParties.getString("RECEIVER_SESSION");
				
				if(rSession != null) {
					list.add(rSession);
				}
				parte.set(new PartyID(resultSetParties.getString("RQ_PARTYID")));
				parte.set(new PartyIDSource('C'));
				parte.set(new PartyRole(resultSetParties.getInt("RQ_PARTYROLE")));

				quote.addGroup(parte);
			}

			/*
			 * partie1.set(new PartyID("DCV")); partie1.set(new PartyIDSource('C'));
			 * partie1.set(new PartyRole(10));
			 */
			// quote.addGroup(parte);

			// System.out.println("*********************\n S FORMADO....\n" + quote +
			// "\n--------------------");

			respuestaMessage.setListSessiones(list);
			respuestaMessage.setMessage(quote);
			Session.sendToTarget(respuestaMessage.getMessage(), Login.getSessionID2());
			
			return respuestaMessage;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}



	public static RespuestaConstrucccionMsgFIX createAJ(ResultSet resultset, String strQuoteId) throws SessionNotFound {

		RespuestaConstrucccionMsgFIX respuestaMessage = new RespuestaConstrucccionMsgFIX();
		
//		Login inicio = new Login();
		
//		String queryMessageAJ = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos WHERE ID_ESCENARIO = 'FIX_AJ' AND ID_CASE = 1";
		
//		ResultSet resultset;
		try {
//			resultset = DataAccess.getQuery(queryMessageAJ);
//			while (resultset.next()) {
				//QuoteRespID quoteRespID = new QuoteRespID(resultset.getString("ID_CASESEQ"));
				QuoteRespID quoteRespID = new QuoteRespID(strQuoteId.substring(1,8));
				QuoteRespType qouteRespType = new QuoteRespType(resultset.getInt("RQ_QUORESPTYPE"));
				QuoteResponse quoteResponse = new QuoteResponse(quoteRespID, qouteRespType); // 35 --> AJ
				
//				QuoteResponse quoteResponse = new QuoteResponse();

				Header header = (Header) quoteResponse.getHeader();
				header.setField(new BeginString(Constantes.PROTOCOL_FIX_VERSION)); // 8

				quoteResponse.setField(new QuoteID(strQuoteId));
//				quoteResponse.setField(new StringField(49, resultset.getString("ID_AFILIADO")));
				quoteResponse.setField(new StringField(54, resultset.getString("RQ_SIDE")));
				quoteResponse.setField(new Symbol(resultset.getString("RQ_SYMBOL")));
				quoteResponse.setField(new StringField(762, resultset.getString("RQ_SECSUBTYPE")));
//				quoteResponse.setField(new StringField(50, resultset.getString("RQ_TRADER")));
//				quoteResponse.setField(new StringField(54, resultset.getString("RQ_RELATEDID")));//?????
//				quoteResponse.setField(new StringField(694, resultset.getString("RQ_NORELATEDSYM")));


				System.out.println("MENSAJE AJ CONSTUIDO " + quoteResponse);
				
				respuestaMessage.setMessage(quoteResponse);
				
				List<String> list = new ArrayList<String>();
				list.add("001");
				list.add("002");
				
				respuestaMessage.setListSessiones(list);
				
				//Session.sendToTarget(respuestaMessage.getMessage(), inicio.getSessionID1());
				
				return respuestaMessage;
//				return quoteResponse;
//			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;
	}
	
	public Message createZ(final SessionID sessionId, final String strQuoteId) throws SessionNotFound {

		System.out.println("******************DATOS RECIBIDOS PARA Z....\nSession: \t:" + sessionId + " - strQuoteId: \t" + strQuoteId);

		QuoteCancel quoteCancel = new QuoteCancel();
		Header header = (Header) quoteCancel.getHeader();
		header.setField(new BeginString(Constantes.PROTOCOL_FIX_VERSION)); // 8
		
		quoteCancel.setField(new QuoteCancelType(5));
		quoteCancel.setField(new QuoteID(strQuoteId));
		
		return quoteCancel;

	}

}
