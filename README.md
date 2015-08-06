# VideoStreamLatest


Copy ```media/video.mp4``` to emulator:

```
adb push video.mp4 /sdcard/reversedub/video.mp4
```

This works on an android device. On an emulator, the video might be slow and hang. 
If you want to run on emulator, comment out the audio recording by commenting out all occurences of
```
mediaRecordWrapper.onRecord(audioPlayToggle);
```

Running on android device:
http://developer.android.com/training/basics/firstapp/running-app.html
