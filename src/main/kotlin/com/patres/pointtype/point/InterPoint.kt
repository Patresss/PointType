package com.patres.pointtype.point

import com.patres.pointtype.fill
import com.patres.pointtype.utils.RandomGenerator
import processing.core.PVector

class InterPoint(
        val manager: InterPointManager,
        val centerPosition: PVector,
        private var angles: Double = RandomGenerator.generateDouble(max = Math.PI * 2f),
        private var anglesSpeed: Double = 0.02 * RandomGenerator.generateBooleanNumber()
) {

    var textBoardPositionPosition: PVector = PVector()
    private var move = false
    private var touched = false
    private val currentPosition: PVector = PVector()
    private var destinationCenterPositionSpeed = PVector()
    private var potentialNeighbourToDraw = mutableListOf<InterPoint>()

    var destinationCenterPosition: PVector = centerPosition
        set(value) {
            field = value
            destinationCenterPositionSpeed = PVector((value.x - centerPosition.x) / manager.speedToNextPoint, (value.y - centerPosition.y) / manager.speedToNextPoint)
            move = true
        }

    init {
        updateCurrentPosition()
    }

    fun draw() {
        val color = if (touched) manager.touchedColor else manager.color
        manager.pApplet.fill(color)
        manager.pApplet.noStroke()
        manager.pApplet.ellipse(currentPosition.x, currentPosition.y, manager.radiusOfPoint, manager.radiusOfPoint)
    }

    fun fillPotentialNeighboursToDraw(points: ArrayList<InterPoint>) {
        potentialNeighbourToDraw = points.filter { getManhattanPotentialCenterDistance(it) - manager.orbitRadius * 2 < manager.neighbourDistance }.toMutableList()
    }

    fun update() {
        updateCenterPosition()
        updateCurrentPosition()
        angles += anglesSpeed / manager.frameSpeedBooster
    }

    private fun updateCurrentPosition() {
        val orbitRadius = manager.currentOrbitRadius
        currentPosition.x = centerPosition.x - (orbitRadius * Math.cos(angles)).toFloat()
        currentPosition.y = centerPosition.y - (orbitRadius * Math.sin(angles)).toFloat()
    }

    private fun updateCenterPosition() {
        if (move) {
            if (Math.abs(centerPosition.x - destinationCenterPosition.x) <= Math.abs(destinationCenterPositionSpeed.x) || Math.abs(centerPosition.y - destinationCenterPosition.y) <= Math.abs(destinationCenterPositionSpeed.y)) {
                centerPosition.x = destinationCenterPosition.x
                centerPosition.y = destinationCenterPosition.y
                move = false
            } else {
                centerPosition.x += destinationCenterPositionSpeed.x
                centerPosition.y += destinationCenterPositionSpeed.y
            }
        }
    }

    fun drawLineToPoint(point: InterPoint) {
        manager.pApplet.line(this.currentPosition.x, this.currentPosition.y, point.currentPosition.x, point.currentPosition.y)
    }

    private fun getManhattanDistance(point: InterPoint): Float = Math.abs(point.currentPosition.x - this.currentPosition.x) + Math.abs(point.currentPosition.y - this.currentPosition.y)

    private fun getManhattanPotentialCenterDistance(point: InterPoint): Float = Math.abs(point.destinationCenterPosition.x - this.destinationCenterPosition.x) + Math.abs(point.destinationCenterPosition.y - this.destinationCenterPosition.y)

    fun getNeighbour(): List<InterPoint> = potentialNeighbourToDraw.filter { getManhattanDistance(it) < manager.neighbourDistance }

}