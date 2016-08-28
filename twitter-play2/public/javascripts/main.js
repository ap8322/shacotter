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

    $('.follow-button').on('click',function () {
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
                if(confirm("フォローを解除しますか?")){
                    $follow_button.removeClass('btn-primary');
                    $follow_button.text('フォロー');
                }
            });
        }
    });

    // 評価ボタン
    $('ul').on('click', 'button', function () {
        var $tweet_box  =  $(this).parent();
        var $good       =  $tweet_box.children('.jsc-good');
        var $good_count =  $tweet_box.children('.good-count');
        var $bad        =  $tweet_box.children('.jsc-bad');
        var $bad_count  =  $tweet_box.children('.bad-count');
        var $tweet_id   =  $tweet_box.parent().attr('id');
        var status = pushedButton($(this));

        //Non -> good or bad (どちらのボタンも押されていない)
        if(!$good.hasClass('btn-success') && !$bad.hasClass('btn-danger')) {
            $.ajax({
                url: '/evaluete/add ',
                type: 'POST',
                data: '{"tweet_id":' + $tweet_id + ', "eval_status":' + status + '}',
                contentType: 'application/json',
                dataType: 'json'
            }).done(function () {
                if (status == 1) {
                    addGoodCount($good,$good_count)
                } else {
                    addBadCount($bad,$bad_count)
                }
            });
            //bad -> good good -> bad (既に押されているボタンと押したボタンが別)
        } else if(($good.hasClass('btn-success') && status == 0) || ($bad.hasClass('btn-danger') && status == 1)) {
            $.ajax({
                url: '/evaluete/update',
                type: 'PUT',
                data: '{"tweet_id":' + $tweet_id + ', "eval_status":' + status + '}',
                contentType: 'application/json',
                dataType: 'json'
            }).done(function () {
                if (status == 1) {
                    addGoodCount($good,$good_count);
                    removeBadCount($bad,$bad_count);
                } else {
                    addBadCount($bad,$bad_count);
                    removeGoodCount($good,$good_count);
                }
            });
            //good or bad -> Non
        } else {
            $.ajax({
                url: '/evaluete/delete',
                type: 'DELETE',
                data: '{"tweet_id":' + $tweet_id + ', "eval_status":-1}',
                contentType: 'application/json',
                dataType: 'json'
            }).done(function () {
                if ($good.hasClass('btn-success')) {
                    removeGoodCount($good,$good_count);
                } else {
                    removeBadCount($bad,$bad_count);
                }
            });
        }
    });

    function pushedButton (data) {
        if (data.text() == "いいね") {
            return 1;
        } else if (data.text() == "どうでもいいね") {
            return 0;
        }
    }

    function addGoodCount(good,good_count){
        good.addClass('btn-success');
        good_count.text(parseInt(good_count.text()) + 1);
    }

    function removeGoodCount(good,good_count){
        good.removeClass('btn-success');
        good_count.text(parseInt(good_count.text()) - 1);
    }


    function addBadCount(bad,bad_count){
        bad.addClass('btn-danger');
        bad_count.text(parseInt(bad_count.text()) + 1);
    }

    function removeBadCount(bad,bad_count){
        bad.removeClass('btn-danger');
        bad_count.text(parseInt(bad_count.text()) - 1);
    }
});
