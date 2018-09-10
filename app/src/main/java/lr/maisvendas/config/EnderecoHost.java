package lr.maisvendas.config;

/**
 * Created by Ronaldo on 10/08/2017.
 */

public class EnderecoHost {

    private final String hostHTTP = "https://webservice.conveyor.cloud/api/";
    private final String hostHTTPRaiz= "http://192.168.56.1:45456/";

    public String getHostHTTP() {
        return hostHTTP;
    }

    public String getHostHTTPRaiz() {
        return hostHTTPRaiz;
    }
}
