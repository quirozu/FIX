package co.bvc.com.test;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import co.bvc.com.basicfix.Constantes;
import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.dao.domain.AutFixRfqDatosCache;
import quickfix.FieldNotFound;
import quickfix.Session;
import quickfix.fix44.Message;
import quickfix.fix44.QuoteRequest;

public class Validaciones {

	private String cadenaAI;
	private String CadenaSPrima;
	private String CadenaRPrima = "";
	private String cadenaOcho;
//	private int contadorBuenos = 0;
//	private int contadorMalos = 0;

//	public void imput() {
//		System.out.println("----------------------------------------");
//		System.out.println("LAS VALIDACIONES CORRECTAS FUERON : " + getContadorBuenos());
//		System.out.println("LAS VALIDACIONES ERRADAS FUERON : " + getContadorMalos());
//		System.out.println("TOTAL VALIDACIONES REALIZADAS : " + (getContadorBuenos() + getContadorMalos()));
//	}

//	public int getContadorBuenos() {
//		return contadorBuenos;
//	}
//
//	public int getContadorMalos() {
//		return contadorMalos;
//	}

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

	public void ValidarRPrima(AutFixRfqDatosCache datosCache, QuoteRequest qr) throws InterruptedException, SQLException, FieldNotFound {
		int contadorBuenos = 0;
		int contadorMalos = 0;
		String cadenaPrima = this.CadenaRPrima;
		String clavePrima;
		String valorPrima;
		ArrayList<String> cad = FragmentarCadena1(cadenaPrima);
		ResultSet resultSet;
		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos "
				+ "WHERE ID_CASESEQ = "+datosCache.getIdCaseseq();

		resultSet = DataAccess.getQuery(queryMessageR);
		
		String message = qr.getString(35);
		
//		message.getString("RQ_SYMBOL");
		String Rq_Symbol = null, Rq_MsgType = null, Rq_SecSubType = null, Rq_Side = null, Rs_Orderqty = null, Rs_Validuntilime = null, RS_NORELATEDSYM = null, h = Constantes.PROTOCOL_FIX_VERSION, i = "3", j = "EXC";

		while (resultSet.next()) {
			if(resultSet.getString("RQ_SYMBOL") != qr.getString(35)) {
//				peristir(diferentes(d))
			}
			
			
			Rq_Symbol = resultSet.getString("RQ_SYMBOL");
			Rq_MsgType = resultSet.getString("RQ_MSGTYPE");
			Rq_SecSubType = resultSet.getString("RQ_SECSUBTYPE");
			Rq_Side = resultSet.getString("RQ_SIDE");
			Rs_Orderqty = resultSet.getString("RS_ORDERQTY");
			Rs_Validuntilime = resultSet.getString("RS_VALIDUNTILTIME");
			RS_NORELATEDSYM = resultSet.getString("RS_NORELATEDSYM");
		}
		System.out.println("----------------------------------------");
		System.out.println("VALIDACION DE R CON R PRIMA");
		System.out.println("  \n");
		for (int z = 0; z < cad.size(); z++) {
			clavePrima = cad.get(z).split("=")[0];
			valorPrima = cad.get(z).split("=")[1];
			switch (clavePrima) {
			case "55":
				if (cad.get(z).split("=")[1].equals(Rq_Symbol)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(55): " + cad.get(z).split("=")[1] + " cadenaR " + Rq_Symbol);
				} else {
					System.out.println("diferentes:  cadenaPrima(55): " + cad.get(z).split("=")[1] + " cadenaR " + Rq_Symbol);
					contadorMalos++;
				}
				break;
			case "35":
				if (cad.get(z).split("=")[1].equals(Rq_MsgType)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(35): " + cad.get(z).split("=")[1] + " cadenaR " + Rq_MsgType);
				} else {
					System.out.println("diferentes:  cadenaPrima(35): " + cad.get(z).split("=")[1] + " cadenaR " + Rq_MsgType);
					contadorMalos++;
				}
				break;
			case "762":
				if (cad.get(z).split("=")[1].equals(Rq_SecSubType)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(762): " + cad.get(z).split("=")[1] + " cadenaR " + Rq_SecSubType);
				} else {
					System.out.println("diferentes:  cadenaPrima(762): " + cad.get(z).split("=")[1] + " cadenaR " + Rq_SecSubType);
					contadorMalos++;
				}
				break;
			case "54":
				if (cad.get(z).split("=")[1].equals(Rq_Side)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(54): " + cad.get(z).split("=")[1] + " cadenaR " + Rq_Side);
				} else {
					System.out.println("diferentes:  cadenaPrima(54): " + cad.get(z).split("=")[1] + " cadenaR " + Rq_Side);
					contadorMalos++;
				}
				break;
			case "38":
				if (cad.get(z).split("=")[1].equals(Rs_Orderqty)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(38): " + cad.get(z).split("=")[1] + " cadenaR " + Rs_Orderqty);
				} else {
					System.out.println("diferentes:  cadenaPrima(38): " + cad.get(z).split("=")[1] + " cadenaR " + Rs_Orderqty);
					contadorMalos++;
				}
				break;
//			case "52":
//				if (cad.get(z).split("=")[1].equals(f)) {
//					System.out.println("iguales:  cadenaPrima(52): " + cad.get(z).split("=")[1] + " cadenaR " + f);
//				} else
//					System.out.println("diferentes:  cadenaPrima(52): " + cad.get(z).split("=")[1] + " cadenaR " + f);
//				break;
			case "146":
				if (cad.get(z).split("=")[1].equals(RS_NORELATEDSYM)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(146): " + cad.get(z).split("=")[1] + " cadenaR " + RS_NORELATEDSYM);
				} else {
					System.out.println("diferentes:  cadenaPrima(146): " + cad.get(z).split("=")[1] + " cadenaR " + RS_NORELATEDSYM);
					contadorMalos++;
				}
				break;

			case "8":
				if (cad.get(z).split("=")[1].equals(h)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(8): " + cad.get(z).split("=")[1] + " cadenaR " + h);
				} else {
					System.out.println("diferentes:  cadenaPrima(8): " + cad.get(z).split("=")[1] + " cadenaR " + h);
					contadorMalos++;
				}
				break;
			case "453":
				if (cad.get(z).split("=")[1].equals(i)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(453): " + cad.get(z).split("=")[1] + " cadenaR " + i);
				} else {
					System.out.println("diferentes:  cadenaPrima(453): " + cad.get(z).split("=")[1] + " cadenaR " + i);
					contadorMalos++;
				}
				break;
			case "49":
				if (cad.get(z).split("=")[1].equals(j)) {
					contadorBuenos++;
					System.out.println("iguales:  cadenaPrima(49): " + cad.get(z).split("=")[1] + " cadenaR " + j);
				} else {
					System.out.println("diferentes:  cadenaPrima(49): " + cad.get(z).split("=")[1] + " cadenaR " + j);
					contadorMalos++;
				}
				break;

			default:

				break;
			}
		}
//		String de = "INSERT INTO logs_fix values(100,9999,1,'New_Order_Single','EXITOSO',1,0,1,'8=FIX.4.4�9=0353�35=8�34=2�49=BVCGATEWAY�56=BCSRBVC�52=20171130-18:53:21.097�11=7500�17=279�37=201711300000000391�198=201711300000000391�150=0�453=4�448=860I001�447=D�452=36�448=066�447=D�452=7�448=029�447=D�452=1�448=029�447=D�452=19�55=ECOPETROL�48=COC04PA00016�22=4�167=CS�207=XBOG�6=0�14=0�38=100�39=0�40=2�44=2150.00�54=2�59=0�60=20171130-18:53:21.097�63=4�151=100�10=150�');";
//		DataAccess.setQuery(de);
		System.out.println("----------------------------------------");
		System.out.println("LAS VALIDACIONES CORRECTAS FUERON : " + contadorBuenos);
		System.out.println("LAS VALIDACIONES ERRADAS FUERON : " + contadorMalos);
		System.out.println("TOTAL VALIDACIONES REALIZADAS : " + (contadorBuenos + contadorMalos));
	}

	
	public void ValidarSPrima() throws InterruptedException, SQLException {
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

	public void ValidaR() throws SQLException {
		int contadorBuenos = 0;
		int contadorMalos = 0;
		String cadena = getCadenaAI();
		ResultSet resultset;
		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos "
				+ "WHERE ID_ESCENARIO = 'FIX_R' and ID_CASE = 1";

		resultset = DataAccess.getQuery(queryMessageR);
		ArrayList<String> cad = FragmentarCadena(cadena);
		String valor;
		String val;
		String type = null, symbol = null, subtype = null, side = null, order = null, time = null, latedsym = null,
				Nopartys = null;
		while (resultset.next()) {
			type = resultset.getString("RS_MSGTYPE");
			symbol = resultset.getString("RS_SYMBOL");
			subtype = resultset.getString("RS_SECSUBTYPE");
			side = resultset.getString("RS_SIDE");
			order = resultset.getString("RS_ORDERQTY");
			time = resultset.getString("RS_VALIDUNTILTIME");
			latedsym = resultset.getString("RS_NORELATEDSYM");
			Nopartys = resultset.getString("RS_NOPARTYIDS");
//                               System.out.println("type: "+type+"symbol: "+symbol);          
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
				} else {
					System.out.println("diferentes:  cadena(55): " + cad.get(i).split("=")[1] + " bd: " + symbol);
					contadorMalos++;
				}
				break;
			case "35":
				if (cad.get(i).split("=")[1].equals(type)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(35): " + cad.get(i).split("=")[1] + " bd: " + type);
				} else {
					System.out.println("diferentes:  cadena(35): " + cad.get(i).split("=")[1] + " bd: " + type);
					contadorMalos++;
				}
				break;
			case "762":
				if (cad.get(i).split("=")[1].equals(subtype)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(762): " + cad.get(i).split("=")[1] + " bd: " + subtype);
				} else {
					System.out.println("diferentes:  cadena(762): " + cad.get(i).split("=")[1] + " bd: " + subtype);
					contadorMalos++;
				}
				break;
			case "54":
				if (cad.get(i).split("=")[1].equals(side)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(54): " + cad.get(i).split("=")[1] + " bd: " + side);
				} else {
					System.out.println("diferentes:  cadena(54): " + cad.get(i).split("=")[1] + " bd: " + side);
					contadorMalos++;
				}
				break;
			case "38":
				if (cad.get(i).split("=")[1].equals(order)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(38): " + cad.get(i).split("=")[1] + " bd: " + order);
				} else {
					System.out.println("diferentes:  cadena(38): " + cad.get(i).split("=")[1] + " bd: " + order);
					contadorMalos++;
				}
				break;
//			case "52":
//				if (cad.get(i).split("=")[1].equals(time)) {
//					System.out.println("iguales:  cadena: " + cad.get(i).split("=")[1] + " bd: " + time);
//				} else
//					System.out.println("diferentes:  cadena: " + cad.get(i).split("=")[1] + " bd: " + time);
//				break;
			case "694":
				if (cad.get(i).split("=")[1].equals(latedsym)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(694): " + cad.get(i).split("=")[1] + " bd: " + latedsym);
				} else {
					System.out.println("diferentes:  cadena(694): " + cad.get(i).split("=")[1] + " bd: " + latedsym);
					contadorMalos++;
				}
				break;
//                    case "453":
//                           if (cad.get(i).split("=")[1].equals(Nopartys)) {
//                                 System.out.println("iguales:  cadena: "+cad.get(i).split("=")[1]+" symbolbd: "+Nopartys);
//                           }else
//                                 System.out.println("diferentes:  cadena: "+cad.get(i).split("=")[1]+" symbolbd: "+Nopartys);
//                           break;

			default:
				break;
			}
//                  System.out.println(valor);
		}
		System.out.println("----------------------------------------");
		System.out.println("LAS VALIDACIONES CORRECTAS FUERON : " + contadorBuenos);
		System.out.println("LAS VALIDACIONES ERRADAS FUERON : " + contadorMalos);
		System.out.println("TOTAL VALIDACIONES REALIZADAS : " + (contadorBuenos + contadorMalos));

	}

	public void ValidaS() throws SQLException, InterruptedException {
		int contadorBuenos = 0;
		int contadorMalos = 0;
		String cadena = getCadenaAI();
		ResultSet resultset;
		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos "
				+ "WHERE ID_ESCENARIO = 'FIX_S' and ID_CASE = 1";

		resultset = DataAccess.getQuery(queryMessageR);
		ArrayList<String> cad = FragmentarCadena(cadena);
		String valor;
		String val;
		String type = null, symbol = null, subtype = null, side = null, order = null, time = null, latedsym = null,
				Nopartys = null;
		while (resultset.next()) {
			type = resultset.getString("RS_MSGTYPE");
			symbol = resultset.getString("RS_SYMBOL");
			subtype = resultset.getString("RS_SECSUBTYPE");
			side = resultset.getString("RS_SIDE");
			order = resultset.getString("RS_ORDERQTY");
			time = resultset.getString("RS_VALIDUNTILTIME");
//                  latedsym=resultset.getString("RS_NORELATEDSYM");
//                  Nopartys=resultset.getString("RS_NOPARTYIDS");
//                  System.out.println("type: "+type+"symbol: "+symbol);        
		}
		System.out.println("----------------------------------------");
		System.out.println("VALIDACION DEL AI CORRESPONDIENTE AL S ENVIADO POR EL RECEPTOR");
		System.out.println("  \n");
		for (int i = 0; i < cad.size(); i++) {
			valor = cad.get(i).split("=")[0];
			val = cad.get(i).split("=")[1];
			switch (valor) {
			case "55":
				if (cad.get(i).split("=")[1].equals(symbol)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(55): " + cad.get(i).split("=")[1] + " bd: " + symbol);
				} else {
					System.out.println("diferentes:  cadena(55): " + cad.get(i).split("=")[1] + " bd: " + symbol);
					contadorMalos++;
				}
				break;
			case "35":
				if (cad.get(i).split("=")[1].equals(type)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(35): " + cad.get(i).split("=")[1] + " bd: " + type);
				} else {
					System.out.println("diferentes:  cadena(35): " + cad.get(i).split("=")[1] + " bd: " + type);
					contadorMalos++;
				}
				break;
			case "762":
				if (cad.get(i).split("=")[1].equals(subtype)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(762): " + cad.get(i).split("=")[1] + " bd: " + subtype);
				} else {
					System.out.println("diferentes:  cadena(762): " + cad.get(i).split("=")[1] + " bd: " + subtype);
					contadorMalos++;
				}
				break;
			case "54":
				if (cad.get(i).split("=")[1].equals(side)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(54): " + cad.get(i).split("=")[1] + " bd: " + side);
				} else {
					System.out.println("diferentes:  cadena(54): " + cad.get(i).split("=")[1] + " bd: " + side);
					contadorMalos++;
				}
				break;
			case "38":
				if (cad.get(i).split("=")[1].equals(order)) {
					contadorBuenos++;
					System.out.println("iguales:  cadena(38): " + cad.get(i).split("=")[1] + " bd: " + order);
				} else {
					System.out.println("diferentes:  cadena(38): " + cad.get(i).split("=")[1] + " bd: " + order);
					contadorMalos++;
				}
				break;
//			case "52":
//				if (cad.get(i).split("=")[1].equals(time)) {
//					System.out.println("iguales:  cadena: " + cad.get(i).split("=")[1] + " symbolbd: " + time);
//				} else
//					System.out.println("diferentes:  cadena: " + cad.get(i).split("=")[1] + " symbolbd: " + time);
//				break;
			default:
				break;
			}
		}
		System.out.println("----------------------------------------");
		System.out.println("LAS VALIDACIONES CORRECTAS FUERON : " + contadorBuenos);
		System.out.println("LAS VALIDACIONES ERRADAS FUERON : " + contadorMalos);
		System.out.println("TOTAL VALIDACIONES REALIZADAS : " + (contadorBuenos + contadorMalos));

	}

	public void validarOcho(String idAfiliado) throws SQLException {
		int contadorBuenos = 0;
		int contadorMalos = 0;
		String cadena = getCadenaOcho();
		ResultSet resultset;
		String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos "
				+ "WHERE ID_ESCENARIO = 'FIX_8' and ID_CASE = 1 and ID_AFILIADO =" + idAfiliado;

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
		System.out.println("VALIDACION DEL EXECUTION REPORT PARA EL: " + idAfiliado);
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
				if (cad.get(i).split("=")[1].equals(g)) {
					contadorBuenos++;
					System.out.println("iguales:  Execution Report(1081): " + cad.get(i).split("=")[1] + " bd: " + g);
				} else {
					System.out
							.println("diferentes:  Execution Report(1081): " + cad.get(i).split("=")[1] + " bd: " + g);
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
