#Import Statements
import RPi.GPIO as GPIO 
import smbus 
import time 
from pyrebase import pyrebase

#Connecting To Database
from pyrebase import pyrebase
config = {
    "apiKey": "AIzaSyD1C1oFmuUKBPeRyCjj5Z4G7m443A5H4lw",
    "authDomain": "radiopi-6998d.firebaseapp.com",
    "databaseURL": "https://radiopi-6998d.firebaseio.com",
    "projectId": "radiopi-6998d",
    "storageBucket": "radiopi-6998d.appspot.com",
    "serviceAccount": "/home/pi/Documents/RadioPi-c20135ff3ec2.json"
    }

firebase = pyrebase.initialize_app(config)

#Authentication

auth = firebase.auth()
#authenticate a user
user = auth.sign_in_with_email_and_password("aldondreu9@gmail.com", "bluejays123")

db = firebase.database()

all_values = db.child("Stations").child("Frequency").child("Name").get(user['idToken']).val()

print all_values

#Initialization of FM Tuner

i2c = smbus.SMBus(1) #use 0 for older RasPi 
 
GPIO.setmode(GPIO.BCM) #board numbering
GPIO.setup(23, GPIO.OUT) 
GPIO.setup(0, GPIO.OUT)  #SDA or SDIO 
 
#put SI4703 into 2 wire mode (I2C) 
GPIO.output(0,GPIO.LOW) 
time.sleep(.1) 
GPIO.output(23, GPIO.LOW) 
time.sleep(.1) 
GPIO.output(23, GPIO.HIGH) 
time.sleep(.1) 
 
address = 0x10 #address of SI4703 from I2CDetect utility
 
print "Initial Register Readings" 
reg = i2c.read_i2c_block_data(address, 0, 32) 
print reg 
 
#write x8100 to reg 7 to activate oscellitor
list1 = [0,0,0,0,0,0,0,0,0,129,0] 
w6 = i2c.write_i2c_block_data(address, 0, list1) 
time.sleep(1) 
 
#write x4001 to reg 2 to turn off mute and activate IC
list1 = [1] 
#print list1 
w6 = i2c.write_i2c_block_data(address, 64, list1) 
time.sleep(.1) 
 
#write volume 
print "Doing VOlume lowest setting" 
list1 = [15,0,0,0,0,0,15] 
w6 = i2c.write_i2c_block_data(address, 64, list1) 
 
#write channel 
print "Setting Channel, pick a strong one" 
 
nc = 1011 #this is 101.1 The Fox In Kansas City Classic Rock!! 
nc *= 10  #this math is for USA FM only 
nc -= 8750 
nc /= 20 
 
list1 = [1,128, nc] 
#set tune bit and set channel 
w6 = i2c.write_i2c_block_data(address, 64, list1) 
time.sleep(1) #allow tuner to tune 
# clear channel tune bit 
list1 = [1,0,nc] 
w6 = i2c.write_i2c_block_data(address, 64, list1) 
 
reg2 = i2c.read_i2c_block_data(address,64, 32) 
print reg2  #just to show final register settings
 
#You should be hearing music now! 
#Headphone Cord acts as antenna

var = 1
while var == 1 :
    
   print "Setting FM Frequency:"
   single_value = db.child("Stations").child("Frequency").child("Name").get(user['idToken']).val()
   print "Tuning", single_value

   nc = int(single_value)
   nc *= 10  #this math is for USA FM only, Greetings from San Jose California
   nc -= 8750 
   nc /= 20  
   print "Param: ", nc
   list1 = [1,128, nc] 
   #set tune bit and set channel 
   w6 = i2c.write_i2c_block_data(address, 64, list1) 
   time.sleep(.1) #allow tuner to tune 
   # clear channel tune bit 
   list1 = [1,0,nc] 
   w6 = i2c.write_i2c_block_data(address, 64, list1) 
   reg2 = i2c.read_i2c_block_data(address,64, 32) 
   print reg2  #Your newly entered Freq Param value will show up happily sitting at the 4th position of the registers array
   
   var2 = single_value
   while var == 1 :
       single_value = db.child("Stations").child("Frequency").child("Name").get(user['idToken']).val()
       if (var2 != single_value):
           print "Changing the Tuner", single_value
           break
