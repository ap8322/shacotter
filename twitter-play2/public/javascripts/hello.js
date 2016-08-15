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

$(function () {
    var func = jsRoutes.controllers.TweetController.ajaxCall().ajax({
        success: function (data) {
            alert(data);
            $('#wrap').html("<button id='button2' class='btn btn-default'>click</button>");
        }
    })

    $('#button').on('click', function () {
        console.log("click");
        func()
    })
});

