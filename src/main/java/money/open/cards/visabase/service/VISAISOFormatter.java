/**
 * 
 */
package money.open.cards.visabase.service;

import money.open.cards.visabase.dto.Message;

import java.util.Hashtable;

public interface VISAISOFormatter {

	Hashtable<String, String> formatMessage(Message msgBuffer, Hashtable<String, String> isoBuffer)
			throws Exception;


	String buildISOErrorMessage(final Hashtable<String, String> isoBuffer);

	String buildSignOnMessage(Hashtable<String, String> isoBuffer);

	String buildTransactionMessage(Hashtable<String, String> isoBuffer);

}
