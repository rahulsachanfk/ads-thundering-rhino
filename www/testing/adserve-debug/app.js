$(document).ready(function() {
    $("#facet").tooltip({
        content:"<p>Facets can be copied from the URL of the search results page on the website.</p>" +
        "<p>An example facet query looks like</p>" +
        "<p>facets.price_range%255B%255D%3DRs.%2B5001%2B-%2BRs.%2B10000&p%5B%5D=facets.brand%255B%255D%3DSamsung</p>",
        items:"#facet"
    });
    $("#slot").tooltip({
        content:"Comma separated values of integers.",
        items: "#slot"
    });

    $("#query").autocomplete ({
        source: "/services/servingdebug/getStores",
        delay: 500,
        minLength: 3,
        focus: function(e, ui) {
            e.preventDefault();
        },
        select: function(e, ui) {
            e.preventDefault();
            $("#store").val(ui.item.value);
            $("#query").val(ui.item.queryVal);
        },
        change: function(e, ui) {
            if(!(ui.item)) {
                $("#store").val("search.flipkart.com");
            }
        }
    });
    $("#submit").click(function(event){
        if(!$("#store").val() || !$("#slot").val() || !$("#query").val() || !$("#listing").val()) {
            alert("Store path, slot, query, listing values cannot be empty");
        } else if (!(/^\d+(,\d+)*$/.test($("#slot").val()))) {
            alert("Slots must be comma separated values of integers");
        } else {
            event.preventDefault()
            $("#display").empty();
            var store = $("#store").val();
            var slot = $("#slot").val();
            var query = $("#query").val();
            var facet = $("#facet").val();
            var listing = $("#listing").val();
            $('#display').load('/services/servingdebug', {
                "store": store,
                "slot": slot,
                "query": query,
                "facet": facet,
                "listing": listing
            });
        }
    });
})