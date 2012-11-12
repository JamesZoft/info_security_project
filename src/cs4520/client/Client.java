package cs4520.client;

import java.net.*;
import java.io.*;
import javax.net.ssl.*;

/**
 * @author Oliver Maskery
 * 
 * Represents a simple client, making a connection to the CS4520 server, logging in and acquiring the secret data.
 * 
 */
public class Client {
	private BufferedReader mReader;		// used to read from the client socket
	private PrintStream mWriter;		// used to write to the client socket
	private LoginResult mResult;		// the result of logging in to the server and performing a query
	private Socket mClient;				// the client socket to communicate with the server
	
	/**
	 * Client constructor
	 * @param _target The target to connect to, either an IP or hostname for the machine running the CS4520 server.
	 * @param _port The port number to establish the connection on, must be the same as the one the server is listening on.
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public Client(String _target, int _port) throws UnknownHostException, IOException
	{
		mResult = null;
		// establish a connection to the server, generally initialise the connection
		mClient = SSLSocketFactory.getDefault().createSocket(_target, _port);
		mReader = new BufferedReader(new InputStreamReader(mClient.getInputStream()));
		mWriter = new PrintStream(mClient.getOutputStream());
	}
	
	/**
	 * Returns the result of the query to the server
	 * @return Returns the login result for this client's connection attempt
	 */
	public LoginResult getResult()
	{
		return mResult;
	}
	
	/**
	 * Attempts to login to the CS4520 server
	 * @param _username	The username to provide as part of the login credentials
	 * @param _secret The 'secret' to provide as part of the login credentials
	 * @return Whether or not the login attempt was successful, and if not, the reason for failure
	 * @throws IOException
	 */
	public LoginResult login(String _username, String _secret, String _query) throws IOException
	{
		LoginResult.Response loginResponse = LoginResult.Response.InvalidResponse;
		LoginResult.Level privilageLevel = null;
		String queryResponse = null;
		
		// send the login credentials
		mWriter.println(_username);
		mWriter.println(_secret);
		
		// await the server's response
		ServerMessage response = new ServerMessage(mReader.readLine());
		
		if(response.header().toLowerCase().equals("valid"))
		{
			if(response.hasParameter("level"))
			{
				loginResponse = LoginResult.Response.ValidCredentials;
				privilageLevel = LoginResult.Level.valueOf(response.parameter("level"));
			}
		}
		else if(response.header().toLowerCase().equals("invalid"))
		{
			if(response.hasParameter("reason"))
			{
				loginResponse = LoginResult.Response.valueOf(response.parameter("reason"));
			}
		}
		
		if(loginResponse == LoginResult.Response.ValidCredentials)
		{
			mWriter.println(_query);
			
			response = new ServerMessage(mReader.readLine());
			
			if(response.header().toLowerCase().equals("ack"))
			{
				if(response.hasParameter(_query))
				{
					queryResponse = response.parameter(_query);
				}
			}
			else if(response.header().toLowerCase().equals("invalid"))
			{
				if(response.hasParameter("reason"))
				{
					queryResponse = response.parameter("reason");
				}
			}
		}
		
		mResult = new LoginResult(loginResponse, queryResponse, privilageLevel);
		return mResult;
	}
	
	public static void main(String[] args) {
		testAllQueries("guest", "meow");
		testAllQueries("rolfharris", "canyoutellwhatitisyet");
		testAllQueries("admin", "secretsecret");
	}
	
	public static void testAllQueries(String _username, String _secret)
	{
		test(_username, _secret, "fact");
		test(_username, _secret, "secret");
		test(_username, _secret, "users");
		test(_username, _secret, "cats");
	}

	public static void test(String _username, String _secret, String _query)
	{
		try {
			// Connect
			System.out.print("Attempting connection to CS4520 server...");
			Client client = new Client("localhost", 28000);
			System.out.println("done!");
			
			// Attempt login
			System.out.print("Attempting login as '" + _username + "' with secret '" + _secret + "' and query type '" + _query + "'...");
			LoginResult result = client.login(_username, _secret, _query);
			
			// Process response
			if(result.response() == LoginResult.Response.ValidCredentials)
			{
				System.out.println("success!");
				
				System.out.println("User privelage level: " + result.level());
				System.out.println("Query response: " + result.queryResult());
			}
			else
			{
				System.out.println("failure!");
				
				System.out.println("Reason for failure: " + result.response().toString());
			}
		} catch (UnknownHostException e) {
			e.printStackTrace(System.err);
		} catch (IOException e) {
			e.printStackTrace(System.err);
		}
	}
}
