package com.example.kotlinpdfimageprint

import android.Manifest
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Toast
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_main.*
import java.io.File
import java.lang.StringBuilder
import java.util.ArrayList

class MainActivity : AppCompatActivity() {
    private val FILE_PRINT:String = "PDFPrint.pdf"

    var superHeroList = ArrayList<SuperHeroModel>()

    private val appPath:String

    get(){
        val dir = File(Environment.getExternalStorageDirectory()
            .toString() + File.separator
        + resources.getString(R.string.app_name)
        + File.separator
        )

        if(!dir.exists())


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
    }

    private fun initModel() {
        var model = SuperHeroModel()
        model.name = "Joel"
        model.description = "JJKJKLJLKJHJKHKHJH"
        model.image = "https://www.shutterstock.com/image-illustration/watercolor-wreath-hand-painted-eucalyptus-flowers-1607210860"

        superHeroList.add(model)



        model.name = "Joel Joel"
        model.description = "JJKJKLJLKJHJKHKHJH"
        model.image = "https://www.shutterstock.com/image-illustration/watercolor-wreath-hand-painted-eucalyptus-flowers-1607210860"

        superHeroList.add(model)

        model.name = "Joel John"
        model.description = "JJKJKLJLdvsdvesdvsvKJHJKHKHJH"
        model.image = "https://www.shutterstock.com/image-illustration/watercolor-wreath-hand-painted-eucalyptus-flowers-1607210860"

        superHeroList.add(model)


        model.name = "Joel Joel"
        model.description = "JJKJKLJLKJHJKHKHJH"
        model.image = "https://www.shutterstock.com/image-illustration/watercolor-wreath-hand-painted-eucalyptus-flowers-1607210860"

        superHeroList.add(model)



        model.name = "Joel Joel"
        model.description = "JJKJKLJLKJHJKHKHJH"
        model.image = "https://www.shutterstock.com/image-illustration/watercolor-wreath-hand-painted-eucalyptus-flowers-1607210860"

        superHeroList.add(model)


        model.name = "Joel Joel"
        model.description = "JJKJKLJLKJHJKHKHJH"
        model.image = "https://www.shutterstock.com/image-illustration/watercolor-wreath-hand-painted-eucalyptus-flowers-1607210860"

        superHeroList.add(model)

    }
}