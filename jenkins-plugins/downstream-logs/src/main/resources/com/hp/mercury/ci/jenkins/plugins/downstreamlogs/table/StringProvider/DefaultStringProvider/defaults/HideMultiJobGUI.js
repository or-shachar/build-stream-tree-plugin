var multijobTable = document.getElementById("projectstatus");

if (multijobTable != null) {

    var showButton = document.createElement("input");
    showButton.type = "button";
    showButton.value = "Toggle MultiJob GUI";
    showButton.onclick = function () {
        multijobTable.style.display = multijobTable.style.display=="none" ? "" : "none";
    }

    var parent = multijobTable.parentNode;
    parent.insertBefore(showButton, multijobTable);

    multijobTable.style.display = "none";
}


