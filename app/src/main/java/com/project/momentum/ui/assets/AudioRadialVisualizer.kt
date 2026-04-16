package com.project.momentum.ui.assets

import android.graphics.Canvas.VertexMode
import android.graphics.Paint
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.max
import kotlin.math.roundToInt
import kotlin.math.sin
import kotlin.math.sqrt
import kotlin.random.Random

private const val MinVisualizerColors = 3
private const val MaxVisualizerColors = 6
private const val MeshColumns = 30
private const val SpeechNoiseFloor = 0.015f
private const val SpeechFullScale = 0.24f
private val FullCircle = (PI * 2.0).toFloat()

private data class VisualizerColorPoint(
    val color: Color,
    val anchorX: Float,
    val anchorY: Float,
    val orbitX: Float,
    val orbitY: Float,
    val phaseX: Float,
    val phaseY: Float,
    val speedX: Float,
    val speedY: Float,
    val fieldStrength: Float,
    val stretchX: Float,
    val stretchY: Float,
    val skew: Float,
    val voiceAngle: Float,
    val voicePush: Float,
    val voicePhase: Float,
    val voiceSensitivity: Float,
)

private data class ResolvedVisualizerPoint(
    val color: Color,
    val x: Float,
    val y: Float,
    val fieldStrength: Float,
    val stretchX: Float,
    val stretchY: Float,
    val skew: Float,
)

@Composable
fun AudioRadialVisualizer(
    level: Float,
    modifier: Modifier = Modifier,
) {
    val colorPoints = remember { createVisualizerColorPoints() }
    val meshPaint = remember { Paint(Paint.ANTI_ALIAS_FLAG) }
    val normalizedLevel = level.coerceIn(0f, 1f)
    val animatedLevel by animateFloatAsState(
        targetValue = normalizedLevel,
        animationSpec = tween(80),
        label = "audio-visualizer-level",
    )
    val speechEnergy by animateFloatAsState(
        targetValue = normalizedLevel.toSpeechEnergy(),
        animationSpec = tween(38),
        label = "audio-visualizer-speech-energy",
    )

    val infinite = rememberInfiniteTransition(label = "audio-visualizer-motion")
    val drift by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 12_000,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Restart,
        ),
        label = "audio-visualizer-drift",
    )
    val breathe by infinite.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 2_400,
                easing = LinearEasing,
            ),
            repeatMode = RepeatMode.Reverse,
        ),
        label = "audio-visualizer-breathe",
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        if (size.width <= 0f || size.height <= 0f) return@Canvas

        val audioEnergy = sqrt(animatedLevel.coerceIn(0f, 1f))
        val movingPoints = colorPoints.mapIndexed { index, point ->
            point.resolve(
                drift = drift,
                breathe = breathe,
                audioEnergy = audioEnergy,
                speechEnergy = speechEnergy,
                index = index,
            )
        }
        val meshRows = max(1, (MeshColumns * size.height / size.width).roundToInt())
        val mesh = buildVisualizerMesh(
            width = size.width,
            height = size.height,
            columns = MeshColumns,
            rows = meshRows,
            points = movingPoints,
            drift = drift,
            audioEnergy = audioEnergy,
            speechEnergy = speechEnergy,
        )

        drawIntoCanvas { canvas ->
            canvas.nativeCanvas.drawVertices(
                VertexMode.TRIANGLES,
                mesh.vertices.size,
                mesh.vertices,
                0,
                null,
                0,
                mesh.colors,
                0,
                mesh.indices,
                0,
                mesh.indices.size,
                meshPaint,
            )
        }
    }
}

