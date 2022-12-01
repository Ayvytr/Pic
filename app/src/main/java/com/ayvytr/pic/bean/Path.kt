package com.ayvytr.pic.bean

/**
 * @author Administrator
 */
data class Path(
    val name: String,
    val path: String,
    val count: Int,
    val previewPhoto: String,
    val previewColor: Int,
    val previewType: PreviewType = PreviewType.IMAGE
)