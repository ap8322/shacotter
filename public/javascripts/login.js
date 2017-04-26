/**
 * Created by yuki.haneda on 2016/08/19.
 */
$(function () {

    // validate
    $('form').validate({
        errorElement: 'p'
    });

    //rotate title logo
    var angle = 0;
    var $title_logo = $('#eval');

    setInterval(function () {
        angle+=1;
        $title_logo.rotate(angle)
    },50);
});