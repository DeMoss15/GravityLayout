package com.example.gravity

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import kotlin.math.abs

class GravityLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), TiltListener {

    companion object {
        const val MAX_ROTATION_CORNER = 75f
    }

    private var azimuthLimits: RotationLimits = RotationLimits()
    private var pitchLimits: RotationLimits = RotationLimits()
    private var rollLimits: RotationLimits = RotationLimits()

    private val tiltSensor: TiltSensor = TiltSensorImpl(context)
        .also { it.addListener(this) }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        azimuthLimits.centerValue = null
        pitchLimits.centerValue = null
        rollLimits.centerValue = null
        tiltSensor.register()
    }

    override fun onDetachedFromWindow() {
        tiltSensor.unregister()
        super.onDetachedFromWindow()
    }

    override fun onTilt(azimuthRad: Double, pitchRad: Double, rollRad: Double) {
        animate().let { animator ->
//            animator.rotation(calculateRotationCorner(azimuthRad, azimuthLimits))
            animator.rotationX(calculateRotationCorner(pitchRad, pitchLimits))
            animator.rotationY(calculateRotationCorner(rollRad, rollLimits))
            animator.duration = 25
            animator.start()
        }
    }

    private fun calculateRotationCorner(
        cornerRad: Double,
        limits: RotationLimits
    ): Float {
        val corner = (cornerRad * 180 / Math.PI).toFloat()
        if (limits.centerValue == null && cornerRad != .0) {
            limits.centerValue = corner
        }

        val plainDistanceBetweenCorners = (corner - (limits.centerValue ?: 0f))

        return -(plainDistanceBetweenCorners.takeIf { abs(it) < (360 - MAX_ROTATION_CORNER) }
            ?: 360 - plainDistanceBetweenCorners)
            .coerceAtLeast(-MAX_ROTATION_CORNER)
            .coerceAtMost(MAX_ROTATION_CORNER) / 2
    }

    class RotationLimits() {

        var centerValue: Float? = null

    }

}