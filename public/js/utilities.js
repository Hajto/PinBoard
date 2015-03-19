var refference ={
    serverAddress: "http://localhost:9000/"
};
//Targeting utils
function getPaperClipById(targetId){
    for(var i=0; i < elemsList.length; i++){
        if(elemsList[i].id == targetId)
            return elemsList[i];
    }
    return null;
}

//Show,hide things
function showMCE(){
    document.getElementById("mainShadowBox").style.display = "block";
}
function hideMCE(){
    document.getElementById("mainShadowBox").style.display = "none";
}

//MCE content utilities
function getContentFromMCE(){
    return tinyMCE.get('content').getContent();
}
function setContentInMCE(content,targetId){
    tinyMCE.get('content').setContent(content);
    document.getElementById("mainShadowBoxButton").dataset.nowProcessingObjectId = targetId;
}
function writeChanges(data){
    var objectReference = getPaperClipById(data.dataset.nowProcessingObjectId);
    objectReference.setInsideText(getContentFromMCE());
    hideMCE();
}
