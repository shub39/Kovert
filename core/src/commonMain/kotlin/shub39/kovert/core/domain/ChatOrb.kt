package shub39.kovert.core.domain

import androidx.compose.ui.graphics.Color

enum class ChatOrb(val colors: List<Color>, val desc: String) {
    NORMAL(
        colors = listOf(
            Color(0xFF6366F1), // Soft indigo
            Color(0xFF8B5CF6), // Purple
            Color.White,
            Color(0xFF06B6D4), // Cyan
            Color(0xFF3B82F6)  // Blue
        ),
        desc = "NORMAL"
    ),

    SUSPICIOUS(
        colors = listOf(
            Color(0xFFFBBF24), // Amber
            Color(0xFFF59E0B), // Orange
            Color.White,
            Color(0xFFEAB308), // Yellow
            Color(0xFFFCD34D)  // Light yellow
        ),
        desc = "SUSPICIOUS"
    ),

    DEFENSIVE(
        colors = listOf(
            Color(0xFFEF4444), // Red
            Color(0xFFF97316), // Orange-red
            Color.White,
            Color(0xFFDC2626), // Dark red
            Color(0xFFFB923C)  // Orange accent
        ),
        desc = "DEFENSIVE"
    ),

    PANIC(
        colors = listOf(
            Color(0xFFDC2626), // Bright red
            Color(0xFF991B1B), // Dark red
            Color.White,
            Color(0xFFEF4444), // Red accent
            Color(0xFF450A0A)  // Very dark red
        ),
        desc = "PANIC"
    ),

    NERVOUS(
        colors = listOf(
            Color(0xFF9333EA), // Vivid purple
            Color(0xFFC026D3), // Magenta
            Color.White,
            Color(0xFF7C3AED), // Deep purple
            Color(0xFFD946EF)  // Bright fuchsia
        ),
        desc = "NERVOUS"
    )
}