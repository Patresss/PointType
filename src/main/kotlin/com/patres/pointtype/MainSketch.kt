package com.patres.pointtype

import com.patres.pointtype.handler.AudioHandler
import com.patres.pointtype.point.InterPointManager
import ddf.minim.Minim
import processing.core.PApplet
import processing.core.PVector
import java.awt.Color


class MainSketch : PApplet() {

    companion object {
        const val SIZE_X = 1000
        const val SIZE_Y = 600
        const val DEFAULT_SIZE_WIDTH = 640
        const val DEFAULT_SIZE_HEIGHT = 480
        const val SCALE_X = SIZE_X.toFloat() / DEFAULT_SIZE_WIDTH.toFloat()
        const val SCALE_Y = SIZE_Y.toFloat() / DEFAULT_SIZE_HEIGHT.toFloat()
    }

    lateinit var interPointManager: InterPointManager
    lateinit var textBoard: TextBoard
    lateinit var audioHandler: AudioHandler
    private val textSeparator = '`'
    private var typedText: String? = null
    private var previousMouse: PVector = PVector(0f,0f)

    override fun settings() {
        size(SIZE_X, SIZE_Y)
    }

    override fun setup() {
        val graphicsText = createGraphics(MainSketch.SIZE_X, MainSketch.SIZE_Y)
        audioHandler = AudioHandler(Minim(this))
        interPointManager = InterPointManager(pApplet = this, audioHandler = audioHandler)
        textBoard = TextBoard(pApplet = this, graphicsText = graphicsText, interPointManager = interPointManager)
    }

    override fun draw() {
        background(0)
        update()
        if(previousMouse.x != mouseX.toFloat() || previousMouse.y != mouseY.toFloat()) {
            textBoard.changeTextPosition(PVector(mouseX.toFloat(), mouseY.toFloat()))
        }
            previousMouse.x = mouseX.toFloat()
            previousMouse.y = mouseY.toFloat()

        interPointManager.draw()
       // drawInformation()
    }

    private fun drawInformation() {
        fill(Color.WHITE)
        text("Frame: $frameRate", 10f, 20f)
        text("Number of points: ${interPointManager.points.size}", 10f, 40f)
    }

    private fun update() {
        interPointManager.update()
    }

    override fun keyPressed() {
        if (key.isDefined()) {
            when {
                typedText == null && textSeparator != key -> textBoard.newText(key.toString())
                typedText == null && textSeparator == key -> typedText = ""
                typedText != null && textSeparator != key -> typedText += key
                typedText != null && textSeparator == key -> {
                    textBoard.newText(typedText?: "")
                    typedText = null
                }
            }
        }
    }

    override fun mousePressed() {
    }

    override fun stop() {
        audioHandler.audioInput.close()
        audioHandler.minim.stop()
        super.stop()
    }

}