

#include <ESP8266WiFi.h>
#include <FS.h>   //Include File System Headers

String ssid = "4Relays";

WiFiServer server(80);

bool states[5] = { false, false, false, false, false };
int pins[5] = { 5, 4, 12, 14, 13 };


void setupWiFi(String password)
{
  WiFi.mode(WIFI_AP);

  uint8_t mac[WL_MAC_ADDR_LENGTH];
  WiFi.softAPmacAddress(mac);
  String macID = String(mac[WL_MAC_ADDR_LENGTH - 2], HEX) +
                 String(mac[WL_MAC_ADDR_LENGTH - 1], HEX);
  macID.toUpperCase();
  String AP_NameString = ssid + "-" + macID;


  WiFi.softAP(AP_NameString.c_str(), password.c_str());
}


void setup() 
{
  delay(1000);
  Serial.begin(115200);

  setupWiFi("12345678");

  for (int i = 0; i < 5; i++)
  {
    pinMode(pins[i], OUTPUT);
    digitalWrite(pins[i], states[i]);
  }

 
  server.begin();
}

void loop() 
{
  // Check if a client has connected
  WiFiClient client = server.available();
  if (!client) {
    return;
  }
 
  // Wait until the client sends some data
  //  Serial.println("new client");
  while(!client.available()){
    delay(1);
  }
 
  // Read the first line of the request
  String req = client.readStringUntil('\r');
 
  client.flush();
  
  String html = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\n\r\n";
 
  if (req.indexOf("/on") != -1)
  {
    for (int i = 0; i < 5; i++)
    {
      states[i] = true;
      pinMode(pins[i], true);
    }
  }
  else if (req.indexOf("/off") != -1)
  {
    for (int i = 0; i < 5; i++)
    {
      states[i] = false;
      pinMode(pins[i], false);
    }
  }
  else if (req.indexOf("/mon0") != -1)
  {
      states[0] = true;
      pinMode(pins[0], true);
  }
  else if (req.indexOf("/moff0") != -1)
  {
      states[0] = false;
      pinMode(pins[0], false);
  }
  else if (req.indexOf("/mon1") != -1)
  {
      states[1] = true;
      pinMode(pins[1], true);
  }
  else if (req.indexOf("/moff1") != -1)
  {
      states[1] = false;
      pinMode(pins[1], false);
  }
  else if (req.indexOf("/mon2") != -1)
  {
      states[2] = true;
      pinMode(pins[2], true);
  }
  else if (req.indexOf("/moff2") != -1)
  {
      states[2] = false;
      pinMode(pins[2], false);
  }
  else if (req.indexOf("/mon3") != -1)
  {
      states[3] = true;
      pinMode(pins[3], true);
  }
  else if (req.indexOf("/moff3") != -1)
  {
      states[3] = false;
      pinMode(pins[3], false);
  }
  else if (req.indexOf("/moff4") != -1)
  {
      states[4] = false;
      pinMode(pins[4], false);
  }

  for (int i = 0; i < 5; i++)
  {
    html += states[i] ? "on\n" : "off\n";
  }


  client.print(html);
  delay(1);
}
