
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

import co.bvc.com.basicfix.BasicFunctions;
import co.bvc.com.basicfix.DataAccess;
import co.bvc.com.orquestador.AutoEngine;
import co.bvc.com.test.Login;
import co.bvc.com.test.AdapterIO;
import co.bvc.com.test.Validaciones;
import quickfix.SessionNotFound;

public class TestFix {

	public static void main(String[] args) throws SQLException, SessionNotFound, InterruptedException, IOException {
		int escenarioEjecucion = 0;
		System.out.println("DIGITE ESCENARIO A PROBAR...");
		@SuppressWarnings("resource")
		Scanner reader = new Scanner(System. in);
		escenarioEjecucion = reader.nextInt();
		
		AutoEngine autoEngine = new AutoEngine();
		autoEngine.iniciarEjecucion(escenarioEjecucion);	

	}
}
