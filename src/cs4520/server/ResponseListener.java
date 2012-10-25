package cs4520.server;

import java.io.IOException;

/**
 * @author Oliver Maskery
 *
 * Interface that handles a response from a client during a Conversation (see the Conversation object)
 */
public interface ResponseListener {
	// method called when a client responds
	public void onResponse(Conversation _conversation, String _response) throws IOException;
}
