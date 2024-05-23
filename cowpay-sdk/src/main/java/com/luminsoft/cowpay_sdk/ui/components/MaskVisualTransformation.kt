package com.luminsoft.cowpay_sdk.ui.components

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation
import kotlin.math.absoluteValue
class MaskVisualTransformation(private val mask: String) : VisualTransformation {

    private val specialSymbolsIndices = mask.indices.filter { mask[it] != '#' }

    override fun filter(text: AnnotatedString): TransformedText {
        var out = ""
        var maskIndex = 0
        text.forEach { char ->
            while (specialSymbolsIndices.contains(maskIndex)) {
                out += mask[maskIndex]
                maskIndex++
            }
            out += char
            maskIndex++
        }
        return TransformedText(AnnotatedString(out), offsetTranslator())
    }

    private fun offsetTranslator() = object : OffsetMapping {
        override fun originalToTransformed(offset: Int): Int {
            val offsetValue = offset.absoluteValue
            if (offsetValue == 0) return 0
            var numberOfHashtags = 0
            val masked = mask.takeWhile {
                if (it == '#') numberOfHashtags++
                numberOfHashtags < offsetValue
            }
            return masked.length + 1
        }

        override fun transformedToOriginal(offset: Int): Int {
            return mask.take(offset.absoluteValue).count { it == '#' }
        }
    }
}
//class VisaVisualTransformation : VisualTransformation {
//    override fun filter(text: AnnotatedString): TransformedText {
//
//        val trimmed = if (text.text.length >= 16) text.text.substring(0..15) else text.text
//        var out = ""
//
//        for (i in trimmed.indices) {
//            out += trimmed[i]
//            if (i % 4 == 3 && i != 15) out += " "
//        }
//        val creditCardOffsetTranslator = object : OffsetMapping {
//            override fun originalToTransformed(offset: Int): Int {
//                if (offset <= 3) return offset
//                if (offset <= 7) return offset + 1
//                if (offset <= 11) return offset + 2
//                if (offset <= 16) return offset + 3
//                return 19
//            }
//
//            override fun transformedToOriginal(offset: Int): Int {
//                if (offset <= 4) return offset
//                if (offset <= 9) return offset - 1
//                if (offset <= 14) return offset - 2
//                if (offset <= 19) return offset - 3
//                return 16
//            }
//        }
//
//        return TransformedText(AnnotatedString(out), creditCardOffsetTranslator)
//    }
//
//}
