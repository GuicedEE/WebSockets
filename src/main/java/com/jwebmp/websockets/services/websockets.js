jw.websocket = {};
jw.websocket.connected = false;
jw.websocket.pollTime = 5000;
jw.websocket.pollCount = 1;
jw.websocket.queuedMessages = [];

var wsAddy = jw.siteAddress.replace('http', 'ws');
wsAddy = wsAddy.replace('https', 'ws');
jw.websocket.address = wsAddy + 'jwebmpwssocket';

jw.websocket.reconnect = function () {
    jw.websocket.connection = new WebSocket(jw.websocket.address);
    jw.websocket.connected = true;
};

jw.websocket.reconnect();

jw.websocket.connection.onmessage = function (e) {
    console.log(e.data);
    try {
        jw.actions.processResponse(e.data, $scope, $parse, $timeout, $compile);
    } catch (e) {
        console.log('This doesn\'t look like a valid JSON: ' + e.data);
    }
};

jw.websocket.authdataproviders = [];


jw.websocket.connection.onopen = function (e) {
    jw.websocket.reconnectTimer.stop();
    jw.websocket.connected = true;
    if (jw.websocket.timer)
        jw.websocket.timer.start();

    WS_AUTH_DATA_PROVIDER_LOAD;

    var dataOut = {};

    $.each(jw.websocket.authdataproviders, function (e) {
        var name = this.name;
        var data = this.data;
        dataOut[name] = data;
    });

    jw.websocket.newMessage('Auth', dataOut);
};

jw.websocket.connection.onclose = function (e) {
    if(e !== undefined)
        console.log('on close ' + e.data);
    else
        console.log('on close - No Data Object');

    jw.websocket.connected = false;
};

jw.websocket.connection.onerror = function (e) {
    console.log('on error ' + e.data);
    jw.websocket.connected = false;
};

jw.websocket.sendPlainTextMessage = function (a) {
    jw.websocket.newMessage('PlainText', {message: a})
};

jw.websocket.newMessage = function (type, data) {
    var news = {};
    news.action = type;
    news.data = data;
    news.data.sessionid = jw.sessionid[0].replace('JSESSIONID=', '');
    jw.websocket.queuedMessages.push(news);
};

jw.websocket.newMessageNow = function (type, data) {
    var news = {};
    news.action = type;
    news.data = data;
    news.data.sessionid = jw.sessionid[0].replace('JSESSIONID=', '');
    jw.websocket.connection.send(JSON.stringify(news));
};

jw.websocket.timer = new DeltaTimer(function (time) {
    //alert('messages : ' + jw.websocket.queuedMessages);
    if (jw.websocket.queuedMessages.length > 0) {
        if (jw.websocket.connected) {
            try {
                var i = jw.websocket.queuedMessages.length;
                while (i--) {
                    jw.websocket.connection.send(JSON.stringify(jw.websocket.queuedMessages[i]));
                    jw.websocket.queuedMessages.splice(i, 1);
                }
            } catch (e) {
                //console.log("Error going through queued messages");
            }
        }
        else {
            jw.websocket.reconnect();
        }
    }
}, 500, jw.websocket.timer);
jw.websocket.timerobj = jw.websocket.timer.start();

jw.websocket.reconnectTimer = new DeltaTimer(function (time) {
    if (!jw.websocket.connected) {
        jw.websocket.timer.stop();
        jw.websocket.reconnect();
        jw.websocket.pollCount++;
        jw.websocket.reconnectTimer.delay = Math.max(jw.websocket.pollCount * jw.websocket.pollTime, 0);
    }
    else {
        jw.websocket.pollCount = 1;
    }
}, 10000, jw.websocket.reconnectTimer);

jw.websocket.reconnectTimerObject = jw.websocket.reconnectTimer.start();
