
import java.sql.SQLException;
import co.bvc.com.orquestador.AutoEngine;
import quickfix.SessionNotFound;

public class TestFix {

	public static void main(String[] args) throws SQLException, SessionNotFound, InterruptedException {
		AutoEngine autoEngine = new AutoEngine();
		
		autoEngine.iniciarEjecucion();	

	}
}
