package com.maestrovs.slovo.common

object Phonetic {

    val wrongCombinations: MutableList<String> = mutableListOf("ЬЬ","ЬЬЬ","АЬ","ЕЬ","ИЬ","ІЬ","ОЬ","УЬ","ЯЬ","ЮЬ","ЄЬ","ЇЬ")

    val vowels: MutableList<Char> = mutableListOf('А', 'Е', 'И', 'І', 'О', 'У', 'Я', 'Ю', 'Є', 'Ї')

    const val softSign = 'Ь'


}

fun Char.isVowels() = Phonetic.vowels.contains(this)

fun Char.isConsonants() = !Phonetic.vowels.contains(this) && this != Phonetic.softSign

fun Char.isSoftSign() = Phonetic.softSign == this