package com.savitech.fintab.util;

import org.json.simple.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


@Service
public class Response {
    public ResponseEntity<Object> successResponse(String message, HttpStatus status){
        JSONObject json = new JSONObject();
        json.put("code", status.value());
        json.put("status", "successful");
        json.put("message", message);
        return new ResponseEntity<>(json, status);
    }

    public ResponseEntity<Object> failResponse(String message, HttpStatus status){
        JSONObject json = new JSONObject();
        json.put("code", status.value());
        json.put("status", "fail");
        json.put("message", message);
        return new ResponseEntity<>(json, status);
    }
}

