package com.arturomarmolejo.acronymsappam.utils

class NullAcronymListResponse(message: String = "Acronym List response is null") : Exception(message)
class FailureResponse(message: String?): Exception(message)