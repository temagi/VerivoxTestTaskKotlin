package utils

import java.io.BufferedReader
import java.io.File
import java.io.FileReader
import java.util.Properties

fun getResource(name: String): List<String> {
    val bufferedReader: BufferedReader = File("src/test/resources/$name.txt").bufferedReader(Charsets.UTF_8)
    return bufferedReader.readLines()
}

fun readProperty(name: String): String = defaultProperties.getProperty(name)

private val defaultProperties: Properties =
    Properties().apply {
        load(FileReader(File("src/test/resources/config.properties")))
    }
