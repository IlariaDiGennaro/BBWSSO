package com.app.bl;

import org.apache.camel.Exchange;

import com.app.dto.MessageDTO;
import com.app.dto.ResponseDTO;

public class BlockManager {
	public ResponseDTO manageBlock(Exchange exchange) {
		ResponseDTO response = new ResponseDTO();
		MessageDTO message = (MessageDTO) exchange.getIn().getBody();
		//TODO manage Block
		
		// build response
		response.setReceiver(message.getSender());
		response.setSender(message.getReceiver());
		response.setValidBlock(true);
		return response;
	}
}
