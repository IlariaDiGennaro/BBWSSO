package com.app;

import org.apache.camel.Exchange;

import com.app.dto.MessageDTO;
import com.google.gson.Gson;

public class BlockManager {
	public MessageDTO manageBlock(Exchange exchange) {
		// TODO fare convertitore json
		Gson gson = new Gson();
		MessageDTO message = gson.fromJson((String) exchange.getIn().getBody(), MessageDTO.class);
		//MessageDTO message = (MessageDTO) exchange.getIn().getBody();
		return message;
	}
}
