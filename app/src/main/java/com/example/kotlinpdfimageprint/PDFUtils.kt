package com.example.kotlinpdfimageprint


import com.itextpdf.text.*
import com.itextpdf.text.pdf.draw.LineSeparator

object PDFUtils {
    @Throws(DocumentException::class)
    fun addNewItem(document:Document, text: String, align:Int, font: Font){

        val chunk = Chunk(text!!,font!!)

        val paragraph = Paragraph(chunk)

        paragraph.alignment = align

        document.add(paragraph)


    }

    @Throws(DocumentException::class)
    fun addLineSeparator(document: Document){
        val lineSeparator = LineSeparator()
        lineSeparator.lineColor = BaseColor(0,0,0,68)

        addLineSpace(document)

        document.add(Chunk(lineSeparator))

    }

    @Throws(DocumentException::class)
    fun addLineSpace(document: Document){
        document.add(Paragraph(""))
    }

    @Throws(DocumentException::class)
    fun addNewItemWithLeftAndRight(document:Document, leftText: String,rightText:String, leftFont: Font, rightFont:Font){
//https://youtu.be/O4CqYhepK1o?t=332
        //https://www.youtube.com/watch?v=z3tWuwEA-Jw
        val chunkTextLeft = Chunk(leftText, leftFont)
        val chunkTextRight = Chunk(rightText, rightFont)
        val p = Paragraph(chunkTextLeft)



    }



}