
fixBuildStreamTreeTableWidth = function (done){
    return function() {
        //fix table width after initial rendering
        if (!done) {

            done = true;

            var widthProperty = "clientWidth";

            var buildStreamTreeTable = document.getElementById("build-stream-tree-table");

            var headers = buildStreamTreeTable.getElementsByTagName("th");
            var headersLength = headers.length;
            for (var i = 0 ; i < headersLength ; i++) {
                var header = headers[i];
                header.style.width = header[widthProperty] + "px";
            }

            buildStreamTreeTable.style.width = buildStreamTreeTable[widthProperty] + "px";
            buildStreamTreeTable.style.tableLayout = "fixed";
        }
    }
}(false);

//plusMinusDiv xpath is //*[@id="build-stream-tree-table"]/tbody/tr[4]/td[1]/div
function expandCollapse(plusMinusDiv) {

     var newImage = "plus.gif";
     var newVisibility = "none";

     var newExpanded = "false";

     if (plusMinusDiv.getAttribute("expanded") === "false") {
          newImage = "minus.gif";
          newVisibility = "";
          newExpanded = "true";
     }

     doExpandCollapse(plusMinusDiv,newImage,newVisibility,newExpanded,true)
}

function expand(plusMinusDiv, unselect) {

    newImage = "minus.gif";
    newVisibility = "";
    newExpanded = "true";
    doExpandCollapse(plusMinusDiv, newImage, newVisibility, newExpanded, unselect)
}

function collapse(plusMinusDiv, unselect) {

    var newImage = "plus.gif";
    var newVisibility = "none";
    var newExpanded = "false";
    doExpandCollapse(plusMinusDiv, newImage, newVisibility, newExpanded, unselect)
}

function doExpandCollapse(plusMinusDiv,newImage,newVisibility,newExpanded, unselect) {

    //make width not change when we hide rows
    fixBuildStreamTreeTableWidth();

    //function from abstract pipeline additional top layout
    if (unselect && (typeof unselectAbstractPipeline == 'function')) {
        unselectAbstractPipeline();
    }

    plusMinusDiv.setAttribute("expanded", newExpanded);

    var img = plusMinusDiv.getElementsByTagName("img")[0];
    img.src = img.src.substring(0,img.src.lastIndexOf('/') + 1) + newImage;

    var prefix = plusMinusDiv.parentNode.getAttribute("prefix");
    var locationIndex = plusMinusDiv.parentNode.cellIndex;
    var tr = plusMinusDiv.parentNode.parentNode;

    var rows = tr.parentNode.rows;

    for (var i = tr.rowIndex + 1 ; i < rows.length ; i++) {

        var row = rows[i];
        var td = row.cells[locationIndex];
        var location = td.getAttribute("prefix");

        if (!prefix) {
            continue;
        }

        if (location.indexOf(prefix) == 0) {

          var divs = td.getElementsByTagName("div");
          var div = divs[0];

          if (div) {
              div.setAttribute("expanded", newExpanded);
              div.getElementsByTagName("img")[0].src = img.src;
          }

          row.style.display = newVisibility;
        }

        else {
           break;
        }
    }
}




