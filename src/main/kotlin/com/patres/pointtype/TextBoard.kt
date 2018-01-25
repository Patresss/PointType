package com.patres.pointtype

import com.patres.pointtype.point.InterPoint
import com.patres.pointtype.point.InterPointManager
import processing.core.*

class TextBoard(
        var pApplet: PApplet,
        var interPointManager: InterPointManager,
        var graphicsText: PGraphics,
        private var font: PFont = pApplet.loadFont("fonts/Roboto-Regular-32.vlw"),
        private var text: String = "S",
        private var interPointByTextBackground: Int = 50,
        private var position: PVector = PVector(MainSketch.SIZE_X / 2.0f, MainSketch.SIZE_Y / 2f)
) {

    var pixelsOfTextBackground = ArrayList<PVector>()

    init {
        prepareGraphicsText()
        loadPixelsOfTextBackground()
        interPointManager.points = generatePointsToText().map { InterPoint(manager = interPointManager, centerPosition = it) }.toMutableList()
        interPointManager.fillPotentialNeighboursToDraw()
    }

    private fun reloadText() {
        newText(text)
    }

    fun newText(text: String) {
        this.text = text
        prepareGraphicsText()
        loadPixelsOfTextBackground()
        val newText = generatePointsToText()
        if (newText.size > interPointManager.points.size) {
            addNewPointsAndDraw(newText)
        } else {
            removeAdditionalPointsAndDraw(newText)
        }
        interPointManager.fillPotentialNeighboursToDraw()
    }

    private fun removeAdditionalPointsAndDraw(newText: ArrayList<PVector>) {
        interPointManager.points = interPointManager.points.take(newText.size).toMutableList()
        for (i in 0 until newText.size) {
            interPointManager.points[i].destinationCenterPosition = newText[i]
            interPointManager.points[i].textBoardPositionPosition = newText[i]
        }
    }

    private fun addNewPointsAndDraw(newText: ArrayList<PVector>) {
        interPointManager.addNewPoints(newText.size - interPointManager.points.size)
        for (i in 0 until interPointManager.points.size) {
            interPointManager.points[i].destinationCenterPosition = newText[i]
            interPointManager.points[i].textBoardPositionPosition = newText[i]
        }
    }

    private fun generatePointsToText(): ArrayList<PVector> {
        val pointsToText = ArrayList<PVector>()
        var counter = 0
        pixelsOfTextBackground.forEach {
            if (counter % interPointByTextBackground == 0) {
                pointsToText.add(it)
            }
            counter++
        }
        return pointsToText
    }


    private fun loadPixelsOfTextBackground() {
        pixelsOfTextBackground = ArrayList()
        for (x in 0..MainSketch.SIZE_X) {
            for (y in 0..MainSketch.SIZE_Y) {
                val c = graphicsText.get(x, y)
                if (pApplet.brightness(c) >= 100) {
                    pixelsOfTextBackground.add(PVector(x.toFloat(), y.toFloat()))
                }
            }
        }
    }

    private fun prepareGraphicsText() {
        graphicsText.beginDraw()
        graphicsText.background(0)
        graphicsText.noStroke()
        graphicsText.fill(255)
        graphicsText.textAlign(PConstants.CENTER, PConstants.CENTER)
        val fontSize = MainSketch.SIZE_Y / 4.0f
        graphicsText.textFont(font, fontSize)
        graphicsText.text(text, position.x, position.y)
        graphicsText.endDraw()

    }

    fun changeTextPosition(fallowingPosition: PVector) {
        position = fallowingPosition.copy()
        reloadText()
    }

}