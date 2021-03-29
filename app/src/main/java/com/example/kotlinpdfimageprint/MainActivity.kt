package com.example.kotlinpdfimageprint

import android.Manifest
import android.content.Context
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.print.PrintAttributes
import android.print.PrintManager
import android.widget.Toast
import com.bumptech.glide.Glide
import com.itextpdf.text.*
import com.itextpdf.text.pdf.BaseFont
import com.itextpdf.text.pdf.PdfWriter
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Scheduler
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*
import java.lang.StringBuilder
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    //private val FILE_PRINT:String = "PDFPrint.pdf"
    companion object{
        private val FILE_PRINT:String = "PDFPrint.pdf"

       fun getBitMapFromUri(context:Context, model:SuperHeroModel,document: Document):Observable<SuperHeroModel>{

           return Observable.fromCallable{
               val bitMap = Glide.with(context)
                   .asBitmap()
                   .load(model.image)
                   .submit()
                   .get()

               val  image = Image.getInstance(bitMapToByteArray(bitMap))

               image.scaleAbsolute(100f,100f)
               document.add(image)
               model
           }


       }

        private fun bitMapToByteArray(bitMap:Bitmap?):ByteArray{
            val stream = ByteArrayOutputStream()
            bitMap!!.compress(Bitmap.CompressFormat.PNG,100,stream)
            return stream.toByteArray()

        }
    }

    var superHeroList = ArrayList<SuperHeroModel>()

    private val appPath:String

    get(){
        val dir = File(Environment.getExternalStorageDirectory()
            .toString() + File.separator
        + resources.getString(R.string.app_name)
        + File.separator
        )

        if(!dir.exists()) dir.mkdir()

        return dir.path + File.separator


    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initModel()

        Dexter.withActivity(this)
            .withPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
            .withListener(object:PermissionListener{
                override fun onPermissionGranted(p0: PermissionGrantedResponse?) {

                    print_btn.setOnClickListener {
                        createPDFFile(StringBuilder(appPath).append(FILE_PRINT).toString())
                    }

                }

                override fun onPermissionDenied(p0: PermissionDeniedResponse?) {
                    Toast.makeText(this@MainActivity, "Please enable storage", Toast.LENGTH_LONG).show()

                }

                override fun onPermissionRationaleShouldBeShown(
                    p0: PermissionRequest?,
                    p1: PermissionToken?
                ) {

                }

            })
            .check()
    }



    private fun createPDFFile(path:String) {
        if(File(path).exists())File(path).delete()

        try{
            val document = Document()
            //save pdf

            PdfWriter.getInstance(document, FileOutputStream(path))

            //open
            document.open()

            //setting
            document.pageSize = PageSize.A4
            document.addCreationDate()
            document.addAuthor("Commander")
            document.addCreator("Tech")

            //font setting
            val colorAccent = BaseColor(0,153,204,255)
            val fontSize = 20.0f


            //custom font
            val fontName = BaseFont.createFont("assets/fonts/brandon_medium.otf","UTF-8",BaseFont.EMBEDDED)

            //create Title of Document
            val titleFont = Font(fontName, 36.0f, Font.NORMAL, BaseColor.BLACK)

            PDFUtils.addNewItem(document, "SUPER HEROES", Element.ALIGN_CENTER, titleFont)


            //Add more
            PDFUtils.addLineSpace(document)
            PDFUtils.addNewItem(document, "Details", Element.ALIGN_CENTER,titleFont)
            PDFUtils.addLineSeparator(document)


            //use RxJava to fetch image and add to PDF
            Observable.fromIterable(superHeroList).
                    flatMap { model:SuperHeroModel -> getBitMapFromUri(this, model,document) }
                .subscribeOn(Schedulers.computation())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({model:SuperHeroModel ->
                    //on next
                    PDFUtils.addNewItemWithLeftAndRight(document,model.name!!,"",titleFont,titleFont)
                    PDFUtils.addLineSeparator(document)
                    PDFUtils.addNewItem(document, model.description!!, Element.ALIGN_LEFT, titleFont)
                    PDFUtils.addLineSeparator(document)


                },{
                    t:Throwable? ->

                    //on error toast
                    Toast.makeText(this,t!!.message,Toast.LENGTH_LONG).show()

                }, {
                    //Oncomplete
                    PDFUtils.addLineSpace(document)
                    PDFUtils.addLineSpace(document)

                    //close
                    document.close()
                    Toast.makeText(this,"Success",Toast.LENGTH_LONG).show()

                    printPDF()

                })



        } catch (e:FileNotFoundException){
            e.printStackTrace()
        }catch (e:IOException){
            e.printStackTrace()
        }catch (e:DocumentException){
            e.printStackTrace()
        }finally {


        }

    }

    private fun printPDF() {
        val printManager = getSystemService(Context.PRINT_SERVICE) as PrintManager
        try{
            val printDocumentAdapter = PDFDocumentAdapter(StringBuilder(appPath).append(FILE_PRINT).toString(), FILE_PRINT)
            printManager.print("Document",printDocumentAdapter,PrintAttributes.Builder().build())
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    private fun initModel() {
        var model = SuperHeroModel()
        model.name = "Joel Crawford"
        model.description = "\n" +
                "Why do we use it?\n" +
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like)."
        model.image = "https://letsbuildthatapp-videos.s3-us-west-2.amazonaws.com/114bec2f-fbfd-4b13-91de-907fe57c6e37"

        superHeroList.add(model)



        model.name = "John Joel"
        model.description = "\n" +
                "Why do we use it?\n" +
                "It is a long established fact that a reader will be distracted by the readable content of a page when looking at its layout. The point of using Lorem Ipsum is that it has a more-or-less normal distribution of letters, as opposed to using 'Content here, content here', making it look like readable English. Many desktop publishing packages and web page editors now use Lorem Ipsum as their default model text, and a search for 'lorem ipsum' will uncover many web sites still in their infancy. Various versions have evolved over the years, sometimes by accident, sometimes on purpose (injected humour and the like)."
        model.image = "https://letsbuildthatapp-videos.s3-us-west-2.amazonaws.com/114bec2f-fbfd-4b13-91de-907fe57c6e37"

        superHeroList.add(model)

        model.name = "Joel John"
        model.description = "JJKJKLJLdvsdvesdvsvKJHJKHKHJH"
        model.image = "https://images.unsplash.com/photo-1470399542183-e6245d78c479?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=667&q=80"

        superHeroList.add(model)


        model.name = "Joel John Craw"
        model.description = "but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem I"
        model.image = "https://images.unsplash.com/photo-1470399542183-e6245d78c479?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=667&q=80"

        superHeroList.add(model)



        model.name = "John Craw"
        model.description = "but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem I"
        model.image = "https://letsbuildthatapp-videos.s3-us-west-2.amazonaws.com/dda5bc77-327f-4944-8f51-ba4f3651ffdf"

        superHeroList.add(model)


        model.name = "Janda"
        model.description = "but the majority have suffered alteration in some form, by injected humour, or randomised words which don't look even slightly believable. If you are going to use a passage of Lorem Ipsum, you need to be sure there isn't anything embarrassing hidden in the middle of text. All the Lorem Ipsum generators on the Internet tend to repeat predefined chunks as necessary, making this the first true generator on the Internet. It uses a dictionary of over 200 Latin words, combined with a handful of model sentence structures, to generate Lorem I"
        model.image = "https://images.unsplash.com/photo-1470399542183-e6245d78c479?ixlib=rb-1.2.1&ixid=MXwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHw%3D&auto=format&fit=crop&w=667&q=80"

        superHeroList.add(model)

    }
}