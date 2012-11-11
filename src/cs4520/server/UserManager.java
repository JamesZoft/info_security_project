package cs4520.server;

import java.util.*;

/**
 * @author Oliver Maskery
 *
 * Class for managing user credentials in the system as well as performing validation
 */
public class UserManager {
	public enum ValidationResult
	{
		ValidCredentials,
		InvalidUsername,
		InvalidSecret,
		UserIsLocked
	}
	
	private HashMap<String,User> mUsers = new HashMap<String,User>();
	
	public UserManager() { }
	
	/**
	 * Method for adding new users to the UserManager
	 * @param _username The username of the new user
	 * @param _secret The user's "secret" for logging in
	 * @return Whether or not the user was successfully added, false if the username is already in use
	 */
	public boolean addUser(String _username, String _secret)
	{
		if(mUsers.containsKey(_username))
		{
			return false;
		}
		else
		{
			mUsers.put(_username, new User(_username, new UserSecret(_secret)));
			return true;
		}
	}
	
	/**
	 * Method to determine whether the user credentials system has a specified user
	 * @param _username The user to check for
	 * @return True if the system contains such a user, false otherwise
	 */
	public boolean hasUser(String _username)
	{
		return mUsers.containsKey(_username);
	}
	
	/**
	 * Method for doing login credential validation
	 * @param _username The username provided
	 * @param _secret The secret provided
	 * @return The result of the validation attempt
	 */
	public ValidationResult validateLogin(String _username, String _secret)
	{
		if(!hasUser(_username))
			return ValidationResult.InvalidUsername;
		
		User user = mUsers.get(_username);
		
		if(user.isLocked())
			return ValidationResult.UserIsLocked;
		
		UserSecret storedSecret = user.secret();
		
		if(!storedSecret.checkSecret(_secret))
		{
			user.incrementAttempts();
			return ValidationResult.InvalidSecret;
		}
		
		return ValidationResult.ValidCredentials;
	}
}
