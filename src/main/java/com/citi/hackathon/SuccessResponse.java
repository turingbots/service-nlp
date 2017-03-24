package com.citi.hackathon;

import io.swagger.annotations.ApiModelProperty;

/**
 * Created by GK82893 on 12/20/2016.
 */

public class SuccessResponse<T> {
   
	@ApiModelProperty( required = true)
    private T data;
    private String message;
    @ApiModelProperty( required = true)
    private int code;
    
	public T getData() {
		return data;
	}
	public void setData(T data) {
		this.data = data;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int getCode() {
		return code;
	}
	public void setCode(int code) {
		this.code = code;
	}

}

