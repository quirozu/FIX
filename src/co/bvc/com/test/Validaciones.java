package co.bvc.com.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.dao.domain.AutFixRfqDatosCache;
import quickfix.FieldNotFound;
import quickfix.fix44.Message;

public class Validaciones {

	private String cadenaAI;
	private String CadenaSPrima;
	private String CadenaRPrima = "";
	
	DataAccess data = new DataAccess();
	private String cadenaOcho;

	public String getCadenaOcho() {
		return cadenaOcho;
	}

	public void setCadenaOcho(String cadenaOcho) {
		this.cadenaOcho = cadenaOcho;
	}

	public String getCadenaRPrima() {
		return CadenaRPrima;
	}

	public void setCadenaRPrima(String cadenaRPrima) {
		CadenaRPrima = cadenaRPrima;
	}

	public String getCadenaSPrima() {
		return CadenaSPrima;
	}

	public void setCadenaSPrima(String cadenaSPrima) {
		CadenaSPrima = cadenaSPrima;
	}

	public String getCadenaAI() {
		return cadenaAI;
	}

	public void setCadenaAI(String cadenaAI) {
		this.cadenaAI = cadenaAI;
	}

	public ArrayList<String> FragmentarCadena(String cadena) {
		ArrayList<String> claveValor = new ArrayList<String>();
		for (int i = 0; i < cadena.split("").length; i++) {
			claveValor.add(cadena.split("")[i]);
//                    System.out.println(claveValor.get(i));
		}
		return claveValor;
	}

	public ArrayList<String> FragmentarCadena1(String cadena) {
		ArrayList<String> claveValor1 = new ArrayList<String>();
		for (int i = 0; i < cadena.split("").length; i++) {
			claveValor1.add(cadena.split("")[i]);
//                    System.out.println(claveValor.get(i));
		}
		return claveValor1;
	}

