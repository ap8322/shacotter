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

// var func = jsRoutes.controllers.TweetController.ajaxCall().ajax({
//     success: function (data) {
//         alert(data);
//         $('#wrap').html("<button id='button2' class='btn btn-default'>click</button>");
//     }
// })

// $(function () {
//     $('#test').on('click', function () {
//         $.ajax({
//             url: '/ajax-call',
//         }).done(function (data) {
//             console.log(data);
//             $('#wrap').html("<button id='button2' class='btn btn-default'>click</button>");
//         })
//     })
//
//     $('#button2').on('click', function () {
//         $.ajax({
//             url: '/ajax-cal',
//         }).done(function (data) {
//             console.log(data)
//             $('#wrap').html("<button id='test' class='btn btn-primary'>click</button>");
//         })
//     })
// });

function follow(id) {
    $.ajax({
        url: '/follow',
        type: 'POST',
        data: '{"id": ' + id + '}',
        contentType: 'application/json',
        dataType: 'json',
    }).done(function (data) {
        console.log(data);
        $('#member' + id).html("<button onclick='unfollow(" + id + ")'>フォロー中</button>");
    })
}

function unfollow(id) {
    $.ajax({
        url: '/unfollow',
        type: 'POST',
        data: '{"id": ' + id + '}',
        contentType: 'application/json',
        dataType: 'json',
    }).done(function (data) {
        console.log(data);
        $('#member' + id).html("<button onclick='follow(" + id + ")'>フォロー</button>");
    })
}
