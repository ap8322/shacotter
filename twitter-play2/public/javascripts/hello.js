function follow(id) {
    $.ajax({
        url: '/follow',
        type: 'POST',
        data: '{"id": ' + id + '}',
        contentType: 'application/json',
        dataType: 'json'
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

$(function () {
    $('.good').click(function () {
        var button = $(this);
        var count = $(this).next();
        var tweet_id = count.next().next().next().text();
        var next = button.next().next();

        //Non -> good
        if (button.val() == 0) {
            if (next.val() == 0) {
                $.ajax({
                    url: '/good',
                    type: 'POST',
                    data: '{"tweet_id":' + tweet_id + ', "eval_status":1}',
                    contentType: 'application/json',
                    dataType: 'json'
                }).done(function () {
                    button.text("いいねしました");
                    button.val('1');
                    count.text(parseInt(count.text()) + 1);
                });

                //bad -> good
            } else {
                $.ajax({
                    url: '/updateStatus',
                    type: 'PUT',
                    data: '{"tweet_id":' + tweet_id + ', "eval_status":1}',
                    contentType: 'application/json',
                    dataType: 'json'
                }).done(function () {
                    button.text("いいねしました");
                    button.val('1');
                    count.text(parseInt(count.text()) + 1);
                    next.text("どうでもいいね");
                    next.val(0);
                    next.next().text(parseInt(next.next().text()) - 1)
                })
            }

            //good -> Non
        } else {
            $.ajax({
                url: '/deleteEval',
                type: 'DELETE',
                data: '{"tweet_id":' + tweet_id + ', "eval_status":10}',
                contentType: 'application/json',
                dataType: 'json'
            }).done(function () {
                button.text("いいね");
                button.val(0);
                count.text(parseInt(count.text()) - 1);
            })
        }
    });

    $('.bad').click(function () {
        var button = $(this);
        var count = $(this).next();
        var prev = $(this).prev().prev();
        var tweet_id = count.next().text();

        //non -> bad
        if (button.val() == 0) {
            if (prev.val() == 0) {
                $.ajax({
                    url: '/bad',
                    type: 'POST',
                    data: '{"tweet_id":' + tweet_id + ', "eval_status":0}',
                    contentType: 'application/json',
                    dataType: 'json'
                }).done(function () {
                    button.text("どうでもいいねしました");
                    button.val(1);
                    count.text(parseInt(count.text()) + 1);
                });

                //good -> bad
            } else {
                $.ajax({
                    url: '/updateStatus',
                    type: 'PUT',
                    data: '{"tweet_id":' + tweet_id + ', "eval_status":0}',
                    contentType: 'application/json',
                    dataType: 'json'
                }).done(function () {
                    button.text("どうでもいいねしました");
                    button.val(1);
                    count.text(parseInt(count.text()) + 1);
                    prev.text("いいね");
                    prev.val(0);
                    prev.next().text(parseInt(prev.next().text()) - 1)
                });
            }
            //bad -> none
        } else {
            $.ajax({
                url: '/deleteEval',
                type: 'DELETE',
                data: '{"tweet_id":' + tweet_id + ', "eval_status":10}',
                contentType: 'application/json',
                dataType: 'json'
            }).done(function () {
                button.text("どうでもいいね");
                button.val(0);
                count.text(parseInt(count.text()) - 1);
            })
        }
    })
});