package cs4520.server;

/**
 * @author Oliver Maskery
 *
 * Interface that represents a listener waiting for a Connection to finish/disconnect
 */
public interface CompletionListener {
	public void onCompletion(Object _sender);	// method called when a Connection is completed
}
