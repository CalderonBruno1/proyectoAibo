package com.upc.aiboapp

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions


class LocalizacionActivity : AppCompatActivity(),OnMapReadyCallback,GoogleMap.OnMyLocationClickListener {

    private lateinit var latitud: String
    private lateinit var longitud: String

    private lateinit var mMap: GoogleMap
    lateinit var mapView: SupportMapFragment
    private lateinit var fusedLocationClient: FusedLocationProviderClient

    private lateinit var confirmarBtnMap: Button
    var storageManager:  FirebaseStorageManager= FirebaseStorageManager()


    companion object{
        const val REQUEST_CODE_LOCATION =0
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_localizacion)
        createFragment()
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        confirmarBtnMap=findViewById(R.id.btnConfirmarMap)
        confirmarBtnMap.setOnClickListener{
            val dniUser: String? =getIntent().getStringExtra("dni")
            if (dniUser != null) {
                storageManager.guardarUbicacion(dniUser,latitud,longitud,this)
            }
        }
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap=googleMap //crea mapa
        mMap.setOnMyLocationClickListener(this)
        enableLocation() //activa localizacion
    }

    private fun createMarker(location : Location){
        val ubicacion = LatLng(location.latitude, location.longitude)
        latitud=location.latitude.toString()
        longitud=location.longitude.toString()

        mMap.addMarker(MarkerOptions().position(ubicacion).title("Mi ubicacion"))
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ubicacion,16.0f))
    }
    private fun createFragment(){
        mapView=supportFragmentManager.findFragmentById(R.id.mapId) as SupportMapFragment

        mapView.getMapAsync(this)
    }
    private fun isLocationPermissionGranted()=ContextCompat.checkSelfPermission(
        this,Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED
    private fun enableLocation(){
        if (!::mMap.isInitialized) return //mapa no inicializado regresa
        if (isLocationPermissionGranted()){ //mapa inicializado
            mMap.isMyLocationEnabled = true //activa localizacion tiempo real
            fusedLocationClient.lastLocation
                .addOnSuccessListener { location : Location ->
                    createMarker(location)
                }

        }else{
            requestLocationPermission() //sino pide los permisos
        }
    }
    private fun  requestLocationPermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,Manifest.permission.ACCESS_FINE_LOCATION)){
            Toast.makeText(this,"ve a ajustes y acepta los permisos",Toast.LENGTH_SHORT).show() // se ha pedido los permisos y ha rechazado
        }else{
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), //pedimos permisos
                REQUEST_CODE_LOCATION)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            REQUEST_CODE_LOCATION->if(grantResults.isNotEmpty()&&grantResults[0]==PackageManager.PERMISSION_GRANTED){
                mMap.isMyLocationEnabled=true //acepta permisos y activamos localizacion
            }else{
                Toast.makeText(this,"ve a ajustes y acepta los permisos",Toast.LENGTH_SHORT).show() //rechaza
            }
            else->{}
        }
    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        if (!isLocationPermissionGranted()){
            if (!::mMap.isInitialized) return //mapa no inicializado regresa
            mMap.isMyLocationEnabled=false
            Toast.makeText(this,"ve a ajustes y acepta los permisos",Toast.LENGTH_SHORT).show() //rechaza
        }
    }

    override fun onMyLocationClick(p0: Location) {
        Toast.makeText(this,"Estas en: ${p0.latitude},  ${p0.longitude}",Toast.LENGTH_SHORT).show()
    }
}