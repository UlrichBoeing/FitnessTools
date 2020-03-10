package de.ulrich_boeing.extensions

fun String.fileNameFromPath() = this.substringAfterLast("/").substringBeforeLast(".")