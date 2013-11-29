/**
 *
 * THIS BIT OF CODE HANDLES COOKIES
 * from: https://developer.mozilla.org/en-US/docs/Web/API/document.cookie
 *
 */

/*\
 |*|
 |*|  :: cookies.js ::
 |*|
 |*|  A complete cookies reader/writer framework with full unicode support.
 |*|
 |*|  https://developer.mozilla.org/en-US/docs/DOM/document.cookie
 |*|
 |*|  This framework is released under the GNU Public License, version 3 or later.
 |*|  http://www.gnu.org/licenses/gpl-3.0-standalone.html
 |*|
 |*|  Syntaxes:
 |*|
 |*|  * docCookies.setItem(name, value[, end[, path[, domain[, secure]]]])
 |*|  * docCookies.getItem(name)
 |*|  * docCookies.removeItem(name[, path], domain)
 |*|  * docCookies.hasItem(name)
 |*|  * docCookies.keys()
 |*|
 \*/
var cookiePrefix = 'buildStreamTree-State-Collapsed-';
var docCookies = {
    getItem: function (sKey) {
        return decodeURIComponent(document.cookie.replace(new RegExp("(?:(?:^|.*;)\\s*" + encodeURIComponent(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=\\s*([^;]*).*$)|^.*$"), "$1")) || null;
    },
    setItem: function (sKey, sValue, vEnd, sPath, sDomain, bSecure) {
        if (!sKey || /^(?:expires|max\-age|path|domain|secure)$/i.test(sKey)) { return false; }
        var sExpires = "";
        if (vEnd) {
            switch (vEnd.constructor) {
                case Number:
                    sExpires = vEnd === Infinity ? "; expires=Fri, 31 Dec 9999 23:59:59 GMT" : "; max-age=" + vEnd;
                    break;
                case String:
                    sExpires = "; expires=" + vEnd;
                    break;
                case Date:
                    sExpires = "; expires=" + vEnd.toUTCString();
                    break;
            }
        }
        document.cookie = encodeURIComponent(sKey) + "=" + encodeURIComponent(sValue) + sExpires + (sDomain ? "; domain=" + sDomain : "") + (sPath ? "; path=" + sPath : "") + (bSecure ? "; secure" : "");
        return true;
    },
    removeItem: function (sKey, sPath, sDomain) {
        if (!sKey || !this.hasItem(sKey)) { return false; }
        document.cookie = encodeURIComponent(sKey) + "=; expires=Thu, 01 Jan 1970 00:00:00 GMT" + ( sDomain ? "; domain=" + sDomain : "") + ( sPath ? "; path=" + sPath : "");
        return true;
    },
    hasItem: function (sKey) {
        return (new RegExp("(?:^|;\\s*)" + encodeURIComponent(sKey).replace(/[\-\.\+\*]/g, "\\$&") + "\\s*\\=")).test(document.cookie);
    },
    keys: /* optional method: you can safely remove it! */ function () {
        var aKeys = document.cookie.replace(/((?:^|\s*;)[^\=]+)(?=;|$)|^\s*|\s*(?:\=[^;]*)?(?:\1|$)/g, "").split(/\s*(?:\=[^;]*)?;\s*/);
        for (var nIdx = 0; nIdx < aKeys.length; nIdx++) { aKeys[nIdx] = decodeURIComponent(aKeys[nIdx]); }
        return aKeys;
    }
};


/**
 * try to make the build-stream-tree table not resize when you click on the +/- buttons
 */
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

    var jobName = plusMinusDiv.parentNode.getAttribute("jobName");
    var cookieCollapseState = cookiePrefix + jobName;
    if (newExpanded === "true") {
        docCookies.removeItem(cookieCollapseState);
    }
    else {
        docCookies.setItem(cookieCollapseState, "true");
    }

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

//this javascript is loaded after the table is loaded, so all the elements are ready and we can collapse/uncollapse according to what we stored in memory...
window.setTimeout(function () {

    var i;
    var cell;

    var buildStreamTreeTable = document.getElementById("build-stream-tree-table");
    var rows = buildStreamTreeTable.rows;
    var firstRow = rows[1];
    if (firstRow) {

        var indexOfCollapseColumn = -1;

        var firstRowCells = firstRow.cells;
        var firstRowCellsCount = firstRowCells.length;
        for (i = 0 ; i < firstRowCellsCount ; i++) {
            cell = firstRowCells[i];
            if (cell.getAttribute('prefix')) {
                indexOfCollapseColumn = i;
                break;
            }
        }

        if (indexOfCollapseColumn > -1) {

            var rowsLength = rows.length;
            for (var rowIdx = 1 ; rowIdx < rowsLength; rowIdx++) {

                var row = rows[rowIdx];
                var collapseTd = row.cells[indexOfCollapseColumn];
                var jobName = collapseTd.getAttribute('jobName');

                if (docCookies.hasItem(cookiePrefix + jobName)) {
                    var div = collapseTd.childNodes[0];
                    collapse(div);
                }
            }
        }
    }

},100);



