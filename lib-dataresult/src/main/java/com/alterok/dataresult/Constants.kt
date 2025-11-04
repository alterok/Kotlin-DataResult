package com.alterok.dataresult

object Constants {
    // FileResultError Constants
    const val ERROR_MSG_FILE_NOT_FOUND = "File not found!"
    const val ERROR_MSG_FILE_READ_FAILED = "File read failed!"
    const val ERROR_MSG_FILE_WRITE_FAILED = "File write failed!"

    // PermissionResultError Constants
    const val ERROR_MSG_PERMISSION_DENIED = "Permission denied!"
    const val ERROR_MSG_PERMISSION_REVOKED = "Permission revoked!"

    // NetworkResultError Constants
    const val ERROR_MSG_BAD_REQUEST = "Bad Request"
    const val ERROR_MSG_UNAUTHORIZED = "Unauthorized"
    const val ERROR_MSG_FORBIDDEN = "Forbidden"
    const val ERROR_MSG_NOT_FOUND = "Not Found"
    const val ERROR_MSG_NO_CONTENT = "No Content"
    const val ERROR_MSG_REQUEST_TIMEOUT = "Request Timeout"
    const val ERROR_MSG_TOO_MANY_REQUESTS = "Too Many Requests"
    const val ERROR_MSG_INTERNAL_SERVER_ERROR = "Internal Server Error"
    const val ERROR_MSG_UNSUPPORTED_MEDIA_TYPE = "Unsupported Media Type"
    const val ERROR_MSG_SERVICE_UNAVAILABLE = "Service Unavailable"

    const val CODE_BAD_REQUEST = 400
    const val CODE_UNAUTHORIZED = 401
    const val CODE_FORBIDDEN = 403
    const val CODE_NOT_FOUND = 404
    const val CODE_NO_CONTENT = 204
    const val CODE_REQUEST_TIMEOUT = 408
    const val CODE_TOO_MANY_REQUESTS = 429
    const val CODE_UNSUPPORTED_MEDIA_TYPE = 415
    const val CODE_INTERNAL_SERVER_ERROR = 500
    const val CODE_SERVICE_UNAVAILABLE = 503

    // Success Codes
    const val CODE_SUCCESS_NA = Int.MAX_VALUE
    const val CODE_SUCCESS_OK = 200
    const val CODE_SUCCESS_CREATED = 201
    const val CODE_SUCCESS_ACCEPTED = 202
    const val CODE_SUCCESS_NO_CONTENT = 204
}
