package cs4520.server;

import java.io.IOException;

/**
 * @author Oliver Maskery
 *
 * Object that represents a conversation with a client, automatically invoking
 * a response listener to handle the clients response
 */
public class Conversation {
	private ResponseListener mListener;		// the response object to handle the client's response
	private Connection mClient;				// the client to talk to
	
	/**
	 * Conversation constructor
	 * @param _client The client to talk to
	 * @param _listener The listener that will handle the client's response
	 */
	public Conversation(Connection _client, ResponseListener _listener)
	{
		mListener = _listener;
		mClient = _client;
	}
	
	/**
	 * Starts the conversation by sending the first message, and then expecting a result
	 * @param _message The message to be sent to the client
	 * @throws IOException
	 */
	public void start(String _message) throws IOException
	{
		mClient.tx(_message);
		expect();
	}
	
	/**
	 * Waits for the client to send a message before letting the response be handled by the response handler
	 * @throws IOException
	 */
	public void expect() throws IOException
	{
		String response = mClient.rx();
		
		if(mListener != null)
			mListener.onResponse(this, response);
	}
}
