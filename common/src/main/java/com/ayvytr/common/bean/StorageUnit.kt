package com.ayvytr.common.bean

/**
 * @author Administrator
 */
enum class StorageUnit(level: Int) {
    Byte(0), KB(1), MB(2), GB(3), TB(4);

//    companion object {
//        fun valueOf(level: Int): StorageUnit {
//            return when (level) {
//                0 -> Byte
//                1 -> KB
//                2 -> MB
//                3 -> GB
//                else  -> TB
//            }
//        }
//    }
}