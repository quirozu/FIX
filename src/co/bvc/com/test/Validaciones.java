package co.bvc.com.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import co.bvc.com.basicfix.Constantes;
import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.dao.domain.AutFixRfqDatosCache;
import quickfix.FieldNotFound;
import quickfix.Message;
import quickfix.Session;
import quickfix.field.MsgType;
import quickfix.field.NoPartyIDs;
import quickfix.field.NoRelatedSym;
import quickfix.field.OrderQty;
import quickfix.field.QuoteRespID;
import quickfix.field.SecuritySubType;
import quickfix.field.Side;
import quickfix.field.Symbol;
import quickfix.field.ValidUntilTime;

import quickfix.fix44.QuoteRequest;

public class Validaciones {

	private String cadenaAI;
	private String CadenaSPrima;
	private String cadenaOcho;

	public String QueryExitoso(ResultSet resultSet, Message message, String ID_EJECUCION, String clave,
			String valor) throws SQLException, FieldNotFound {
		String cadena = "INSERT INTO aut_log_ejecucion VALUES(" + ID_EJECUCION + ","
				+ resultSet.getString("ID_ESCENARIO") + "," + resultSet.getString("ID_CASE") + ","
				+ resultSet.getString("ID_SECUENCIA") + ",now(),EXISTOSO," + clave + "=" + valor + "," + " " + ",null";

		return cadena;
	}

	public String QueryFallido(ResultSet resultSet, Message message, String ID_EJECUCION, String clave,
			String valor) throws SQLException, FieldNotFound {
		String cadena = "INSERT INTO aut_log_ejecucion VALUES(" + ID_EJECUCION + ","
				+ resultSet.getString("ID_ESCENARIO") + "," + resultSet.getString("ID_CASE") + ","
				+ resultSet.getString("ID_SECUENCIA") + ",now(),FALLIDO," + clave + "=" + valor + "," + message
				+ ",null";

		return cadena;
	}

	public String getCadenaOcho() {
		return cadenaOcho;
	}

	public void setCadenaOcho(String cadenaOcho) {
		this.cadenaOcho = cadenaOcho;
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

	public void ValidaAI(AutFixRfqDatosCache datosCache, QuoteRequest qr, String ID_EJECUCION) throws SQLException, FieldNotFound {
		int contadorBuenos = 0;
		int contadorMalos = 0;
		
		ResultSet resultSet;

		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos " + "WHERE ID_CASESEQ = "
				+ datosCache.getIdCaseseq();

		resultSet = DataAccess.getQuery(queryMessageR);

		System.out.println("----------------------------------------");
		System.out.println("VALIDACION DEL AI CORRESPONDIENTE AL R INICIAL");
		System.out.println("  \n");

		while (resultSet.next()) {

			if (resultSet.getString("RS_SYMBOL") == qr.getString(Symbol.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_SYMBOL"),
						qr.getString(Symbol.FIELD)));
			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_SYMBOL"),
						qr.getString(Symbol.FIELD)));
			}

