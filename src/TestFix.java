
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import co.bvc.com.basicfix.BasicFunctions;
import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.orquestador.AutoEngine;
import co.bvc.com.test.Login;
import co.bvc.com.test.AdapterIO;
import co.bvc.com.test.Validaciones;
import quickfix.SessionNotFound;

public class TestFix {

	public static void main(String[] args) throws SQLException, SessionNotFound, InterruptedException {
		AutoEngine autoEngine = new AutoEngine();
		autoEngine.iniciarEjecucion();	

	}
}
