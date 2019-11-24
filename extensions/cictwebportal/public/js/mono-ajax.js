/**
 * Request Class For AJAX.
 * @param requestType
 * @constructor
 */
function Request() {
    /**
     * Class Body, Basic Settings
     */
    // request URL
    this.url;
    // request type POST or GET
    this.type;
    // before sending callback
    this.begin; // equivalent to beforeSend call back
    // the data that will be sent to the server
    this.data;
    // content type: the data type that you will send to the server
    this.contentType;
    this.contentForm(); // default content type
    // the expected return value from the server
    this.replyType;
    // different data types
    // xml, html,  script, json, text
    // this values can be separated by spaces e.g ("text xml")

    /* @depricated
    // success callback
    this.success;
    // error callback
    this.error;
    // complete whether success or error this will be called when request was finished
    this.complete;
    */

    this.done; // success
    this.fail; // error
    this.always; // complete
    /**
     * Extended Settings.
     * Do not change its values here. The corresponding values are
     * default values. Values Modification should be apply upon
     * instantiation.
     */

    // Set this option to true if you want to force jQuery to recognize the current environment as “local”
    this.local = false;
    // Set this options to false to force requested pages not to be cached by the browser
    this.cache = false; // boolean value // works only with GET request
    // A number that specifies a timeout (in milliseconds) for the request
    this.timeout = 10000; // long value
    // Set this options to false to perform a synchronous request
    this.asynchronous = true;
    // transform data into a query string as application/x-www-form-urlencoded by default
    // if you will pass another content type make this false
    this.processData = true;

    // status code callback
    this.statusCode = {};


}

/**
 * Class Methods
 */

Request.prototype.send = function () {
    $.ajax({
        /**
         * AJAX REQUEST BODY
         */
        url: this.url,
        type: this.type, // request type
        data: this.data,
        contentType: this.contentType,

        dataType: this.replyType,
        beforeSend: this.begin,
        /*
        @depricated
        success: this.success,
        error: this.error,
        complete: this.complete,
        */
        //----
        /*
        @tailing: not included in options must be used at the end
        done: this.done,
        fail: this.fail,
        always: this.always,
        */

        //
        isLocal: this.local,
        async: this.asynchronous,
        cache: this.cache,
        timeout: this.timeout,
        processData: this.processData,

        // status codes
        statusCode: this.statusCode,

    }).done(this.done)
        .fail(this.fail)
        .always(this.always);

}

//------------- Content Types
Request.prototype.contentForm = function () {
    this.contentType = 'application/x-www-form-urlencoded; charset=UTF-8';
}
Request.prototype.contentMultiPart = function () {
    this.contentType = 'multipart/form-data';
}
Request.prototype.contentText = function () {
    this.contentType = 'text/plain';
}
Request.prototype.contentJson = function () {
    this.contentType = 'application/json';
}

