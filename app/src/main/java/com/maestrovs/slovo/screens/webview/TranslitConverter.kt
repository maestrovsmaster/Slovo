package com.maestrovs.slovo.screens.webview

fun translitUkrToLat(text: String): String {
    val ukrToLatMap = mapOf(
        'а' to "a", 'б' to "b", 'в' to "v", 'г' to "gh", 'ґ' to "g",
        'д' to "d", 'е' to "e", 'є' to "je", 'ж' to "zh", 'з' to "z",
        'и' to "y", 'і' to "i", 'ї' to "ji", 'й' to "j", 'к' to "k",
        'л' to "l", 'м' to "m", 'н' to "n", 'о' to "o", 'п' to "p",
        'р' to "r", 'с' to "s", 'т' to "t", 'у' to "u", 'ф' to "f",
        'х' to "kh", 'ц' to "c", 'ч' to "ch", 'ш' to "sh", 'щ' to "shh",
        'ь' to "j", 'ю' to "iu", 'я' to "ja",
        'ї' to "ji"
    )

    val result = StringBuilder()
    for (char in text) {
        val translit = ukrToLatMap[char.toLowerCase()]
        if (translit != null) {
            result.append(translit)
        } else {
            result.append(char)
        }
    }
    return result.toString()
}