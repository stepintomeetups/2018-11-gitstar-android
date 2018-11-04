/*
 * Created by Tamás Szincsák on 2018-11-03.
 * Copyright (c) 2018 Tamás Szincsák.
 */

package hu.stepintomeetups.gitstar.ui.common

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.text.TextPaint
import androidx.annotation.ColorInt
import androidx.collection.LruCache
import androidx.core.graphics.*
import hu.stepintomeetups.gitstar.api.entities.Repo
import kotlin.math.roundToInt

private val nameMap = mapOf(
    "C Sharp" to "C#",
    "JavaScript" to "JS",
    "Objective-C" to "O-C",
    "Objective-C++" to "O-C++",
    "TypeScript" to "TS"
)

private val colorMap = mapOf(
    "Assembly" to 0xFF6E4C13.toInt(),
    "C" to 0xFF555555.toInt(),
    "C#" to 0xFF178600.toInt(),
    "C++" to 0xFF555555.toInt(),
    "CSS" to 0xFF563D7C.toInt(),
    "Dart" to 0xFF00B4AB.toInt(),
    "HTML" to 0xFFE44B23.toInt(),
    "Java" to 0xFFB07219.toInt(),
    "JavaScript" to 0xFFf1E05A.toInt(),
    "Kotlin" to 0xFFF18E33.toInt(),
    "Objective-C" to 0xFF438EFF.toInt(),
    "Objective-C++" to 0xFF6866FB.toInt(),
    "PHP" to 0xFF4F5D95.toInt(),
    "Ruby" to 0xFF701516.toInt(),
    "Shell" to 0xFF89E051.toInt(),
    "Swift" to 0xFFFFAC45.toInt(),
    "TypeScript" to 0xFF2B7489.toInt()
)

class RepositoryIconHelper {
    private val cachedIcons = LruCache<String, Bitmap>(12)

    fun getIconForRepository(context: Context, repo: Repo): Bitmap {
        val key = repo.language ?: "no-language"

        return cachedIcons.get(key) ?: generateRepositoryIcon(context, repo).also { cachedIcons.put(key, it) }
    }

    private fun generateRepositoryIcon(context: Context, repo: Repo): Bitmap {
        val multiplier = context.resources.displayMetrics.density

        val w = (48 * multiplier).roundToInt()
        val h = w

        return createBitmap(w, h).applyCanvas {
            val backgroundColor = colorMap[repo.language] ?: 0xFFDEDEC0.toInt()

            drawColor(backgroundColor)

            val text = nameMap[repo.language] ?: repo.language ?: "-"

            val textPaint = TextPaint(Paint.ANTI_ALIAS_FLAG).apply {
                textSize = 15.0f * multiplier
                textSize = calculateFitTextSize(this, w * 0.66f, text).coerceIn(6.0f * multiplier, 17.0f * multiplier)
                color = calculateTextColor(backgroundColor)
                textAlign = Paint.Align.CENTER
            }

            drawText(text, w / 2.0f, h / 2.0f - ((textPaint.descent() + textPaint.ascent()) / 2), textPaint)
        }
    }

    private fun calculateFitTextSize(paint: TextPaint, width: Float, text: String): Float {
        val currentWidth = paint.measureText(text)
        return width / currentWidth * paint.textSize
    }

    @ColorInt
    private fun calculateTextColor(@ColorInt backgroundColor: Int): Int {
        val brightness = (0.299 * backgroundColor.red + 0.587 * backgroundColor.green + 0.114 * backgroundColor.blue) / 255

        return when {
            brightness < 0.65 -> 0xFFFFFFFF.toInt()
            else -> 0xFF000000.toInt()
        }
    }
}