package de.ulrich_boeing.brush

import de.ulrich_boeing.basics.Vec
import processing.core.PApplet.ceil

class ExampleBrush (val stepSize: Float, val proposalWidth: Float) {
    var curPos = Vec()

    fun paint(startPos: Vec) {
        curPos = startPos

    }

    fun step(pos: Vec) {
        val numProposals = ceil(proposalWidth / 2)
        val startProposal = pos + Vec(pos.x + stepSize, pos.y - proposalWidth / 2)
        val endProposal = pos + Vec(pos.x + stepSize, pos.y + proposalWidth / 2)
//        val proposals = Array(numProposals) {
//            i -> s
//        }
    }

}