	public void ValidarRPrima(AutFixRfqDatosCache datosCache, Message message)
			throws InterruptedException, SQLException, FieldNotFound {

		int contadorBuenos = 0;
		int contadorMalos = 0;
//		String cadenaPrima = "" + qr; 
		String cadenaPrima = message.toString();
		String clavePrima;
		ArrayList<String> cad = FragmentarCadena1(cadenaPrima);
		ResultSet resultset;
		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos " + "WHERE ID_CASESEQ = "
				+ datosCache.getIdCaseseq();
//		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos "
//				+ "WHERE ID_ESCENARIO = 'FIX_R' and ID_CASE = 1";

		resultset = DataAccess.getQuery(queryMessageR);
		String symbol = null, msgType = null, secSubTypec = null, side = null, orderQty = null, validuntiltime = null,
				norelatedSymg = null, idCase = null, beginString = "FIX.4.4", SenderCompID = "EXC", noPartyId = null,
				id_Escenario = null;
		int idSecuencia = 0;

		while (resultset.next()) {
			symbol = resultset.getString("RQ_SYMBOL");
			msgType = resultset.getString("RQ_MSGTYPE");
			secSubTypec = resultset.getString("RQ_SECSUBTYPE");
			side = resultset.getString("RQ_SIDE");
			orderQty = resultset.getString("RS_ORDERQTY");
			validuntiltime = resultset.getString("RQ_VALIDUNTILTIME");
			norelatedSymg = resultset.getString("RQ_NORELATEDSYM");
			idCase = resultset.getString("ID_CASE");
			idSecuencia = resultset.getInt("ID_SECUENCIA");
			noPartyId = resultset.getString("RQ_NOPARTYIDS");
			id_Escenario = resultset.getString("ID_ESCENARIO");
		}
		System.out.println("----------------------------------------");
		System.out.println("VALIDACION DE R CON R PRIMA\n");
		
		for (int z = 0; z < cad.size(); z++) {
			clavePrima = cad.get(z).split("=")[0];
			switch (clavePrima) {
			case "55":
				if (cad.get(z).split("=")[1].equals(symbol)) {

					contadorBuenos++;
					
					cadenaDeMensaje(" RQ_SYMBOL", clavePrima , symbol);

					DataAccess.cargarLogsExitosos(message, datosCache.getIdEjecucion(), symbol, cad.get(z).split("=")[1],
							id_Escenario, idCase, idSecuencia, clavePrima);
				} else {
					System.out.println(" RQ_SYMBOL (" + clavePrima + ") MSG: " +  cad.get(z).split("=")[1] + " BD " + symbol);
					contadorMalos++;
					DataAccess.cargarLogsFallidos(message, datosCache.getIdEjecucion(), symbol, cad.get(z).split("=")[1],
							id_Escenario, idCase, idSecuencia, clavePrima);
				}
				break;
			case "35":
				if (cad.get(z).split("=")[1].equals(msgType)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" RQ_MSGTYPE", clavePrima, msgType);

					DataAccess.cargarLogsExitosos(message, datosCache.getIdEjecucion(), msgType, cad.get(z).split("=")[1],
							id_Escenario, idCase, idSecuencia, cadenaPrima );
				} else {
					System.out.println(" RQ_MSGTYPE (" + clavePrima + ") MSG: " + cad.get(z).split("=")[1] + " BD " + msgType);
					DataAccess.cargarLogsFallidos(message, datosCache.getIdEjecucion(), msgType, cad.get(z).split("=")[1],
							id_Escenario, idCase, idSecuencia, clavePrima );
					contadorMalos++;
				}
				break;
			case "762":
				if (cad.get(z).split("=")[1].equals(secSubTypec)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" RQ_SECSUBTYPE", clavePrima, secSubTypec);

					DataAccess.cargarLogsExitosos(message, datosCache.getIdEjecucion(), secSubTypec, cad.get(z).split("=")[1], id_Escenario, idCase, idSecuencia, clavePrima);

				} else {
					System.out.println(" RQ_SECSUBTYPE (" + clavePrima + ") MSG: " + cad.get(z).split("=")[1] + " BD: " + secSubTypec);
					DataAccess.cargarLogsFallidos(message, datosCache.getIdEjecucion(), secSubTypec,
							cad.get(z).split("=")[1], id_Escenario, idCase, idSecuencia, clavePrima );
					contadorMalos++;
				}
				break;
			case "54":
				if (cad.get(z).split("=")[1].equals(side)) {
					contadorBuenos++;
					cadenaDeMensaje(" RQ_SIDE", clavePrima, side);

					DataAccess.cargarLogsExitosos(message, datosCache.getIdEjecucion(), side, cad.get(z).split("=")[1],
							id_Escenario, idCase, idSecuencia, clavePrima );
				} else {
					System.out
							.println("RQ_SIDE (" + clavePrima + ") MSG: " + cad.get(z).split("=")[1] + " BD: " + side);
					DataAccess.cargarLogsFallidos(message, datosCache.getIdEjecucion(), side, cad.get(z).split("=")[1],
							id_Escenario, idCase, idSecuencia, clavePrima);
					contadorMalos++;
				}
				break;
			case "38":
				if (cad.get(z).split("=")[1].equals(orderQty)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" RS_ORDERQTY", clavePrima, orderQty);

					DataAccess.cargarLogsExitosos(message, datosCache.getIdEjecucion(), orderQty, cad.get(z).split("=")[1],
							id_Escenario, idCase, idSecuencia, clavePrima);
				} else {
					System.out.println( "RS_ORDERQTY (" + clavePrima + ") MSG: " + cad.get(z).split("=")[1] + " BD: " + orderQty);
					
					DataAccess.cargarLogsFallidos(message, datosCache.getIdEjecucion(), orderQty, cad.get(z).split("=")[1],
							id_Escenario, idCase, idSecuencia, clavePrima );
					contadorMalos++;
				}
				break;
			case "146":
				if (cad.get(z).split("=")[1].equals(norelatedSymg)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" RQ_NORELATEDSYM", clavePrima, norelatedSymg);

