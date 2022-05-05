/**
 * 
 */
package money.open.cards.visabase.exception;

/**
 * @author Elango.D
 * Dec 27, 2011
 */
public class RspInteruptedException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * 
	 */
	public RspInteruptedException() {
		// TODO Auto-generated constructor stub
	}
	


	/**
	 * @param message
	 */
	public RspInteruptedException( String message, String rspCode ) {
		super( rspCode );
	}

	/**
	 * @param cause
	 */
	public RspInteruptedException( Throwable cause ) {
		super( cause );
	}

	/**
	 * @param message
	 * @param cause
	 */
	public RspInteruptedException( String message, Throwable cause ) {
		super( message, cause );
	}



}
