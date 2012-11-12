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
		IncorrectUsername,
		IncorrectSecret,
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
	public boolean addUser(String _username, String _secret, User.Level _level)
	{
		if(mUsers.containsKey(_username))
		{
			return false;
		}
		else
		{
			mUsers.put(_username, new User(_username, new UserSecret(_secret), _level));
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
			return ValidationResult.IncorrectUsername;
		
		User user = mUsers.get(_username);
		
		if(user.isLocked())
			return ValidationResult.UserIsLocked;
		
		UserSecret storedSecret = user.secret();
		
		if(!storedSecret.checkSecret(_secret))
		{
			user.incrementAttempts();
			return ValidationResult.IncorrectSecret;
		}
		
		return ValidationResult.ValidCredentials;
	}
	
	/**
	 * Fetches a user from the User Manager, returns null if not a valid username
	 * @param _username The username to search for
	 * @return The User object requested or null if such a user does not exist
	 */
	public User getUser(String _username)
	{
		return mUsers.get(_username);
	}
	
	/**
	 * Fetch the entire user information database as a string including the usernames and passwords
	 * @return The user database contents as a string
	 */
	public String getUserData()
	{
		boolean first = true;
		
		String result = "";
		result += "[";
		for(User user : mUsers.values())
		{
			if(!first) result += ",";
			first = false;
			result += user.username() + "|" + user.secret().toString();
		}
		result += "]";
		return result;
	}
}
