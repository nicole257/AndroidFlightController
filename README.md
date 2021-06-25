# AndroidFlightController
This Android app is a remote controller for a flight.  
Made as part of Advanced Programming 2nd course, Bar-Ilan University.  
Created by: Nicole Sharabany and Amber Weiner.

## Overview and Features:
Our multithread application implemented in Kotlin with the MVVM software architectural pattern.  
Using FlightGear simulator, the program features a convenient user interface to control an aircraft.  
The GUI contains 4 main components: 
1. Connection to a server
2. Joystick
3. Rudder controller
4. Throttle controller

## Organization of the Project:
Our project is organized in 3 main folders:
1. models- ControllerModel
2. viewModels- ControllerViewModel
3. views- contains MainActivity and a Joystick class.

## Required Installations:
1. Download and install FlightGear 2020.3.6 on your computer
2. Download and install Android Studio (or any other Android-compatible IDE)  
Notice: make sure you use an Android API level 16 and above.

## Manual:
1. Download the repository.  
2. Launch FlightGear and set Settings->Additional Settings to the following: "--telnet=socket,in,10,127.0.0.1,6400,tcp".  
3. In FlightGear- press "Fly" and wait for loading.  
Notice: you can switch to chase view by pressing 'v' button twice.  
4. In FlightGear- choose the aircraft name and press Autostart.  
5. Enter an IP number- make sure to use only digits/'.'  
Notice: make sure you use the correct IP number of your computer (not local host). 
6. Enter a port number- make sure to use only digits
7. Press the "Connect" button to connect the server
8. The simulation is on, feel free to check it out and use the different features!  
Moreover, in any case you want to disconnect from the server and reconnect afterwards, you can use the "Disconnect" and "Connect" buttons.  
Notice: when the app is successfuly connected or disconnected, a proper massage appears.

## UML diagram:
![image](https://user-images.githubusercontent.com/63461543/123432200-c9c65980-d5d2-11eb-8a8d-d6b4e8b393b2.png)


## Demo:

