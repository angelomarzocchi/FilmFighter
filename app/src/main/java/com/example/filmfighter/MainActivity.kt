package com.example.filmfighter


import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.filmfighter.model.ViewModel
import com.google.android.gms.nearby.Nearby
import com.google.android.gms.nearby.connection.*
import com.google.android.material.color.DynamicColors
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.nio.charset.StandardCharsets.UTF_8


class MainActivity : AppCompatActivity() {

    private var isDiscovering: Boolean = false
    private var isAdvertising: Boolean = false

    private val endpointIds = mutableListOf<String>()

    private val REQUEST_CODE_LOCATION_BLUETOOTH = 1
    private val REQUEST_CODE_NEARBY = 2

    private val STRATEGY = Strategy.P2P_STAR



    private val viewModel: ViewModel by viewModels()

    private lateinit var connectionsClient: ConnectionsClient


    @kotlinx.serialization.Serializable
    data class CostumPayload(val type: String, val data: String)



    private val payloadCallback: PayloadCallback = object : PayloadCallback() {

        override fun onPayloadReceived(endpointId: String, payload: Payload) {

            val payloadReceivedString = payload.asBytes()?.let { String(it,UTF_8) }.toString()
            val receivedPayload = Json.decodeFromString<CostumPayload>(payloadReceivedString)
            Log.d("costumPayload",receivedPayload.type)

            when (receivedPayload.type) {
                "startGame" -> {
                    // Avvia la partita
                    viewModel.startGame()
                }
                "quizAnswer" -> {
                    // Registra la risposta
                    val fighter = receivedPayload.data.substringBefore('?')
                    val vote = receivedPayload.data.substringAfter('?').toInt()
                    viewModel.vote(fighter,vote)
                    Log.d("answers",
                        viewModel.fighters.value?.find{it.name == fighter}?.answers?.get(0).toString()
                    )



                }
                "hostInfo" -> {
                    val name = receivedPayload.data.substringBefore('?')
                    val movie = receivedPayload.data.substringAfter('?')
                    viewModel.addFighter(name,movie)
                }

            }



        }

        override fun onPayloadTransferUpdate(endpointId: String, update: PayloadTransferUpdate) {
            when(update.status) {
                PayloadTransferUpdate.Status.SUCCESS -> Log.d("NearbyStatusTransfer",endpointId)
                PayloadTransferUpdate.Status.FAILURE -> Log.d("NearbyStatusTransfer","failure")
                PayloadTransferUpdate.Status.CANCELED -> Log.d("NearbyStatusTransfer","canceled")
                PayloadTransferUpdate.Status.IN_PROGRESS -> Log.d("NearbyStatusTransfer","in progess")
            }

        }


    }

    private val connectionLifecycleCallback = object : ConnectionLifecycleCallback() {
        override fun onConnectionInitiated(endpointId: String, info: ConnectionInfo) {
            connectionsClient.acceptConnection(endpointId,payloadCallback)
            Log.d("connectionInfo",info.endpointName + String(info.endpointInfo))

                viewModel.addFighter(
                    info.endpointName.substringBefore('?'),
                    info.endpointName.substringAfter('?')
                )

        }

        override fun onConnectionResult(endpointId: String, result: ConnectionResolution) {
            if(result.status.isSuccess) {
                endpointIds.add(endpointId)

            }
        }

        override fun onDisconnected(endpointId: String) {
                viewModel.resetGame()
           // connectionsClient = Nearby.getConnectionsClient(this@MainActivity)
        }

    }

    // Callbacks for finding other devices
    private val endpointDiscoveryCallback = object : EndpointDiscoveryCallback() {
        override fun onEndpointFound(endpointId: String, info: DiscoveredEndpointInfo) {
            Log.d("connectionInfo","called onEndpointFound")
            connectionsClient.requestConnection(viewModel.myName + '?' + viewModel.movie.value!!.title, endpointId, connectionLifecycleCallback)
        }

        override fun onEndpointLost(endpointId: String) {
        }
    }



    fun startAdvertising() {
        val options = AdvertisingOptions.Builder().setStrategy(STRATEGY).build()
        if(isAdvertising){
            connectionsClient.stopAdvertising()
          //  connectionsClient.stopAllEndpoints()
            isAdvertising = false
        }
        isAdvertising = true
        connectionsClient.startAdvertising(
            viewModel.myName + '?' + viewModel.movie.value!!.title,
            packageName,
            connectionLifecycleCallback,
            options
        )

    }

    fun startDiscovery() {
        Log.d("costumPayload","start Discovery")
        val options = DiscoveryOptions.Builder().setStrategy(STRATEGY).build()
        if(isDiscovering){
            connectionsClient.stopDiscovery()
          //  connectionsClient.stopAllEndpoints()
            isDiscovering = false


        }
        isDiscovering = true
        connectionsClient.startDiscovery(packageName,endpointDiscoveryCallback,options)
    }

    fun stopNearbyConnections() {
        if(viewModel.hosting)
            connectionsClient.stopAdvertising()
        else
            connectionsClient.stopDiscovery()



        connectionsClient.stopAllEndpoints()
        endpointIds.clear()



    }

    fun startGame() {
        val payload = CostumPayload("startGame", "gameStarted")
        val payloadBytes = Json.encodeToString(payload).toByteArray(Charsets.UTF_8)
        Log.d("costumPayload",payload.type)
        connectionsClient.sendPayload(endpointIds, Payload.fromBytes(payloadBytes))

    }

    fun sendQuizAnswer(fighter: String,answer: String) {
        val payload = CostumPayload("quizAnswer", "$fighter?$answer")
        val payloadBytes = Json.encodeToString(payload).toByteArray(Charsets.UTF_8)
        connectionsClient.sendPayload(endpointIds, Payload.fromBytes(payloadBytes))

    }








    @RequiresApi(Build.VERSION_CODES.S)
    val permissions = arrayOf(
        "android.permission.ACCESS_FINE_LOCATION",
        "android.permission.BLUETOOTH_ADVERTISE",
        "android.permission.BLUETOOTH_CONNECT",
        "android.permission.BLUETOOTH_SCAN"
    )

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    val nearbyPermission = arrayOf("android.permission.NEARBY_WIFI_DEVICES","android.permission.BLUETOOTH_ADVERTISE",
        "android.permission.BLUETOOTH_CONNECT",
        "android.permission.BLUETOOTH_SCAN")


    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DynamicColors.applyToActivityIfAvailable(this)
        setContentView(R.layout.activity_main)

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        setupActionBarWithNavController(navController)
        connectionsClient = Nearby.getConnectionsClient(this)
    }





    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }


    override fun onStart() {
        super.onStart()

        if(Build.VERSION.SDK_INT >= 31 && Build.VERSION.SDK_INT < 33)
            requestPermissions(permissions,REQUEST_CODE_LOCATION_BLUETOOTH)

            else if(Build.VERSION.SDK_INT >= 33)
                requestPermissions(nearbyPermission,REQUEST_CODE_NEARBY)



    }

    override fun onStop() {
        stopNearbyConnections()
        super.onStop()
    }





}