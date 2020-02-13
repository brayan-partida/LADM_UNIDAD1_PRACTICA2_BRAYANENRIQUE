package ittepic.edu.mx.ladm_unidad1_practica2_brayanenrique

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import kotlinx.android.synthetic.main.activity_main.*
import java.io.*


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_DENIED
        ) {
            //PERMISO NO CONCEDIDO, ENTONCES SE UTILIZA
            //Los permisos se deben solicitar en el manifest
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ),
                0
            )
        } else {
            mensaje("Los permisos ya fueron otorgados!!")
        }//todo :======================PERMISOS================>
        btnAbrir.setOnClickListener {
            if (rInterno.isChecked == true) {
                leerArchivoInterno()
            }
            if (rExterno.isChecked == true) {
                leerArchivoSD()
            }

        }//todo: --------> abrir el archivo =========================>


        btnGuardar.setOnClickListener {

            if (rInterno.isChecked == true) {
                guardarArchivoInterno()
            }
            if (rExterno.isChecked == true) {
                guardarArchivoSD()
            }

        }//todo: --------> guardar el archivo =========================>


    }


    fun leerArchivoSD() {
        if (noSD()) {
            return
        }
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, txtArchivo.text.toString())
            var flujoEnntrada = BufferedReader(InputStreamReader(FileInputStream(datosArchivo)))
            var data = flujoEnntrada.readLine()
            var vector = data.split("&")
            asignarText(vector[0])
        } catch (error: IOException) {
            mensaje(error.message.toString())
        }
    }

    fun guardarArchivoSD() {
        if (noSD()) {
            mensaje("No hay memoria")
            return
        }
        try {
            var rutaSD = Environment.getExternalStorageDirectory()
            var datosArchivo = File(rutaSD.absolutePath, txtArchivo.text.toString())
            var flujoSalida = OutputStreamWriter(FileOutputStream(datosArchivo))
            var data = txtFrase.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("Exito!! Se creo el archivo")
            asignarText("")
        } catch (error: IOException) {
            mensaje(error.message.toString())
        }
    }// todo :------> aguardarArchivo en la SD

    fun noSD(): Boolean {
        var estado = Environment.getExternalStorageState()
        if (estado != Environment.MEDIA_MOUNTED) {
            return true
        } else {
            return false
        }
    }

    private fun guardarArchivoInterno() {
        try {
            var flujoSalida =
                OutputStreamWriter(openFileOutput(txtArchivo.text.toString(), Context.MODE_PRIVATE))
            var data = txtFrase.text.toString()
            flujoSalida.write(data)
            flujoSalida.flush()
            flujoSalida.close()
            mensaje("Exito!! Se creo el archivo")
            asignarText("")
        } catch (error: IOException) {
            mensaje(error.message.toString())
        }
    }//todo : -------> guardar archivo interno

    fun leerArchivoInterno() {
        try {
            var flujoEnntrada =
                BufferedReader(InputStreamReader(openFileInput(txtArchivo.text.toString())))
            var data = flujoEnntrada.readLine()
            var vector = data.split("&")
            asignarText(vector[0])
        } catch (error: IOException) {
            mensaje(error.message.toString())
        }
    }//todo:------> leer archivo interno

    fun mensaje(m: String) {
        AlertDialog.Builder(this).setTitle("ATENCION").setMessage(m)
            .setPositiveButton("ACEPTAR") { d, i ->
            }.show()
    }//todo:metodo para mandar mensajes---->

    fun asignarText(t1: String) {
        txtFrase.setText(t1)

    }//todo:metodo para asignar mensajes---->
}
