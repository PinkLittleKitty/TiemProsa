package com.santyfisela.tiemprosa.utils

object TextUtils {
    
    fun getTimePatterns(): List<String> = listOf(
        "una de la madrugada", "dos de la mañana", "tres de la madrugada", "cuatro de la mañana",
        "cinco de la mañana", "seis de la mañana", "siete en punto", "ocho de la mañana",
        "nueve menos diez", "diez de la mañana", "once de la mañana", "mediodía", "doce en punto",
        "una de la tarde", "dos de la tarde", "tres de la tarde", "cuatro de la tarde",
        "cinco de la tarde", "seis de la tarde", "siete de la noche", "ocho de la noche",
        "nueve de la noche", "diez de la noche", "once de la noche", "medianoche",
        "las doce", "las trece", "las catorce", "las quince", "las dieciséis", "las diecisiete",
        "las dieciocho", "las diecinueve", "las veinte", "las veintiuna", "las veintidós", "las veintitrés",
        "doce de la noche", "la una", "las dos", "las tres", "las cuatro", "las cinco",
        "las seis", "las siete", "las ocho", "las nueve", "las diez", "las once"
    )
    
    fun findTimeInText(text: String): String? {
        val lowerText = text.lowercase()
        return getTimePatterns().find { pattern ->
            lowerText.contains(pattern.lowercase())
        }
    }
    
    fun getTimeFromText(text: String): Pair<String, Int>? {
        val lowerText = text.lowercase()
        getTimePatterns().forEachIndexed { _, pattern ->
            val index = lowerText.indexOf(pattern.lowercase())
            if (index != -1) {
                val originalText = text.substring(index, index + pattern.length)
                return Pair(originalText, index)
            }
        }
        return null
    }
}