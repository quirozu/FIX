package co.bvc.com.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import co.bvc.com.basicfix.BasicFunctions;
import co.bvc.com.basicfix.Constantes;
import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.dao.domain.RespuestaConstrucccionMsgFIX;
import quickfix.FieldNotFound;
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
import quickfix.field.SenderCompID;
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
	


	public RespuestaConstrucccionMsgFIX createR(ResultSet resultSet) throws SessionNotFound, SQLException, FieldNotFound {

		RespuestaConstrucccionMsgFIX respuestaMessage = new RespuestaConstrucccionMsgFIX();

		String queryParties = "SELECT linea.ID_ESCENARIO, partes.RQ_PARTYID, partes.RQ_PARTYIDSOURCE, partes.RQ_PARTYROLE, partes.RECEIVER_SESSION\r\n"
				+ "FROM aut_fix_rfq_datos linea INNER JOIN aut_fix_rfqparty_datos partes\r\n"
				+ "	ON linea.ID_CASESEQ = partes.RFQ_IDCASE\r\n" + "WHERE linea.ID_CASESEQ ="
				+ BasicFunctions.getIdCaseSeq();

		System.out.println("++++++++++++++++++++++++++++++++ ES ESTE +++++++++++++++++  "+ BasicFunctions.getIdCaseSeq());
		ResultSet resultSetParties;
		String cIdRandom = Integer.toString((int) ((Math.random() * 80_000_000) + 1_000_000));
		try {
			resultSetParties = DataAccess.getQuery(queryParties);

			QuoteReqID quoteReqID = new QuoteReqID(cIdRandom); // 131
			QuoteRequest quoteRequest = new QuoteRequest(quoteReqID); // 35 --> R
			Header header = (Header) quoteRequest.getHeader();
			header.setField(new BeginString(Constantes.PROTOCOL_FIX_VERSION)); // 8
			QuoteRequest.NoRelatedSym noRelatedSym = new QuoteRequest.NoRelatedSym();

			noRelatedSym.set(new Symbol(resultSet.getString("RQ_SYMBOL")));
			noRelatedSym.setField(new SecurityIDSource("M"));
			noRelatedSym.setField(new OrderQty(resultSet.getDouble("RQ_ORDERQTY")));
			noRelatedSym.setField(new StringField(54, resultSet.getString("RQ_SIDE")));
			noRelatedSym.setField(new SecuritySubType(resultSet.getString("RQ_SECSUBTYPE")));
			noRelatedSym.setField(new NoPartyIDs());

			QuoteRequest.NoRelatedSym.NoPartyIDs parte = new QuoteRequest.NoRelatedSym.NoPartyIDs();

			List<String> list = new ArrayList<String>();
			String idAfiliado = resultSet.getString(SenderCompID.FIELD);
			list.add(idAfiliado);
			// Parties
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
			if(noRelatedSym.getInt(NoPartyIDs.FIELD) == 1){
				System.out.println("\n\nPARA TODO EL MERCADO.....\n");
				list.clear();
				Iterator<String> it = Login.getMapSessiones().keySet().iterator();
				
				while(it.hasNext()){
				 idAfiliado = it.next();
				 list.add(idAfiliado);
				 System.out.println("Nuevo Afiliado: " + idAfiliado + " -> Session: " + Login.getMapSessiones().get(idAfiliado));
				}
				}
//			QuoteRequest.NoRelatedSym.NoPartyIDs partie2 = new QuoteRequest.NoRelatedSym.NoPartyIDs();
//			partie2.set(new PartyID("DCV"));
//			partie2.set(new PartyIDSource('C'));
//			partie2.set(new PartyRole(10));
//			
//			noRelatedSym.addGroup(partie2);

			quoteRequest.addGroup(noRelatedSym);

			respuestaMessage.setMessage(quoteRequest);
			respuestaMessage.setListSessiones(list);

			System.out.println("***************");
			System.out.println("** R CREADO  **");
			System.out.println(quoteRequest);
			System.out.println("***************");

			return respuestaMessage;

		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

	public RespuestaConstrucccionMsgFIX createS(ResultSet resultset, String strQuoteReqId) 
			throws SessionNotFound, SQLException {

		RespuestaConstrucccionMsgFIX respuestaMessage = new RespuestaConstrucccionMsgFIX();

		String queryParties = "SELECT linea.ID_ESCENARIO, partes.RQ_PARTYID, partes.RQ_PARTYIDSOURCE, partes.RQ_PARTYROLE, partes.RECEIVER_SESSION\r\n"
				+ "FROM aut_fix_rfq_datos linea INNER JOIN aut_fix_rfqparty_datos partes\r\n"
				+ "	ON linea.ID_CASESEQ = partes.RFQ_IDCASE\r\n" + "WHERE linea.ID_CASESEQ ="
				+ BasicFunctions.getIdCaseSeq();

		ResultSet resultSetParties;
		String cIdRandom = Integer.toString((int) ((Math.random() * 80_000_000) + 1_000_000));

		try {
			resultSetParties = DataAccess.getQuery(queryParties);

			QuoteID quoteID = new QuoteID(cIdRandom);
			Quote quote = new Quote(quoteID); // 35 --> S

			Header header = (Header) quote.getHeader();
			header.setField(new BeginString(Constantes.PROTOCOL_FIX_VERSION)); // 8

			quote.setField(new QuoteReqID(strQuoteReqId)); // 131
			quote.set(new Symbol(resultset.getString("RQ_SYMBOL")));
			quote.setField(new SecuritySubType(resultset.getString("RQ_SECSUBTYPE")));
			quote.setField(new OfferSize(resultset.getDouble("RQ_OFFERSIZE")));
			quote.setField(new OfferYield(resultset.getDouble("RQ_OFFERYIELD")));

			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.0");
			LocalDateTime dateTime = LocalDateTime.parse(resultset.getString("RQ_VALIDUNTILTIME"), formatter);
			quote.setField(new ValidUntilTime(dateTime)); // "20190404-23:00:00";

			// Parties
			Quote.NoPartyIDs parte = new Quote.NoPartyIDs();

			List<String> list = new ArrayList<String>();

			while (resultSetParties.next()) {
				String rSession = resultSetParties.getString("RECEIVER_SESSION");
				if (rSession != null) {
					list.add(rSession);
				}
				parte.set(new PartyID(resultSetParties.getString("RQ_PARTYID")));
				parte.set(new PartyIDSource('C'));
				parte.set(new PartyRole(resultSetParties.getInt("RQ_PARTYROLE")));

				quote.addGroup(parte);
			}

			respuestaMessage.setListSessiones(list);
			respuestaMessage.setMessage(quote);

			System.out.println("***************");
			System.out.println("** S CREADO  **");
			System.out.println(quote);
			System.out.println("***************");

			return respuestaMessage;

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;

	}

	public RespuestaConstrucccionMsgFIX createAJ(ResultSet resultset, String strQuoteId) throws SessionNotFound {

		RespuestaConstrucccionMsgFIX respuestaMessage = new RespuestaConstrucccionMsgFIX();
		String cIdRandom = Integer.toString((int) ((Math.random() * 80_000_000) + 1_000_000));

		try {
			QuoteRespID quoteRespID = new QuoteRespID(cIdRandom);
			QuoteRespType qouteRespType = new QuoteRespType(resultset.getInt("RQ_QUORESPTYPE"));
			QuoteResponse quoteResponse = new QuoteResponse(quoteRespID, qouteRespType); // 35 --> AJ

			Header header = (Header) quoteResponse.getHeader();
			header.setField(new BeginString(Constantes.PROTOCOL_FIX_VERSION)); // 8

			quoteResponse.setField(new QuoteID(strQuoteId));
			quoteResponse.setField(new StringField(54, resultset.getString("RQ_SIDE")));
			quoteResponse.set(new Symbol(resultset.getString("RQ_SYMBOL")));
			quoteResponse.setField(new StringField(762, resultset.getString("RQ_SECSUBTYPE")));

			System.out.println("NOS Message Sent : " + quoteResponse);

			respuestaMessage.setMessage(quoteResponse);

			List<String> list = new ArrayList<String>();
			list.add("001");
			list.add("002");

			respuestaMessage.setListSessiones(list);

			System.out.println("****************");
			System.out.println("** AJ CREADO  **");
			System.out.println(quoteResponse);
			System.out.println("****************");

			return respuestaMessage;
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return null;
	}

	public Message createZ(final SessionID sessionId, final String strQuoteId) throws SessionNotFound {

		System.out.println("******************DATOS RECIBIDOS PARA Z....\nSession: \t:" + sessionId
				+ " - strQuoteId: \t" + strQuoteId);

		QuoteCancel quoteCancel = new QuoteCancel();
		Header header = (Header) quoteCancel.getHeader();
		header.setField(new BeginString(Constantes.PROTOCOL_FIX_VERSION)); // 8

		quoteCancel.setField(new QuoteCancelType(5));
		quoteCancel.setField(new QuoteID(strQuoteId));

		return quoteCancel;

	}

}
