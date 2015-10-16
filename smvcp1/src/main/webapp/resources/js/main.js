$(document).ready(function() {

});

$(function() {
	$("#about_dialog").dialog({
		autoOpen : false,
		modal : true,
		width : 650,
		height : 350,
		resize : true,
		show : {
			effect : "blind",
			delay : 100,
			duration : 1000
		},
		hide : {
			/* fade */
			effect : "explode",
			duration : 1000
		}
	});

	$("#about_opener").click(function() {
		$("#about_dialog").dialog("open");
	});
});

$(function() {
	$("#help_dialog").dialog({
		autoOpen : false,
		modal : true,
		width : 650,
		height : 350,
		resize : true,
		show : {
			effect : "blind",
			delay : 100,
			duration : 1000
		},
		hide : {
			/* fade */
			effect : "explode",
			duration : 1000
		}
	});

	$("#help_opener").click(function() {
		$("#help_dialog").dialog("open");
	});
});

$(function() {
	$("#unsubscribe").click(function(e) {
		e.preventDefault();
		var targetUrl = $(this).attr("href");

		$("#confirm-dialog").dialog( {
			buttons : {
				"Confirm" : function() {
					window.location.href = targetUrl;
				},
				"Cancel" : function() {
					$(this).dialog("close");
				}
			}
		});

		$("#confirm-dialog").dialog("open");
	});
});