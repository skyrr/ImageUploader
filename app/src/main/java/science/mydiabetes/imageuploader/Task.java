package science.mydiabetes.imageuploader;

import android.util.Base64;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * Created by sky on 24.03.2017.
 */

public class Task implements Runnable {

    @Override
    public void run() {

        System.out.println("MyString" + "inside Task");

        try {
//            System.out.println("MyString" + " inside try 1");
//            URL url = new URL("http://server.rixton.com.ua/base/hs/exchange/data/");
//            String user = "Site";
//            String pass = "Site";

//            System.out.println("MyString" + " inside try 2");
//            URLConnection uc = url.openConnection();
//            System.out.println("MyString" + " inside try 3");
//            String userPass = "Site" + ":" + "Site";
//            System.out.println("MyString" + " inside try 4");
//            byte[] encodeBytes = userPass.getBytes();
//            System.out.println("MyString" + " inside try 5");
//            uc.setRequestProperty ("Authorization", encodeBytes.toString());
//            System.out.println("MyString" + " inside try 6");
//            uc.connect();
//            System.out.println("MyString" + " inside try 7");
//            InputStream in = uc.getInputStream();
//            System.out.println("MyString" + " inside try 8");
            HttpURLConnection httpURLConnection;
            String user = "Site";
            String pass = "Site";
            String userPass = user + ":" + pass;
            byte[] data = userPass.getBytes("UTF-8");
            String base64 = Base64.encodeToString(data, Base64.DEFAULT);

            URL url = new URL("http://server.rixton.com.ua/base/hs/exchange/data/");
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("POST");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(true);
            httpURLConnection.setRequestProperty ("Authorization", "Basic " +  base64);
            httpURLConnection.setRequestProperty("Content-Length", "" +
                    Integer.toString(pass.getBytes().length));
            httpURLConnection.setRequestProperty("Content-Language", "en-US");

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    httpURLConnection.getOutputStream ());
            wr.writeBytes (pass);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = httpURLConnection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();

//            httpURLConnection.connect();

            StringBuilder builder = new StringBuilder();
            builder.append(httpURLConnection.getResponseCode())
                    .append(" ")
                    .append(httpURLConnection.getResponseMessage())
                    .append("\n");

            Map<String, List<String>> map = httpURLConnection.getHeaderFields();
            for (Map.Entry<String, List<String>> entry : map.entrySet())
            {
                if (entry.getKey() == null)
                    continue;
                builder.append( entry.getKey())
                        .append(": ");

                List<String> headerValues = entry.getValue();
                Iterator<String> it = headerValues.iterator();
                if (it.hasNext()) {
                    builder.append(it.next());

                    while (it.hasNext()) {
                        builder.append(", ")
                                .append(it.next());
                    }
                }

                builder.append("\n");
            }

            System.out.println("MyString " + builder);


        } catch (Exception e) {
            System.out.println("MyString" + " error");
        }
    }

    private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
        StringBuilder result = new StringBuilder();
        boolean first = true;
        for(Map.Entry<String, String> entry : params.entrySet()){
            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
        }

        return result.toString();
    }}
