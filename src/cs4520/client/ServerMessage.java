package cs4520.client;

import java.util.*;

/**
 * Class representing a message returned from the server
 * @author Oliver Maskery
 */
public class ServerMessage {
	public HashMap<String,String> mParameters;		// parameters as name/value pairs attached to this message
	public String mOriginal;						// original message text unparsed
	public String mHeader;							// the header of the message (plain string indicating message type)
	
	/**
	 * Constructor
	 * @param _text The plain text sent from the server
	 */
	public ServerMessage(String _text)
	{
		mParameters = new HashMap<String,String>();
		mOriginal = _text;
		mHeader = null;
		
		// split the header from the tail of the message
		String[] parts = splitResponse(mOriginal);
		
		// check the message is long enough to even have a header
		if(parts.length < 1) return;
		
		mHeader = parts[0];
		
		// check the message is long enough to have a tail
		if(parts.length < 2) return;
		
		// split the name and value in the parameter section
		String[] params = splitParameters(parts[1]);
		
		// if there is nothing in tail abort (we technically already checked this with the last check... but alas)
		if(params.length < 1) return;
		
		// store name, but prepare a null in case the message is incomplete - in which case we store the name of the parameter but flag it as a null value
		String name = params[0];
		String value = null;
		
		// fetch the value for the name/value pair
		if(params.length > 1)
		{
			value = params[1];
		}
		
		mParameters.put(name, value);
	}
	
	/**
	 * Getter for the original message text, unparsed and unmodified
	 * @return The original text
	 */
	public String original()
	{
		return mOriginal;
	}
	
	/**
	 * Getter for the message header identifying the type of message
	 * @return The message header
	 */
	public String header()
	{
		return mHeader;
	}
	
	/**
	 * Method for determining of this message contains a parameter with a given name
	 * @param _name The name of the parameter being checked for
	 * @return Whether or not the parameter is present, true if so, false otherwise
	 */
	public boolean hasParameter(String _name)
	{
		return mParameters.containsKey(_name);
	}
	
	/**
	 * Getter for retrieving a parameter of a specified name
	 * @param _name The name of the parameter to retrieve
	 * @return The String value of the name/value pair requested, if no such name exists null is returned
	 */
	public String parameter(String _name)
	{
		return mParameters.get(_name);
	}
	
	/**
	 * Split the response string of the form "[success]:[parameters]"
	 * @param _response The string to be parsed
	 * @return An array of tokens, should usually be of length 2
	 */
	private String[] splitResponse(String _response)
	{
		return _response.split(":");
	}
	
	/**
	 * Split the parameter string of the form "[parameter_name]=[parameter_value]"
	 * @param _parameterString The parameter string to be parsed
	 * @return An array of tokens, should usually be of length 2
	 */
	private String[] splitParameters(String _parameterString)
	{
		return _parameterString.split("=");
	}
}