					DataAccess.cargarLogsExitosos(message, datosCache.getIdEjecucion(), norelatedSymg,
							cad.get(z).split("=")[1], id_Escenario, idCase, idSecuencia, clavePrima);
				} else {
					System.out.println(" RQ_NORELATEDSYM(" + clavePrima + ") MSG: " + cad.get(z).split("=")[1] + " BD: " + norelatedSymg);
					DataAccess.cargarLogsFallidos(message, datosCache.getIdEjecucion(), norelatedSymg,
							cad.get(z).split("=")[1], id_Escenario, idCase, idSecuencia, clavePrima);
					contadorMalos++;
				}
				break;

			case "8":
				if (cad.get(z).split("=")[1].equals(beginString)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" CADENA PRIMA", clavePrima, beginString);

					DataAccess.cargarLogsExitosos(message, datosCache.getIdEjecucion(), beginString,
							cad.get(z).split("=")[1], id_Escenario, idCase, idSecuencia, clavePrima);
				} else {
					System.out.println(" CADENA PRIMA(" + clavePrima + ") MSG: " + cad.get(z).split("=")[1] + " BD: " + beginString);
					DataAccess.cargarLogsFallidos(message, datosCache.getIdEjecucion(), beginString,
							cad.get(z).split("=")[1], id_Escenario, idCase, idSecuencia, clavePrima );
					contadorMalos++;
				}
				break;
			case "453":
				if (cad.get(z).split("=")[1].equals(noPartyId)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" RQ_NOPARTYIDS", clavePrima, noPartyId);

					DataAccess.cargarLogsExitosos(message, datosCache.getIdEjecucion(), noPartyId, cad.get(z).split("=")[1],
							id_Escenario, idCase, idSecuencia, clavePrima);
				} else {
					System.out.println(" RQ_NOPARTYIDS(" + clavePrima + ") MSG: " + cad.get(z).split("=")[1] + " BD: " + noPartyId);
					DataAccess.cargarLogsFallidos(message, datosCache.getIdEjecucion(), noPartyId, cad.get(z).split("=")[1],
							id_Escenario, idCase, idSecuencia, clavePrima);
					contadorMalos++;
				}
				break;
			case "49":
				if (cad.get(z).split("=")[1].equals(SenderCompID)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" CADENA PRIMA ", clavePrima, SenderCompID);

					DataAccess.cargarLogsExitosos(message, datosCache.getIdEjecucion(), SenderCompID,
							cad.get(z).split("=")[1], id_Escenario, idCase, idSecuencia, clavePrima );
				} else {
					System.out.println(" CADENA PRIMA(" + clavePrima + ") MSG: " + cad.get(z).split("=")[1] + " BD: " + SenderCompID);
					DataAccess.cargarLogsFallidos(message, datosCache.getIdEjecucion(), SenderCompID,
							cad.get(z).split("=")[1], id_Escenario, idCase, idSecuencia, clavePrima);
					contadorMalos++;
				}
				break;

			default:

				break;
			}
		}
