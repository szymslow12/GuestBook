"use strict"

function connectWithXMLGuestbook() {
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function () {
        if (this.readyState == 4 && this.status == 200) {
         /*use this to send xml*/
            parseXMLToHTMLGuest(this.responseText);
            }
        };
    xhttp.open("GET", "/guests", true);
    xhttp.send();
    }


function parseXMLToHTMLGuest(responseText) {
    var json = JSON.parse(responseText);
    var content = document.getElementById("guestsbook-messages");
    console.log(json);
    for (var i = 0; i < json.length; i++) {
        content.appendChild(getGuest(json[i]));
    }
    }


function getGuest(json) {
    var guest = document.createElement("div");
    guest.setAttribute("class", "guest");

    var h1 = document.createElement("h1");
    var p1 = document.createElement("p1");
    var name = "Name: " + json.name;
    var date = " Date: " + json.date;
    var message = json.message;
    h1.appendChild(document.createTextNode(name + date));
    p1.appendChild(document.createTextNode(message));
    guest.appendChild(h1);
    guest.appendChild(p1);
    return guest;
    }

    function stickTo(id) {
        let newMessageBox = document.getElementById(id);
        newMessageBox.style.top = window.scrollY +'px'; 
    }

    function clearInputBoxes() {
        let nameBox = document.getElementById("name");
        let messageBox = document.getElementById("message");
        nameBox.value = "";
        messageBox.value = "";               
    }
    window.onload = clearInputBoxes;
    /*window.setInterval(() => {
        let container = document.getElementById("guestsbook-messages");
        container.style.width = container.offsetWidth + "px";

        while (container.firstElementChild) {
            container.removeChild(container.firstElementChild);
        }

        connectWithXMLGuestbook();
    }, 5000);*/

    function updateGuests() {
        /*let exec = new XMLHttpRequest();
        exec.onreadystatechange = () => {
            
        };
        exec.open("GET", "/static/guestbook.xml", true);
        exec.send();*/
        let container = document.getElementById("guestsbook-messages");
        container.style.width = container.offsetWidth + "px";

        while (container.firstElementChild) {
            container.removeChild(container.firstElementChild);
        }

        connectWithXMLGuestbook();
    }

    