import PySimpleGUI as sg #Libreria - Para installar correr pip install PySimpleGUI en la consola
import vlc #Libreria - Para installar correr pip install python-vlc en la consola
import jpysocket #Libreria - Para installar correr pip install jpysocket en la consola
import socket
import _thread

#Inicializa un socket
socket = socket.socket() 

#Instancia del VCL y Estructura del reproductor
vcl_instancia = vlc.Instance() 
list_player = vcl_instancia.media_list_player_new()
media_list = vcl_instancia.media_list_new([])
list_player.set_media_list(media_list)
player = list_player.get_media_player()

#Inicializacion del videoplayer
def initMediaPlayer():
    #Direccion de pruebas
    #C:\Users\PC\Desktop\Repo\src\resources\media\videos\test.mp4

    #Configuracion del Media Player
    sg.theme('DarkBlue')
    layout=[[sg.Image('', size=(600, 400), key='-MEDIA_PLAYER-')], #Visor del mediaplayer
            [sg.Text('Esperando servidor...', key='-MESSAGE_AREA-')]] #Mensaje de informacion


    window = sg.Window('Reproductor', layout, element_justification='center', finalize=True, resizable=True) #Definiciones de la ventana
    window['-MEDIA_PLAYER-'].expand(True, True)  #Permite el responsive del visor           

    #------------ Media Player Setup ---------#
    player.set_hwnd(window['-MEDIA_PLAYER-'].Widget.winfo_id())

    #------------ The Event Loop ------------#
    while True:
        event, values = window.read(timeout=1000)       # run with a timeout so that current location can be updated
        
        if event == sg.WIN_CLOSED:
            break

        #Si el video cambia su estado actualiza el mensaje de informacion del usuario 
        if player.is_playing():  
            window['-MESSAGE_AREA-'].update('En reproduccion')
        else:
            window['-MESSAGE_AREA-'].update('Esperando servidor...')

    window.close()

#Controles para el VideoPlayer
def setVideo(url):
    media_list.add_media(url)
    list_player.set_media_list(media_list)
    playVideo()

def playVideo():
    list_player.play()

def pauseVideo():
    list_player.pause()

def endVideo():
    list_player.stop()

#Inicializacion de la conexion al socket
def initSocket(): 
    host= "localhost"   #Nombre del host
    port=12345 #Puerto a utilizar

    socket.connect((host,port)) #Establece conexion
    print("Conexion establecida")

    #Bucle que recibe y muestra todos los mensajes del servidor
    while True:
        msj=socket.recv(1024) #Recibe mensaje del server
        msj=jpysocket.jpydecode(msj) #Desencripta 
        
        if msj != "":
            print("Mensaje recibido: ",msj)
        
        if '--set:' in msj:
            url = msj.replace("--set:", "")    
            print("Iniciar reproduccion")
            setVideo(url)
            
        if '--play' in msj:
            print ('Reproducir')
            playVideo()

        if '--pause' in msj:
            print ('Pausar')
            pauseVideo()

        if '--exit' in msj:
            endVideo()

        if  '--end' in msj: 
            print ('Fin del Video')
            endVideo()
            break

    socket.close() #Cierra conexion

    print("Conexion cerrada")


_thread.start_new_thread(initSocket, ())
initMediaPlayer()


#set:C:\Users\Fabricio\Desktop\Repo\src\resources\media\videos\test.mp4