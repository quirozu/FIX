
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.orquestador.AutoEngine;
import co.bvc.com.test.Login;
import co.bvc.com.test.Adapters;
import co.bvc.com.test.Validaciones;
import quickfix.SessionNotFound;

public class TestFix {

	public static DataAccess bd = new DataAccess();
	static Validaciones validar = new Validaciones();
	static ResultSet resultSet1;
	private static String idFound;
	static String ID_EJECUCION;
	
	public static String getIdFound() {
		return idFound;
	}

	public static void setIdFound(String idFound) {
		TestFix.idFound = idFound;
	}

	public static void main(String[] args) throws SQLException, SessionNotFound, InterruptedException {
		bd.Conexion();
		Login login = new Login();
		
		
		
		
		login.initiation();
		Adapters adapters = new Adapters(login);
		AutoEngine autoEngine = new AutoEngine(bd, login);
		
//		autoEngine.iniciarEjecucion();
		autoEngine.iniciarEjecucion();	

	}
}
