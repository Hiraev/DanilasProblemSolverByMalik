package utils

import com.google.gson.Gson
import io.CmdLineArgs
import io.Input
import io.Output
import org.kohsuke.args4j.CmdLineParser
import java.io.File

fun parse(args: Array<String>) = CmdLineArgs().also {
    CmdLineParser(it).parseArgument(*args)
}

fun readInputJson(file: File) =
    Gson().fromJson(file.reader(), Input::class.java) ?: throw RuntimeException("Give me a correct input file")

fun writeOutputInFile(file: File, output: Output) = file.writeText(Gson().toJson(output))