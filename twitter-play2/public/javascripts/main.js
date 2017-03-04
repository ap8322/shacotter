$(function () {
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

    $('#image-link').balloon({
        css: {
            fontSize: '15px'
        },
        position: 'right'
    });

    $('.follow-button').on('click', function () {
        var $follow_button = $(this);
        var $member_id = $follow_button.parent().attr('id');

        if (!$follow_button.hasClass('btn-primary')) {
            $.ajax({
                url: '/follow',
                type: 'POST',
                data: '{"id": ' + $member_id + '}',
                contentType: 'application/json',
                dataType: 'json'
            }).done(function () {
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
            }).done(function () {
                if (confirm("フォローを解除しますか?")) {
                    $follow_button.removeClass('btn-primary');
                    $follow_button.text('フォロー');
                }
            });
        }
    });

    var good_status = "GOOD";
    var bad_status = "BAD";
    var no_evaluate = "NO_EVALUATE";

    // 評価ボタン
    $('ul').on('click', 'button', function () {
        var $button = $(this)
        var $tweet_box = $button.parent();
        var $good = $tweet_box.children('.jsc-good');
        var $good_count = $tweet_box.children('.good-count');
        var $bad = $tweet_box.children('.jsc-bad');
        var $bad_count = $tweet_box.children('.bad-count');
        var $tweet_id = $tweet_box.parent().attr('id');
        var status = pushedButton($button);

        //Non -> good or bad (どちらのボタンも押されていない)
        if (!$good.hasClass('btn-success') && !$bad.hasClass('btn-danger')) {
            $.ajax({
                url: '/evaluate/add ',
                type: 'POST',
                data: '{"tweet_id":' + $tweet_id + ', "eval_status":"' + status + '"}',
                contentType: 'application/json',
                dataType: 'json'
            }).done(function () {
                if (status == good_status) {
                    addGoodCount($good, $good_count)
                } else {
                    addBadCount($bad, $bad_count)
                }
            });
            //bad -> good good -> bad (既に押されているボタンと押したボタンが別)
        } else if (($good.hasClass('btn-success') && status == bad_status) || ($bad.hasClass('btn-danger') && status == good_status)) {
            $.ajax({
                url: '/evaluate/update',
                type: 'PUT',
                data: '{"tweet_id":' + $tweet_id + ', "eval_status":"' + status + '"}',
                contentType: 'application/json',
                dataType: 'json'
            }).done(function () {
                if (status == good_status) {
                    addGoodCount($good, $good_count);
                    removeBadCount($bad, $bad_count);
                } else {
                    addBadCount($bad, $bad_count);
                    removeGoodCount($good, $good_count);
                }
            });
            //good or bad -> Non
        } else {
            $.ajax({
                url: '/evaluate/delete',
                type: 'DELETE',
                data: '{"tweet_id":' + $tweet_id + ', "eval_status":"' + no_evaluate + '"}',
                contentType: 'application/json',
                dataType: 'json'
            }).done(function () {
                if ($good.hasClass('btn-success')) {
                    removeGoodCount($good, $good_count);
                } else {
                    removeBadCount($bad, $bad_count);
                }
            });
        }
    });

    //どちらのボタンが押されたか判定
    function pushedButton(data) {
        if (data.text() == "いいね") {
            return good_status;
        } else if (data.text() == "どうでもいいね") {
            return bad_status;
        }
    }

    function addGoodCount(good, good_count) {
        good.addClass('btn-success');
        good_count.text(parseInt(good_count.text()) + 1);
    }

    function removeGoodCount(good, good_count) {
        good.removeClass('btn-success');
        good_count.text(parseInt(good_count.text()) - 1);
    }


    function addBadCount(bad, bad_count) {
        bad.addClass('btn-danger');
        bad_count.text(parseInt(bad_count.text()) + 1);
    }

    function removeBadCount(bad, bad_count) {
        bad.removeClass('btn-danger');
        bad_count.text(parseInt(bad_count.text()) - 1);
    }
});
