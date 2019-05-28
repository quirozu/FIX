package co.bvc.com.basicfix;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;

public class DataAccess {

	private static String _usuario;
	private static String _pwd;
	private static String _db;
	private static String HOST;
	private static String PORT;
	private static Connection conn = null;

	public void Conexion() throws SQLException {
		ParametersRead p = new ParametersRead();
		String[] lineas = p.leerConexion();
		_usuario = lineas[0].split("=")[1].trim();
		_pwd = lineas[1].split("=")[1].trim();
		_db = lineas[2].split("=")[1].trim();
		HOST = lineas[3].split("=")[1].trim();
		PORT = lineas[4].split("=")[1].trim();
		String _url = "jdbc:mysql://" + HOST + ":" + PORT + "/" + _db;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(_url, _usuario, _pwd);
			if (conn != null) {
				System.out.println("CONECTADO ");
			}
		} catch (ClassNotFoundException c) {
			System.out.println("error");
		}
	}

	public static ResultSet getQuery(String _query) throws SQLException {
		Statement state = null;
		ResultSet resultSet = null;
		try {
			state = (Statement) conn.createStatement();
			resultSet = state.executeQuery(_query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return resultSet;
	}

	public static void setQuery(String _query) throws SQLException {
		Statement state = null;
		int resultSet;
		try {
			state = (Statement) conn.createStatement();
			resultSet = state.executeUpdate(_query);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static String getStringCache(ResultSet rs, String session) {
		String query;
		
		"INSERT INTO `bvc_automation_db`.`aut_fix_rfq_cache`
		(`RECEIVER_SESSION`,
				`ID_CASESEQ`,
				`ID_CASE`,
				`ID_SECUENCIA`,
				`ESTADO`,
				`ID_ESCENARIO`,
				`ACCION`,
				`ID_AFILIADO`,
				`FECHA_CREACION`,
				`FECHA_MODIFICACION`,
				`RQ_MSGTYPE`,
				`RQ_TRADER`,
				`RQ_FIRM`,
				`RQ_SESSION`,
				`RQ_RELATEDID`,
				`RQ_SYMBOL`,
				`RQ_SECSUBTYPE`,
				`RQ_ACCOUNT`,
				`RQ_SIDE`,
				`RQ_ORDERQTY`,
				`RQ_VALIDUNTILTIME`,
				`RQ_BIDPX`,
				`RQ_BIDYIELD`,
				`RQ_OFFERPX`,
				`RQ_OFFERYIELD`,
				`RQ_BIDSIZE`,
				`RQ_OFFERSIZE`,
				`RQ_QUOCANCTYPE`,
				`RQ_QUORESPTYPE`,
				`RQ_NORELATEDSYM`,
				`RQ_NOPARTYIDS`,
				`RQ_QUOTERESPID`,
				`RQ_QUOTEID`,
				`RS_RELATEDID`,
				`RS_QUOTEID`,
				`RS_QUOTERESPID`,
				`RS_MSGTYPE`,
				`RS_SYMBOL`,
				`RS_SECSUBTYPE`,
				`RS_QUOTECANCELTYPE`,
				`RS_REJREASON`,
				`RS_SIDE`,
				`RS_ACCOUNT`,
				`RS_ORDERQTY`,
				`RS_BIDPX`,
				`RS_BIDYIELD`,
				`RS_OFFERPX`,
				`RSOFFERYIELD`,
				`RS_BIDSIZE`,
				`RS_OFFERSIZE`,
				`RS_VALIDUNTILTIME`,
				`RS_QUOTESTATUS`,
				`RS_QREQREJREASON`,
				`RS_TEXT`,
				`RS_NORELATEDSYM`,
				`RS_NOPARTYIDS`,
				`ER_IDCASE`,
				`ER_IDREQUEST`,
				`ER_IDEXEC`,
				`ER_CLORDID`,
				`ER_EXECID`,
				`ER_ORDERID`,
				`ER_ORIGCLORDID`,
				`ER_SECUNDARYCLORDID`,
				`ER_SECUNDARYORDERID`,
				`ER_ORDSTATUSREQID`,
				`ER_NOPARTYIDS`,
				`ER_AVGPX`,
				`ER_CUMQTY`,
				`ER_LASTPX`,
				`ER_LASTQTY`,
				`ER_STARTCASH`,
				`ER_ENDCASH`,
				`ER_MINQTY`,
				`ER_TRANSACTTIME`,
				`ER_TRADEDATE`,
				`ER_EXPIREDATE`,
				`ER_EXPIRETIME`,
				`ER_SETTITYPE`,
				`ER_SETTIDATE`,
				`ER_ORDREJREASON`,
				`ER_TRMATCHID`,
				`ER_TRDTYPE`,
				`ER_TRDSUBTYPE`,
				`ER_TRADELEGREFID`,
				`ER_AGGRINDICATOR`,
				`ER_DISPLAYQTY`,
				`ER_COPYMSGINDICATOR`,
				`ER_ORDSTATUS`,
				`ER_EXECTYPE`,
				`ER_SYMBOL`,
				`ER_SECSUBTYPE`,
				`ER_SENDERSUBID`,
				`ER_ACCOUNT`,
				`ER_EXECINST`,
				`ER_ORDERQTY`,
				`ER_ORDERTYPE`,
				`ER_SIDE`,
				`ER_MAXFLOOR`,
				`ER_TOTALNETVALUE`,
				`ER_DIRTYPRICE`,
				`ER_PRICE`,
				`ER_YIELD`,
				`ER_TRIGGERPRICE`,
				`ER_LEAVEQTY`,
				`ER_AGREEID`,
				`ER_STARTDATE`,
				`ER_ENDDATE`,
				`ER_UNDERSYMBOL`,
				`ER_STIPULATIONTYPE`,
				`ER_STIPULATIONVALUE`,
				`ER_GROSSTRADEAMT`,
				`ER_TIMEINFORCE`,
				`ER_TEXT`,
				`ER_FILLYIELD`,
				`ER_REFORDERID`,
				`ER_REFORDERIDSCR`,
				`ER_PARTYIDSOURCE`,
				`ER_SETTLOCVAL`,
				`ER_SETTLOC`,
				`ER_EXECFIRMVAL`,
				`ER_EXECFIRM`,
				`ER_EXECTRADERVAL`,
				`ER_EXECTRADER`,
				`ER_CONTRATRADERVAL`,
				`ER_CONTRATRADER`,
				`ER_CONTRAFIRMVAL`,
				`ER_CONTRAFIRM`,
				`ER_ENTERTRADERVAL`,
				`ER_ENTERTRADER`))
		VALUES
		("+inicio.getSessionId2()" +", " + rs.getString('ID_CASESEQ') + ", " + rs.getString('ID_CASE') + ", " + rs.getString('ID_SECUENCIA') + ", " + rs.getString('ESTADO') + ", " +
		rs.getString('ID_ESCENARIO') + ", " + rs.getString('ACCION') + ", " + rs.getString('ID_AFILIADO') + ", " + rs.getString('FECHA_CREACION') + ", " + rs.getString('FECHA_MODIFICACION') + ", " +
		rs.getString('RQ_MSGTYPE') + ", " + rs.getString('RQ_TRADER') + ", " + rs.getString('RQ_FIRM') + ", " + rs.getString('RQ_SESSION') + ", " +
		rs.getString('RQ_RELATEDID') + ", " + rs.getString('RQ_SYMBOL') + ", " + rs.getString('RQ_SECSUBTYPE') + ", " + rs.getString('RQ_ACCOUNT') + ", " +
		rs.getString('RQ_SIDE') + ", " + rs.getString('RQ_ORDERQTY') + ", " + rs.getString('RQ_VALIDUNTILTIME') + ", " + rs.getString('RQ_BIDPX') + ", " +
		rs.getString('RQ_BIDYIELD') + ", " + rs.getString('RQ_OFFERPX') + ", " + rs.getString('RQ_OFFERYIELD') + ", " + rs.getString('RQ_BIDSIZE') + ", " +
		rs.getString('RQ_OFFERSIZE') + ", " + rs.getString('RQ_QUOCANCTYPE') + ", " + rs.getString('RQ_QUORESPTYPE') + ", " + rs.getString('RQ_NORELATEDSYM') + ", " +
		rs.getString('RQ_NOPARTYIDS') + ", " + rs.getString('RQ_QUOTERESPID') + ", " + rs.getString('RQ_QUOTEID') + ", " + rs.getString('RS_RELATEDID') + ", " +
		rs.getString('RS_QUOTEID') + ", " + rs.getString('RS_QUOTERESPID') + ", " + rs.getString('RS_MSGTYPE') + ", " + rs.getString('RS_SYMBOL') + ", " +
		rs.getString('RS_SECSUBTYPE') + ", " + rs.getString('RS_QUOTECANCELTYPE') + ", " + rs.getString('RS_REJREASON') + ", " + rs.getString('RS_SIDE') + ", " +
		rs.getString('RS_ACCOUNT') + ", " + rs.getString('RS_ORDERQTY') + ", " + rs.getString('RS_BIDPX') + ", " + rs.getString('RS_BIDYIELD') + ", " +
		rs.getString('RS_OFFERPX') + ", " + rs.getString('RSOFFERYIELD') + ", " + rs.getString('RS_BIDSIZE') + ", " + rs.getString('RS_OFFERSIZE') + ", " +
		rs.getString('RS_VALIDUNTILTIME') + ", " + rs.getString('RS_QUOTESTATUS') + ", " + rs.getString('RS_QREQREJREASON') + ", " + rs.getString('RS_TEXT') + ", " +
		rs.getString('RS_NORELATEDSYM') + ", " + rs.getString('RS_NOPARTYIDS') + ", " + rs.getString('ER_IDCASE') + ", " + rs.getString('ER_IDREQUEST') + ", " +
		rs.getString('ER_IDEXEC') + ", " + rs.getString('ER_CLORDID') + ", " + rs.getString('ER_EXECID') + ", " + rs.getString('ER_ORDERID') + ", " +
		rs.getString('ER_ORIGCLORDID') + ", " + rs.getString('ER_SECUNDARYCLORDID') + ", " + rs.getString('ER_SECUNDARYORDERID') + ", " + rs.getString('ER_ORDSTATUSREQID') + ", " +
		rs.getString('ER_NOPARTYIDS') + ", " + rs.getString('ER_AVGPX') + ", " + rs.getString('ER_CUMQTY') + ", " + rs.getString('ER_LASTPX') + ", " +
		rs.getString('ER_LASTQTY') + ", " + rs.getString('ER_STARTCASH') + ", " + rs.getString('ER_ENDCASH') + ", " + rs.getString('ER_MINQTY') + ", " +
		rs.getString('ER_TRANSACTTIME') + ", " + rs.getString('ER_TRADEDATE') + ", " + rs.getString('ER_EXPIREDATE') + ", " + rs.getString('ER_EXPIRETIME') + ", " +
		rs.getString('ER_SETTITYPE') + ", " + rs.getString('ER_SETTIDATE') + ", " + rs.getString('ER_ORDREJREASON') + ", " + rs.getString('ER_TRMATCHID') + ", " +
		rs.getString('ER_TRDTYPE') + ", " + rs.getString('ER_TRDSUBTYPE') + ", " + rs.getString('ER_TRADELEGREFID') + ", " + rs.getString('ER_AGGRINDICATOR') + ", " +
		rs.getString('ER_DISPLAYQTY') + ", " + rs.getString('ER_COPYMSGINDICATOR') + ", " + rs.getString('ER_ORDSTATUS') + ", " + rs.getString('ER_EXECTYPE') + ", " +
		rs.getString('ER_SYMBOL') + ", " + rs.getString('ER_SECSUBTYPE') + ", " + rs.getString('ER_SENDERSUBID') + ", " + rs.getString('ER_ACCOUNT') + ", " +
		rs.getString('ER_EXECINST') + ", " + rs.getString('ER_ORDERQTY') + ", " + rs.getString('ER_ORDERTYPE') + ", " + rs.getString('ER_SIDE') + ", " +
		rs.getString('ER_MAXFLOOR') + ", " + rs.getString('ER_TOTALNETVALUE') + ", " + rs.getString('ER_DIRTYPRICE') + ", " + rs.getString('ER_PRICE') + ", " +
		rs.getString('ER_YIELD') + ", " + rs.getString('ER_TRIGGERPRICE') + ", " + rs.getString('ER_LEAVEQTY') + ", " +
		rs.getString('ER_AGREEID') + ", " + rs.getString('ER_STARTDATE') + ", " + rs.getString('ER_ENDDATE') + ", " + rs.getString('ER_UNDERSYMBOL') + ", " +
		rs.getString('ER_STIPULATIONTYPE') + ", " + rs.getString('ER_STIPULATIONVALUE') + ", " + rs.getString('ER_GROSSTRADEAMT') + ", " + rs.getString('ER_TIMEINFORCE') + ", " +
		rs.getString('ER_TEXT') + ", " + rs.getString('ER_FILLYIELD') + ", " + rs.getString('ER_REFORDERID') + ", " + rs.getString('ER_REFORDERIDSCR') + ", " +
		rs.getString('ER_PARTYIDSOURCE') + ", " + rs.getString('ER_SETTLOCVAL') + ", " + rs.getString('ER_SETTLOC') + ", " + rs.getString('ER_EXECFIRMVAL') + ", " +
		rs.getString('ER_EXECFIRM') + ", " + rs.getString('ER_EXECTRADERVAL') + ", " + rs.getString('ER_EXECTRADER') + ", " + rs.getString('ER_CONTRATRADERVAL') + ", " +
		rs.getString('ER_CONTRATRADER') + ", " + rs.getString('ER_CONTRAFIRMVAL') + ", " + rs.getString('ER_CONTRAFIRM') + ", " + rs.getString('ER_ENTERTRADERVAL') + ", " +
		rs.getString('ER_ENTERTRADER');

		
	}

}
