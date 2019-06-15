package extentions

import java.util.*
import kotlin.streams.toList

fun BitSet.indecies() = stream().toList()
