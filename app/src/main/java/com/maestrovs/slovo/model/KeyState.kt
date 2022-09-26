package com.maestrovs.slovo.model

enum class KeyState {
    None, Wrong, Mistake, Right


}

fun KeyState.weight(): Int{
    return when(this){
        KeyState.None -> 0
        KeyState.Wrong -> 1
        KeyState.Mistake -> 2
        KeyState.Right -> 3
    }
}

fun KeyState.isMoreWeight(other : KeyState) = this.weight() > other.weight()