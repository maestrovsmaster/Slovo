package com.maestrovs.slovo.common

object WordInspector {

    private val wordL = 5

    public fun isWordWrong(word: String) =
        word.length != wordL ||
                isAllVowels(word) ||
                isManyVowelsINRow(word)||
                isAllConsonant(word) ||
                isManyConsonantsINRow(word)||
                isManySameSymbols(word) ||
                isFirstSoftSign(word) ||
                hasWrongCombination(word)


    private fun isAllConsonant(word: String): Boolean {
        var cnt = 0
        word.map {
            if (it.isConsonants()){
                cnt += 1
            }
        }
        return cnt == wordL
    }

    private fun isAllVowels(word: String): Boolean {
        var cnt = 0
        word.map {
            if (it.isVowels()){
                cnt += 1
            }
        }
        return cnt == wordL
    }

    private fun isManySameSymbols(word: String): Boolean{
        word.map { ch ->
            val countOfSameSlovoSymbols = word
                .filter { it == ch }
                .length
            return countOfSameSlovoSymbols >= wordL-1
        }
      return false
    }


    private fun isManyConsonantsINRow(word: String): Boolean{
        var cnt = 0
        word.map {
            if (it.isConsonants()){
                cnt += 1
            }else{
                cnt = 0
            }
            if(cnt >=wordL-1) return true
        }
        return false
    }

    private fun isManyVowelsINRow(word: String): Boolean{
        var cnt = 0
        word.map {
            if (it.isVowels()){
                cnt += 1
            }else{
                cnt = 0
            }
            if(cnt >=wordL-1) return true
        }
        return false
    }

    private fun isFirstSoftSign(word: String) = word[0].isSoftSign()


    private fun hasWrongCombination(word: String): Boolean{
        var has = false
        Phonetic.wrongCombinations.map {
            if(word.contains(it)){
                has = true
            }
        }
        return has
    }

}