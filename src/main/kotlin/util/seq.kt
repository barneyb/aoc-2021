package util

fun countForever() = generateSequence(0, Int::inc)

class CountedToForeverException :
    IllegalStateException("Successfully counted to forever!")
