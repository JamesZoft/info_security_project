package cs4520.client;

/**
 * Class to represent the result of the client attempting to login to the server and performing a query
 * @author Oliver Maskery
 */
public class LoginResult {
	public enum Response
	{
		ValidCredentials,
		IncorrectUsername,
		IncorrectSecret,
		UserIsLocked,
		InvalidResponse
	}
	
	public enum Level
	{
		Guest,
		User,
		Administrator
	}
	
	private String mQueryResult;		// the result of the query sent to the server
	private Response mResponse;			// the response from the server regarding the login credentials
	private Level mLevel;				// the user privilege level according to the server upon login
	
	/**
	 * Constructor
	 * @param _response The response from the server
	 * @param _queryResult The query result from the server
	 * @param _level The user privilege level returned by the server upon login
	 */
	public LoginResult(Response _response, String _queryResult, Level _level)
	{
		mResponse = _response;
		mQueryResult = _queryResult;
		mLevel = _level;
	}
	
	/**
	 * Getter method for the response returned by the server
	 * @return The LoginResponse from the server
	 */
	public Response response()
	{
		return mResponse;
	}
	
	/**
	 * Getter method for the query response returned by the server
	 * @return The String representation of the server's response to the query
	 */
	public String queryResult()
	{
		return mQueryResult;
	}
	
	/**
	 * Getter method for the user privilege level returned by the server
	 * @return The user privilege Level from the server
	 */
	public Level level()
	{
		return mLevel;
	}
}