//		String de = "INSERT INTO logs_fix (100,"+h+","+i+","+"TIPO"+","+"ESTADO"+","+contadorBuenos+","+contadorMalos+","+(contadorBuenos + contadorMalos)+",PRUEVA)";
//		data.setQuery(de);
		System.out.println("----------------------------------------");
		System.out.println("LAS VALIDACIONES CORRECTAS FUERON : " + contadorBuenos);
		System.out.println("LAS VALIDACIONES ERRADAS FUERON : " + contadorMalos);
		System.out.println("TOTAL VALIDACIONES REALIZADAS : " + (contadorBuenos + contadorMalos));
	}

	public void ValidarSPrima(AutFixRfqDatosCache datosCache, quickfix.Message messageIn) throws InterruptedException, SQLException {
		int contadorBuenos = 0;
		int contadorMalos = 0;
		String cadenaPrima = "" + messageIn;
		String clavePrima;
		ArrayList<String> cad = FragmentarCadena1(cadenaPrima);
		ResultSet resultset;
		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos " + "WHERE ID_CASESEQ = "
				+ datosCache.getIdCaseseq();

		resultset = DataAccess.getQuery(queryMessageR);
		String  msgType = null, OfferSize = "500000000", secSubType = null, symbol = null,
				noPartyId = "5", SecurityIDSource = "M", SenderCompID = "EXC", beginString = "FIX.4.4", idCase = null,
				idEscenario = null;
		int idSecuencia = 0;

		while (resultset.next()) {
			msgType = resultset.getString("RQ_MSGTYPE");
			secSubType = resultset.getString("RQ_SECSUBTYPE");
			symbol = resultset.getString("RQ_SYMBOL");
			idCase = resultset.getString("ID_CASE");
			idSecuencia = resultset.getInt("ID_SECUENCIA");
			idEscenario = resultset.getString("ID_ESCENARIO");

		}
		System.out.println("----------------------------------------");
		System.out.println("VALIDACION DE S CON S PRIMA\n");
		for (int z = 0; z < cad.size(); z++) {
			clavePrima = cad.get(z).split("=")[0];
			switch (clavePrima) {

			case "55":
				if (cad.get(z).split("=")[1].equals(symbol)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" RQ_SYMBOL", clavePrima, symbol);
			
					DataAccess.cargarLogsExitosos(messageIn, datosCache.getIdEjecucion(), symbol, cad.get(z).split("=")[1],
							idEscenario, idCase, idSecuencia, clavePrima);
				} else {
					System.out.println(" RQ_SYMBOL (" + clavePrima + "): MSG" + cad.get(z).split("=")[1] + " BD: " + symbol);
					DataAccess.cargarLogsFallidos(messageIn, datosCache.getIdEjecucion(), symbol, cad.get(z).split("=")[1],
							idEscenario, idCase, idSecuencia, clavePrima);
					contadorMalos++;
				}
				break;
			case "762":
				if (cad.get(z).split("=")[1].equals(secSubType)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" RQ_SECSUBTYPE", clavePrima, secSubType);
					
					DataAccess.cargarLogsExitosos(messageIn, datosCache.getIdEjecucion(), secSubType, cad.get(z).split("=")[1],
							idEscenario, idCase, idSecuencia, clavePrima);
				} else {
					System.out.println(" RQ_SECSUBTYPE (" + clavePrima + "): MSG" +cad.get(z).split("=")[1] + " BD: " + secSubType);
					DataAccess.cargarLogsFallidos(messageIn, datosCache.getIdEjecucion(), secSubType, cad.get(z).split("=")[1],
							idEscenario, idCase, idSecuencia, clavePrima);
					contadorMalos++;
				}
				break;
			case "35":
				if (cad.get(z).split("=")[1].equals(msgType)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" RQ_MSGTYPE", clavePrima, msgType);
					
					DataAccess.cargarLogsExitosos(messageIn, datosCache.getIdEjecucion(), msgType, cad.get(z).split("=")[1],
							idEscenario, idCase, idSecuencia, clavePrima);
				} else {
					System.out.println(" RQ_MSGTYPE (" + clavePrima + "): MSG" + cad.get(z).split("=")[1] + " BD: " + msgType);
					DataAccess.cargarLogsFallidos(messageIn, datosCache.getIdEjecucion(), msgType, cad.get(z).split("=")[1],
							idEscenario, idCase, idSecuencia, clavePrima);
					contadorMalos++;
				}
				break;
			case "135":
				if (cad.get(z).split("=")[1].equals(OfferSize)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" OFFER SIZE", clavePrima, OfferSize);

					DataAccess.cargarLogsExitosos(messageIn, datosCache.getIdEjecucion(), OfferSize, cad.get(z).split("=")[1],
							idEscenario, idCase, idSecuencia, clavePrima);
				} else {
					System.out.println(" OFFER SIZE (" + clavePrima + "): MSG" +cad.get(z).split("=")[1] + " BD: " + OfferSize);
					DataAccess.cargarLogsFallidos(messageIn, datosCache.getIdEjecucion(), OfferSize, cad.get(z).split("=")[1],
							idEscenario, idCase, idSecuencia, clavePrima);
					contadorMalos++;
				}
				break;
			case "453":
				if (cad.get(z).split("=")[1].equals(noPartyId)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" NO PARTY ID", clavePrima, noPartyId);
					
					DataAccess.cargarLogsExitosos(messageIn, datosCache.getIdEjecucion(), noPartyId, cad.get(z).split("=")[1],
							idEscenario, idCase, idSecuencia, clavePrima);
				} else {
					System.out.println(" NO PARTY ID (" + clavePrima + "): MSG" + cad.get(z).split("=")[1] + " BD: " + noPartyId);
					DataAccess.cargarLogsFallidos(messageIn, datosCache.getIdEjecucion(), noPartyId, cad.get(z).split("=")[1],
							idEscenario, idCase, idSecuencia, clavePrima);
					contadorMalos++;
				}
				break;
			case "22":
				if (cad.get(z).split("=")[1].equals(SecurityIDSource)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" M ", clavePrima, SecurityIDSource);
					
					DataAccess.cargarLogsExitosos(messageIn, datosCache.getIdEjecucion(), SecurityIDSource,
							cad.get(z).split("=")[1], idEscenario, idCase, idSecuencia, clavePrima);
				} else {
					System.out.println(" M (" + clavePrima + "): MSG" + cad.get(z).split("=")[1] + " BD: "
							+ SecurityIDSource);
					contadorMalos++;
				}
				break;
			case "49":
				if (cad.get(z).split("=")[1].equals(SenderCompID)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" EXC ", clavePrima, SenderCompID);
					
					DataAccess.cargarLogsExitosos(messageIn, datosCache.getIdEjecucion(), SenderCompID,
							cad.get(z).split("=")[1], idEscenario, idCase, idSecuencia, clavePrima);
				} else {
					System.out.println(" EXC (" + clavePrima + "): MSG" + cad.get(z).split("=")[1] + " BD: " + SenderCompID);
					DataAccess.cargarLogsExitosos(messageIn, datosCache.getIdEjecucion(), SenderCompID,
							cad.get(z).split("=")[1], idEscenario, idCase, idSecuencia, clavePrima);
					
					contadorMalos++;
				}
				break;
			case "8":
				if (cad.get(z).split("=")[1].equals(beginString)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" FIX.4.4 ", clavePrima, beginString);
					
					DataAccess.cargarLogsExitosos(messageIn, datosCache.getIdEjecucion(), beginString,
							cad.get(z).split("=")[1], idEscenario, idCase, idSecuencia, clavePrima);
				} else {
					System.out.println(" FIX.4.4 (" + clavePrima + "): MSG" + cad.get(z).split("=")[1] + " BD: " + beginString);
					DataAccess.cargarLogsExitosos(messageIn, datosCache.getIdEjecucion(), beginString,
							cad.get(z).split("=")[1], idEscenario, idCase, idSecuencia, clavePrima);
					contadorMalos++;
				}
				break;

			default:
				break;
			}
		}