private fun createVisualizerColorPoints(): List<VisualizerColorPoint> {
    val random = Random(System.nanoTime())
    val colorCount = random.nextInt(MinVisualizerColors, MaxVisualizerColors + 1)
    val baseHue = random.nextFloatBetween(0f, 360f)

    return List(colorCount) { index ->
        val angleTurn = (index.toFloat() / colorCount.toFloat()) +
            random.nextFloatBetween(-0.12f, 0.12f)
        val angle = FullCircle * angleTurn
        val distanceFromCenter = random.nextFloatBetween(0.2f, 0.46f)
        val anchorX = calculatePointAnchor(
            base = 0.5f,
            offset = cos(angle.toDouble()).toFloat() * distanceFromCenter,
            random = random,
        )
        val anchorY = calculatePointAnchor(
            base = 0.5f,
            offset = sin(angle.toDouble()).toFloat() * distanceFromCenter,
            random = random,
        )
        val hueStep = 360f / colorCount.toFloat()
        val hue = (baseHue + index * hueStep + random.nextFloatBetween(-28f, 28f) + 360f) % 360f

        VisualizerColorPoint(
            color = Color.hsv(
                hue = hue,
                saturation = random.nextFloatBetween(0.68f, 0.96f),
                value = random.nextFloatBetween(0.82f, 1f),
            ),
            anchorX = anchorX,
            anchorY = anchorY,
            orbitX = random.nextFloatBetween(0.08f, 0.22f),
            orbitY = random.nextFloatBetween(0.07f, 0.2f),
            phaseX = random.nextFloatBetween(0f, FullCircle),
            phaseY = random.nextFloatBetween(0f, FullCircle),
            speedX = random.nextFloatBetween(0.54f, 1.28f),
            speedY = random.nextFloatBetween(0.72f, 1.54f),
            fieldStrength = random.nextFloatBetween(0.72f, 1.28f),
            stretchX = random.nextFloatBetween(0.74f, 1.35f),
            stretchY = random.nextFloatBetween(0.72f, 1.38f),
            skew = random.nextFloatBetween(-0.42f, 0.42f),
            voiceAngle = random.nextFloatBetween(0f, FullCircle),
            voicePush = random.nextFloatBetween(0.12f, 0.28f),
            voicePhase = random.nextFloatBetween(0f, FullCircle),
            voiceSensitivity = random.nextFloatBetween(0.72f, 1.35f),
        )
    }
}

private fun calculatePointAnchor(
    base: Float,
    offset: Float,
    random: Random,
): Float {
    return (base + offset + random.nextFloatBetween(-0.08f, 0.08f)).coerceIn(0.08f, 0.92f)
}

private fun VisualizerColorPoint.resolve(
    drift: Float,
    breathe: Float,
    audioEnergy: Float,
    speechEnergy: Float,
    index: Int,
): ResolvedVisualizerPoint {
    val xPhase = drift * FullCircle * speedX + phaseX
    val yPhase = drift * FullCircle * speedY + phaseY
    val speechPhase = speechEnergy * FullCircle * voiceSensitivity + voicePhase
    val speechPulse = speechEnergy *
        (0.82f + sin(speechPhase.toDouble()).toFloat() * 0.18f)
    val speechAngle = voiceAngle +
        sin((breathe * FullCircle + voicePhase).toDouble()).toFloat() * 0.55f
    val reactiveShift = audioEnergy * (0.035f + index * 0.006f)
    val speechShift = voicePush * speechPulse
    val x = anchorX +
        cos(xPhase.toDouble()).toFloat() * orbitX +
        sin((yPhase * 0.71f).toDouble()).toFloat() * orbitX * 0.45f +
        sin((xPhase * 1.9f + breathe * FullCircle).toDouble()).toFloat() * reactiveShift +
        cos(speechAngle.toDouble()).toFloat() * speechShift
    val y = anchorY +
        sin(yPhase.toDouble()).toFloat() * orbitY +
        cos((xPhase * 1.17f).toDouble()).toFloat() * orbitY * 0.42f +
        cos((yPhase * 1.63f - breathe * FullCircle).toDouble()).toFloat() * reactiveShift +
        sin(speechAngle.toDouble()).toFloat() * speechShift
    val fieldPulse = 1f +
        sin((speechPhase + index * 0.83f).toDouble()).toFloat() * speechEnergy * 0.58f
    val stretchPulse = 1f +
        cos((speechPhase * 0.74f).toDouble()).toFloat() * speechEnergy * 0.22f

    return ResolvedVisualizerPoint(
        color = color,
        x = x.coerceIn(-0.28f, 1.28f),
        y = y.coerceIn(-0.28f, 1.28f),
        fieldStrength = fieldStrength *
            (0.72f + audioEnergy * 0.2f + speechEnergy * 0.5f) *
            fieldPulse.coerceIn(0.38f, 1.72f),
        stretchX = stretchX * stretchPulse.coerceIn(0.7f, 1.32f),
        stretchY = stretchY / stretchPulse.coerceIn(0.76f, 1.28f),
        skew = skew + sin(speechPhase.toDouble()).toFloat() * speechEnergy * 0.22f,
    )
}

private data class VisualizerMesh(
    val vertices: FloatArray,
    val colors: IntArray,
    val indices: ShortArray,
)

