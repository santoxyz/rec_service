import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:rec_service/rec_service.dart';

void main() {
  runApp(MyApp());
}

class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion = 'Unknown';
  bool _muted = false;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }

  // Platform messages are asynchronous, so we initialize in an async method.
  Future<void> initPlatformState() async {
    String platformVersion;
    // Platform messages may fail, so we use a try/catch PlatformException.
    try {
      platformVersion = await RecService.platformVersion;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }

    // If the widget was removed from the tree while the asynchronous platform
    // message was in flight, we want to discard the reply rather than calling
    // setState to update our non-existent appearance.
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
    });
  }

  void updateState(dynamic newState){
    if (!mounted) return;

    setState(() {
      _platformVersion = newState;
    });
  }
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('rec_service example app'),
        ),
        body: Center(
          child: new Column(

            children: <Widget>[
              Text('Running on: $_platformVersion\n'),
              new RaisedButton(
                child: new Text("REC"),
                color:  Colors.blueAccent[600],
                onPressed: () async {print("REC pressed");updateState(await RecService.start);},
              ),

              new RaisedButton(
                child: new Text("PAUSE"),
                color:  Colors.blueAccent[600],
                onPressed:  () async {print("PAUSE pressed");updateState(await RecService.pause);},
              ),

              new RaisedButton(
                child: new Text("STOP"),
                color:  Colors.blueAccent[600],
                onPressed:  () async {print("STOP pressed");updateState(await RecService.stop);},
              ),

              new RaisedButton(
                child: new Text("MUTE"),
                color:  Colors.blueAccent[600],
                onPressed:  () async {print("MUTE pressed");updateState(await RecService.mute(_muted = !_muted));},
              ),
            ],
          ),
        ),
      ),
    );
  }
}
