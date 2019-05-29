package co.bvc.com.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.dao.domain.AutFixRfqDatosCache;
import quickfix.FieldNotFound;
import quickfix.Message;

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
		}
		return claveValor;
	}

	public ArrayList<String> FragmentarCadena1(String cadena) {
		ArrayList<String> claveValor1 = new ArrayList<String>();
		for (int i = 0; i < cadena.split("").length; i++) {
			claveValor1.add(cadena.split("")[i]);
		}
		return claveValor1;
	}

	public void ValidarRPrima(AutFixRfqDatosCache datosCache, Message qr)
	
	      
			throws InterruptedException, SQLException, FieldNotFound {
		int contadorBuenos = 0;
		int contadorMalos = 0;
		String cadenaPrima = "" + qr;
		String clavePrima;
		String valorPrima;
		ArrayList<String> cad = FragmentarCadena1(cadenaPrima);
		ResultSet resultset;
		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos " + "WHERE ID_CASESEQ = "
				+ datosCache.getIdCaseseq();

		resultset = DataAccess.getQuery(queryMessageR);
		String symbol = null, msgType = null, secSubTypec = null, side = null, orderQty = null, validuntiltime = null,
				norelatedSymg = null, idCase = null, beginString = "FIX.4.4", SenderCompID = "EXC", noPartyId = "3",
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
		System.out.println("VALIDACION DE R CON R PRIMA");
		System.out.println("  \n");
		for (int z = 0; z < cad.size(); z++) {
			clavePrima = cad.get(z).split("=")[0];
			valorPrima = cad.get(z).split("=")[1];
			switch (clavePrima) {
			case "55":
				if (cad.get(z).split("=")[1].equals(symbol)) {

					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(55): " + cad.get(z).split("=")[1] + " cadenaR " + symbol);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), symbol, cad.get(z).split("=")[1], id_Escenario,
							idCase, idSecuencia);
				} else {
					System.out.println(
							"diferentes:  cadenaPrima(55): " + cad.get(z).split("=")[1] + " cadenaR " + symbol);
					contadorMalos++;
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), symbol, cad.get(z).split("=")[1], id_Escenario,
							idCase, idSecuencia);
				}
				break;
			case "35":
				if (cad.get(z).split("=")[1].equals(msgType)) {
					contadorBuenos++;
					System.out
							.println("iguales:  cadenaPrima(35): " + cad.get(z).split("=")[1] + " cadenaR " + msgType);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), msgType, cad.get(z).split("=")[1], id_Escenario,
							idCase, idSecuencia);
				} else {
					System.out.println(
							"diferentes:  cadenaPrima(35): " + cad.get(z).split("=")[1] + " cadenaR " + msgType);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), msgType, cad.get(z).split("=")[1], id_Escenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "762":
				if (cad.get(z).split("=")[1].equals(secSubTypec)) {
					contadorBuenos++;
					System.out.println(
							"iguales:  cadenaPrima(762): " + cad.get(z).split("=")[1] + " cadenaR " + secSubTypec);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), secSubTypec, cad.get(z).split("=")[1], id_Escenario,
							idCase, idSecuencia);

				} else {
					System.out.println(
							"diferentes:  cadenaPrima(762): " + cad.get(z).split("=")[1] + " cadenaR " + secSubTypec);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), secSubTypec, cad.get(z).split("=")[1], id_Escenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "54":
				if (cad.get(z).split("=")[1].equals(side)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(54): " + cad.get(z).split("=")[1] + " cadenaR " + side);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), side, cad.get(z).split("=")[1], id_Escenario,
							idCase, idSecuencia);
				} else {
					System.out
							.println("diferentes:  cadenaPrima(54): " + cad.get(z).split("=")[1] + " cadenaR " + side);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), side, cad.get(z).split("=")[1], id_Escenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "38":
				if (cad.get(z).split("=")[1].equals(orderQty)) {
					contadorBuenos++;
					System.out
							.println("iguales:  cadenaPrima(38): " + cad.get(z).split("=")[1] + " cadenaR " + orderQty);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), orderQty, cad.get(z).split("=")[1], id_Escenario,
							idCase, idSecuencia);
				} else {
					System.out.println(
							"diferentes:  cadenaPrima(38): " + cad.get(z).split("=")[1] + " cadenaR " + orderQty);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), orderQty, cad.get(z).split("=")[1], id_Escenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;

			case "146":
				if (cad.get(z).split("=")[1].equals(norelatedSymg)) {
					contadorBuenos++;
					System.out.println(
							"iguales:  cadenaPrima(146): " + cad.get(z).split("=")[1] + " cadenaR " + norelatedSymg);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), norelatedSymg, cad.get(z).split("=")[1],
							id_Escenario, idCase, idSecuencia);
				} else {
					System.out.println(
							"diferentes:  cadenaPrima(146): " + cad.get(z).split("=")[1] + " cadenaR " + norelatedSymg);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), norelatedSymg, cad.get(z).split("=")[1],
							id_Escenario, idCase, idSecuencia);
					contadorMalos++;
				}
				break;

			case "8":
				if (cad.get(z).split("=")[1].equals(beginString)) {
					contadorBuenos++;
					System.out
							.println("iguales:  cadenaPrima(146): " + cad.get(z).split("=")[1] + " cadenaR " + beginString);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), beginString, cad.get(z).split("=")[1], id_Escenario,
							idCase, idSecuencia);
				} else {
					System.out.println(
							"diferentes:  cadenaPrima(146): " + cad.get(z).split("=")[1] + " cadenaR " + beginString);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), beginString, cad.get(z).split("=")[1], id_Escenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "453":
				if (cad.get(z).split("=")[1].equals(noPartyId)) {
					contadorBuenos++;
					System.out.println(
							"iguales:  cadenaPrima(146): " + cad.get(z).split("=")[1] + " cadenaR " + noPartyId);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), noPartyId, cad.get(z).split("=")[1], id_Escenario,
							idCase, idSecuencia);
				} else {
					System.out.println(
							"diferentes:  cadenaPrima(146): " + cad.get(z).split("=")[1] + " cadenaR " + noPartyId);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), noPartyId, cad.get(z).split("=")[1], id_Escenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "49":
				if (cad.get(z).split("=")[1].equals(SenderCompID)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(146): " + cad.get(z).split("=")[1] + " cadenaR " + SenderCompID);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), SenderCompID, cad.get(z).split("=")[1], id_Escenario, idCase,
							idSecuencia);
				} else {
					System.out.println("diferentes:  cadenaPrima(146): " + cad.get(z).split("=")[1] + " cadenaR " + SenderCompID);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), SenderCompID, cad.get(z).split("=")[1], id_Escenario, idCase,
							idSecuencia);
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

	public void ValidarSPrima(AutFixRfqDatosCache datosCache, Message qr)
			throws InterruptedException, SQLException {
		int contadorBuenos = 0;
		int contadorMalos = 0;
		String cadenaPrima = "" + qr;
		String clavePrima;
		String valorPrima;
		ArrayList<String> cad = FragmentarCadena1(cadenaPrima);
		ResultSet resultset;
		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos " + "WHERE ID_CASESEQ = "
				+ datosCache.getIdCaseseq();

		resultset = DataAccess.getQuery(queryMessageR);
		String validuntiltime = null, msgType = null, OfferSize = "500000000", secSubType = null, symbol = null,
				noPartyId = "5", SecurityIDSource = "M", SenderCompID = "EXC", beginString = "FIX.4.4", idCase = null,
				idEscenario = null;
		int idSecuencia = 0;

		while (resultset.next()) {
			validuntiltime = resultset.getString("RQ_VALIDUNTILTIME");
			msgType = resultset.getString("RQ_MSGTYPE");
			secSubType = resultset.getString("RQ_SECSUBTYPE");
			symbol = resultset.getString("RQ_SYMBOL");
			idCase = resultset.getString("ID_CASE");
			idSecuencia = resultset.getInt("ID_SECUENCIA");
			idEscenario = resultset.getString("ID_ESCENARIO");

		}
		System.out.println("----------------------------------------");
		System.out.println("VALIDACION DE S CON S PRIMA");
		System.out.println("  \n");
		for (int z = 0; z < cad.size(); z++) {
			clavePrima = cad.get(z).split("=")[0];
			valorPrima = cad.get(z).split("=")[1];
			switch (clavePrima) {

			case "55":
				if (cad.get(z).split("=")[1].equals(symbol)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(52): " + cad.get(z).split("=")[1] + " cadenaS " + symbol);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), symbol, cad.get(z).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println(
							"diferentes:  cadenaPrima(52): " + cad.get(z).split("=")[1] + " cadenaS " + symbol);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), symbol, cad.get(z).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "762":
				if (cad.get(z).split("=")[1].equals(secSubType)) {
					contadorBuenos++;
					System.out.println(
							"iguales:  cadenaPrima(52): " + cad.get(z).split("=")[1] + " cadenaS " + secSubType);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), secSubType, cad.get(z).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println(
							"diferentes:  cadenaPrima(52): " + cad.get(z).split("=")[1] + " cadenaS " + secSubType);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), secSubType, cad.get(z).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "35":
				if (cad.get(z).split("=")[1].equals(msgType)) {
					contadorBuenos++;
					System.out
							.println("iguales:  cadenaPrima(35): " + cad.get(z).split("=")[1] + " cadenaS " + msgType);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), msgType, cad.get(z).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println(
							"diferentes:  cadenaPrima(35): " + cad.get(z).split("=")[1] + " cadenaS " + msgType);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), msgType, cad.get(z).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "135":
				if (cad.get(z).split("=")[1].equals(OfferSize)) {
					contadorBuenos++;
					System.out.println(
							"iguales:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + OfferSize);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), OfferSize, cad.get(z).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println(
							"diferentes:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + OfferSize);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), OfferSize, cad.get(z).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "453":
				if (cad.get(z).split("=")[1].equals(noPartyId)) {
					contadorBuenos++;
					System.out.println(
							"iguales:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + noPartyId);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), noPartyId, cad.get(z).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println(
							"diferentes:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + noPartyId);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), noPartyId, cad.get(z).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "22":
				if (cad.get(z).split("=")[1].equals(SecurityIDSource)) {
					contadorBuenos++;
					System.out.println(
							"iguales:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + SecurityIDSource);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), SecurityIDSource, cad.get(z).split("=")[1],
							idEscenario, idCase, idSecuencia);
				} else {
					System.out.println("diferentes:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS "
							+ SecurityIDSource);
					contadorMalos++;
				}
				break;
			case "49":
				if (cad.get(z).split("=")[1].equals(SenderCompID)) {
					contadorBuenos++;
					System.out.println(
							"iguales:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + SenderCompID);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), SenderCompID, cad.get(z).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println(
							"diferentes:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + SenderCompID);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), SenderCompID, cad.get(z).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "8":
				if (cad.get(z).split("=")[1].equals(beginString)) {
					contadorBuenos++;
					System.out.println(
							"iguales:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + beginString);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), beginString, cad.get(z).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println(
							"diferentes:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + beginString);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), beginString, cad.get(z).split("=")[1], idEscenario,
							idCase, idSecuencia);
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

	public void ValidaAI(AutFixRfqDatosCache datosCache, Message qr) throws SQLException {
		int contadorBuenos = 0;
		int contadorMalos = 0;
		String cadena = "" + qr;
		ResultSet resultset;

		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos " + "WHERE ID_CASESEQ = "
				+ datosCache.getIdCaseseq();

		resultset = DataAccess.getQuery(queryMessageR);
		ArrayList<String> cad = FragmentarCadena(cadena);
		String valor;
		String val;
		String type = null, symbol = null, subtype = null, side = null, order = null, time = null, latedsym = null,idCase=null,idEscenario=null,
				Nopartys = null;
		int idSecuencia=0;
		while (resultset.next()) {
			type = resultset.getString("RS_MSGTYPE");
			symbol = resultset.getString("RS_SYMBOL");
			subtype = resultset.getString("RS_SECSUBTYPE");
			side = resultset.getString("RS_SIDE");
			order = resultset.getString("RS_ORDERQTY");
			time = resultset.getString("RS_VALIDUNTILTIME");
			latedsym = resultset.getString("RS_NORELATEDSYM");
			Nopartys = resultset.getString("RS_NOPARTYIDS");
			idCase = resultset.getString("ID_CASE");
			idSecuencia = resultset.getInt("ID_SECUENCIA");
			idEscenario = resultset.getString("ID_ESCENARIO");
		}
		System.out.println("----------------------------------------");
		System.out.println("VALIDACION DEL AI CORRESPONDIENTE AL R INICIAL");
		System.out.println("  \n");
		for (int i = 0; i < cad.size(); i++) {
			valor = cad.get(i).split("=")[0];
			val = cad.get(i).split("=")[1];
			switch (valor) {
			case "55":
				if (cad.get(i).split("=")[1].equals(symbol)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(55): " + cad.get(i).split("=")[1] + " bd: " + symbol);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), symbol, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println("diferentes:  cadena(55): " + cad.get(i).split("=")[1] + " bd: " + symbol);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), symbol, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "35":
				if (cad.get(i).split("=")[1].equals(type)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(35): " + cad.get(i).split("=")[1] + " bd: " + type);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), type, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println("diferentes:  cadena(35): " + cad.get(i).split("=")[1] + " bd: " + type);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), type, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "762":
				if (cad.get(i).split("=")[1].equals(subtype)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(762): " + cad.get(i).split("=")[1] + " bd: " + subtype);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), subtype, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println("diferentes:  cadena(762): " + cad.get(i).split("=")[1] + " bd: " + subtype);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), subtype, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "54":
				if (cad.get(i).split("=")[1].equals(side)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(54): " + cad.get(i).split("=")[1] + " bd: " + side);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), side, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println("diferentes:  cadena(54): " + cad.get(i).split("=")[1] + " bd: " + side);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), side, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "38":
				if (cad.get(i).split("=")[1].equals(order)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(38): " + cad.get(i).split("=")[1] + " bd: " + order);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), order, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println("diferentes:  cadena(38): " + cad.get(i).split("=")[1] + " bd: " + order);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), order, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;

			case "694":
				if (cad.get(i).split("=")[1].equals(latedsym)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(694): " + cad.get(i).split("=")[1] + " bd: " + latedsym);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), latedsym, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println("diferentes:  cadena(694): " + cad.get(i).split("=")[1] + " bd: " + latedsym);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), latedsym, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
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
		String cadena = ""+qr;
		ResultSet resultset;
		
		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos " + "WHERE ID_CASESEQ = "
				+ datosCache.getIdCaseseq();

		resultset = DataAccess.getQuery(queryMessageR);

		ArrayList<String> cad = FragmentarCadena(cadena);
		String valor;
		String val;
		String execType = null, orderStatus = null, side = null, leaveQty = null, orderQty = null, price = null, grosstradeamt = null, nopartyIds = null, secSubtype = null, reforderIdsCr = null,
				symbol = null, senderSubId = null, dirtyPrice = null, partyIdsSource = null,idCase=null,idEscenario=null;
		int idSecuencia=0;
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
			idEscenario = resultset.getString("ID_ESCENARIO");

		}
		System.out.println("----------------------------------------");
		System.out.println("VALIDACION DEL EXECUTION REPORT ");
		System.out.println("  \n");
		for (int i = 0; i < cad.size(); i++) {
			valor = cad.get(i).split("=")[0];
			val = cad.get(i).split("=")[1];
			switch (valor) {
			case "150":
				if (cad.get(i).split("=")[1].equals(execType)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(150): " + cad.get(i).split("=")[1] + " bd: " + execType);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), execType, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println("diferentes:  Execution Report(150): " + cad.get(i).split("=")[1] + " bd: " + execType);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), execType, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "39":
				if (cad.get(i).split("=")[1].equals(orderStatus)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(39): " + cad.get(i).split("=")[1] + " bd: " + orderStatus);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), orderStatus, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println("diferentes:  Execution Report(39): " + cad.get(i).split("=")[1] + " bd: " + orderStatus);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), orderStatus, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "54":
				if (cad.get(i).split("=")[1].equals(side)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(54): " + cad.get(i).split("=")[1] + " bd: " + side);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), side, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println("diferentes:  Execution Report(54): " + cad.get(i).split("=")[1] + " bd: " + side);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), side, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "151":
				if (cad.get(i).split("=")[1].equals(leaveQty)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(151): " + cad.get(i).split("=")[1] + " bd: " + leaveQty);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), leaveQty, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println("diferentes:  Execution Report(151): " + cad.get(i).split("=")[1] + " bd: " + leaveQty);
					contadorMalos++;
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), leaveQty, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				}
				break;
			case "38":
				if (cad.get(i).split("=")[1].equals(orderQty)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(38): " + cad.get(i).split("=")[1] + " bd: " + orderQty);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), orderQty, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					System.out.println("diferentes:  Execution Report(38): " + cad.get(i).split("=")[1] + " bd: " + orderQty);
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), orderQty, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					contadorMalos++;
				}
				break;
			case "44":
				if (cad.get(i).split("=")[1].equals(price)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(44): " + cad.get(i).split("=")[1] + " bd: " + price);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), price, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), price, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					System.out.println("diferentes:  Execution Report(44): " + cad.get(i).split("=")[1] + " bd: " + price);
					contadorMalos++;
				}
				break;
			case "387":
				if (cad.get(i).split("=")[1].equals(grosstradeamt)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(387): " + cad.get(i).split("=")[1] + " bd: " + grosstradeamt);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), grosstradeamt, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), grosstradeamt, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					System.out.println("diferentes:  Execution Report(387): " + cad.get(i).split("=")[1] + " bd: " + grosstradeamt);
					contadorMalos++;
				}
				break;
			case "453":
				if (cad.get(i).split("=")[1].equals(nopartyIds)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(453): " + cad.get(i).split("=")[1] + " bd: " + nopartyIds);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), nopartyIds, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), nopartyIds, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					System.out.println("diferentes:  Execution Report(453): " + cad.get(i).split("=")[1] + " bd: " + nopartyIds);
					contadorMalos++;
				}
				break;
			case "762":
				if (cad.get(i).split("=")[1].equals(secSubtype)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(762): " + cad.get(i).split("=")[1] + " bd: " + secSubtype);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), secSubtype, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), secSubtype, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					System.out.println("diferentes:  Execution Report(762): " + cad.get(i).split("=")[1] + " bd: " + secSubtype);
					contadorMalos++;
				}
				break;
			case "1081":
				if (cad.get(i).split("=")[1].equals(reforderIdsCr)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(1081): " + cad.get(i).split("=")[1] + " bd: " + reforderIdsCr);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), reforderIdsCr, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), reforderIdsCr, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					System.out
							.println("diferentes:  Execution Report(1081): " + cad.get(i).split("=")[1] + " bd: " + reforderIdsCr);
					contadorMalos++;
				}
				break;
			case "55":
				if (cad.get(i).split("=")[1].equals(symbol)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(55): " + cad.get(i).split("=")[1] + " bd: " + symbol);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), symbol, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), symbol, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					System.out.println("diferentes:  Execution Report(55): " + cad.get(i).split("=")[1] + " bd: " + symbol);
					contadorMalos++;
				}
				break;
			case "50":
				if (cad.get(i).split("=")[1].equals(senderSubId)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(50): " + cad.get(i).split("=")[1] + " bd: " + senderSubId);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), senderSubId, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), senderSubId, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					System.out.println("diferentes:  Execution Report(50): " + cad.get(i).split("=")[1] + " bd: " + senderSubId);
					contadorMalos++;
				}
				break;
			case "20102":
				if (cad.get(i).split("=")[1].equals(dirtyPrice)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(20102): " + cad.get(i).split("=")[1] + " bd: " + dirtyPrice);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), dirtyPrice, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), dirtyPrice, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					System.out
							.println("diferentes:  Execution Report(20102): " + cad.get(i).split("=")[1] + " bd: " + dirtyPrice);
					contadorMalos++;
				}
				break;
			case "447":
				if (cad.get(i).split("=")[1].equals(partyIdsSource)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(447): " + cad.get(i).split("=")[1] + " bd: " + partyIdsSource);
					DataAccess.cargarLogsExitosos(qr, datosCache.getIdEjecucion(), partyIdsSource, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
				} else {
					DataAccess.cargarLogsFallidos(qr, datosCache.getIdEjecucion(), partyIdsSource, cad.get(i).split("=")[1], idEscenario,
							idCase, idSecuencia);
					System.out.println("diferentes:  Execution Report(447): " + cad.get(i).split("=")[1] + " bd: " + partyIdsSource);
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
}
