package com.maestrovs.slovo.model

object KeysList {

     val keysList = arrayListOf<KeyUI>(
        KeyUI(Key("Й",  KeyState.None), Row.Row1, KeyType.Letter),
        KeyUI(Key("Ц",  KeyState.None), Row.Row1, KeyType.Letter),
        KeyUI(Key("У",  KeyState.None), Row.Row1, KeyType.Letter),
        KeyUI(Key("К",  KeyState.None), Row.Row1, KeyType.Letter),

        KeyUI(Key("Е",  KeyState.None), Row.Row1, KeyType.Letter),
        KeyUI(Key("Н",  KeyState.None), Row.Row1, KeyType.Letter),
        KeyUI(Key("Г",  KeyState.None), Row.Row1, KeyType.Letter),
        KeyUI(Key("Ш",  KeyState.None), Row.Row1, KeyType.Letter),

        KeyUI(Key("Щ",  KeyState.None), Row.Row1, KeyType.Letter),
        KeyUI(Key("З",  KeyState.None), Row.Row1, KeyType.Letter),
        KeyUI(Key("Х",  KeyState.None), Row.Row1, KeyType.Letter),
        KeyUI(Key("Ї",  KeyState.None), Row.Row1, KeyType.Letter),



        KeyUI(Key("Ф",  KeyState.None), Row.Row2, KeyType.Letter),
        KeyUI(Key("І",  KeyState.None), Row.Row2, KeyType.Letter),
        KeyUI(Key("В",  KeyState.None), Row.Row2, KeyType.Letter),
        KeyUI(Key("А",  KeyState.None), Row.Row2, KeyType.Letter),

        KeyUI(Key("П",  KeyState.None), Row.Row2, KeyType.Letter),
        KeyUI(Key("Р",  KeyState.None), Row.Row2, KeyType.Letter),
        KeyUI(Key("О",  KeyState.None), Row.Row2, KeyType.Letter),
        KeyUI(Key("Л",  KeyState.None), Row.Row2, KeyType.Letter),

        KeyUI(Key("Д",  KeyState.None), Row.Row2, KeyType.Letter),
        KeyUI(Key("Ж",  KeyState.None), Row.Row2, KeyType.Letter),
        KeyUI(Key("Є",  KeyState.None), Row.Row2, KeyType.Letter),
        KeyUI(Key("Ґ",  KeyState.None), Row.Row2, KeyType.Letter),



        KeyUI(Key("⏎",  KeyState.None), Row.Row3, KeyType.Enter), //Enter

        KeyUI(Key("Я",  KeyState.None), Row.Row3, KeyType.Letter),
        KeyUI(Key("Ч",  KeyState.None), Row.Row3, KeyType.Letter),
        KeyUI(Key("С",  KeyState.None), Row.Row3, KeyType.Letter),
        KeyUI(Key("М",  KeyState.None), Row.Row3, KeyType.Letter),

        KeyUI(Key("И",  KeyState.None), Row.Row3, KeyType.Letter),
        KeyUI(Key("Т",  KeyState.None), Row.Row3, KeyType.Letter),
        KeyUI(Key("Ь",  KeyState.None), Row.Row3, KeyType.Letter),
        KeyUI(Key("Б",  KeyState.None), Row.Row3, KeyType.Letter),

        KeyUI(Key("Ю",  KeyState.None), Row.Row3, KeyType.Letter),

        KeyUI(Key("⌫",  KeyState.None), Row.Row3, KeyType.Backspace), //Back


    )

    fun keyByString(str: String):KeyUI?{
       keysList.map {
          if(it.key.value == str){
             return it
          }
       }
       return null
    }
}