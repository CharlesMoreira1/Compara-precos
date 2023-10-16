package com.z1.comparaprecos.core.model.exceptions

class ErrorEmptyTitle(message: String = " "): Exception(
    "${message}code: ${ExceptionsCode.TITULO_VAZIO}"
)

class ErrorInserte(message: String = " "): Exception(
    "${message}code: ${ExceptionsCode.ERROR_CREATE}"
)

class ErrorUpdate(message: String = " "): Exception(
    "${message}code: ${ExceptionsCode.ERROR_UPDATE}"
)

class ErrorDelete(message: String = " "): Exception(
    "${message}code: ${ExceptionsCode.ERROR_DELETE}"
)