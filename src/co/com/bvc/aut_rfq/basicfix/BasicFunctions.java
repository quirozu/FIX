package co.com.bvc.aut_rfq.basicfix;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import co.com.bvc.aut_rfq.db.DataAccess;

public class BasicFunctions {

	private static Map<String, String> quoteReqId = new HashMap<String, String>();
	private static String quoteIdGenered;
	private static String quoteId;
	private static long idEjecution;
	private static int idCaseSeq;
	private static int idCase;
	private static int escenarioPrueba;
	private static String iniciator;
	private static String receptor;
	private static int escenarioFinal;
	private static boolean allMarket = false;
	private static int numEscenario;
	private static int idTcrSeq;
	private static Map<String, String> cuentas = new HashMap<String, String>();

	public static void addCuenta(String k, String v) {
		BasicFunctions.cuentas.put(k, v);
	}

	public static String getCuentaOfAfiliado(String afiliado) {
		return BasicFunctions.cuentas.get(afiliado);
	}
	
	public static Map<String, String> getCuentas() {
		return cuentas;
	}

	public static void setCuentas(Map<String, String> cuentas) {
		BasicFunctions.cuentas = cuentas;
	}

	public static int getNumEscenario() {
		return numEscenario;
	}

	public static void setNumEscenario(int numEscenario) {
		BasicFunctions.numEscenario = numEscenario;
	}

	public static int getIdTcrSeq() {
		return idTcrSeq;
	}

	public static void setIdTcrSeq(int idTcrSeq) {
		BasicFunctions.idTcrSeq = idTcrSeq;
	}

	public static int getEscenarioFinal() {
		return escenarioFinal;
	}

	public static void setEscenarioFinal(int escenarioFinal) {
		BasicFunctions.escenarioFinal = escenarioFinal;
	}

	public static String getReceptor() {
		return receptor;
	}

	public static void setReceptor(String receptor) {
		BasicFunctions.receptor = receptor;
	}

	public static boolean isAllMarket() {
		return allMarket;
	}

	public static void setAllMarket(boolean allMarket) {
		BasicFunctions.allMarket = allMarket;
	}

	public static String getIniciator() {
		return iniciator;
	}

	public static void setIniciator(String iniciator) {
		BasicFunctions.iniciator = iniciator;
	}

	public static int getIdCase() {
		return idCase;
	}

	public static void setIdCase(int d) {
		BasicFunctions.idCase = d;
	}

	// Metodo de QuoteReqId
	public static void addQuoteReqId(String k, String v) {
		BasicFunctions.quoteReqId.put(k, v);
	}

	public static String getQuoteReqIdOfAfiliado(String afiliado) {
		return BasicFunctions.quoteReqId.get(afiliado);
	}

	public static void setQuoteReqId(Map<String, String> quoteReqId) {
		BasicFunctions.quoteReqId = quoteReqId;
	}

	public static Map<String, String> getQuoteReqId() {
		return quoteReqId;
	}

	public static String getQuoteIdGenered() {
		return quoteIdGenered;
	}

	public static void setQuoteIdGenered(String quoteIdGenered) {
		BasicFunctions.quoteIdGenered = quoteIdGenered;
	}

	public static String getQuoteId() {
		return quoteId;
	}

	public static void setQuoteId(String quoteId) {
		BasicFunctions.quoteId = quoteId;
	}

	public static long getIdEjecution() {
		return idEjecution;
	}

	public static void setIdEjecution(long idEjecution) {
		BasicFunctions.idEjecution = idEjecution;
	}

	public static int getIdCaseSeq() {
		return idCaseSeq;
	}

	public static void setIdCaseSeq(int idCaseSeq) {
		BasicFunctions.idCaseSeq = idCaseSeq;
	}

	public static int getEscenarioPrueba() {
		return escenarioPrueba;
	}

	public static void setEscenarioPrueba(int escenarioPrueba) {
		BasicFunctions.escenarioPrueba = escenarioPrueba;
	}

	public static void startVariables() {
		SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmss");
		long id_ejecution = Long.parseLong(SDF.format(new Date()));
		System.out.println("ID_EJECUCION GENERADO : " + id_ejecution + "\n");

		BasicFunctions.setIdEjecution(id_ejecution);
		BasicFunctions.setNumEscenario(1);
		BasicFunctions.setIdTcrSeq(1);

	}

	public static int getFirtsIdCaseSeq(int escenarioEjecucion) throws SQLException {
		int firstIdDB = DataAccess.getFirstIdCaseSeq(escenarioEjecucion);
		return firstIdDB;
	}

	public static void imprimir(String vari) {
		System.out.println(
				"\n#####################\nCLASE: " + vari.getClass() + "VARIABLE: " + vari + "\n#####################");
	}

	public static void imprimir(int vari) {
		System.out.println("\n#####################\nVARIABLE ENTERA: " + vari + "\n#####################");
	}

	public static void imprimir(boolean vari) {
		System.out.println("\n#####################\nVARIABLE BOOLEAN: " + vari + "\n#####################");
	}

}
