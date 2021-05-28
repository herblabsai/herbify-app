package com.herblabs.herbifyapp.vo

/**
 * Bisa menggunaka class ini untuk menangani state service internet
 */
class Resource<T>(val status: StatusMessage, val data: T?, val message: String?){
    companion object{
        fun <T> success(data: T?): Resource<T> = Resource(StatusMessage.SUCCESS, data, null)

        fun <T> error(msg: String?, data: T?): Resource<T> = Resource(StatusMessage.ERROR, data, msg)

        fun <T> loading(data: T?): Resource<T> = Resource(StatusMessage.LOADING, data, null)

        fun <T> empty(msg: String, data: T?): Resource<T> = Resource(StatusMessage.EMPTY, data, msg)
    }
}