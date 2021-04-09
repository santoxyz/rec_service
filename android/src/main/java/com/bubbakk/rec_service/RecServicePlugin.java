package com.bubbakk.rec_service;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;

/** RecServicePlugin */
public class RecServicePlugin implements FlutterPlugin, MethodCallHandler, ActivityAware {
  /// The MethodChannel that will the communication between Flutter and native Android
  ///
  /// This local reference serves to register the plugin with the Flutter Engine and unregister it
  /// when the Flutter Engine is detached from the Activity
  private Activity activity;
  private Context context;
  private MethodChannel channel;
  private static String TAG = "RecServicePlugin";

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    context = flutterPluginBinding.getApplicationContext();
    channel = new MethodChannel(flutterPluginBinding.getBinaryMessenger(), "rec_service");
    channel.setMethodCallHandler(this);
  }

  @Override
  public void onMethodCall(@NonNull MethodCall call, @NonNull Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    } else if (call.method.equals("START")) {
      startRecorder();
      result.success("Android " + android.os.Build.VERSION.RELEASE + " -- START");
    } else if (call.method.equals("STOP")) {
      stopRecorder();
      result.success("Android " + android.os.Build.VERSION.RELEASE + " -- STOP");
    } else if (call.method.equals("PAUSE")) {
      boolean p = (boolean)call.arguments;
      String pstr = p ? "ON" : "OFF";
      setPaused(p);
      result.success("Android " + android.os.Build.VERSION.RELEASE + " -- PAUSE " + pstr);
    } else if (call.method.equals("MUTE")) {
      boolean m = (boolean)call.arguments;
      String mstr = m ? "ON" : "OFF";
      setMuted(m);
      result.success("Android " + android.os.Build.VERSION.RELEASE + " -- MUTE " + mstr);
    }
    else {
      result.notImplemented();
    }
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    channel.setMethodCallHandler(null);
  }

  public void setMuted(boolean m) {
    Intent serviceIntent = new Intent(context, RecService.class);
    serviceIntent.putExtra("mute",m);
    ContextCompat.startForegroundService(activity, serviceIntent);
  }

  public void setPaused(boolean p) {
    Intent serviceIntent = new Intent(context, RecService.class);
    serviceIntent.putExtra("pause",p);
    ContextCompat.startForegroundService(activity, serviceIntent);
  }

  public void startRecorder() {
    Log.i(TAG, "Starting the foreground-thread");

    Intent serviceIntent = new Intent(context, RecService.class);
    serviceIntent.putExtra("inputExtra", "Foreground Service Example in Android");
    ContextCompat.startForegroundService(activity, serviceIntent);

  }

  public void stopRecorder() {
    Log.i(TAG, "Stopping the foreground-thread");

    Intent serviceIntent = new Intent(context, RecService.class);
    context.stopService(serviceIntent);

  }

  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    activity = binding.getActivity();

    if (ContextCompat.checkSelfPermission(activity,
            Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(activity,
              new String[]{Manifest.permission.RECORD_AUDIO},
              1234);
    }
    if (ContextCompat.checkSelfPermission(activity,
            Manifest.permission.WRITE_EXTERNAL_STORAGE)
            != PackageManager.PERMISSION_GRANTED) {
      ActivityCompat.requestPermissions(activity,
              new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
              1235);
    }
  }


  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }
}
