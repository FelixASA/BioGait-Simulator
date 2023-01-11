package com.example.biogait_simulator.Bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.ContentValues.TAG
import android.os.Handler
import android.os.Message
import android.util.Log
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

class BluetoothService(adapter: BluetoothAdapter, mHandler: Handler) {
    private val bluetoothAdapter: BluetoothAdapter = adapter
    private val handler: Handler = mHandler
    private lateinit var sendReceive: SendReceive
    val STATE_LISTENING: Int = 1
    val STATE_CONNECTED: Int = 3
    val STATE_DISCONNECTED: Int = 4
    val APP_NAME = "BioGaitS"
    val uuid: UUID = UUID.fromString("b2d4c1c4-9171-11ed-a1eb-0242ac120002") // uuid generator

    //  Clase servidor
    @SuppressLint("MissingPermission")
    inner class ServerClass: Thread(){
        private var serverSocket: BluetoothServerSocket? = null

        init{
            try{
                serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(APP_NAME,uuid)
            }catch (e : IOException){
                e.printStackTrace()
            }
        }

        override fun run(){
            var socket: BluetoothSocket?=null
            while(socket == null){

                try{
                    val msg = Message.obtain()
                    msg.what = STATE_LISTENING
                    handler.sendMessage(msg)
                    socket = serverSocket!!.accept()
                }catch (e : IOException){
                    e.printStackTrace()
                    val msg = Message.obtain()
                    msg.what = STATE_DISCONNECTED
                    handler.sendMessage(msg)
                }

                if(socket != null){
                    val msg = Message.obtain()
                    msg.what = STATE_CONNECTED
                    sendReceive = SendReceive(socket)
                    sendReceive.start()
                    break
                }
            }
        }

        fun cancel(){
            try{
                serverSocket?.close()
            }catch (e: IOException){
                Log.e(TAG, "Could not close the connect socket", e)
            }
        }
    }

    inner class SendReceive(private val bluetoothSocket: BluetoothSocket?): Thread(){
        private val inputStream: InputStream?
        private val outputStream: OutputStream?

        init{
            var tmpInputStream: InputStream? = null
            var tmpOutputStream: OutputStream? =null
            try{
                tmpInputStream = bluetoothSocket!!.inputStream
                tmpOutputStream = bluetoothSocket.outputStream
            }catch (e: IOException){
                e.printStackTrace()
            }
            inputStream = tmpInputStream
            outputStream = tmpOutputStream
        }

        /*
        //  No se utiliza porque no recibe mensajes
        override fun run(){
            val buffer = ByteArray(1024)
            var bytes: Int
            while(true){
                try{
                    bytes = inputStream?.read(buffer)!!
                    handler.obtainMessage(STATE_MESSAGE_RECEIVEDm byte, -1, buffer).sendToTarget()
                }catch (e: IOException){
                    e.printStackTrace()
                }
            }
        }
         */

        fun write(bytes: ByteArray?){
            try{
                outputStream?.write(bytes)
            }catch (e:IOException){
                e.printStackTrace()
            }
            /*
            // Share the sent message with the UI activity.
            val writtenMsg = handler.obtainMessage(
                MESSAGE_WRITE, -1, -1, mmBuffer)
            writtenMsg.sendToTarget()
            */
        }

        fun cancel(){
            try{
                bluetoothSocket!!.close()
            }catch (e: IOException){
                e.printStackTrace()
            }
        }

    }
}