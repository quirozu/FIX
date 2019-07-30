package co.com.bvc.aut_rfq.orchestrator;

public enum Path {
	
    PATH_DATADICTIONARY("resources\\datadictionary\\FIX44.xml"),
    PATH_LOGS("resources\\logs\\inicial"),
  
    
    PATH_CONFIG1("resources\\sessionSettings1.cfg"),
    PATH_CONFIG2("resources\\sessionSettings2.cfg"),
    PATH_CONFIG3("resources\\sessionSettings3.cfg"),
    PATH_CONFIG4("resources\\sessionSettings4.cfg"),
    PATH_CONFIG5("resources\\sessionSettings5.cfg"),
    PATH_CONFIG6("resources\\sessionSettings6.cfg");
	
    private String code;
    Path(String code) {
                   this.code = code;
    }
    public String getCode() {
                   return this.code;
    }


}
