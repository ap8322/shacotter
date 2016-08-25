$('form').validate({
    rules: {
        tweet: {
            required: true,
            maxlength: 140
        }
    },
    messages: {
        tweet: {
            required: "空ツイートはやめてね",
            maxlength: "140文字以上はツイートはできません｡"
        }
    },
    errorElement: 'p'
});

$('.follow-button').click(function () {
    var $follow_button = $(this);
    var $parent = $(this).parent();
    var $member_id = $parent.attr('id');

    if (!$follow_button.hasClass('btn-primary')) {
        $.ajax({
            url: '/follow',
            type: 'POST',
            data: '{"id": ' + $member_id + '}',
            contentType: 'application/json',
            dataType: 'json'
        }).done(function (data) {
            $follow_button.addClass('btn-primary');
            $follow_button.text('フォロー中');
        });
    } else {
        $.ajax({
            url: '/remove',
            type: 'POST',
            data: '{"id": ' + $member_id + '}',
            contentType: 'application/json',
            dataType: 'json'
        }).done(function (data) {
            confirm("フォローを解除しますか?");
            $follow_button.removeClass('btn-primary');
            $follow_button.text('フォロー');
        });
    }
});

// 評価ボタン
$('.jsc-good').click(function () {
    var $good = $(this);
    var $parent = $(this).parent();
    var $count = $parent.children('.good-count');
    var $tweet_id = $parent.parent().attr('id');
    var $bad = $parent.children('.jsc-bad');

    //Non -> good
    if (!$good.hasClass('btn-success')) {
        if (!$bad.hasClass('btn-danger')) {
            $.ajax({
                url: '/addEval',
                type: 'POST',
                data: '{"tweet_id":' + $tweet_id + ', "eval_status":1}',
                contentType: 'application/json',
                dataType: 'json'
            }).done(function () {
                $good.addClass('btn-success');
                $count.text(parseInt($count.text()) + 1);
            });

            //bad -> good
        } else {
            $.ajax({
                url: '/updateEval',
                type: 'PUT',
                data: '{"tweet_id":' + $tweet_id + ', "eval_status":1}',
                contentType: 'application/json',
                dataType: 'json'
            }).done(function () {
                $good.addClass('btn-success');
                $count.text(parseInt($count.text()) + 1);
                $bad.removeClass('btn-danger');
                $bad.next().text(parseInt($bad.next().text()) - 1)
            })
        }

        //good -> Non
    } else {
        $.ajax({
            url: '/deleteEval',
            type: 'DELETE',
            data: '{"tweet_id":' + $tweet_id + ', "eval_status":10}',
            contentType: 'application/json',
            dataType: 'json'
        }).done(function () {
            $good.removeClass('btn-success');
            $count.text(parseInt($count.text()) - 1);
        })
    }
});

$('.jsc-bad').click(function () {
    var $bad = $(this);
    var $parent = $bad.parent();
    var $count = $parent.children('.bad-count');
    var $good = $parent.children('.jsc-good');
    var $tweet_id = $parent.parent().attr('id');

    //non -> bad
    if (!$bad.hasClass('btn-danger')) {
        if (!$good.hasClass('btn-success')) {
            $.ajax({
                url: '/addEval',
                type: 'POST',
                data: '{"tweet_id":' + $tweet_id + ', "eval_status":0}',
                contentType: 'application/json',
                dataType: 'json'
            }).done(function () {
                $bad.addClass('btn-danger');
                $count.text(parseInt($count.text()) + 1);
            });

            //good -> bad
        } else {
            $.ajax({
                url: '/updateEval',
                type: 'PUT',
                data: '{"tweet_id":' + $tweet_id + ', "eval_status":0}',
                contentType: 'application/json',
                dataType: 'json'
            }).done(function () {
                $bad.addClass('btn-danger');
                $count.text(parseInt($count.text()) + 1);
                $good.removeClass('btn-success');
                $good.next().text(parseInt($good.next().text()) - 1)
            });
        }
        //bad -> none
    } else {
        $.ajax({
            url: '/deleteEval',
            type: 'DELETE',
            data: '{"tweet_id":' + $tweet_id + ', "eval_status":10}',
            contentType: 'application/json',
            dataType: 'json'
        }).done(function () {
            $bad.toggleClass('btn-danger');
            $count.text(parseInt($count.text()) - 1);
        })
    }
});