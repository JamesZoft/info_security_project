package cs4520.server;

import java.util.Calendar;

/**
 * @author James Reed
 *
 */
public class User {
	private final long Timeout = 15*60*1000;	// The timeout used to block multiple logins after max login attempts
	private final int MaxLoginAttempts = 3;		// Max login attempts allowed before blocking the user
	
	private long mLastTimeCheck;				// Variable to help check whether user's timeout has ended yet
	private int mLoginAttempts;					// Number of times user has tried to login
	private boolean mLocked;					// Variable set to true if user is locked from attempting to log in
	
	private UserSecret mSecret;					// The user's password
	private String mUsername;					// The user's username
	
	/**
	 * Constructor for the User object
	 * @param _username The user's username
	 * @param _secret The user's password or "secret"
	 */
	public User(String _username, UserSecret _secret)
	{
		mLastTimeCheck = 0;
		mLoginAttempts = 0;
		mLocked = false;
		
		mUsername = _username;
		mSecret = _secret;
	}
	
	/**
	 * Method to increment the number of attempted logins by 1
	 * This method also checks whether the user has reached the max login attempts, and if so, locks the user out
	 */
	public void incrementAttempts()
	{
		System.out.println("login attempts" + mLoginAttempts);
		if(mLoginAttempts == MaxLoginAttempts)
		{
			mLocked = true;
			mLastTimeCheck = Calendar.getInstance().getTimeInMillis();
		}
		else
			mLoginAttempts++;
	}
	
	/**
	 * Method to check if user is locked or not
	 * @return A boolean; if true, user is locked 
	 */
	public boolean isLocked()
	{
		if(mLocked && ((Calendar.getInstance().getTimeInMillis() - mLastTimeCheck) > Timeout))
		{
			mLocked = false;
			mLoginAttempts = 0;
		}
		return mLocked;
	}
	
	/**
	 * Method to get the number of failed login attempts the user has
	 * @return An int which describes the number of failed login attempts the user has
	 */
	public int attempts()
	{
		return mLoginAttempts;
	}

	/**
	 * Method to get the secret of the user
	 * @return A String, the value of which is the user's secret
	 */
	public UserSecret secret()
	{
		return mSecret;
	}

	/**
	 * Getter method for retrieving the user's username
	 * @return The user's username as a String
	 */
	public String username()
	{
		return mUsername;
	}
}
