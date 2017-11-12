package ru.spbau.mit.parser

import java.io.BufferedOutputStream
import java.io.FileOutputStream
import java.io.PrintStream
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Paths

fun redirectOut(fileName: String) {
    System.setOut(PrintStream(BufferedOutputStream(FileOutputStream(fileName))))
}

fun readFile(fileName: String): List<String> {
    return Files.readAllLines(Paths.get(fileName), Charset.defaultCharset())
}