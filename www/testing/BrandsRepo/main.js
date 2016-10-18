$(function() {
    $("form input").keypress(function (e) {
        if ((e.which && e.which == 13) || (e.keyCode && e.keyCode == 13)) {
            $('button[type=submit] .default').click();
            return false;
        } else {
            return true;
        }
    });
});

$(document).ready(function(){
    $("#submitButton").click(function() {

        var cmpid = document.getElementById('camp').value;
        var sdate = document.getElementById('sd').value;
        var edate = document.getElementById('ed').value;

        if( sdate == '' || edate == '') {
            var url = "http://10.32.237.248:25916/brandsrepo/viewactions/" + cmpid ;
            var win = window.open(url, '_blank');
            if (win) {
                win.focus();
            } else {
                alert("Please allow popups for this website");
            }

        } else {
            var url = "http://10.32.237.248:25916/brandsrepo/viewactions/" + cmpid + "/" + sdate + "/" + edate;
            var win = window.open(url, '_blank');
            if (win) {
                win.focus();
            } else {
                alert("Please allow popups for this website");
            }
        }

    });
});

