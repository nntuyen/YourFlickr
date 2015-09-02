package com.nntuyen.yourflickr.app.constant;


public class FlickrApiConst {
	
	// API KEY
	public static final String API_KEY = "59aadc28a0947a7bbea6794425dd4195";
	public static final String API_SEC = "0f80bc39f801f629";

	// Rest URL
	public static final String BASE_URL = "http://flickr.com/services/rest/?";
	public static final String AUTH_URL = "http://flickr.com/services/auth/?";
	public static final String AUTH_PATH = "https://www.flickr.com/services/auth/"; // Use for getting back to previous activity
	
	public static final String METHOD_PARAM = "method";
	public static final String GET_FROB_METHOD_VALUE = "flickr.auth.getFrob";
	public static final String GET_TOKEN_METHOD_VALUE = "flickr.auth.getToken";
	public static final String GET_INFO_METHOD_VALUE = "flickr.people.getInfo";
	public static final String GET_PHOTOS_METHOD_VALUE = "flickr.people.getPhotos";
	public static final String CHECK_TOKEN_METHOD_VALUE = "flickr.auth.checkToken";
	
	public static final String FORMAT_PARAM = "format";
	public static final String JSON_FORMAT_VALUE = "json";
	
	public static final String PERMS_PARAM = "perms";
	public static final String WRITE_PERMS_VALUE = "write";
	
	public static final String API_KEY_PARAM = "api_key";
	public static final String API_SIG_PARAM = "api_sig";
	public static final String AUTH_TOKEN_PARAM = "auth_token";
	public static final String FROB_PARAM = "frob";
	public static final String USER_ID_PARAM = "user_id";
	
	public static final String FLICKR_CALLBACK = "jsonFlickrApi";
	
	// Messages
	public static final String GET_FROB_SUCCESS_MSG = "Get frob successfully";
	public static final String GET_FROB_FAIL_MSG = "Get frob fail";
	public static final String AUTH_SUCCESS_MSG = "Authenticate successfully";
	public static final String AUTH_FAIL_MSG = "Authenticate fail";
	public static final String GET_TOKEN_SUCCESS_MSG = "Get token successfully";
	public static final String GET_TOKEN_FAIL_MSG = "Get token fail";
	public static final String CHECK_TOKEN_SUCCESS_MSG = "Check token successfully";
	public static final String CHECK_TOKEN_FAIL_MSG = "Check token fail";
	public static final String GET_PHOTOS_SUCCESS_MSG = "Get photos successfully";
	public static final String GET_PHOTOS_FAIL_MSG = "Get photos fail";
}
