package com.ayvytr.common.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.math.BigDecimal
import java.math.RoundingMode

/**
 * @author Administrator
 */
@Parcelize
data class Storage(val byte: Long, val scale: Int = 2): Parcelable {
    lateinit var unit: StorageUnit

    val size by lazy {
//        var kb = 1L.shl(10)
        val bd = BigDecimal(byte)
        if (byte >= 1L.shl(40)) {
            unit = StorageUnit.TB
            bd.divide(BigDecimal(1L.shl(40)), scale, RoundingMode.HALF_UP).toPlainString()
        } else if (byte >= 1L.shl(30)) {
            unit = StorageUnit.GB
            bd.divide(BigDecimal(1L.shl(30)), scale, RoundingMode.HALF_UP).toPlainString()
        } else if (byte >= 1L.shl(20)) {
            unit = StorageUnit.MB
            bd.divide(BigDecimal(1L.shl(20)), scale, RoundingMode.HALF_UP).toPlainString()
        } else if (byte >= 1L.shl(10)) {
            unit = StorageUnit.KB
            bd.divide(BigDecimal(1L.shl(10)), scale, RoundingMode.HALF_UP).toPlainString()
        } else {
            unit = StorageUnit.Byte
            byte.toString()
        }
    }


    constructor(byte: Int, scale: Int = 2): this(byte.toLong(), scale)

    override fun toString(): String {
        return "$size$unit"
    }
}