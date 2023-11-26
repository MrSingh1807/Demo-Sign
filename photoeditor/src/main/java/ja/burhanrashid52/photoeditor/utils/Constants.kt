package ja.burhanrashid52.photoeditor.utils

import android.util.Log


fun logDebug(value: String) {
    Log.d("Mr_Singh", "logDebug: $value")
}

fun logInfo(value: String) {
    Log.d("Mr_Singh", "logInfo: $value")
}

fun Double.beInt(): Int {
    val decimalPart = this - this.toInt() // Extract decimal part
    val firstDigitAfterDecimal =
        (decimalPart * 10).toInt() % 10 // Get the first digit after the decimal point

    return if (firstDigitAfterDecimal >= 5) this.toInt() + 1 else this.toInt()
}

