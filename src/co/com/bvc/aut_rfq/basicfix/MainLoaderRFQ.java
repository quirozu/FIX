package co.com.bvc.aut_rfq.basicfix;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

import co.com.bvc.aut_rfq.orchestrator.AutoEngine;
import quickfix.FieldNotFound;
import quickfix.SessionNotFound;

public class MainLoaderRFQ {

	public static void main(String[] args) throws SQLException, SessionNotFound, InterruptedException, IOException, FieldNotFound {
		
		System.out.println("   __                     _                 __     ___    ____ \r\n" + 
				"  / /    ___    __ _   __| |  ___  _ __    /__\\   / __\\  /___ \\\r\n" + 
				" / /    / _ \\  / _` | / _` | / _ \\| '__|  / \\//  / _\\   //  / /\r\n" + 
				"/ /___ | (_) || (_| || (_| ||  __/| |    / _  \\ / /    / \\_/ / \r\n" + 
				"\\____/  \\___/  \\__,_| \\__,_| \\___||_|    \\/ \\_/ \\/     \\___,_\\ \r\n" + 
				"                                                               ");
		
		@SuppressWarnings("resource")
		Scanner reader = new Scanner(System. in);
		
		int escenarioInicial = 0;
		int escenarioFinal = 0;
				
		System.out.println("DIGITE PRIMER ESCENARIO A PROBAR...");
		escenarioInicial = reader.nextInt();
		System.out.println("DIGITE ULTIMO ESCENARIO A PROBAR...");
		escenarioFinal = reader.nextInt();
		
		if (escenarioFinal < escenarioInicial) {
			int aux = escenarioFinal;
			escenarioFinal = escenarioInicial;
			escenarioInicial = aux;
		}
			
		AutoEngine autoEngine = new AutoEngine();
		autoEngine.iniciarEjecucion(escenarioInicial, escenarioFinal);	

	}
}
