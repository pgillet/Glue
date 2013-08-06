package com.glue.api.application;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.AuthCache;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.BasicAuthCache;
import org.apache.http.protocol.BasicHttpContext;

import com.glue.api.conf.Configuration;
import com.glue.api.operations.StreamOperations;
import com.glue.exceptions.GlueException;
import com.glue.struct.IMedia;
import com.glue.struct.IStream;
import com.glue.struct.IUser;
import com.glue.struct.impl.Media;
import com.glue.struct.impl.User;

public class GlueImpl implements Glue {

	private Log log = LogFactory.getLog(GlueImpl.class);

	private static ResourceBundle messages = ResourceBundle.getBundle("messages");

	private static final String USER_LOGIN = "user/login";

	private static final long serialVersionUID = -8571499192744671742L;

	protected GlueClientContext ctx = new GlueClientContext();

	private StreamOperations streamOperations;

	GlueImpl() {
		streamOperations = new StreamOperationsImpl(ctx);
	}

	@Override
	public IUser createUser(IUser user) throws GlueException {
		return createOrUpdateUser(user);
	}

	@Override
	public IUser createUser(String name, String email, String password) throws GlueException {
		// Create User DTO
		IUser user = new User();
		user.setName(name);
		user.setMailAddress(email);
		user.setPassword(password);
		return createOrUpdateUser(user);
	}

	@Override
	public IUser updateUser(IUser user) throws GlueException {
		return createOrUpdateUser(user);
	}

	private IUser createOrUpdateUser(IUser user) throws GlueException {
		return HttpHelper.sendGlueObject(ctx.getHttpClient(), user, User.class, "services/users");
	}

	@Override
	public IMedia createMedia(Long streamId, String caption, String extension, String mimeType, Double latitude,
			Double longitude, Long startDate, File file) throws GlueException {
		IMedia media = new Media();
		media.setStreamId(streamId);
		media.setCaption(caption);
		media.setExtension(extension);
		media.setMimeType(mimeType);
		media.setLatitude(latitude);
		media.setLongitude(longitude);
		media.setStartDate(startDate);
		return HttpHelper.sendGlueObject(ctx.getHttpClient(), media, Media.class, "CreateMedia", file);
	}

	@Override
	public IMedia createMedia(IMedia media, File file) throws GlueException {
		return HttpHelper.sendGlueObject(ctx.getHttpClient(), media, Media.class, "CreateMedia", file);
	}

	/**
	 * Authenticate preemptively using BASIC scheme.
	 */
	public void login(String username, String password) throws GlueException {

		try {
			URL baseURL = new URL(Configuration.getBaseUrl());

			HttpHost targetHost = new HttpHost(baseURL.getHost(), baseURL.getPort(), baseURL.getProtocol());

			ctx.getHttpClient().getCredentialsProvider().setCredentials(
					new AuthScope(targetHost.getHostName(), targetHost.getPort()),
					new UsernamePasswordCredentials(username, password));

			// Create AuthCache instance
			AuthCache authCache = new BasicAuthCache();
			// Generate BASIC scheme object and add it to the local
			// auth cache
			BasicScheme basicAuth = new BasicScheme();
			authCache.put(targetHost, basicAuth);

			// Add AuthCache to the execution context
			BasicHttpContext localcontext = new BasicHttpContext();
			localcontext.setAttribute(ClientContext.AUTH_CACHE, authCache);

			URL loginURL = new URL(baseURL, USER_LOGIN);
			HttpGet httpget = new HttpGet(loginURL.toExternalForm());

			log.info("executing request: " + httpget.getRequestLine() + " to target: " + targetHost);

			// for (int i = 0; i < 3; i++) {
			HttpResponse response = ctx.getHttpClient().execute(targetHost, httpget, localcontext);
			HttpEntity entity = response.getEntity();

			log.info("----------------------------------------");
			log.info(response.getStatusLine());

			int status = response.getStatusLine().getStatusCode();
			if (status != HttpStatus.SC_OK) {
				String msg = messages.getString("login.failed");
				throw new GlueException(msg);
			}

			if (entity != null) {
				log.info("Response content length: " + entity.getContentLength());
			}

			// EntityUtils.consume(entity);
			if (entity != null && entity.isStreaming()) {
				InputStream instream = entity.getContent();
				if (instream != null) {
					instream.close();
				}
			}
			// }

		} catch (ClientProtocolException e) {
			throw new GlueException(e);
		} catch (IOException e) {
			throw new GlueException(e);
		}
	}

	@Override
	public void logout() throws GlueException {
		// TODO Auto-generated method stub

	}

	@Override
	public IStream createStream(String title, String description, boolean publicc, boolean open, Set<String> tags,
			Map<String, String> invitedParticipants, String sharedSecretQuestion, String sharedSecretAnswer,
			boolean shouldRequestToParticipate, long startDate, long endDate, double latitude, double longitude,
			String address) throws GlueException {
		return streamOperations.createStream(title, description, publicc, open, tags, invitedParticipants,
				sharedSecretQuestion, sharedSecretAnswer, shouldRequestToParticipate, startDate, endDate, latitude,
				longitude, address);
	}

	@Override
	public IStream createStream(IStream stream) throws GlueException {
		return streamOperations.createStream(stream);
	}

	@Override
	public IStream updateStream(IStream stream) throws GlueException {
		return streamOperations.updateStream(stream);
	}

	@Override
	public List<IStream> searchStreams(String query) throws GlueException {
		return streamOperations.searchStreams(query);
	}

	@Override
	public void joinStream(long streamID) throws GlueException {
		streamOperations.joinStream(streamID);

	}

	@Override
	public void joinStream(IStream stream) throws GlueException {
		streamOperations.joinStream(stream);

	}

	@Override
	public void registerCredentials(String username, String password) {
		try {
			URL baseURL = new URL(Configuration.getBaseUrl());

			HttpHost targetHost = new HttpHost(baseURL.getHost(),
					baseURL.getPort(), baseURL.getProtocol());

			ctx.getHttpClient()
					.getCredentialsProvider()
					.setCredentials(
							new AuthScope(targetHost.getHostName(),
									targetHost.getPort()),
							new UsernamePasswordCredentials(username, password));
		} catch (MalformedURLException e) {
			// Should never be here
		}
	}
}
