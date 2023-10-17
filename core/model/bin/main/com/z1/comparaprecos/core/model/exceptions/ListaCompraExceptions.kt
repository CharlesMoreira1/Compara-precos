package com.z1.comparaprecos.core.model.exceptions

class ErrorEmptyTitle(message: String = " "): Exception(
    "${message}code: ${ExceptionsCode.TITULO_VAZIO}"
)

class ErrorInsert(message: String = " "): Exception(
    "${message}code: ${ExceptionsCode.ERROR_CREATE}"
)

class ErrorUpdate(message: String = " "): Exception(
    "${message}code: ${ExceptionsCode.ERROR_UPDATE}"
)

class ErrorDelete(message: String = " "): Exception(
    "${message}code: ${ExceptionsCode.ERROR_DELETE}"
)

class ErrorProductExists(message: String = " "): Exception(
    "${message}code: ${ExceptionsCode.PRODUCT_EXISTS}"
)

class ErrorProductData(message: String = " ", private val uiMessageId: Int?): Exception(
    "${message}code: ${ExceptionsCode.ERROR_PRODUCT_DATA}"
) {
    fun getUiMessageId() = uiMessageId
}

class ErrorEmptyList(message: String = " "): Exception(
    "${message}code: ${ExceptionsCode.EMPTY_LIST}"
)