			if (resultSet.getString("RS_MSGTYPE") == qr.getString(MsgType.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_MSGTYPE"),
						qr.getString(MsgType.FIELD)));
			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_MSGTYPE"),
						qr.getString(MsgType.FIELD)));

			}
			if (resultSet.getString("RS_SECSUBTYPE") == qr.getString(SecuritySubType.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_SECSUBTYPE"),
						qr.getString(SecuritySubType.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_SECSUBTYPE"),
						qr.getString(SecuritySubType.FIELD)));

			}

			if (resultSet.getString("RS_SIDE") == qr.getString(Side.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_SIDE"),
						qr.getString(Side.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_SIDE"),
						qr.getString(Side.FIELD)));

			}

			if (resultSet.getString("RS_ORDERQTY") == qr.getString(OrderQty.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_ORDERQTY"),
						qr.getString(OrderQty.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_ORDERQTY"),
						qr.getString(OrderQty.FIELD)));

			}
			if (resultSet.getString("RS_VALIDUNTILTIME") == qr.getString(ValidUntilTime.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_VALIDUNTILTIME"),
						qr.getString(ValidUntilTime.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_VALIDUNTILTIME"),
						qr.getString(ValidUntilTime.FIELD)));

			}
			if (resultSet.getString("RS_QUOTERESPID") == qr.getString(QuoteRespID.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_QUOTERESPID"),
						qr.getString(QuoteRespID.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_QUOTERESPID"),
						qr.getString(QuoteRespID.FIELD)));

			}
		}

		System.out.println("----------------------------------------");
		System.out.println("LAS VALIDACIONES CORRECTAS FUERON : " + contadorBuenos);
		System.out.println("LAS VALIDACIONES ERRADAS FUERON : " + contadorMalos);
		System.out.println("TOTAL VALIDACIONES REALIZADAS : " + (contadorBuenos + contadorMalos));

	}

	public void ValidarRPrima(AutFixRfqDatosCache datosCache, Message qr, String ID_EJECUCION)
			throws InterruptedException, SQLException, FieldNotFound {
		int contadorBuenos = 0;
		int contadorMalos = 0;

		ResultSet resultSet;

		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos " + "WHERE ID_CASESEQ = "
				+ datosCache.getIdCaseseq();
		
		System.out.println("CONSULTA NUEVA " + queryMessageR);

		resultSet = DataAccess.getQuery(queryMessageR);

		String BeginString = Constantes.PROTOCOL_FIX_VERSION, SenderCompID = "EXC";

		System.out.println("----------------------------------------");
		System.out.println("VALIDACION DE R CON R PRIMA");
		System.out.println("  \n");
		while (resultSet.next()) {
			String rt = resultSet.getString("RQ_SYMBOL");
			System.out.println(rt);
			System.out.println("Mensaje de Entrada=== "+qr.getString(55));
			if (resultSet.getString("RQ_SYMBOL").equals(qr.getString(Symbol.FIELD))) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_SYMBOL"),
						qr.getString(Symbol.FIELD)));
			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_SYMBOL"),
						qr.getString(Symbol.FIELD)));

			}

			if (resultSet.getString("RQ_MSGTYPE") == qr.getString(MsgType.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_MSGTYPE"),
						qr.getString(MsgType.FIELD)));
			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_MSGTYPE"),
						qr.getString(MsgType.FIELD)));

			}

			if (resultSet.getString("RQ_SECSUBTYPE") == qr.getString(SecuritySubType.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_SECSUBTYPE"),
						qr.getString(SecuritySubType.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_SECSUBTYPE"),
						qr.getString(SecuritySubType.FIELD)));

			}

			if (resultSet.getString("RQ_SIDE") == qr.getString(Side.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_SIDE"),
						qr.getString(Side.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_SIDE"),
						qr.getString(Side.FIELD)));

			}

			if (resultSet.getString("RQ_ORDERQTY") == qr.getString(OrderQty.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_ORDERQTY"),
						qr.getString(OrderQty.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_ORDERQTY"),
						qr.getString(OrderQty.FIELD)));

			}

			if (resultSet.getString("RQ_VALIDUNTILTIME") == qr.getString(ValidUntilTime.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_VALIDUNTILTIME"),
						qr.getString(ValidUntilTime.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_VALIDUNTILTIME"),
						qr.getString(ValidUntilTime.FIELD)));

			}

			if (resultSet.getString("RQ_NORELATEDSYM") == qr.getString(NoRelatedSym.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_NORELATEDSYM"),
						qr.getString(NoRelatedSym.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_NORELATEDSYM"),
						qr.getString(NoRelatedSym.FIELD)));

			}

			if (BeginString == qr.getString(quickfix.field.BeginString.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, BeginString,
						qr.getString(quickfix.field.BeginString.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, BeginString,
						qr.getString(quickfix.field.BeginString.FIELD)));

			}

			if (resultSet.getString("RQ_NOPARTYIDS") == qr.getString(NoPartyIDs.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_NOPARTYIDS"),
						qr.getString(NoPartyIDs.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RQ_NOPARTYIDS"),
						qr.getString(NoPartyIDs.FIELD)));

			}

			if (SenderCompID == qr.getString(quickfix.field.SenderCompID.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, SenderCompID,
						qr.getString(quickfix.field.SenderCompID.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, SenderCompID,
						qr.getString(quickfix.field.SenderCompID.FIELD)));

			}

		}

		System.out.println("----------------------------------------");
		System.out.println("LAS VALIDACIONES CORRECTAS FUERON : " + contadorBuenos);
		System.out.println("LAS VALIDACIONES ERRADAS FUERON : " + contadorMalos);
		System.out.println("TOTAL VALIDACIONES REALIZADAS : " + (contadorBuenos + contadorMalos));
	}

	public void ValidarSPrima(ResultSet resultSet, QuoteRequest qr, String ID_EJECUCION)
			throws InterruptedException, SQLException {
		int contadorBuenos = 0;
		int contadorMalos = 0;
		String cadenaPrima = this.CadenaSPrima;
		String clavePrima;
		String valorPrima;
		ArrayList<String> cad = FragmentarCadena1(cadenaPrima);
		ResultSet resultset;
		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos "
				+ "WHERE ID_ESCENARIO = 'FIX_S' and ID_CASE = 1";

		resultset = DataAccess.getQuery(queryMessageR);
		String a = null, b = null, c = "500000000", d = null, e = null, i = "5", j = "M", k = "EXC", l = "FIX.4.4";

		while (resultset.next()) {
			a = resultset.getString("RQ_VALIDUNTILTIME");
			b = resultset.getString("RQ_MSGTYPE");
			d = resultset.getString("RQ_SECSUBTYPE");
			e = resultset.getString("RQ_SYMBOL");

		}
		System.out.println("----------------------------------------");
		System.out.println("VALIDACION DE S CON S PRIMA");
		System.out.println("  \n");
		for (int z = 0; z < cad.size(); z++) {
			clavePrima = cad.get(z).split("=")[0];
			valorPrima = cad.get(z).split("=")[1];
			switch (clavePrima) {
//			case "52":
//				if (cad.get(z).split("=")[1].equals(a)) {
//					System.out.println("iguales:  cadenaPrima(52): " + cad.get(z).split("=")[1] + " cadenaS " + a);
//				} else
//					System.out.println("diferentes:  cadenaPrima(52): " + cad.get(z).split("=")[1] + " cadenaS " + a);
//				break;
			case "55":
				if (cad.get(z).split("=")[1].equals(e)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(52): " + cad.get(z).split("=")[1] + " cadenaS " + e);
				} else {
					System.out.println("diferentes:  cadenaPrima(52): " + cad.get(z).split("=")[1] + " cadenaS " + e);
					contadorMalos++;
				}
				break;
			case "762":
				if (cad.get(z).split("=")[1].equals(d)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(52): " + cad.get(z).split("=")[1] + " cadenaS " + d);
				} else {
					System.out.println("diferentes:  cadenaPrima(52): " + cad.get(z).split("=")[1] + " cadenaS " + d);
					contadorMalos++;
				}
				break;
			case "35":
				if (cad.get(z).split("=")[1].equals(b)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(35): " + cad.get(z).split("=")[1] + " cadenaS " + b);
				} else {
					System.out.println("diferentes:  cadenaPrima(35): " + cad.get(z).split("=")[1] + " cadenaS " + b);
					contadorMalos++;
				}
				break;
			case "135":
				if (cad.get(z).split("=")[1].equals(c)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + c);
				} else {
					System.out.println("diferentes:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + c);
					contadorMalos++;
				}
				break;
			case "453":
				if (cad.get(z).split("=")[1].equals(i)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + i);
				} else {
					System.out.println("diferentes:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + i);
					contadorMalos++;
				}
				break;
			case "422":
				if (cad.get(z).split("=")[1].equals(j)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + j);
				} else {
					System.out.println("diferentes:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + j);
					contadorMalos++;
				}
				break;
			case "49":
				if (cad.get(z).split("=")[1].equals(k)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + k);
				} else {
					System.out.println("diferentes:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + k);
					contadorMalos++;
				}
				break;
			case "8":
				if (cad.get(z).split("=")[1].equals(l)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + l);
				} else {
					System.out.println("diferentes:  cadenaPrima(135): " + cad.get(z).split("=")[1] + " cadenaS " + l);
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

	public void ValidaS(ResultSet resultSet, QuoteRequest qr, String ID_EJECUCION) throws SQLException, FieldNotFound {
		int contadorBuenos = 0;
		int contadorMalos = 0;

		System.out.println("----------------------------------------");
		System.out.println("VALIDACION DEL AI CORRESPONDIENTE AL S ENVIADO POR EL RECEPTOR\n");
		System.out.println("  \n");

		while (resultSet.next()) {

			if (resultSet.getString("RS_SYMBOL") == qr.getString(Symbol.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_SYMBOL"),
						qr.getString(Symbol.FIELD)));
			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_SYMBOL"),
						qr.getString(Symbol.FIELD)));
			}

			if (resultSet.getString("RS_MSGTYPE") == qr.getString(MsgType.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_MSGTYPE"),
						qr.getString(MsgType.FIELD)));
			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_MSGTYPE"),
						qr.getString(MsgType.FIELD)));

			}
			if (resultSet.getString("RS_SECSUBTYPE") == qr.getString(SecuritySubType.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_SECSUBTYPE"),
						qr.getString(SecuritySubType.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_SECSUBTYPE"),
						qr.getString(SecuritySubType.FIELD)));

			}

			if (resultSet.getString("RS_SIDE") == qr.getString(Side.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_SIDE"),
						qr.getString(Side.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_SIDE"),
						qr.getString(Side.FIELD)));

			}

			if (resultSet.getString("RS_ORDERQTY") == qr.getString(OrderQty.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_ORDERQTY"),
						qr.getString(OrderQty.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_ORDERQTY"),
						qr.getString(OrderQty.FIELD)));

			}
			if (resultSet.getString("RS_VALIDUNTILTIME") == qr.getString(ValidUntilTime.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_VALIDUNTILTIME"),
						qr.getString(ValidUntilTime.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_VALIDUNTILTIME"),
						qr.getString(ValidUntilTime.FIELD)));

			}
			if (resultSet.getString("RS_QUOTERESPID") == qr.getString(QuoteRespID.FIELD)) {
				contadorBuenos++;
				DataAccess.setQuery(QueryExitoso(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_QUOTERESPID"),
						qr.getString(QuoteRespID.FIELD)));

			} else {
				contadorMalos++;
				DataAccess.setQuery(QueryFallido(resultSet, qr, ID_EJECUCION, resultSet.getString("RS_QUOTERESPID"),
						qr.getString(QuoteRespID.FIELD)));

			}
		}

		System.out.println("----------------------------------------");
		System.out.println("LAS VALIDACIONES CORRECTAS FUERON : " + contadorBuenos);
		System.out.println("LAS VALIDACIONES ERRADAS FUERON : " + contadorMalos);
		System.out.println("TOTAL VALIDACIONES REALIZADAS : " + (contadorBuenos + contadorMalos));

	}

	public void validarOcho() throws SQLException {
		int contadorBuenos = 0;
		int contadorMalos = 0;
		String cadena = getCadenaOcho();
		ResultSet resultset;
		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos "
				+ "WHERE ID_ESCENARIO = 'FIX_AJ' and ID_CASE = 1 ;";

		resultset = DataAccess.getQuery(queryMessageR);

		ArrayList<String> cad = FragmentarCadena(cadena);
		String valor;
		String val;
		String a = null, b = null, c = null, d = null, e = null, f = null, g = null, h = null, j = null, k = null,
				l = null, m = null, n = null, o = null;
		while (resultset.next()) {

//			switch(resultset.getInt(columnIndex))
			a = resultset.getString("ER_EXECTYPE");
			b = resultset.getString("ER_ORDSTATUS");
			c = resultset.getString("ER_SIDE");
			d = resultset.getString("ER_LEAVEQTY");
			e = resultset.getString("ER_ORDERQTY");
			f = resultset.getString("ER_PRICE");
			g = resultset.getString("ER_GROSSTRADEAMT");
			h = resultset.getString("ER_NOPARTYIDS");
			j = resultset.getString("ER_SECSUBTYPE");
			k = resultset.getString("ER_REFORDERIDSCR");
			l = resultset.getString("ER_SYMBOL");
			m = resultset.getString("ER_SENDERSUBID");
			n = resultset.getString("ER_DIRTYPRICE");
			o = resultset.getString("ER_PARTYIDSOURCE");

		}
		System.out.println("----------------------------------------");
		System.out.println("VALIDACION DEL EXECUTION REPORT ");
		System.out.println("  \n");
		for (int i = 0; i < cad.size(); i++) {
			valor = cad.get(i).split("=")[0];
			val = cad.get(i).split("=")[1];
			switch (valor) {
			case "150":
				if (cad.get(i).split("=")[1].equals(a)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(150): " + cad.get(i).split("=")[1] + " bd: " + a);
				} else {
					System.out.println("diferentes:  Execution Report(150): " + cad.get(i).split("=")[1] + " bd: " + a);
					contadorMalos++;
				}
				break;
			case "39":
				if (cad.get(i).split("=")[1].equals(b)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(39): " + cad.get(i).split("=")[1] + " bd: " + b);
				} else {
					System.out.println("diferentes:  Execution Report(39): " + cad.get(i).split("=")[1] + " bd: " + b);
					contadorMalos++;
				}
				break;
			case "54":
				if (cad.get(i).split("=")[1].equals(c)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(54): " + cad.get(i).split("=")[1] + " bd: " + c);
				} else {
					System.out.println("diferentes:  Execution Report(54): " + cad.get(i).split("=")[1] + " bd: " + c);
					contadorMalos++;
				}
				break;
			case "151":
				if (cad.get(i).split("=")[1].equals(d)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(151): " + cad.get(i).split("=")[1] + " bd: " + d);
				} else {
					System.out.println("diferentes:  Execution Report(151): " + cad.get(i).split("=")[1] + " bd: " + d);
					contadorMalos++;
				}
				break;
			case "38":
				if (cad.get(i).split("=")[1].equals(e)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(38): " + cad.get(i).split("=")[1] + " bd: " + e);
				} else {
					System.out.println("diferentes:  Execution Report(38): " + cad.get(i).split("=")[1] + " bd: " + e);
					contadorMalos++;
				}
				break;
			case "44":
				if (cad.get(i).split("=")[1].equals(f)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(44): " + cad.get(i).split("=")[1] + " bd: " + f);
				} else {
					System.out.println("diferentes:  Execution Report(44): " + cad.get(i).split("=")[1] + " bd: " + f);
					contadorMalos++;
				}
				break;
			case "387":
				if (cad.get(i).split("=")[1].equals(g)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(387): " + cad.get(i).split("=")[1] + " bd: " + g);
				} else {
					System.out.println("diferentes:  Execution Report(387): " + cad.get(i).split("=")[1] + " bd: " + g);
					contadorMalos++;
				}
				break;
			case "453":
				if (cad.get(i).split("=")[1].equals(h)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(453): " + cad.get(i).split("=")[1] + " bd: " + h);
				} else {
					System.out.println("diferentes:  Execution Report(453): " + cad.get(i).split("=")[1] + " bd: " + h);
					contadorMalos++;
				}
				break;
			case "762":
				if (cad.get(i).split("=")[1].equals(j)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(762): " + cad.get(i).split("=")[1] + " bd: " + j);
				} else {
					System.out.println("diferentes:  Execution Report(762): " + cad.get(i).split("=")[1] + " bd: " + j);
					contadorMalos++;
				}
				break;
			case "1081":
				if (cad.get(i).split("=")[1].equals(k)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(1081): " + cad.get(i).split("=")[1] + " bd: " + k);
				} else {
					System.out
							.println("diferentes:  Execution Report(1081): " + cad.get(i).split("=")[1] + " bd: " + k);
					contadorMalos++;
				}
				break;
			case "55":
				if (cad.get(i).split("=")[1].equals(l)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(55): " + cad.get(i).split("=")[1] + " bd: " + l);
				} else {
					System.out.println("diferentes:  Execution Report(55): " + cad.get(i).split("=")[1] + " bd: " + l);
					contadorMalos++;
				}
				break;
			case "50":
				if (cad.get(i).split("=")[1].equals(m)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(50): " + cad.get(i).split("=")[1] + " bd: " + m);
				} else {
					System.out.println("diferentes:  Execution Report(50): " + cad.get(i).split("=")[1] + " bd: " + m);
					contadorMalos++;
				}
				break;
			case "20102":
				if (cad.get(i).split("=")[1].equals(n)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(20102): " + cad.get(i).split("=")[1] + " bd: " + n);
				} else {
					System.out
							.println("diferentes:  Execution Report(20102): " + cad.get(i).split("=")[1] + " bd: " + n);
					contadorMalos++;
				}
				break;
			case "447":
				if (cad.get(i).split("=")[1].equals(o)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(447): " + cad.get(i).split("=")[1] + " bd: " + o);
				} else {
					System.out.println("diferentes:  Execution Report(447): " + cad.get(i).split("=")[1] + " bd: " + o);
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
