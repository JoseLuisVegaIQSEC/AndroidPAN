package mx.com.iqsec.sdkpan.data.util

import mx.com.iqsec.sdkpan.data.dtos.AntispoffingDTO
import mx.com.iqsec.sdkpan.data.dtos.INEDTO
import mx.com.iqsec.sdkpan.data.dtos.MunicipalitiesDTO
import mx.com.iqsec.sdkpan.data.dtos.StatesDTO
import mx.com.iqsec.sdkpan.data.dtos.DataUserDTO
import mx.com.iqsec.sdkpan.data.dtos.ValidaINEDTO
import mx.com.iqsec.sdkpan.data.dtos.VerifyFramesDTO
import mx.com.iqsec.sdkpan.data.dtos.CloseFileDTO
import mx.com.iqsec.sdkpan.data.dtos.SaveSignDTO
import mx.com.iqsec.sdkpan.data.dtos.ValidateMembershipDTO
import mx.com.iqsec.sdkpan.domain.model.AntispoffingDataModel
import mx.com.iqsec.sdkpan.domain.model.AntispoffingModel
import mx.com.iqsec.sdkpan.domain.model.CloseFileModel
import mx.com.iqsec.sdkpan.domain.model.DataUserModel
import mx.com.iqsec.sdkpan.domain.model.INEModel
import mx.com.iqsec.sdkpan.domain.model.MunicipalitiesModel
import mx.com.iqsec.sdkpan.domain.model.SaveSignModel
import mx.com.iqsec.sdkpan.domain.model.StatesModel
import mx.com.iqsec.sdkpan.domain.model.UserINEDataValidationModel
import mx.com.iqsec.sdkpan.domain.model.ValidateMembershipModel
import mx.com.iqsec.sdkpan.domain.model.VerifyFramesModel

internal fun AntispoffingDTO.asDomain(): AntispoffingModel = AntispoffingModel(
    codError = this.codError,
    descripcion = this.descripcion,
    data = AntispoffingDataModel(
        analisis = this.data.analisis,
        promedio = this.data.promedio,
        indices = this.data.indices
    )
)

internal fun CloseFileDTO.asDomain(): CloseFileModel = CloseFileModel(
    estado = this.estado,
    descripcion = this.descripcion,
    folio = this.folio,
    identificacion = this.identificacion,
    nombre = this.nombre,
    primerApellido = this.primerApellido,
    segundoApellido = this.segundoApellido,
    municipio = this.municipio,
    entidadFederativa = this.entidadFederativa,
    perfil = this.perfil,
    vencimiento = this.vencimiento,
    token = this.token,
    rostro = this.rostro,
    qr = this.qr,
    registro = this.registro,
    folioAfiliado = this.folioAfiliado
)

internal fun INEDTO.asDomain(): INEModel = INEModel(
    estado = this.estado,
    descripcion = this.descripcion,
    tipo = this.tipo,
    subTipo = this.subTipo,
    claveElector = this.claveElector,
    registro = this.registro,
    curp = this.curp,
    seccion = this.seccion,
    vigencia = this.vigencia,
    emision = this.emision,
    noEmision = this.noEmision,
    sexo = this.sexo,
    primerApellido = this.primerApellido,
    segundoApellido = this.segundoApellido,
    nombres = this.nombres,
    calle = this.calle,
    colonia = this.colonia,
    ciudad = this.ciudad,
    fechaNacimiento = this.fechaNacimiento,
    folio = this.folio,
    mrz = this.mrz,
    cic = this.cic,
    ocr = this.ocr,
    identificadorCiudadano = this.identificadorCiudadano,
    referencia = this.referencia
)

internal fun ValidaINEDTO.asDomain(): UserINEDataValidationModel = UserINEDataValidationModel(
    tipoSituacionRegistral = this.tipoSituacionRegistral,
    claveElector = this.claveElector,
    anioRegistro = this.anioRegistro,
    apellidoPaterno = this.apellidoPaterno,
    anioEmision = this.anioEmision,
    numeroEmisionCredencial = this.numeroEmisionCredencial,
    nombre = this.nombre,
    curp = this.curp,
    apellidoMaterno = this.apellidoMaterno,
    ocr = this.ocr
)

internal fun VerifyFramesDTO.asDomain(): VerifyFramesModel = VerifyFramesModel(
    estado = estado ?: 0,
    descripcion = this.descripcion,
    score = this.score ?: 0.0
)

internal fun List<MunicipalitiesDTO>.asListMunicipalities(): ArrayList<MunicipalitiesModel> {

    val listSignedDocuments = arrayListOf<MunicipalitiesModel>()

    this.map {
        val signedDocumentModel = MunicipalitiesModel(
            id_municipio = it.id_municipio,
            nombre = it.nombre
        )

        listSignedDocuments.add(signedDocumentModel)
    }

    return listSignedDocuments
}

internal fun List<StatesDTO>.asListStates(): ArrayList<StatesModel> {

    val listSignedDocuments = arrayListOf<StatesModel>()

    this.map {
        val signedDocumentModel = StatesModel(
            id_estado = it.id_estado,
            nombre = it.nombre,
            abreviatura = it.abreviatura
        )

        listSignedDocuments.add(signedDocumentModel)
    }

    return listSignedDocuments
}

internal fun DataUserDTO.asDomain(): DataUserModel = DataUserModel(
    estado = this.estado,
    descripcion = this.descripcion,
    folio = this.folio,
    identificacion = this.identificacion,
    nombre = this.nombre,
    primerApellido = this.primerApellido,
    segundoApellido = this.segundoApellido,
    municipio = this.municipio,
    entidadFederativa = this.entidadFederativa,
    perfil = this.perfil,
    vencimiento = this.vencimiento,
    token = this.token,
    rostro = this.rostro,
    qr = this.qr,
    registro = this.registro,
    folioAfiliado = this.folioAfiliado
)

internal fun SaveSignDTO.asDomain(): SaveSignModel = SaveSignModel(
    estado = this.estado,
    descripcion = this.descripcion,
    folio = this.folio,
    referencia = this.referencia
)

internal fun ValidateMembershipDTO.asDomain(): ValidateMembershipModel = ValidateMembershipModel(
    existe = this.existe,
    folioAfiliado = this.folioAfiliado,
    referencia = this.referencia
)