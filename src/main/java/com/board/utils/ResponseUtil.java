package com.board.utils;

import java.util.HashMap;
import java.util.Map;

public class ResponseUtil {

    // 성공 응답: { "data": { "success": true, ... } }
	public static Map<String, Object> successResponse(Object data) {
	    Map<String, Object> response = new HashMap<>();
	    response.put("success", true);
	    response.put("data", data);
	    return response;  // { "success": true, "data": data }
	}

    // 실패 응답: { "data": { "success": false, "message": "..." } }
    public static Map<String, Object> errorResponse(String message) {
        Map<String, Object> innerResponse = new HashMap<>();
        innerResponse.put("success", false);
        innerResponse.put("message", message);

        Map<String, Object> outerResponse = new HashMap<>();
        outerResponse.put("data", innerResponse);

        return outerResponse;
    }
}
