import 'dart:convert';
import 'dart:ui';

import 'package:flutter/material.dart';
import 'package:flutter/services.dart';

void main() => runApp(chooseWidget(window.defaultRouteName));

Widget chooseWidget(String route) {
  switch (route) {
    case 'gotoFlutter':
      return FlutterUi();
    default:
      return Center(child: Text("Unknown Route"));
  }
}

class FlutterUi extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      debugShowCheckedModeBanner: false,
      home: FlutterUiHomePage(),
    );
  }
}

class FlutterUiHomePage extends StatefulWidget {
  @override
  _FlutterUiHomePageState createState() => _FlutterUiHomePageState();
}

class _FlutterUiHomePageState extends State<FlutterUiHomePage> {
  TextEditingController textFieldController = TextEditingController();
  String _resultAndroidData = "";
  String _resultToSendData = "";
  String resultStr = "";

  static const platform = const MethodChannel('com.jp.flutterintonative/data');

  _FlutterUiHomePageState() {
    platform.setMethodCallHandler(_receiveFromHost);
  }

  Future<void> _receiveFromHost(MethodCall call) async {
    String d = "";

    try {
      print(call.method);

      if (call.method == "fromHostToClient") {
        final String data = call.arguments;
        print(call.arguments);
        final jData = jsonDecode(data);

        d = jData['data'];
      }
    } on PlatformException catch (e) {
      //platform may not able to send proper data.
    }

    setState(() {
      _resultAndroidData = d;
    });
  }

  _setResults(String txt) {
    setState(() {
      _resultToSendData = txt;
    });
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      body: Center(
        child: Column(
          mainAxisAlignment: MainAxisAlignment.center,
          children: <Widget>[
            Padding(
              padding: const EdgeInsets.all(32.0),
              child: TextField(
                controller: textFieldController,
                style: TextStyle(
                  fontSize: 24,
                  color: Colors.black,
                ),
              ),
            ),
            RaisedButton(
              child: Text(
                'Send to Android Screen',
                style: TextStyle(fontSize: 24),
              ),
              onPressed: () {
                _sendResultsToAndroidiOS();
              },
            ),
            Container(
                margin: EdgeInsets.all(10),
                child: Text(
                  'Result: $_resultAndroidData',
                  style: TextStyle(color: Colors.blue, fontSize: 16),
                ))
          ],
        ),
      ),
    );
  }

  void _sendResultsToAndroidiOS() {
    _setResults(textFieldController.text.toString());
    Map<String, dynamic> resultMap = Map();
    resultMap['result'] = _resultToSendData;
    setState(() {
      resultStr = resultMap.toString();
    });

    platform.invokeMethod("FromClientToHost", resultMap);
  }
}
