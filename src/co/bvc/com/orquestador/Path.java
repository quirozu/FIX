package co.bvc.com.orquestador;

public enum Path {
	
    PATH_DATADICTIONARY("resources\\datadictionary\\FIX44.xml"),
    PATH_LOGS("resources\\logs\\inicial"),
  
    
    
    
     
    PATH_CONFIG_1("resources\\sessionSettings_1.cfg"),
    PATH_CONFIG_2("resources\\sessionSettings_2.cfg"),
    PATH_CONFIG_3("resources\\sessionSettings_3.cfg"),
    PATH_CONFIG_4("resources\\sessionSettings_4.cfg"),
	PATH_CONFIG_5("resources\\sessionSettings_5.cfg"),
	PATH_CONFIG_6("resources\\sessionSettings_6.cfg");
	
    private String code;
    Path(String code) {
                   this.code = code;
    }
    public String getCode() {
                   return this.code;
    }


}
