
import java.io.IOException;
import java.sql.SQLException;
<<<<<<< HEAD
import java.util.Scanner;
=======
import java.text.SimpleDateFormat;
import java.util.Date;
import co.bvc.com.basicfix.BasicFunctions;
import co.bvc.com.basicfix.DataAccess;
>>>>>>> c6c656196ec2c91aaad3bb7dcad54df1faff29f8
import co.bvc.com.orquestador.AutoEngine;
import quickfix.FieldNotFound;
import quickfix.SessionNotFound;

public class TestFix {

	public static void main(String[] args) throws SQLException, SessionNotFound, InterruptedException, IOException, FieldNotFound {
		int escenarioEjecucion = 0;
		System.out.println("DIGITE ESCENARIO A PROBAR...");
		@SuppressWarnings("resource")
		Scanner reader = new Scanner(System. in);
		escenarioEjecucion = reader.nextInt();
		
		AutoEngine autoEngine = new AutoEngine();
		autoEngine.iniciarEjecucion(escenarioEjecucion);	

	}
}
