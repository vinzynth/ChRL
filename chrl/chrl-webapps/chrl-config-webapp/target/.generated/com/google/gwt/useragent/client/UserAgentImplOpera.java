package com.google.gwt.useragent.client;

public class UserAgentImplOpera implements com.google.gwt.useragent.client.UserAgent {
  
  public native String getRuntimeValue() /*-{
    var ua = navigator.userAgent.toLowerCase();
    if ((function() { 
      return (ua.indexOf('opera') != -1);
    })()) return 'opera';
    if ((function() { 
      return (ua.indexOf('webkit') != -1);
    })()) return 'safari';
    if ((function() { 
      return (ua.indexOf('msie') != -1 && ($doc.documentMode == 10));
    })()) return 'ie10';
    if ((function() { 
      return (ua.indexOf('msie') != -1 && ($doc.documentMode >= 9));
    })()) return 'ie9';
    if ((function() { 
      return (ua.indexOf('msie') != -1 && ($doc.documentMode >= 8));
    })()) return 'ie8';
    if ((function() { 
      return (ua.indexOf('gecko') != -1);
    })()) return 'gecko1_8';
    return 'unknown';
  }-*/;
  
  
  public String getCompileTimeValue() {
    return "opera";
  }
}
