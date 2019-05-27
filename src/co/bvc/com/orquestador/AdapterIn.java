package co.bvc.com.orquestador;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.test.CreateMessage;
import co.bvc.com.test.Login;
import co.bvc.com.test.TestApplicationImpl;
import quickfix.SessionNotFound;
import quickfix.fix44.Message;

public class AdapterIn {

	Login logon = new Login();
	
	AdapterOut adapterOut = new AdapterOut();
	
	public ArrayList<ResultSet> datosIn () throws SQLException, SessionNotFound, InterruptedException {
		
		ArrayList<ResultSet> caso = new ArrayList<ResultSet>();
		
		for(int i=19; i<20; i++) {
			
			ResultSet resultsetCantidad;
			
			String cantidadEsc = "SELECT count(1) as cantidad FROM bvc_automation_db.aut_fix_rfq_datos where ID_CASE=" + i;
				
			resultsetCantidad  = DataAccess.getQuery(cantidadEsc);	
			while (resultsetCantidad.next()) {
				
				int cantidadEscenarios = resultsetCantidad.getInt("cantidad"); 				
				System.out.println("ESCENARIO " + i);
				tipoMensaje(consultaMensajes(i, cantidadEscenarios));
				
			}
						
		}
		
//		System.out.println(caso);
		return caso;
		
	}
	
	public ArrayList<ResultSet> consultaMensajes(int i, int cantidad) throws SQLException {
						

		ArrayList<ResultSet> mensaje = new ArrayList<ResultSet>();
		
		for (int j=1; j<=cantidad; j++) {
			
			ResultSet consultaM;
			String consulta = "SELECT * FROM bvc_automation_db.aut_fix_rfq_datos where ID_CASE=" + i +" and ID_SECUENCIA=" + j;
			consultaM = DataAccess.getQuery(consulta);
			mensaje.add(consultaM);
			
		}
		
		return mensaje;
		
	}
	
	
	public ArrayList<String> tipoMensaje(ArrayList<ResultSet> mensaje) throws SessionNotFound, InterruptedException, SQLException {

		ArrayList<String> msg = new ArrayList<String>();
		Message mess = new Message();
		String idQuoteReqFound = TestApplicationImpl.getIDQuoteFound();
	
		
		for(int j=0; j<mensaje.size(); ) {
			
			ResultSet rs = mensaje.get(j);
//			System.out.println(mensaje.get(j));
			while(rs.next()) {
				
				String tipoMensaje = mensaje.get(j).getString("RQ_MSGTYPE");
				
//				System.out.println(mensaje);
				
				switch(tipoMensaje) {
				
				case "R":
					
					adapterOut.processR(mensaje.get(j));
					
					break;
				
				case "S":
					
					System.out.println("Mensaje S");
					
					break;
					
				case "AJ":
					
					System.out.println("Mensaje AJ");
					break;
				
				case "Z":
					
					System.out.println("Mensaje Z");
				break;
				
				case "8":
				
					System.out.println("Mensaje 8");
				break;
			
				default:
					System.out.println("Error");
					break;
				
				}
				
				msg.add(tipoMensaje);
			}
			j++;
		
		}
		
		System.out.println(msg);
		return msg;
	}

}
