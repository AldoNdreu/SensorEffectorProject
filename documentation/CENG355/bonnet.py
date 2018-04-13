import vlc
import time
import urllib
from pyrebase import pyrebase

#Connect to Database
config = {
	"apiKey": "AIzaSyD1C1oFmuUKBPeRyCjj5Z4G7m443A5H4lw",
	"authDomain": "radiopi-6998d.firebaseapp.com",
	"databaseURL": "https://radiopi-6998d.firebaseio.com",
	"projectId": "radiopi-6888d",
	"storageBucket": "radiopi-6998d.appspot.com",
	"serviceAccount": "/home/pi/Documents/RadioPi-c20135ff3ec2.json"
	}

firebase = pyrebase.initialize_app(config)

auth = firebase.auth()
user = auth.sign_in_with_email_and_password("ErickCantos2018@gmail.com", "HumberCollege")
db = firebase.database()

var2 = None
var = 1

while var == 1 :
	

	oldsingle_value = db.child("Stations").child("Frequency").child("Name").get(user['idToken']).val()
	single_value = int(oldsingle_value) 
	print single_value
	var2 = single_value 
	# This block can turn into switch case statement
	if(single_value == 973):
		p = vlc.MediaPlayer('http://newcap.leanstream.co/CHBMFM?args=3rdparty_02&uid=1a03ae7d-8b81-4a05-83c5-ecdaad822d6d')
		p.play()
		time.sleep(2)
		var2 = single_value
		
		while var == 1:
			oldsingle_value = db.child("Stations").child("Frequency").child("Name").get(user['idToken']).val()
			single_value = int(oldsingle_value)
			if(var2 != single_value):
				print "Changed Tuned", single_value
				p.stop()
				break
				

	elif(single_value == 941):
		p = vlc.MediaPlayer('http://cbc_r2_tor.akacast.akamaistream.net/7/364/451661/v1/rc.akacast.akamaistream.net/cbc_r2_tor')
		p.play()
		time.sleep(2)
		var2 = single_value
		
		while var == 1:
			oldsingle_value = db.child("Stations").child("Frequency").child("Name").get(user['idToken']).val()
			single_value = int(oldsingle_value)
			if(var2 != single_value):
				print "Changed Tuned", single_value
				p.stop()
				break

        elif(single_value == 969):
        	p = vlc.MediaPlayer('http://radio_cklg-lh.akamaihd.net/i/VAN969_1@183241/index_48_a-b.m3u8?sd=10&rebase=on')
		p.play()
		time.sleep(2)
		var2 = single_value
		
		while var == 1:
			oldsingle_value = db.child("Stations").child("Frequency").child("Name").get(user['idToken']).val()
			single_value = int(oldsingle_value)
			if(var2 != single_value):
				print "Changed Tuned", single_value
				p.stop()
				break
	
	elif(single_value == 925):
		p = vlc.MediaPlayer('http://radio_ckis-lh.akamaihd.net/i/TOR925_1@176956/index_48_a-p.m3u8?sd=10&rebase=on')
		p.play()
		time.sleep(2)
		var2 = single_value
		
		while var == 1:
			oldsingle_value = db.child("Stations").child("Frequency").child("Name").get(user['idToken']).val()
			single_value = int(oldsingle_value)
			if(var2 != single_value):
				print "Changed Tuned", single_value
				p.stop()
				break

	elif(single_value == 935):
		p = vlc.MediaPlayer('http://newcap.leanstream.co/CFXJFM?args=3rdparty_02&uid=fca8e4a9-dcf4-4b05-9b6a-85af80a32d74')
		p.play()
		time.sleep(2)
		var2 = single_value
		
		while var == 1:
			oldsingle_value = db.child("Stations").child("Frequency").child("Name").get(user['idToken']).val()
			single_value = int(oldsingle_value)
			if(var2 != single_value):
				print "Changed Tuned", single_value
				p.stop()
				break
        		
	elif(single_value == 1035):
		p = vlc.MediaPlayer('http://ice23.securenetsystems.net/CIDC?playSessionID=556E08CF-155D-C0F3-03C6BFB41A25C35B')
		p.play()
		time.sleep(2)
		var2 = single_value
		
		while var == 1:
			oldsingle_value = db.child("Stations").child("Frequency").child("Name").get(user['idToken']).val()
			single_value = int(oldsingle_value)
			if(var2 != single_value):
				print "Changed Tuned", single_value
				p.stop()
				break
	else:
		print "Error: No radio station found on data base"
		break

#Instance = vlc.Instance()
#player = Instance.media_player_new()
#media = Instance.media_new(url)
#player.set_media(media)
#player.play()

#time.sleep(2)

#p = vlc.MediaPlayer(url)
#p.play()
#time.sleep(2)
	
while var == 1:
	pass
	
	single_value = db.child("Stations").child("Frequency").child("Name").get(user['idToken']).val()
	if(var2 != single_value):
		print "Changed Tune", single_value
		break
		



