
import 'dart:async';

import 'package:flutter/services.dart';

class RecService {
  static const MethodChannel _channel =
      const MethodChannel('rec_service');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future<String> start({String prefix, int chunkSize, bool alsoWholeRec}) async {
    final String version = await _channel.invokeMethod('START',{"prefix":prefix, "chunkSize":chunkSize, "alsoWholeRec":alsoWholeRec});
    return version;
  }

  static Future<String> get stop async {
    final String version = await _channel.invokeMethod('STOP');
    return version;
  }

  static Future<String> pause(bool p) async {
    final String version = await _channel.invokeMethod('PAUSE',p);
    return version;
  }

  static Future<String> mute(bool m) async {
    final String version = await _channel.invokeMethod('MUTE',m);
    return version;
  }

}
