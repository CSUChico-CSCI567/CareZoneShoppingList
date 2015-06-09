# CareZoneShoppingList
This is an app example Aaron McIntyre to demo recyclerview, cardview, and retrofit w/ OkHttp. The rest of the readme was provided by Aaron:

## About
SampleApp for CareZone android position
I know that the instructions say only spend 3 hours on this but I enjoy learning new things and I had a couple of libraries I have been watching but never got a chance to use(FAB and Material Dialogs). So I spent some time learning some new stuff before I started to code this. The coding took around 2-3 hours. My learning added another 3 hours and I spent an hour whitebroading the program structure. Then spent a little time making this nice README. Enjoy and thank you for the opportunity to show you how I code.

## Using this project
There are 2 things to do in order to use this project.

1. Add a file called release.properties to your app directory. There is a sample version of this file that you can copy and use.

2. Add a file called api-keys.xml that contains your API key for the carezone shopping list app.

## Developer Notes
This app uses the appcompat library, the support library, and 4/5 3rd party libraries

#### Support Library
##### 'com.android.support:cardview-v7:21.0.3'
and
##### 'com.android.support:recyclerview-v7:21.0.3'
#### 3rd party libraries

##### 'com.squareup.retrofit:retrofit:1.9.0'
I use this far all http calls and editing the headers to add the api key

##### 'com.squareup.okhttp:okhttp:2.3.0'
I used okhttp to get some sort of http caching. I am actually not sure if it works because the headers being returned from the server tell the client to not cache. I would need to test more.
##### 'com.google.code.gson:gson:2.3'
I do not really consider this as a third party library but technically it is. It is used with retrofit to parse the JSON data into objects.
##### 'com.melnykov:floatingactionbutton:1.2.0'
This library is a really neat implementation of the Floating action button that is not yet in the support library. To get material design on pre 5.0 devices you need to either implement it yourself or use somebody elses implementation. I dont reinvent wheels and this library seemed very active within this year.
##### 'com.afollestad:material-dialogs:0.6.7.2'
This library is a nice implementation of a material alert dialog. This feature is also not in the support library and while it mostly consists of style options it was nice to not have to build myself.
