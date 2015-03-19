function ajaxAPI(haitoRequest){
    var xmlhttp;
    if (window.XMLHttpRequest)
    {// code for IE7+, Firefox, Chrome, Opera, Safari
        xmlhttp=new XMLHttpRequest();
    }
    else
    {// code for IE6, IE5
        xmlhttp=new ActiveXObject("Microsoft.XMLHTTP");
    }
    xmlhttp.onreadystatechange=function()
    {
        if (xmlhttp.readyState==4) {
            if(xmlhttp.status != 200){
                haitoRequest.onError(xmlhttp.responseText);
            }else {
                haitoRequest.onSuccess(xmlhttp.responseText);
            }
        }
    };
    xmlhttp.open(haitoRequest.method,haitoRequest.url,true);
    xmlhttp.setRequestHeader("Content-type","application/json");
    xmlhttp.setRequestHeader("Accept","application/json, application/json");
    xmlhttp.send(haitoRequest.data);
}

function _haitoRequest(url, method, data, onSuccess, onError){
    this.url = url;
    this.method = method;
    this.data = data;
    this.onSuccess = onSuccess;
    this.onError = onError;
}