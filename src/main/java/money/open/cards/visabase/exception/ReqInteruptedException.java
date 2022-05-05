/**
 * 
 */
package money.open.cards.visabase.exception;

/**
 * @author Elango.D Dec 23, 2011
 */
public class ReqInteruptedException extends Exception {

	/**
	 * 
	 */
	private static final long	serialVersionUID	= 1L;

	/**
	 * 
	 */
	public ReqInteruptedException() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param message
	 */
	public ReqInteruptedException( String rspCode ) {
		super( rspCode );
	}

	/**
	 * @param cause
	 */
	public ReqInteruptedException( Throwable cause ) {
		super( cause );
	}

	/**
	 * @param message
	 * @param cause
	 */
	public ReqInteruptedException( String message, Throwable cause ) {
		super( message, cause );
	}

}
