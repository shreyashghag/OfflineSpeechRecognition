package com.android.offlinespeechrec
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log

class SpeechRecognizerManager(context: Context, listener:onResultsReady) {
    protected var mAudioManager: AudioManager
    protected var mSpeechRecognizer: SpeechRecognizer
    protected var mSpeechRecognizerIntent: Intent
    protected var mIsListening:Boolean = false
    private var mIsStreamSolo:Boolean = false
    var isInMuteMode = true
    private lateinit var  mListener:onResultsReady
    init{
        try
        {
            mListener = listener
        }
        catch (e:ClassCastException) {
            Log.e(TAG, e.toString())
        }
        mAudioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
        mSpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
        mSpeechRecognizer.setRecognitionListener(SpeechRecognitionListener())
        mSpeechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
            RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        mSpeechRecognizerIntent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
            context.getPackageName())
        startListening()
    }

    private fun listenAgain() {
        if (mIsListening)
        {
            mIsListening = false
            mSpeechRecognizer.cancel()
            startListening()
        }
    }

    private fun startListening() {
        if (!mIsListening)
        {
            mIsListening = true
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN)
            {
                // turn off beep sound
                if (!mIsStreamSolo && isInMuteMode)
                {
                    mAudioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, true)
                    mAudioManager.setStreamMute(AudioManager.STREAM_ALARM, true)
                    mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, true)
                    mAudioManager.setStreamMute(AudioManager.STREAM_RING, true)
                    mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, true)
                    mIsStreamSolo = true
                }
            }
            mSpeechRecognizer.startListening(mSpeechRecognizerIntent)
        }
    }

    fun destroy() {
        mIsListening = false
        if (!mIsStreamSolo)
        {
            mAudioManager.setStreamMute(AudioManager.STREAM_NOTIFICATION, false)
            mAudioManager.setStreamMute(AudioManager.STREAM_ALARM, false)
            mAudioManager.setStreamMute(AudioManager.STREAM_MUSIC, false)
            mAudioManager.setStreamMute(AudioManager.STREAM_RING, false)
            mAudioManager.setStreamMute(AudioManager.STREAM_SYSTEM, false)
            mIsStreamSolo = true
        }
        Log.d(TAG, "onDestroy")
        if (mSpeechRecognizer != null)
        {
            mSpeechRecognizer.stopListening()
            mSpeechRecognizer.cancel()
            mSpeechRecognizer.destroy()
        }
    }

    inner class SpeechRecognitionListener: RecognitionListener {
        override fun onBeginningOfSpeech() {}
        override fun onBufferReceived(buffer:ByteArray) {
        }
        override fun onEndOfSpeech() {}
        @Synchronized override fun onError(error:Int) {
            if (error == SpeechRecognizer.ERROR_RECOGNIZER_BUSY)
            {
                if (mListener != null)
                {
                    val errorList = ArrayList<String>(1)
                    errorList.add("ERROR RECOGNIZER BUSY")
                    if (mListener != null)
                        mListener.onResults(errorList)
                }
                return
            }
            if (error == SpeechRecognizer.ERROR_NO_MATCH)
            {
                if (mListener != null)
                    mListener.onResults(null!!)
            }
            if (error == SpeechRecognizer.ERROR_NETWORK)
            {
                val errorList = ArrayList<String>(1)
                errorList.add("STOPPED LISTENING")
                if (mListener != null)
                    mListener.onResults(errorList)
            }
            Log.d(TAG, "error = " + error)
            Handler().postDelayed(object:Runnable {
                public override fun run() {
                    listenAgain()
                }
            }, 100)
        }

        override fun onEvent(eventType:Int, params: Bundle) {
        }

        override fun onPartialResults(partialResults:Bundle) {
        }

        override fun onReadyForSpeech(params:Bundle) {}

        override fun onResults(results:Bundle) {
            if (results != null && mListener != null)
                mListener.onResults(results.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)!!)
            listenAgain()
        }

        override fun onRmsChanged(rmsdB:Float) {}
    }

    fun ismIsListening():Boolean {
        return mIsListening
    }

    interface onResultsReady {
        fun onResults(results:ArrayList<String>)
    }

    fun mute(mute:Boolean) {
        isInMuteMode = mute
    }

    companion object {
        private val TAG = "SpeechRecognizerManager"
    }
}