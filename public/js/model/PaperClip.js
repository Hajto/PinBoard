function _paperClips(id, text, width, height, posX, posY, posZ) {
    elemsList.push(this);

    this.id = 0;

    if (width == undefined ||
        height == undefined ||
        posX == undefined ||
        posY == undefined ||
        posZ == undefined) {

        this.width = 200;
        this.height = 200;

        this.posX = 100;
        this.posY = 1;
        this.posZ = 100;

        this.insideText = "<p>Nowa kartka</p>";
    } else {
        this.width = width;
        this.height = height;

        this.id = id;
        this.insideText = text;

        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ
    }

    this.createAndInit = function () {
        var thing = this;

        if (this.id == 0) {
            ajaxAPI(new _haitoRequest(refference.serverAddress + "db/insertPaperClip",
                    "POST",
                    JSON.stringify({
                        tid: tid,
                        text: thing.insideText,
                        width: thing.width,
                        height: thing.height,
                        posX: thing.posX,
                        posY: thing.posY,
                        posZ: thing.posZ
                    }),
                    function (res) {
                        thing.id = JSON.parse(res).id;
                        appendToBody()
                    },
                    function () {
                        alert("Się nie udało się, karteczka nie zostanie zapisana");
                        elemsList.pop()
                    }
                )
            );
        } else {
            appendToBody();
            //alert("Coś!")
        }

        function appendToBody() {
            var paperClip = document.createElement("div");
            paperClip.style.width = thing.width + "px";
            paperClip.style.height = thing.height + "px";
            paperClip.style.position = "absolute";
            paperClip.style.left = thing.posX + "px";
            paperClip.style.top = thing.posZ + "px";
            paperClip.style.zIndex = thing.posY;
            paperClip.style.border = "dotted";
            paperClip.style.backgroundColor = "#C0C0C0";
            paperClip.id = thing.id.toString();
            paperClip.innerHTML = thing.insideText;

            paperClip.appendChild(generateEditButton(thing.id));

            console.log(paperClip);
            document.body.appendChild(paperClip);
        }

    };
    this.updateView = function () {
        var paperClip = document.getElementById(this.id);
        paperClip.style.width = this.width + "px";
        paperClip.style.height = this.height + "px";
        paperClip.style.position = "absolute";
        paperClip.style.left = this.posX + "px";
        paperClip.style.top = this.posZ + "px";
        paperClip.style.zIndex = this.posY;
        paperClip.style.border = "dotted";
        paperClip.style.backgroundColor = "#C0C0C0";
        paperClip.id = this.id;
        paperClip.innerHTML = this.insideText;
    };
    this.setMeInMCEAndShowIt = function () {
        setContentInMCE(this.insideText, this.id);
        showMCE();
    };
    this.setInsideText = function (text) {
        this.insideText = text;
        this.updateView();
    };

    //Text Edition Packets
    this.sendTextEditionPacket = function () {
        ajaxAPI(new _haitoRequest("",
            "POST",
            JSON.stringify(this.getEditionPacket()),
            function () {
                alert("Przesłano zmiany");
            },
            function () {
                alert("Coś poszło nie tak");
            }
        ))
    };
    this.getEditionPacket = function () {
        return {
            id: this.id,
            text: this.insideText
        }
    };

    //Movement Packets
    this.sendMovePacket = function () {
        ajaxAPI(new _haitoRequest("",
            "POST",
            JSON.stringify(this.getMovePacket()),
            function () {

            },
            function () {

            }
        ))
    };
    this.getMovePacket = function () {
        return {
            id: this.id,
            pozX: this.posX,
            posY: this.posY,
            posZ: this.posZ
        }
    };

    //Resize packet
    this.sendResizePacket = function () {
        ajaxAPI(new _haitoRequest("",
            "POST",
            JSON.stringify(this.getResizePacket()),
            function () {
                alert("Przesłano zmiany");
            },
            function () {
                alert("Coś poszło nie tak");
            }
        ))
    };
    this.getResizePacket = function () {
        return {
            width: this.width,
            height: this.height
        }
    };

    this.createAndInit();
}

function synqWithServer(data) {

    for (var i = 0; i < data.length; i++) {
        appendNewClip(data[i])
    }

    function appendNewClip(descriptiveObject) {
        new _paperClips(descriptiveObject.id,
            descriptiveObject.text,
            descriptiveObject.width,
            descriptiveObject.height,
            descriptiveObject.posX,
            descriptiveObject.posY,
            descriptiveObject.posZ
        );
    }
}

function generateEditButton(id) {
    var editButton = document.createElement("button");
    editButton.innerHTML = "Edytuj";
    editButton.setAttribute("cId", id);
    editButton.addEventListener("click", function (event) {
        var thing = event.target;
        console.log(thing.getAttribute("cId"));
        console.log(findPaperClipById(thing.getAttribute("cId")));
        findPaperClipById(thing.getAttribute("cId")).setMeInMCEAndShowIt();


    }, false);

    editButton.style.position = "absolute";
    editButton.style.top = "0px";
    editButton.style.left = "0px";

    return editButton;
}

function findPaperClipById(id){
    for(var i = 0; i < elemsList.length; i++)
        if(elemsList[i].id == id)
            return elemsList[i]
}