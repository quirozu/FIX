//package co.bvc.com.test;
//
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import co.bvc.com.basicfix.DataAccess;
//import quickfix.Message;
//import quickfix.Session;
//import quickfix.SessionNotFound;
//
//public class Orquestador {
//	
//public ArrayList<ArrayList<ResultSet>> obtenerDatos() throws SQLException {
//		
//		
//		ArrayList<ArrayList<ResultSet>> ResultasT = new ArrayList<ArrayList<ResultSet>>();
//		
//		for(int i=0; i<2; i++) {
//			
//			
//			ArrayList<ResultSet> respuestas = new ArrayList<ResultSet>();
//			
//			ResultSet resultsetR;
//			ResultSet resultsetS;
//			ResultSet resultsetAJ;
//			
//			String queryMessageR = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos "
//					+ "WHERE ID_ESCENARIO = 'FIX_R' and ID_CASE =" + i;
//			
//			String queryMessageS = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos "
//					+ "WHERE ID_ESCENARIO = 'FIX_S' and ID_CASE = 1" + i;
//			
//			String queryMessageAJ = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos WHERE ID_ESCENARIO = 'FIX_AJ' AND ID_CASE =" + i;
//			
//			resultsetR = DataAccess.getQuery(queryMessageR);
//			resultsetS = DataAccess.getQuery(queryMessageS);
//			resultsetAJ = DataAccess.getQuery(queryMessageAJ);
//			
//			respuestas.add(resultsetR);
//			respuestas.add(resultsetS);
//			respuestas.add(resultsetAJ);
//			
//			System.out.println("R: " + resultsetR.toString()+ "\n" + "S: " + resultsetS.toString() + "\n" + "AJ: "+resultsetAJ.toString() + "\n");
//			
//			//System.out.println(respuestas.toString());
//		
//			ResultasT.add(respuestas);
//		}
//		
////		System.out.println(ResultasT.toString());
//		System.out.println(ResultasT.get(0).get(0).toString() + "\n");
//		
//		return ResultasT;
//		
//		//
//	}
//
//	public void enviarMensajes(ArrayList<ArrayList<ResultSet>> obtenerDatos) throws InterruptedException, SQLException {
//		
//		Login inicio = new Login();
//		inicio.initiation();
//		
//		if(obtenerDatos != null) {
//			
//			
//			Message mess = new Message();
//			SendMessage message = new SendMessage();
//			String idQuoteReqFound = TestApplicationImpl.getIDQuoteFound();
//			
//			for(int i=0; i<obtenerDatos.size(); i++) {
//				
//				
//				try {
//					
////					System.out.println("----------- Enviando mensaje R -----------------");
////					
//					System.out.println("-#-#-#--- R: "+obtenerDatos.get(i).get(0));
//					
//					mess = message.sendR(inicio.getSessionID1(), inicio.getcIdRandom(), obtenerDatos.get(i).get(0));
//					Session.sendToTarget(mess, inicio.getSessionID1());
//					
//					if(!idQuoteReqFound.contentEquals(null)) {
//						
//						System.out.println("EL VALOR DEL NUEVO ID ES: " + idQuoteReqFound);
//					}
//					
//					
//					
////					Message message = sm.sendR(inicio.getSessionID1(), inicio.getcIdRandom(), obtenerDatos.get(i).get(0));
//					
//					
//					//mensaje S
//					
//					
//					
//					//mensaje AJ
//					
//				} catch (SessionNotFound e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				
//				
//			}
//		}else {
//			Thread.sleep(3000);
//		}
//		
//		
//	}
//
//}
