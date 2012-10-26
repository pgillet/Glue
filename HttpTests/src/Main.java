import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;


public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		HttpClient httpclient = new DefaultHttpClient();
		
		URIBuilder builder = new URIBuilder();
		builder.setScheme("http").setHost("badminton-toulouse.fr").setPath("/tests/connection.php")
		    .setParameter("username", "admin")
		    .setParameter("password", "234akalum");
		URI uri = null;
		try {
			uri = builder.build();
		} catch (URISyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		HttpPost httppost = new HttpPost(uri);
		System.out.println(httppost.getURI());
		
		HttpResponse response = null;
		try {
			response = httpclient.execute(httppost);
			HttpEntity entity = response.getEntity();
			System.out.println(entity);
			System.out.println(entity.getContentType());
		    System.out.println(entity.getContentLength());
		    System.out.println(EntityUtils.toString(entity));
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println(e);
		}		
	}

}
