function follow(id) {
    $.ajax({
        url: '/follow',
        type: 'POST',
        data: '{"id": ' + id + '}',
        contentType: 'application/json',
        dataType: 'json',
    }).done(function (data) {
        console.log(data);
        $('#member' + id).html("<button onclick='remove(" + id + ")'>フォロー中</button>");
    })
}

function remove(id) {
    $.ajax({
        url: '/remove',
        type: 'POST',
        data: '{"id": ' + id + '}',
        contentType: 'application/json',
        dataType: 'json',
    }).done(function (data) {
        console.log(data);
        confirm("フォローを解除しますか?")
        $('#member' + id).html("<button onclick='follow(" + id + ")'>フォロー</button>");
    })
}
