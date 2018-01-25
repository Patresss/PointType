package com.patres.pointtype.point

import com.patres.pointtype.handler.AudioHandler
import com.patres.pointtype.MainSketch
import com.patres.pointtype.utils.RandomGenerator
import processing.core.PApplet
import processing.core.PVector
import java.awt.Color

class InterPointManager(
        val pApplet: PApplet,
        var neighbourDistance: Float = 15f * MainSketch.SCALE_X,
        val radiusOfPoint: Float = 2f,
        val orbitRadius: Float = 3f * MainSketch.SCALE_X,
        val speedToNextPoint: Float = 10f,
        private val audioHandler: AudioHandler
) {

    var points: MutableList<InterPoint> = ArrayList()
    var currentOrbitRadius = orbitRadius
    var frameSpeedBooster = 60f
    var color: Color = Color.WHITE
    var touchedColor: Color = Color.YELLOW

    fun draw() {
        updateVolume()
        frameSpeedBooster = pApplet.frameRate / 60f
        points.forEach { it.draw() }
        drawNeighbourEffect()
    }

    private fun updateVolume() {
        val yellow = 255 - (audioHandler.getVolume() * 255).toInt()
        color = Color(255, 255, yellow)
        //currentOrbitRadius = orbitRadius + audioHandler.getVolume() * 100
    }

    private fun drawNeighbourEffect() {
        val pointsToAnalise = ArrayList<InterPoint>(points)
        pApplet.stroke(color.rgb, 100f)
        points.forEach { checkedPoint ->
            pointsToAnalise.remove(checkedPoint)
            val neighbours = checkedPoint.getNeighbour()
            neighbours.forEach { neighbour -> neighbour.drawLineToPoint(checkedPoint) }
        }
    }


    fun fillPotentialNeighboursToDraw() {
        val pointsToAnalise = ArrayList<InterPoint>(points)
        points.forEach { checkedPoint ->
            pointsToAnalise.remove(checkedPoint)
            checkedPoint.fillPotentialNeighboursToDraw(pointsToAnalise)
        }
    }

    fun addNewPoints(numberOfPoints: Int) {
        if(points.isEmpty()) {
            for(i in 0 until numberOfPoints) {
                addPoint(RandomGenerator.generateVector(maxX = MainSketch.SIZE_X.toFloat(), maxY = MainSketch.SIZE_Y.toFloat()))
            }
        } else {
            for(i in 0 until numberOfPoints) {
                addPoint(points[i].centerPosition.copy())
            }
        }
    }

    fun update() {
        points.forEach { it.update() }
    }

    private fun addPoint(point: InterPoint) {
        points.add(point)
    }

    private fun addPoint(position: PVector) {
        addPoint(InterPoint(manager = this, centerPosition = position))
    }



}
