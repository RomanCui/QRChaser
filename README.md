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

We built a mobile application that allows us to hunt for the coolest QR codes that score the most points. Players will run around scanning QR codes, trying to find codes that give them the most points. QR codes will be hashed and the hashes they produce will be analyzed and scored. We let users to compete with each other for the highest scoring QR codes, the most QR codes, the highest sum of QR codes, or highest scoring QR codes in a region.

## <div align="center">User Manual</div>

A user can start using this app by creating a new account. If the user already have an account, a login QR code will be saved in your device and the user can use it for login.

<div align="left">
  <p>
    <img width="300" src="https://github.com/RonggangCui/QRChaser/blob/main/doc/AppScreenshots/AppScreenshots-01.png"></a>
  </p>
</div>

After login, you can see all the QR codes that you already obtained with the associated score. 

<div align="left">
  <p>
    <img width="300" src="https://github.com/RonggangCui/QRChaser/blob/main/doc/AppScreenshots/AppScreenshots-02.png"></a>
  </p>
</div>

You can click on the QR code to see more details or make some changes

<div align="left">
  <p>
    <img width="300" src="https://github.com/RonggangCui/QRChaser/blob/main/doc/AppScreenshots/AppScreenshots-03.png"></a>
  </p>
</div>

The second tab on the bottom manu shows a page that allow you to see other players on this app and all of their QR codes. 

<div align="left">
  <p>
    <img width="300" src="https://github.com/RonggangCui/QRChaser/blob/main/doc/AppScreenshots/AppScreenshots-04.png"></a>
  </p>
</div>

<div align="left">
  <p>
    <img width="300" src="https://github.com/RonggangCui/QRChaser/blob/main/doc/AppScreenshots/AppScreenshots-06.png"></a>
  </p>
</div>

The third tab on the bottom manu shows a page that allow you to see other players on this app and all of their QR codes.
This feature is still under development and the map is a placeholder for now.

<div align="left">
  <p>
    <img width="300" src="https://github.com/RonggangCui/QRChaser/blob/main/doc/AppScreenshots/AppScreenshots-08.png"></a>
  </p>
</div>

The last tab on the bottom manu shows your profile and you can edit your profile there.

<div align="left">
  <p>
    <img width="300" src="https://github.com/RonggangCui/QRChaser/blob/main/doc/AppScreenshots/AppScreenshots-09.png"></a>
  </p>
</div>

<div align="left">
  <p>
    <img width="300" src="https://github.com/RonggangCui/QRChaser/blob/main/doc/AppScreenshots/AppScreenshots-10.png"></a>
  </p>
</div>





Users:
	• Player: a person who plays the game <br>
	• Owner: the entity that owns the infrastructure that the game runs on.

I open my QRHunter app. I see a QR code in my wallet. I indicate I want to add a new QR code and I use the phone camera to add the QR code. The QR code is scored and I’m told that my QR score is 30. The system prompts me for a photo of the object I scanned. I decline since this was an ID card. I also decline geolocation because it is in my wallet. The system adds the 30 points to my total score and records a hash of the QR code. I then see some sticker on a pole. I scan it and am told it is worth 1000 points! I record the geolocation and take a photo of the pole and save it to my account. 1000 points wow. Then I see that other users have found this pole as well. So I open the map for nearby QR codes and I see something worth 10000 is 100 meters away so I’m going to head on over there!