private fun buildVisualizerMesh(
    width: Float,
    height: Float,
    columns: Int,
    rows: Int,
    points: List<ResolvedVisualizerPoint>,
    drift: Float,
    audioEnergy: Float,
    speechEnergy: Float,
): VisualizerMesh {
    val vertexColumns = columns + 1
    val vertexRows = rows + 1
    val vertexCount = vertexColumns * vertexRows
    val vertices = FloatArray(vertexCount * 2)
    val colors = IntArray(vertexCount)
    var vertexIndex = 0
    var coordinateIndex = 0

    for (row in 0..rows) {
        val y = row.toFloat() / rows.toFloat()
        for (column in 0..columns) {
            val x = column.toFloat() / columns.toFloat()
            vertices[coordinateIndex++] = width * x
            vertices[coordinateIndex++] = height * y
            colors[vertexIndex++] = sampleVisualizerColor(
                x = x,
                y = y,
                points = points,
                drift = drift,
                audioEnergy = audioEnergy,
                speechEnergy = speechEnergy,
            ).toArgb()
        }
    }

    val indices = ShortArray(columns * rows * 6)
    var index = 0
    for (row in 0 until rows) {
        for (column in 0 until columns) {
            val topLeft = row * vertexColumns + column
            val topRight = topLeft + 1
            val bottomLeft = topLeft + vertexColumns
            val bottomRight = bottomLeft + 1

            indices[index++] = topLeft.toShort()
            indices[index++] = topRight.toShort()
            indices[index++] = bottomLeft.toShort()
            indices[index++] = topRight.toShort()
            indices[index++] = bottomRight.toShort()
            indices[index++] = bottomLeft.toShort()
        }
    }

    return VisualizerMesh(
        vertices = vertices,
        colors = colors,
        indices = indices,
    )
}

private fun sampleVisualizerColor(
    x: Float,
    y: Float,
    points: List<ResolvedVisualizerPoint>,
    drift: Float,
    audioEnergy: Float,
    speechEnergy: Float,
): Color {
    val driftAngle = drift * FullCircle
    val warpAmount = 0.028f + audioEnergy * 0.024f + speechEnergy * 0.095f
    val warpedX = x +
        sin((y * 7.2f + driftAngle * 0.52f).toDouble()).toFloat() * warpAmount +
        sin((x * 11.3f - driftAngle * 0.37f).toDouble()).toFloat() * warpAmount * 0.36f
    val warpedY = y +
        cos((x * 6.4f - driftAngle * 0.43f).toDouble()).toFloat() * warpAmount +
        sin((y * 9.5f + driftAngle * 0.29f).toDouble()).toFloat() * warpAmount * 0.33f
    val backgroundWeight = 0.3f + (1f - audioEnergy) * 0.24f - speechEnergy * 0.18f

    var totalWeight = backgroundWeight
    var red = 0.016f * backgroundWeight
    var green = 0.018f * backgroundWeight
    var blue = 0.024f * backgroundWeight

    points.forEachIndexed { index, point ->
        val dx = warpedX - point.x
        val dy = warpedY - point.y
        val skewedX = dx * point.stretchX + dy * point.skew
        val skewedY = dy * point.stretchY - dx * point.skew * 0.35f
        val distanceSquared = skewedX * skewedX + skewedY * skewedY
        val localWave = 0.9f +
            sin(
                (
                    driftAngle * (0.7f + index * 0.11f) +
                        point.x * 4f +
                        speechEnergy * FullCircle * (0.8f + index * 0.17f)
                    ).toDouble(),
            ).toFloat() * (0.1f + speechEnergy * 0.24f)
        val weight = point.fieldStrength * localWave /
            (distanceSquared * (4.9f - audioEnergy * 0.8f - speechEnergy * 1.8f) + 0.042f)

        totalWeight += weight
        red += point.color.red * weight
        green += point.color.green * weight
        blue += point.color.blue * weight
    }

    val brightness = 0.7f + audioEnergy * 0.14f + speechEnergy * 0.22f
    return Color(
        red = ((red / totalWeight) * brightness).coerceIn(0f, 1f),
        green = ((green / totalWeight) * brightness).coerceIn(0f, 1f),
        blue = ((blue / totalWeight) * brightness).coerceIn(0f, 1f),
        alpha = 1f,
    )
}

private fun Float.toSpeechEnergy(): Float {
    val compressedLevel = ((coerceIn(0f, 1f) - SpeechNoiseFloor) /
        (SpeechFullScale - SpeechNoiseFloor)).coerceIn(0f, 1f)
    return sqrt(compressedLevel)
}

private fun Random.nextFloatBetween(from: Float, until: Float): Float {
    return from + (until - from) * nextFloat()
}
