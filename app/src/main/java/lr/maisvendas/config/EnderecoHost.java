package lr.maisvendas.config;

/**
 * Created by Ronaldo on 10/08/2017.
 */

public class EnderecoHost {

    private final String hostHTTP = "http://192.168.15.12:8080/api/";
   // private final String hostHTTPRaiz= "https://webservice.conveyor.cloud";
    private final String hostHTTPRaiz= "http://192.168.15.12:8080";

    public String getHostHTTP() {
        return hostHTTP;
    }

    public String getHostHTTPRaiz() {
        return hostHTTPRaiz;
    }
}
