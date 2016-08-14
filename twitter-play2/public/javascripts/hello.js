// $(function() {
//     $('#button').on('click', function () {
//         alert("クリックされました");
//         $('#button').html("<button id='button2' class='btn btn-default'>click</button>");
//     });
//
//     $('#button2').click(function () {
//         alert("クリックされました");
//         $('#button2').html("<button id='button' class='btn btn-primary'>クリック</button>");
//     });
// });

$(function() {
    $('#button').click(ajaxCall());
});

var ajaxCall = function() {
    var ajaxCallBack = {
        success : onSuccess,
        error : onError
    }

    jsRoutes.controllers.TweetController.ajaxCall().ajax(ajaxCallBack);
};

var  onSuccess = function(data) {
    alert(data);
    $('#wrap').html("<button id='button2' class='btn btn-default'>click</button>");
}

var onError = function(error) {
    alert(error);
}