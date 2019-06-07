
import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;
import co.bvc.com.orquestador.AutoEngine;
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
