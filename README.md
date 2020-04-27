# OfflineSpeechRecognition


To get a Git project into your build:

Step 1. Add the JitPack repository to your build file

gradle
Add it in your root build.gradle at the end of repositories:

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
Step 2. Add the dependency
   
       dependencies {
         implementation 'com.github.shreyashghag:OfflineSpeechRecognition:v0.0.1'
       }
       
Step 3.
declare the variable

    lateinit var mSpeechRecognizerManager: SpeechRecognizerManager

Step 4.    
check for runtime permission
   
   
      if (PermissionHandler.checkPermission(this, PermissionHandler.RECORD_AUDIO)) {
           Log.d("MainActivity:","Permission Granted")
      }
      else{
            PermissionHandler.askForPermission(PermissionHandler.RECORD_AUDIO, this)
      }

Step 5.
initailze the mSpeechRecognizerManager

      mSpeechManager=SpeechRecognizerManager(this,object:SpeechRecognizerManager.onResultsReady{
            override fun onResults(results: ArrayList<String>) =
                if (results != null && results.size > 0) {
                    var results1=results
                    Log.e("error",results.toString())
                    Log.e("error",results1.toString())
                    if (results1.size == 1) {
                        mSpeechManager.destroy()
                        mSpeechManager = null!!
                        //here result_tv is the textview to show the results.
                        
                        result_tv.text = results1[0]
                    } else {
                        val sb = StringBuilder()
                        if (results1.size > 5) {
                            results1 = results1.subList(0, 5) as ArrayList<String>
                            Log.d("results",results1[0].toString())
                        }
                        for (result in results1) {
                            sb.append(result).append("\n")
                        }
                        result_tv.text = sb.toString()
                    }
                } else result_tv.text = getString(R.string.no_results_found)
        })
        
        
        
        
        
  Methods/properties which can be used are
  | Method Name| Description |
  | ---------- | ----------- |
  |isInMuteMode|returns back a boolean value whether it is mute mode or not.|
  |ismIsListening|returns back a boolean value whether it is Listening or not.|
  |mute(boolean:boolean)|accepts a boolean value as parameter to mute or unmute the mic.|
  |destroy|destroys the mSpeechRecognizerManager.|
  
