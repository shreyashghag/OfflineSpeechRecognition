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

    @BindView(R.id.result_tv)
    lateinit var result_tv: TextView
    @BindView(R.id.start_listen_btn)
    lateinit var start_listen_btn: Button
    @BindView(R.id.stop_listen_btn)
    lateinit var stop_listen_btn: Button
    @BindView(R.id.mute)
    lateinit var mute: Button

Step 6.

setup Listensers for buttons.

	start_listen_btn.setOnClickListener {
            setSpeechListener()
            result_tv.text = getString(R.string.you_may_speak)

        }
        stop_listen_btn.setOnClickListener {
            if(mSpeechManager != null) {
                result_tv.text = getString(R.string.destroied)
                mSpeechManager.destroy()
            }
        }
        mute.setOnClickListener {
            if(mSpeechManager!=null) {
                if(mSpeechManager.isInMuteMode) {
                    mute.text = getString(R.string.mute)
                    mSpeechManager.mute(false)
                }
                else
                {
                    mute.text = getString(R.string.un_mute)
                    mSpeechManager.mute(true)
                }
            }
        }

Step 7.

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
  
  
  
  
  
    
MIT License

Copyright (c) 2020 Shreyash Ghag

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
  
