package cs4520.server;

import java.security.NoSuchAlgorithmException;
import java.security.spec.*;
import java.util.Random;

import javax.crypto.spec.*;
import javax.crypto.*;

/**
 * Object for representing a user's secret data (password hash and salt)
 * @author Oliver Maskery
 */
public class UserSecret {
	private byte[] mHash;	// the user's secret hash
	private byte[] mSalt;	// the salt used in generating the user's secret hash
	
	/**
	 * The basic constructor for creating a new UserSecret from a secret string
	 * @param _secret The secret that should be used to generate the new UserSecret object
	 */
	public UserSecret(String _secret)
	{
		Random random = new Random();
		
		mSalt = new byte[16];
		mHash = null;
		
		// generate a new salt
		random.nextBytes(mSalt);
		// use the salt to generate a new hash from the original secret
		mHash = generateHash(_secret, mSalt);
	}
	
	/**
	 * Constructor for when loading a secret from storage and both the hash and salt are already known
	 * @param _hash The hash generated from the user's secret and the salt
	 * @param _salt The salt used to generate this user's secret
	 */
	public UserSecret(byte[] _hash, byte[] _salt)
	{
		mHash = _hash;
		mSalt = _salt;
	}
	
	/**
	 * Method used to validate that a passed secret is the same secret used to generate this UserSecret object
	 * @param _secret The secret (in plain text) that is to be checked against this object
	 * @return Whether or not the secret was correct, true if correct, false otherwise
	 */
	public boolean checkSecret(String _secret)
	{
		// generate a hash using the provided secret and the stored salt
		byte[] generated = generateHash(_secret, mSalt);
		// if the generated hash matches our stored hash, it is the correct secret
		return (generated.equals(mHash));
	}
	
	/**
	 * Internal method for generating a hash from a secret in plain text and a salt value
	 * @param _secret The secret in plain text to be hashed with the salt
	 * @param _salt The salt to be used in generating a hash of the secret
	 * @return The result of hashing the secret and the salt value together
	 */
	private byte[] generateHash(String _secret, byte[] _salt)
	{
		byte[] result = null;
		
		try
		{
			KeySpec keyspec = null;
			// define the secret, the salt and parameters to the hashing algorithm
			keyspec = new PBEKeySpec(_secret.toCharArray(), _salt, 2048, 160);
			// retrieve a factory object for generating keys (secrets)
			SecretKeyFactory f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			// generate and store the new key for returning
			result = f.generateSecret(keyspec).getEncoded();
		}
		catch(NoSuchAlgorithmException nsae)
		{
			// the algorithm we asked for with SecretKeyFactory.getInstance does not exist
			System.err.println("No such algorithm:");
			nsae.printStackTrace();
		}
		catch (InvalidKeySpecException ikse)
		{
			// the keyspec we generated and passed to the SecretKeyFactory is somehow invalid
			System.err.println("Invalid key spec:");
			ikse.printStackTrace();
		}
		
		return result;
	}
}
