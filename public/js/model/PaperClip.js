function _paperClips() {
    elemsList.push(this);

    this.width = 200;
    this.height = 200;

    this.posX = 100;
    this.posY = 1;
    this.posZ = 100;

    this.insideText = "<p>Nowa kartka</p>";

    this.createAndInit = function () {
        var thing = this;
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
                    thing.id = JSON.parse(res).id

                    var paperClip = document.createElement("div");
                    paperClip.style.width = thing.width + "px";
                    paperClip.style.height = thing.height + "px";
                    paperClip.style.position = "absolute";
                    paperClip.style.left = thing.posX + "px";
                    paperClip.style.top = thing.posZ + "px";
                    paperClip.style.zIndex = thing.posY;
                    paperClip.style.border = "dotted";
                    paperClip.style.backgroundColor = "#C0C0C0";
                    paperClip.id = thing.id;
                    paperClip.innerHTML = thing.insideText;

                    console.log(paperClip);
                    document.body.appendChild(paperClip);
                },
                function () {
                    alert("Się nie udało się, karteczka nie zostanie zapisana");
                    elemsList.pop()
                }
            )

        );

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

    //Get all the stuff and things


    this.createAndInit();
}

function synqWithServer() {
    ajaxAPI(new _haitoRequest("",
        "POST",
        JSON.stringify(this.getResizePacket()),
        function () {
            //TODO:Recreate objects
        },
        function () {
            alert("Coś poszło nie tak");
        }
    ));

    function parse(string) {
        return JSON.parse(string);
    }

    function appendNewClip(descriptiveObject) {
        var object = new _paperClips();

        object.height = descriptiveObject.height;
        object.width = descriptiveObject.width;
        object.insideText = descriptiveObject.insideText;

        object.posX = descriptiveObject.posX;
        object.posY = descriptiveObject.posY;
        object.posZ = descriptiveObject.posZ;
        object.updateView();
    }
}