//		Thread.sleep(5000);
//		imput();
		System.out.println("----------------------------------------");
		System.out.println("LAS VALIDACIONES CORRECTAS FUERON : " + contadorBuenos);
		System.out.println("LAS VALIDACIONES ERRADAS FUERON : " + contadorMalos);
		System.out.println("TOTAL VALIDACIONES REALIZADAS : " + (contadorBuenos + contadorMalos));

	}

	public void validaAI(AutFixRfqDatosCache datosCache, Message qr) throws SQLException {
		int contadorBuenos = 0;
		int contadorMalos = 0;
		String cadena = "" + qr;
		ResultSet resultset;

		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos " + "WHERE ID_CASESEQ = "
				+ datosCache.getIdCaseseq();
		System.out.println("consulta: " + queryMessageR);

		resultset = DataAccess.getQuery(queryMessageR);
		ArrayList<String> cad = FragmentarCadena(cadena);
		String valor;
		String type = null, symbol = null, subtype = null, side = null, order = null, latedsym = null,
				idCase = null, idEscenario = null;
		int idSecuencia = 0;
		while (resultset.next()) {
			type = resultset.getString("RS_MSGTYPE");
			symbol = resultset.getString("RS_SYMBOL");
			subtype = resultset.getString("RS_SECSUBTYPE");
			side = resultset.getString("RS_SIDE");
			order = resultset.getString("RS_ORDERQTY");
			latedsym = resultset.getString("RS_NORELATEDSYM");
			idCase = resultset.getString("ID_CASE");
			idSecuencia = resultset.getInt("ID_SECUENCIA");
//			idEscenario = resultset.getString("ID_ESCENARIO");
			idEscenario = "FIX_AI";

		}
		System.out.println("----------------------------------------");
		System.out.println("VALIDACION DEL AI\n ");
		for (int i = 0; i < cad.size(); i++) {
			valor = cad.get(i).split("=")[0];
			switch (valor) {
			case "55":
				if (cad.get(i).split("=")[1].equals(symbol)) {
					contadorBuenos++;
					
					cadenaDeMensaje("RS_SYMBOL", valor, symbol);

					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), symbol, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					System.out.println("RS_SYMBOL (" + valor + "): MSG" + cad.get(i).split("=")[1] + " BD: " + symbol);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), symbol, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					contadorMalos++;
				}
				break;
			case "35":
				if (cad.get(i).split("=")[1].equals(type)) {
					contadorBuenos++;
					cadenaDeMensaje("RS_MSGTYPE", valor, type);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), type, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					System.out.println("RS_MSGTYPE (" + valor + "): MSG" + cad.get(i).split("=")[1] + " BD: " + type);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), type, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					contadorMalos++;
				}
				break;
			case "762":
				if (cad.get(i).split("=")[1].equals(subtype)) {
					contadorBuenos++;					
					cadenaDeMensaje("RS_SECSUBTYPE", valor, subtype);

					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), subtype, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					System.out.println("RS_SECSUBTYPE(" + valor + "): MSG" + cad.get(i).split("=")[1] + " BD: " + subtype);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), subtype, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					contadorMalos++;
				}
				break;
			case "54":
				if (cad.get(i).split("=")[1].equals(side)) {
					contadorBuenos++;
					
					cadenaDeMensaje("RS_SIDE", valor, side);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), side, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					System.out.println("RS_SIDE (" + valor + "): MSG" + cad.get(i).split("=")[1] + " BD: " + side);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), side, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					contadorMalos++;
				}
				break;
			case "38":
				if (cad.get(i).split("=")[1].equals(order)) {
					contadorBuenos++;
					cadenaDeMensaje("RS_ORDERQTY", valor, order);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), order, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					System.out.println("RS_ORDERQTY (" + valor + "): MSG" + cad.get(i).split("=")[1] + " BD: " + order);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), order, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					contadorMalos++;
				}
				break;

			case "694":
				if (cad.get(i).split("=")[1].equals(latedsym)) {
					contadorBuenos++;
					cadenaDeMensaje("RS_NORELATEDSYM", valor, latedsym);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), latedsym, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					System.out.println("RS_NORELATEDSYM (" + valor + "): MSG" + cad.get(i).split("=")[1] + " BD: " + latedsym);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), latedsym, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					contadorMalos++;
				}
				break;

			default:
				break;
			}
		}
		System.out.println("----------------------------------------");
		System.out.println("LAS VALIDACIONES CORRECTAS FUERON : " + contadorBuenos);
		System.out.println("LAS VALIDACIONES ERRADAS FUERON : " + contadorMalos);
		System.out.println("TOTAL VALIDACIONES REALIZADAS : " + (contadorBuenos + contadorMalos));

	}

	public void validarOcho(AutFixRfqDatosCache datosCache, Message qr) throws SQLException {
		int contadorBuenos = 0;
		int contadorMalos = 0;
		String cadena = "" + qr;
		ResultSet resultset;

		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos " + "WHERE ID_CASESEQ = "
				+ datosCache.getIdCaseseq();

		resultset = DataAccess.getQuery(queryMessageR);

		ArrayList<String> cad = FragmentarCadena(cadena);
		String valor;
		String execType = null, orderStatus = null, side = null, leaveQty = null, orderQty = null, price = null,
				grosstradeamt = null, nopartyIds = null, secSubtype = null, reforderIdsCr = null, symbol = null,
				senderSubId = null, dirtyPrice = null, partyIdsSource = null, idCase = null, idEscenario = null;
		int idSecuencia = 0;
		while (resultset.next()) {

//			switch(resultset.getInt(columnIndex))
			execType = resultset.getString("ER_EXECTYPE");
			orderStatus = resultset.getString("ER_ORDSTATUS");
			side = resultset.getString("ER_SIDE");
			leaveQty = resultset.getString("ER_LEAVEQTY");
			orderQty = resultset.getString("ER_ORDERQTY");
			price = resultset.getString("ER_PRICE");
			grosstradeamt = resultset.getString("ER_GROSSTRADEAMT");
			nopartyIds = resultset.getString("ER_NOPARTYIDS");
			secSubtype = resultset.getString("ER_SECSUBTYPE");
			reforderIdsCr = resultset.getString("ER_REFORDERIDSCR");
			symbol = resultset.getString("ER_SYMBOL");
			senderSubId = resultset.getString("ER_SENDERSUBID");
			dirtyPrice = resultset.getString("ER_DIRTYPRICE");
			partyIdsSource = resultset.getString("ER_PARTYIDSOURCE");
			idCase = resultset.getString("ID_CASE");
			idSecuencia = resultset.getInt("ID_SECUENCIA");
			idEscenario = "FIX_8";

		}
		System.out.println("----------------------------------------");
		System.out.println(" VALIDACION DEL EXECUTION REPORT\n ");

		for (int i = 0; i < cad.size(); i++) {
			valor = cad.get(i).split("=")[0];
			switch (valor) {
			case "150":
				if (cad.get(i).split("=")[1].equals(execType)) {
					contadorBuenos++;
					
					cadenaDeMensaje("ER_EXECTYPE", valor, execType);
//					System.out.println(
//							"iguales:  Execution Report(150): " + cad.get(i).split("=")[1] + " bd: " + execType);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), execType, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					System.out.println(" ER_EXECTYPE (" + valor + "): MSG" +cad.get(i).split("=")[1] + " bd: " + execType);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), execType, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					contadorMalos++;
				}
				break;
			case "39":
				if (cad.get(i).split("=")[1].equals(orderStatus)) {
					contadorBuenos++;
					
					cadenaDeMensaje("ER_ORDSTATUS", valor, orderStatus);
//					System.out.println(
//							"iguales:  Execution Report(39): " + cad.get(i).split("=")[1] + " bd: " + orderStatus);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), orderStatus,
							cad.get(i).split("=")[1], idEscenario, idCase, idSecuencia, valor);
				} else {
					System.out.println(" ER_ORDSTATUS (" + valor + "): MSG" + cad.get(i).split("=")[1] + " BD: " + orderStatus);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), orderStatus,
							cad.get(i).split("=")[1], idEscenario, idCase, idSecuencia, valor);
					contadorMalos++;
				}
				break;
			case "54":
				if (cad.get(i).split("=")[1].equals(side)) {
					contadorBuenos++;
					
					cadenaDeMensaje("ER_SIDE", valor, side);
					
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), side, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					System.out.println(" ER_SIDE (" + valor + "): MSG" + cad.get(i).split("=")[1] + " BD: " + side);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), side, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					contadorMalos++;
				}
				break;
			case "151":
				if (cad.get(i).split("=")[1].equals(leaveQty)) {
					contadorBuenos++;
					
					cadenaDeMensaje("ER_LEAVEQTY", valor, leaveQty);
					
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), leaveQty, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					System.out.println(" ER_LEAVEQTY (" + valor + "): MSG" +cad.get(i).split("=")[1] + " BD: " + leaveQty);
					contadorMalos++;
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), leaveQty, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				}
				break;
			case "38":
				if (cad.get(i).split("=")[1].equals(orderQty)) {
					contadorBuenos++;
					
					cadenaDeMensaje("ER_ORDERQTY", valor, orderQty);
					
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), orderQty, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					System.out.println(" ER_ORDERQTY (" + valor + "): MSG" +  cad.get(i).split("=")[1] + " bd: " + orderQty);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), orderQty, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					contadorMalos++;
				}
				break;
			case "44":
				if (cad.get(i).split("=")[1].equals(price)) {
					contadorBuenos++;
					
					cadenaDeMensaje("ER_PRICE", valor, price);
		
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), price, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), price, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					System.out.println(" ER_PRICE (" + valor + "): MSG" + cad.get(i).split("=")[1] + " bd: " + price);
					contadorMalos++;
				}
				break;
			case "387":
				if (cad.get(i).split("=")[1].equals(grosstradeamt)) {
					contadorBuenos++;
					
					cadenaDeMensaje("ER_GROSSTRADEAMT", valor, grosstradeamt);
					
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), grosstradeamt,
							cad.get(i).split("=")[1], idEscenario, idCase, idSecuencia, valor);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), grosstradeamt,
							cad.get(i).split("=")[1], idEscenario, idCase, idSecuencia, valor);
					System.out.println(" ER_GROSSTRADEAMT(" + valor + "): MSG" + cad.get(i).split("=")[1] + " BD: "
							+ grosstradeamt);
					contadorMalos++;
				}
				break;
			case "453":
				if (cad.get(i).split("=")[1].equals(nopartyIds)) {
					contadorBuenos++;
					
					cadenaDeMensaje("ER_NOPARTYIDS", valor, nopartyIds);
					
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), nopartyIds, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), nopartyIds, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					System.out.println(" ER_NOPARTYIDS (" + valor + "): MSG" + cad.get(i).split("=")[1] + " BD: " + nopartyIds);
					contadorMalos++;
				}
				break;
			case "762":
				if (cad.get(i).split("=")[1].equals(secSubtype)) {
					contadorBuenos++;
					
					cadenaDeMensaje("ER_SECSUBTYPE", valor, secSubtype);
					
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), secSubtype, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), secSubtype, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					System.out.println(" ER_SECSUBTYPE (" + valor + "): MSG" + cad.get(i).split("=")[1] + " BD: " + secSubtype);
					contadorMalos++;
				}
				break;
			case "1081":
				if (cad.get(i).split("=")[1].equals(reforderIdsCr)) {
					contadorBuenos++;
					
					cadenaDeMensaje("ER_REFORDERIDSCR", valor, reforderIdsCr);
				
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), reforderIdsCr,
							cad.get(i).split("=")[1], idEscenario, idCase, idSecuencia, valor);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), reforderIdsCr,
							cad.get(i).split("=")[1], idEscenario, idCase, idSecuencia, valor);
					System.out.println(" ER_REFORDERIDSCR (" + valor + "): MSG" +cad.get(i).split("=")[1] + " BD: "
							+ reforderIdsCr);
					contadorMalos++;
				}
				break;
			case "55":
				if (cad.get(i).split("=")[1].equals(symbol)) {
					contadorBuenos++;
					
					cadenaDeMensaje("ER_SYMBOL", valor, symbol);

					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), symbol, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), symbol, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					System.out.println(" ER_SYMBOL (" + valor + "): MSG" + cad.get(i).split("=")[1] + " BD: " + symbol);
					contadorMalos++;
				}
				break;
			case "50":
				if (cad.get(i).split("=")[1].equals(senderSubId)) {
					contadorBuenos++;

					cadenaDeMensaje("ER_SENDERSUBID", valor, senderSubId);

					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), senderSubId,
							cad.get(i).split("=")[1], idEscenario, idCase, idSecuencia, valor);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), senderSubId,
							cad.get(i).split("=")[1], idEscenario, idCase, idSecuencia, valor);
					System.out.println(" ER_SENDERSUBID (" + valor + "): MSG" +cad.get(i).split("=")[1] + " BD: " + senderSubId);
					contadorMalos++;
				}
				break;
			case "20102":
				if (cad.get(i).split("=")[1].equals(dirtyPrice)) {
					contadorBuenos++;
					
					cadenaDeMensaje("ER_DIRTYPRICE", valor, dirtyPrice);

					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), dirtyPrice, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), dirtyPrice, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					System.out.println(" ER_DIRTYPRICE (" + valor + "): MSG" + cad.get(i).split("=")[1] + " BD: " + dirtyPrice);
					contadorMalos++;
				}
				break;
			case "447":
				if (cad.get(i).split("=")[1].equals(partyIdsSource)) {
					contadorBuenos++;
					
					cadenaDeMensaje("ER_PARTYIDSOURCE", valor, partyIdsSource); 
					
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), partyIdsSource,
							cad.get(i).split("=")[1], idEscenario, idCase, idSecuencia, valor);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), partyIdsSource,
							cad.get(i).split("=")[1], idEscenario, idCase, idSecuencia, valor);
					System.out.println(" ER_PARTYIDSOURCE (" + valor + "): MSG" +cad.get(i).split("=")[1] + " BD: "
							+ partyIdsSource);
					contadorMalos++;
				}
				break;

			default:
				break;
			}
		}
		
		System.out.println("----------------------------------------");
		System.out.println("---------------------");
		System.out.println("-- VALIDACIONES ER --");
		System.out.println("LAS VALIDACIONES CORRECTAS FUERON : " + contadorBuenos);
		System.out.println("LAS VALIDACIONES ERRADAS FUERON : " + contadorMalos);
		System.out.println("TOTAL VALIDACIONES REALIZADAS : " + (contadorBuenos + contadorMalos));

	}
	
	public void validarZ(AutFixRfqDatosCache datosCache, Message qr) throws SQLException {

		int contadorBuenos = 0;
		int contadorMalos = 0;
		String cadena = "" + qr;
		ResultSet resultset;

		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos " + "WHERE ID_CASESEQ = "
				+ datosCache.getIdCaseseq();

		resultset = DataAccess.getQuery(queryMessageR);

		ArrayList<String> cad = FragmentarCadena(cadena);
		String valor;
		String quoteId = null, quoteCancelType = null, idCase = null, idEscenario = null;
		int idSecuencia = 0;
		while (resultset.next()) {

//			switch(resultset.getInt(columnIndex))
			quoteId = resultset.getString("RQ_QUOTEID");
			quoteCancelType = resultset.getString("RQ_QUOCANCTYPE");
			idEscenario = "FIX_8";

		}
		System.out.println("----------------------------------------");
		System.out.println("VALIDACION DEL Z\n ");

		for (int i = 0; i < cad.size(); i++) {
			valor = cad.get(i).split("=")[0];
			switch (valor) {
			case "117":
				if (cad.get(i).split("=")[1].equals(quoteId)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" RQ_QUOTEID", valor, quoteId); 
					
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), quoteId, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					System.out.println(" RQ_QUOTEID (" + valor + "): MSG" + cad.get(i).split("=")[1] + " BD: " + quoteId);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), quoteId, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					contadorMalos++;
				}
				break;
			case "298":
				if (cad.get(i).split("=")[1].equals(quoteCancelType)) {
					contadorBuenos++;
					
					cadenaDeMensaje(" RQ_QUOCANCTYPE", valor, quoteCancelType); 

					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), quoteCancelType, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
				} else {
					System.out
							.println(" RQ_QUOCANCTYPE (" + valor + "): MSG" +  cad.get(i).split("=")[1] + " BD: " + quoteCancelType);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), quoteCancelType, cad.get(i).split("=")[1],
							idEscenario, idCase, idSecuencia, valor);
					contadorMalos++;
				}
				break;

			default:
				break;
			}
		}
		
		System.out.println("----------------------------------------");
		System.out.println("---------------------");
		System.out.println("-- VALIDACIONES Z --");
		System.out.println("LAS VALIDACIONES CORRECTAS FUERON : " + contadorBuenos);
		System.out.println("LAS VALIDACIONES ERRADAS FUERON : " + contadorMalos);
		System.out.println("TOTAL VALIDACIONES REALIZADAS : " + (contadorBuenos + contadorMalos));

	}
	
	public void cadenaDeMensaje(String columna, String valor, String comparar ){
	
		System.out.println(columna +"("+ valor + "): " + comparar);
	}
}
