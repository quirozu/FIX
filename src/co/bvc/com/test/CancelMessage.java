package co.bvc.com.test;

import quickfix.Session;
import quickfix.SessionID;
import quickfix.SessionNotFound;
import quickfix.field.BeginString;
import quickfix.field.QuoteCancelType;
import quickfix.field.QuoteID;
import quickfix.fix44.Message;
import quickfix.fix44.QuoteCancel;
import quickfix.fix44.Message.Header;

public class CancelMessage {
	
	private static Message sendZ(SessionID sessionId, String strQuoteID) throws SessionNotFound {

		

		QuoteID quoteID = new QuoteID(strQuoteID);
		QuoteCancelType quoteCancelType = new QuoteCancelType(5);

		QuoteCancel quoteCancel = new QuoteCancel(quoteID, quoteCancelType); // 35 --> Z

		Header header = (Header) quoteCancel.getHeader();
		header.setField(new BeginString("FIX.4.4")); // 8

		boolean sent = Session.sendToTarget(quoteCancel, sessionId);
		System.out.println("NOS Message Sent : " + sent);
		
		return quoteCancel;
	}

}
