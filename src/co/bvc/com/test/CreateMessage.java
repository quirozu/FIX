package co.bvc.com.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import co.bvc.com.basicfix.DataAccess;
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

	public Message createR(SessionID sessionId, String cid) throws SessionNotFound, SQLException {

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
			header.setField(new BeginString("FIX.4.4")); // 8
			QuoteRequest.NoRelatedSym noRelatedSym = new QuoteRequest.NoRelatedSym();

			while (resultset.next()) {
				noRelatedSym.set(new Symbol(resultset.getString("RQ_SYMBOL")));
				noRelatedSym.setField(new SecurityIDSource("M"));
				noRelatedSym.setField(new OrderQty(resultset.getDouble("RQ_ORDERQTY")));
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
			header.setField(new BeginString("FIX.4.4")); // 8
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

	public Message createS(final SessionID sessionId, final String strQuoteId, final String strQReqId)
			throws SessionNotFound, SQLException {

		System.out.println("******************DATOS RECIBIDOS PARA S....\nSession: \t:" + sessionId
				+ " - strQuoteId: \t" + strQuoteId + " - strReqId: " + strQReqId);

		String queryMessageS = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos "
				+ "WHERE ID_ESCENARIO = 'FIX_S' and ID_CASE = 1";

		String queryParties = "SELECT linea.ID_ESCENARIO, partes.RQ_PARTYID, partes.RQ_PARTYIDSOURCE, partes.RQ_PARTYROLE\r\n"
				+ "FROM aut_fix_rfq_datos linea INNER JOIN aut_fix_rfqparty_datos partes\r\n"
				+ "	ON linea.ID_CASESEQ = partes.RFQ_IDCASE\r\n" + "WHERE linea.ID_CASESEQ = 2";
		// -------------------

		ResultSet resultset;
		ResultSet resultSetParties;

		try {
			resultset = DataAccess.getQuery(queryMessageS);
			resultSetParties = DataAccess.getQuery(queryParties);

			QuoteID quoteID = new QuoteID(strQuoteId);
			Quote quote = new Quote(quoteID); // 35 --> S

			Header header = (Header) quote.getHeader();
			header.setField(new BeginString("FIX.4.4")); // 8

			quote.setField(new QuoteReqID(strQReqId)); // 131

			while (resultset.next()) {
				quote.set(new Symbol(resultset.getString("RQ_SYMBOL")));
				quote.setField(new SecuritySubType(resultset.getString("RQ_SECSUBTYPE")));
				quote.setField(new OfferSize(resultset.getDouble("RQ_OFFERSIZE")));
				quote.setField(new OfferYield(resultset.getDouble("RQ_OFFERYIELD")));

				DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");
				LocalDateTime dateTime = LocalDateTime.parse(resultset.getString("RQ_VALIDUNTILTIME"), formatter);
				quote.setField(new ValidUntilTime(dateTime)); // "20190404-23:00:00";
			}

			// Parties
			Quote.NoPartyIDs parte = new Quote.NoPartyIDs();

			while (resultSetParties.next()) {
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

			return quote;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}



	public static Message createAJ(SessionID sessionId, String strQuoteId, String strQRespId) throws SessionNotFound {

		String queryMessageAJ = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos WHERE ID_ESCENARIO = 'FIX_AJ' AND ID_CASE = 1";
		int stQt = Integer.parseInt(strQuoteId);
		stQt += 1;
		String strQuotedID = Integer.toString(stQt);
		ResultSet resultset;
		try {
			resultset = DataAccess.getQuery(queryMessageAJ);
			while (resultset.next()) {
				QuoteRespID quoteRespID = new QuoteRespID(strQuotedID);
				QuoteRespType qouteRespType = new QuoteRespType(1);
				QuoteResponse quoteResponse = new QuoteResponse(quoteRespID, qouteRespType); // 35 --> AJ

				Header header = (Header) quoteResponse.getHeader();
				header.setField(new BeginString("FIX.4.4")); // 8

				quoteResponse.setField(new QuoteID(strQRespId));
//				quoteResponse.setField(new StringField(49, resultset.getString("ID_AFILIADO")));
				quoteResponse.setField(new StringField(54, resultset.getString("RQ_SIDE")));
				quoteResponse.setField(new Symbol(resultset.getString("RQ_SYMBOL")));
				quoteResponse.setField(new StringField(762, resultset.getString("RQ_SECSUBTYPE")));
//				quoteResponse.setField(new StringField(50, resultset.getString("RQ_TRADER")));
//				quoteResponse.setField(new StringField(54, resultset.getString("RQ_RELATEDID")));//?????
//				quoteResponse.setField(new StringField(694, resultset.getString("RQ_NORELATEDSYM")));

				boolean sent = Session.sendToTarget(quoteResponse, sessionId);
//				System.out.println("NOS Message Sent : " + sent);
				return quoteResponse;
			}
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
		header.setField(new BeginString("FIX.4.4")); // 8
		
		quoteCancel.setField(new QuoteCancelType(5));
		quoteCancel.setField(new QuoteID(strQuoteId));
		
		return quoteCancel;

	}

}
