package com.citi.hackathon;

import com.fasterxml.jackson.annotation.JsonIgnore;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by GK82893 on 12/20/2016.
 */

public class ErrorResponse {
    @ApiModelProperty(required = true)
    private int code;
    @ApiModelProperty(required = true)
    private String message;
    @JsonIgnore
    private Object exception;
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Object getException() {
		return exception;
	}
	public void setException(Object exception) {
		this.exception = exception;
	}
    
    
}
