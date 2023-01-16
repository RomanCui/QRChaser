## <div align="center">QRChaser</div>

<div align="center">
  <p>
     This is a mobile game app that let users to scan QR codes to get points and compete with other players
  </p>
</div>

## <div align="center">Software Design</div>
This app was developed in CMPUT 301 at University of Alberta in Winter 2022 under the instruction of Abram Hindle <br>
Developed using Java in Android Studio <br>
Used firebase for realtime update for the scores and QR code information

## <div align="center">Introduction</div>

We built a mobile application that allows us to hunt for the coolest QR codes that score the most points. Players will run around scanning QR codes, trying to find codes that give them the most points. QR codes will be hashed and the hashes they produce will be analyzed and scored. <br>
We let users to compete with each other for the highest scoring QR codes, the most QR codes, the highest sum of QR codes, or highest scoring QR codes in a region.

## <div align="center">User Manual</div>

When a player scans a QR code they will take a photo of what or where the QR code is and also record the geolocation of the QR code.
Players can see on a map local QR codes that other players have scanned.

Example use of the app: 
	
Users:
	• Player: a person who plays the game <br>
	• Owner: the entity that owns the infrastructure that the game runs on.

I open my QRHunter app. I see a QR code in my wallet. I indicate I want to add a new QR code and I use the phone camera to add the QR code. The QR code is scored and I’m told that my QR score is 30. The system prompts me for a photo of the object I scanned. I decline since this was an ID card. I also decline geolocation because it is in my wallet. The system adds the 30 points to my total score and records a hash of the QR code. I then see some sticker on a pole. I scan it and am told it is worth 1000 points! I record the geolocation and take a photo of the pole and save it to my account. 1000 points wow. Then I see that other users have found this pole as well. So I open the map for nearby QR codes and I see something worth 10000 is 100 meters away so I’m going to head on over there!
