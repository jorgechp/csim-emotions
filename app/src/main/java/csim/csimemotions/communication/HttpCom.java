package csim.csimemotions.communication;




import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.util.Map;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.client.params.ClientPNames;

/**
 * Define la comunicación con un servidor utilizando el protocolo http
 * Created by jorge on 6/02/16.
 */
public class HttpCom {
    /**
     * Tiempo de espera maximo, en segundos
     */
    private static final int MAX_HIT = 350;


    private AsyncHttpClient client;
    private String BASE_URL;
    private AsyncHttpResponseHandler respuesta;

    private byte[] response;
    private boolean isResponseReceived;

    public HttpCom(String url) {
        this.BASE_URL = url;
        this.client = new AsyncHttpClient();
        this.client.setEnableRedirects(true);
        this.isResponseReceived = false;


        this.respuesta = new AsyncHttpResponseHandler() {
            @Override
            public synchronized void onSuccess(int i, Header[] headers, byte[] bytes) {
                HttpCom.this.response = bytes;
                isResponseReceived = true;
            }

            @Override
            public synchronized void onFailure(int i, Header[] headers, byte[] bytes, Throwable throwable) {

                isResponseReceived = true;
            }
        };
    }

    private void get(RequestParams params) {
        isResponseReceived = false;
        client.get(BASE_URL, params, this.respuesta);
    }

    private void post( RequestParams params) {
        isResponseReceived = false;
        client.post(BASE_URL, params, this.respuesta);
    }

    private String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }

    public void communicate(Map<String,String> paramsInput) {

        RequestParams params = new RequestParams(paramsInput);

        this.post(params);

    }

    /**
     * Comprueba si existe el usuario, en la base de datos
     * @param username
     * @return
     */
    public void isUserExists(String username) {

        RequestParams params = new RequestParams();
        params.put("nickName", username);
        this.post(params);

        int contador = 0;



        if(this.response != null){
            char[] resp = new char[response.length];

            for(int c = 0; c < response.length; ++c){
                resp[c] = (char) response[c];
            }
        }

    }

    public boolean isResponseReceived() {
        return isResponseReceived;
    }

    public void procesar(){
        int a = 0;
    }
}
