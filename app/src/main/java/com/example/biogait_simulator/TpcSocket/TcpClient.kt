package com.example.biogait_simulator.TpcSocket

import android.util.Log
import java.io.IOException
import java.io.OutputStream
import java.net.Socket

class TcpClient (ip : String, port : Int) : Thread(){
    lateinit var socket: Socket
    lateinit var dataOutputStream: OutputStream
    var ip: String = ""
    var port: Int = 0

    init {
        this.ip = ip
        this.port = port
    }

    override fun run(){
        try{
            socket = Socket(ip, port)
            dataOutputStream = socket.getOutputStream()
            Log.i("SOCKET","Connected " + socket.inetAddress + " " + socket.localPort)
        }catch (e : IOException){
            e.printStackTrace()
            Log.i("SOCKET","Failed to connect " + ip + " " + port)
        }
    }

    //  Envia el mensaje al servidor
    fun sendMessage(bytes: ByteArray?){
        try{
            dataOutputStream!!.write(bytes)
        }catch (e : IOException){

        }
    }

    //  Cierra el socket
    fun close(){
        try{
            socket?.close()
        }catch (e : IOException){
            e.printStackTrace()
        }
    }
}