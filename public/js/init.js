function init() {
    document.addEventListener("mousedown", onMouseDown, false);
    document.addEventListener("mouseup", onMouseUp, false);
}

var onMouseDown = function (event) {
    console.log(event.target.classList);
    if (event.target.classList[0] != "not") {
        var paperX = event.clientX - parseInt(event.target.style.left);
        var paperZ = event.clientY - parseInt(event.target.style.top);
        console.log(paperX + " " + paperZ);
        relationalCoords[0] = paperX;
        relationalCoords[1] = paperZ;
        magicHappens(event.target);

        var percentageSizeX = parseInt(event.target.style.width) - 20;
        var percentageSizeY = parseInt(event.target.style.height) - 20;
        if (relationalCoords[0] < percentageSizeX && relationalCoords[1] < percentageSizeY) {
            event.target.addEventListener("mousemove", onMouseMove1, false);
            whichMove = 1
        }
        else {
            event.target.addEventListener("mousemove", onMouseMove2, false);
            whichMove = 2
        }
    }
};

var onMouseUp = function (event) {
    if (whichMove == 1)
        event.target.removeEventListener("mousemove", onMouseMove1);
    else if (whichMove == 2)
        event.target.removeEventListener("mousemove", onMouseMove2);
    console.log("Mouse is going up..")
};

var onMouseMove1 = function (event) {
    var trueTarget = document.getElementById(event.target.id);

    trueTarget.style.left = (event.clientX - relationalCoords[0]) + "px";
    trueTarget.style.top = (event.clientY - relationalCoords[1]) + "px";

};
var onMouseMove2 = function (event) {
    var trueTarget = document.getElementById(event.target.id);

    console.log(" Resize thing");
    trueTarget.style.height = (event.clientY - parseInt(event.target.style.top)) + 25 + "px";
    trueTarget.style.width = (event.clientX - parseInt(event.target.style.left)) + 25 + "px";
};

function magicHappens(element) {
    for (var i = 0; i < elemsList.length; i++) {
        if (elemsList[i] != 0 && document.getElementById(elemsList[i].id).style.zIndex != 1) {
            document.getElementById(elemsList[i].id).style.zIndex = 1;
        }
    }
    element.style.zIndex = 2;
}