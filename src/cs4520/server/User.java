package cs4520.server;

import java.util.Calendar;

/**
 * @author James Reed
 *
 */

public class User {
	
	private String secret;						// The user's password
	private int loginAttempts = 0;				// Number of times user has tried to login
	private final long timeout = 15*60*1000;	// The timeout used to block multiple logins after max login attempts
	private final int maxLoginAttempts = 3;		// Max login attempts allowed before blocking the user
	private long lastTimeCheck = 0;				// Variable to help check whether user's timeout has ended yet
	private boolean locked = false;				// Variable set to true if user is locked from attempting to log in
	
	public User(String secret)
	{
		this.secret = secret;
	}
	
	/**
	 * Method to increment the number of attempted logins by 1
	 * This method also checks whether the user has reached the max login attempts, and if so, locks the user out
	 */
	public void incrementAttempts()
	{
		System.out.println("login attempts" + loginAttempts);
		if(loginAttempts == maxLoginAttempts)
		{
			locked = true;
			lastTimeCheck = Calendar.getInstance().getTimeInMillis();
		}
		else
			loginAttempts++;
	}
	
	/**
	 * Method to check if user is locked or not
	 * @return A boolean; if true, user is locked 
	 */
	public boolean isLocked()
	{
		if(locked && ((Calendar.getInstance().getTimeInMillis() - lastTimeCheck) > timeout))
		{
			locked = false;
			loginAttempts = 0;
		}
		return locked;
	}
	
	/**
	 * Method to get the number of failed login attempts the user has
	 * @return An int which describes the number of failed login attempts the user has
	 */
	public int getAttempts()
	{
		return loginAttempts;
	}

	/**
	 * Method to get the secret of the user
	 * @return A String, the value of which is the user's secret
	 */
	public String getSecret()
	{
		return secret;
	}

}
