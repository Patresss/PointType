package com.patres.pointtype.handler

import ddf.minim.AudioInput
import ddf.minim.Minim

class AudioHandler(
       val minim: Minim,
       val audioInput: AudioInput = minim.getLineIn(Minim.STEREO, 512)
) {

    fun getVolume(): Float = audioInput.mix.level()

}