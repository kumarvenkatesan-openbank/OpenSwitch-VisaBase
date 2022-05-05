package money.open.cards.visabase.service.impl;


import money.open.cards.visabase.dto.Message;

public interface TransactionProcessAPIImpl {

	Message authorize(Message msgBuffer);

	Message signOn(Message msgBuffer);

	Message keyEchange(Message msgBuffer);

	Message signOff(Message msgBuffer);

	Message echo(Message msgBuffer);
	
	Message sessionActivation(Message msgBuffer);

	Message respond(Message msgBuffer) throws Exception;

}
