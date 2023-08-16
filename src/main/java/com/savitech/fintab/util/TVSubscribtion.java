package com.savitech.fintab.util;


import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;

@Component
public class TVSubscribtion {
    
    public JsonNode getTvVariant(String product) throws JsonMappingException, JsonProcessingException{
        HttpResponse<String> response = Unirest.get(String.format("https://sandbox.vtpass.com/api/service-variations?serviceID=%s", product))
        .asString();
        ObjectMapper objectMapper = new ObjectMapper();

        // Convert JSON string to JsonNode
        JsonNode jsonNode = objectMapper.readTree(response.getBody());
		return jsonNode.get("content").get("varations");

    }
}
