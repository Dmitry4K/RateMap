package ru.dmitry4k.geomarkback.exceptions

class InvalidParametersException(
    parameterName: String,
    reason: String
) : Exception("Not valid parameter: $parameterName, reason: $reason")