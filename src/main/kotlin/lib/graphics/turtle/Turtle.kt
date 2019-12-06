package lib.graphics.turtle

import lib.graphics.turtle.TurtleState.Turtle
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

/** publishes [TurtleState.Turtle] to package level */
typealias Turtle = TurtleState.Turtle

/** read-only [TurtleState] view */
interface TurtleStateView {
    val x: Double
    val y: Double
    val rotation: Double
    val isDown: Boolean
    val isVisible: Boolean
    val phi get() = rotation * PI / 180
}

/** represents mutable properties which are readable by everyone while writable by [Turtle] only */
class TurtleState(
    x: Double = 0.0,
    y: Double = 0.0,
    rotation: Double = 0.0,
    isDown: Boolean = true,
    isVisible: Boolean = true
) : TurtleStateView {
    override var x = x
        private set
    override var y = y
        private set
    override var rotation = rotation
        private set
    override var isDown = isDown
        private set
    override var isVisible = isVisible
        private set

    /**
     * Provides [basic set](https://www.tutorialspoint.com/logo/logo_turtle.htm )
     * of [Turtle graphics](https://en.wikipedia.org/wiki/Turtle_graphics) commands
     * over wrapped [TurtleCore] instance
     */
    class Turtle(
        private val core: TurtleCore,
        private val state: TurtleState = TurtleState()
    ) : TurtleStateView by state {
        /**
         * Move forward by number of pixels specified by [distance]
         */
        infix fun fd(distance: Number): Turtle = apply {
            core.showTurtle()

            val d = distance.toDouble()
            val nx = x + cos(phi) * d
            val ny = y + sin(phi) * d
            if (isDown) core.line(x, y, nx, ny)
            state.x = nx
            state.y = ny

            core.showTurtle()
        }

        /**
         * Move backward by number of pixels specified by [distance]
         */
        infix fun bk(distance: Number) = fd(-distance.toDouble())

        /**
         * Rotate turtle right by [degrees]
         */
        infix fun rt(degrees: Number): Turtle = apply {
            core.showTurtle()
            state.rotation += degrees.toDouble()
            state.rotation %= 360
            core.showTurtle()
        }

        /**
         * Rotate turtle left by [degrees]
         */
        infix fun lt(degrees: Number) = rt(-degrees.toDouble())

        /**
         * Set width of line drawn by turtle
         */
        infix fun pw(penWidth: Number): Turtle = apply { core.pen(penWidth.toFloat()) }

        /**
         * Pen up, i. e. move but not draw
         */
        fun pu(): Turtle = apply { state.isDown = false }

        /**
         * Pen down, i. e. draw when moving
         */
        fun pd(): Turtle = apply { state.isDown = true }

        /**
         * Clear screen
         */
        fun cs(): Turtle = apply {
            core.clear()
            core.showTurtle()
        }
    }
}

/**
 * Set of core operations that other [Turtle] operations are translated to
 */
interface TurtleCore {
    fun clear()
    fun line(x1: Double, y1: Double, x2: Double, y2: Double)
    fun pen(width: Float)
    fun showTurtle()
}