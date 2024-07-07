package com.robbiebowman

import com.github.doyaaaaaken.kotlincsv.dsl.csvReader

class Dictionary {

    val words: Set<String>

    init {
        words = getCOCAWordsFromFile("BNC_COCA_lists.csv")
    }

    private fun getCOCAWordsFromFile(path: String): Set<String> {
        val minFrequency = 40
        val resource = this::class.java.classLoader.getResource(path)
        val words = csvReader().readAll(resource!!.readText()).drop(1)
            .flatMap { (listName, headword, relatedForms, totalFrequency, blank) ->
                val forms = relatedForms.trim('"').split(',').mapNotNull { str ->
                    val (word, numInParen) = str.trim().split(' ')
                    val num = numInParen.trim('(', ')').toInt()
                    if (num < minFrequency) null else word
                }
                forms
            }.toSet()
        return words
    }

    private fun getRowsFromFile(path: String): List<String> {
        val resource = this::class.java.classLoader.getResource(path)
        val lines = resource!!.readText().split("\\n".toRegex())
        return lines
    }

}