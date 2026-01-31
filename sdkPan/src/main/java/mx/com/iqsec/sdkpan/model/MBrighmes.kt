package mx.com.iqsec.sdkpan.model

import android.graphics.Bitmap
import com.google.mlkit.vision.face.Face

class Sector {
    var index: Int = 0
    lateinit var image: Bitmap
    var brighnes: Int = 0
    var coordenadas: String = ""

    constructor()

    constructor(
        index: Int,
        image: Bitmap,
        brighnes: Int,
        coord: String
    ) {
        this.index = index
        this.image = image
        this.brighnes = brighnes
        this.coordenadas = coord
    }
}

class MBrighmes {
    lateinit var Part: TypeFrame
    lateinit var brighnes: statusBrighnes
    var valueBrighnes: Double = 0.0

    constructor()

    constructor(typeFrame: TypeFrame, brighnes: statusBrighnes) {
        Part = typeFrame
        this.brighnes = brighnes
    }
}

enum class TypeFrame {
    leftPart(1),
    rigthPart(2);

    var part: Int? = null

    constructor()

    constructor(
        part: Int
    ) {
        this.part = part
    }
}

enum class statusBrighnes {
    lowBrignes(1),
    brighnesOk(2),
    hightBrignes(3);

    var valStatus: Int? = null

    constructor()

    constructor(
        valStatus: Int
    ) {
        this.valStatus = valStatus
    }
}

class frames {
    var id: Int = 0
    lateinit var image: Bitmap
    var isCentered: Boolean = false
    var isFaceDetected: Boolean = false
    var isValidOrientation: Boolean = false
    var isFacingForward: Boolean = false
    var eyes_distance: Float = 0f
    var face: Face? = null

    constructor()

    constructor(
        id: Int,
        image: Bitmap,
        isCentered: Boolean = false,
        isFaceDetected: Boolean = false,
        isValidOrientation: Boolean = false,
        isFacingForward: Boolean = false,
        eyes_distance: Float = 0f,
        face: Face
    ) {
        this.id = id
        this.image = image
        this.isCentered = isCentered
        this.isFaceDetected = isFaceDetected
        this.isValidOrientation = isValidOrientation
        this.isFacingForward = isFacingForward
        this.eyes_distance = eyes_distance
        this.face = face
    }
}