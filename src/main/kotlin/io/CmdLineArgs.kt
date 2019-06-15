package io

import org.kohsuke.args4j.Option
import java.io.File

class CmdLineArgs {

    @Option(name = "-i", required = true)
    lateinit var input: File
    @Option(name = "-o", required = true)
    lateinit var output: File

}
